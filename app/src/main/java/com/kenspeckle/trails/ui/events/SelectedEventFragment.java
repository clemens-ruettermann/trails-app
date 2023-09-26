package com.kenspeckle.trails.ui.events;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kenspeckle.trails.R;
import com.kenspeckle.trails.databinding.FragmentSelectedEventBinding;
import com.kenspeckle.trails.dtos.EventDto;
import com.kenspeckle.trails.utils.DateUtils;

import java.io.Serializable;
import java.util.Objects;

public class SelectedEventFragment extends Fragment {
	private FragmentSelectedEventBinding binding;

	private EventDto eventDto;

	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Serializable eventDto = getArguments().getSerializable("eventDto");
		if (eventDto != null) {
			this.eventDto = (EventDto) eventDto;
		}
	}

	@SuppressLint("SetTextI18n")
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		binding = FragmentSelectedEventBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		setEventDto(eventDto);
		return root;
	}

	public Void setEventDto(EventDto dto) {
		binding.selectedEventTitle.setText(dto.getTitle());
		binding.selectedEventBody.setText(Html.fromHtml(dto.getDescription(), Html.FROM_HTML_MODE_COMPACT));
		binding.selectedEventAuthor.setText(dto.getAuthor().getName());
		String startTime = DateUtils.convertSeperateDateAndTimeString(dto.getStartDate(), dto.getStartTime());
		if (dto.getEndDate() != null) {
			String endTime = DateUtils.convertSeperateDateAndTimeString(dto.getEndDate(), dto.getEndTime());
			binding.selectedEventDate.setText(startTime + " - " + endTime);
		} else {
			binding.selectedEventDate.setText(startTime);
		}
		if (dto.isRegistrationEnabled()) {
			binding.selectedEventRegistrationCheckbox.setVisibility(View.VISIBLE);

			if (dto.getLoggedInRegistration() != null) {
				binding.selectedEventRegistrationCheckbox.setText(R.string.angemeldet);
				binding.selectedEventRegistrationCheckbox.setChecked(true);

			} else {
				binding.selectedEventRegistrationCheckbox.setChecked(false);
				binding.selectedEventRegistrationCheckbox.setText(R.string.nicht_angemeldet);
			}
		} else {
			binding.selectedEventRegistrationCheckbox.setVisibility(View.GONE);
			binding.selectedEventRegistrationCheckbox.setText("");
		}
		binding.selectedEventRegistrationCheckbox.setOnClickListener(RegistrationUtils.getOnClickListener(requireContext(), this::setEventDto, dto));

		return null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
