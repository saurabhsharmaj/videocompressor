package com.abyod.filter;

import java.io.File;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.abyod.utils.VideoCompressorConstant;

// Implements Filter class
public class SetUpDirectoryFilter implements Filter {

	Logger logger = Logger.getLogger(SetUpDirectoryFilter.class);
	
	private File srcDirectory = null;
	private File outputDirectory = null;

	public void init(FilterConfig config) throws ServletException {
		PropertyConfigurator.configure("log4j.properties");
		// Get init parameter
		String uploadDir = config.getInitParameter("uploadDir");
		File uploadDirectory = new File(uploadDir);
		uploadDirectory.mkdirs();

		srcDirectory = new File(uploadDir, VideoCompressorConstant.SOURCE_DIRECTORY);
		srcDirectory.mkdirs();
		logger.info("Source Directory created ["+srcDirectory.getAbsolutePath()+"].");
		
		outputDirectory = new File(uploadDir, VideoCompressorConstant.OUTPUT_DIRECTORY);
		outputDirectory.mkdirs();
		logger.info("output Directory created ["+outputDirectory.getAbsolutePath()+"].");
	}
	

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws java.io.IOException, ServletException {
		request.setAttribute(VideoCompressorConstant.SOURCE_DIRECTORY, srcDirectory.getAbsolutePath());
		request.setAttribute(VideoCompressorConstant.OUTPUT_DIRECTORY, outputDirectory.getAbsolutePath());
		logger.info("Source Directory: "+srcDirectory.getAbsolutePath()+" output Directory: "+outputDirectory.getAbsolutePath()+ "Set in Request.");
		// Pass request back down the filter chain
		chain.doFilter(request, response);
	}

	public void destroy() {
		/*
		 * Called before the Filter instance is removed from service by the web
		 * container
		 */
	}
}