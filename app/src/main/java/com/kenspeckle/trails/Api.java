package com.kenspeckle.trails;

import com.kenspeckle.trails.data.CourseCategory;
import com.kenspeckle.trails.data.TimeFrame;
import com.kenspeckle.trails.dtos.BaseDto;
import com.kenspeckle.trails.dtos.CourseAssetsDto;
import com.kenspeckle.trails.dtos.EventDto;
import com.kenspeckle.trails.dtos.NewsDto;
import com.kenspeckle.trails.dtos.QAndADto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

	String SORTING = "rank,asc";

	@GET("api/news")
	Call<BaseDto<NewsDto>> getNews(@Query("page") int page, @Query("pageSize") int pageSize);

	@GET("api/event/{uuid}")
	Call<EventDto> getEvent(@Path("uuid") String uuid);

	@GET("api/event")
	Call<BaseDto<EventDto>> getEvents(@Query("page") int page, @Query("pageSize") int pageSize, @Query("timeFrame") TimeFrame timeFrame);

	@POST("api/event/register/{uuid}")
	Call<Void> registerForEvent(@Path("uuid") String uuid);

	@DELETE("api/event/register/{uuid}")
	Call<Void> unRegisterForEvent(@Path("uuid") String uuid);

	@GET("api/question")
	Call<BaseDto<QAndADto>> getQAndAs(@Query("page") int page, @Query("pageSize") int pageSize);

	@GET("api/asset/course")
	Call<CourseAssetsDto> getCourseAssets(@Query("category") CourseCategory category, @Query("sort") String sort);

	@GET("api/asset/{uuid}/download")
	Call<ResponseBody> downloadFile(@Path("uuid") String uuid);
}
