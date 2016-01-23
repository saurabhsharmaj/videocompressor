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
		doughnutWidget.render({fileUploaded:{
		      val: percentComplete,
		      color: '#57B4F2',
		      click: function(e) {
		        console.log('hi');
		      }
		    }});
		
		
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
		$('#filestableBody').html('');
		var data='';
		response.responseText.replace(/,\s*$/, "").split(',').forEach(function(fileName) {
			data += '<tr><td><input type="checkbox"/></td><td>'+fileName+'</td><td> <img src="images/trash.png" onclick="deleteFile(this);" /> &nbsp; <img src="images/compress.png" onclick="compress(this);" /></td></tr>';
		});
		$('#filestableBody').append(data);
	},
	error : function() {
		$("#message").html("<font color='red'> ERROR: unable to upload files</font>");
	}
};
$("#UploadForm").ajaxForm(options);
});