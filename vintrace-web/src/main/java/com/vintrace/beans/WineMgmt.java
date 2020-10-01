package com.vintrace.beans;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.gson.Gson;
import com.vintrace.model.GrapeComponent;
import com.vintrace.model.Wine;
import com.vintrace.utils.FileUtils;

@Named
@ConversationScoped
public class WineMgmt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
    private Conversation conversation;
	
	private List<Wine> wines = new ArrayList<>();
	private Wine selectedWine = new Wine();
	private Map<String, Double> componentBreakdown = new HashMap<String, Double>();
	private List<String> componentProperties = new ArrayList<String>();
	private String[] selectedProperties;
	private String breakdownLabel;

	@PostConstruct
	public void init() {
		wines = initWineList();
		componentProperties = initComponentProperties();
		if (!conversation.isTransient()){
	        conversation.end();
	    }
		
		conversation.begin();
	}
	

	public void refreshBreakdown() {
		System.out.println("Update Breakdown");
		if (selectedProperties.length > 0) {
			List<String> props = Arrays.asList(selectedProperties).stream().map(a -> (a.substring(0, 1).toUpperCase() + a.substring(1))).collect(Collectors.toList());
			breakdownLabel = String.join(",", props);
		} else {
			breakdownLabel = "Select Property";
		}
		
		componentBreakdown = new HashMap<String, Double>();

		for (GrapeComponent gc : selectedWine.getSortedComponents()) {
			List<String> propsVal = new ArrayList<String>();
			for (String prop : selectedProperties) {
				try {
					Field f = gc.getClass().getDeclaredField(prop.toLowerCase());
					f.setAccessible(true);
					propsVal.add(f.get(gc).toString());
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
					e.printStackTrace();
				}
			}

			putToMap(componentBreakdown, String.join("|", propsVal), gc.getPercentage());
		}
		
		Map<String, Double> sorted = new LinkedHashMap<>();
		componentBreakdown.entrySet().stream().sorted(Map.Entry.<String, Double> comparingByValue().reversed()).forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
		setComponentBreakdown(sorted);
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

	public List<Wine> initWineList() {
		List<Wine> wineList = new ArrayList<Wine>();

		String is = FileUtils.getJsonsDir();

		try (Stream<Path> walk = Files.walk(Paths.get(is))) {

			List<String> jsons = walk.map(x -> x.toString()).filter(f -> f.endsWith(".json")).collect(Collectors.toList());

			Gson g = new Gson();
			for (String json : jsons) {
				String content = Files.readString(Paths.get(json), StandardCharsets.UTF_8);

				Wine wine = g.fromJson(content, Wine.class);
				wineList.add(wine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return wineList;
	}

	public List<String> initComponentProperties() {
		List<String> result = Arrays.asList(GrapeComponent.class.getDeclaredFields()).stream().filter(a -> !a.getName().equalsIgnoreCase("percentage"))
				.map(a -> (a.getName().substring(0, 1).toUpperCase() + a.getName().substring(1))).collect(Collectors.toList());

		if (result.size() > 0) {
			selectedProperties = new String[1];
			selectedProperties[0] = result.get(0);

			breakdownLabel = result.get(0);
		}
		return result;
	}

	public String formatPercentage(Double percentage) {
		return String.format("%.0f%%", percentage);
	}
	
	public List<Wine> getWines() {
		return wines;
	}

	public void setWines(List<Wine> wines) {
		this.wines = wines;
	}

	public Wine getSelectedWine() {
		return selectedWine;
	}

	public void setSelectedWine(Wine selectedWine) {
		this.selectedWine = selectedWine;
		refreshBreakdown();
	}

	public Map<String, Double> getComponentBreakdown() {
		return componentBreakdown;
	}

	public void setComponentBreakdown(Map<String, Double> componentBreakdown) {
		this.componentBreakdown = componentBreakdown;
	}

	public String[] getSelectedProperties() {
		return selectedProperties;
	}

	public void setSelectedProperties(String[] selectedProperties) {
		this.selectedProperties = selectedProperties;
	}

	public List<String> getComponentProperties() {
		return componentProperties;
	}

	public void setComponentProperties(List<String> componentProperties) {
		this.componentProperties = componentProperties;
	}

	public String getBreakdownLabel() {
		return breakdownLabel;
	}

	public void setBreakdownLabel(String breakdownLabel) {
		this.breakdownLabel = breakdownLabel;
	}

}
