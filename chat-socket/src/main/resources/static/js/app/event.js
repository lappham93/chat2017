/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/event";
Photo.type = App.photoType.EVENT;
Photo.url = URI + "/event";
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
	
	$('#addEventFrmId').submit(function(e) {
		//init
		$(document).scrollTop(0);
		App.showLoader('Processing', '#addEventFrmId');
		App.initAjaxRequest('#addEventFrmId');
		
		var photoFile = $('#photoIptId')[0].files;
	    
	    App.ajaxUploadPhotoRequest(App.photoType.EVENT, photoFile, function(data){
	    	if (data.code >= 0) {
	    		var atlasId = $('#atlasIdAddIptId').val();
				var cate = $('#cateAddSltId').val();
				var name = $('#nameAddIptId').val();
				var desc = $('#descAddIptId').val();
				var addressId = $('#addAddSltId').val();
				var startTime = merDateAndTime($('#startDateAddIptId').val(), $('#startTimeAddIptId').val());
				var endTime = merDateAndTime($('#endDateAddIptId').val(), $('#endTimeAddIptId').val());
				var mondayStart = "", mondayEnd = "", isMonday = $('#mondayAddChxId').prop('checked');
				if (isMonday) {
					mondayStart = $('#mondayStartAddIptId').val();
					mondayEnd = $('#mondayEndAddIptId').val();
				}
				var tuesdayStart = "", tuesdayEnd = "", isTuesday = $('#tuesdayAddChxId').prop('checked');
				if (isTuesday) {
					tuesdayStart = $('#tuesdayStartAddIptId').val();
					tuesdayEnd = $('#tuesdayEndAddIptId').val();
				}
				var wednesdayStart = "", wednesdayEnd = "", isWednesday = $('#wednesdayAddChxId').prop('checked');
				if (isWednesday) {
					wednesdayStart = $('#wednesdayStartAddIptId').val();
					wednesdayEnd = $('#wednesdayEndAddIptId').val();
				}
				var thursdayStart = "", thursdayEnd = "", isThursday = $('#thursdayAddChxId').prop('checked');
				if (isThursday) {
					thursdayStart = $('#thursdayStartAddIptId').val();
					thursdayEnd = $('#thursdayEndAddIptId').val();
				}
				var fridayStart = "", fridayEnd = "", isFriday = $('#fridayAddChxId').prop('checked');
				if (isFriday) {
					fridayStart = $('#fridayStartAddIptId').val();
					fridayEnd = $('#fridayStartAddIptId').val();
				}
				var saturdayStart = "", saturdayEnd = "", isSaturday = $('#saturdayAddChxId').prop('checked');
				if (isSaturday) {
					saturdayStart = $('#saturdayStartAddIptId').val();
					saturdayEnd = $('#saturdayEndAddIptId').val();
				}
				var sundayStart = "", sundayEnd = "", isSunday = $('#sundayAddChxId').prop('checked');
				if (isSunday) {
					sundayStart = $('#sundayStartAddIptId').val();
					sundayEnd = $('#sundayEndAddIptId').val();
				}
				var businessHour = {'mondayStart': mondayStart, 'mondayEnd': mondayEnd, 'tuesdayStart': tuesdayStart,
						'tuesdayEnd': tuesdayEnd, 'wednesdayStart': wednesdayStart, 'wednesdayEnd': wednesdayEnd,
						'thursdayStart': thursdayStart, 'thursdayEnd': thursdayEnd, 'fridayStart': fridayStart,
						'fridayEnd': fridayEnd, 'saturdayStart': saturdayStart, 'saturdayEnd': saturdayEnd,
						'sundayStart': sundayStart, 'sundayEnd': sundayEnd}
				var	isFeature = $('#featureAddIptId').prop("checked");
				var	isActive = $('#activeAddIptId').prop("checked");
				var photoId = data.data.length > 0 ? data.data[0] : 0;
				var data = {'atlasId': atlasId,'categoryId': cate, 'name': name, 'desc': desc, 
						 'businessHour': businessHour, 'startTime': startTime, 'endTime': endTime,
						 'addressId': addressId, 'feature': isFeature,'active': isActive, 'photo': photoId}
				var url = URI + "/event";
				App.ajaxRequest('POST', url, data, function(resp){
					App.callbackAjaxRequest('#addEventFrmId', resp);
				})
	    	} else {
	    		var errorMsg = $('#addEventFrmId').find('.form-error');
	    		errorMsg.text(data.msg);
		    	$(errorMsg).show();
		    	App.hideLoader();
	    	}
	    }, function(){
	    	var errorMsg = $('#addEventFrmId').find('.form-error');
	    	$(errorMsg).text("Server error");
	    	$(errorMsg).show();
	    	App.hideLoader();
	    })
		
		return false;
	});
	
	$('#editEventFrmId').submit(function(e) {
		$('#editEventMdlId').scrollTop(0);
		//init
		App.showLoader('Processing', '#editEventFrmId');
		App.initAjaxRequest('#editEventFrmId');
		
		var id = $('#idEditIptId').val();
		var atlasId = $('#atlasIdEditIptId').val();
		var cate = $('#cateEditSltId').val();
		var name = $('#nameEditIptId').val();
		var desc = $('#descEditIptId').val();
		var addressId = $('#addEditSltId').val();
		var startTime = merDateAndTime($('#startDateEditIptId').val(), $('#startTimeEditIptId').val());
		var endTime = merDateAndTime($('#endDateEditIptId').val(), $('#endTimeEditIptId').val());
		var mondayStart = "", mondayEnd = "", isMonday = $('#mondayEditChxId').prop('checked');
		if (isMonday) {
			mondayStart = $('#mondayStartEditIptId').val();
			mondayEnd = $('#mondayEndEditIptId').val();
		}
		var tuesdayStart = "", tuesdayEnd = "", isTuesday = $('#tuesdayEditChxId').prop('checked');
		if (isTuesday) {
			tuesdayStart = $('#tuesdayStartEditIptId').val();
			tuesdayEnd = $('#tuesdayEndEditIptId').val();
		}
		var wednesdayStart = "", wednesdayEnd = "", isWednesday = $('#wednesdayEditChxId').prop('checked');
		if (isWednesday) {
			wednesdayStart = $('#wednesdayStartEditIptId').val();
			wednesdayEnd = $('#wednesdayEndEditIptId').val();
		}
		var thursdayStart = "", thursdayEnd = "", isThursday = $('#thursdayEditChxId').prop('checked');
		if (isThursday) {
			thursdayStart = $('#thursdayStartEditIptId').val();
			thursdayEnd = $('#thursdayEndEditIptId').val();
		}
		var fridayStart = "", fridayEnd = "", isFriday = $('#fridayEditChxId').prop('checked');
		if (isFriday) {
			fridayStart = $('#fridayStartEditIptId').val();
			fridayEnd = $('#fridayStartEditIptId').val();
		}
		var saturdayStart = "", saturdayEnd = "", isSaturday = $('#saturdayEditChxId').prop('checked');
		if (isSaturday) {
			saturdayStart = $('#saturdayStartEditIptId').val();
			saturdayEnd = $('#saturdayEndEditIptId').val();
		}
		var sundayStart = "", sundayEnd = "", isSunday = $('#sundayEditChxId').prop('checked');
		if (isSunday) {
			sundayStart = $('#sundayStartEditIptId').val();
			sundayEnd = $('#sundayEndEditIptId').val();
		}
		var businessHour = {'mondayStart': mondayStart, 'mondayEnd': mondayEnd, 'tuesdayStart': tuesdayStart,
				'tuesdayEnd': tuesdayEnd, 'wednesdayStart': wednesdayStart, 'wednesdayEnd': wednesdayEnd,
				'thursdayStart': thursdayStart, 'thursdayEnd': thursdayEnd, 'fridayStart': fridayStart,
				'fridayEnd': fridayEnd, 'saturdayStart': saturdayStart, 'saturdayEnd': saturdayEnd,
				'sundayStart': sundayStart, 'sundayEnd': sundayEnd}
		var	isFeature = $('#featureEditIptId').prop("checked");
		var	isActive = $('#activeEditIptId').prop("checked");
		var data = {'atlasId': atlasId,'categoryId': cate, 'name': name, 'desc': desc, 
				 'businessHour': businessHour, 'startTime': startTime, 'endTime': endTime,
				 'addressId': addressId, 'feature': isFeature,'active': isActive, 'updateType': App.updateType.INFO}
		var url = URI + "/event/" + id;
		App.ajaxRequest('PUT', url, data, function(resp) {
			App.callbackAjaxRequest('#editEventFrmId', resp);
		})
		
		return false;
	});
	
	$('#deleteEventFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteEventFrmId');
		App.initAjaxRequest('#deleteEventFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/event/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteEventFrmId', resp);
		})
		
		return false;
	});
	
	$('input[type="checkbox"]').change(function(){
		var $grandParent = $(this).parent().parent();
		$grandParent.find('input[type="time"]').prop('disabled', !$(this).prop('checked'));
	});
	
	$('#sundayAddChxId').prop('checked', false);
	$('#sundayAddChxId').trigger('change');
	$('#saturdayAddChxId').prop('checked', false);
	$('#saturdayAddChxId').trigger('change');
});

function openEventEditModal(id) {
	var modal = $('#editEventMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editEventMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	var url = URI + "/event/" + id;
	App.ajaxRequest('GET', url, null, function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#atlasIdEditIptId').val(resp.data.atlasId);
			$('#cateEditSltId').val(resp.data.category.id);
			$('#nameEditIptId').val(resp.data.name);
			$('#descEditIptId').val(resp.data.desc);
			$('#addEditSltId').val(resp.data.address.id);
			$('#startDateEditIptId').val(App.getValidDateFormat(resp.data.startTime));
			$('#endDateEditIptId').val(App.getValidDateFormat(resp.data.endTime));
			$('#startTimeEditIptId').val(App.getValidTimeFormat(resp.data.startTime));
			$('#endTimeEditIptId').val(App.getValidTimeFormat(resp.data.endTime));
			$('#featureEditIptId').prop("checked", resp.data.feature);
			$('#activeEditIptId').prop('checked', resp.data.active);
			var businessHour = resp.data.businessHour;
			if (businessHour.mondayStart != '' && businessHour.mondayEnd != '') {
				
			}
			for (var key in businessHour) {
				var end = key.indexOf('Start');
				if (end < 0) {
					end = key.indexOf('End');
				}
				var day = key.substring(0, end);
				if (businessHour[key] == '') {
					var $ele = $('#' + day + 'EditChxId');
					if ($ele.length > 0) {
						$ele.prop('checked', false);
						$ele.trigger('change');
					}
				} else {
					var $ele = $('#' + key + 'EditIptId');
					if ($ele.length > 0) {
						$ele.val(businessHour[key]);
					}
				}
			}
			
			//animate
			App.animateSelect('#cateEditSltId');
			App.animateSelect('#addEditSltId');
			App.animateSwitch('#activeEditIptId');
			App.animateSwitch('#featureEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitEditEvent() {
	$('#editEventBtnId').trigger('click');
}

function openEventDeleteModal(id) {
	var modal = $('#deleteEventMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete event will remove permanently event out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteEvent() {
	$('#deleteEventBtnId').trigger('click');
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
	
	$(warningMsg).text('Warning! Delete category causes deleting their events.');
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
	var eventName = $('#eventSearchIptId').val().trim();
	if (eventName.length > 0) {
		var servletPath = "/event/event?name=" + eventName; 
		App.redirect(servletPath);
	}
}