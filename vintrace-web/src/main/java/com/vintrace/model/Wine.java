package com.vintrace.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Wine implements Serializable {

	private static final long serialVersionUID = -6892434771833441384L;
	
	// this is a simplified model for demonstration purposes

	private Set<GrapeComponent> components = new HashSet<GrapeComponent>();
	private String lotCode;
	private double volume;
	private String description;
	private String tankCode;
	private String productState;
	private String ownerName;
	
	public Wine() {
	}
	
	public Wine(String lotCode, double volume) {
		this.lotCode = lotCode;
		this.volume = volume;
	}
	
	public Set<GrapeComponent> getComponents() {
		return components;
	}

	public String getLotCode() {
		return lotCode;
	}

	public void setLotCode(String lotCode) {
		this.lotCode = lotCode;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTankCode() {
		return tankCode;
	}

	public void setTankCode(String tankCode) {
		this.tankCode = tankCode;
	}

	public String getProductState() {
		return productState;
	}

	public void setProductState(String productState) {
		this.productState = productState;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public void setComponents(Set<GrapeComponent> components) {
		this.components = components;
	}
	
	public String getFormattedVolume() {
		return String.format("%.0f", volume);
	}
	
	public List<GrapeComponent> getSortedComponents() {
		List<GrapeComponent> sorted = new ArrayList<>();
		sorted = components.stream().sorted((c1,c2) -> Double.compare(c2.getPercentage(), c1.getPercentage())).collect(Collectors.toList());
		return sorted;
	}
	
}

