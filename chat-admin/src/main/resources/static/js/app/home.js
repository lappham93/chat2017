var URI = App.adminPrefix;

var zipCodes = null;
var appStat = null;

$(function() {
    $('#dateRangeBtnId span').html(moment().subtract(29, 'days').format('MMMM D, YYYY') + ' &nbsp; - &nbsp; ' + moment().format('MMMM D, YYYY'));
    $('#dateRangeBtnId').daterangepicker(
        {
            startDate: moment().subtract(29, 'days'),
            endDate: moment(),
            minDate: '01/01/2017',
            // maxDate: '12/31/2016',
            dateLimit: { days: 60 },
            ranges: {
                'Today': [moment(), moment()],
                'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                'This Month': [moment().startOf('month'), moment().endOf('month')],
                'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
            },
            opens: 'left',
            applyClass: 'btn-small bg-slate-600',
            cancelClass: 'btn-small btn-default'
        },
        function(start, end) {
            $('#dateRangeBtnId span').html(start.format('MMMM D, YYYY') + ' &nbsp; - &nbsp; ' + end.format('MMMM D, YYYY'));
        }
    );

    $('#stateAddSltId').change(function() {
        var $cityAddSltId = $('#cityAddSltId');
        $cityAddSltId.text("");
        var state = $(this).val();
        var url = App.adminPrefix + "/zipcode/state/" + state + "/city";
        var data = null;
        App.ajaxRequest('GET', url, data, function(resp){
            $cityAddSltId.append("<option disabled='disabled' selected='selected' value=''>Select City</option>");
            resp.forEach(function(city) {
                $cityAddSltId.append("<option value='" + city.id + "'>" + city.name+ "</option>");
            });
        });
    });

    $('#cityAddSltId').change(function() {
        var $zipcodeAddSltId = $('#zipcodeAddSltId');
        $zipcodeAddSltId.text("");
        var city = $(this).val();
        var url = App.adminPrefix + "/zipcode/city/" + city + "/zipcode";
        var data = null;
        App.ajaxRequest('GET', url, data, function(resp){
            resp.forEach(function(zipCode) {
                $zipcodeAddSltId.append("<option value='" + zipCode.code + "'>" + zipCode.code+ "</option>");
            });
        });
    });

    require.config({
        paths: {
            echarts: App.adminPrefix + '/static/limitless/js/plugins/visualization/echarts'
        }
    });

    require(
        [
            'echarts',
            'echarts/theme/limitless',
            'echarts/chart/bar',
            'echarts/chart/line',
            'echarts/chart/pie'
        ],


        // Charts setup
        function (ec, limitless) {

            $('#reportFilterFrmId').submit(function(e) {
                // App.showLoader("Processing", "#reportFilterFrmId");
                // App.initAjaxRequest('#reportFilterFrmId');
                var $this = $(this);
                var data = $this.serialize();
                var dateRange = $('#dateRangeBtnId span').html().split(' &nbsp; - &nbsp; ');
                data += "&from=" + dateRange[0] + "&to=" + dateRange[1];
                var url = URI + "/stat";
                App.ajaxRequest('GET', url + "?" + data, null, function(resp){
                    renderSignUpChart(ec, limitless, resp.data);
                    renderSocialChart(ec, limitless, resp.data);
                    // App.callbackAjaxRequest('#reportFilterFrmId', resp);
                    // App.hideLoader();
                });

                return false;
            });

            $('#reportFilterFrmId').trigger('submit');
            
            renderEventPie(ec, limitless);
        }

    )
});

function renderSignUpChart(ec, limitless, data) {

    var legendData = ['User Sign Up'];
    var xAxisData = [];
    var signUpData = [];
    if (data != null) {
	    $.each(data.stats, function (index, stat) {
	        xAxisData.push(stat.statTime);
	        signUpData.push(stat.appStatTotal.totalSignUp);
	    });
    }

    var seriesData = [
        {
            name: 'User Sign Up',
            type: 'line',
            data: signUpData
        }
    ];

    createChart(ec, limitless, '#signUpChart', legendData, xAxisData, seriesData);
}
//charts, 

function renderSocialChart(ec, limitless, data) {

    var legendData = ['Post', 'Comment', 'Like', 'Follow', 'Share'];
    var xAxisData = [];
    var postData = [];
    var commentData = [];
    var likeData = [];
    var followData = [];
    var shareData = [];
    if (data != null) {
	    $.each(data.stats, function (index, stat) {
	        xAxisData.push(stat.statTime);
	        postData.push(stat.appStatTotal.totalFeed);
	        commentData.push(stat.appStatTotal.totalComment);
	        likeData.push(stat.appStatTotal.totalLike);
	        followData.push(stat.appStatTotal.totalFollow);
	        shareData.push(stat.appStatTotal.totalShare);
	    });
    }

    var seriesData = [
        {
            name: 'Post',
            type: 'line',
            data: postData
        },
        {
            name: 'Comment',
            type: 'line',
            data: commentData
        },
        {
            name: 'Like',
            type: 'line',
            data: likeData
        },
        {
            name: 'Follow',
            type: 'line',
            data: followData
        },
        {
            name: 'Share',
            type: 'line',
            data: shareData
        },
    ];

    createChart(ec, limitless, '#socialChart', legendData, xAxisData, seriesData);
}

function renderEventPie(ec, limitless) {
	if (appStat != null) {
		var legendData = ["Finished", "Ongoing", "Upcoming"];
		var seriesData = [
			{value: appStat.totalFinishEvent, name: 'Finished'},
	        {value: appStat.totalOngoingEvent, name: 'Ongoing'},
	        {value: appStat.totalUpcomingEvent, name: 'Upcoming'}
	      ];
		var xAxixData = [];
		createPie(ec, limitless, "#eventPie", legendData, xAxixData, seriesData);
	}
}

function createChart(ec, limitless, chartId, legendData, xAxisData, seriesData, customColors, valueFormat) {
    // Initialize charts
    // ------------------------------

    var chart = ec.init($(chartId)[0], limitless);

    // Charts setup
    // ------------------------------

    //
    // Chart options
    //

    var legendTextLength = 0;

    $.each(legendData, function(index, legend) {
        legendTextLength += legend.length;
    });

    if (!valueFormat) {
        valueFormat = '{value}';
    }

    var chartOptions = {

        // Setup grid
        grid: {
            x: 40,
            x2: 40,
            y: 5 + 28 * Math.ceil(legendTextLength / 100),
            y2: 25
        },

        // Add tooltip
        tooltip: {
            trigger: 'axis'
        },

        // Add legend
        legend: {
            data: legendData
        },

        // Add custom colors
        color: customColors,

        // Enable drag recalculate
        calculable: true,

        // Horizontal axis
        xAxis: [{
            type: 'category',
            boundaryGap: false,
            data: xAxisData
        }],

        // Vertical axis
        yAxis: [{
            type: 'value',
            axisLabel: {
                formatter: valueFormat
            }
        }],

        // Add series
        series: seriesData
    };

    // Apply options
    // ------------------------------

    chart.setOption(chartOptions);


    // Resize charts
    // ------------------------------

    window.onresize = function () {
        setTimeout(function () {
            chart.resize();
        }, 200);
    }
}

function createPie(ec, limitless, pieId, legendData, xAxixData, seriesData) {
	var pie = ec.init($(pieId)[0], limitless);
	var pieOption = {
            // Add title
//            title: {
//                text: 'Browser popularity',
//                subtext: 'Open source information',
//                x: 'center'
//            },
            // Add tooltip
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },

            // Add legend
            legend: {
                orient: 'vertical',
                x: 'left',
                data: legendData
            },

            // Display toolbox
//            toolbox: {
//                show: true,
//                orient: 'vertical',
//                feature: {
//                    mark: {
//                        show: true,
//                        title: {
//                            mark: 'Markline switch',
//                            markUndo: 'Undo markline',
//                            markClear: 'Clear markline'
//                        }
//                    },
//                    dataView: {
//                        show: true,
//                        readOnly: false,
//                        title: 'View data',
//                        lang: ['View chart data', 'Close', 'Update']
//                    },
//                    magicType: {
//                        show: true,
//                        title: {
//                            pie: 'Switch to pies',
//                            funnel: 'Switch to funnel',
//                        },
//                        type: ['pie', 'funnel'],
//                        option: {
//                            funnel: {
//                                x: '25%',
//                                y: '20%',
//                                width: '50%',
//                                height: '70%',
//                                funnelAlign: 'left',
//                                max: 1548
//                            }
//                        }
//                    },
//                    restore: {
//                        show: true,
//                        title: 'Restore'
//                    },
//                    saveAsImage: {
//                        show: true,
//                        title: 'Same as image',
//                        lang: ['Save']
//                    }
//                }
//            },

            // Enable drag recalculate
            calculable: true,

            // Add series
            series: [{
                name: 'Browsers',
                type: 'pie',
                radius: '70%',
                center: ['50%', '57.5%'],
                data: seriesData
            }]
        };
	pie.setOption(pieOption);
	
	window.onresize = function () {
        setTimeout(function () {
            pie.resize();
        }, 200);
    }
}