package com.abyod.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.abyod.service.VideoCompressorService;
import com.abyod.videocompressor.EncoderException;
import com.abyod.videocompressor.InputFormatException;
import com.fileupload.Utils;


public class CommonActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("action")!=null){
			if(request.getParameter("action").equals("format")){
				response.getWriter().append(Utils.convertTOString(VideoCompressorService.getAvailableFormats()));
			}else if(request.getParameter("action").equals("delete")){
				String fileName = request.getParameter("fileName");
				String uploadedDirectory = (String) request.getAttribute("uploadDirectory");
				VideoCompressorService.deleteFile(uploadedDirectory,fileName);
				response.getWriter().append(fileName +" has been Deleted.");
			}else if(request.getParameter("action").equals("status")){				
				response.getWriter().append(VideoCompressorService.getProgressStatus().toString());
			}else if(request.getParameter("action").equals("convert")){
				String fileName = request.getParameter("fileName");
				String format = request.getParameter("format");
				String uploadedDirectory = (String) request.getAttribute("uploadDirectory");
				
				File downloadedFile = null;
				try{
				if(format!=null){
					if(format.equals("FLV")){
						downloadedFile = new VideoCompressorService().encodeTOFlv(uploadedDirectory+File.separator+fileName);
					} else if(format.equals("MP3")){
						downloadedFile = new VideoCompressorService().encodeToMp3(uploadedDirectory+File.separator+fileName);
					} else if(format.equals("AVI")){
						downloadedFile = new VideoCompressorService().encodeTOAvi(uploadedDirectory+File.separator+fileName);
					} else if(format.equals("Mpeg4")){
						downloadedFile = new VideoCompressorService().encodeToMpeg4(uploadedDirectory+File.separator+fileName);
					} else if(format.equals("3GP")){
						downloadedFile = new VideoCompressorService().encodeTo3GP(uploadedDirectory+File.separator+fileName);
					}
					writeFileInResponse(downloadedFile,response);
				}
				}catch(Exception ex){
					response.getWriter().append(ex.getMessage());	
				}
				
			}else{
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
