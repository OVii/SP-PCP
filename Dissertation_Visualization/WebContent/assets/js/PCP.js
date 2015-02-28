/*
 *A Multi-task Comparative Study on Scatter Plots and Parallel Coordinates Plots
 *Developed as part of an MSc in Computer Science at the University of Oxford
 *Copyright (C) 2014 Rassadarie Kanjanabose
 *
 *This program is free software: you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *You should have received a copy of the GNU General Public License
 *along with this program.  If not, see <http://www.gnu.org/licenses/>. 
*/

// Use D3.js repository "http://mbostock.github.io/d3/talk/20111116/iris-parallel.html" as an example

function drawPCP(directory, fileName) {
	
	var index = fileName.indexOf(".csv");
	var name = fileName.substring(0,index);
	var task = name.substring(0, 2);

	var m = [ 100, 100, 100, 100 ], w = 900, h = 300;
	var x = d3.scale.ordinal().rangePoints([ 0, w ]), y = {};
	var line = d3.svg.line(), axis = d3.svg.axis().orient("left"), foreground;

	// Create the SVG container
	var svg = d3.select("body").append("svg:svg")
			.attr("width", w + m[1] + m[3]).attr("height", h + m[0] + m[2])
			.append("svg:g").attr("transform",
					"translate(" + m[3] + "," + m[0] + ")");
	
	  // Picture Name
	  svg.append("text")
	   .attr("x", -20).attr("y", -50)
	   .attr("fill", "red").attr("font-size", "25px")
	   .attr("font-family", "sans-serif")
	   .text(name);

	// Load data
	d3.csv(directory + task + "/"+ fileName, function(data) {

		// Extract the list of dimensions and create a scale for each
		x.domain(dimensions = d3.keys(data[0]).filter(function(d) {

			// For numerical data
			y[d] = d3.scale.linear().domain([ 0, 100 ]).range([ h, 0 ]);

			return true;
		}));

		// Add foreground lines
		foreground = svg.append("svg:g").attr("class", "foreground").selectAll(
				"path").data(data).enter().append("svg:path").attr("d",
				path).attr("class", "standard");

		// Add a group element for each dimension
		var g = svg.selectAll(".dimensions").data(dimensions).enter().append(
				"svg:g").attr("class", "dimensions").attr("transform",
				function(d) {
					return "translate(" + x(d) + ")";
				}).call(d3.behavior.drag().origin(function(d) {
			return {
				x : x(d)
			};
		}).on("dragstart", dragstart).on("drag", drag).on("dragend", dragend));

		// Add an axis and title
		g.append("svg:g").attr("class", "axis").each(function(d) {
			d3.select(this).call(axis.scale(y[d]).tickPadding(5));
		}).append("svg:text").attr("class", "title").attr("y", -10)
				.text(String);

		// Add and store a brush for each axis
		g.append("svg:g").attr("class", "brush").each(
				function(d) {
					d3.select(this).call(
							y[d].brush = d3.svg.brush().y(y[d]).on("brush",
									brush));
				}).selectAll("rect").attr("x", -8).attr("width", 16);

		// Drag function
		function dragstart(d) {
			i = dimensions.indexOf(d);
		}

		function drag(d) {
			x.range()[i] = d3.event.x;
			dimensions.sort(function(a, b) {
				return x(a) - x(b);
			});
			g.attr("transform", function(d) {
				return "translate(" + x(d) + ")";
			});
			foreground.attr("d", path);
		}

		function dragend(d) {
			x.domain(dimensions).rangePoints([ 0, w ]);
			var t = d3.transition().duration(500);
			t.selectAll(".dimensions").attr("transform", function(d) {
				return "translate(" + x(d) + ")";
			});
			t.selectAll(".foreground path").attr("d", path);
		}
	});

	// Returns the path for a given data point
	function path(d) {
		return line(dimensions.map(function(p) {
			return [ x(p), y[p](d[p]) ];
		}));
	}

	// Handles a brush event, toggling the display of foreground lines
	function brush() {
		var actives = dimensions.filter(function(p) {
			return !y[p].brush.empty();
		}), extents = actives.map(function(p) {
			return y[p].brush.extent();
		});

		foreground.classed("fade", function(d) {
			return !actives.every(function(p, i) {
				// convert to pixel range if ordinal
				var p_new = (y[p].ticks) ? d[p] : y[p](d[p]);
				return extents[i][0] <= p_new && p_new <= extents[i][1];
			});
		});

	}
}
