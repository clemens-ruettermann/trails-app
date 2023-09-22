package com.kenspeckle.trails.dtos;

import com.kenspeckle.trails.data.CourseAssetType;
import com.kenspeckle.trails.data.CourseCategory;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class CourseAssetDto {

	@SerializedName("id")
	private UUID id;

	@SerializedName("assetId")
	private UUID assetId;

	@SerializedName("name")
	private String name;

	@SerializedName("authorId")
	private UUID authorId;

	@SerializedName("category")
	private CourseCategory category;

	@SerializedName("rank")
	private int rank;

	@SerializedName("enabled")
	private boolean enabled;

	@SerializedName("mimeType")
	private String mimeType;

	@SerializedName("fileExtension")
	private String fileExtension;

	@SerializedName("type")
	private CourseAssetType type;

	@SerializedName("size")
	private int size;


	public UUID getId() {
		return id;
	}

	public UUID getAssetId() {
		return assetId;
	}

	public String getName() {
		return name;
	}

	public UUID getAuthorId() {
		return authorId;
	}

	public CourseCategory getCategory() {
		return category;
	}

	public int getRank() {
		return rank;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public CourseAssetType getType() {
		return type;
	}

	public int getSize() {
		return size;
	}
}
