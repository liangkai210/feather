package org.feather.crawler;

import java.util.Map;

import org.feather.common.util.JsonUtil;

public class Auto {

	private String title;
	private String description;
	private String autoName;
	private long postedTime;
	private long updatedTime;
	private double price;
	private Map<String, String> autoInfo;

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAutoName() {
		return autoName;
	}

	public void setAutoName(String autoName) {
		this.autoName = autoName;
	}

	public long getPostedTime() {
		return postedTime;
	}

	public void setPostedTime(long postedTime) {
		this.postedTime = postedTime;
	}

	public long getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Map<String, String> getAutoInfo() {
		return autoInfo;
	}

	public void setAutoInfo(Map<String, String> autoInfo) {
		this.autoInfo = autoInfo;
	}

}
