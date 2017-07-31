var App = {
		
	adminPrefix: '',
	
	PHOTO_UPLOAD_URI: '/upload/photo',
	
	photoType: {PRODUCT:2, EVENT:4, FEED: 5, USER: 7, BANNER: 8, MAP: 9, FACE: 1}, 
	
	updateType: {INFO: 1, PHOTO: 2, OTHER: 3},
	
	dynamicPaging: {target: "", total: 0},
	
	objectType: {UNKNOW: 0, BOOTH: 1, PRODUCT: 2, COMMENT: 3, EVENT: 4, FEED: 5, SUBFEED: 6, 
		USER: 7, BANNER: 8, MAP: 9, ADDRESS: 10, HOME: 11},
	
	errMsg: {
		createCandidateErr: "Can't create candidate.",
		uploadDocumentErr: "Can't upload candidate's documents.",
		createReportErr: "Can't create candidate report."
	},
	
	succMsg: {
		checkrSucc: "Create checkr successfully. Checkr is processing and will take some days to finish."
	},
		
	ajaxRequest: function(method, url, data, callback) {
		data = JSON.stringify(data);
		$.ajax({
	    	  headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
	    	  url: url,
	    	  method: method,
	    	  dataType: 'json',
	    	  data: data,
	    	  success: callback,
	    	  error: function(httpObj) {
	    		  if (httpObj.status == 401) {
	    			  App.redirect("/login");
	    		  } else {
	    			  App.showMessage("Server error + " + httpObj.status);
	    		  }
	    	  }
		});
	},
	
	sajaxRequest: function(method, url, data, callback) {
		data = JSON.stringify(data);
		$.ajax({
	    	  headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
	    	  url: url,
	    	  method: method,
	    	  dataType: 'json',
	    	  data: data,
	    	  success: callback,
	    	  async: false,
	    	  error: function(httpObj) {
	    		  if (httpObj.status == 401) {
	    			  App.redirect(App.adminPrefix + "/login");
	    		  } else {
	    			  App.showMessage("Server error + " + httpObj.status);
	    		  }
	    	  }
		});
	},
	
	ajaxUploadPhotoRequest: function(type, files, successCallback, errorCallback) {
		var data = new FormData();
        data.append("type", type);
        $.each(files, function(key, value) {
        	data.append("photos", value);
        })
		$.ajax({
	        url: App.adminPrefix + App.PHOTO_UPLOAD_URI,
	        type: 'POST',
	        data: data,
	        cache: false,
	        dataType: 'json',
	        processData: false, // Don't process the files
	        contentType: false, // Set content type to false as jQuery will tell the server its a query string request
	        success: successCallback,
	        error: errorCallback,
		})
	},
	
	initAjaxRequest: function(formEle) {
		var errorMsg = $(formEle).find('.form-error');
		var successMsg = $(formEle).find('.form-success');
		var warningMsg = $(formEle).find('.form-warning');
		$(errorMsg).hide();
		$(successMsg).hide();
		$(warningMsg).hide();
	},
	
	callbackAjaxRequest: function(formEle, dataResp) {
		var errorMsg = $(formEle).find('.form-error');
		var successMsg = $(formEle).find('.form-success');
		if (dataResp.code >= 0) {
			$(successMsg).text(dataResp.msg);
			$(successMsg).show();
			setTimeout(function() {
				App.reload();
            }, 2000);
		} else {
			$(errorMsg).text(dataResp.msg);
			$(errorMsg).show();
		}
		App.hideLoader();
	},
	
	callbackAjaxRequest2: function(formEle, dataResp, redirectUri) {
		var errorMsg = $(formEle).find('.form-error');
		var successMsg = $(formEle).find('.form-success');
		if (dataResp.code >= 0) {
			$(successMsg).text(dataResp.msg);
			$(successMsg).show();
			setTimeout(function() {
				App.redirect(redirectUri);
            }, 2000);
		} else {
			$(errorMsg).text(dataResp.msg);
			$(errorMsg).show();
		}
		App.hideLoader();
	},

	redirect: function(uri) {
		window.location.replace(this.adminPrefix + uri);
	},
	
	reload: function() {
		window.location.reload();
	},
	
	animateSelect: function(ele) {
		$(ele).select2();
	}, 
	
	animateSwitch: function(ele) {
		var parent = $(ele).parent();
		var grand = $(parent).parent();
		if ($(grand).hasClass("bootstrap-switch")) {
			var active = $(ele).prop("checked");
			if (active && $(grand).hasClass("bootstrap-switch-off")) {
				$(grand).removeClass("bootstrap-switch-off");
				$(grand).addClass("bootstrap-switch-on");
				$(parent).css("margin-left", "0px");
			} else if (!active && $(grand).hasClass("bootstrap-switch-on")) {
				$(grand).removeClass("bootstrap-switch-on");
				$(grand).addClass("bootstrap-switch-off");
				$(parent).css("margin-left", "-50px");
			}
		}
		$(ele).bootstrapSwitch();
	},
	
	animateMultiSelect: function(ele) {
		var parent = $(ele).parent();
		var eleRmv = $(parent).children('.btn-group');
		$(eleRmv).remove();
		$(ele).data('multiselect', null);
		$(ele).multiselect({
	        includeSelectAllOption: true,
	        enableFiltering: true,
	        templates: {
	            filter: '<li class="multiselect-item multiselect-filter"><i class="icon-search4"></i> <input class="form-control" type="text"></li>'
	        },
	        onSelectAll: function() {
	            $.uniform.update();
	        }
	    });
	},
	
	files: [],
	
	//@param date: MM/dd/yyyy HH:mm:ss
	getValidDateFormat: function(date) {
		return date.substring(6, 10) +  "-" + date.substring(0, 2) + "-" + date.substring(3, 5);
	},
	
	//@param date: MM/dd/yyyy HH:mm:ss
	getValidTimeFormat: function(datetime) {
		return datetime.substring(11, 13) + ":" + datetime.substring(14, 16);
	},
	
	addCookie: function(cname, cvalue, expireMilis) {
		var d = new Date();
		d.setTime(d.getTime() + expireMilis);
		var expires = "expires=" + d.toUTCString();
		document.cookie = cname + "=" + cvalue + ";" + expires;
	},
	
	getCookie: function(cname) {
		var name = cname + "=";
	    var decodedCookie = decodeURIComponent(document.cookie);
	    var ca = decodedCookie.split(';');
	    for(var i = 0; i <ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0) == ' ') {
	            c = c.substring(1);
	        }
	        if (c.indexOf(name) == 0) {
	            return c.substring(name.length, c.length);
	        }
	    }
	    return "";
	},
	
	set2List: function(sourceSet) {
		var target = []
		if (sourceSet != null) {
			sourceSet.forEach(function(ele) {
				target.push(ele);
			})
		}
		return target;
	},
	
	cloneSet: function(sourceSet) {
		var target = new Set();
		return App.appendSet(target, sourceSet);
	},
	
	appendSet: function(target, sourceSet) {
		if (sourceSet != null) {
			sourceSet.forEach(function(ele){
				target.add(ele);
			});
		}
		return target;
	},
	
	cloneMap: function(sourceMap) {
		var target = {};
		return App.appendMap(target, sourceMap);
	}, 
	
	appendMap: function(target, sourceMap) {
		if (sourceMap != null) {
			for (var key in sourceMap){
				target[key] = sourceMap[key];
			};
		}
		return target;
	},
	
	showLoader: function(message, container) {
		if (container) {
			$(container + " .loader .loader-text").text(message);
			$(container + " .loader").show();
		} else {
			$('.loader .loader-text').text(message);
			$('.loader').show();
		}
	},
	
	hideLoader: function() {
		$('.loader').hide();
	},
	
	imagePopup: function(url, name) {
		var newwindow = window.open(url, name, 'height=600,width=600,top=100,left=300');
	    return false;
	},
	
	showMessage: function(message, callback) {
		App.hideLoader();
		bootbox.dialog({
	        message: message,
	        buttons: {
	            "success" : {
	                "label" : "OK",
	                "className" : "btn-sm btn-primary",
	                "callback": callback
	            }
	        }
	    });
	},
	
	buildSimpleGetParam: function(url, paramObject) {
		if (!url) {
			return "";
		}
		url += "?";
		for (var name in paramObject) {
			if (paramObject[name]) {
				url += name + "=" + paramObject[name] + "&";
			}
		}
		if (url[url.length - 1] == '&') {
			url = url.substring(0, url.length - 1);
		}
		return url;
	}, 
	
	renderFloor: function(eventId, $floorEle, floorValue) {
		if ($floorEle.length <= 0) {
			return;
		}
		$floorEle.text('');
		var url = App.adminPrefix + "/floor/event/" + eventId;
		var data = "";
		App.ajaxRequest('GET', url, data, function(resp){
			if (resp.code >= 0) {
				var floors = resp.data;
				if (floors && floors.length > 0) {
					$floorEle.append("<option disabled='disabled' selected='selected' value=''>Select Floor</option>");
					for (var i = 0; i < floors.length; i++) {
						$floorEle.append("<option value='" + floors[i].id + "'>" + floors[i].name + "</option>");
	    			}
					if (floorValue) {
						$floorEle.val(floorValue);
					}
				}
			}
		});
	},
	
	renderFloorNumber: function(eventId, $floorEle, floorNumber) {
		if ($floorEle.length <= 0) {
			return;
		}
		$floorEle.text('');
		var url = App.adminPrefix + "/floor/event/" + eventId;
		var data = "";
		App.ajaxRequest('GET', url, data, function(resp){
			if (resp.code >= 0) {
				var floors = resp.data;
				if (floors && floors.length > 0) {
					$floorEle.append("<option disabled='disabled' selected='selected' value=''>Select Floor</option>");
					for (var i = 0; i < floors.length; i++) {
						$floorEle.append("<option value='" + floors[i].number + "'>" + floors[i].name + "</option>");
	    			}
					if (floorNumber) {
						$floorEle.val(floorNumber);
					}
				}
			}
		});
	},
	
	renderEvent: function(onlyActive, $eventEle, eventValue) {
		if ($eventEle.length <= 0) {
			return;
		}
		$eventEle.text('');
		var url = App.adminPrefix + "/event/event/list/" + onlyActive;
		var data = "";
		App.ajaxRequest('GET', url, data, function(resp){
			if (resp.code >= 0) {
				var events = resp.data;
				if (events && events.length > 0) {
					$eventEle.append("<option disabled='disabled' selected='selected' value=''>Select Event</option>");
					for (var i = 0; i < events.length; i++) {
						$eventEle.append("<option value='" + events[i].id + "'>" + events[i].name + "</option>");
	    			}
					if (eventValue) {
						$eventEle.val(eventValue);
					}
				}
			}
		});
	},
	
	renderBooth: function(onlyActive, eventValue, $boothEle, boothValue) {
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
						$boothEle.append("<option value='" + booths[i].id + "'>" + booths[i].name + "</option>");
	    			}
					if (boothValue) {
						$boothEle.val(boothValue);
					}
				}
			}
		});
	},
	
	renderProduct: function(onlyActive, boothValue, $productEle, productValue) {
		if ($productEle.length <= 0) {
			return;
		}
		$productEle.text('');
		var url = App.adminPrefix + "/product/list/" + boothValue + "/" + onlyActive;
		var data = "";
		App.ajaxRequest('GET', url, data, function(resp){
			if (resp.code >= 0) {
				var products = resp.data;
				if (products && products.length > 0) {
					$productEle.append("<option disabled='disabled' selected='selected' value=''>Select product</option>");
					for (var i = 0; i < products.length; i++) {
						$productEle.append("<option value='" + products[i].id + "'>" + products[i].name + "</option>");
	    			}
					if (productValue) {
						$productEle.val(productValue);
					}
				}
			}
		});
	}
};

$(function(){
	// Switchery
    if (Array.prototype.forEach) {
        var elems = Array.prototype.slice.call(document.querySelectorAll('.switchery'));
        elems.forEach(function(html) {
            var switchery = new Switchery(html);
        });
    }
    else {
        var elems = document.querySelectorAll('.switchery');
        for (var i = 0; i < elems.length; i++) {
            var switchery = new Switchery(elems[i]);
        }
    }


    // Styled checkboxes/radios
    $(".styled, .multiselect-container input").uniform({
        radioClass: 'choice'
    });

    // Update uniform when select between styled and unstyled
    $('.input-group-addon input[type=radio]').on('click', function() {
        $.uniform.update("[name=addon-radio]");
    });



    // Touchspin spinners
    // ------------------------------

    // Basic example
    $(".touchspin-basic").TouchSpin({
        postfix: '<i class="icon-paragraph-justify2"></i>'
    });


    // Postfix
    $(".touchspin-postfix").TouchSpin({
        min: 0,
        max: 100,
        step: 0.1,
        decimals: 2,
        postfix: '%'
    });


    // Prefix
    $(".touchspin-prefix").TouchSpin({
        min: 0,
        max: 100,
        step: 0.1,
        decimals: 2,
        prefix: '$'
    });


    // Init with empty values
    $(".touchspin-empty").TouchSpin();


    // Disable mousewheel
    $(".touchspin-no-mousewheel").TouchSpin({
        mousewheel: false
    });


    // Incremental/decremental steps
    $(".touchspin-step").TouchSpin({
        step: 10
    });


    // Set value
    $(".touchspin-set-value").TouchSpin({
        initval: 40
    });


    // Inside button group
    $(".touchspin-button-group").TouchSpin({
        prefix: "pre",
        postfix: "post"
    });


    // Vertical spinners
    $(".touchspin-vertical").TouchSpin({
        verticalbuttons: true,
        verticalupclass: 'icon-arrow-up22',
        verticaldownclass: 'icon-arrow-down22'
    });

    // Vertical spinners
    $(".touchspin-display-order").TouchSpin({
    	min: 1,
    	max: 1000,
        verticalbuttons: true,
        verticalupclass: 'icon-arrow-up22',
        verticaldownclass: 'icon-arrow-down22'
    });

    // Touchspin colors
    // ------------------------------

    //
    // Addons
    //

    // Default
    $(".touchspin-addon-default").TouchSpin({
        prefix: '<i class="icon-accessibility"></i>',
        postfix: '<i class="icon-paragraph-justify2"></i>'
    });

    // Primary
    $(".touchspin-addon-primary").TouchSpin({
        prefix_extraclass: 'input-group-addon-primary',
        postfix_extraclass: 'input-group-addon-primary',
        prefix: '<i class="icon-accessibility"></i>',
        postfix: '<i class="icon-paragraph-justify2"></i>'
    });

    // Danger
    $(".touchspin-addon-danger").TouchSpin({
        prefix_extraclass: 'input-group-addon-danger',
        postfix_extraclass: 'input-group-addon-danger',
        prefix: '<i class="icon-accessibility"></i>',
        postfix: '<i class="icon-paragraph-justify2"></i>'
    });

    // Success
    $(".touchspin-addon-success").TouchSpin({
        prefix_extraclass: 'input-group-addon-success',
        postfix_extraclass: 'input-group-addon-success',
        prefix: '<i class="icon-accessibility"></i>',
        postfix: '<i class="icon-paragraph-justify2"></i>'
    });

    // Warning
    $(".touchspin-addon-warning").TouchSpin({
        prefix_extraclass: 'input-group-addon-warning',
        postfix_extraclass: 'input-group-addon-warning',
        prefix: '<i class="icon-accessibility"></i>',
        postfix: '<i class="icon-paragraph-justify2"></i>'
    });

    // Info
    $(".touchspin-addon-info").TouchSpin({
        prefix_extraclass: 'input-group-addon-info',
        postfix_extraclass: 'input-group-addon-info',
        prefix: '<i class="icon-accessibility"></i>',
        postfix: '<i class="icon-paragraph-justify2"></i>'
    });


    //
    // Buttons
    //

    // Default
    $(".touchspin-button-default").TouchSpin({
        prefix: '<i class="icon-accessibility"></i>',
        postfix: '<i class="icon-paragraph-justify2"></i>',
        buttondown_class: "btn btn-default",
        buttonup_class: "btn btn-default"
    });

    // Primary
    $(".touchspin-button-primary").TouchSpin({
        prefix: '<i class="icon-accessibility"></i>',
        postfix: '<i class="icon-paragraph-justify2"></i>',
        buttondown_class: "btn btn-primary",
        buttonup_class: "btn btn-primary"
    });

    // Danger
    $(".touchspin-button-danger").TouchSpin({
        prefix: '<i class="icon-accessibility"></i>',
        postfix: '<i class="icon-paragraph-justify2"></i>',
        buttondown_class: "btn btn-danger",
        buttonup_class: "btn btn-danger"
    });

    // Success
    $(".touchspin-button-success").TouchSpin({
        prefix: '<i class="icon-accessibility"></i>',
        postfix: '<i class="icon-paragraph-justify2"></i>',
        buttondown_class: "btn btn-success",
        buttonup_class: "btn btn-success"
    });

    // Warning
    $(".touchspin-button-warning").TouchSpin({
        prefix: '<i class="icon-accessibility"></i>',
        postfix: '<i class="icon-paragraph-justify2"></i>',
        buttondown_class: "btn btn-warning",
        buttonup_class: "btn btn-warning"
    });

    // Info
    $(".touchspin-button-info").TouchSpin({
        prefix: '<i class="icon-accessibility"></i>',
        postfix: '<i class="icon-paragraph-justify2"></i>',
        buttondown_class: "btn btn-info",
        buttonup_class: "btn btn-info"
    });

    // Bootstrap switch
    $(".switch").bootstrapSwitch();
    
    // Image lightbox
    $('[data-popup="lightbox"]').fancybox({
	    padding: 3
    });
    
    // Defaults
    Dropzone.autoDiscover = false;
    
    // Dropzone single file
    $(".dropzone_single").dropzone({
        paramName: "photos", // The name that will be used to transfer the file
        maxFilesize: 10, // MB
        maxFiles: 1,
        dictDefaultMessage: 'Drop file to upload <span>or CLICK</span>',
        acceptedFiles: 'image/*',
        addRemoveLinks: false,
        autoProcessQueue: false,
        init: function() {
            this.on('addedfile', function(file){
            	App.files = [];
            	App.files.push(file);
                if (this.fileTracker) {
                this.removeFile(this.fileTracker);
            }
                this.fileTracker = file;
            });
        }
    });
    
	// Dropzone multiple files
    $(".dropzone_multiple").dropzone({
        paramName: "photos", // The name that will be used to transfer the file
        maxFilesize: 10, // MB
        dictDefaultMessage: 'Drop file to upload <span>or CLICK</span>',
        acceptedFiles: 'image/*',
        addRemoveLinks: true,
        autoProcessQueue: false,
        init: function() {
            this.on('addedfile', function(file){
            	App.files.push(file);
            });
        }
    });
    
    // Flat style
    $(".bootpag-flat").bootpag({
        total: App.dynamicPaging.total,
        maxVisible: 6,
        leaps: false
        }).on("page", function(event, num){
            $(App.dynamicPaging.target).html("Page " + num); // or some ajax content loading...
    }).children('.pagination').addClass('pagination-flat pagination-sm');
    
});