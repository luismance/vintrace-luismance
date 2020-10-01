package com.vintrace.model;

public class GrapeComponent {

	private double percentage;
	private int year;
	private String variety;
	private String region;
	
	public GrapeComponent() {
		
	}
	
	public GrapeComponent(double percentage, int year, String variety, String region) {
		this.percentage = percentage;
		this.year = year;
		this.variety = variety;
		this.region = region;
	}

	public double getPercentage() {
		return percentage;
	}
	
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	
	public String getVariety() {
		return variety;
	}
	
	public void setVariety(String variety) {
		this.variety = variety;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	
	public String getFormattedPercentage() {
		return String.format("%.0f%%", percentage);
	}

}
