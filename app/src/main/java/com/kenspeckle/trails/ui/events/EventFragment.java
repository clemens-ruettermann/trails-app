package com.kenspeckle.trails.ui.events;

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
import com.kenspeckle.trails.databinding.FragmentEventsBinding;
import com.kenspeckle.trails.dtos.BaseDto;
import com.kenspeckle.trails.Constants;
import com.kenspeckle.trails.LoginFailedException;
import com.kenspeckle.trails.data.TimeFrame;
import com.kenspeckle.trails.dtos.EventDto;
import com.kenspeckle.trails.ui.EndlessRecyclerViewScrollListener;
import com.kenspeckle.trails.ui.login.LoginActivity;
import com.kenspeckle.trails.ui.news.NewsFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventFragment extends Fragment {

	private FragmentEventsBinding binding;

	private EventAdapter eventAdapter;

	private EndlessRecyclerViewScrollListener scrollListener;

	private static int SCROLL_POSITION = 0;

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		binding = FragmentEventsBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		RecyclerView recyclerView = binding.recyclerViewEvents;
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);
		DividerItemDecoration decor = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
		recyclerView.addItemDecoration(decor);

		scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
			@Override
			public void onLoadMore(int page, RecyclerView view) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to the bottom of the list
				loadData(page, TimeFrame.FUTURE);
			}
		};
		recyclerView.addOnScrollListener(scrollListener);
		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

				RecyclerView.LayoutManager layoutManager1 = recyclerView.getLayoutManager();
				if (layoutManager1 instanceof LinearLayoutManager) {
					SCROLL_POSITION = ((LinearLayoutManager)layoutManager1).findFirstVisibleItemPosition();
				}
			}
		});
		eventAdapter = new EventAdapter(new ArrayList<>());
		recyclerView.setAdapter(eventAdapter);

		loadData(0, TimeFrame.PRESENT);


		return root;
	}

	/**
	 * When called with {@link TimeFrame.PRESENT} it will load all present pages using a recursive call
	 * I am not sure how to improve this, to not be a recursive call.
	 * After all present pages are loaded it will switch over to {@link TimeFrame.FUTURE} and load the first page
	 *
	 * @param page
	 * @param timeFrame
	 */
	private void loadData(int page, TimeFrame timeFrame) {
		try {
			Api api = RetrofitClient.getApi();

			Callback<BaseDto<EventDto>> presentCallback = new Callback<>() {
				@Override
				public void onResponse(@NonNull Call<BaseDto<EventDto>> call, @NonNull Response<BaseDto<EventDto>> response) {
					if (RetrofitClient.LOGIN_FAILED) {
						Intent intent = new Intent(getContext(), LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(intent);
					}
					BaseDto<EventDto> body = response.body();
					if (body != null && body.getElements() != null) {
						List<EventDto> newsList = body.getElements();
						if (scrollListener.getTotalItemCount() == 0) {
							scrollListener.setTotalItemCount(body.getTotalElements());
						}
						scrollListener.setTotalItemCount(body.getTotalElements());
						eventAdapter.addElementsAndSort(newsList);

						if (timeFrame == TimeFrame.PRESENT) {
							// for present load all data
							// I know that this is a recursive call, but I don't know what a better way would look like
							if (body.getNumber() + 1 < body.getTotalPages()) {
								loadData(page + 1, timeFrame);
							} else {
								loadData(0, TimeFrame.FUTURE);
							}
						}
					} else if (timeFrame == TimeFrame.PRESENT){
						// if there are no elements and the timeFrame is PRESENT, switch to FUTURE
						loadData(0, TimeFrame.FUTURE);
					}
				}

				@Override
				public void onFailure(@NonNull Call<BaseDto<EventDto>> call, @NonNull Throwable t) {
					Log.e(NewsFragment.class.getName(), "onFailure: EventFragemnt#loadData()", t);
					Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
				}
			};
			Call<BaseDto<EventDto>> callFuture = api.getEvents(page, Constants.PAGE_SIZE, timeFrame);
			Log.e("EventFragment", "loadMore(" + page + ", " + timeFrame.name() + ")");
			callFuture.enqueue(presentCallback);
			System.out.println();
		} catch (LoginFailedException e) {
			Intent intent = new Intent(getContext(), LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		} catch (Exception e) {
			Log.e(NewsFragment.class.getName(), "onFailure: EventFragemnt#loadData()", e);
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
