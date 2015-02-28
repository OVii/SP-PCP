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

function drawTable(directory, fileName) {
	
	var index = fileName.indexOf(".csv");
	var name = fileName.substring(0, index);

	// Load data
	d3.csv(directory + fileName, function(data) {
		var columns = d3.keys(data[0]);
		tabulate(data, columns);
	});

	// Tabulate data
	function tabulate(data, columns) {
		var table = d3.select("body").append("table");
		var tname = table.append('caption');
		var thead = table.append('thead');
		var tbody = table.append('tbody');

		tname.append('caption').text(name).append('caption');

		thead.append('tr').selectAll('th').data(columns).enter().append('th')
				.text(function(d) {
					return d;
				});

		var rows = tbody.selectAll('tr').data(data).enter().append('tr').attr(
				"class", function(d, i) {
					return "tableRow" + (i + 1);
				});

		var cells = rows.selectAll('td').data(function(row) {
			return columns.map(function(column) {
				return {
					column : column,
					value : row[column]
				};
			});
		}).enter().append('td').text(function(d) {
			return d.value;
		});

		return table;
	}
}