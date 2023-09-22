package com.kenspeckle.trails;

import android.util.Pair;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// This whole class is just a mess, but I don't know how to improve the code
public class RetrofitClient {

	private static Retrofit retrofit = null;

	private static Api apiInstance = null;

	private static String jSessionId = null;

	public static boolean LOGIN_FAILED = false;

	public static Pair<String, String> USERNAME_PASSWORD_PAIR = null;

	public static Retrofit getClient() {
		if (retrofit == null) {
			OkHttpClient okHttpClient = new OkHttpClient()
					.newBuilder()
					.addInterceptor(new LoginInterceptor())
					.build();
			retrofit = new Retrofit.Builder()
					.baseUrl(Constants.TRAILS_BASE_URL)
					.client(okHttpClient)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}
		return retrofit;
	}


	// Maybe this is a bad practise (saving the Api instances), but I think it is neater the save it, then recreating it
	// every time
	public static Api getApi() {
		if (apiInstance == null) {
			apiInstance = getClient().create(Api.class);
		}
		return apiInstance;
	}

	private static class LoginInterceptor implements Interceptor {

		private void login() throws IOException, GeneralSecurityException {

			if (USERNAME_PASSWORD_PAIR == null) {
				LOGIN_FAILED = true;
				return;
			}

			RequestBody formBody = new MultipartBody.Builder("---------------------------410477594322669617344224093291")
					.addFormDataPart("username", USERNAME_PASSWORD_PAIR.first)
					.addFormDataPart("password", USERNAME_PASSWORD_PAIR.second)
					.setType(MediaType.parse("multipart/form-data"))
					.build();


			Request request = new Request.Builder()
					.url(Constants.TRAILS_BASE_URL + "login")
					.post(formBody)
					.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/116.0")
					.addHeader("Origin", "https://trails.kjs-aachen.de")
					.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/116.0")
					.addHeader("Accept", "application/json")
					.addHeader("Referer", "https://trails.kjs-aachen.de/kjs-aachen/jungjaegerkurs-2023-2024/events")
					.build();


			HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
			logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
			Call call = new OkHttpClient()
					.newBuilder()
					.addInterceptor(logging)
					.build()
					.newCall(request);
			try (Response response = call.execute()) {
				if (response.isSuccessful()) {
					Optional<String> jsessionIdHeader = response
							.headers("Set-Cookie")
							.stream()
							.filter(s -> s.contains("JSESSIONID"))
							.findFirst();
					if (jsessionIdHeader.isPresent()) {
						jSessionId = jsessionIdHeader.get();
						LOGIN_FAILED = false;
					} else {
						LOGIN_FAILED = true;
					}
					return;
				}
			}
			LOGIN_FAILED = true;
		}
		@NonNull
		@Override
		public Response intercept(@NonNull Chain chain) throws IOException {
			if (RetrofitClient.jSessionId == null) {
				try {
					login();
				} catch (GeneralSecurityException e) {
					LOGIN_FAILED = true;
				}
			}
			if (LOGIN_FAILED) {
				return chain.proceed(chain.request());
			}
			Request request = chain.request()
					.newBuilder()
					.addHeader("Cookie", jSessionId)
					.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/116.0")
					.removeHeader("User-Agent")
					.build();
			return chain.proceed(request);
		}
	}



	private static String bodyToString(final Request request){

		try {
			final Request copy = request.newBuilder().build();
			final Buffer buffer = new Buffer();
			copy.body().writeTo(buffer);
			return buffer.readUtf8();
		} catch (final IOException e) {
			return "did not work";
		}
	}

}
