package com.abyod.utils;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

public class Utils {

	static Logger logger = Logger.getLogger(Utils.class);
	
	public static String convertTOString(Object[] listFiles) {
		StringBuffer filenames = new StringBuffer();

		for (Object file : listFiles) {
			if(listFiles instanceof File[]){
				//"abc.3gp#123,xyz.wap#12"	
				filenames.append(((File)file).getName()).append("#").append(humanReadableByteCount(((File)file).length(),false)).append(",");
			}else if(listFiles instanceof String[]){
				filenames.append(((String)file)).append(",");
			}
		}
		logger.info("Converted String: "+filenames);
		return filenames.toString();
	}

	public static String getBaseName(String fileName){
		return FilenameUtils.getBaseName(fileName);
	}

	public static String getExtenstion(String fileName){
		return FilenameUtils.getExtension(fileName);
	}

	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
