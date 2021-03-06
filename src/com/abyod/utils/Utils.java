package com.abyod.utils;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.abyod.model.VideoCompressResponse;

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
		
	public static List<VideoCompressResponse> sortFilesByCreationDate(List<VideoCompressResponse> list){
		Collections.sort(list, new Comparator<VideoCompressResponse>() {
		    public int compare(VideoCompressResponse v1, VideoCompressResponse v2) {
		        return v1.getLastModifiedTime().compareTo(v2.getLastModifiedTime());
		    }
		});
		return list;
	}

	public static List<VideoCompressResponse> readFiles(File dir) throws IOException {
		List<VideoCompressResponse> list = new ArrayList<>();
		for (File file : dir.listFiles()) {
			BasicFileAttributes attr = Files.readAttributes(dir.toPath(), BasicFileAttributes.class);
			String timeTaken = readValue(file.getParentFile().getParentFile(), Utils.getBaseName(file.getName())+".txt");
			list.add(new VideoCompressResponse(file.getName(), file.length(), file.getAbsolutePath(), attr.creationTime().toMillis(),timeTaken));
		}
		return sortFilesByCreationDate(list);
	}

	private static String readValue(File parentFile, String name) {
		BufferedReader br = null;

		String elapasedTime = "0";
		try {


			br = new BufferedReader(new FileReader(parentFile+File.separator+name));

			while ((elapasedTime = br.readLine()) != null) {
				System.out.println(elapasedTime);
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return elapasedTime;
	}
}
