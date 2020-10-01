package com.vintrace.utils;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class FileUtils {

	public static String getJsonsDir() {
		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		return context.getRealPath("/resources/jsons");
	}
}
