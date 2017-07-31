/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/product";
Photo.type = App.photoType.PRODUCT;
Photo.url = URI;

$(function(){
	$('#addProductFrmId').submit(function(e) {
		$(document).scrollTop(0);
		App.showLoader("Processing", "#addProductFrmId");
		//init
		App.initAjaxRequest('#addProductFrmId');
		
		var photoFile = $('#photoIptId')[0].files;
	    
	    App.ajaxUploadPhotoRequest(App.photoType.PRODUCT, photoFile, function(data){
	    	if (data.code >= 0) {
				var eventId = $('#eventAddSltId').val();
				var boothId = $('#boothAddSltId').val();
				var name = $('#nameAddIptId').val();
				var desc = $('#descAddIptId').val();
				var retailPrice = $('#rpriceAddIptId').val();
				var salePrice = $('#spriceAddIptId').val();
				var	isFeature = $('#featureAddIptId').prop("checked");
				var	isActive = $('#activeAddIptId').prop("checked");
				var displayOrder = $('#disOrderAddIptId').val();
				var photoId = data.data.length > 0 ? data.data[0] : 0;
				console.log("photoId = " + photoId);
				
				var data = {'eventId': eventId, 'boothId': boothId, 'name': name, 'desc': desc, 'retailPrice': retailPrice,
						'salePrice': salePrice, 'active': isActive, 'feature': isFeature, 'photo': photoId, 'displayOrder': displayOrder};
				var url = URI + "";
				App.ajaxRequest('POST', url, data, function(resp){
					App.callbackAjaxRequest('#addProductFrmId', resp);
					App.hideLoader();
				})
	    	} else {
	    		var errorMsg = $('#addProductFrmId').find('.form-error');
	    		errorMsg.text(data.msg);
		    	$(errorMsg).show();
		    	App.hideLoader();
	    	}
	    }, function(){
	    	var errorMsg = $('#addProductFrmId').find('.form-error');
	    	$(errorMsg).text("Server error");
	    	$(errorMsg).show();
	    	App.hideLoader();
	    })
		
		return false;
	});
	
	$('#editProductFrmId').submit(function(e) {
		$('#editProductMdlId').scrollTop(0);
		App.showLoader("Processing", "#editProductFrmId");
		//init
		App.initAjaxRequest('#editProductFrmId');
		
		var id = $('#idEditIptId').val();
		var eventId = $('#eventEditSltId').val();
		var boothId = $('#boothEditSltId').val();
		var name = $('#nameEditIptId').val();
		var desc = $('#descEditIptId').val();
		var retailPrice = $('#rpriceEditIptId').val();
		var salePrice = $('#spriceEditIptId').val();
		var	isFeature = $('#featureEditIptId').prop("checked");
		var	isActive = $('#activeEditIptId').prop("checked");
		var displayOrder = $('#disOrderEditIptId').val();
		
		var data = {'eventId': eventId, 'boothId': boothId, 'name': name, 'desc': desc, 'retailPrice': retailPrice,
				'salePrice': salePrice, 'active': isActive, 'feature': isFeature, 'updateType': App.updateType.INFO, 'displayOrder': displayOrder};
		var url = URI + "/" + id;
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#editProductFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
	$('#deleteProductFrmId').submit(function(e) {
		App.showLoader("Processing", "#deleteProductFrmId");
		//init
		App.initAjaxRequest('#deleteProductFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteProductFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
	$('#eventAddSltId').change(function(){
		App.renderBooth(true, $(this).val(), $('#boothAddSltId'));
	});
	
	$('#eventEditSltId').change(function(){
		App.renderBooth(true, $(this).val(), $('#boothAddSltId'));
	});
	
	App.renderBooth(false, eventSelect, $("#boothAddSltId"), boothSelect);
});

function openProductEditModal(id) {
	var modal = $('#editProductMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editProductMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	
	var url = URI + "/" + id;
	App.ajaxRequest('GET', url, null, function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			var eventId = resp.data.event.id;
			var boothId = resp.data.booth.id;
			$('#eventEditSltId').val(eventId);
			App.renderBooth(true, eventId, $('#boothEditSltId'), boothId);
			$('#nameEditIptId').val(resp.data.name);
			$('#descEditIptId').val(resp.data.desc);
			$('#rpriceEditIptId').val(resp.data.retailPrice);
			$('#spriceEditIptId').val(resp.data.salePrice);
			$('#featureEditIptId').prop('checked', resp.data.feature);
			$('#activeEditIptId').prop('checked', resp.data.active);
			$('#disOrderEditIptId').val(resp.data.displayOrder);
			//animate
			App.animateSelect('#eventEditSltId');
			App.animateSelect('#boothEditSltId');
			App.animateSwitch('#activeEditIptId');
			App.animateSwitch('#featureEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitEditProduct() {
	$('#editProductBtnId').trigger('click');
}

function openProductDeleteModal(id) {
	var modal = $('#deleteProductMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete product will remove permanently product out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteProduct() {
	$('#deleteProductBtnId').trigger('click');
}

function search() {
	var productName = $('#productSearchIptId').val().trim();
	var event = $('#eventSearchSltId').val().trim();
	var servletPath = "/product";
	if (productName != "" || event != "0") {
		servletPath += "?";
		if (productName != "") {
			servletPath += "name=" + productName;
		}
		if (event != "0") {
			servletPath += "&event=" + event;
		}
	}
	App.redirect(servletPath);
}