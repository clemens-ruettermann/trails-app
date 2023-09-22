package com.kenspeckle.trails.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
	// The minimum amount of items to have below your current scroll position
	// before loading more.
	private int visibleThreshold = 5;
	// The current offset index of data you have loaded
	private int currentPage = 0;
	// The total number of items in the dataset after the last load
	private int previousTotalItemCount = 0;
	// True if we are still waiting for the last set of data to load.
	private boolean loading = true;
	// Sets the starting page index
	private final int startingPageIndex = 0;

	private int totalItemCount = 0;
	private int currentItemCount = 0;


	RecyclerView.LayoutManager mLayoutManager;

	public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
		this.mLayoutManager = layoutManager;
	}

	public int getLastVisibleItem() {
		if (mLayoutManager instanceof StaggeredGridLayoutManager) {
			int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
			// get maximum element within the list
			int maxSize = 0;
			for (int i = 0; i < lastVisibleItemPositions.length; i++) {
				if (i == 0) {
					maxSize = lastVisibleItemPositions[i];
				}
				else if (lastVisibleItemPositions[i] > maxSize) {
					maxSize = lastVisibleItemPositions[i];
				}
			}
			return maxSize;
		} else if (mLayoutManager instanceof GridLayoutManager) {
			return ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
		} else if (mLayoutManager instanceof LinearLayoutManager) {
			return ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
		} else {
			return 0;
		}
	}

	// This happens many times a second during a scroll, so be wary of the code you place here.
	// We are given a few useful parameters to help us work out if we need to load some more data,
	// but first we check if we are waiting for the previous load to finish.
	@Override
	public void onScrolled(@NonNull RecyclerView view, int dx, int dy) {
		currentItemCount = mLayoutManager.getItemCount();
		if (currentItemCount == totalItemCount) {
			return;
		}

		int lastVisibleItemPosition = getLastVisibleItem();

		// If the total item count is zero and the previous isn't, assume the
		// list is invalidated and should be reset back to initial state
		if (currentItemCount < previousTotalItemCount) {
			this.currentPage = this.startingPageIndex;
			this.previousTotalItemCount = currentItemCount;
			if (currentItemCount == 0) {
				this.loading = true;
			}
		}
		// If it’s still loading, we check to see if the dataset count has
		// changed, if so we conclude it has finished loading and update the current page
		// number and total item count.
		if (loading && (currentItemCount > previousTotalItemCount)) {
			loading = false;
			previousTotalItemCount = currentItemCount;
		}

		// If it isn’t currently loading, we check to see if we have breached
		// the visibleThreshold and need to reload more data.
		// If we do need to reload some more data, we execute onLoadMore to fetch the data.
		// threshold should reflect how many total columns there are too
		if (!loading && (lastVisibleItemPosition + visibleThreshold) > currentItemCount) {
			currentPage++;
			onLoadMore(currentPage, view);
			loading = true;
		}
	}

	// Call this method whenever performing new searches
	public void resetState() {
		this.currentPage = this.startingPageIndex;
		this.previousTotalItemCount = 0;
		this.loading = true;
		this.totalItemCount = 0;
		this.currentItemCount = 0;
	}

	public int getTotalItemCount() {
		return totalItemCount;
	}

	public void setTotalItemCount(int totalItemCount) {
		this.totalItemCount = totalItemCount;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	// Defines the process for actually loading more data based on page
	public abstract void onLoadMore(int page, RecyclerView view);

}