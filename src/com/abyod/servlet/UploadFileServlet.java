package com.abyod.servlet;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.abyod.utils.Utils;
import com.abyod.utils.VideoCompressorConstant;

public class UploadFileServlet extends HttpServlet {
	
	Logger logger = Logger.getLogger(UploadFileServlet.class);
	
	private static final long serialVersionUID = 1L;	

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		// process only if its multipart content
		if (isMultipart) {
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				// Parse the request
				List<FileItem> multiparts = upload.parseRequest(request);
				File dir = new File((String) request.getAttribute(VideoCompressorConstant.SOURCE_DIRECTORY));
				dir.mkdir();
				for (FileItem item : multiparts) {
					if (!item.isFormField()) {
						String name = getFileName(dir, new File(item.getName()).getName());

						item.write(new File(dir,name));
					}
				}
				logger.info("File uploaded successfully over server.");
				response.getWriter().write(Utils.convertTOString(dir.listFiles()));
			} catch (Exception e) 
			{
				System.out.println("File upload failed");
			}

		}
	}	

	private String getFileName(File uploadDirectory, final String fileName) {
		final String basename = FilenameUtils.getBaseName(fileName);
		final String extension = FilenameUtils.getExtension(fileName);
		
		FilenameFilter filter =  new FilenameFilter() {

			@Override
			public boolean accept(File dir, String file) {
				if(file.startsWith(basename)){
					return true;
				} else {
					return false;
				}
			}
		};
		int count =  uploadDirectory.listFiles(filter).length;
		if(count !=0 ){
			return new File(uploadDirectory, basename+"_(" + count + ")."+extension).getName();
		} else {
			return new File(uploadDirectory, fileName).getName();
		}

	}
}