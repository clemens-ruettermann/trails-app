package com.kenspeckle.trails;

import com.kenspeckle.trails.data.TimeFrame;
import com.kenspeckle.trails.dtos.BaseDto;
import com.kenspeckle.trails.dtos.EventDto;
import com.kenspeckle.trails.dtos.NewsDto;
import com.kenspeckle.trails.dtos.QAndADto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

	@GET("api/news")
	Call<BaseDto<NewsDto>> getNews(@Query("page") int page, @Query("pageSize") int pageSize);

	@GET("api/event")
	Call<BaseDto<EventDto>> getEvents(@Query("page") int page, @Query("pageSize") int pageSize, @Query("timeFrame") TimeFrame timeFrame);

	@GET("api/question")
	Call<BaseDto<QAndADto>> getQAndAs(@Query("page") int page, @Query("pageSize") int pageSize);
}
