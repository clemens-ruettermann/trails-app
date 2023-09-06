package com.kenspeckle.trails.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

public class NewsDto implements Serializable {

	private static final long serialVersionUID = 0L;

	@SerializedName("id")
	private UUID id;
	@SerializedName("title")
	private String title;
	@SerializedName("body")
	private String body;
	@SerializedName("teaser")
	private String teaser;
	@SerializedName("creationDate")
	private String creationDate;
	@SerializedName("course")
	private CourseDto course;
	@SerializedName("author")
	private AuthorDto author;

	public UUID getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public String getTeaser() {
		return teaser;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public CourseDto getCourse() {
		return course;
	}

	public AuthorDto getAuthor() {
		return author;
	}
}
