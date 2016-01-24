$(document).ready(function() {
	var options = {
			beforeSend : function() {
				$("#progressbox").show();
				// clear everything
				$("#progressbar").width('0%');
				$("#message").empty();
				$("#percent").html("0%");
			},
			uploadProgress : function(event, position, total, percentComplete) {
				$("#progressbar").width(percentComplete + '%');
				$("#percent").html(percentComplete + '%');
				$('#knob').val(percentComplete).trigger('change');
				// change message text and % to red after 50%
				if (percentComplete > 50) {
					$("#message").html("<font color='red'>File Upload is in progress .. </font>");
				}
			},
			success : function() {
				$("#progressbar").width('100%');
				$("#percent").html('100%');
			},

			complete : function(response) {
				$("#message").html("<font color='blue'>Your file has been uploaded!</font>");
				$('#srcfilestableBody').html('');
				
				var data=JSON.parse(response.responseText);
				if(data.length===0){
					data += '<tr>'+
								'<td colspan="5" align="center">No Flie Available.</td>'+			
							'</tr>';
				} else {
				data.forEach(function(file) {
					data += '<tr>'+
								'<td><input type="checkbox"/></td>'+
								'<td>'+file.fileName+'</td>'+						
								'<td>'+file.sizeInString+'</td>'+
								'<td>'+file.lastModifiedTime+'</td>'+
								'<td><img src="images/trash.png" onclick="deleteFile(this,\'src\');" title="delete file" />'+
								'&nbsp;&nbsp;<img src="images/compress.png" title="compress file" onclick="compress(this);" /></td>'+
							'</tr>';
				});
				}
				$('#srcfilestableBody').append(data);
				$("tbody tr:even").css("background-color", "#CCCCCC");
			},
			error : function() {
				$("#message").html("<font color='red'> ERROR: unable to upload files</font>");
			}
	};
	$("#UploadForm").ajaxForm(options);
});