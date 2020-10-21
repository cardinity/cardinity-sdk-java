package com.cardinity.model;

import com.google.gson.annotations.SerializedName;

public class BrowserInfo {
	private String acceptHeader;
	private String browserLanguage;
	private Integer screenWidth;
	private Integer screenHeight;
	private ChallengeWindowSize challengeWindowSize;
	private String userAgent;
	private Integer colorDepth;
	private Integer timeZone;
	private String ipAddress;
	private Boolean javaEnabled;
	private Boolean javascriptEnabled;

	public String getAcceptHeader() {
		return acceptHeader;
	}

	public void setAcceptHeader(String acceptHeader) {
		this.acceptHeader = acceptHeader;
	}

	public String getBrowserLanguage() {
		return browserLanguage;
	}

	public void setBrowserLanguage(String browserLanguage) {
		this.browserLanguage = browserLanguage;
	}

	public Integer getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(Integer screenWidth) {
		this.screenWidth = screenWidth;
	}

	public Integer getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(Integer screenHeight) {
		this.screenHeight = screenHeight;
	}

	public ChallengeWindowSize getChallengeWindowSize() {
		return challengeWindowSize;
	}

	public void setChallengeWindowSize(ChallengeWindowSize challengeWindowSize) {
		this.challengeWindowSize = challengeWindowSize;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Integer getColorDepth() {
		return colorDepth;
	}

	public void setColorDepth(Integer colorDepth) {
		this.colorDepth = colorDepth;
	}

	public Integer getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(Integer timeZone) {
		this.timeZone = timeZone;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Boolean getJavaEnabled() {
		return javaEnabled;
	}

	public void setJavaEnabled(Boolean javaEnabled) {
		this.javaEnabled = javaEnabled;
	}

	public Boolean getJavascriptEnabled() {
		return javascriptEnabled;
	}

	public void setJavascriptEnabled(Boolean javascriptEnabled) {
		this.javascriptEnabled = javascriptEnabled;
	}

	public enum ChallengeWindowSize {
		@SerializedName("250x400")
		SIZE_250X400,
		@SerializedName("390x400")
		SIZE_390X400,
		@SerializedName("500x600")
		SIZE_500X600,
		@SerializedName("600x400")
		SIZE_600X400,
		@SerializedName("full-screen")
		SIZE_FULL_SCREEN;
	}
}
