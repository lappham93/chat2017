/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/speaker";
Photo.type = App.photoType.USER;
Photo.url = URI;
$(function(){
	$('#addSpeakerFrmId').submit(function(e) {
		//init
		$(document).scrollTop(0);
		App.showLoader('Processing', '#addSpeakerFrmId');
		App.initAjaxRequest('#addSpeakerFrmId');
		
		var photoFile = $('#photoIptId')[0].files;
		
		if (photoFile.length > 0) {
		    App.ajaxUploadPhotoRequest(App.photoType.USER, photoFile, function(data){
		    	if (data.code >= 0) {
					addSpeaker(data.data.length > 0 ? data.data[0] : 0);
		    	} else {
		    		var errorMsg = $('#addSpeakerFrmId').find('.form-error');
		    		errorMsg.text(data.msg);
			    	$(errorMsg).show();
			    	App.hideLoader();
		    	}
		    }, function(){
		    	var errorMsg = $('#addSpeakerFrmId').find('.form-error');
		    	$(errorMsg).text("Server error");
		    	$(errorMsg).show();
		    	App.hideLoader();
		    })
		} else {
			addSpeaker(0);
		}
		
		return false;
	});
	
	$('#editSpeakerFrmId').submit(function(e) {
		$('#editSpeakerMdlId').scrollTop(0);
		//init
		App.showLoader('Processing', '#editSpeakerFrmId');
		App.initAjaxRequest('#editSpeakerFrmId');
		
		var id = $('#idEditIptId').val();
		var fname = $('#fnameEditIptId').val();
		var lname = $('#lnameEditIptId').val();
		var job = $('#jobEditIptId').val();
		var sumStory = $('#sumStoryEditIptId').val();
		var	isActive = $('#activeEditIptId').prop("checked");
		var url = URI + "/" + id;
		var data = {'firstName': fname, 'lastName': lname, 'job': job, 'sumStory': sumStory, 'active': isActive,
				'updateType': App.updateType.INFO};
		App.ajaxRequest('PUT', url, data, function(resp) {
			App.callbackAjaxRequest('#editSpeakerFrmId', resp);
		})
		
		return false;
	});
	
	$('#deleteSpeakerFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteSpeakerFrmId');
		App.initAjaxRequest('#deleteSpeakerFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteSpeakerFrmId', resp);
		})
		
		return false;
	});
	
});

function openSpeakerEditModal(id) {
	var modal = $('#editSpeakerMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editSpeakerMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	var url = URI + "/" + id;
	App.ajaxRequest('GET', url, "", function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#fnameEditIptId').val(resp.data.firstName);
			$('#lnameEditIptId').val(resp.data.lastName);
			$('#jobEditIptId').val(resp.data.job);
			$('#sumStoryEditIptId').val(resp.data.sumStory);
			$('#activeEditIptId').prop('checked', resp.data.active);
			//animate
			App.animateSwitch('#activeEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitEditSpeaker() {
	$('#editSpeakerBtnId').trigger('click');
}

function openSpeakerDeleteModal(id) {
	var modal = $('#deleteSpeakerMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete speaker will remove permanently speaker out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteSpeaker() {
	$('#deleteSpeakerBtnId').trigger('click');
}

function addSpeaker(photoId) {
	var fname = $('#fnameAddIptId').val();
	var lname = $('#lnameAddIptId').val();
	var job = $('#jobAddIptId').val();
	var sumStory = $('#sumStoryAddIptId').val();
	var	isActive = $('#activeAddIptId').prop("checked");
	var data = {'firstName': fname, 'lastName': lname, 'job': job, 'sumStory': sumStory,
			'active': isActive, 'photo': photoId};
	var url = URI;
	App.ajaxRequest('POST', url, data, function(resp){
		App.callbackAjaxRequest('#addSpeakerFrmId', resp);
	});
}