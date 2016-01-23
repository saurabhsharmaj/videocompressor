package com.abyod.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.abyod.service.VideoCompressorService;
import com.abyod.utils.Utils;
import com.abyod.utils.VideoCompressorConstant;


public class CommonActionServlet extends HttpServlet {
	
	Logger logger = Logger.getLogger(CommonActionServlet.class);
	
	private static final long serialVersionUID = 1L;
       
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("CommonAction: Action="+request.getParameter("action"));
		
		if(request.getParameter("action")!=null){
			if(request.getParameter("action").equals("format")){
				response.getWriter().append(Utils.convertTOString(VideoCompressorService.getAvailableFormats()));
			}else if(request.getParameter("action").equals("delete")){
				String fileName = request.getParameter("fileName");
				String parentDirectory = null;
				logger.info("type: "+request.getParameter("type"));
				if(request.getParameter("type")==null || request.getParameter("type").equals("src")){
					parentDirectory = (String) request.getAttribute(VideoCompressorConstant.SOURCE_DIRECTORY);
				} else {
					parentDirectory = (String) request.getAttribute(VideoCompressorConstant.OUTPUT_DIRECTORY);
				}
				Boolean isDelted = VideoCompressorService.deleteFile(parentDirectory ,fileName);
				logger.info("Deleted: "+isDelted);
				
				response.getWriter().append(fileName +" has been Deleted.");
			}else if(request.getParameter("action").equals("status")){				
				response.getWriter().append(VideoCompressorService.getProgressStatus().toString());
			}else if(request.getParameter("action").equals("convert")){
				String fileName = request.getParameter("fileName");
				String format = request.getParameter("format");
				String srcDirectory = (String) request.getAttribute(VideoCompressorConstant.SOURCE_DIRECTORY);
				String targetDirectory = (String) request.getAttribute(VideoCompressorConstant.OUTPUT_DIRECTORY);
				
				File downloadedFile = null;
				try{
				if(format!=null){
					if(format.equals("FLV")){
						downloadedFile = new VideoCompressorService().encodeTOFlv(srcDirectory+File.separator+fileName ,targetDirectory);
					} else if(format.equals("MP3")){
						downloadedFile = new VideoCompressorService().encodeToMp3(srcDirectory+File.separator+fileName,targetDirectory);
					} else if(format.equals("AVI")){
						downloadedFile = new VideoCompressorService().encodeTOAvi(srcDirectory+File.separator+fileName,targetDirectory);
					} else if(format.equals("Mpeg4")){
						downloadedFile = new VideoCompressorService().encodeToMpeg4(srcDirectory+File.separator+fileName,targetDirectory);
					} else if(format.equals("3GP")){
						downloadedFile = new VideoCompressorService().encodeTo3GP(srcDirectory+File.separator+fileName,targetDirectory);
					}
					writeFileInResponse(downloadedFile,response);
				}
				}catch(Exception ex){
					response.getWriter().append(ex.getMessage());	
				}
				
			}else if(request.getParameter("action").equalsIgnoreCase("getfiles")){
				File dir;
				if(request.getParameter("type")==null || request.getParameter("type").equals("src")){
					dir = new File((String) request.getAttribute(VideoCompressorConstant.SOURCE_DIRECTORY));
				} else {
					dir = new File((String) request.getAttribute(VideoCompressorConstant.OUTPUT_DIRECTORY));
					
				}				
				response.getWriter().write(dir!=null?Utils.convertTOString(dir.listFiles()):"");
			} else {
				response.getWriter().append(request.getContextPath());
			}
		} else {
		response.getWriter().append(request.getContextPath());
		}
	}

	private void writeFileInResponse(File downloadFile, HttpServletResponse response){

		try {			
			
			response.setContentType("application/octet-stream");
	        response.setContentLength((int) downloadFile.length());
	         
	        // forces download
	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
	        response.setHeader(headerKey, headerValue);
	         
	        // obtains response's output stream
	        OutputStream outStream = response.getOutputStream();
	         
	        byte[] buffer = new byte[4096];
	        int bytesRead = -1;
	        FileInputStream inStream = new FileInputStream(downloadFile);
	        while ((bytesRead = inStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	        
	        inStream.close();
	        outStream.close(); 
	        
		} catch (Exception e) {
			try {
				response.getWriter().append(e.getMessage());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	
	}

}
