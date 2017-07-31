/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/adv";
$(function(){
	CKEDITOR.replace( 'editor-full', {
        height: '400px',
        extraPlugins: 'forms'
    });
	
	$('#addAdvFrmId').submit(function(e) {
		//init
		
		$(document).scrollTop(0);
		App.showLoader('Processing', '#addAdvFrmId');
		App.initAjaxRequest('#addAdvFrmId');
		//call request to get content
		App.ajaxRequest('GET', URI + "/id", "", function(resp){
			var eventId = $('#eventAddSltId').val();
			var boothId = $('#boothAddSltId').val();
			var content = $('#contentAddIptId').val();
			var	isActive = $('#activeAddIptId').prop("checked");
			var data = {'eventId': eventId, 'boothId': boothId, 'content': content, 'active': isActive};
			var url = URI;
			App.ajaxRequest('POST', url, data, function(resp){
				App.callbackAjaxRequest('#addAdvFrmId', resp);
			});
		});
		
		return false;
	});
	
	$('#editAdvFrmId').submit(function(e) {
		$('#editAdvMdlId').scrollTop(0);
		//init
		App.showLoader('Processing', '#editAdvFrmId');
		App.initAjaxRequest('#editAdvFrmId');
		
		App.ajaxRequest('GET', URI + "/id", "", function(resp){
			var id = $('#idEditIptId').val();
			var eventId = $('#eventEditSltId').val();
			var boothId = $('#boothEditSltId').val();
			var content = $('#contentEditIptId').val();
			var	isActive = $('#activeEditIptId').prop("checked");
			var url = URI + "/" + id;
			var data = {'eventId': eventId, 'boothId': boothId, 'content': content, 'active': isActive};
			App.ajaxRequest('PUT', url, data, function(resp) {
				App.callbackAjaxRequest('#editAdvFrmId', resp);
			})
		});
		
		return false;
	});
	
	$('#deleteAdvFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#deleteAdvFrmId');
		App.initAjaxRequest('#deleteAdvFrmId');
		
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteAdvFrmId', resp);
		})
		
		return false;
	});
	
	$('#eventAddSltId').change(function(){
		App.renderBooth(false, $(this).val(), $('#boothAddSltId'));
	});
	
	$('#eventEditSltId').change(function(){
		App.renderBooth(false, $(this).val(), $('#boothEditSltId'));
	});
	
	$('#eventSearchSltId').change(function(){
		var event = $(this).val().trim();
		var servletPath = event != '0' ? "/adv?event=" + event : "/adv"; 
		App.redirect(servletPath);
	});
});

function openAdvEditModal(id) {
	var modal = $('#editAdvMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	App.showLoader('Loading', '#editAdvMdlId');
	$(errorMsg).hide();
	$(successMsg).hide();
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	var url = URI + "/" + id;
	App.ajaxRequest('GET', url, "", function(resp){
		if (resp.code >= 0) {
			$('#idEditIptId').val(id);
			$('#eventEditSltId').val(resp.data.event.id);
			$('#contentEditIptId').val(resp.data.content);
			$('#activeEditIptId').prop('checked', resp.data.active);
			App.renderBooth(false, resp.data.event.id, $('#boothEditSltId'), resp.data.booth.id);
			//animate
			App.animateSelect('#boothEditSltId');
			App.animateSelect('#eventEditSltId');
			App.animateSwitch('#activeEditIptId');
			CKEDITOR.replace( 'editor-full-edit', {
		        height: '400px',
		        extraPlugins: 'forms'
		    });
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	})
}

function submitEditAdv() {
	$('#editAdvBtnId').trigger('click');
}

function openAdvDeleteModal(id) {
	var modal = $('#deleteAdvMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete Advertisement will remove permanently advertisement out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitDeleteAdv() {
	$('#deleteAdvBtnId').trigger('click');
}