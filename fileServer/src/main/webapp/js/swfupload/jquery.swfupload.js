/*
 * SWFUpload jQuery Plugin v1.0.0
 *
 * Copyright (c) 2009 Adam Royle
 * Licensed under the MIT license.
 * Compile by aFen @2011
 * http://afen.it
 */
 
 var paramMessage =[];
 var fdiframe=null;
 
 var bid = bid||{};
 bid.file={};
 bid.downloadurl="http://192.168.15.40/fileServer/downloadFile";
 
function downloadf(param)
{
$.ajax({
        type: "get",
        async:false,
        dataType: "text",
        url: STATIC_TOKEN_URL+"?"+Math.random(),
        success: function(token){
          var downloadurl = bid.downloadurl+"?token="+token+"&code="+param;
        	
    if(fdiframe==null)
    {
    fdiframe = document.createElement("iframe");
    fdiframe.src = downloadurl;
    fdiframe.style.display = "none";
    document.body.appendChild(fdiframe);
    	
    }else
    {
    	fdiframe.src = downloadurl;
    }   	

   }
	});
	
	
};


var Url = {
		// public method for url encoding
		encode : function(string) {
			return escape(this._utf8_encode(string));
		},

		// public method for url decoding
		decode : function(string) {
			return this._utf8_decode(unescape(string));
		},

		// private method for UTF-8 encoding
		_utf8_encode : function(string) {
			string = string.replace(/\r\n/g, "\n");
			var utftext = "";

			for ( var n = 0; n < string.length; n++) {

				var c = string.charCodeAt(n);

				if (c < 128) {
					utftext += String.fromCharCode(c);
				} else if ((c > 127) && (c < 2048)) {
					utftext += String.fromCharCode((c >> 6) | 192);
					utftext += String.fromCharCode((c & 63) | 128);
				} else {
					utftext += String.fromCharCode((c >> 12) | 224);
					utftext += String.fromCharCode(((c >> 6) & 63) | 128);
					utftext += String.fromCharCode((c & 63) | 128);
				}

			}

			return utftext;
		},

		// private method for UTF-8 decoding
		_utf8_decode : function(utftext) {
			var string = "";
			var i = 0;
			var c = c1 = c2 = 0;

			while (i < utftext.length) {

				c = utftext.charCodeAt(i);

				if (c < 128) {
					
					if(c===43)
					{
						string += String.fromCharCode(' ');
					}else
					{
						string += String.fromCharCode(c);
					}
					
					i++;
				} else if ((c > 191) && (c < 224)) {
					c2 = utftext.charCodeAt(i + 1);
					string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
					i += 2;
				} else {
					c2 = utftext.charCodeAt(i + 1);
					c3 = utftext.charCodeAt(i + 2);
					string += String.fromCharCode(((c & 15) << 12)
							| ((c2 & 63) << 6) | (c3 & 63));
					i += 3;
				}

			}

			return string;
		}

	}
;


(function($){
	
  function delayupload(obj)
  {
  		obj.swfupload('startUpload');
  }
  
	var defaultHandlers = ['swfupload_loaded_handler','file_queued_handler','file_queue_error_handler','file_dialog_start_handler','file_dialog_complete_handler','upload_start_handler','upload_progress_handler','upload_error_handler','upload_success_handler','upload_complete_handler','queue_complete_handler'];
	var additionalHandlers = [];

	
	$.fn.swfupload = function(){
		var args = $.makeArray(arguments);
		
		
		if(args != 'startUpload'){
			paramMessage = args[0].post_params;
			
		}
		
		return this.each(function(){
			var swfu;
			if (args.length == 1 && typeof(args[0]) == 'object') {
				swfu = $(this).data('__swfu');
				if (!swfu) {
					var settings = args[0];
					var $magicUploadControl = $(this);
					var handlers = [];
					$.merge(handlers, defaultHandlers);
					$.merge(handlers, additionalHandlers);
					$.each(handlers, function(i, v){
						var eventName = v.replace(/_handler$/, '').replace(/_([a-z])/g, function(){ return arguments[1].toUpperCase(); });
						settings[v] = function() {
							var event = $.Event(eventName);
							$magicUploadControl.trigger(event, $.makeArray(arguments));
							return !event.isDefaultPrevented();
						};
					});
					
					$(this).data('__swfu', new SWFUpload(settings));
					
					
				}
			} else if (args.length > 0 && typeof(args[0]) == 'string') {
				var methodName = args.shift();
				swfu = $(this).data('__swfu');
				if (swfu && swfu[methodName]) {
					swfu[methodName].apply(swfu, args);
				}
			}
		});
	};
	
	$.swfupload = {
		additionalHandlers: function() {
			if (arguments.length === 0) {
				return additionalHandlers.slice();
			} else {
				$(arguments).each(function(i, v){
					$.merge(additionalHandlers, $.makeArray(v));
				});
			}
		},
		defaultHandlers: function() {
			return defaultHandlers.slice();
		},
		getInstance: function(el) {
			return $(el).data('__swfu');
		}
	};

	$.fn.swupbind = function(callback){
		$(this)
		.bind('swfuploadLoaded', function(event){
		})
		.bind('fileQueued', function(event, file){
			//alert($("#swfupload_loading").length);
		//	console.log($("#swfupload_loading"));
			if($("#swfupload_loading").length==0){
			$("#swfupload_loading").remove();
			$('<div id="swfupload_loading" style="z-index:1; padding:1px; left:0; top:0; color:#03C; background:#FFF; width:200px; border: 1px solid #03C; height: 15px;"><div id="swfupload_complete" style="float:left;background: #2F63FF; z-index:2; color: #fff; font-size: 12px;width:0px; text-align: center; height: 15px; overflow: hidden;">上传中...</div></div>').appendTo(this);
			
			}else{
			$(this).data('__swfu').cancelUpload(file.id,false);
			alert("请等待文件上传完毕再进行文件上传！");
			}
		})
		/**
		.bind('fileQueueError', function(event, file, errorCode, message){
			$('<div id="swfupload_loading" style="z-index:1; padding:1px; left:0; top:0; color:#03C; background:#FFF; width:200px; border: 1px solid #03C; height: 15px;"><div id="swfupload_complete" style="float:left;background: #2F63FF; z-index:2; color: #fff; font-size: 12px;width:200px; text-align: center; height: 15px; overflow: hidden;">uploading...</div></div>').appendTo(this);
			$('#swfupload_complete').html("Error:"+errorCode+" "+message);
		})**/
		.bind('fileDialogStart', function(event){
			
			
			$.ajax({
        type: "get",
        async:false,
        dataType: "text",
        url: STATIC_TOKEN_URL+"?"+Math.random(),
        success: function(token){
        submittoken = token;
       }
	});
	
  

})
		.bind('fileDialogComplete', function(event, numFilesSelected, numFilesQueued){
			
			if(submittoken!=null)
			{
				$(this).swfupload('startUpload');
			}else
			{
				window.setTimeout(delayupload, 500, $(this));
			}
			
		})
		.bind('uploadStart', function(event, file){
			
			$(this).data('__swfu').addPostParam("token",submittoken);
			submittoken=null;
			
			//console.log($(this).data('__swfu').addPostParam);
			
			$(this).data('__swfu').addPostParam("fileSize",file.size);
			$('#swfupload_complete').html("上传中...");
		})
		.bind('uploadProgress', function(event, file, bytesLoaded,bytesTotal){
			$('#swfupload_complete').width(200*bytesLoaded/bytesTotal);
		})
		.bind('uploadSuccess', function(event, file, serverData){
			//alert(serverData);
			serverData=Url.decode(serverData);
			eval("loaddata="+serverData);
			if(!loaddata.error){
				//$("#"+objinput).val(loaddata.normal);
				bid.file.size =file.size; 
				callback(loaddata);
				$("#swfupload_loading").remove();
			}else{
				$('#swfupload_complete').html(loaddata.error);
			}
		})
		.bind('uploadComplete', function(event, file){
		
			//post_params
			
			$(this).swfupload('startUpload');
		})
		.bind('uploadError', function(event, file, errorCode, message){
			//$('#swfupload_complete').html("error:"+errorCode+" "+message);
				try {

		//	alert(errorCode);
		var ctx = this.customSettings.scope_handler;
		// 设置了移除为false的时候修改上传进度和状态
					if (!ctx.autoRemove) {
						if (ctx.autoRemoveStoped) {
							var currentLine = ctx.getCurrentLine(file);
							var file_id = currentLine.attr("fileId");
							ctx.cancelFileUpload(file_id);
							currentLine.remove();
						} else {
							switch (Number(errorCode)) {
								case -200 : {
									ctx.msgKey = ctx.Constant.HTTP_ERROR;
								}
									break;
								case -210 : {
									ctx.msgKey = ctx.Constant.MISSING_UPLOAD_URL;
								}
									break;
								case -220 : {
									alert("上传失败，请检查网络连接！");
									break;
									//ctx.msgKey = ctx.Constant.IO_ERROR;
								}
									break;
								case -230 : {
									ctx.msgKey = ctx.Constant.SECURITY_ERROR;
								}
									break;
								case -240 : {
									ctx.msgKey = ctx.Constant.UPLOAD_LIMIT_EXCEEDED;
								}
									break;
								case -250 : {
									ctx.msgKey = ctx.Constant.UPLOAD_FAILED;
								}
									break;
								case -260 : {
									ctx.msgKey = ctx.Constant.SPECIFIED_FILE_ID_NOT_FOUND;
								}
									break;
								case -270 : {
									ctx.msgKey = ctx.Constant.FILE_VALIDATION_FAILED;
								}
									break;
								case -280 : {
									ctx.msgKey = ctx.Constant.FILE_CANCELLED;
								}
									break;
								case -290 : {
									ctx.msgKey = ctx.Constant.UPLOAD_STOPPED;
								}
									break;
								case -300 : {
									ctx.msgKey = ctx.Constant.RESIZE;
								}
									break;
							}
						}
					}
				} catch (e) {
					alert("文件上传失败，请检查网络是否连接正常!");
				}

		})
		.bind('fileQueueError', function(event, file, errorCode, message){
			//alert(errorCode);
				 //console.log(file);
				 // console.log(paramMessage);
				 try {
					switch (errorCode) {
						case -100 :
							alert('待上传文件列表数量超限，不能选择！');
							break;
						case -110 :
							alert(paramMessage.sizeMessage);
							break;
						case -120 :
							alert('该文件大小为0，不能选择！');
							break;
						case -130 :
							alert('该文件类型不可以上传！');
							break;
                                                case -140 :
                                                        alert('未知错误！');
                                                        break;
						case -220 :
							alert('文件上传失败，请检查网络是否连接正常!');
							break;
					}
				} catch (ex) {
					this.debug(ex);
				}
		});
	}
	
})(jQuery);

/*
<script type="text/javascript" src="js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="js/swfupload/jquery.swfupload.js"></script>
	$('#picupload').swfupload({
		upload_url: "upload.php",
		file_size_limit : "2048", //2MB
		file_types : "*.jpeg;*.jpg;*.gif;*.png;",
		file_types_description : "All Files",
		file_upload_limit : "0",
		flash_url : "js/swfupload/swfupload.swf",
		button_image_url : 'js/swfupload/XPButtonUploadText_61x22.png',
		button_width : 61,
		button_height : 22,
		button_placeholder : $('#picload')[0],
		post_params: {"PHPSESSID" : "","adminid":<?php echo($sessions['admin_id']) ?>}
	})
		$('#picupload').swupbind(function(data){$('#pic').val(data);})


		<div id="picupload">
			<input name="pic" type="text" class="input" id="pic" value="" size="30" />
			<div id="picload" style="width:61px;height:22px;"></div>(max 2M jpg gif png)
		</div>
*/
