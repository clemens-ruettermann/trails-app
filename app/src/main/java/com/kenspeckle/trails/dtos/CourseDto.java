package com.kenspeckle.trails.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

public class CourseDto implements Serializable {
	@SerializedName("id")
	private UUID id;
	@SerializedName("name")
	private String name;
	@SerializedName("alias")
	private String alias;
	@SerializedName("organization")
	private OrganizationDto organization;
	@SerializedName("startDate")
	private String startDate;
	@SerializedName("endDate")
	private String endDate;
	@SerializedName("location")
	private String location;
	@SerializedName("shootingRangeUrl")
	private String shootingRangeUrl;
	@SerializedName("meetingUrl")
	private String meetingUrl;
}
