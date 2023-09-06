package com.kenspeckle.trails.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import androidx.security.crypto.EncryptedSharedPreferences;

import com.kenspeckle.trails.LoginFailedException;
import com.kenspeckle.trails.RetrofitClient;

import java.io.IOException;
import java.security.GeneralSecurityException;

import androidx.security.crypto.MasterKey;

public class LoginRepository {

	public static SharedPreferences loadEncryptedSharedPreferences(Context context) throws GeneralSecurityException, IOException {
		MasterKey masterKey = new MasterKey.Builder(context)
				.setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
				.build();

		return EncryptedSharedPreferences.create(
				context,
				"secret_shared_prefs",
				masterKey,
				EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
				EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
		);
	}

	public static boolean credentialsSaved(Context context) throws GeneralSecurityException, IOException {
		SharedPreferences sharedPreferences = loadEncryptedSharedPreferences(context);
		return sharedPreferences.contains("username") && sharedPreferences.contains("password");
	}

	public static void saveCredentials(Context context, String username, String password) throws GeneralSecurityException, IOException {
		SharedPreferences sharedPreferences = loadEncryptedSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("username", username);
		editor.putString("password", password);
		editor.apply();

		RetrofitClient.USERNAME_PASSWORD_PAIR = new Pair<>(username, password);
	}

	public static Pair<String, String> loadCredentials(Context context) throws GeneralSecurityException, IOException {
		SharedPreferences sharedPreferences = loadEncryptedSharedPreferences(context);
		String username = sharedPreferences.getString("username", null);
		String password = sharedPreferences.getString("password", null);

		if (username == null || password == null) {
			throw new LoginFailedException();
		}
		return new Pair<>(username, password);
	}

	public static void clearCredentials(Context context) throws GeneralSecurityException, IOException {
		SharedPreferences sharedPreferences = loadEncryptedSharedPreferences(context);
		sharedPreferences.edit().remove("username").remove("password").apply();
		RetrofitClient.USERNAME_PASSWORD_PAIR = null;
	}
}