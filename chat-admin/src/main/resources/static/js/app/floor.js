/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/floor";
Photo.type = App.photoType.MAP;
Photo.url = URI;
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
	
	$('#editFloorFrmId').submit(function(e) {
		$('#editFloorMdlId').scrollTop(0);
		//init
		App.showLoader('Processing', '#editFloorFrmId');
		App.initAjaxRequest('#editFloorFrmId');
		
		var id = $('#idEditIptId').val();
		var atlasId = $('#atlasIdEditIptId').val();
		var eventId = $('#eventEditSltId').val();
		var name = $('#nameEditIptId').val();
		var number = $('#numberEditIptId').val();
		var actualWidth = $('#widthEditIptId').val();
		var actualHeight = $('#heightEditIptId').val();
		var rotate = $('#rotateEditIptId').val();
		var	isActive = $('#activeEditIptId').prop("checked");
		var url = URI + "/" + id;
		var data = {'atlasId': atlasId, 'eventId': eventId, 'number': number, 
				'name': name, 'actualWidth': actualWidth, updateType: App.updateType.INFO,
				'actualHeight': actualHeight, 'rotate': rotate, 'active': isActive};
		App.ajaxRequest('PUT', url, data, function(resp) {
			App.callbackAjaxRequest('#editFloorFrmId', resp);
		})
		
		return false;
	});
	
	$('#deleteFloorFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteFloorFrmId');
		App.initAjaxRequest('#deleteFloorFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteFloorFrmId', resp);
		})
		
		return false;
	});
	
	$('#eventSearchSltId').change(function(){
		var event = $(this).val().trim();
		var servletPath = event != '0' ? "/floor?event=" + event : "/floor"; 
		App.redirect(servletPath);
	});
});

function openFloorEditModal(id) {
	var modal = $('#editFloorMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editFloorMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	var url = URI + "/" + id;
	App.ajaxRequest('GET', url, "", function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#atlasIdEditIptId').val(resp.data.atlasId);
			$('#eventEditSltId').val(resp.data.event.id);
			$('#nameEditIptId').val(resp.data.name);
			$('#numberEditIptId').val(resp.data.number);
			$('#widthEditIptId').val(resp.data.actualWidth);
			$('#heightEditIptId').val(resp.data.actualHeight);
			$('#rotateEditIptId').val(resp.data.rotate);
			$('#activeEditIptId').prop('checked', resp.data.active);
			//animate
			App.animateSelect('#eventEditSltId');
			App.animateSwitch('#activeEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitEditFloor() {
	$('#editFloorBtnId').trigger('click');
}

function openFloorDeleteModal(id) {
	var modal = $('#deleteFloorMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete floor will remove permanently floor out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteFloor() {
	$('#deleteFloorBtnId').trigger('click');
}