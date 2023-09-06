package com.kenspeckle.trails.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseDto<T> {
	// probably page number
	@SerializedName("number")
	private int number;
	@SerializedName("numberOfElements")
	private int numberOfElements;

	// requested number of elements
	@SerializedName("size")
	private int size;
	@SerializedName("totalElements")
	private int totalElements;
	@SerializedName("totalPages")
	private int totalPages;
	@SerializedName("elements")
	private List<T> elements;

	public int getNumber() {
		return number;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public int getSize() {
		return size;
	}

	public int getTotalElements() {
		return totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public List<T> getElements() {
		return elements;
	}
}
