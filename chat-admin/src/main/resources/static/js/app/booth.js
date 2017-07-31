/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/booth";
Photo.type = App.photoType.EVENT;
Photo.url = URI + "/booth";
$(function(){
	$('#addBoothFrmId').submit(function(e) {
		//init
		$(document).scrollTop(0);
		App.showLoader('Processing', '#addBoothFrmId');
		App.initAjaxRequest('#addBoothFrmId');
		
		var photoFile = $('#photoIptId')[0].files;
		var model = $('#modelIptId')[0].files;
		var texture = $('#textIptId')[0].files;
	    var files = [];
	    var idxs = [-1, -1, -1];
	    if (photoFile.length > 0) {
	    	files.push(photoFile[0]);
	    	idxs[0] = files.length - 1;
	    }
	    if (model.length > 0) {
	    	files.push(model[0]);
	    	idxs[1] = files.length - 1;
	    }
	    if (texture.length > 0) {
	    	files.push(texture[0]);
	    	idxs[2] = files.length - 1;
	    }
		
	    App.ajaxUploadPhotoRequest(App.photoType.EVENT, files, function(data){
	    	if (data.code >= 0) {
	    		var photoId = 0, textureId = 0, modelId = 0;
	    		try {
	    			if (idxs[0] >= 0) {
	    				photoId = data.data[idxs[0]];
	    			}
	    			if (idxs[1] >= 0) {
	    				modelId = data.data[idxs[1]];
	    			}
	    			if (idxs[2] >= 0) {
	    				textureId = data.data[idxs[2]];
	    			}
	    		} catch (e){}
				var eventId = $('#eventAddSltId').val();
				var categoryId = $('#cateAddSltId').val();
				var name = $('#nameAddIptId').val();
				var floorId = $('#floorAddSltId').val();
				var desc = $('#descAddIptId').val();
				var site = $('#siteAddIptId').val();
				var phone = $('#phoneAddIptId').val();
				var	isFeature = $('#featureAddIptId').prop("checked");
				var	isActive = $('#activeAddIptId').prop("checked");
				var photoId = data.data.length > 0 ? data.data[0] : 0;
				var data = {'eventId': eventId, 'floorId': floorId, 'name': name,
						'active': isActive, 'categoryId': categoryId, 'desc': desc, 'site': site, 
						'phone': phone, 'photo': photoId, 'arModel': modelId, 'arTexture': textureId,
						'feature': isFeature};
				var url = URI + "/booth";
				App.ajaxRequest('POST', url, data, function(resp){
					App.callbackAjaxRequest('#addBoothFrmId', resp);
				});
	    	} else {
	    		var errorMsg = $('#addBoothFrmId').find('.form-error');
	    		errorMsg.text(data.msg);
		    	$(errorMsg).show();
		    	App.hideLoader();
	    	}
	    }, function(){
	    	var errorMsg = $('#addBoothFrmId').find('.form-error');
	    	$(errorMsg).text("Server error");
	    	$(errorMsg).show();
	    	App.hideLoader();
	    })
		
		return false;
	});
	
	$('#editBoothFrmId').submit(function(e) {
		$('#editBoothMdlId').scrollTop(0);
		//init
		App.showLoader('Processing', '#editBoothMdlId');
		App.initAjaxRequest('#editBoothMdlId');
		
		var id = $('#idEditIptId').val();
		var eventId = $('#eventEditSltId').val();
		var categoryId = $('#cateEditSltId').val();
		var name = $('#nameEditIptId').val();
		var floorId = $('#floorEditSltId').val();
		var desc = $('#descEditIptId').val();
		var site = $('#siteEditIptId').val();
		var phone = $('#phoneEditIptId').val();
		var	isFeature = $('#featureEditIptId').prop("checked");
		var	isActive = $('#activeEditIptId').prop("checked");
		var url = URI + "/booth/" + id;
		var data = {'eventId': eventId, 'categoryId': categoryId, 'name': name, 'floorId': floorId,
				'desc': desc, 'site': site, 'phone': phone, 'feature': isFeature,'active': isActive,
				'updateType': App.updateType.INFO};
		App.ajaxRequest('PUT', url, data, function(resp) {
			App.callbackAjaxRequest('#editBoothMdlId', resp);
		})
		
		return false;
	});
	
	$('#deleteBoothFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteBoothFrmId');
		App.initAjaxRequest('#deleteBoothFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/booth/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteBoothFrmId', resp);
		})
		
		return false;
	});
	
	$('#addCateFrmId').submit(function(e) {
		//init
		$(document).scrollTop(0);
		App.showLoader('Processing', '#addCateFrmId');
		App.initAjaxRequest('#addCateFrmId');
		
		var photoFile = $('#photoIptId')[0].files;
	    if (photoFile.length > 0) {
		    App.ajaxUploadPhotoRequest(App.photoType.EVENT, photoFile, function(data){
		    	if (data.code >= 0) {
					var photoId = data.data.length > 0 ? data.data[0] : 0;
					addCategory(photoId);
		    	} else {
		    		var errorMsg = $('#addCateFrmId').find('.form-error');
		    		errorMsg.text(data.msg);
			    	$(errorMsg).show();
			    	App.hideLoader();
		    	}
		    }, function(){
		    	var errorMsg = $('#addCateFrmId').find('.form-error');
		    	$(errorMsg).text("Server error");
		    	$(errorMsg).show();
		    	App.hideLoader();
		    })
	    } else {
	    	addCategory(0);
	    }
		
		return false;
	});
	
	$('#editCateFrmId').submit(function(e) {
		$('#editCateMdlId').scrollTop(0);
		//init
		App.showLoader('Processing', '#editCateFrmId');
		App.initAjaxRequest('#editCateFrmId');
		
		var id = $('#idEditIptId').val();
		var eventId = $('#eventEditSltId').val();
		var name = $('#nameEditIptId').val();
		var desc = $('#descEditIptId').val();
		var	isActive = $('#activeEditIptId').prop("checked");
		var url = URI + "/category/" + id;
		var data = {'eventId': eventId, 'name': name, 
				'desc': desc, 'active': isActive, 'updateType': App.updateType.INFO};
		App.ajaxRequest('PUT', url, data, function(resp) {
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
	
	$('#eventAddSltId').change(function(){
		renderCategory($(this).val(), $('#cateAddSltId'));
		App.renderFloor($(this).val(), $('#floorAddSltId'))
	});
	
	$('#eventEditSltId').change(function(e, cateValue, floorValue){
		renderCategory($(this).val(), $('#cateEditSltId'), cateValue);
		App.renderFloor($(this).val(), $('#floorEditSltId'), floorValue);
	});
	
});

function openBoothEditModal(id) {
	var modal = $('#editBoothMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editBoothMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	var url = URI + "/booth/" + id;
	App.ajaxRequest('GET', url, "", function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#eventEditSltId').val(resp.data.event.id);
			$('#nameEditIptId').val(resp.data.name);
			$('#descEditIptId').val(resp.data.desc);
			$('#siteEditIptId').val(resp.data.site);
			$('#phoneEditIptId').val(resp.data.phone);
			$('#featureEditIptId').prop('checked', resp.data.feature);
			$('#activeEditIptId').prop('checked', resp.data.active);
			$('#eventEditSltId').trigger('change', [resp.data.category.id, resp.data.floor.id]);
			//animate
			App.animateSelect('#eventEditSltId');
			App.animateSelect('#floorEditSltId');
			App.animateSelect('#cateEditSltId');
			App.animateSwitch('#activeEditIptId');
			App.animateSwitch('#featureEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitEditBooth() {
	$('#editBoothBtnId').trigger('click');
}

function openBoothDeleteModal(id) {
	var modal = $('#deleteBoothMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete booth will remove permanently booth out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteBooth() {
	$('#deleteBoothBtnId').trigger('click');
}


function openCateEditModal(id) {
	var modal = $('#editCateMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editCateMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	var url = URI + "/category/" + id;
	App.ajaxRequest('GET', url, "", function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#eventEditSltId').val(resp.data.event.id);
			$('#nameEditIptId').val(resp.data.name);
			$('#descEditIptId').val(resp.data.desc);
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

function submitEditCate() {
	$('#editCateBtnId').trigger('click');
}

function openCateDeleteModal(id) {
	var modal = $('#deleteCateMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete category will remove permanently category out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteCate() {
	$('#deleteCateBtnId').trigger('click');
}

function renderCategory(eventId, $cateEle, cateValue) {
	if ($cateEle.length <= 0) {
		return;
	}
	$cateEle.text('');
	var url = App.adminPrefix + "/booth/category/event/" + eventId;
	var data = "";
	App.ajaxRequest('GET', url, data, function(resp){
		if (resp.code >= 0) {
			var cates = resp.data;
			if (cates.length > 0) {
				$cateEle.append("<option disabled='disabled' selected='selected' value=''>Select Category</option>");
				for (var i = 0; i < cates.length; i++) {
					$cateEle.append("<option value='" + cates[i].id + "'>" + cates[i].name + "</option>");
    			}
				if (cateValue) {
					$cateEle.val(cateValue);
				}
			}
		}
	});
}

function addCategory(photoId) {
	var eventId = $('#eventAddSltId').val();
	var name = $('#nameAddIptId').val();
	var desc = $('#descAddIptId').val();
	var	isActive = $('#activeAddIptId').prop("checked");
	var data = {'eventId': eventId, 'name': name, 'photo': photoId, 
			'desc': desc, 'active': isActive};
	var url = URI + "/category";
	App.ajaxRequest('POST', url, data, function(resp){
		App.callbackAjaxRequest('#addCateFrmId', resp);
	})
}

function search() {
	var boothName = $('#boothSearchIptId').val().trim();
	var event = $('#eventSearchSltId').val().trim();
	var servletPath = "/booth/booth";
	if (boothName != "" || event != "0") {
		servletPath += "?";
		if (boothName != "") {
			servletPath += "name=" + boothName;
		}
		if (event != "0") {
			servletPath += "&event=" + event;
		}
	}
	App.redirect(servletPath);
}