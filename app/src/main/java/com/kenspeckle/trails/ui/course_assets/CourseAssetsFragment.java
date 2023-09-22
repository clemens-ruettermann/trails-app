package com.kenspeckle.trails.ui.course_assets;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.kenspeckle.trails.Api;
import com.kenspeckle.trails.RetrofitClient;
import com.kenspeckle.trails.data.CourseCategory;
import com.kenspeckle.trails.databinding.FragmentCourseAssetsBinding;
import com.kenspeckle.trails.dtos.CourseAssetDto;
import com.kenspeckle.trails.dtos.CourseAssetsDto;
import com.kenspeckle.trails.R;
import com.kenspeckle.trails.utils.MiscUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseAssetsFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback{

	public static final Path DOWNLOADS_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toPath();
	private FragmentCourseAssetsBinding binding;

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		binding = FragmentCourseAssetsBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		loadAssets(CourseCategory.JAGDHUNDE, binding.layoutJagdhunde);
		loadAssets(CourseCategory.JAGDPRAXIS, binding.layoutJagdpraxis);
		loadAssets(CourseCategory.NIEDERWILD, binding.layoutNiederwild);
		loadAssets(CourseCategory.ORGANISATION, binding.layoutOrganisation);
		loadAssets(CourseCategory.RECHT, binding.layoutRecht);
		loadAssets(CourseCategory.SCHALENWILD, binding.layoutSchalenwild);
		loadAssets(CourseCategory.SONSTIGES, binding.layoutSonstiges);
		loadAssets(CourseCategory.WAFFEN, binding.layoutWaffen);

		return root;
	}

	private void saveResponseBodyToFileAndOpen(@NonNull ResponseBody body, @NonNull CourseCategory category, @NonNull String filename, @NonNull String mimeType) {
		try {
			Path trailsPath = createFolderStructureInDownloads(category);
			Path path = trailsPath.resolve(filename);

			if (!Files.exists(path) || body.contentLength() != Files.size(path)) {
				Files.createFile(path);

				InputStream is = body.byteStream();

				FileOutputStream fos = new FileOutputStream(path.toFile());
				int read;
				byte[] buffer = new byte[32768];
				while( (read = is.read(buffer)) > 0) {
					fos.write(buffer, 0, read);
				}

				fos.close();
				is.close();
			}
			openFile(path, mimeType);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadAssets(final CourseCategory category, final LinearLayout layout) {

		Callback<CourseAssetsDto> callback = new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<CourseAssetsDto> call, @NonNull Response<CourseAssetsDto> response) {
				addAssetsToLayout(response, layout);
			}

			@Override
			public void onFailure(@NonNull Call<CourseAssetsDto> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), "Fehler beim Laden von " + category.name(), Toast.LENGTH_LONG).show();
			}
		};
		RetrofitClient
				.getApi()
				.getCourseAssets(category, Api.SORTING)
				.enqueue(callback);
	}

	private void addAssetsToLayout(@NonNull Response<CourseAssetsDto> response, LinearLayout layout) {
		CourseAssetsDto body = response.body();
		if (body != null && body.getAssets() != null) {
			for (CourseAssetDto dto : body.getAssets()) {

				final TextView textView = new TextView(getContext());
				textView.setText(dto.getName());
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				params.setMargins(
						0,
						MiscUtils.convertDipToPixels(getContext(), 16),
						0,
						0);
				textView.setLayoutParams(params);

				try {
					if (Files.exists(createFolderStructureInDownloads(dto.getCategory()).resolve(dto.getName()))) {
						textView.setBackgroundColor(layout.getResources().getColor(R.color.light_green, getContext().getTheme()));
					}
				} catch (IOException e) {
					Log.e("CourseAssetsFragment", e.toString());
					Toast.makeText(getContext(), "Fehler beim Erstellen der Ordner. Datei kann nicht runtergeladen werden", Toast.LENGTH_LONG).show();
					textView.setBackgroundColor(layout.getResources().getColor(R.color.light_red, getContext().getTheme()));
				}


				View.OnClickListener onClickListener = v -> {
					try {
						Path path = createFolderStructureInDownloads(dto.getCategory()).resolve(dto.getName());

						if (!Files.exists(path)) {
							RetrofitClient
									.getApi()
									.downloadFile(dto.getAssetId().toString())
									.enqueue(createAssetDownloadCallback(textView, dto.getCategory(), dto.getName(), dto.getMimeType()));
						} else {
							String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path.getFileName().toString()));
							openFile(path, mimeType);
						}

					} catch (IOException e) {
						Log.e("CourseAssetsFragment", e.toString());
					}
				};

				textView.setOnClickListener(onClickListener);
				layout.addView(textView);
			}
		}
	}

	private Path createFolderStructureInDownloads(@NonNull CourseCategory category) throws IOException {
		Path trails = DOWNLOADS_DIR.resolve("Trails");
		if (!Files.isDirectory(trails)) {
			Files.createDirectories(trails);
		}

		Path course = trails.resolve(category.name());
		if (!Files.isDirectory(course)) {
			Files.createDirectories(course);
		}
		return course;
	}

	private void openFile(Path path, String mimeType) {
		Uri uriForFile = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", path.toFile(), path.getFileName().toString());

		// Open file with user selected app
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(uriForFile, mimeType);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		// todo fixme
	}


	private Callback<ResponseBody> createAssetDownloadCallback(
			@NonNull final TextView textView,
			@NonNull final CourseCategory category,
			@NonNull final String filename,
			@NonNull final String mimeType) {

		return new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

				try (ResponseBody body = response.body()) {
					if (body == null) {
						Toast.makeText(getContext(), "Fehler beim Download", Toast.LENGTH_LONG).show();
						return;
					}
					textView.setBackgroundColor(requireContext().getResources().getColor(R.color.light_green, requireContext().getTheme()));
					saveResponseBodyToFileAndOpen(body, category, filename, mimeType);
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), "Fehler beim Download der Datei", Toast.LENGTH_LONG).show();
			}
		};
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
