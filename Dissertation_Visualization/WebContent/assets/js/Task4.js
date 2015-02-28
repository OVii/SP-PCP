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

var directory = "assets/data/T4/";

for (var s = 1; s <= 6; s++) {
	drawTable(directory, "T4S" + s + "R1.csv");
	drawSCPLegend(directory, "T4S" + s + "R2.csv");
	drawPCPLegend(directory, "T4S" + s + "R3.csv");
}