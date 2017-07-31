/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/chat";
var p
$(function(){
	$('#addFloorFrmId').submit(function(e) {
		//init
		$(document).scrollTop(0);
		App.showLoader('Processing', '#addFloorFrmId');
		App.initAjaxRequest('#addFloorFrmId');
		
		var photoFile = $('#photoIptId')[0].files;
	    
	    App.ajaxUploadPhotoRequest(App.photoType.MAP, photoFile, function(data){
	    	if (data.code >= 0) {
	    		var atlasId = $('#atlasIdAddIptId').val();
				var eventId = $('#eventAddSltId').val();
				var name = $('#nameAddIptId').val();
				var number = $('#numberAddIptId').val();
				var actualWidth = $('#widthAddIptId').val();
				var actualHeight = $('#heightAddIptId').val();
				var rotate = $('#rotateAddIptId').val();
				var	isActive = $('#activeAddIptId').prop("checked");
				var photoId = data.data.length > 0 ? data.data[0] : 0;
				var data = {'atlasId': atlasId, 'eventId': eventId, 'number': number, 
						'name': name, 'photo': photoId, 'actualWidth': actualWidth, 
						'actualHeight': actualHeight, 'rotate': rotate, 'active': isActive};
				var url = URI;
				App.ajaxRequest('POST', url, data, function(resp){
					App.callbackAjaxRequest('#addFloorFrmId', resp);
				})
	    	} else {
	    		var errorMsg = $('#addFloorFrmId').find('.form-error');
	    		errorMsg.text(data.msg);
		    	$(errorMsg).show();
		    	App.hideLoader();
	    	}
	    }, function(){
	    	var errorMsg = $('#addFloorFrmId').find('.form-error');
	    	$(errorMsg).text("Server error");
	    	$(errorMsg).show();
	    	App.hideLoader();
	    })
		
		return false;
	});
});

var stompClient = null;

function setConnected(connected) {
//    $("#connect").prop("disabled", connected);
//    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/api');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/message', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});