<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SWFUpload</title>
<script type="text/javascript" src="js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="js/swfupload/jquery.swfupload.js"></script>

</head>
<body>
文件上传服务测试界面
<div id="image_upload">  
  <div id="upload_btn"></div>
</div>


返回的文件信息
<input id="fileName"  type="text"  size="200" /> <br>
返回的文件路径

<div id="filepath"></div>

<script type="text/javascript">	
var tokenurl = "http://192.168.15.40/fileServer/getToken";
var fileserverurl="http://192.168.15.40/fileServer/";
var STATIC_TOKEN_URL="http://192.168.15.40/fileServer/getToken";
	
$(function(){
	
	function swfFileUpload(myObj){
		
	//服务器端
	$('#' + myObj.divId ).swfupload(
	{
		upload_url:             fileserverurl+"upload?applicationId=" + myObj.applicationId +"&praseth_Method="+myObj.praseth_Method,
		file_size_limit:        "51200",
		file_types:             myObj.file_types,		
		file_upload_limit:      "0",
		flash_url:              "js/swfupload/swfupload.swf",
		button_image_url:       'js/swfupload/upload_my.png',
		button_width:           56,
		button_height:          22,
		button_placeholder:     $('#' +myObj.buttonId )[0],
		post_params:            {"applicationId": myObj.applicationId,"sizeMessage": myObj.sizeMessage == null || myObj.sizeMessage=='' ? '文件过大！' : myObj.sizeMessage}
	}
	);
	
	
	//$('#image_upload').swupbind(function(data){$('#upload_file').val(data.normal);});
	$('#' + myObj.divId).swupbind(
				myObj.callback
	);
}

		//客户端
	var obj={
		file_types:             "*.txt;*.jpeg;*.jpg;*.gif;*.png;*.bmp;*.doc;*.xls;*.wmv;",
		buttonId:     					"upload_btn",
		divId:     							"image_upload",
		applicationId:           6,
		praseth_Method:			0,
		companyId:               31,
		//sizeMessage: "",//"文件大于2M，不能上传！",
		callback: function(data){
			$('#fileName').val(  $('#fileName').val() + "  " + data.codes.fileName + "(" + data.codes.code + ")");
			
			$('#filepath').html("<a class='a1'  href='javascript:void(0)' onclick=downloadf('"+data.codes.code+"')>"+data.codes.fileName+"</a>");
			
			
		}
	}
	swfFileUpload(obj)
});



</script>
</body>
</html>

<%@ include file="encrypt.jsp"%>
