/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/banner";
Photo.type = App.photoType.BANNER;
Photo.url = URI;
var bannerType = {WEB: 1, EVENT: 2, BOOTH: 3, WELCOME: 4};

$(function(){
	$('#addBannerFrmId').submit(function(e) {
		$(document).scrollTop(0);
		App.showLoader("Processing", "#addBannerFrmId");
		//init
		App.initAjaxRequest('#addBannerFrmId');
		
		var photoFile = $('#photoIptId')[0].files;
	    
	    App.ajaxUploadPhotoRequest(App.photoType.BANNER, photoFile, function(data){
	    	if (data.code >= 0) {
	    		var objectType = $('#objectTypeAddSltId').val();
	    		var objectIdEvent = $('#objectIdEventAddSltId').val();
	    		var objectIdBooth = $('#objectIdBoothAddSltId').val();
				var type = $('#typeAddSltId').val();
				var eventId = $('#eventAddSltId').val();
				var boothId = $('#boothAddSltId').val();
				var msg = $('#msgAddIptId').val();
				var info = $('#infoAddIptId').val();
				var	isActive = $('#activeAddIptId').prop("checked");
				var displayOrder = $('#disOrderAddIptId').val();
				var photoId = data.data.length > 0 ? data.data[0] : 0;
				console.log("photoId = " + photoId);
				
				var objectRefId = objectType == App.objectType.EVENT ? objectIdEvent : 
					objectType == App.objectType.BOOTH ? objectIdBooth : 0;
				var data = {'objectRefType': objectType, 'objectRefId': objectRefId, 'type': type, 'msg': msg, 'info': info, 
						'active': isActive, 'photo': photoId, 'displayOrder': displayOrder, 'eventId': eventId, 'boothId': boothId};
				var url = URI + "";
				App.ajaxRequest('POST', url, data, function(resp){
					App.callbackAjaxRequest('#addBannerFrmId', resp);
					App.hideLoader();
				})
	    	} else {
	    		var errorMsg = $('#addBannerFrmId').find('.form-error');
	    		errorMsg.text(data.msg);
		    	$(errorMsg).show();
		    	App.hideLoader();
	    	}
	    }, function(){
	    	var errorMsg = $('#addBannerFrmId').find('.form-error');
	    	$(errorMsg).text("Server error");
	    	$(errorMsg).show();
	    	App.hideLoader();
	    })
		
		return false;
	});
	
	$('#editBannerFrmId').submit(function(e) {
		$('#editBannerMdlId').scrollTop(0);
		App.showLoader("Processing", "#editBannerFrmId");
		//init
		App.initAjaxRequest('#editBannerFrmId');
		
		var id = $('#idEditIptId').val();
		var type = $('#typeEditSltId').val();
		var objectType = $('#objectTypeEditSltId').val();
		var objectIdEvent = $('#objectIdEventEditSltId').val();
		var objectIdBooth = $('#objectIdBoothEditSltId').val();
		var msg = $('#msgEditIptId').val();
		var info = $('#infoEditIptId').val();
		var	isActive = $('#activeEditIptId').prop("checked");
		var displayOrder = $('#disOrderEditIptId').val();
		
		var objectRefId = objectType == App.objectType.EVENT ? objectIdEvent : 
			objectType == App.objectType.BOOTH ? objectIdBooth : 0;
		var data = {'objectRefType': objectType, 'objectRefId': objectRefId, 'type': type, 'msg': msg, 'info': info, 'active': isActive, 'updateType': App.updateType.INFO, 'displayOrder': displayOrder};
		var url = URI + "/" + id;
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#editBannerFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
	$('#deleteBannerFrmId').submit(function(e) {
		App.showLoader("Processing", "#deleteBannerFrmId");
		//init
		App.initAjaxRequest('#deleteBannerFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteBannerFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
	$('#objectTypeAddSltId').change(function(){
		var value = $(this).val();
		if (value == App.objectType.EVENT) {
			showRequired($('#objectIdEventDivId'), $('#objectIdEventAddSltId'));
			hideRequired($('#objectIdBoothDivId'), $('#objectIdBoothAddSltId'));
		} else if (value == App.objectType.BOOTH) {
			showRequired($('#objectIdEventDivId'), $('#objectIdEventAddSltId'));
			showRequired($('#objectIdBoothDivId'), $('#objectIdBoothAddSltId'));
			if ($('#objectIdEventAddSltId').val()) {
				App.renderBooth(true, $('#objectIdEventAddSltId').val(), $('#objectIdBoothAddSltId'));
			}
		} else {
			hideRequired($('#objectIdEventDivId'), $('#objectIdEventAddSltId'));
			hideRequired($('#objectIdBoothDivId'), $('#objectIdBoothAddSltId'));
		}
	});
	
	$('#objectIdEventAddSltId').change(function(){
		App.renderBooth(true, $(this).val(), $('#objectIdBoothAddSltId'));
	});
	
	$('#typeAddSltId').change(function(){
		var value = $(this).val();
		if (value == bannerType.EVENT) {
			showRequired($('#eventAddDivId'), $('#eventAddSltId'));
			hideRequired($('#boothAddDivId'), $('#boothAddSltId'));
		} else if (value == bannerType.BOOTH) {
			showRequired($('#eventAddDivId'), $('#eventAddSltId'));
			showRequired($('#boothAddDivId'), $('#boothAddSltId'));
			if ($('#eventAddSltId').val()) {
				App.renderBooth(true, $('#eventAddSltId').val(), $('#boothAddSltId'));
			}
 		} else {
 			hideRequired($('#eventAddDivId'), $('#eventAddSltId'));
			hideRequired($('#boothAddDivId'), $('#boothAddSltId'));
 		}
	});
	
	$('#eventAddSltId').change(function(){
		App.renderBooth(true, $(this).val(), $('#boothAddSltId'));
	});
	
	$('#objectTypeEditSltId').change(function(){
		var value = $(this).val();
		if (value == App.objectType.EVENT) {
			showRequired($('#objectIdEventEditDivId'), $('#objectIdEventEditSltId'));
			hideRequired($('#objectIdBoothEditDivId'), $('#objectIdBoothEditSltId'));
		} else if (value == App.objectType.BOOTH) {
			showRequired($('#objectIdEventEditDivId'), $('#objectIdEventEditSltId'));
			showRequired($('#objectIdBoothEditDivId'), $('#objectIdBoothEditSltId'));
			if ($('#objectIdEventEditSltId').val() && !$('#objectIdBoothEditSltId')) {
				App.renderBooth(true, $('#objectIdEventEditSltId').val(), $('#objectIdBoothEditSltId'));
			}
		} else {
			hideRequired($('#objectIdEventEditDivId'), $('#objectIdEventEditSltId'));
			hideRequired($('#objectIdBoothEditDivId'), $('#objectIdBoothEditSltId'));
		}
	});
	
	$('#objectIdEventEditSltId').change(function(){
		App.renderBooth(true, $(this).val(), $('#objectIdBoothEditSltId'));
	});
	
	
});

function openBannerEditModal(id) {
	var modal = $('#editBannerMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editBannerMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	
	var url = URI + "/" + id;
	App.ajaxRequest('GET', url, null, function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			var objectType = resp.data.objectRef.type.value;
			$('#objectTypeEditSltId').val(objectType);
			if (objectType == App.objectType.EVENT) {
				$('#objectIdEventEditSltId').val(resp.data.objectRef.object.id);
			} else if (objectType == App.objectType.BOOTH) {
				var eventValue = resp.data.objectRef.object.event.id;
				$('#objectIdEventEditSltId').val(eventValue);
				App.renderBooth(true, eventValue, $('#objectIdBoothEditSltId'), resp.data.objectRef.object.id);
			}
			$('#objectTypeEditSltId').trigger('change');
			$('#msgEditIptId').val(resp.data.msg);
			$('#infoEditIptId').val(resp.data.info);
			$('#activeEditIptId').prop('checked', resp.data.active);
			$('#disOrderEditIptId').val(resp.data.displayOrder);
			//animate
			App.animateSelect('#objectTypeEditSltId');
			App.animateSelect('#objectIdEventEditSltId');
			App.animateSelect('#objectIdBoothEditSltId');
			App.animateSwitch('#activeEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitBannerEdit() {
	$('#editBannerBtnId').trigger('click');
}

function openBannerDeleteModal(id) {
	var modal = $('#deleteBannerMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete banner will remove permanently banner out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitBannerDelete() {
	$('#deleteBannerBtnId').trigger('click');
}

function showRequired($div, $ele) {
	$div.show();
	$ele.prop('required', true);
}

function hideRequired($div, $ele) {
	$div.hide();
	$ele.prop('required', false);
}