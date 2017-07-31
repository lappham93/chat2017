/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/talk";
Photo.type = App.photoType.USER;
Photo.url = URI;
$(function(){
	$('#addTalkFrmId').submit(function(e) {
		//init
		$(document).scrollTop(0);
		App.showLoader('Processing', '#addTalkFrmId');
		App.initAjaxRequest('#addTalkFrmId');
		
		var photoFile = $('#photoIptId')[0].files;
		
		if (photoFile.length > 0) {
		    App.ajaxUploadPhotoRequest(App.photoType.USER, photoFile, function(data){
		    	if (data.code >= 0) {
					addTalk(data.data.length > 0 ? data.data[0] : 0);
		    	} else {
		    		var errorMsg = $('#addTalkFrmId').find('.form-error');
		    		errorMsg.text(data.msg);
			    	$(errorMsg).show();
			    	App.hideLoader();
		    	}
		    }, function(){
		    	var errorMsg = $('#addTalkFrmId').find('.form-error');
		    	$(errorMsg).text("Server error");
		    	$(errorMsg).show();
		    	App.hideLoader();
		    })
		} else {
			addTalk(0);
		}
		
		return false;
	});
	
	$('#editTalkFrmId').submit(function(e) {
		$('#editTalkMdlId').scrollTop(0);
		//init
		App.showLoader('Processing', '#editTalkFrmId');
		App.initAjaxRequest('#editTalkFrmId');
		
		var id = $('#idEditIptId').val();
		var eventId = $('#eventEditSltId').val();
		var floorId = $('#floorEditSltId').val();
		var speakerId = $('#speakerEditSltId').val();
		var title = $('#titleEditIptId').val();
		var desc = $('#descEditIptId').val();
		var startTime = merDateAndTime($('#startDateEditIptId').val(), $('#startTimeEditIptId').val());
		var endTime = merDateAndTime($('#endDateEditIptId').val(), $('#endTimeEditIptId').val());
		var	isActive = $('#activeEditIptId').prop("checked");
		var url = URI + "/" + id;
		var data = {'speakerId': speakerId, 'eventId': eventId, 'floorId': floorId,
				'name': title, 'title': title, 'desc': desc, 'startTime': startTime, 'endTime': endTime ,'active': isActive,
				'updateType': App.updateType.INFO};
		App.ajaxRequest('PUT', url, data, function(resp) {
			App.callbackAjaxRequest('#editTalkFrmId', resp);
		})
		
		return false;
	});
	
	$('#deleteTalkFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteTalkFrmId');
		App.initAjaxRequest('#deleteTalkFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteTalkFrmId', resp);
		})
		
		return false;
	});
	
	
	$('#eventAddSltId').change(function(){
		App.renderFloor($(this).val(), $('#floorAddSltId'));
	});
	
	$('#eventEditSltId').change(function(){
		App.renderFloor($(this).val(), $('#floorEditSltId'));
	});
	
	$('#eventSearchSltId').change(function(){
		var event = $(this).val().trim();
		var servletPath = event != '0' ? "/talk?event=" + event : "/talk"; 
		App.redirect(servletPath);
	});
});

function openTalkEditModal(id) {
	var modal = $('#editTalkMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editTalkMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	var url = URI + "/" + id;
	App.ajaxRequest('GET', url, "", function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#speakerEditSltId').val(resp.data.speaker.id);
			$('#titleEditIptId').val(resp.data.title);
			$('#descEditIptId').val(resp.data.desc);
			var eventId = resp.data.event.id;
			var floorId = resp.data.floor.id
			$('#eventEditSltId').val(eventId);
			App.renderFloor(eventId, $('#floorEditSltId'), floorId);
			$('#startDateEditIptId').val(App.getValidDateFormat(resp.data.startTime));
			$('#endDateEditIptId').val(App.getValidDateFormat(resp.data.endTime));
			$('#startTimeEditIptId').val(App.getValidTimeFormat(resp.data.startTime));
			$('#endTimeEditIptId').val(App.getValidTimeFormat(resp.data.endTime));
			$('#activeEditIptId').prop('checked', resp.data.active);
			//animate
			App.animateSelect('#eventEditSltId');
			App.animateSelect('#speakerEditSltId');
			App.animateSelect('#floorEditSltId');
			App.animateSwitch('#activeEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitEditTalk() {
	$('#editTalkBtnId').trigger('click');
}

function openTalkDeleteModal(id) {
	var modal = $('#deleteTalkMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete talk will remove permanently talk out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteTalk() {
	$('#deleteTalkBtnId').trigger('click');
}

function addTalk(photoId) {
	var eventId = $('#eventAddSltId').val();
	var floorId = $('#floorAddSltId').val();
	var speakerId = $('#speakerAddSltId').val();
	var title = $('#titleAddIptId').val();
	var desc = $('#descAddIptId').val();
	var startTime = merDateAndTime($('#startDateAddIptId').val(), $('#startTimeAddIptId').val());
	var endTime = merDateAndTime($('#endDateAddIptId').val(), $('#endTimeAddIptId').val());
	var	isActive = $('#activeAddIptId').prop("checked");
	var url = URI;
	var data = {'speakerId': speakerId, 'eventId': eventId, 'floorId': floorId,
			'name': title, 'title': title, 'desc': desc, 'startTime': startTime, 'endTime': endTime ,'active': isActive,
			'photo': photoId};
	App.ajaxRequest('POST', url, data, function(resp){
		App.callbackAjaxRequest('#addTalkFrmId', resp);
	});
}

function merDateAndTime(date, time) {
	return date + " " + time;
}