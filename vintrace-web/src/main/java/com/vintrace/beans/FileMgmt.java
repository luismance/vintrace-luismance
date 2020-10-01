package com.vintrace.beans;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.vintrace.utils.FileUtils;

@Named
@ConversationScoped
public class FileMgmt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> files = new ArrayList<String>();
	private boolean filterJsons = true;

	@Inject
	private Conversation conversation;

	@PostConstruct
	public void init() {
		files = initFileList();
		if (!conversation.isTransient()) {
			conversation.end();
		}

		conversation.begin();
	}

	public void refreshFileList() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("List Updated"));
		files = initFileList();
	}

	public List<String> initFileList() {
		String is = FileUtils.getJsonsDir();

		try (Stream<Path> walk = Files.walk(Paths.get(is))) {

			if (filterJsons) {
				List<String> jsons = walk.map(x -> x.getFileName().toString()).filter(f -> !f.isBlank()).filter(f -> f.endsWith(".json")).collect(Collectors.toList());
				return jsons;
			} else {
				List<String> jsons = walk.map(x -> x.getFileName().toString()).filter(f -> !f.isBlank()).collect(Collectors.toList());
				jsons.remove(0);
				return jsons;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ArrayList<String>();
	}

	public String getJsonFileDir() {
		return FileUtils.getJsonsDir();
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public boolean isFilterJsons() {
		return filterJsons;
	}

	public void setFilterJsons(boolean filterJsons) {
		this.filterJsons = filterJsons;
	}

}
