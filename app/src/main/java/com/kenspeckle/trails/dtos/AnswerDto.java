package com.kenspeckle.trails.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

public class AnswerDto implements Serializable {

	@SerializedName("id")
	private UUID id;

	@SerializedName("body")
	private String body;

	@SerializedName("creationDate")
	private String creationDate;

	@SerializedName("accepted")
	private Boolean accepted;

	@SerializedName("acceptedAtDate")
	private String acceptedAtDate;

	@SerializedName("author")
	private AuthorDto author;

	public UUID getId() {
		return id;
	}

	public String getBody() {
		return body;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public String getAcceptedAtDate() {
		return acceptedAtDate;
	}

	public AuthorDto getAuthor() {
		return author;
	}
}
