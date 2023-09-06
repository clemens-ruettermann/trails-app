package com.kenspeckle.trails.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kenspeckle.trails.RetrofitClient;
import com.kenspeckle.trails.data.LoginRepository;
import com.kenspeckle.trails.ui.login.LoginActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SuperActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			if (LoginRepository.credentialsSaved(getApplicationContext())) {
				RetrofitClient.USERNAME_PASSWORD_PAIR = LoginRepository.loadCredentials(getApplicationContext());

				Intent intent = new Intent(SuperActivity.this, MainActivity.class);
				intent.setAction(Intent.ACTION_VIEW);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivityForResult(intent, 0);
				return;
			}
		} catch (GeneralSecurityException | IOException e) {
			throw new RuntimeException(e);
		}

		Intent intent = new Intent(this, LoginActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivityForResult(intent, 0);

		finish();
	}

}