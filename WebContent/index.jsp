<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>Video Compressor</title>
<link rel="shortcut icon" href="data:image/x-icon;," type="image/x-icon"> 
<!-- Include jQuery form & jQuery script file. -->
<script src="js/jquery.js" ></script>
<!--[if IE]><script type="text/javascript" src="js/excanvas.js"></script><![endif]-->
<script src="js/jquery.knob.min.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/jquery.form.js" ></script>
<script src="js/fileUploadScript.js" ></script>
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
	
	$.ajax({url: "commonAction?action=getfiles&type=src", success: function(response){
		$('#srcfilestableBody').html('');
		var data=JSON.parse(response);
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
    }});
	
	
	$.ajax({url: "commonAction?action=getfiles&type=target", success: function(response){
		$('#targetfilestableBody').html('');
		var data=JSON.parse(response);
		
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
							'<td><img src="images/trash.png" onclick="deleteFile(this,\'target\');" title="delete file" />'+
						'</tr>';
			});
		}
		
		$('#targetfilestableBody').append(data);
		$("tbody tr:even").css("background-color", "#CCCCCC");
    }});
	
	
	$( "#dialog" ).dialog({autoOpen: false,title:'Compress'});	
	
	renderChart();
});

function renderChart(){
	$("#knob").knob({
        change : function (value) {
            console.log("change : " + value);
        },
        release : function (value) {
            //console.log(this.$.attr('value'));
            console.log("release : " + value);
        },
        cancel : function () {
            console.log("cancel : ", this);
        },
        /*format : function (value) {
            return value + '%';
        },*/
        draw : function () {

            // "tron" case
            if(this.$.data('skin') == 'tron') {

                this.cursorExt = 0.3;

                var a = this.arc(this.cv)  // Arc
                    , pa                   // Previous arc
                    , r = 1;

                this.g.lineWidth = this.lineWidth;

                if (this.o.displayPrevious) {
                    pa = this.arc(this.v);
                    this.g.beginPath();
                    this.g.strokeStyle = this.pColor;
                    this.g.arc(this.xy, this.xy, this.radius - this.lineWidth, pa.s, pa.e, pa.d);
                    this.g.stroke();
                }

                this.g.beginPath();
                this.g.strokeStyle = r ? this.o.fgColor : this.fgColor ;
                this.g.arc(this.xy, this.xy, this.radius - this.lineWidth, a.s, a.e, a.d);
                this.g.stroke();

                this.g.lineWidth = 2;
                this.g.beginPath();
                this.g.strokeStyle = this.o.fgColor;
                this.g.arc( this.xy, this.xy, this.radius - this.lineWidth + 1 + this.lineWidth * 2 / 3, 0, 2 * Math.PI, false);
                this.g.stroke();

                return false;
            }
        }
    });
}
function deleteFile(ref,type){
	ConfirmDeleteDialog(ref, 'Are you sure?',type);
	
}

function compress(ref){
	var fileName = $(ref).closest('tr').find('td').eq(1).html();
	$('#selectedFile').val(fileName);
	$("#dialog").dialog('open');	
}

function convertVideo(){	
debugger
	$('#knob').val(0).trigger('change');
	var fileName =$('#selectedFile').val();
	var format = $('#format').val();	
	$("#dialog").dialog('close');
	var timeoutID;
	(function worker() {
		  $.ajax({
		    url: 'commonAction?action=status', 
		    success: function(data) {
		      $('#result').html(data);
		      $('#knob').val(parseInt(data)).trigger('change');
		      if(parseInt(data) == 100){
		    	  refreshOutputTable();		    	  
		      }
		    },
		    complete: function(data) {
		      // Schedule the next request when the current one's complete
		      console.log(parseInt(data.responseText) == 100);
		      if(parseInt(data.responseText) == 100){		    	  
		    	  clearTimeout(timeoutID,function(){
		    		  refreshOutputTable();
		    	  });
		      } else {
		      	timeoutID = setTimeout(worker, 1000);
		      }
		    }
		  });
		})();
	
	window.location.href = "commonAction?action=convert&fileName="+fileName+"&format="+format;
	refreshOutputTable();
}

function refreshOutputTable(){
	
	$.ajax({url: "commonAction?action=getfiles&type=target", success: function(response){		
		
		var data=JSON.parse(response);
				
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
							'<td><img src="images/trash.png" onclick="deleteFile(this,\'target\');" title="delete file" />'+							
						'</tr>';
			});
		}
			$('#targetfilestableBody').html(data);
			$("tbody tr:even").css("background-color", "#CCCCCC");
	    }});
	
}
function ConfirmDeleteDialog(ref, message,type){
	
    $('<div></div>').appendTo('body')
                    .html('<div><h6>'+message+'?</h6></div>')
                    .dialog({
                        modal: true, title: 'Delete', zIndex: 10000, autoOpen: true,
                        width: 'auto', resizable: false,
                        buttons: {
                            Yes: function () {
                            	$(this).dialog("close");
                            	var fileName = $(ref).closest('tr').find('td').eq(1).html();
                            	$.ajax({url: "commonAction?action=delete&fileName="+fileName+"&type="+type, success: function(response){
                            		$('#message').html(fileName+ 'has been deleted.');
                            		
                            		
                            		if($(ref).closest('tbody > tr').siblings().length == 0){
                            			$(ref).closest('tbody').append(
					                            					'<tr>'+
					                        							'<td colspan="4" align="center">No Flie Available.</td>'+			
					                        						'</tr>');
                            			$(ref).closest('tr').remove();
                            		} else {
                            			$(ref).closest('tr').remove();
                            		}
                            		
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
    
    function home(){
    	window.location = "http://localhost:8080/videocompressor/";
    }
</script>
<!-- Include css styles here -->
<link rel="stylesheet" href="css/jquery-ui.css">
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="logo"><img src="images/logo.jpg" alter="Jecrc University"></div>
	<h3>Compress Your Video <button class="btn" onclick="home();">Home</button></h3>
	
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
	<div class="left">
	<form id="UploadForm" action="UploadFile" method="post"
		enctype="multipart/form-data">
		
			<input type="file" size="60" id="myfile" name="myfile"> <input
				type="submit" value="Upload">
		
		<div id="result"></div>
		<br />
		
		<div id="message"></div>
	</form>
	</div>
		<div class="right">
			<input id="knob" data-width="100%" value="0">
		</div>
	<br>
	<fieldset>
	<legend>Source Files</legend>
		<table >
		<thead>
			<tr><th>Select</th><th>FileName</th><th>Actual Size</th><th>Last Modified Time</th><th>Actions</th></tr>
		</thead>
		<tbody id="srcfilestableBody">
			<tr><td colspan="4" align="center"> No file Available</td></tr>
		</tbody>
		</table>
	</fieldset>
<br><br>
	<fieldset>
	<legend>Compress Files &nbsp;<buttion class="btn" onclick="refreshOutputTable();">Refresh</buttion></legend>
		<table >
		<thead>
			<tr><th>Select</th><th>FileName</th><th>Compress  Size</th><th>Last Modified Time</th><th>Actions</th></tr>
		</thead>
		<tbody id="targetfilestableBody">
			<tr><td colspan="4" align="center"> No file Available</td></tr>
		</tbody>
		</table>
	</fieldset>
</body>
</html>
