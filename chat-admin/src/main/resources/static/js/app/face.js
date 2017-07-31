/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/face";
var OK = 0; 
var PROCESSING = 1;

var basicEyeBrow = {
		basic_eye_left: 512,
        basic_eye_right: 768,
        basic_eye_left_outer: 1024,
        basic_eye_right_outer: 1280,
        basic_eye_left_inner: 1536,
        basic_eye_right_inner: 1792
        }
$(function(){
	$('#faceAnalysisFrmId').submit(function(e) {
		//init
		$(document).scrollTop(0);
		App.showLoader('Processing', '#faceAnalysisFrmId');
		App.initAjaxRequest('#faceAnalysisFrmId');
		
		var photoFile = $('#photoIptId')[0].files[0];
	    upload(photoFile, function(data){
	    	if (data.code >= 0) {
//	    		$('#imgFullId').prop('src', data.data.link);
	    		initSvg("#imgSvgId", data.data.photo);
	    		getPhoto(data.data.beta.img_uid);
	    	} else {
	    		var errorMsg = $('#faceAnalysisFrmId').find('.form-error');
	    		errorMsg.text(data.msg);
		    	$(errorMsg).show();
		    	App.hideLoader();
	    	}
	    }, function(){
	    	var errorMsg = $('#faceAnalysisFrmId').find('.form-error');
	    	$(errorMsg).text("Server error");
	    	$(errorMsg).show();
	    	App.hideLoader();
	    })
		
		return false;
	});
	
	//faceId1, faceId2
	$('#faceMatchFrmId').submit(function(e) {
		//init
		$(document).scrollTop(0);
		App.showLoader('Processing', '#faceMatchFrmId');
		App.initAjaxRequest('#faceMatchFrmId');
		
		var sourceFile = $('#sourceFaceIptId')[0].files[0];
		var targetFile = $('#targetFaceIptId')[0].files[0];
		var sourceFaceId = null;
		var targetFaceId = null;
	    upload(sourceFile, function(data){
	    	if (data.code >= 0) {
	    		$('#sourceImgId').prop('src', data.data.link);
	    		sourceFaceId = getFaceId(data.data.beta.img_uid);
	    		upload(targetFile, function(data){
	    			if (data.code >= 0) {
	    				$('#targetImgId').prop('src', data.data.link);
	    	    		targetFaceId = getFaceId(data.data.beta.img_uid);
	    	    		match(sourceFaceId, targetFaceId);
	    			}
	    		}, function(){})
	    	} else {
	    		var errorMsg = $('#faceMatchFrmId').find('.form-error');
	    		errorMsg.text(data.msg);
		    	$(errorMsg).show();
	    	}
	    }, function(){})
		
		return false;
	});
	
});

function getFacePhoto(faceId) {
	var data = '';
	var url = URI + "/face/" + faceId;
	App.ajaxRequest('GET', url, data, function(resp){
		if (resp.code == OK) {
			$('#faceDivId').append("<img src=" + resp.data.link+ " />");
		}
	})
}

function getPhoto(photoId) {
	var data = '';
	var url = URI + "/photo/" + photoId;
	App.ajaxRequest('GET', url, data, function(resp){
		if (resp.code >= 0) {
			if (resp.data.int_response == OK) {
				var faces = resp.data.faces;
				if (faces) {
					$('#faceDivId').text('');
					for (var i = 0; i < faces.length; i++) {
						drawOnFace(faces[i]);
						getFacePhoto(faces[i].uid);
					}
				}
				App.hideLoader();
			} else if (resp.data.int_response == PROCESSING) {
				setTimeout(function(){
					getPhoto(photoId);
				}, 500);
			} else {
				var errorMsg = $('#faceAnalysisFrmId').find('.form-error');
		    	$(errorMsg).text(resp.data.string_response);
		    	$(errorMsg).show();
		    	App.hideLoader();
			}
		} else {
			var errorMsg = $('#faceAnalysisFrmId').find('.form-error');
	    	$(errorMsg).text("Server error");
	    	$(errorMsg).show();
	    	App.hideLoader();
		}
	});
}

function getFaceId(photoId) {
	var data = '';
	var url = URI + "/photo/" + photoId;
	var faceId = null;
	App.sajaxRequest('GET', url, data, function(resp){
		if (resp.code >= 0) {
			if (resp.data.int_response == OK) {
				var faces = resp.data.faces;
				if (faces && faces.length >= 1) {
					faceId = faces[0].uid;
				}
				App.hideLoader();
			} else if (resp.data.int_response == PROCESSING) {
				setTimeout(function(){
					faceId = getFaceId(photoId);
				}, 500);
			} else {
				var errorMsg = $('#faceMatchFrmId').find('.form-error');
		    	$(errorMsg).text(resp.data.string_response);
		    	$(errorMsg).show();
			}
		} else {
			var errorMsg = $('#faceMatchFrmId').find('.form-error');
	    	$(errorMsg).text("Server error");
	    	$(errorMsg).show();
		}
	});
	return faceId;
}

function upload(file, successCallback, errorCallback) {
	var data = new FormData();
	data.append('photo', file);
	$.ajax({
        url: URI + "/photo",
        type: 'POST',
        data: data,
        cache: false,
        dataType: 'json',
        processData: false, // Don't process the files
        contentType: false, // Set content type to false as jQuery will tell the server its a query string request
        success: successCallback,
        error: errorCallback,
	})
}

function match(sourceFaceId, targetFaceId) {
	if (sourceFaceId && targetFaceId) {
		var data = {'sourceFaceId': sourceFaceId, 'targetFaceId': targetFaceId};
		var url = URI + "/match";
		App.ajaxRequest('POST', url, data, function(resp){
			if (resp.data) {
				if (resp.data.int_response == OK) {
					getMatchResult(sourceFaceId, targetFaceId, resp.data.recognize_uid);
				}
			}
		});
	}
}

function getMatchResult(sourceFaceId, targetFaceId, matchId) {
	var data = '';
	var url = URI + "/match/" + matchId + "?sourceFaceId=" + sourceFaceId + "&targetFaceId=" + targetFaceId;
	App.ajaxRequest('GET', url, data, function(resp){
		if (resp.code >= 0) {
			var status = resp.data.status;
			if (status == OK) {
				$('#confidenceSpanId').text(resp.data.confidence);
				$('#isMatchSpanId').text(resp.data.isMatched);
			} else if (status == PROCESSING) {
				setTimeout(function(){
					getMatchResult(sourceFaceId, targetFaceId, matchId);
				}, 500);
			}
		}
	});
}

var gcc, container;

function initSvg(ele, photo) {
	var map = d3.select(ele).attr("width", photo.width).attr("height",
			photo.height).on("dblclick", dblclickMap);

	container = map.append("g");

	// Create the drag and drop behavior to set for the objects crated
	var drag = d3.behavior.drag().origin(function(d) {
		return d;
	}).on("dragstart", dragstarted).on("drag", dragged).on("dragend", dragended);

	// Get the background image from the server and set the viewport size
	// .attr("xlink:href","http://wafi.iit.cnr.it/webvis/tmp/tiger.svg")
	// .attr("xlink:href","{{static-domain}}/map/mapdemo.svg")
	var mapImage = container.append("image")
			.attr('id', "mapimg")
			.attr("xlink:href", photo.link).attr("width", photo.width)
			.attr("height", photo.height)
			.attr("transform", "translate(0,0)")
			.classed('draggable', true)
			.on("dblclick", dblclickImg)
			.on("mouseup", mouseupImg)
			.on("mousemove", mousemoveImg).call(drag);

	gcc = container.append("g")
		.attr('id', "gcc")
		.attr("width", photo.width)
		.attr("height", photo.height).attr("transform", "translate(0,0)");
	
	var zoom = d3.behavior.zoom().scaleExtent([ 0.2, 10 ]).on(
			'zoom', zoomed);
	container.call(zoom).on("mousedown.zoom", null).on("dblclick.zoom", null);
	
}

//=================================svg functions================================
function zoomed() {
	container.attr("transform", "translate(" + d3.event.translate
			+ ")scale(" + d3.event.scale + ")");
}

function dblclickMap(d) {
	var coordinates = [ 0, 0 ];
	coordinates = d3.mouse(this);
	var mx = coordinates[0];
	var my = coordinates[1];
	console.log("dblclick on map at: [x:" + mx + ", y:" + my + "]");
}

//flag tracking drag.
var drg = false;
// Called when drag event starts. It stop the propagation of the click event
function dragstarted(d) {
	console.log("================dragstarted==============");
	drg = false;
	d3.event.sourceEvent.stopPropagation();
}

// Called when the drag event occurs (object should be moved)
function dragged(d) {
	d.x = d3.event.x;
	d.y = d3.event.y;
	// console.log("drag: [x:"+d.x+", y:"+d.y+"]");
	// Translate the object on the actual moved point
	d3.select(this).attr({
		transform : "translate(" + d.x + "," + d.y + ")"
	});
	gcc.attr({
		transform : "translate(" + d.x + "," + d.y + ")"
	});

	drg = true;

}

function dragended(d) {
	// d3.event.sourceEvent.stopPropagation();
	console.log("================dragended==============");

	// If has drag, update position list circles.
	if (drg) {

	}
	drg = false;
}

var line = d3.svg.line().x(function(d) {
	return d[0];
}).y(function(d) {
	return d[1];
});

function dblclickImg() {
}

function mousemoveImg() {}

function mouseupImg() {}

//==========================app functions ==============================
function drawOnFace(face) {
	if (!face || !face.points) {
		return;
	}
	for (var i = 0; i < face.points.length; i++) {
		var point = face.points[i];
		if (point.name.indexOf("eyebrow") >= 0) {
			console.log(point.name);
			if (point.name.indexOf("basic") >= 0) {
				drawPoint(point.x, point.y, "red", point.name);
			} else {
				drawPoint(point.x, point.y, "white", point.name);
			}
		}
	}
	
	console.log("x=" + face.x + ", y=" + face.y + ", width=" + face.width + ", height=" + face.height + ", angle=" + face.angle);
	face.width = face.width/2;face.height = face.height/2;
	var point = [face.x, face.y];
	var point1 = [face.x - face.width, face.y - face.height]; point1 = rotatePoint(-face.angle, point, point1);
	var point2 = [face.x - face.width, face.y + face.height]; point2 = rotatePoint(-face.angle, point, point2);
	var point3 = [face.x + face.width, face.y + face.height]; point3 = rotatePoint(-face.angle, point, point3);
	var point4 = [face.x + face.width, face.y - face.height]; point4 = rotatePoint(-face.angle, point, point4);
	drawPoint(point1[0], point1[1], "black");
	drawPoint(point2[0], point2[1], "black");
	drawPoint(point3[0], point3[1], "black");
	drawPoint(point4[0], point4[1], "black");
	drawLine(point1[0], point1[1], point2[0], point2[1], "blue");
	drawLine(point2[0], point2[1], point3[0], point3[1], "blue");
	drawLine(point3[0], point3[1], point4[0], point4[1], "blue");
	drawLine(point4[0], point4[1], point1[0], point1[1], "blue");
}

function drawPoint(x, y, color, title) {
	if (!gcc) {
		return;
	}
	gcc.append("circle").attr("cx", x).attr("cy", y).attr("r", 1.5).on('mouseover', function(){console.log(title)})
			.style("fill", color);
}

function drawLine(xb, yb, xe, ye, color) {
	d3.select('#gcc').append('path').attr('d', line([[xb, yb], [xe, ye]]))
		.style({ 
			'stroke' : color,
			'stroke-width' : 1
		});
}

function rotatePoint(angle, pointRoot, point) {
	var point1 = translate([-pointRoot[0], -pointRoot[1]], point);
	var point2 = rotateO(angle, point1);
	var point3 = translate(pointRoot, point2);
	return point3;
}

function rotateO(angle, point) {
	var radian = toRadian(angle);
	var matrix = [[Math.cos(radian), Math.sin(radian)], [-Math.sin(radian), Math.cos(radian)]];
	var newPoint = [matrix[0][0] * point[0] + matrix[1][0] * point[1], matrix[0][1] * point[0] + matrix[1][1] * point[1]];
	return newPoint;
}

function translate(vector, point) {
	return [point[0] + vector[0], point[1] + vector[1]];
}

function toRadian(angle) {
	return angle * Math.PI / 180;
}