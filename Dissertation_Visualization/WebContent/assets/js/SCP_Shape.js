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

// Use D3.js repository "http://mbostock.github.io/d3/talk/20111116/iris-splom.html" as an example

function drawSCP(directiry, fileName) {
	
	var index = fileName.indexOf(".csv");
	var name = fileName.substring(0,index);
	var task = name.substring(0, 2);
	
	// Size parameters.
	var size = 350, padding = 70;

	// Position scales.
	var x = {}, y = {};
	
	  // Root panel.
	  var svg = d3.select("body").append("svg:svg")
	      .attr("width", 1200)
	      .attr("height", 500)
	    .append("svg:g")
	      .attr("transform", "translate(100,50)");
	  
	  // Picture Number
	  svg.append("text")
	   .attr("x", 0).attr("y", 0)
	   .attr("fill", "red").attr("font-size", "25px")
	   .attr("font-family", "sans-serif")
	   .text(name);
	
	  // Load data
	  d3.csv(directory + task + "/"+ fileName, function(data) {

	// Extract the list of dimensions and create a scale for each
	  dimensions = d3.keys(data[0]);
	  dimensions.forEach(function(d) {
	        domain = [0, 100],
		    x[d] = d3.scale.linear().domain(domain).range([0, size - padding]);
		    y[d] = d3.scale.linear().domain(domain).range([size, padding]);
	  });

	  // Axes.
	  var axis = d3.svg.axis().ticks(10).tickPadding(5);

	  // Brush.
	  var brush = d3.svg.brush()
	      .on("brushstart", brushstart)
	      .on("brush", brush)
	      .on("brushend", brushend);

	// X-Grid.
	  svg.selectAll("g.x.grid")
	      .data(dimensions.slice(1))
	    .enter().append("svg:g")
	      .attr("class", "grid")
	      .attr("transform", function(d, i) { return "translate(" + i * size + ","+ size +")"; })
	      .each(function(d) { d3.select(this).call(axis.scale(x[d]).orient("bottom").tickSize(-size+padding,0,0).tickFormat("")); });
	  
	// Y-Grid.
	  svg.selectAll("g.y.grid")
	      .data(dimensions[0].concat(dimensions[0]).concat(dimensions[0]))
	    .enter().append("svg:g")
	      .attr("class", "grid")
	      .attr("transform", function(d, i) { return "translate(" + i * size + ",0)"; })
	      .each(function(d) { d3.select(this).call(axis.scale(y[d]).orient("left").tickSize(-size+padding,0,0).tickFormat("")); });
	  
	  // X-axis.
	  svg.selectAll("g.x.axis")
	      .data(dimensions.slice(1))
	    .enter().append("svg:g")
	      .attr("class", "x axis")
	      .attr("transform", function(d, i) { return "translate(" + i * size + ","+ size +")"; })
	      .each(function(d) { d3.select(this).call(axis.scale(x[d]).orient("bottom").tickSize(6).tickFormat(null)); })
	      .append("svg:text").attr("class", "title").attr("x", size - padding + 20).attr("y", 5)
				.text(String);
	  
	  // Y-axis.
	  svg.selectAll("g.y.axis")
	      .data(dimensions[0].concat(dimensions[0]).concat(dimensions[0]))
	    .enter().append("svg:g")
	      .attr("class", "y axis")
	      .attr("transform", function(d, i) { return "translate(" + i * size + ",0)"; })
	      .each(function(d) { d3.select(this).call(axis.scale(y[d]).orient("left").tickSize(6).tickFormat(null)); })
	      .append("svg:text").attr("class", "title").attr("y", padding - 10)
				.text(String);

	  // Cell and plot.
	  var cell = svg.selectAll("g.cell")
	      .data(cross(dimensions.slice(1), dimensions[0]))
	    .enter().append("svg:g")
	      .attr("class", "cell")
	      .attr("transform", function(d) { return "translate(" + (d.i * (size)) + "," + (d.j * size) + ")"; })
	      .each(plot);

	  function plot(p) {
	    var cell = d3.select(this);

	    // Plot frame.
	    cell.append("svg:rect")
	        .attr("class", "frame")
	        .attr("x", padding / 2)
	        .attr("y", padding / 2)
	        .attr("width", size - padding)
	        .attr("height", size - padding);

	    // First Method: number in circle
	    // Plot dots.
	    cell.selectAll("circle")
	    	.data(data).enter().append("svg:circle")
	        .attr("class", function(d, i) { return "scpRow"+(i+1); })
	        .attr("cx", function(d) { return x[p.x](d[p.x]); })
	        .attr("cy", function(d) { return y[p.y](d[p.y]); })
	        .attr("r", 9.5);
	    
	    // Write ID.
	    cell.selectAll("ID")
	    	.data(data).enter().append("svg:text")
	    	.attr("dx", function(d) { return x[p.x](d[p.x])-5; })
	    	.attr("dy", function(d) { return y[p.y](d[p.y])+6; })
	    	.text(function(d){return d.ID;});

	    // Plot brush.
	    cell.call(brush.x(x[p.x]).y(y[p.y]));
	  }

	  // Clear the previously-active brush, if any.
	  function brushstart(p) {
	    if (brush.data !== p) {
	      cell.call(brush.clear());
	      brush.x(x[p.x]).y(y[p.y]).data = p;
	    }
	  }

	  // Highlight the selected circles.
	  function brush(p) {
	    var e = brush.extent();
	    svg.selectAll(".cell circle").attr("class", function(d) {
	      return e[0][0] <= d[p.x] && d[p.x] <= e[1][0]
	          && e[0][1] <= d[p.y] && d[p.y] <= e[1][1]
	          ? d.species : null;
	    });
	  }

	  // If the brush is empty, select all circles.
	  function brushend() {
	    if (brush.empty()) svg.selectAll(".cell circle").attr("class", function(d) {
	      return d.species;
	    });
	  }

	  function cross(a, b) {
	    var c = [], n = a.length, m = b.length, i, j;
	    for (i = -1; ++i < n;) for (j = -1; ++j < m;) c.push({x: a[i], i: i, y: b[j], j: j});
	    return c;
	  }
	});

	    

}
