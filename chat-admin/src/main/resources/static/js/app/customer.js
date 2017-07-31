/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/customer";
$(function(){
	$('#deleteCustomerFrmId').submit(function(e) {
		//init
		App.initAjaxRequest('#deleteCustomerFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteCustomerFrmId', resp);
		})
		
		return false;
	});
	
	$('#activeCustomerFrmId').submit(function(e) {
		//init
		App.initAjaxRequest('#activeCustomerFrmId');
		
		var id = $('#idActiveIptId').val();
		var data = null;
		var url = URI + "/" + id + "/active";
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#activeCustomerFrmId', resp);
		})
		
		return false;
	});
	
	$('#inactiveCustomerFrmId').submit(function(e) {
		//init
		App.initAjaxRequest('#inactiveCustomerFrmId');
		
		var id = $('#idInactiveIptId').val();
		var data = null;
		var url = URI + "/" + id + "/inactive";
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#inactiveCustomerFrmId', resp);
		})
		
		return false;
	});
	
});

function openCustomerDeleteModal(id) {
	var modal = $('#deleteCustomerMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete customer will remove permanently customer out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitCustomerDelete() {
	$('#deleteCustomerBtnId').trigger('click');
}

function openCustomerActiveModal(id) {
	var modal = $('#activeCustomerMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Are you sure you want to active this user?');
	$(warningMsg).show();
	$('#idActiveIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitCustomerActive() {
	$('#activeCustomerBtnId').trigger('click');
}

function openCustomerInactiveModal(id) {
	var modal = $('#inactiveCustomerMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Are you sure you want to ban this user?');
	$(warningMsg).show();
	$('#idInactiveIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitCustomerInactive() {
	$('#inactiveCustomerBtnId').trigger('click');
}

function search() {
	var query = $('#customerSearchIptId').val().trim();
	if (query.length > 0) {
		var servletPath = "/customer?query=" + query; 
		App.redirect(servletPath);
	}
}
