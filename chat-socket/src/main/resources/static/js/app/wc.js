/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/wc";
$(function(){
	$('#addWCFrmId').submit(function(e) {
		//init
		$(document).scrollTop(0);
		App.showLoader('Processing', '#addWCFrmId');
		App.initAjaxRequest('#addWCFrmId');
	    
		var eventId = $('#eventAddSltId').val();
		var name = $('#nameAddIptId').val();
		var floorId = $('#floorAddSltId').val();
		var	isActive = $('#activeAddIptId').prop("checked");
		var data = {'eventId': eventId, 'name': name, 'floorId': floorId, 'active': isActive};
		var url = URI;
		App.ajaxRequest('POST', url, data, function(resp){
			App.callbackAjaxRequest('#addWCFrmId', resp);
		});
		
		return false;
	});
	
	$('#editWCFrmId').submit(function(e) {
		$('#editWCMdlId').scrollTop(0);
		//init
		App.showLoader('Processing', '#editWCFrmId');
		App.initAjaxRequest('#editWCFrmId');
		
		var id = $('#idEditIptId').val();
		var eventId = $('#eventEditSltId').val();
		var name = $('#nameEditIptId').val();
		var floorId = $('#floorEditSltId').val();
		var	isActive = $('#activeEditIptId').prop("checked");
		var url = URI + "/" + id;
		var data = {'eventId': eventId, 'name': name, 'floorId': floorId, 'active': isActive};
		App.ajaxRequest('PUT', url, data, function(resp) {
			App.callbackAjaxRequest('#editWCFrmId', resp);
		})
		
		return false;
	});
	
	$('#deleteWCFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteWCFrmId');
		App.initAjaxRequest('#deleteWCFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteWCFrmId', resp);
		})
		
		return false;
	});
	
	$('#eventAddSltId').change(function(){
		App.renderFloor($('#eventAddSltId').val(), $('#floorAddSltId'));
	});
	
	$('#eventEditSltId').change(function(){
		App.renderFloor($('#eventEditSltId').val(), $('#floorEditSltId'));
	});
	
	$('#eventSearchSltId').change(function(){
		var event = $(this).val().trim();
		var servletPath = event != '0' ? "/wc?event=" + event : "/wc"; 
		App.redirect(servletPath);
	});
});

function openWCEditModal(id) {
	var modal = $('#editWCMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editWCMdlId');
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
			App.renderFloor(resp.data.event.id, $('#floorEditSltId'), resp.data.floor.id);
			//animate
			App.animateSelect('#floorEditSltId');
			App.animateSelect('#eventEditSltId');
			App.animateSwitch('#activeEditIptId');
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitEditWC() {
	$('#editWCBtnId').trigger('click');
}

function openWCDeleteModal(id) {
	var modal = $('#deleteWCMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete WC will remove permanently WC out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteWC() {
	$('#deleteWCBtnId').trigger('click');
}