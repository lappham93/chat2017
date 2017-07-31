/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/escalator";
$(function(){
	$('#addEscalatorFrmId').submit(function(e) {
		//init
		$(document).scrollTop(0);
		App.showLoader('Processing', '#addEscalatorFrmId');
		App.initAjaxRequest('#addEscalatorFrmId');
	    
		var eventId = $('#eventAddSltId').val();
		var name = $('#nameAddIptId').val();
		var fromFloor = $('#fromFloorAddSltId').val();
		var toFloor = $('#toFloorAddSltId').val();
		var direction = $('#directionAddSltId').val();
		var	isActive = $('#activeAddIptId').prop("checked");
		var data = {'eventId': eventId, 'name': name, 'fromFloorNumber': fromFloor, 'toFloorNumber': toFloor,
				'direction': direction, 'active': isActive};
		var url = URI;
		App.ajaxRequest('POST', url, data, function(resp){
			App.callbackAjaxRequest('#addEscalatorFrmId', resp);
		});
		
		return false;
	});
	
	$('#editEscalatorFrmId').submit(function(e) {
		$('#editEscalatorMdlId').scrollTop(0);
		//init
		App.showLoader('Processing', '#editEscalatorFrmId');
		App.initAjaxRequest('#editEscalatorFrmId');
		
		var id = $('#idEditIptId').val();
		var eventId = $('#eventEditSltId').val();
		var name = $('#nameEditIptId').val();
		var fromFloor = $('#fromFloorEditSltId').val();
		var toFloor = $('#toFloorEditSltId').val();
		var direction = $('#directionEditSltId').val();
		var	isActive = $('#activeEditIptId').prop("checked");
		var url = URI + "/" + id;
		var data = {'eventId': eventId, 'name': name, 'fromFloorNumber': fromFloor, 'toFloorNumber': toFloor, 
				'direction': direction, 'active': isActive};
		App.ajaxRequest('PUT', url, data, function(resp) {
			App.callbackAjaxRequest('#editEscalatorFrmId', resp);
		});
		
		return false;
	});
	
	$('#deleteEscalatorFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteEscalatorFrmId');
		App.initAjaxRequest('#deleteescalatorFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteEscalatorFrmId', resp);
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
		var servletPath = event != '0' ? "/escalator?event=" + event : "/escalator"; 
		App.redirect(servletPath);
	});
	
});

function openEscalatorEditModal(id) {
	var modal = $('#editEscalatorMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editEscalatorMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	var url = URI + "/" + id;
	App.ajaxRequest('GET', url, "", function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#eventEditSltId').val(resp.data.event.id);
			$('#nameEditIptId').val(resp.data.name);
			$('#directionEditSltId').val(resp.data.direction.value);
			$('#activeEditIptId').prop('checked', resp.data.active);
			App.renderFloorNumber(resp.data.event.id, $('#fromFloorEditSltId'), resp.data.fromFloor.number);
			App.renderFloorNumber(resp.data.event.id, $('#toFloorEditSltId'), resp.data.toFloor.number);
			//animate
			App.animateSelect('#toFloorEditSltId');
			App.animateSelect('#fromFloorEditSltId');
			App.animateSelect('#directionEditSltId');
			App.animateSelect('#eventEditSltId');
			App.animateSwitch('#activeEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitEditEscalator() {
	$('#editEscalatorBtnId').trigger('click');
}

function openEscalatorDeleteModal(id) {
	var modal = $('#deleteEscalatorMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete Escalator will remove permanently Escalator out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteEscalator() {
	$('#deleteEscalatorBtnId').trigger('click');
}