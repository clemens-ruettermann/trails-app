package com.kenspeckle.trails.ui.news;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kenspeckle.trails.databinding.FragmentSelectedNewsBinding;
import com.kenspeckle.trails.dtos.NewsDto;
import com.kenspeckle.trails.utils.DateUtils;

import java.io.Serializable;

public class SelectedNewsFragment extends Fragment {

	private FragmentSelectedNewsBinding binding;

	private NewsDto newsDto;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Serializable newsDto = getArguments().getSerializable("newsDto");
		if (newsDto != null) {
			this.newsDto = (NewsDto) newsDto;
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		binding = FragmentSelectedNewsBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		binding.selectedNewsTitle.setText(newsDto.getTitle());
		binding.selectedNewsBody.setText(Html.fromHtml(newsDto.getBody(), Html.FROM_HTML_MODE_COMPACT));
		binding.selectedNewsBody.setMovementMethod(LinkMovementMethod.getInstance());
		binding.selectedNewsBody.setAutoLinkMask(Linkify.WEB_URLS);
		binding.selectedNewsBody.setLinksClickable(true);

		binding.selectedNewsAuthor.setText(newsDto.getAuthor().getName());
		binding.selectedNewsDate.setText(DateUtils.convertLocalDateTimeToLocalizedDateTime(newsDto.getCreationDate()));

		return root;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
