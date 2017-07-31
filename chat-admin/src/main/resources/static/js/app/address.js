/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/address";
var addressStatus = {ACTIVE: 1, INACTIVE: 0}

$(function(){
	$('#addAddressFrmId').submit(function(e) {
		$(document).scrollTop(0);
		App.showLoader("Processing", "#addAddressFrmId");
		$('#getPlaceDivId .loader').hide();
		//init
		App.initAjaxRequest('#addAddressFrmId');
		
		var name = $('#nameAddIptId').val();
		var add1 = $('#add1AddIptId').val();
		var add2 = $('#add2AddIptId').val();
		var countryCode = $('#countryAddSltId').val();
		var stateId = $('#stateAddSltId').val();
		var cityId = $('#cityAddSltId').val();
		var city = $('#cityAddIptId').val();
		var zipcode = $('#zipcodeAddIptId').val();
		var phone = $('#phoneAddIptId').val();
		var lon = $('#lonAddIptId').val();
		var lat = $('#latAddIptId').val();
		var	isActive = $('#activeAddIptId').prop("checked");
		var coordinate = {'lon': lon, 'lat': lat};
		var data = {'name': name, 'addressLine1': add1, 'addressLine2': add2, 'cityId': cityId, 'city': city,
				'stateId': stateId, 'countryCode': countryCode, 'zipCode': zipcode, 'phone': phone, 
				'coordinate': coordinate, 'active': isActive};
		var url = URI + "/event";
		App.ajaxRequest('POST', url, data, function(resp){
			App.callbackAjaxRequest('#addAddressFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
	$('#editAddressFrmId').submit(function(e) {
		$('#editAddressMdlId').scrollTop(0);
		App.showLoader("Processing", "#editAddressFrmId");
		$('#getPlaceEditDivId .loader').hide();
		//init
		App.initAjaxRequest('#editAddressFrmId');
		
		var name = $('#nameEditIptId').val();
		var add1 = $('#add1EditIptId').val();
		var add2 = $('#add2EditIptId').val();
		var countryCode = $('#countryEditSltId').val();
		var stateId = $('#stateEditSltId').val();
		var cityId = $('#cityEditSltId').val();
		var city = $('#cityEditIptId').val();
		var zipcode = $('#zipcodeEditIptId').val();
		var phone = $('#phoneEditIptId').val();
		var lon = $('#lonEditIptId').val();
		var lat = $('#latEditIptId').val();
		var	isActive = $('#activeEditIptId').prop("checked");
		var coordinate = {'lon': lon, 'lat': lat};
		var data = {'name': name, 'addressLine1': add1, 'addressLine2': add2, 'cityId': cityId, 'city': city,
				'stateId': stateId, 'countryCode': countryCode, 'zipCode': zipcode, 'phone': phone, 
				'coordinate': coordinate, 'active': isActive};
		var id = $('#idEditIptId').val();
		var url = URI + "/event/" + id;
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#editAddressFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
	$('#deleteAddressFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteAddressFrmId');
		App.initAjaxRequest('#deleteAddressFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/event/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteAddressFrmId', resp);
		})
		
		return false;
	});
	
	$('#activeStateFrmId').submit(function(e) {
		App.showLoader("Processing", "#activeStateFrmId");
		//init
		App.initAjaxRequest('#activeStateFrmId');
		
		var id = $('#idActiveIptId').val();
		var data = "";
		var url = URI + "/state/" + id + "/status/" + addressStatus.ACTIVE;
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#activeStateFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
	$('#inactiveStateFrmId').submit(function(e) {
		App.showLoader("Processing", "#inactiveStateFrmId");
		//init
		App.initAjaxRequest('#inactiveStateFrmId');
		
		var id = $('#idInactiveIptId').val();
		var data = "";
		var url = URI + "/state/" + id + "/status/" + addressStatus.INACTIVE;
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#inactiveStateFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
	$('#activeCityFrmId').submit(function(e) {
		App.showLoader("Processing", "#activeCityFrmId");
		//init
		App.initAjaxRequest('#activeCityFrmId');
		
		var id = $('#idActiveIptId').val();
		var data = "";
		var url = URI + "/city/" + id + "/status/" + addressStatus.ACTIVE;
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#activeCityFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
	$('#inactiveCityFrmId').submit(function(e) {
		App.showLoader("Processing", "#inactiveCityFrmId");
		//init
		App.initAjaxRequest('#inactiveCityFrmId');
		
		var id = $('#idInactiveIptId').val();
		var data = "";
		var url = URI + "/city/" + id + "/status/" + addressStatus.INACTIVE;
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#inactiveCityFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
	$('#stateFilterSltId').change(function() {
		var state = $(this).val();
		var currentUrl = document.URL;
		var currentUrlSplit = currentUrl.split("?");
		var baseUrl = currentUrlSplit[0];
		if (currentUrlSplit.length >= 2) {
			var params = currentUrlSplit[1];
			if (params.indexOf('state=') >= 0) {
				var splitParams = params.split("&");
				for (var i = 0; i < splitParams.length; i++) {
					if (splitParams[i].indexOf('state=') >= 0) {
						params = params.replace(splitParams[i], 'state=' + state);
					} else if (splitParams[i].indexOf('page=') >= 0) {
						params = params.replace(splitParams[i], 'page=1');
					}
				}
				baseUrl += '?' + params;
			} else {
				baseUrl += 'state=' + state;
			}
		} else {
			baseUrl += '?state=' + state;
		}
		window.location.replace(baseUrl);
	});
	
	$('#placeAddSltId').change(function(){
    	var coordinate = $(this).val();
    	$('#lonAddIptId').val(coordinate.split("_")[0]);
    	$('#latAddIptId').val(coordinate.split("_")[1]);
    });
	
	$('#countryAddSltId').change(function(){
		renderState($(this).val(), $('#stateAddSltId'));
	});
	
	$('#stateAddSltId').change(function(){
		renderCity($(this).val(), $('#cityAddSltId'));
	});
	
	renderState('US', $('#stateAddSltId'));
	
	$('#placeEditSltId').change(function(){
    	var coordinate = $(this).val();
    	$('#lonEditIptId').val(coordinate.split("_")[0]);
    	$('#latEditIptId').val(coordinate.split("_")[1]);
    });
	
	$('#countryEditSltId').change(function(e, stateValue, cityValue, cityEle){
		$('#cityEditSltId').text("");
		$('#cityEditSltId').val("");
		renderState($(this).val(), $('#stateEditSltId'), stateValue, cityValue, $(cityEle));
	});
	
	$('#stateEditSltId').change(function(){
		renderCity($(this).val(), $('#cityEditSltId'));
	});
});

function openStateActiveModal(id) {
	var modal = $('#activeStateMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Are you sure you want to make state ACTIVE?');
	$(warningMsg).show();
	$('#idActiveIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitStateActive() {
	$('#activeStateBtnId').trigger('click');
}

function openStateInactiveModal(id) {
	var modal = $('#inactiveStateMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Are you sure you want to make state INACTIVE?');
	$(warningMsg).show();
	$('#idInactiveIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitStateInactive() {
	$('#inactiveStateBtnId').trigger('click');
}

function openCityActiveModal(id) {
	var modal = $('#activeCityMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Are you sure you want to make city ACTIVE?');
	$(warningMsg).show();
	$('#idActiveIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitCityActive() {
	$('#activeCityBtnId').trigger('click');
}

function openCityInactiveModal(id) {
	var modal = $('#inactiveCityMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Are you sure you want to make city INACTIVE?');
	$(warningMsg).show();
	$('#idInactiveIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitCityInactive() {
	$('#inactiveCityBtnId').trigger('click');
}

function getCoordinate() {
	App.showLoader("Loading", "#getPlaceDivId")
	var $errEle = $($('#addAddressFrmId').find('.form-error'));
	var $placeDiv = $('#placeDivId');
	$errEle.hide();
	$placeDiv.hide();
	var add1 = $('#add1AddIptId').val();
	var add2 = $('#add2AddIptId').val();
	var city = $('#cityAddSltId').val();
	var state = $('#stateAddSltId').val();
	var country = $('#countryAddSltId').val();
	if (!(add1 || add2 || city || state || country)) {
		$errEle.text('Please filling address information fields before getting coordinates. ');
		$errEle.show();
		return false;
	}
	var url = URI + "/place";
	var data = {'add1': add1, 'add2': add2, 'cityId': city, 'stateId': state, 'country': country};
	url = App.buildSimpleGetParam(url, data);
	App.ajaxRequest('GET', url, data, function(resp){
		if (resp.code >= 0) {
			var places = resp.data;
			if (places.length > 0) {
				$('#placeAddSltId').text('');
    			$('#placeAddSltId').append("<option value=''>Select place</option>");
    			for (var i = 0; i < places.length; i++) {
    				var value = places[i].coordinate.lon + "_" + places[i].coordinate.lat;
        			$('#placeAddSltId').append("<option value='" + value + "'>" + places[i].fullAddress + "</option>");
    			}
    			$('#placeDivId').show();
			}
		}
		App.hideLoader();
	});
}

function getCoordinateEdit() {
	App.showLoader("Loading", "#getPlaceEditDivId")
	var $errEle = $($('#editAddressFrmId').find('.form-error'));
	var $placeDiv = $('#placeEditDivId');
	$errEle.hide();
	$placeDiv.hide();
	var add1 = $('#add1EditIptId').val();
	var add2 = $('#add2EditIptId').val();
	var city = $('#cityEditSltId').val();
	var state = $('#stateEditSltId').val();
	var country = $('#countryEditSltId').val();
	if (!(add1 || add2 || city || state || country)) {
		$errEle.text('Please filling address information fields before getting coordinates. ');
		$errEle.show();
		return false;
	}
	var url = URI + "/place";
	var data = {'add1': add1, 'add2': add2, 'cityId': city, 'stateId': state, 'country': country};
	url = App.buildSimpleGetParam(url, data);
	App.ajaxRequest('GET', url, data, function(resp){
		if (resp.code >= 0) {
			var places = resp.data;
			if (places.length > 0) {
				$('#placeEditSltId').text('');
    			$('#placeEditSltId').append("<option value=''>Select place</option>");
    			for (var i = 0; i < places.length; i++) {
    				var value = places[i].coordinate.lon + "_" + places[i].coordinate.lat;
        			$('#placeEditSltId').append("<option value='" + value + "'>" + places[i].fullAddress + "</option>");
    			}
    			$('#placeEditDivId').show();
			}
		}
		App.hideLoader();
	});
}

function renderState(country, $stateEle, stateValue, cityValue, $cityEle) {
	$stateEle.text('');
	var url = URI + "/state/" + country;
	var data = "";
	App.ajaxRequest('GET', url, data, function(resp){
		if (resp.code >= 0) {
			var states = resp.data;
			if (states.length > 0) {
				$stateEle.append("<option disabled='disabled' selected='selected' value=''>Select State</option>");
				for (var i = 0; i < states.length; i++) {
					$stateEle.append("<option value='" + states[i].id + "'>" + states[i].name + "</option>");
    			}
				if (stateValue) {
					$stateEle.val(stateValue);
					renderCity(stateValue, $cityEle, cityValue);
				}
			}
		}
	});
}

function renderCity(state, $cityEle, cityValue) {
	$cityEle.text('');
	var url = URI + "/city/" + state;
	var data = "";
	App.ajaxRequest('GET', url, data, function(resp){
		if (resp.code >= 0) {
			var cities = resp.data;
			if (cities.length > 0) {
				$cityEle.append("<option disabled='disabled' selected='selected' value=''>Select City</option>");
				for (var i = 0; i < cities.length; i++) {
					$cityEle.append("<option value='" + cities[i].id + "'>" + cities[i].name + "</option>");
    			}
				if (cityValue) {
					$cityEle.val(cityValue);
				}
			}
			showCityDiv(cities.length > 0);
		}
	});
}

function openAddressEditModal(id) {
	var modal = $('#editAddressMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editAddressMdlId');
	$('#getPlaceEditDivId .loader').hide();
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	
	var url = URI + "/event/" + id;
	App.ajaxRequest('GET', url, "", function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#nameEditIptId').val(resp.data.name);
			$('#add1EditIptId').val(resp.data.addressLine1);
			$('#add2EditIptId').val(resp.data.addressLine2);
			$('#countryEditSltId').val(resp.data.countryCode);
			$('#zipcodeEditIptId').val(resp.data.zipcode);
			$('#phoneEditIptId').val(resp.data.phone);
			$('#lonEditIptId').val(resp.data.coordinate.lon);
			$('#latEditIptId').val(resp.data.coordinate.lat);
			$('#activeEditIptId').prop("checked", resp.data.active);
			$('#countryEditSltId').trigger('change', [resp.data.stateId, resp.data.cityId, '#cityEditSltId']);
			$('#cityEditIptId').val(resp.data.city);
			//animate
			App.animateSelect('#countryEditSltId');
			App.animateSelect('#stateEditSltId');
			App.animateSelect('#cityEditSltId');
			App.animateSwitch('#activeEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	});
}

function submitEditAddress() {
	$('#editAddressBtnId').trigger('click');
}

function openAddressDeleteModal(id) {
	var modal = $('#deleteAddressMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Are you sure you want to delete address?');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteAddress() {
	$('#deleteAddressBtnId').trigger('click');
}

function showCityDiv(hasData) {
	if (hasData) {
		$('#cityAddSltId').parent().parent().show();
		$('#cityEditSltId').parent().parent().show();
		$('#cityAddIptId').parent().parent().hide();
		$('#cityEditIptId').parent().parent().hide();
	} else {
		$('#cityAddSltId').parent().parent().hide();
		$('#cityEditSltId').parent().parent().hide();
		$('#cityAddIptId').parent().parent().show();
		$('#cityEditIptId').parent().parent().show();
	}
}