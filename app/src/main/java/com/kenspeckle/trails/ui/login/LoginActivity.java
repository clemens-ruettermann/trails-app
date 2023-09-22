package com.kenspeckle.trails.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.kenspeckle.trails.data.LoginRepository;
import com.kenspeckle.trails.databinding.ActivityLoginBinding;
import com.kenspeckle.trails.ui.MainActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LoginActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		final EditText usernameEditText = binding.username;
		final EditText passwordEditText = binding.password;
		final Button loginButton = binding.login;
		final ProgressBar loadingProgressBar = binding.loading;

		loginButton.setOnClickListener(v -> {
			loadingProgressBar.setVisibility(View.VISIBLE);
			saveLoginData(usernameEditText, passwordEditText);
		});
	}

	private void saveLoginData(EditText usernameEditText, EditText passwordEditText) {
		try {
			LoginRepository.saveCredentials(getApplicationContext(), usernameEditText.getText().toString(), passwordEditText.getText().toString());
			startMainActivity();
		} catch (GeneralSecurityException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void startMainActivity() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
}