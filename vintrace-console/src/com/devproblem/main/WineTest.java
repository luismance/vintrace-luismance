package com.devproblem.main;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.devproblem.model.GrapeComponent;
import com.devproblem.model.Wine;

public class WineTest {

	public static void main(String[] args) {

		Wine w = new Wine("11YVCHAR001", 1000);
		w.setDescription("2011 Yarra Valley Chardonnay");
		w.setTankCode("T25-01");
		w.setProductState("Ready for bottling");
		w.setOwnerName("YV Wines Pty Ltd");
		
		
		w.getComponents().add(new GrapeComponent(80D, 2011, "Chardonnay", "Yarra Valley"));
		w.getComponents().add(new GrapeComponent(10D, 2010, "Chardonnay", "Macedon"));
		w.getComponents().add(new GrapeComponent(5D, 2011, "Pinot Noir", "Mornington"));
		w.getComponents().add(new GrapeComponent(5D, 2010, "Pinot Noir", "Macedon"));
		
		System.out.println("======================================");
		printYearBreakdown(w);
		printVarietyBreakdown(w);
		printRegionBreakdown(w);
		printYearAndVarietyBreakdown(w);
		
	}

	private static void putToMap(Map<String, Double> propertyMap, String propertyName, Double percentage) {
		if (propertyMap.containsKey(propertyName)) {
			Double currPercentage = propertyMap.get(propertyName);
			currPercentage += percentage;
			propertyMap.put(propertyName, currPercentage);
		} else {
			propertyMap.put(propertyName, percentage);
		}
	}
	
	private static void displayMap(String propertyMap, Map<String, Double> sorted) {
		System.out.println(propertyMap + " Breakdown");
		System.out.println("--------------------------------------");
		for (String key : sorted.keySet()) {
			System.out.println(sorted.get(key) + "% - " + key);
		}
		System.out.println("======================================");
	}
	
	private static void printVarietyBreakdown(Wine w) {		
		Map<String, Double> varietyMap = new HashMap<>();
		for (GrapeComponent gc : w.getComponents()) {
			putToMap(varietyMap, gc.getVariety(), gc.getPercentage());
		}
		
		Map<String, Double> sorted = new LinkedHashMap<>();
		varietyMap.entrySet().stream().sorted(Map.Entry.<String, Double> comparingByValue().reversed()).forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));

		displayMap("Variety", sorted);
	}

	private static void printYearBreakdown(Wine w) {
		Map<String, Double> yearMap = new HashMap<>();
		for (GrapeComponent gc : w.getComponents()) {
			putToMap(yearMap, String.valueOf(gc.getYear()), gc.getPercentage());
		}
		
		Map<String, Double> sorted = new LinkedHashMap<>();
		yearMap.entrySet().stream().sorted(Map.Entry.<String, Double> comparingByValue().reversed()).forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));

		displayMap("Year", sorted);
	}
	
	private static void printRegionBreakdown(Wine w) {
		Map<String, Double> regionMap = new HashMap<>();
		for (GrapeComponent gc : w.getComponents()) {
			putToMap(regionMap, gc.getRegion(), gc.getPercentage());
		}
		
		Map<String, Double> sorted = new LinkedHashMap<>();
		regionMap.entrySet().stream().sorted(Map.Entry.<String, Double> comparingByValue().reversed()).forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));

		displayMap("Region", sorted);
	}
	
	private static void printYearAndVarietyBreakdown(Wine w) {
		Map<String, Double> yearAndVarietyMap = new HashMap<>();
		for (GrapeComponent gc : w.getComponents()) {
			putToMap(yearAndVarietyMap, gc.getVariety() + " (" + String.valueOf(gc.getYear()) +")", gc.getPercentage());
		}
		
		Map<String, Double> sorted = new LinkedHashMap<>();
		yearAndVarietyMap.entrySet().stream().sorted(Map.Entry.<String, Double> comparingByValue().reversed()).forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));

		displayMap("Year and Variety", sorted);
	}

}
