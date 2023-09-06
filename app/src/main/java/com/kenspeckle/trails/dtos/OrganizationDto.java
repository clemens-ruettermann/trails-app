package com.kenspeckle.trails.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

public class OrganizationDto implements Serializable  {

	@SerializedName("id")
	private UUID id;
	@SerializedName("name")
	private String name;
	@SerializedName("alias")
	private String alias;
	@SerializedName("contactName")
	private String contactName;
	@SerializedName("contactEmail")
	private String contactEmail;
	@SerializedName("contactAddress")
	private String contactAddress;
	@SerializedName("contactPhoneNumber")
	private String contactPhoneNumber;
	@SerializedName("mailHost")
	private String mailHost;
	@SerializedName("mailPort")
	private int mailPort;
	@SerializedName("mailSenderName")
	private String mailSenderName;
	@SerializedName("mailUsername")
	private String mailUsername;

}
