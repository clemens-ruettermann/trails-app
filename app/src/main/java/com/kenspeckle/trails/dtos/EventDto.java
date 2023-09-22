package com.kenspeckle.trails.dtos;

import com.kenspeckle.trails.data.TimeFrame;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class EventDto implements Serializable {

	@SerializedName("id")
	private UUID id;

	@SerializedName("title")
	private String title;

	@SerializedName("description")
	private String description;

	@SerializedName("startDate")
	private String startDate;

	@SerializedName("startTime")
	private String startTime;

	@SerializedName("endDate")
	private String endDate;

	@SerializedName("endTime")
	private String endTime;

	@SerializedName("creationDate")
	private String creationDate;

	@SerializedName("location")
	private String location;

	@SerializedName("course")
	private CourseDto course;

	@SerializedName("author")
	private AuthorDto author;

	@SerializedName("registrationEnabled")
	private boolean registrationEnabled;

	@SerializedName("maxAttendees")
	private Integer maxAttendees;

	@SerializedName("loggedInRegistration")
	private LoggedInRegistrationDto loggedInRegistration;

	@SerializedName("currentAttendees")
	private Integer currentAttendees;

	@SerializedName("registrationDeadline")
	private String registrationDeadline;

	@SerializedName("registrationOpen")
	private boolean registrationOpen;

	private TimeFrame timeFrame;


	public UUID getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public String getLocation() {
		return location;
	}

	public CourseDto getCourse() {
		return course;
	}

	public AuthorDto getAuthor() {
		return author;
	}

	public boolean isRegistrationEnabled() {
		return registrationEnabled;
	}

	public Integer getMaxAttendees() {
		return maxAttendees;
	}

	public LoggedInRegistrationDto getLoggedInRegistration() {
		return loggedInRegistration;
	}

	public Integer getCurrentAttendees() {
		return currentAttendees;
	}

	public String getRegistrationDeadline() {
		return registrationDeadline;
	}

	public boolean isRegistrationOpen() {
		return registrationOpen;
	}

	public TimeFrame getTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(TimeFrame timeFrame) {
		this.timeFrame = timeFrame;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EventDto eventDto = (EventDto) o;
		return Objects.equals(id, eventDto.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
