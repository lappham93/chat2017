/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/applicant";
var applicantStatus = {PENDING: 1, QUALIFIED: 2, DISQUALIFIED: 3, PAYMENT_RECEIVED: 4, APPROVED: 5, REJECTED: 6}

$(function(){
	$('#qualifyApplicantFrmId').submit(function(e) {
		App.showLoader("Processing", "#qualifyApplicantFrmId");
		//init
		App.initAjaxRequest('#qualifyApplicantFrmId');
		
		var id = $('#idQualifyIptId').val();
		var firstName = $('#firstNameEditIptId').val();
		var lastName = $('#lastNameEditIptId').val();
		var businessName = $('#businessNameEditIptId').val();
		var birthDay = $('#birthDayEditIptId').val();
		var ssn = $('#ssnEditIptId').val();
		var driverLicense = $('#driverLicenseEditIptId').val();
		var addressLine1 = $('#addressLine1EditIptId').val();
		var city = $('#cityEditIptId').val();
		var state = $('#stateEditSltId').val();
		var zipCode = $('#zipCodeEditIptId').val();
		var bankName = $('#bankNameEditIptId').val();
		var bankingAccount = $('#bankingAccountNumberEditIptId').val();
		var routingNumber = $('#routingNumberEditIptId').val();
		var status = $('#disqualifiedIptId').prop('checked') ? applicantStatus.DISQUALIFIED : applicantStatus.QUALIFIED;
		var disqualifiedReason = $('#disqualifiedReasonEditSltId').val();
		
		var data = {'firstName': firstName, 'lastName': lastName, 'businessName': businessName, 'birthDay': birthDay, 'socialSecurityNumberStr': ssn, 'driverLicenseStr': driverLicense,
				'addressLine1': addressLine1, 'city': city, 'state': state, 'zipCode': zipCode, 'bankName': bankName, 'bankingAccountNumber': bankingAccount, 'routingNumber': routingNumber, 
				'status': status, 'disqualifiedReason': disqualifiedReason};
		var url = URI + "/qualify/" + id;
		App.ajaxRequest('PUT', url, data, function(resp) {
			App.callbackAjaxRequest('#qualifyApplicantFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
	$('#approveApplicantFrmId').submit(function(e) {
		App.showLoader("Processing", "#approveApplicantFrmId");
		//init
		App.initAjaxRequest('#approveApplicantFrmId');
		
		var id = $('#idApproveIptId').val();
		var status = $('#statusApproveIptId').prop('checked') ? applicantStatus.APPROVED : applicantStatus.REJECTED;
		var rejectedReason = $('#rejectedReasonEditSltId').val();
		var providerType = $('#providerTypeEditSltId').val();
		var data = {'status': status, 'rejectedReason': rejectedReason, 'providerType': providerType};
		var url = URI + "/approve/" + id;
		App.ajaxRequest('PUT', url, data, function(resp) {
			App.callbackAjaxRequest('#approveApplicantFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
	$('#deleteApplicantFrmId').submit(function(e) {
		App.showLoader("Processing", "#deleteApplicantFrmId");
		//init
		App.initAjaxRequest('#deleteApplicantFrmId');
		var id = $('#idDeleteIptId').val();
		var data = null;
		var url = URI + "/" + id;
		App.ajaxRequest('DELETE', url, data, function(resp){
			App.callbackAjaxRequest('#deleteApplicantFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
	
	$('#checkrApplicantFrmId').submit(function(e) {
		//init
		App.initAjaxRequest('#checkrApplicantFrmId');
		App.showLoader("Create Candidate", "#checkrApplicantFrmId");
		var errorMsg = $(this).find('.form-error');
		var successMsg = $(this).find('.form-success');
		var id = $('#idCheckrIptId').val();
		var checkrUrl = App.adminPrefix + "/checkr/";
		//create candidate
		var url = checkrUrl + "/candidate/" + id;
		App.ajaxRequest('POST', url, null, function(dataResp) {
			if (dataResp.code < 0) {
				$(errorMsg).text(dataResp.msg);
				$(errorMsg).show();
				App.hideLoader();
			} else {
				//upload document
				App.showLoader("Upload Documents", "#checkrApplicantFrmId");
				url = checkrUrl + "/document/" + id;
				App.ajaxRequest('POST', url, null, function(dataResp) {
					if (dataResp.code < 0) {
						$(errorMsg).text(dataResp);
						$(errorMsg).show();
						App.hideLoader();
					} else {
						//create report
						App.showLoader("Create Report", "#checkrApplicantFrmId");
						url = checkrUrl + "/report/" + id;
						App.sajaxRequest('POST', url, null, function(dataResp) {
							App.hideLoader();
							if (dataResp.code < 0) {
								$(errorMsg).text(App.errMsg.createReportErr);
								$(errorMsg).show();
							} else {
								$(successMsg).text(App.succMsg.checkrSucc);
								$(successMsg).show();
								setTimeout(function() {App.reload();}, 3000)
							}
						});
					}
				});
			}
		});
		
		return false;
	});
	
	$('#resetStatusFrmId').submit(function(e) {
		App.showLoader("Create Report", "#resetStatusFrmId");
		//init
		App.initAjaxRequest('#resetStatusFrmId');
		
		var id = $('#idResetStatusIptId').val();
		var status = $('#statusSltId').val();
		var data = {'status': status};
		var url = URI + "/status/reset/" + id;
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#resetStatusFrmId', resp);
			App.hideLoader();
		})
		
		return false;
	});
});

function openApplicantQualifyModal(id) {
	App.showLoader("Create Report", "#qualifyApplicantMdlId");
	var modal = $('#qualifyApplicantMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	$(errorMsg).hide();
	$(successMsg).hide();
	var url = URI + "/" + id;
	App.ajaxRequest('GET', url, null, function(resp) {
		if (resp.code >= 0) {
			$('#idQualifyIptId').val(id);
//			$('#statusSltId').val(resp.data.status.value);
			$('#firstNameEditIptId').val(resp.data.firstName);
			$('#lastNameEditIptId').val(resp.data.lastName);
			$('#businessNameEditIptId').val(resp.data.businessName);
			$('#birthDayEditIptId').val(resp.data.birthDay);
			$('#ssnEditIptId').val(resp.data.socialSecurityNumberStr);
			$('#driverLicenseEditIptId').val(resp.data.driverLicenseStr);
			$('#addressLine1EditIptId').val(resp.data.addressLine1);
			$('#cityEditIptId').val(resp.data.city);
			$('#stateEditSltId').val(resp.data.state);
			$('#zipCodeEditIptId').val(resp.data.zipCode);
			$('#bankNameEditIptId').val(resp.data.bankName);
			$('#bankingAccountNumberEditIptId').val(resp.data.bankingAccountNumber);
			$('#routingNumberEditIptId').val(resp.data.routingNumber);
//			$('#statusSltId').prop('disabled', !resp.data.changed);
//			$('#dobLinkId').prop('href', resp.data.socialSecurityNumber);
			$('#dobLinkId').click(function(){App.imagePopup(resp.data.socialSecurityNumber, "DOB")});
			$('#ssnLinkId').click(function(){App.imagePopup(resp.data.socialSecurityNumber, "DOB")});
			$('#driverLicenseLinkId').click(function(){App.imagePopup(resp.data.driverLicense, "Driver License")});
			$('#rountingNumberLinkId').click(function(){App.imagePopup(resp.data.rountingNumberPhoto, "Driver License")});
			//animate
			App.animateSelect('#stateEditSltId');
//			App.animateSelect('#statusSltId');
			modal.modal({backdrop: 'static', keyboard: false, show: true});
			
		} else {
			errorMsg.text(resp.msg);
			errorMsg.show();
		}
		App.hideLoader();
	});
	
}

function submitApplicantQualify() {
	$('#qualifyApplicantBtnId').trigger('click');
}

function openApplicantDeleteModal(id) {
	var modal = $('#deleteApplicantMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Warning! Delete applicant will delete permanently applicant out of system.');
	$(warningMsg).show();
	$('#idDeleteIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitApplicantDelete() {
	$('#deleteApplicantBtnId').trigger('click');
}

function openCheckrModal(id) {
	var modal = $('#checkrApplicantMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$(warningMsg).text('Checkr fee is $30. Do you want to continue?');
	$(warningMsg).show();
	$('#idCheckrIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitApplicantCheckr() {
	$('#checkrApplicantBtnId').trigger('click');
}

function openReportModal(reportId, candidateId) {
	var modal = $('#reportApplicantMdlId');
	modal.modal({backdrop: 'static', keyboard: false, show: true});
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	$(errorMsg).hide();
	$(successMsg).hide();
	App.showLoader("Loading ...");
	var url = App.adminPrefix + "/checkr/report/" + reportId;
	App.ajaxRequest('GET', url, null, function(resp) {
		if (resp.code >= 0) {
			var data = resp.data;
			$('#statusReportId').text(data.status);
			$('#adjudicationReportId').text(data.adjudication);
			$('#packageReportId').text(data.pkg);
			$('#createdAtReportId').text(data.createdAt);
			$('#completedAtReportId').text(data.completedAt);
			
			if (data.ssnTrace != null) {
				$('#statusSsnId').text(data.ssnTrace.status);
				if (data.ssnTrace.addresses.length > 0) {
					for (var i = 0; i < data.ssnTrace.addresses.length; i++) {
						var address = data.ssnTrace.addresses[i];
						$('#addressSsnId').append("<span> - " + address.street + ", " + address.city + ", " + address.state + "</span><br/>");
					}
				}
			} else {
				$('.ssn ul li a').trigger('click');
			}
			
			if (data.sexOffenderSearch != null) {
				$('#statusSexId').text(data.sexOffenderSearch.status);
				if (data.sexOffenderSearch.records.length > 0) {
					for (var i = 0; i < data.sexOffenderSearch.records.length; i++) {
						var record = data.sexOffenderSearch.records[i];
						$('#recordSexId').append("<span> - " + "Full name: " + record.full_name + ", age: " + record.age + ", dob" + record.dob + 
								", race: " + record.race + ", gender: " + record.gender + ", eye color: " + record.eye_color + ", hair color: " + record.hair_color +
								", height: " + record.heght + ", weight: " + record.weight + ", registration start: " + record.registration_start + ", registration end: " + record.registration_end + 
								"</span><br/>");
					}
				}
			} else {
				$('.sex-offender ul li a').trigger('click');
			}
			
			if (data.nationalCriminalSearch != null) {
				$('#statusNationalCriminalId').text(data.nationalCriminalSearch.status);
				if (data.nationalCriminalSearch.records.length > 0) {
					for (var i = 0; i < data.nationalCriminalSearch.records.length; i++) {
						var record = data.nationalCriminalSearch.records[i];
						$('#recordNationalCriminalId').append("<span> - " + "Case number: " + record.case_number + ", arresting agency: " + record.arresting_agency + ", court jurisdiction" + record.court_jurisdiction + 
								", dob: " + record.dob + ", full name: " + record.full_name + 
								"</span><br/>");
					}
				}
			} else {
				$('.national-criminal ul li a').trigger('click');
			}
			
			$('#countyCriminalDiv').html("");
			if (data.countyCriminalSearchs != null) {
				for (var i = 0; i < data.countyCriminalSearchs.length; i++) {
					var countyCriminalSearch = countyCriminalSearchs[i];
					var countyCriminalEle = "#countyCriminalEle";
					if (i > 0) {
						var newEle = $('#countyCriminalEle').clone();
						newEle.prop('id', newEle.prop('id') + "_" + i);
						$('#countyCriminalDiv').append("<hr/>")
						$('#countyCriminalDiv').append(newEle);
						countyCriminalEle = "#countyCriminalEle" + "_" + i;
					}
					$(countyCriminalEle + " #statusCountyCriminalId").text(countyCriminalSearch.status);
					$(countyCriminalEle + " #countyCountyCriminalId").text(countyCriminalSearch.county);
					$(countyCriminalEle + " #stateCountyCriminalId").text(countyCriminalSearch.state);
					if (countyCriminalSearch.records.length > 0) {
						for (var j = 0; j < countyCriminalSearch.records.length; j++) {
							var record = countyCriminalSearch.records[j];
							$(countyCriminalEle + " #recordCountyCriminalId").append("<span> - " + "Case number: " + record.case_number + ", arresting agency: " + record.arresting_agency + ", court jurisdiction" + record.court_jurisdiction + 
									", dob: " + record.dob + ", full name: " + record.full_name + 
									"</span><br/>");
						}
					}
				}
			} else {
				$('.county-criminal ul li a').trigger('click');
			}
			
			if (data.motorVehicleReport != null) {
				$('#statusMotorVerhicleId').text(data.motorVehicleReport.status);
				$('#fullNameMotorVerhicleId').text(data.motorVehicleReport.full_name);
				$('#licenseNumberMotorVerhicleId').text(data.motorVehicleReport.license_number);
				$('#licenseStateMotorVerhicleId').text(data.motorVehicleReport.license_state);
				$('#licenseTypeMotorVerhicleId').text(data.motorVehicleReport.license_type);
				$('#licenseClassMotorVerhicleId').text(data.motorVehicleReport.license_class);
				$('#expirationDateMotorVerhicleId').text(data.motorVehicleReport.expiration_date);
				$('#issuedDateMotorVerhicleId').text(data.motorVehicleReport.issued_date);
				$('#firstIssuedDateMotorVerhicleId').text(data.motorVehicleReport.first_issued_date);
//				$('#inferredIssuedDateMotorVerhicleId').text(data.motorVehicleReport.inferred_issued_date);
				if (data.restrictions.length > 0) {
					for (var i = 0; i < data.restrictions.length; i++) {
						$('#restrictionMotorVerhicleId').append("<span> - " + data.restrictions[i] + "</span><br/>")
					}
				}
				if (data.accidents.length > 0) {
					for (var i = 0; i < data.accidents.length; i++) {
						var accident = data.accidents[i];
						$('#accidentMotorVerhicleId').append("<span> - " + "Date: " + accident.accident_date + ", description: " + accident.description + 
								", city: " + accident.city + ", county: " + accident.county  + ", state: " + accident.state + ", severity: " + accident.severity + ", violation number: " + accident.violation_number +
								"</span><br/>");
					}
				}
				if (data.violations.length > 0) {
					for (var i = 0; i < data.violations.length; i++) {
						var violation = data.violations[i];
						$('#violationMotorVerhicleId').append("<span> - " + "Type: " + violation.type + ", issuedDate: " + violation.issued_date + 
								", description: " + violation.description + ", city: " + violation.city + ", county: " + violation.county + 
								", state: " + violation.state +
								"</span");
					}
				}
			} else {
				$('.motor-verhicle ul li a').trigger('click');
			}
			
			$('#stateCriminalDiv').html("");
			if (data.stateCriminalSearchs != null) {
				for (var i = 0; i < data.stateCriminalSearchs.length; i++) {
					var stateCriminalSearch = stateCriminalSearchs[i];
					var stateCriminalEle = "#stateCriminalEle";
					if (i > 0) {
						var newEle = $('#stateCriminalEle').clone();
						newEle.prop('id', newEle.prop('id') + "_" + i);
						$('#stateCriminalDiv').append("<hr/>")
						$('#stateCriminalDiv').append(newEle);
						stateCriminalEle = "#stateCriminalEle" + "_" + i;
					}
					$(stateCriminalEle + " #statusStateCriminalId").text(stateCriminalSearch.status);
					$(stateCriminalEle + " #stateStateCriminalId").text(stateCriminalSearch.state);
					if (stateCriminalSearch.records.length > 0) {
						for (var j = 0; j < stateCriminalSearch.records.length; j++) {
							var record = stateCriminalSearch.records[j];
							$(stateCriminalEle + " #recordStateCriminalId").append("<span> - " + "Case number: " + record.case_number + ", arresting agency: " + record.arresting_agency + ", court jurisdiction" + record.court_jurisdiction + 
									", dob: " + record.dob + ", full name: " + record.full_name + 
									"</span><br/>");
						}
					}
				}
			} else {
				$('.state-criminal ul li a').trigger('click');
			}
			
			if (data.candidate != null) {
				$('#firstNameCanId').text(data.candidate.first_name);
				$('#middleNameCanId').text(data.candidate.middle_name);
				$('#lastNameCanId').text(data.candidate.last_name);
				$('#birthdayCanId').text(data.candidate.dob);
				$('#ssnCanId').text(data.candidate.ssn);
				$('#zipcodeCanId').text(data.candidate.zipcode);
				$('#driverLicenseCanId').text(data.candidate.driver_license_number);
				$('#emailCanId').text(data.candidate.email);
				$('#phoneCanId').text(data.candidate.phone);
				$('#createAtCanId').text(data.candidateCreatedAt);
				
			}
			
			App.hideLoader();
		} else {
			$(errorMsg).text(resp.msg);
			$(errorMsg).show();
			App.hideLoader();
		}
	});
}

function disqualifySelect() {
	if ($('#disqualifiedIptId').prop('checked')) {
		$('#firstNameEditIptId').prop("required", false);
		$('#lastNameEditIptId').prop("required", false);
		$('#birthDayEditIptId').prop("required", false);
		$('#addressLine1EditIptId').prop("required", false);
		$('#cityEditIptId').prop("required", false);
		$('#stateEditSltId').prop("required", false);
		$('#zipCodeEditIptId').prop("required", false);
		$('#bankingAccountNumberEditIptId').prop("required", false);
		$('#routingNumberEditIptId').prop("required", false);
		$('#ssnEditIptId').prop('required', false);
		$('#qualifyDivId').hide();
		$('#disqualifiedReasonEditSltId').prop('required', true);
		$('#disqualifiedReasonDivId').show();
	} else {
		$('#firstNameEditIptId').prop("required", true);
		$('#lastNameEditIptId').prop("required", true);
		$('#birthDayEditIptId').prop("required", true);
		$('#addressLine1EditIptId').prop("required", true);
		$('#cityEditIptId').prop("required", true);
		$('#stateEditSltId').prop("required", true);
		$('#zipCodeEditIptId').prop("required", true);
		$('#bankingAccountNumberEditIptId').prop("required", true);
		$('#routingNumberEditIptId').prop("required", true);
		$('#ssnEditIptId').prop('required', true);
		$('#qualifyDivId').show();
		$('#disqualifiedReasonEditSltId').prop('required', false);
		$('#disqualifiedReasonDivId').hide();
	}
}

function openApplicantApproveModal(id) {
	var modal = $('#approveApplicantMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	$(errorMsg).hide();
	$(successMsg).hide();
	$('#idApproveIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitApplicantApprove() {
	$('#approveApplicantBtnId').trigger('click');
}

function openResetStatusModal(id) {
	var modal = $('#resetStatusMdlId');
	var errorMsg = modal.find('.form-error');
	var successMsg = modal.find('.form-success');
	var warningMsg = modal.find('.form-warning');
	$(errorMsg).hide();
	$(successMsg).hide();
	
	$('#idResetStatusIptId').val(id);
	modal.modal({backdrop: 'static', keyboard: false, show: true});
}

function submitResetStatus() {
	$('#resetStatusBtnId').trigger('click');
}

function rejectSelect(status) {
	if (status) {
		$('#rejectedReasonEditSltId').prop('required', true);
		$('#rejectedReasonDivId').show();
		$('#providerTypeEditSltId').prop('required', false);
		$('#approveDivId').hide();
	} else {
		$('#rejectedReasonEditSltId').prop('required', false);
		$('#rejectedReasonDivId').hide();
		$('#providerTypeEditSltId').prop('required', true);
		$('#approveDivId').show();
	}
}

