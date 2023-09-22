package com.kenspeckle.trails.ui.events;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kenspeckle.trails.RetrofitClient;
import com.kenspeckle.trails.dtos.EventDto;

import java.util.UUID;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationUtils {

	public static Callback<Void> getChangeRegistrationCallback(@NonNull Context context, Function<EventDto, Void> setter, UUID uuid) {
		return new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
				if (uuid == null || !response.isSuccessful()) {
					Toast.makeText(context, "Fehler", Toast.LENGTH_LONG).show();
					return;
				}
				RetrofitClient.getApi().getEvent(uuid.toString()).enqueue(getRefreshEventCallback(setter));
				String msg;

				try (okhttp3.Response raw = response.raw()) {
					// see Api.java
					if ("post".equalsIgnoreCase(raw.request().method())) {
						msg = "Erfolgreich angemeldet";
					} else {
						msg = "Erfolgreich abgemeldet";
					}
					Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				} catch (IllegalStateException e) {
					e.printStackTrace();
					Log.e("EventAdapter", e.getLocalizedMessage());
				}
			}

			@Override
			public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
				Toast.makeText(context, "Fehler bei der An/Abmeldung", Toast.LENGTH_LONG).show();
			}
		};
	}

	public static Callback<EventDto> getRefreshEventCallback(Function<EventDto, Void> setter) {
		return new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<EventDto> call, @NonNull Response<EventDto> response) {
				if (response.code() == 200 && response.body() != null) {
					setter.apply(response.body());
				}
			}
			@Override public void onFailure(@NonNull Call<EventDto> call, @NonNull Throwable t) {}
		};
	}


	@NonNull
	public static View.OnClickListener getOnClickListener(@NonNull Context context, Function<EventDto, Void> setter, EventDto eventDto) {
		return v -> {
			if (eventDto.getLoggedInRegistration() != null) {

				RetrofitClient.getApi()
						.unRegisterForEvent(eventDto.getId().toString())
						.enqueue(RegistrationUtils.getChangeRegistrationCallback(context, setter, eventDto.getId()));
			} else {
				RetrofitClient
						.getApi()
						.registerForEvent(eventDto.getId().toString())
						.enqueue(RegistrationUtils.getChangeRegistrationCallback(context, setter, eventDto.getId()));
			}
		};
	}
}
