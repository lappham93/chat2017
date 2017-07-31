/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/app-constant";
var type = {Element: 1, Collection: 2, Map: 3};
var valueEleId = "eleValueEditIptId";
var collectionEleId = "colValueEditIptId";
var mapEleId = "mapValueEditIptId";

$(function(){
	$('#editAppConstantFrmId').submit(function(e) {
		App.showLoader("Processing", "#editAppConstantFrmId");
		//init
		App.initAjaxRequest('#editAppConstantFrmId');
		
		var key = $('#keyEditIptId').val();
		var value = "";
		var valueType = $('#typeIptId').val();
		if (valueType == type.Element) {
			value = $('#' + valueEleId).val();
		} else if (valueType == type.Collection) {
			var total = $('#' + collectionEleId);
			value = [];
			for (var i = 0; i < total; i++) {
				value.push($('#' + collectionEleId + i).val());
			}
		} else if (valueType == type.Map) {
			var total = $('#' + mapEleId).val();
			value = {};
			for (var i = 0; i < total; i++) {
				var eleKey = $('#key-' + mapEleId + i).val();
				var eleValue = $('#value-' + mapEleId + i).val();
				value[eleKey] = eleValue;
			}
		}
		
		var data = {'key': key, 'value': value}
		var url = URI + "/" + key + "/edit";
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#editAppConstantFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
});

function openAppConstantEditModal(id) {
	var modal = $('#editAppConstantMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	$(errorMsg).hide();
	$(successMsg).hide();
	$('#keyEditIptId').val("");
	$('#valueDivId').html("");
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	App.showLoader("Loading", "#editAppConstantMdlId")
	var url = URI + "/" + id + "/get";
	App.ajaxRequest('GET', url, null, function(resp){
		if (resp.code >= 0) {
			$('#keyEditIptId').val(resp.data.key);
			var valueType = resp.data.element ? type.Element : resp.data.collection ? type.Collection : type.Map;
			$('#typeIptId').val(valueType);
			var parent = 'valueDivId';
			if (valueType == type.Element) {
				renderEleValue(parent, valueEleId, resp.data.value);
			} else if (valueType == type.Collection) {
				for (var i = 0; i < resp.data.value.length; i++) {
					renderEleValue(parent, collectionEleId + i, resp.data.value[i]);
				}
				renderHiddenEleValue(parent, collectionEleId, resp.data.value.length);
			} else if (valueType == type.Map) {
				var i = 0;
				for (var key in resp.data.value) {
					renderMapValue(parent, mapEleId + i++, key, resp.data.value[key]);
				}
				renderHiddenEleValue(parent, mapEleId, i);
			}
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function renderEleValue(parentId, eleId, value) {
	$('#' + parentId).append("<input type='text' id='" + eleId + "' class='form-control' required='required' placeholder='Value'/>");
	$('#' + eleId).val(value);
}

function renderHiddenEleValue(parentId, eleId, value) {
	$('#' + parentId).append("<input type='hidden' id='" + eleId + "'/>");
	$('#' + eleId).val(value);
}

function renderMapValue(parentId, eleId, key, value) {
	$('#' + parentId).append("<input type='text' id='" + 'key-' + eleId + "' class='form-control' required='required' placeholder='Value'/>");
	$('#key-' + eleId).val(key);
	$('#' + parentId).append("<input type='text' id='" + 'value-' + eleId + "' class='form-control' required='required' placeholder='Value'/>");
	$('#value-' + eleId).val(value);
	$('#' + parentId).append("<br/>");
}

function submitAppConstantEdit() {
	$('#editAppConstantBtnId').trigger('click');
}
