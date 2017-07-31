/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/service";
var addPrices = {};
var editPrices = {};
var tmpPrices = {};
var zipCodes = null;
Photo.type = App.photoType.SERVICE;
Photo.url = URI + "/service";
$(function(){
	init();
	$('#addServiceCateFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#addServiceCateFrmId');
		App.initAjaxRequest('#addServiceCateFrmId');
		
		var parent = $('#cateAddSltId').val();
		var name = $('#nameAddIptId').val();
		var displayOrder = $('#disOrderAddIptId').val();
		var	isActive = $('#activeAddIptId').prop("checked");
		
		var data = {'parentId': parent, 'name': name, 'displayOrder': displayOrder, 'active': isActive}
		var url = URI + "/category";
		App.ajaxRequest('POST', url, data, function(resp){
			App.callbackAjaxRequest('#addServiceCateFrmId', resp);
		})
		
		return false;
	});
	
	$('#editServiceCateFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#editServiceCateFrmId');
		App.initAjaxRequest('#editServiceCateFrmId');
		
		var id = $('#idEditIptId').val();
		var parent = $('#cateEditSltId').val();
		var name = $('#nameEditIptId').val();
		var displayOrder = $('#disOrderEditIptId').val();
		var	isActive = $('#activeEditIptId').prop("checked");
		
		var data = {'parentId': parent, 'name': name, 'displayOrder': displayOrder, 'active': isActive}
		var url = URI + "/category/" + id;
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#editServiceCateFrmId', resp);
		})
		
		return false;
	});
	
	$('#deleteServiceCateFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteServiceCateFrmId');
		App.initAjaxRequest('#deleteServiceCateFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/category/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteServiceCateFrmId', resp);
		})
		
		return false;
	});
	
	$('#addServiceFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#addServiceFrmId');
		App.initAjaxRequest('#addServiceFrmId');
		
		var photoFile = $('#photoIptId')[0].files;
	    
	    App.ajaxUploadPhotoRequest(App.photoType.SERVICE, photoFile, function(data){
	    	if (data.code >= 0) {
				var cate = $('#cateAddSltId').val();
				var name = $('#nameAddIptId').val();
				var desc = $('#descAddIptId').val();
				var oldPrice = $('#oldPriceAddIptId').val();
				var price = $('#defaultPriceAddIptId').val();
				var displayOrder = $('#disOrderAddIptId').val();
				var	isDefault = $('#defaultAddIptId').prop("checked");
				var	isActive = $('#activeAddIptId').prop("checked");
				var photoId = data.data.length > 0 ? data.data[0] : 0;
				var basePrices = {};
				for (var place in addPrices) {
					var placeArr = place.split(" ");
					basePrices[placeArr[placeArr.length - 1]] = addPrices[place];
				}
				console.log("photoId = " + photoId);
				var data = {'categoryId': cate, 'name': name, 'description': desc, 'baseOldPrice': oldPrice, 'basePrice': price, 'basePrices': basePrices,
							'displayOrder': displayOrder, 'default': isDefault, 'active': isActive, 'photoId': photoId}
				var url = URI + "/service";
				App.ajaxRequest('POST', url, data, function(resp){
					App.callbackAjaxRequest('#addServiceFrmId', resp);
				})
	    	} else {
	    		var errorMsg = $('#addServiceFrmId').find('.form-error');
	    		errorMsg.text(data.msg);
		    	$(errorMsg).show();
		    	App.hideLoader();
	    	}
	    }, function(){
	    	var errorMsg = $('#addServiceFrmId').find('.form-error');
	    	$(errorMsg).text("Server error");
	    	$(errorMsg).show();
	    	App.hideLoader();
	    })
		
		return false;
	});
	
	$('#editServiceFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#editServiceFrmId');
		App.initAjaxRequest('#editServiceFrmId');
		
		var id = $('#idEditIptId').val();
		var cate = $('#cateEditSltId').val();
		var name = $('#nameEditIptId').val();
		var desc = $('#descEditIptId').val();
		var oldPrice = $('#oldPriceEditIptId').val();
		var price = $('#priceEditIptId').val();
		var displayOrder = $('#disOrderEditIptId').val();
		var	isDefault = $('#defaultEditIptId').prop("checked");
		var	isActive = $('#activeEditIptId').prop("checked");
		
		var data = {'categoryId': cate, 'name': name, 'description': desc, 'baseOldPrice': oldPrice, 'basePrice': price, 
				'displayOrder': displayOrder, 'default': isDefault, 'active': isActive, updateType: App.updateType.INFO}
		var url = URI + "/service/" + id;
		App.ajaxRequest('PUT', url, data, function(resp) {
			App.callbackAjaxRequest('#editServiceFrmId', resp);
		})
		
		return false;
	});
	
	$('#deleteServiceFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteServiceFrmId');
		App.initAjaxRequest('#deleteServiceFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/service/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteServiceFrmId', resp);
		})
		
		return false;
	});
	
	$('#stateAddSltId').change(function() {
		$('#zipcodeAddSltId').text("");
		var state = $(this).val();
		var zipcodes = zipCodes[state];
		if (zipcodes != null) {
			for (var i = 0; i < zipcodes.length; i++) {
				$('#zipcodeAddSltId').append("<option value='" + zipcodes[i].cityName + ", " + zipcodes[i].code + "'>" + zipcodes[i].cityName + " " + zipcodes[i].code + "</option>");
			}
		}
		App.animateMultiSelect('#zipcodeAddSltId');
	});
	
	$('#addPriceFrmId').unbind('submit').submit(function(e){
		addPriceToList();
		return false;
	});
	
});

function init() {
	var data = null;
	var url = App.adminPrefix + "/zipcode";
	App.ajaxRequest('GET', url, data, function(resp){
		zipCodes = resp;
	})
}

function openServiceEditModal(id) {
	var modal = $('#editServiceMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editServiceMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	var url = URI + "/service/" + id;
	App.ajaxRequest('GET', url, null, function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#cateEditSltId').val(resp.data.serviceCate.id);
			$('#nameEditIptId').val(resp.data.name);
			$('#descEditIptId').val(resp.data.description);
			$('#oldPriceEditIptId').val(resp.data.baseOldPrice);
			$('#priceEditIptId').val(resp.data.basePrice);
			$('#disOrderEditIptId').val(resp.data.displayOrder);
			$('#defaultEditIptId').prop('checked', resp.data.default);
			$('#activeEditIptId').prop('checked', resp.data.active);
			//animate
			App.animateSelect('#cateEditSltId');
			App.animateSwitch('#activeEditIptId');
			App.animateSwitch('#defaultEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitServiceEdit() {
	$('#editServiceBtnId').trigger('click');
}

function openServiceDeleteModal(id) {
	var modal = $('#deleteServiceMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete service will remove permanently service out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitServiceDelete() {
	$('#deleteServiceBtnId').trigger('click');
}

function openServiceCateEditModal(id) {
	var modal = $('#editServiceCateMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editServiceCateMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	
	var url = URI + "/category/" + id;
	App.ajaxRequest('GET', url, null, function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#cateEditSltId').val(resp.data.parent.id);
			$('#nameEditIptId').val(resp.data.name);
			$('#disOrderEditIptId').val(resp.data.displayOrder);
			$('#activeEditIptId').prop('checked', resp.data.active);
			//animate
			App.animateSelect('#cateEditSltId');
			App.animateSwitch('#activeEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitServiceCateEdit(e) {
	$('#editCateBtnId').trigger('click');
}

function openServiceCateDeleteModal(id) {
	var modal = $('#deleteServiceCateMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete category causes deleting all child categories and their services.')
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitServiceCateDelete() {
	$('#deleteCateBtnId').trigger('click');
}

function addPriceToList() {
	var state = $('#stateAddSltId option:selected').text();
	var zcode = $('#zipcodeAddSltId').val();
	var type = $('#priceModalTypeId').val();
	if (zcode != null) {
		for (var i = 0; i < zcode.length; i++) {
			tmpPrices[state + ", " + zcode[i]] = createPriceObject();
		}
		renderPriceList(tmpPrices);
	}
}

function createPriceObject() {
	var staticPrice = $('#priceAddIptId').val();
	var minPrice = $('#minPriceAddIptId').val();
	var maxPrice = $('#maxPriceAddIptId').val();
	var autoAdjust = $('#autoAdjustIptId').prop("checked");
	var haMinPrice = $('#haMinPriceAddIptId').val();
	var haMaxPrice = $('#haMaxPriceAddIptId').val();
	var haAvgPrice = $('#haAvgPriceAddIptId').val();
	var haMinAvgPrice = $('#haMinAvgPriceAddIptId').val();
	var haMaxAvgPrice = $('#haMaxAvgPriceAddIptId').val();
	var priceObject = {dynamicPrice: staticPrice, staticPrice: staticPrice, minPrice: minPrice, maxPrice: maxPrice, autoAdjust: autoAdjust,
						homeAdMinPrice: haMinPrice, homeAdMaxPrice: haMaxPrice, homeAdAvgPrice: haAvgPrice, homeAdMinAvgPrice: haMinAvgPrice, homeAdMaxAvgPrice: haMaxAvgPrice};
	return priceObject;
}

function openAddPriceList() {
	tmpPrices = App.cloneMap(addPrices);
	openPriceListModal(tmpPrices, 1);
}

function openPriceListModal(prices, type) {
	App.showLoader('Loading', '#priceMdlId');
	var modal = $('#priceMdlId');
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	var priceList = modal.find('#priceListId');
	renderPriceList(prices);
	$('#priceAddIptId').val('');
	$('#maxPriceAddIptId').val('');
	$('#minPriceAddIptId').val('');
	$('#haMinPriceAddIptId').val('');
	$('#haMaxPriceAddIptId').val('');
	$('#haAvgPriceAddIptId').val('');
	$('#haMinAvgPriceAddIptId').val('');
	$('#haMaxAvgPriceAddIptId').val('');
	$('#priceListSearchIptId').val('');
	$('#priceModalTypeId').val(type);
	$('#priceListSearchIptId').keyup(function(e) {
		filterPriceList(e, $(this).val(), priceList.find('.priceEleClc'));
	})
	App.animateSwitch('#autoAdjustIptId');
	App.hideLoader();
}

function renderPriceList(prices) {
	var modal = $('#priceMdlId');
	var priceListEle = modal.find('#priceListId');
	var priceEle = modal.find('#priceEleId');
	var num = 1;
	priceListEle.html('');
	for (var place in prices) {
		var ele = priceEle.clone();
		ele.prop('id', ele.prop('id') + "_" + num++);
		ele.show();
		$(ele.find('.placeClc')[0]).text(place);
		$(ele.find('.priceClc')[0]).text(prices[place].staticPrice);
		$(ele.find('.minPriceClc')[0]).text(prices[place].minPrice);
		$(ele.find('.maxPriceClc')[0]).text(prices[place].maxPrice);
		$(ele.find('.autoAdjustClc')[0]).text(prices[place].autoAdjust);
		$(ele.find('.haMinPriceClc')[0]).text(prices[place].homeAdMinPrice);
		$(ele.find('.haMaxPriceClc')[0]).text(prices[place].homeAdMaxPrice);
		$(ele.find('.haAvgPriceClc')[0]).text(prices[place].homeAdAvgPrice);
		$(ele.find('.haMinAvgPriceClc')[0]).text(prices[place].homeAdMinAvgPrice);
		$(ele.find('.haMaxAvgPriceClc')[0]).text(prices[place].homeAdMaxAvgPrice);
		$(ele.find('.deleteClc')[0]).click(function(e) {
			var ancestor = $(this).parent().parent().parent();
			ancestor.remove();
			delete prices[place];
		})
		$(ele.find('.cloneClc')[0]).click(function(e) {
			var ancestor = $(this).parent().parent().parent();
			$('#priceAddIptId').val($(ancestor.find('.priceClc')[0]).text());
			$('#autoAdjustIptId').prop("checked", $(ancestor.find('.autoAdjustClc')[0]).text() == 'true');
			$('#maxPriceAddIptId').val($(ancestor.find('.maxPriceClc')[0]).text());
			$('#minPriceAddIptId').val($(ancestor.find('.minPriceClc')[0]).text());
			$('#haMinPriceAddIptId').val($(ancestor.find('.haMinPriceClc')[0]).text());
			$('#haMaxPriceAddIptId').val($(ancestor.find('.haMaxPriceClc')[0]).text());
			$('#haAvgPriceAddIptId').val($(ancestor.find('.haAvgPriceClc')[0]).text());
			$('#haMinAvgPriceAddIptId').val($(ancestor.find('.haMinAvgPriceClc')[0]).text());
			$('#haMaxAvgPriceAddIptId').val($(ancestor.find('.haMaxAvgPriceClc')[0]).text());
			
			$('#autoAdjustIptId').trigger('onchange');
			filterValue();
			App.animateSwitch('#autoAdjustIptId');
		})
		priceListEle.prepend(ele);
	}
}

function openEditPriceList(serviceId) {
	var url = URI + "/service/" + serviceId + "/prices";
	App.ajaxRequest('GET', url, null, function(resp) {
		if (resp.code >= 0) {
			$('#idPriceIptId').val(serviceId);
			editPrices = resp.data;
			tmpPrices = App.cloneMap(editPrices);
			openPriceListModal(tmpPrices, 2);
		}
	})
}

//search price list
function filterPriceList(e, text, dests) {
	if (dests.length <= 0 || text == null) {
		return;
	}
	text = text.trim();
	if (text == '') {
		for (var i = 0; i < dests.length; i++) {
			$(dests[i]).show();
		}
	} else {
		for (var i = 0; i < dests.length; i++) {
			var ele = dests[i];
			var eleText = $($(ele).find('.placeClc')[0]).text();
			if (eleText.toLowerCase().includes(text.toLowerCase())) {
				$(ele).show();
			} else {
				$(ele).hide();
			}
		}
	}
	
}

function submitServicePricesEdit() {
	$('html, body').animate({ scrollTop: 0 }, 'fast');
	var modal = $('#priceMdlId');
	var type = $('#priceModalTypeId').val();
	var serviceId = $('#idPriceIptId').val();
	if (type == 1) {
		addPrices = App.cloneMap(tmpPrices);
		modal.modal('hide');
	} else {
		App.showLoader('Processing', '#priceMdlId');
		editPrices = {};
		for (var place in tmpPrices) {
			var placeArr = place.split(" ");
			editPrices[placeArr[placeArr.length - 1]] = tmpPrices[place];
		}
		App.initAjaxRequest('#priceMdlId');
		var data = {'basePrices': editPrices, updateType: App.updateType.OTHER}
		var url = URI + "/service/" + serviceId;
		App.ajaxRequest('PUT', url, data, function(resp) {
			App.callbackAjaxRequest('#priceMdlId', resp);
		})
	}
}

function showAdjustDiv() {
	var isShow = $('#autoAdjustIptId').prop('checked');
	if (isShow) {
		$('#autoAdjustDivId').show();
		$('#minPriceAddIptId').prop("required", true);
		$('#maxPriceAddIptId').prop("required", true);
		filterValue();
	} else {
		$('#autoAdjustDivId').hide();
		$('#minPriceAddIptId').prop("required", false);
		$('#maxPriceAddIptId').prop("required", false);
		
		$('#priceAddIptId').removeAttr("max");
		$('#priceAddIptId').prop("min", 0);
		$('#minPriceAddIptId').removeAttr("max");
		$('#minPriceAddIptId').prop("min", 0);
		$('#maxPriceAddIptId').removeAttr("max");
		$('#maxPriceAddIptId').prop("min", 0);
	}
}

function filterValue() {
	if ($('#autoAdjustIptId').prop('checked')) {
		var min = $('#minPriceAddIptId').val();
		var max = $('#maxPriceAddIptId').val();
		var value = $('#priceAddIptId').val();
		
		var maxFilter = value != '' ? value : max != '' ? max : '';
		if (maxFilter != '') {
			$('#minPriceAddIptId').prop('max', maxFilter);
		}
		
		var minFilter = value != '' ? value : min != '' ? min : '';
		if (maxFilter != '') {
			$('#maxPriceAddIptId').prop('min', minFilter);
		}
		
		if (min != '') {
			$('#priceAddIptId').prop('min', min);
		}
		if (max != '') {
			$('#priceAddIptId').prop('max', max);
		}
	}
}

