<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>Video Compressor</title>
<link rel="shortcut icon" href="data:image/x-icon;," type="image/x-icon"> 
<!-- Include jQuery form & jQuery script file. -->
<script src="js/jquery.js" ></script>
<script src="js/jquery-ui.js"></script>
<script src="js/jquery.form.js" ></script>
<script src="js/fileUploadScript.js" ></script>
<script src="js/Chart.min.js"></script>
  <script src="js/pretty-doughtnut.js"></script>
<script type="text/javascript">
$( document ).ready(function() {
	$('#message').html('');
	$('#result').html('');
	$.ajax({url: "commonAction?action=format", success: function(response){
		$('#format').html('');
		response.replace(/,\s*$/, "").split(',').forEach(function(format){
			$('#format').append('<option value="'+format+'">'+format+'</option>');
		});
	}
	});
	
	$.ajax({url: "existingFile", success: function(response){
		$('#filestableBody').html('');
		var data='';
		response.replace(/,\s*$/, "").split(',').forEach(function(fileName) {
			data += '<tr>'+
						'<td><input type="checkbox"/></td>'+
						'<td>'+fileName.split('#')[0]+'</td>'+						
						'<td>'+fileName.split('#')[1]+'</td>'+
						'<td><img src="images/trash.png" onclick="deleteFile(this);" title="delete file" /></td>'+
						'<td><img src="images/compress.png" title="compress file" onclick="compress(this);" /></td>'+
					'</tr>';
		});
		$('#filestableBody').append(data);
    }});
	
	
	$( "#dialog" ).dialog({autoOpen: false,title:'Compress'});	
	
	doughnutWidget.options = {
		    container: $('#container'),
		    width: 100,
		    height: 100,
		    class: 'myClass',
		    cutout: 50
		  };
});

function deleteFile(ref){
	ConfirmDeleteDialog(ref, 'Are you sure?');
	
}

function compress(ref){
	var fileName = $(ref).closest('tr').find('td').eq(1).html();
	$('#selectedFile').val(fileName);
	$("#dialog").dialog('open');	
}

function convertVideo(){	
	var fileName =$('#selectedFile').val();
	var format = $('#format').val();	
	$("#dialog").dialog('close');
	var timeoutID;
	(function worker() {
		  $.ajax({
		    url: 'commonAction?action=status', 
		    success: function(data) {
		      $('#result').html(data);
		      doughnutWidget.render({compress:{
			      val: parseInt(data),
			      color: '#57B4F2',
			      click: function(e) {
			        console.log('hi');
			      }
			    }});
		      
		    },
		    complete: function(data) {
		      // Schedule the next request when the current one's complete
		      console.log(parseInt(data.responseText) == 100);
		      if(parseInt(data.responseText) == 100){
		    	  clearTimeout(timeoutID);
		      } else {
		      	timeoutID = setTimeout(worker, 1000);
		      }
		    }
		  });
		})();
	
	window.location.href = "commonAction?action=convert&fileName="+fileName+"&format="+format;
	
}

function ConfirmDeleteDialog(ref, message){
	
    $('<div></div>').appendTo('body')
                    .html('<div><h6>'+message+'?</h6></div>')
                    .dialog({
                        modal: true, title: 'Delete', zIndex: 10000, autoOpen: true,
                        width: 'auto', resizable: false,
                        buttons: {
                            Yes: function () {
                            	$(this).dialog("close");
                            	var fileName = $(ref).closest('td').prev().html();
                            	$.ajax({url: "commonAction?action=delete&fileName="+fileName, success: function(response){
                            		$('#message').html(fileName+ 'has been deleted.');
                            		$(ref).closest('tr').remove();
                            		
                            	}});
                                
                            },
                            No: function () {
                                $(this).dialog("close");
                            }
                        },
                        close: function (event, ui) {
                            $(this).remove();
                        }
                    });
    };
</script>
<!-- Include css styles here -->
<link rel="stylesheet" href="css/jquery-ui.css">
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="logo"><img src="images/logo.jpg" alter="Jecrc University"></div>
	<h3>Compress Your Video</h3>
	
	<div id="dialog" title="Basic dialog">
	  <p>Select Output Format : </p>
	  	<select id="format">
	  		<option>FLV</option>
	  		<option>FLV</option>
	  	</select>
	  	<button onclick="convertVideo();">Convert & Download</button>
	</div>
	
	<div id="message"></div>
	<input type="hidden" id="selectedFile">
	<form id="UploadForm" action="UploadFile" method="post"
		enctype="multipart/form-data">
		<input type="file" size="60" id="myfile" name="myfile"> <input
			type="submit" value="Upload">

		<div id="progressbox">
			<div id="progressbar"></div>
			<div id="percent">0%</div>
		</div>
		<div id="result"></div>
		<br />
		<div id="container" class="margin" role="group"></div>
		<div id="message"></div>
	</form>
	<fieldset>
	<legend>Source Files</legend>
		<table >
			<tr><th>Select</th><th>FileName</th><th>Actual Size</th><th>Delete</th><th>Compress</th></tr>
		<tbody id="filestableBody">
			<tr><td colspan="5" align="center"> No file Available</td></tr>
		</tbody>
		</table>
	</fieldset>

	<fieldset>
	<legend>Compress Files</legend>
		<table >
			<tr><th>Select</th><th>FileName</th><th>Delete</th><th>Before Size</th><th>After Size</th><th>Action</th></tr>
		<tbody id="compressFilesTableBody">
			<tr><td colspan="5" align="center"> No file Available</td></tr>
		</tbody>
		</table>
	</fieldset>
</body>
</html>
