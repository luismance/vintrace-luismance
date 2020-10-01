package com.vintrace.beans;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;

import com.vintrace.utils.FileUtils;

@Named
@ConversationScoped
public class FileUploadManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UploadedFile file;
	private UploadedFiles files;

	@Inject
	private Conversation conversation;

	@PostConstruct
	public void init() {

		if (!conversation.isTransient()) {
			conversation.end();
		}

		conversation.begin();
	}

	public UploadedFiles getFiles() {
		return files;
	}

	public void setFiles(UploadedFiles files) {
		this.files = files;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void upload() {
		if (file != null) {
			FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void uploadMultiple() {
		if (files != null) {
			System.out.println("File size : " + files.getFiles().size());
			for (UploadedFile f : files.getFiles()) {
				FacesMessage message = new FacesMessage("Successful", f.getFileName() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);

				try {

					// Initialize a pointer
					// in file using OutputStream
					OutputStream os = new FileOutputStream(FileUtils.getJsonsDir() + "/" + f.getFileName());

					// Starts writing the bytes in it
					os.write(f.getContent());

					// Close the file
					os.close();
				}

				catch (Exception e) {
					System.out.println("Exception: " + e);
				}
			}
		} else {
			System.out.println("No files found");
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		try {
			OutputStream os = new FileOutputStream(FileUtils.getJsonsDir() + "/" + event.getFile().getFileName());

			// Starts writing the bytes in it
			os.write(event.getFile().getContent());

			// Close the file
			os.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}
