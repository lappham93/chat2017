/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/notification";
Photo.type = App.photoType.BANNER;
Photo.url = URI;

$(function(){
	$('#webNotificationFrmId').submit(function(e) {
		//init
		App.showLoader("Processing", "#webNotificationFrmId");
		App.initAjaxRequest('#webNotificationFrmId');
		
		var photoFile = $('#photoIptId')[0].files;
	    
	    App.ajaxUploadPhotoRequest(App.photoType.BANNER, photoFile, function(data){
	    	if (data.code >= 0) {
				var msg = $('#msgAddIptId').val();
				var url = $('#urlAddIptId').val();
				var photoId = data.data.length > 0 ? data.data[0] : 0;
				console.log("photoId = " + photoId);
				var data = {'msg': msg, 'url': url, 'thumb': photoId}
				var url = URI + "/web";
				App.ajaxRequest('POST', url, data, function(resp){
					App.callbackAjaxRequest('#webNotificationFrmId', resp);
					App.hideLoader();
				})
	    	} else {
	    		var errorMsg = $('#webNotificationFrmId').find('.form-error');
	    		errorMsg.text(data.msg);
		    	$(errorMsg).show();
		    	App.hideLoader();
	    	}
	    }, function(){
	    	var errorMsg = $('#webNotificationFrmId').find('.form-error');
	    	$(errorMsg).text("Server error");
	    	$(errorMsg).show();
	    	App.hideLoader();
	    })
		
		return false;
	});
	
	$('#eventNotificationFrmId').submit(function(e) {
		//init
		App.showLoader("Processing", "#eventNotificationFrmId");
		App.initAjaxRequest('#eventNotificationFrmId');
		var msg = $('#msgNEIptId').val();
		var eventId = $('#eventNESltId').val();
		var data = {'message': msg, 'eventId': eventId}
		var url = URI + "/event";
		App.ajaxRequest('POST', url, data, function(resp){
			App.callbackAjaxRequest('#eventNotificationFrmId', resp);
			App.hideLoader();
		})
		return false;
	});
	
	$('#boothNotificationFrmId').submit(function(e) {
		//init
		App.showLoader("Processing", "#boothNotificationFrmId");
		App.initAjaxRequest('#boothNotificationFrmId');
		var msg = $('#msgNBIptId').val();
		var boothId = $('#boothNBSltId').val();
		var data = {'message': msg, 'boothId': boothId}
		var url = URI + "/booth";
		App.ajaxRequest('POST', url, data, function(resp){
			App.callbackAjaxRequest('#boothNotificationFrmId', resp);
			App.hideLoader();
		})
		return false;
	});
	
	$('#productNotificationFrmId').submit(function(e) {
		//init
		App.showLoader("Processing", "#productNotificationFrmId");
		App.initAjaxRequest('#productNotificationFrmId');
		var msg = $('#msgNPIptId').val();
		var productId = $('#productNPSltId').val();
		var data = {'message': msg, 'productId': productId}
		var url = URI + "/product";
		App.ajaxRequest('POST', url, data, function(resp){
			App.callbackAjaxRequest('#productNotificationFrmId', resp);
			App.hideLoader();
		})
		return false;
	});
	
	$('#advNotificationFrmId').submit(function(e) {
		//init
		App.showLoader("Processing", "#advNotificationFrmId");
		App.initAjaxRequest('#advNotificationFrmId');
		var msg = $('#msgNAIptId').val();
		var boothId = $('#boothNASltId').val();
		var data = {'message': msg, 'boothId': boothId}
		var url = URI + "/adv";
		App.ajaxRequest('POST', url, data, function(resp){
			App.callbackAjaxRequest('#advNotificationFrmId', resp);
			App.hideLoader();
		})
		return false;
	});
	
	$('#eventNBSltId').change(function(){
		App.renderBooth(true, $(this).val(), $('#boothNBSltId'));
	});
	
	$('#eventNPSltId').change(function(){
		App.renderBooth(true, $(this).val(), $('#boothNPSltId'));
	});
	
	$('#eventNASltId').change(function(){
		renderBoothAdvertisement(true, $(this).val(), $('#boothNASltId'));
	});
	
	$('#boothNPSltId').change(function(){
		App.renderProduct(true, $(this).val(), $('#productNPSltId'));
	});
});

function renderBoothAdvertisement(onlyActive, eventValue, $boothEle, boothValue) {
	if ($boothEle.length <= 0) {	
		return;
	}
	$boothEle.text('');
	var url = App.adminPrefix + "/booth/booth/list/" + eventValue + "/" + onlyActive;
	var data = "";
	App.ajaxRequest('GET', url, data, function(resp){
		if (resp.code >= 0) {
			var booths = resp.data;
			if (booths && booths.length > 0) {
				$boothEle.append("<option disabled='disabled' selected='selected' value=''>Select Booth</option>");
				for (var i = 0; i < booths.length; i++) {
					if (booths[i].advertised) {
						$boothEle.append("<option value='" + booths[i].id + "'>" + booths[i].name + "</option>");
					}
    			}
				if (boothValue) {
					$boothEle.val(boothValue);
				}
			}
		}
	});
}