package com.kenspeckle.trails.ui.q_and_a;

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
import com.kenspeckle.trails.databinding.FragmentQAndABinding;
import com.kenspeckle.trails.dtos.BaseDto;
import com.kenspeckle.trails.dtos.QAndADto;
import com.kenspeckle.trails.Constants;
import com.kenspeckle.trails.ui.EndlessRecyclerViewScrollListener;
import com.kenspeckle.trails.ui.login.LoginActivity;
import com.kenspeckle.trails.ui.news.NewsFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QAndAFragment extends Fragment {

	private FragmentQAndABinding binding;

	private QAndAAdapter qAndAAdapter;

	private EndlessRecyclerViewScrollListener scrollListener;

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		binding = FragmentQAndABinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		RecyclerView recyclerView = binding.recyclerViewQAndA;
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
		scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
			@Override
			public void onLoadMore(int page, RecyclerView view) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to the bottom of the list
				loadData(page);
			}
		};
		recyclerView.addOnScrollListener(scrollListener);
		qAndAAdapter = new QAndAAdapter(new ArrayList<>());
		recyclerView.setAdapter(qAndAAdapter);
		loadData(0);
		return root;
	}


	private void loadData(int page) {
		try {
			Api api = RetrofitClient.getApi();

			Callback<BaseDto<QAndADto>> presentCallback = new Callback<>() {
				@Override
				public void onResponse(@NonNull Call<BaseDto<QAndADto>> call, @NonNull Response<BaseDto<QAndADto>> response) {
					if (RetrofitClient.LOGIN_FAILED) {
						Intent intent = new Intent(getContext(), LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(intent);
					}
					BaseDto<QAndADto> body = response.body();
					if (body != null && body.getElements() != null) {
						List<QAndADto> qAndADtos = body.getElements();
						if (scrollListener.getTotalItemCount() == 0) {
							scrollListener.setTotalItemCount(body.getTotalElements());
						}
						scrollListener.setTotalItemCount(body.getTotalElements());
						qAndAAdapter.addElementsAtEnd(qAndADtos);
					}
				}

				@Override
				public void onFailure(@NonNull Call<BaseDto<QAndADto>> call, @NonNull Throwable t) {
					Log.e(NewsFragment.class.getName(), "onFailure: QAndAFragemnt#loadData()", t);
					Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
				}
			};
			Call<BaseDto<QAndADto>> callFuture = api.getQAndAs(page, Constants.PAGE_SIZE);
			callFuture.enqueue(presentCallback);
		} catch (Exception e) {
			Log.e(NewsFragment.class.getName(), "onFailure: QAndAFragemnt#loadData()", e);
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