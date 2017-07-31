/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/eyebrow";
Photo.type = App.photoType.FACE;
Photo.url = URI;
$(function(){
	$('#addCateFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#addCateFrmId');
		App.initAjaxRequest('#addCateFrmId');
		
		var name = $('#nameAddIptId').val();
		var desc = $('#descAddIptId').val();
		var	isActive = $('#activeAddIptId').prop("checked");
		
		var data = {'name': name, 'desc': desc, 'active': isActive}
		var url = URI + "/category";
		App.ajaxRequest('POST', url, data, function(resp){
			App.callbackAjaxRequest('#addCateFrmId', resp);
		})
		
		return false;
	});
	
	$('#editCateFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#editCateFrmId');
		App.initAjaxRequest('#editCateFrmId');
		
		var id = $('#idEditIptId').val();
		var name = $('#nameEditIptId').val();
		var desc = $('#descEditIptId').val();
		var	isActive = $('#activeEditIptId').prop("checked");
		
		var data = {'name': name, 'desc': desc, 'active': isActive};
		var url = URI + "/category/" + id;
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#editCateFrmId', resp);
		})
		
		return false;
	});
	
	$('#deleteCateFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteCateFrmId');
		App.initAjaxRequest('#deleteCateFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/category/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteCateFrmId', resp);
		})
		
		return false;
	});
	
	$('#addEyebrowFrmId').submit(function(e) {
		//init
		$(document).scrollTop(0);
		App.showLoader('Processing', '#addEyebrowFrmId');
		App.initAjaxRequest('#addEyebrowFrmId');
		
		var photoFile = $('#photoIptId')[0].files;
	    
	    App.ajaxUploadPhotoRequest(App.photoType.FACE, photoFile, function(data){
	    	if (data.code >= 0) {
				var cate = $('#cateAddSltId').val();
				var name = $('#nameAddIptId').val();
				var type = $('#typeAddIptId').val();
				var desc = $('#descAddIptId').val();
				var	isActive = $('#activeAddIptId').prop("checked");
				var photoId = data.data.length > 0 ? data.data[0] : 0;
				var data = {'cate': cate, 'name': name, 'type': type, 'desc': desc, 
						 'active': isActive, 'photo': photoId}
				var url = URI;
				App.ajaxRequest('POST', url, data, function(resp){
					App.callbackAjaxRequest('#addEyebrowFrmId', resp);
				})
	    	} else {
	    		var errorMsg = $('#addEyebrowFrmId').find('.form-error');
	    		errorMsg.text(data.msg);
		    	$(errorMsg).show();
		    	App.hideLoader();
	    	}
	    }, function(){
	    	var errorMsg = $('#addEyebrowFrmId').find('.form-error');
	    	$(errorMsg).text("Server error");
	    	$(errorMsg).show();
	    	App.hideLoader();
	    })
		
		return false;
	});
	
	$('#editEyebrowFrmId').submit(function(e) {
		$('#editEyebrowMdlId').scrollTop(0);
		//init
		App.showLoader('Processing', '#editEyebrowFrmId');
		App.initAjaxRequest('#editEyebrowFrmId');
		
		var id = $('#idEditIptId').val();
		var cate = $('#cateEditSltId').val();
		var name = $('#nameEditIptId').val();
		var type = $('#typeEditSltId').val();
		var desc = $('#descEditIptId').val();
		var	isActive = $('#activeEditIptId').prop("checked");
		var data = {'cate': cate, 'name': name, 'type': type, 'desc': desc, 
				 'active': isActive, 'updateType': App.updateType.INFO}
		var url = URI + "/" + id;
		App.ajaxRequest('PUT', url, data, function(resp) {
			App.callbackAjaxRequest('#editEyebrowFrmId', resp);
		})
		
		return false;
	});
	
	$('#deleteEyebrowFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteEyebrowFrmId');
		App.initAjaxRequest('#deleteEyebrowFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteEyebrowFrmId', resp);
		})
		
		return false;
	});
});

function openEyebrowEditModal(id) {
	var modal = $('#editEyebrowMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editEyebrowMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	var url = URI + "/" + id;
	App.ajaxRequest('GET', url, null, function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#cateEditSltId').val(resp.data.cate.id);
			$('#nameEditIptId').val(resp.data.name);
			$('#typeEditSltId').val(resp.data.type.value);
			$('#descEditIptId').val(resp.data.desc);
			$('#activeEditIptId').prop('checked', resp.data.active);
			//animate
			App.animateSelect('#cateEditSltId');
			App.animateSelect('#typeEditSltId');
			App.animateSwitch('#activeEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitEditEyebrow() {
	$('#editEyebrowBtnId').trigger('click');
}

function openEyebrowDeleteModal(id) {
	var modal = $('#deleteEyebrowMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete eyebrow will remove permanently eyebrow out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteEyebrow() {
	$('#deleteEyebrowBtnId').trigger('click');
}

function openCateEditModal(id) {
	var modal = $('#editCateMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editCateMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	
	var url = URI + "/category/item/" + id;
	App.ajaxRequest('GET', url, null, function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#nameEditIptId').val(resp.data.name);
			$('#descEditIptId').val(resp.data.desc);
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

function submitEditCate(e) {
	$('#editCateBtnId').trigger('click');
}

function openCateDeleteModal(id) {
	var modal = $('#deleteCateMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete category causes deleting their eyebrows.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteCate() {
	$('#deleteCateBtnId').trigger('click');
}

function merDateAndTime(date, time) {
	return date + " " + time;
}

function search() {
	var eyebrowName = $('#nameSearchIptId').val().trim();
	var cate = $('#cateSearchSltId').val().trim();
	var servletPath = "/eyebrow";
	if (eyebrowName != "" || cate != "0") {
		servletPath += "?";
		if (eyebrowName != "") {
			servletPath += "name=" + eyebrowName;
		}
		if (cate != "0") {
			servletPath += "&category=" + cate;
		}
	}
	App.redirect(servletPath);
}