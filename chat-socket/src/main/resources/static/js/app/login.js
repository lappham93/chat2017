/* ------------------------------------------------------------------------------
 *
 *  # Login page
 *
 *  Specific JS code additions for login and registration pages
 *
 *  Version: 1.0
 *  Latest update: Feb 17, 2017
 *
 * ---------------------------------------------------------------------------- */
var URI = App.adminPrefix + "/login";
var ACCOUNT_KIT_TYPE = 1;
var FACEBOOK_TYPE = 2;
var GOOGLE_TYPE = 3;

$(function(){
	$('#loginFrmId').submit(function(e) {
		App.showLoader("Processing", "#loginFrmId");
		App.initAjaxRequest('#loginFrmId');
		var name = $('#nameIptId').val();
		var password = $('#passwordIptId').val();
		
		var data = {'name': name, 'password': password};
		var url = URI;
		App.ajaxRequest('POST', url, data, function(resp) {
			App.callbackAjaxRequest2('#loginFrmId', resp, "/home");
			App.hideLoader();
		})
		
		return false;
	})
});

// account kit login callback
function accountKitLoginCallback(response) {
	if (response.status === "PARTIALLY_AUTHENTICATED") {
		var code = response.code;
		var csrf = response.state;
		// Send code to server to exchange for access token
		var url = App.adminPrefix + "/login/shop";
		var data = {
			'token' : code,
			'type' : ACCOUNT_KIT_TYPE,
			'csrf' : csrf
		};
		App.ajaxRequest('POST', url, data, function(data) {
			if (data.code >= 0) {
				var dataResp = data.data;
				if (dataResp.profile == null) {
					//handler register
					var phone = dataResp.socialProfile.phone;
					var email = dataResp.socialProfile.email;
					$('#emailIptId').val(email);
					$('#phoneIptId').val(phone);
					$('#typeHdnId').val(ACCOUNT_KIT_TYPE);
					$('#tokenHdnId').val(code);
					
					$('#loginFrmId').fadeOut(100, function() {
						$('#registerFrmId').fadeIn(100);
					})
				} else {
					App.redirect("/");
				}
			} else {
				//handler show error
				alert(data.msg);
			}
		});
	} else if (response.status === "NOT_AUTHENTICATED") {
		// handle authentication failure
		alert("Authentication failure");
	} else if (response.status === "BAD_PARAMS") {
		// handle bad parameters
		alert("Parameters invalid");
	}
}

// account kit phone form submission handler
function accountKitSMSLogin() {
	var countryCode = "+1";
	var phoneNumber = "";
	AccountKit.login(
		'PHONE',
		{
			countryCode : countryCode,
			phoneNumber : phoneNumber
		}, // will use default values if not specified
		accountKitLoginCallback
	);
	return false;
}

// account kit email form submission handler
function accountKitEmailLogin() {
	var emailAddress = "";
	AccountKit.login(
		'EMAIL',
		{
			emailAddress : emailAddress
		},
		accountKitLoginCallback
	);
	return false;
}

//register user information
function register() {
	return true;
}

