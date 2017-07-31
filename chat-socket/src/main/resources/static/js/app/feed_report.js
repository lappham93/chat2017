/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/feedback/feed";

$(function(){
	$('#reviewFeedFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#reviewFeedFrmId');
		App.initAjaxRequest('#reviewFeedFrmId');
		
		var id = $('#idReviewIptId').val();
		var option = $('#optionReviewSltId').val();
		
		var data = '';
		var url = URI + "/" + id + "/review/" + (option == 1);
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#reviewFeedFrmId', resp);
		})
		
		return false;
	});
});

function openFeedReviewModal(id) {
	var modal = $('#reviewFeedMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$('#idReviewIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitReviewFeed() {
	$('#reviewFeedBtnId').trigger('click');
}