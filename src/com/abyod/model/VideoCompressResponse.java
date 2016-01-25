package com.abyod.model;

import java.util.Date;

import com.abyod.utils.Utils;

public class VideoCompressResponse {

	private String fileName;
	private long size;
	private String sizeInString;
	private String path;
	private String timeTaken;
	private Date lastModifiedTime;

	public VideoCompressResponse() {

	}

	public VideoCompressResponse(String fileName, long size, String path, long lastModifiedTime,String timeTaken) {
		this.fileName = fileName;
		this.size = size;
		this.sizeInString = Utils.humanReadableByteCount(size, false);
		this.path = path;
		this.lastModifiedTime = new Date(lastModifiedTime);
		this.timeTaken = timeTaken;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSize() {
		return Utils.humanReadableByteCount(size,true);
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getSizeInString() {
		return sizeInString;
	}

	public void setSizeInString(String sizeInString) {
		this.sizeInString = sizeInString;
	}	
	
	
}
