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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class checkKMeanClustering {

	private static String readDirectory = "src/assets/excel/T2/";
	// Standard Data: 8 rows, 4 variables
	static int[][] standard = new int[8][4];

	static CommonMethods method = new CommonMethods(readDirectory);

	public static void main(String[] args) {
		readFile("T2S6R1.csv");
		method.printArray(standard);
		int[][] seeds = new int[2][4];
		seeds[0] = new int[] { 70, 50, 25, 20 };
		seeds[1] = new int[] { 55, 30, 40, 55 };
		int[] cluster = method.kMeanClustering(standard, seeds);
		System.out.println("cluster");
		method.printArray(cluster);
	}

	private static void readFile(String filename) {
		System.out.println("Reading a file: " + filename);
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					readDirectory + filename));

			try {
				String line;
				int index1;
				int[] value;
				String[] data;
				// skip header
				br.readLine();
				int count = 0;
				while ((line = br.readLine()) != null) {
					index1 = line.lastIndexOf(",");
					data = line.substring(0, index1).split(
							",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					value = new int[data.length];
					for (int i = 0; i < data.length; i++) {
						value[i] = Integer.parseInt(data[i]);
					}
					standard[count] = value;
					count++;
				}
			} finally {
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
