package com.fileupload;

import java.io.File;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
 
// Implements Filter class
public class ReadDirectoryFilter implements Filter  {
   private File uploadDirectory = null;
	public void  init(FilterConfig config) 
                         throws ServletException{
      // Get init parameter 
      String uploadDir = config.getInitParameter("uploadDir"); 
      uploadDirectory = new File(uploadDir);
      uploadDirectory.mkdir();      
      //Print the init parameter 
      System.out.println("Upload Directory: " + uploadDirectory.getAbsolutePath()); 
   }
   
   public void  doFilter(ServletRequest request, 
                 ServletResponse response,
                 FilterChain chain) 
                 throws java.io.IOException, ServletException {
	  request.setAttribute("uploadDirectory",uploadDirectory.getAbsolutePath());
      request.setAttribute("files",uploadDirectory.listFiles());
 
      // Pass request back down the filter chain
      chain.doFilter(request,response);
   }
   public void destroy( ){
      /* Called before the Filter instance is removed 
      from service by the web container*/
   }
}