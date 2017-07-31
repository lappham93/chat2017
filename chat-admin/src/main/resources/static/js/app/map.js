/**
 * lappv POST - create, GET - retrieve PUT - update DELETE - delete
 */
var URI = App.adminPrefix + "/map";

var ACTION = {ADD: 1, DELETE: 2};

var TYPE = {NAVIGATION_POINT: 1, BOOTH: 2, ELEVATOR: 3, ESCALATOR: 4, BEACON: 5, WC: 6, TALK: 7, USER: 8}

class MapObject {
	constructor(name, type, color) {
		this.name = name;
		this.type = type;
		this.color = color;
		this.cc = {};
		this.ncc = {};
		this.dcc = {};
		this.dataPoint = {}; //load from DB
	}
}

var MAP = {
	//MAP.WIdth x Height of raw image.
	WI : 5000,// {{WIDTH_IMG}}; //5000;
	HI : 5389,// {{HEIGHT_IMG}}; //5389;
	LINK: "",
	//MAP.WIdth x Height of map.
	WM : 978,
	HM : 600, // 768;
	//MAP.WIdth x Height of view image.
	WV : 556,// {{WIDTH_VIEW}}; //556; //713;
	HV : 600, // 768;

	// Color
	CL_Green : "#ae2",
	CL_GreenLeaf : "#009688",
	CL_Orange : "#FFA500",
	CL_Brown : "#A52A2A",
	CL_Blue : "#2196F3",
	CL_Violet : "#9C27B0",
	CL_Cham : "#3F51B5",
	CL_Lam : "#00BCD4",
	CL_Red : "#F43636",
	CL_Yellow : "#FFEB3B",
	CL_Gray : "#607D8B",

	// Zoom
	MAX_ZOOM_IN : 10.0,
	MAX_ZOOM_OUT : 0.2,
	zoomStep : 0.2,
	actualZoomLevel : 1.0,
	MOVE_STEP : 100,
	nodes : {},
	nodes_data : {},
	zoomable_layer : {},
	zoom : {},

	// Map Label.
	mapLabel : {},
	mapInfoLabel : {},

	// store data point.
	index : 0,
	// store data edge.
	mapPath : {}, // map store object path SVG.
	mapNEdge : {}, // map store add new object edge.
	mapDEdge : {}, // map store delete old object edge.
	indexEdge : 0,

	nType : 8,

	// data load form DB.
	dataPoint : {},// {{DATA_POINT}};
	dataEdge : {},// {{DATA_EDGE}};
	Circle_Radius: 5,
	Line_Width: 2
	
}

var navigationPointObject = new MapObject('Navigation Point', TYPE.NAVIGATION_POINT, MAP.CL_Green);
var boothObject = new MapObject('Booth', TYPE.BOOTH, MAP.CL_Violet);
var elevatorObject = new MapObject('Elevator', TYPE.ELEVATOR, MAP.CL_Cham);
var escalatorObject = new MapObject('Escalator', TYPE.ESCALATOR, MAP.CL_Lam);
var beaconObject = new MapObject('Beacon', TYPE.BEACON, MAP.CL_Red);
var wCObject = new MapObject('WC', TYPE.WC, MAP.CL_Yellow);
var talkObject = new MapObject('Talk', TYPE.TALK, MAP.CL_Gray);
var userObject  = new MapObject('User', TYPE.USER, MAP.CL_Red);

var mapObject = {navigation: navigationPointObject, 
		booth: boothObject,
		elevator: elevatorObject,
		escalator: escalatorObject,
		beacon: beaconObject,
		wc: wCObject,
		talk: talkObject,
		user: userObject};

$(function() {
	
	$('#viewMapFrmId').submit(function(e) {
		$(document).scrollTop(0);
		// init
		App.showLoader('Processing', '#viewMapFrmId');
		App.initAjaxRequest('#viewMapFrmId');

		var eventId = $('#eventViewSltId').val();
		var floorId = $('#floorViewSltId').val();
		var data = {
			'event' : eventId,
			'floor' : floorId
		};
		var url = App.buildSimpleGetParam("/map", data);
		App.redirect(url);

		return false;
	});

	$('#eventViewSltId').change(function() {
		App.renderFloor($(this).val(), $('#floorViewSltId'));
	});

	var map = d3.select("#mapSvgId").attr("width", MAP.WM).attr("height",
			MAP.HM).on("dblclick", dblclickMap);

	var container = map.append("g");

	// Create the drag and drop behavior to set for the objects crated
	var drag = d3.behavior.drag().origin(function(d) {
		return d;
	}).on("dragstart", dragstarted).on("drag", dragged).on("dragend", dragended);

	// Get the background image from the server and set the viewport size
	// .attr("xlink:href","http://wafi.iit.cnr.it/webvis/tmp/tiger.svg")
	// .attr("xlink:href","{{static-domain}}/map/mapdemo.svg")
	var mapImage = container.append("image")
			.attr('id', "mapimg")
			.attr("xlink:href", MAP.LINK).attr("width", MAP.WV)
			.attr("height", MAP.HV)
			.attr("transform", "translate(0,0)")
			.classed('draggable', true)
			.on("dblclick", dblclickImg)
			.on("mouseup", mouseupImg)
			.on("mousemove", mousemoveImg).call(drag);

	var gcc = container.append("g")
		.attr('id', "gcc")
		.attr("width", MAP.WV)
		.attr("height", MAP.HV).attr("transform", "translate(0,0)");
	// .classed('draggable', true)
	// .on("dblclick", dblclickImg)
	// .call(drag);

	// =========== Tooltips ===========//
	function positionTooltip(mouse, scene, tt) {
		var t = mouse.y - tt.height - 35;
		var l = mouse.x - (tt.width / 2) - 10;
		return {
			top : t,
			left : l
		};
	}

	var tooltip = d3.select("body").append("div").attr("id", "maptt").attr(
			"class", "d3-tip").style("position", "absolute").style("z-index",
			"10").style("visibility", "hidden").text("a simple tooltip");

	var margin = {
		x : 10,
		y : 10
	};
	var padding = {
		x : 10,
		y : 10
	};
	var tt = {
		width : $('#maptt').width(),
		height : $('#maptt').height(),
	}
	var scene = {
		x : margin.x + padding.x,
		y : margin.y + padding.y,
		width : MAP.WV - (margin.x * 2) - (padding.x * 2),
		height : MAP.HV - (margin.y * 2) - (padding.y * 2)
	}

	function showTT() {
		// return tooltip.style("top",
		// (event.pageY-45)+"px").style("left",(event.pageX-20)+"px").style("visibility",
		// "visible").text(text);
		var id = this.id;
		var text = "Id: " + id;

		var clazz = $(this).attr('class');
		console.log("showTT circle clazz: " + clazz);
		var isNew = true;
		var type = '';
		if (clazz && clazz.length > 0) {
			isNew = clazz.indexOf('cnew') > -1 ? true : false;
			for (var i = 1; i <= MAP.nType; i++) {
				var stype = 'type' + i;
				if (clazz.indexOf(stype) > -1) {
					type = '' + i;
					break;
				}
			}
		}
		console.log("showTT circle isNew: " + isNew);
		console.log("showTT circle type: " + type);
		visitMapObject(type, function(object){
			if (type == mapObject.navigation.type) {
				return;
			}
			var old;
			if (isNew) { // circle add new.
				old = object.ncc[id];
			} else { // circle old from dataPointBTH.
				old = object.dataPoint[id];
			}
			if (old) {
				text = text + "<br>" + "Name: " + old.name;
			}
		});
		
		if (this.style) {
			this.style.fill = MAP.CL_Orange;
		}

		tooltip.html(text).style("visibility", "visible").style("opacity", 0.6);
		tt = {
			width : $('#maptt').width(),
			height : $('#maptt').height(),
		}
		var pos = positionTooltip({
			x : event.pageX,
			y : event.pageY
		}, scene, tt);
		return tooltip.style("left", function() {
			return (pos.left) + 'px';
		}).style("top", function() {
			return (pos.top) + 'px';
		});
	}

	function moveTT() {
		// var id = this.id;
		// var text = "nnnnnnnnnnnid: " + id;
		// return tooltip.style("top",
		// (event.pageY-45)+"px").style("left",(event.pageX-20)+"px");
		// //.text(text);

		var pos = positionTooltip({
			x : event.pageX,
			y : event.pageY
		}, scene, tt);

		return tooltip.style("left", function() {
			return (pos.left) + 'px';
		}).style("top", function() {
			return (pos.top) + 'px';
		});
	}

	function hideTT() {
		// tooltip.transition().duration(500).style("opacity", 0);
		var clazz = $(this).attr('class');
		console.log("hideTT circle clazz: " + clazz);
		var type = '';
		if (clazz && clazz.length > 0) {
			isNew = clazz.indexOf('cnew') > -1 ? true : false;
			for (var i = 1; i <= MAP.nType; i++) {
				var stype = 'type' + i;
				if (clazz.indexOf(stype) > -1) {
					type = '' + i;
					break;
				}
			}
		}
		console.log("hideTT circle type: " + type);
		var color = '';
		var $this = $(this);
		visitMapObject(type, function(object){
			if (type == mapObject.navigation.type) {
				if ($this.prop('id').length > 6) {
					color = MAP.CL_GreenLeaf;
				} else {
					color = MAP.CL_Green;
				}
			} else {
				 color = object.color;
			}
		});
		if (this.style) {
			this.style.fill = color;
		}

		return tooltip.style("visibility", "hidden");
	}
	// =========== End Tooltips ===========//

	// =========== Drag ===========//
	// flag tracking drag.
	var drg = false;
	// Called when drag event starts. It stop the propagation of the click event
	function dragstarted(d) {
		console.log("================dragstarted==============");
		drg = false;
		d3.event.sourceEvent.stopPropagation();
	}

	// Called when the drag event occurs (object should be moved)
	function dragged(d) {
		d.x = d3.event.x;
		d.y = d3.event.y;
		// console.log("drag: [x:"+d.x+", y:"+d.y+"]");
		// Translate the object on the actual moved point
		d3.select(this).attr({
			transform : "translate(" + d.x + "," + d.y + ")"
		});
		gcc.attr({
			transform : "translate(" + d.x + "," + d.y + ")"
		});

		drg = true;

	}

	function dragended(d) {
		// d3.event.sourceEvent.stopPropagation();
		console.log("================dragended==============");

		// If has drag, update position list circles.
		if (drg) {

		}
		drg = false;
	}

	// =========== Event Mouse DoubleClick ===========//
	function drawCircle(idp, x, y, text, type) {
		console.log("drawCircle at: [x:" + x + ", y:" + y + "]");
		var stype = 'type' + type;
		var id = idp != '' ? idp : MAP.index;
		var color = ''; // = idp != '' ? CL_GreenLeaf : CL_Green;
		var clazz = idp != '' ? "cold" : "cnew";
		clazz = clazz + " " + stype;
		var c;
		visitMapObject(type, function(object) {
			if (!object) {
				return '';
			}
			var radius = type == mapObject.user.type ? 2 * MAP.Circle_Radius / 3 : MAP.Circle_Radius;
			if (type == mapObject.navigation.type) {
				color = idp != '' ? MAP.CL_GreenLeaf : MAP.CL_Green;
				c = gcc.append("circle").attr("id", id).attr("class",
						"node " + clazz).attr("cx", x).attr("cy", y).attr("r", radius)
						.on("dblclick", dblclickCC).on("mouseover", showTT).on(
								"mousemove", moveTT).on("mouseout", hideTT).on(
								"mousedown", mousedownCC).on("mousemove",
								mousemoveCC).on("mouseup", mouseupCC).style("fill",
								color);
			} else {
				color = object.color;
				c = gcc.append("circle").attr("id", id).attr("class",
						"node " + clazz).attr("cx", x).attr("cy", y).attr("r", radius)
						.on("dblclick", dblclickCC).on("mouseover", showTT).on(
								"mousemove", moveTT).on("mouseout", hideTT).style(
								"fill", color);
			}
			object.cc[id] = c;
			var smapCC = JSON.stringify(object.cc);
			console.log("drawCircle objecttype = " + type + ": " + smapCC);
			return c;
		});
		// map label
		if (text) {
			var tid = id + "_t";
			var t = gcc.append("text").attr("id", tid).attr("class", "wrap")
					.attr("x", x + 3).attr("y", y).text(text).style(
							"font-size", "9");
			var infoLabel = {
				text : text,
				x : x,
				y : y
			};
			MAP.mapLabel[tid] = t;
			MAP.mapInfoLabel[tid] = infoLabel;
		}
		
		return c;
	}

	function dblclickCC(d) {
		console.log("---------------dblclickCC---------------");
		var clazz = $(this).attr('class');
		console.log("delete circle clazz: " + clazz);
		var sid = this.id;
		console.log("delete circle sid: " + sid);

		var isNew = true;
		var type = '';
		if (clazz && clazz.length > 0) {
			isNew = clazz.indexOf('cnew') > -1 ? true : false;
			for (var i = 1; i <= MAP.nType; i++) {
				var stype = 'type' + i;
				if (clazz.indexOf(stype) > -1) {
					type = '' + i;
					break;
				}
			}
		}
		console.log("delete circle isNew: " + isNew);
		console.log("delete circle type: " + type);
		
		visitMapObject(type, function(object){
			if (!object) {
				return "";
			}
			
			var id = sid;
			var c = object.cc[id];
			if (c != null) {
				c.remove();
			}
			if (type == mapObject.navigation.type) {
				deleteEdgeByCC(id);
			}				
			delete object.cc[id];

			if (isNew) { // delete circle add new.
				//show in select
				addToSelect(type, object.ncc[id].id, object.ncc[id].name);
				delete object.ncc[id];
			} else { // delete circle old.
				// Add to mapDPCC and Delete from dataPoint.
				//remove from select
				addToSelect(type, object.dataPoint[id].id, object.dataPoint[id].name);
				var oldp = object.dataPoint[id];
				if (oldp) {
					object.dcc[id] = oldp;
				}
				delete object.dataPoint[id];
			}
		});

		// delete text name.
		var tid = sid + '_t';
		var t = MAP.mapLabel[tid];
		if (t) {
			t.remove();
		}
		if (MAP.mapInfoLabel[tid]) {
			delete MAP.mapInfoLabel[tid];
		}

		hideTT();

		// print console.
		visitMapObject(type, function(object){
			var smapCC = JSON.stringify(object.cc);
			console.log("dblclickCC smapCC: " + smapCC);
			var smapNPCC = JSON.stringify(object.ncc);
			console.log("dblclickCC mapNPCC: " + smapNPCC);
			var smapDPCC = JSON.stringify(object.dcc);
			console.log("dblclickCC mapDPCC: " + smapDPCC);
		});

		return false;
	}

	function dblclickMap(d) {
		var coordinates = [ 0, 0 ];
		coordinates = d3.mouse(this);
		var mx = coordinates[0];
		var my = coordinates[1];
		console.log("dblclick on map at: [x:" + mx + ", y:" + my + "]");
	}

	function dblclickImg(d) {

		// Position of image (id=mapimg)MAP.WIth map.
		var currentx = d3.transform(d3.select(this).attr("transform")).translate[0];
		var currenty = d3.transform(d3.select(this).attr("transform")).translate[1];
		console.log("position current img at: [x:" + currentx + ", y:"
				+ currenty + "]");

		var x = d3.event.x;
		var y = d3.event.y;
		console.log("dblclick on d3.event at: [x:" + x + ", y:" + y + "]");

		// Position mouse click on image.
		var coordinates = [ 0, 0 ];
		coordinates = d3.mouse(this);
		var mx = coordinates[0];
		var my = coordinates[1];
		console.log("dblclick on image at: [x:" + mx + ", y:" + my + "]");

		// Position circle (unit pixel) on image.
		// convert coordinate of View SVG to coordinate of image.
		var ix = (mx * MAP.WI) / MAP.WV;
		var iy = (my * MAP.HI) / MAP.HV;

		// Position circle on map.
		// var cx = currentx + mx;
		// var cy = currenty + my;
		var cx = mx;
		var cy = my;

		showDL(cx, cy, ix, iy);

	}

	function trackingPointCC(x, y, type, id, name) {
		console.log('trackingPointCC(x=' + x + ', y=' + y + ', type=' + type
				+ ', id=' + id + ', name=' + name + ')');
		// var key = id != '' ? id : index;
		visitMapObject(type, function(object){
			var pcc;
			if (type == mapObject.navigation.type) {
				pcc = {
						x : x,
						y : y
					};
			} else {
				pcc = {
						x : x,
						y : y,
						id : id,
						name : name
					};
			}
			object.ncc[MAP.index] = pcc;
			// print console.
			var smapNPCC = JSON.stringify(object.ncc);
			console.log("Put mapNPCC type = " + type + ": " + smapNPCC);
		});
	}

	// =========== DrawLine ===========//
	// var mapNEdge = {};
	// var indexEdge = 0;
	var xy0, xy1, path, keep = false, line = d3.svg.line().x(function(d) {
		return d[0];
	}).y(function(d) {
		return d[1];
	});
	var idBegin = -1;
	var idEnd = -1;
	function mousedownCC() {
		console.log('.....mousedownCC.....');
		path = null;
		keep = true;
		xy0 = d3.mouse(this);
		var sxy0 = JSON.stringify(xy0);
		console.log("mousedownCC xy0: " + sxy0);
		idBegin = this.id;
		console.log("mousedownCC idBegin: " + idBegin);
		if (idBegin != -1) {
			// var id = parseInt(idBegin);
			var c = mapObject.navigation.cc[idBegin];
			if (c != null) {
				xy0 = [ parseFloat(c.attr("cx")), parseFloat(c.attr("cy")) ];
			}
		}
		path = d3.select('#gcc').append('path').attr("id", MAP.indexEdge).attr('d',
				line([ xy0, xy0 ])).on("dblclick", dblclickLine).on(
				"mouseover", overPath).on("mouseout", outPath).style({
			'stroke' : MAP.CL_Brown,
			'stroke-width' : MAP.Line_Width
		});
	}

	function mousemoveCC() {
		console.log('.....mousemoveCC.....');
		if (keep) {
			Line = line([ xy0, d3.mouse(this).map(function(x) {
				return x - 1;
			}) ]);
			// console.log(Line);
			path.attr('d', Line);
		}
	}

	function mouseupCC() {
		console.log('.....mouseupCC.....');
		idEnd = this.id;
		console.log("mouseupCC idEnd: " + idEnd);
		if (keep) {
			keep = false;
			if (idEnd != -1 && idEnd != idBegin) {
				// var id = parseInt(idEnd);
				var c = mapObject.navigation.cc[idEnd];
				if (c != null) {
					xy1 = [ parseFloat(c.attr("cx")), parseFloat(c.attr("cy")) ];
				}
				Line = line([ xy0, xy1 ]);
				console.log(Line);
				path.attr('d', Line);

				// check idBegin && idEnd has exist.
				if (!isExistEdge(idBegin, idEnd)) {
					var oEdge = {
						ib : idBegin,
						ie : idEnd
					};
					MAP.mapNEdge[MAP.indexEdge] = oEdge;
					MAP.mapPath[MAP.indexEdge] = path;
					MAP.indexEdge++;
				} else {
					console.log("xxxxxxx Edge Exist xxxxxx");
					path.remove();
				}
				var smapNEdge = JSON.stringify(MAP.mapNEdge);
				console.log("Print mapNEdge: " + smapNEdge);
				var smapPath = JSON.stringify(MAP.mapPath);
				console.log("Print mapPath: " + smapPath);
			} else {
				path.remove();
			}
		}

		idBegin = -1;
		idEnd = -1;
	}

	function isExistEdge(idBegin, idEnd) {
		if (MAP.mapNEdge) {
			for ( var i in MAP.mapNEdge) {
				var oEdge = MAP.mapNEdge[i];
				if ((idBegin == oEdge.ib || idBegin == oEdge.ie)
						&& (idEnd == oEdge.ib || idEnd == oEdge.ie)) {
					return true;
				}
			}
		}
		if (MAP.dataEdge) {
			for ( var i in MAP.dataEdge) {
				var oEdge = MAP.dataEdge[i];
				if ((idBegin == oEdge.ib || idBegin == oEdge.ie)
						&& (idEnd == oEdge.ib || idEnd == oEdge.ie)) {
					return true;
				}
			}
		}
		return false;
	}

	function mousemoveImg() {
		// console.log('.....mousemoveImg.....');
		if (keep) {
			Line = line([ xy0, d3.mouse(this).map(function(x) {
				return x - 1;
			}) ]);
			// console.log(Line);
			path.attr('d', Line);
		}
	}

	function mouseupImg() {
		console.log('.....mouseupIIIIIIII.....');
		if (keep && path) {
			path.remove();
		}
		keep = false;
		idBegin = -1;
		idEnd = -1;
	}

	function dblclickLine(d) {
		var pid = this.id;
		console.log("Delete mapNEdge[" + pid + "]");
		if (MAP.mapNEdge[pid]) {
			delete MAP.mapNEdge[pid];
		}
		if (MAP.dataEdge[pid]) {
			var op = MAP.dataEdge[pid];
			MAP.mapDEdge[pid] = op;
			delete MAP.dataEdge[pid];
		}
		delete MAP.mapPath[pid];
		this.remove();

		var smapNEdge = JSON.stringify(MAP.mapNEdge);
		console.log("Print mapNEdge: " + smapNEdge);
		var smapDEdge = JSON.stringify(MAP.mapDEdge);
		console.log("Print mapDEdge: " + smapDEdge);
		var sdataEdge = JSON.stringify(MAP.dataEdge);
		console.log("Print dataEdge: " + sdataEdge);
	}

	// delete all edge has coordinate belong to point is deleted.
	function deleteEdgeByCC(idcc) {
		if (MAP.mapNEdge) {
			for ( var i in MAP.mapNEdge) {
				var oEdge = MAP.mapNEdge[i];
				if ((idcc == oEdge.ib || idcc == oEdge.ie)) {
					delete MAP.mapNEdge[i];
					var p = MAP.mapPath[i];
					if (p) {
						p.remove();
					}
					delete MAP.mapPath[i];
				}
			}
		}
		if (MAP.dataEdge) {
			for ( var i in MAP.dataEdge) {
				var oEdge = MAP.dataEdge[i];
				if ((idcc == oEdge.ib || idcc == oEdge.ie)) {
					// Add to map Delete Old Edge mapDEdge.
					MAP.mapDEdge[i] = oEdge;
					// Delete from dataEdge.
					delete MAP.dataEdge[i];
					var p = MAP.mapPath[i];
					if (p) {
						p.remove();
					}
					delete MAP.mapPath[i];
				}
			}
		}

		// print console.
		var smapNEdge = JSON.stringify(MAP.mapNEdge);
		console.log("DeleteEdgeByCC(" + idcc + ") mapNEdge: " + smapNEdge);
		var smapDEdge = JSON.stringify(MAP.mapDEdge);
		console.log("DeleteEdgeByCC(" + idcc + ") mapDEdge: " + smapDEdge);
		var sdataEdge = JSON.stringify(MAP.dataEdge);
		console.log("DeleteEdgeByCC(" + idcc + ") dataEdge: " + sdataEdge);
		var smapPath = JSON.stringify(MAP.mapPath);
		console.log("DeleteEdgeByCC(" + idcc + ") mapPath: " + smapPath);
		return false;
	}

	function overPath() {
		if (this.style) {
			this.style.stroke = MAP.CL_Blue;
		}
	}

	function outPath() {
		if (this.style) {
			this.style.stroke = MAP.CL_Brown;
		}
	}

	// =========== End DrawLine ===========//

	// =========== Zoom ===========//
	// Create the zoom behavior to set for the draw
	MAP.zoom = d3.behavior.zoom().scaleExtent([ MAP.MAX_ZOOM_OUT, MAP.MAX_ZOOM_IN ]).on(
			'zoom', zoomed);

	// Function called on the zoom event. It translate the draw on the zoommed
	// point and scaleMAP.WIth a certain factor
	function zoomed() {
		container.attr("transform", "translate(" + d3.event.translate
				+ ")scale(" + d3.event.scale + ")");
	}

	// Set the zoom behavior on the container variable (the draw), disable
	// mousedown event for the zoom and set the function to call on the double
	// click event .on("mousedown.zoom", null).on("dblclick.zoom", dblclickMap)
	container.call(MAP.zoom).on("mousedown.zoom", null).on("dblclick.zoom", null);

	// Matrix containing the x and y coordinates of the created objects (used
	// for draggable events)
	MAP.nodes_data = [ {
		x : 0,
		y : 0
	}, {
		x : 0,
		y : 0
	}, {
		x : 0,
		y : 0
	}, {
		x : 0,
		y : 0
	}, {
		x : 0,
		y : 0
	}, {
		x : 0,
		y : 0
	}, {
		x : 0,
		y : 0
	}, {
		x : 0,
		y : 0
	}, {
		x : 0,
		y : 0
	} ];

	// Set the drag behavior on the objects having the "draggable" class and set
	// their position on the viewport (by the "node_data" matrix)
	MAP.nodes = container.selectAll(".draggable").call(drag).data(MAP.nodes_data);

	function zoomIn() {
		// Calculate and set the new zoom level
		actualZoomLevel = roundFloat(parseFloat(MAP.actualZoomLevel)
				+ parseFloat(MAP.zoomStep));
		MAP.zoom.scale(MAP.actualZoomLevel);
		// Get the actual position of the container
		var xPosition = d3.transform(container.attr("transform")).translate[0];
		var yPosition = d3.transform(container.attr("transform")).translate[1];
		// Esecute the transformation setting the actual position and the new
		// zoom level
		container.attr("transform", "translate(" + xPosition + ", " + yPosition
				+ ")scale(" + MAP.zoom.scale() + ")");
	}

	function zoomOut() {
		actualZoomLevel = roundFloat(parseFloat(MAP.actualZoomLevel)
				- parseFloat(MAP.zoomStep));
		MAP.zoom.scale(MAP.actualZoomLevel);
		var xPosition = d3.transform(container.attr("transform")).translate[0];
		var yPosition = d3.transform(container.attr("transform")).translate[1];
		container.attr("transform", "translate(" + xPosition + ", " + yPosition
				+ ")scale(" + MAP.zoom.scale() + ")");
	}

	function moveDrawLeft() {
		var xPosition = d3.transform(container.attr("transform")).translate[0];
		var yPosition = d3.transform(container.attr("transform")).translate[1];
		container.attr("transform", "translate(" + (xPosition - MAP.MOVE_STEP)
				+ ", " + yPosition + ")scale(" + MAP.zoom.scale() + ")");
	}

	function moveDrawRight() {
		var xPosition = d3.transform(container.attr("transform")).translate[0];
		var yPosition = d3.transform(container.attr("transform")).translate[1];
		container.attr("transform", "translate(" + (xPosition + MAP.MOVE_STEP)
				+ ", " + yPosition + ")scale(" + MAP.zoom.scale() + ")");
	}

	function moveDrawTop() {
		var xPosition = d3.transform(container.attr("transform")).translate[0];
		var yPosition = d3.transform(container.attr("transform")).translate[1];
		container.attr("transform", "translate(" + xPosition + ", "
				+ (yPosition - MAP.MOVE_STEP) + ")scale(" + MAP.zoom.scale() + ")");
	}

	function moveDrawBottom() {
		var xPosition = d3.transform(container.attr("transform")).translate[0];
		var yPosition = d3.transform(container.attr("transform")).translate[1];
		container.attr("transform", "translate(" + xPosition + ", "
				+ (yPosition + MAP.MOVE_STEP) + ")scale(" + MAP.zoom.scale() + ")");
	}

	function roundFloat(value) {
		return value.toFixed(2);
	}

	function saveMap() {
		var modal = $('#saveMapMdlId');
		var errorMsg = modal.find('.form-error');
		var successMsg = modal.find('.form-success');
		var warningMsg = modal.find('.form-warning');
		$(errorMsg).hide();
		$(successMsg).hide();
		
		$(warningMsg).text('Are you sure you want to save map?');
		$(warningMsg).show();

		// Add new Edge.
		var smapNEdge = JSON.stringify(MAP.mapNEdge);
		console.log("Save ane: " + smapNEdge);
		$('#ane').val(smapNEdge);

		// Delete old Edge.
		var smapDEdge = JSON.stringify(MAP.mapDEdge);
		console.log("Save doe: " + smapDEdge);
		$('#doe').val(smapDEdge);

		modal.modal({backdrop: 'static', keyboard: false, show: true});
	}

	var shLabel = true;
	function showHideLabel() {
		console.log("~~~~~showHideLabel~~~~~~");
		if (shLabel) { // hide all label.
			shLabel = false;
			// remove all label has class 'wrap'.
			d3.selectAll(".wrap").remove();
			delete MAP.mapLabel;
			$('#showHideLabel').text('Show Label');
		} else { // show all label.
			shLabel = true;
			$('#showHideLabel').text('Hide Label');
			if (MAP.mapInfoLabel) {
				for ( var tid in MAP.mapInfoLabel) {
					var infoLabel = MAP.mapInfoLabel[tid];
					var t = gcc.append("text").attr("id", tid).attr("class",
							"wrap").attr("x", infoLabel.x + 3).attr("y",
							infoLabel.y).text(infoLabel.text).style(
							"font-size", "9");
					MAP.mapLabel = [];
					MAP.mapLabel[tid] = t;
				}
			}
		}
		return false;
	}

	// =========== Dialog Add Location ===========//
	// override dialog's title function to allow for HTML titles
	$.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
		_title : function(title) {
			var $title = this.options.title || '&nbsp;'
			if (("title_html" in this.options)
					&& this.options.title_html == true)
				title.html($title);
			else
				title.text($title);
		}
	}));

	var oneEnter = true;
	function showDL(lx, ly, ix, iy) {
		$('#al_alert_err').hide();
		$('#namelc').val('');
		$('#idlc').val(MAP.index);
		$('#lx').val(lx);
		$('#ly').val(ly);
		$('#ix').val(ix);
		$('#iy').val(iy);

		var dialog = $("#dialog-message")
				.removeClass('hide')
				.dialog(
						{
							modal : true,
							title : "<div class='widget-headerMAP.WIdget-header-small'><h4 class='smaller'><i class='ace-icon fa fa-map-marker'></i> Add location</h4></div>",
							title_html : true,
							buttons : [ {
								text : "Cancel",
								"class" : "btn btn-minier ui-state-focus",
								click : function() {
									$(this).dialog("close");
								}
							}, {
								text : "OK",
								"class" : "btn btn-primary btn-minier",
								click : function() {
									submitDLAddPoint();
								}
							} ],
							open : function() {
								// $(this).siblings('.ui-dialog-buttonpane').find("button:contains('Cancel')").focus();
								oneEnter = true;
								$("#namelc").keydown(function(e) {
									if (e.keyCode == $.ui.keyCode.ENTER) {
										console.log('ui.keyCode.ENTER');
										// $("#dialog-message").siblings('.ui-dialog-buttonpane').find("button:contains('OK')").click();
										if (oneEnter) {
											oneEnter = false;
											submitDLAddPoint();
										}
									}
								})
							}
						});
		$('#namelc').focus();
	}
	;

	function submitDLAddPoint() {
		// validate.
		var serr = "";
		var id = "";
		var name = "";
		var type = $('#typeSltId').val();
		visitMapObject(type, function(object){
			if (type == mapObject.booth.type) {
				id = $('#boothSltId').val();
				name = $("#boothSltId option[value='" + id + "']").text();
			} else if (type == mapObject.elevator.type) {
				id = $('#elevatorSltId').val();
				name = $("#elevatorSltId option[value='" + id + "']").text();
			} else if (type == mapObject.escalator.type) {
				id = $("#escalatorSltId").val();
				name = $("#escalatorSltId option[value='" + id + "']").text();
			} else if (type == mapObject.beacon.type) {
				id = $("#beaconSltId").val();
				name = $("#beaconSltId option[value='" + id + "']").text();
			} else if (type == mapObject.talk.type) {
				id = $("#talkSltId").val();
				name = $("#talkSltId option[value='" + id + "']").text();
			} else if (type == mapObject.wc.type) {
				id = $('#wcSltId').val();
				name = $("#wcSltId option[value='" + id + "']").text();
			}
			if (type != mapObject.navigation.type && !id) {
				serr = object.name + " required. ";
			} else {
				var existed = false;
				if (type != mapObject.elevator.type && type != mapObject.escalator.type) {
					if (object.dataPoint) {
						for (var i in object.dataPoint) {
							if (object.dataPoint[i].located && object.dataPoint[i].id == id) {
								existed = true;
								break;
							}
						}
					}
					if (!existed && object.ncc) {
						for (var i in object.ncc) {
							if (object.ncc[i].id == id) {
								existed = true;
								break;
							}
						}
					}
				}
				if (existed) {
					serr = object.name + " have been added. Please delete before placing at new location.";
				}
			}
		});

		if (serr == '') {
			var ix = parseFloat($('#ix').val());
			var iy = parseFloat($('#iy').val());
			var cx = parseFloat($('#lx').val());
			var cy = parseFloat($('#ly').val());
			drawCircle('', cx, cy, name, type);
			trackingPointCC(ix, iy, type, id, name);
			//remove from list select
			removeFromSelect(type, id, name);
			
			++MAP.index;
			$("#dialog-message").dialog("close");
		} else {
			oneEnter = true;
			$('#al_alert_err').html(serr);
			$('#al_alert_err').show();
			setTimeout(function() {
				$('#al_alert_err').hide();
			}, 3000);
		}
	}

	// draw map when page ready.
	$(function() {
		drawMapInit();
	});

	function drawMapInit() {
		for (var objectKey in mapObject) {
			var object = mapObject[objectKey];
			if (object.dataPoint) {
				for ( var idp in object.dataPoint) {
					var op = object.dataPoint[idp];
					if (op && op.located) {
						// convert coordinate of image to coordinate of View SVG.
						var x = (op.x * MAP.WV) /MAP.WI;
						var y = (op.y * MAP.HV) /MAP.HI;
						var name = op.name;
						if (!name) {
							name = '';
						}
						drawCircle(idp, x, y, name, object.type);
					}
				}
			}
			
			var sdataPoint = JSON.stringify(object.dataPoint);
			console.log("dataPoint: " + sdataPoint);
		}
		// print console.
		var sdataEdge = JSON.stringify(MAP.dataEdge);
		console.log("dataEdge: " + sdataEdge);
		// draw edge.
		if (MAP.dataEdge) {
			for ( var ide in MAP.dataEdge) {
				var oe = MAP.dataEdge[ide];
				if (oe) {
					var opb = mapObject.navigation.dataPoint[oe.ib];
					var ope = mapObject.navigation.dataPoint[oe.ie];
					if (opb && ope) {
						// convert coordinate of image to coordinate of View
						// SVG.
						var xb = (opb.x * MAP.WV) /MAP.WI;
						var yb = (opb.y * MAP.HV) /MAP.HI;
						var xyb = [ xb, yb ];

						// convert coordinate of image to coordinate of View
						// SVG.
						var xe = (ope.x * MAP.WV) /MAP.WI;
						var ye = (ope.y * MAP.HV) /MAP.HI;
						var xye = [ xe, ye ];

						path = d3.select('#gcc').append('path').attr("id",
								ide).attr('d', line([ xyb, xye ])).on(
								"dblclick", dblclickLine).on("mouseover",
								overPath).on("mouseout", outPath).style({
							'stroke' : MAP.CL_Brown,
							'stroke-width' : MAP.Line_Width
						});
						MAP.mapPath[MAP.indexEdge] = path;
						MAP.indexEdge++;
					}
				}
			}
		}

	}

	$('#zoomIn').click(function(){ 
		zoomIn();
	});
	$('#zoomOut').click(function(){ 
		zoomOut();
	});
	$('#moveLeft').click(function(){ 
		moveDrawLeft();
	});
	$('#moveRight').click(function(){ 
		moveDrawRight();
	});
	$('#moveTop').click(function(){ 
		moveDrawTop();
	});
	$('#moveBottom').click(function(){ 
		moveDrawBottom();
	});
	$('#showHideLabel').click(function(){ 
		showHideLabel();
	});
	$('#saveMap').click(function(){ 
		saveMap();
	});
	
	$('#saveMapFrmId').submit(function(e) {
		//init
		App.showLoader('Processing', '#saveMapFrmId');
		App.initAjaxRequest('#saveMapFrmId');
		
		var id = $('#idSaveIptId').val();
		var points = [];
		for (var objectKey in mapObject) {
			var object = mapObject[objectKey];
			for (var key in object.ncc) {
				var nid = object.type == TYPE.NAVIGATION_POINT ? key : object.ncc[key].id;
				points.push({ 'id': nid,
						'point': {'x': object.ncc[key].x, 'y': object.ncc[key].y},
						'name': object.ncc[key].name,
						'type': object.type, 
						'action': ACTION.ADD});
			}
			for (var key in object.dcc) {
				points.push({'id': object.dcc[key].id,
						'point': {'x': object.dcc[key].x, 'y': object.dcc[key].y}, 
						'name': object.dcc[key].name,
						'type': object.type, 
						'action': ACTION.DELETE});
			}
		}
		var paths = [];
		for (var objectKey in MAP.mapNEdge) {
			var object = MAP.mapNEdge[objectKey];
			paths.push({'beginId': object.ib,
						'endId': object.ie,
						'action': ACTION.ADD});
		}
		for (var objectKey in MAP.mapDEdge) {
			var object = MAP.mapDEdge[objectKey];
			paths.push({'beginId': object.ib,
						'endId': object.ie,	
						'action': ACTION.DELETE});
		}
		var data = {'floorId': id, 'points': points, 'paths': paths};
		var url = URI + "/" + id;
		App.ajaxRequest('PUT', url, data, function(resp){
			App.callbackAjaxRequest('#saveMapFrmId', resp);
		})
		
		return false;
	});
	
	$('#typeSltId').change(function(){
		changeType($(this).val());
	});
	
	eleInit();
});

function eleInit() {
	mapObject.booth.$divId = $('#boothDivId'); 
	mapObject.booth.$eleId = $('#boothSltId');
	mapObject.elevator.$divId = $('#elevatorDivId'); 
	mapObject.elevator.$eleId = $('#elevatorSltId');
	mapObject.escalator.$divId = $('#escalatorDivId'); 
	mapObject.escalator.$eleId = $('#escalatorSltId');
	mapObject.beacon.$divId = $('#beaconDivId'); 
	mapObject.beacon.$eleId = $('#beaconSltId');
	mapObject.wc.$divId = $('#wcDivId'); 
	mapObject.wc.$eleId = $('#wcSltId');
	mapObject.talk.$divId = $('#talkDivId'); 
	mapObject.talk.$eleId = $('#talkSltId');
	
}

function hideAllType() {
	for (var key in mapObject) {
		var object = mapObject[key];
		if (object.$divId) {
			object.$divId.hide();
			object.$eleId.prop('required', false);
		}
	}
}

function changeType(type) {
	hideAllType();
	visitMapObject(type, function(object){
		if (object && object.$divId) {
			object.$divId.show();
			object.$eleId.prop('required', true);
		}
	});
}

function visitMapObject(type, onProcessFunc) {
	for (objectKey in mapObject) {
		if (mapObject[objectKey].type == type) {
			return onProcessFunc(mapObject[objectKey]);
		}
	}
}

function submitSaveMap() {
	$('#saveMapBtnId').trigger('click');
}

function addToSelect(type, id, name) {
	if (type != mapObject.navigation.type && type != mapObject.elevator.type && type != mapObject.escalator.type) {
		visitMapObject(type, function(object){
			if (object && object.$eleId) {
				object.$eleId.append("<option value='" + id + "'>" + name + "</option>");
			}
		});
	}
}

function removeFromSelect(type, id, name) {
	if (type != mapObject.navigation.type && type != mapObject.elevator.type && type != mapObject.escalator.type) {
		visitMapObject(type, function(object){
			if (object && object.$eleId) {
				$(object.$eleId.find("option[value='" + id + "']")).remove();
			}
		});
	}
}
