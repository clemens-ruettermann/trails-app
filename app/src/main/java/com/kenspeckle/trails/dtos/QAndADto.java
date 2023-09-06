package com.kenspeckle.trails.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class QAndADto implements Serializable {

	@SerializedName("id")
	private UUID id;

	@SerializedName("course")
	private CourseDto course;

	@SerializedName("title")
	private String title;

	@SerializedName("teaser")
	private String teaser;

	@SerializedName("body")
	private String body;

	@SerializedName("category")
	private String category;

	@SerializedName("creationDate")
	private String creationDate;

	@SerializedName("readCounter")
	private Integer readCounter;

	@SerializedName("answers")
	private List<AnswerDto> answers;

	@SerializedName("answered")
	private Boolean answered;

	@SerializedName("author")
	private AuthorDto author;

	public UUID getId() {
		return id;
	}

	public CourseDto getCourse() {
		return course;
	}

	public String getTitle() {
		return title;
	}

	public String getTeaser() {
		return teaser;
	}

	public String getBody() {
		return body;
	}

	public String getCategory() {
		return category;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public Integer getReadCounter() {
		return readCounter;
	}

	public List<AnswerDto> getAnswers() {
		return answers;
	}

	public Boolean getAnswered() {
		return answered;
	}

	public AuthorDto getAuthor() {
		return author;
	}
}
