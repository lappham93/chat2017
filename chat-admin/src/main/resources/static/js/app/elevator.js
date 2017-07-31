/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/elevator";
$(function(){
	$('#addElevatorFrmId').submit(function(e) {
		//init
		$(document).scrollTop(0);
		App.showLoader('Processing', '#addElevatorFrmId');
		App.initAjaxRequest('#addElevatorFrmId');
	    
		var eventId = $('#eventAddSltId').val();
		var name = $('#nameAddIptId').val();
		var fromFloor = $('#fromFloorAddSltId').val();
		var toFloor = $('#toFloorAddSltId').val();
		var	isActive = $('#activeAddIptId').prop("checked");
		var data = {'eventId': eventId, 'name': name, 'fromFloorNumber': fromFloor, 'toFloorNumber': toFloor, 'active': isActive};
		var url = URI;
		App.ajaxRequest('POST', url, data, function(resp){
			App.callbackAjaxRequest('#addElevatorFrmId', resp);
		});
		
		return false;
	});
	
	$('#editElevatorFrmId').submit(function(e) {
		$('#editElevatorMdlId').scrollTop(0);
		//init
		App.showLoader('Processing', '#editElevatorFrmId');
		App.initAjaxRequest('#editElevatorFrmId');
		
		var id = $('#idEditIptId').val();
		var eventId = $('#eventEditSltId').val();
		var name = $('#nameEditIptId').val();
		var fromFloor = $('#fromFloorEditSltId').val();
		var toFloor = $('#toFloorEditSltId').val();
		var	isActive = $('#activeEditIptId').prop("checked");
		var url = URI + "/" + id;
		var data = {'eventId': eventId, 'name': name, 'fromFloorNumber': fromFloor, 'toFloorNumber': toFloor, 'active': isActive};
		App.ajaxRequest('PUT', url, data, function(resp) {
			App.callbackAjaxRequest('#editElevatorFrmId', resp);
		})
		
		return false;
	});
	
	$('#deleteElevatorFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteElevatorFrmId');
		App.initAjaxRequest('#deleteElevatorFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteElevatorFrmId', resp);
		})
		
		return false;
	});
	
	$('#eventAddSltId').change(function(){
		App.renderFloorNumber($('#eventAddSltId').val(), $('#fromFloorAddSltId'));
		App.renderFloorNumber($('#eventAddSltId').val(), $('#toFloorAddSltId'));
	});
	
	$('#eventEditSltId').change(function(){
		App.renderFloorNumber($('#eventEditSltId').val(), $('#fromFloorEditSltId'));
		App.renderFloorNumber($('#eventEditSltId').val(), $('#toFloorEditSltId'));
	});
	
	$('#eventSearchSltId').change(function(){
		var event = $(this).val().trim();
		var servletPath = event != '0' ? "/elevator?event=" + event : "/elevator"; 
		App.redirect(servletPath);
	});
	
});

function openElevatorEditModal(id) {
	var modal = $('#editElevatorMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editElevatorMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	var url = URI + "/" + id;
	App.ajaxRequest('GET', url, "", function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#eventEditSltId').val(resp.data.event.id);
			$('#nameEditIptId').val(resp.data.name);
			$('#activeEditIptId').prop('checked', resp.data.active);
			App.renderFloorNumber(resp.data.event.id, $('#fromFloorEditSltId'), resp.data.fromFloor.number);
			App.renderFloorNumber(resp.data.event.id, $('#toFloorEditSltId'), resp.data.toFloor.number);
			//animate
			App.animateSelect('#toFloorEditSltId');
			App.animateSelect('#fromFloorEditSltId');
			App.animateSelect('#eventEditSltId');
			App.animateSwitch('#activeEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitEditElevator() {
	$('#editElevatorBtnId').trigger('click');
}

function openelEvatorDeleteModal(id) {
	var modal = $('#deleteElevatorMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete elevator will remove permanently elevator out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteElevator() {
	$('#deleteElevatorBtnId').trigger('click');
}