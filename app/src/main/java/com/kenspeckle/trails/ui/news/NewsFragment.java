package com.kenspeckle.trails.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kenspeckle.trails.Api;
import com.kenspeckle.trails.RetrofitClient;
import com.kenspeckle.trails.databinding.FragmentNewsBinding;
import com.kenspeckle.trails.dtos.BaseDto;
import com.kenspeckle.trails.dtos.NewsDto;
import com.kenspeckle.trails.Constants;
import com.kenspeckle.trails.ui.EndlessRecyclerViewScrollListener;
import com.kenspeckle.trails.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {

	private FragmentNewsBinding binding;

	private NewsAdapter newsAdapter;

	private EndlessRecyclerViewScrollListener scrollListener;

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		binding = FragmentNewsBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		RecyclerView recyclerView = binding.recyclerViewNews;
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);
		DividerItemDecoration decor = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
		recyclerView.addItemDecoration(decor);
		scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
			@Override
			public void onLoadMore(int page, RecyclerView view) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to the bottom of the list
				loadData(page);
			}
		};
		recyclerView.addOnScrollListener(scrollListener);
		newsAdapter = new NewsAdapter(new ArrayList<>());
		recyclerView.setAdapter(newsAdapter);

		loadData(0);

		return root;
	}

	private void loadData(int page) {
		try {
			Api api = RetrofitClient.getApi();

			Call<BaseDto<NewsDto>> call = api.getNews(page, Constants.PAGE_SIZE);
			call.enqueue(new Callback<>() {
				@Override
				public void onResponse(@NonNull Call<BaseDto<NewsDto>> call, @NonNull Response<BaseDto<NewsDto>> response) {
					if (RetrofitClient.LOGIN_FAILED) {
						Intent intent = new Intent(getContext(), LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(intent);
					}
					BaseDto<NewsDto> body = response.body();
					if (body != null && body.getElements() != null) {
						List<NewsDto> newsList = body.getElements();
						if (scrollListener.getTotalItemCount() == 0) {
							scrollListener.setTotalItemCount(body.getTotalElements());
						}
						scrollListener.setTotalItemCount(body.getTotalElements());
						newsAdapter.addElementsAtEnd(newsList);
					}
				}

				@Override
				public void onFailure(@NonNull Call<BaseDto<NewsDto>> call, @NonNull Throwable t) {
					Log.e(NewsFragment.class.getName(), "onFailure: NewsFragemnt#loadData()", t);
					Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
				}
			});
		} catch (Exception e) {
			Log.e(NewsFragment.class.getName(), "onFailure: NewsFragemnt#loadData()", e);
			Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}

		if (RetrofitClient.LOGIN_FAILED) {
			Intent intent = new Intent(getContext(), LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}