/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/maplevel";
$(function(){
	$('#viewMapFrmId').submit(function(e) {
		$(document).scrollTop(0);
		// init
		App.showLoader('Processing', '#viewMapFrmId');
		App.initAjaxRequest('#viewMapFrmId');

		var eventId = $('#eventAddSltId').val();
		var floorId = $('#floorAddSltId').val();
		var data = {
			'event' : eventId,
			'floor' : floorId
		};
		var url = App.buildSimpleGetParam("/maplevel", data);
		App.redirect(url);

		return false;
	});
	
	$('#eventAddSltId').change(function(){
		App.renderFloor($(this).val(), $('#floorAddSltId'))
	});
	
});