package com.kenspeckle.trails.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

public class AuthorDto implements Serializable {

	@SerializedName("id")
	private UUID id;
	@SerializedName("name")
	private String name;
	@SerializedName("role")
	private String role;
	@SerializedName("deletedAccount")
	private boolean deletedAccount;

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRole() {
		return role;
	}

	public boolean isDeletedAccount() {
		return deletedAccount;
	}
}
