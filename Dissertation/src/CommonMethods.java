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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CommonMethods {

	private Random rand = new Random();
	private String writeDirectory;

	// Header in 3 visualizations, 4 variables
	private String[][] header = new String[3][4];

	// Data in 3 visualizations: 8 rows, 4 variables
	private int[][] standard = new int[8][4];
	private int[][] ssData = new int[8][4];
	private int[][] scpData = new int[8][4];
	private int[][] pcpData = new int[8][4];

	// QA for each picture
	private int questionColIndex, answerColIndex, answerRowIndex,
			correctChoiceIndex;
	private int[] choiceRowIndex;
	private StringBuilder QA = new StringBuilder();

	public CommonMethods(String writeDirectory) {
		this.writeDirectory = writeDirectory;
	}

	protected StringBuilder getQA() {
		return QA;
	}

	protected void writeTablesAndQuestionsTask1(int stimuli, int[][] standard,
			int questionColIndex, int answerColIndex, int answerRowIndex,
			int correctChoiceIndex, int[] choiceRowIndex, int[] choicesType,
			boolean writeID) {

		// Assign value
		this.standard = standard;
		this.questionColIndex = questionColIndex;
		this.answerColIndex = answerColIndex;
		this.answerRowIndex = answerRowIndex;
		this.correctChoiceIndex = correctChoiceIndex;
		this.choiceRowIndex = choiceRowIndex;

		// Write tables
		writeAllTables(1, stimuli, writeID);

		// Write question string
		writeQuestionTask1(stimuli, 1, getTask1Question(1, ssData), ssData,
				choicesType);
		writeQuestionTask1(stimuli, 2, getTask1Question(2, scpData), scpData,
				choicesType);
		writeQuestionTask1(stimuli, 3, getTask1Question(3, pcpData), pcpData,
				choicesType);
	}

	private String getTask1Question(int representation, int[][] data) {
		String question = "\"When %s equals to %s, what is the value of %s?\"";
		String result = String.format(question,
				header[representation - 1][questionColIndex],
				data[answerRowIndex][questionColIndex],
				header[representation - 1][answerColIndex]);
		return result;
	}

	private void writeQuestionTask1(int stimuli, int representation,
			String question, int[][] data, int[] choicesType) {
		// Question, Choice1+type, Choice2+type, Choice3+type, Choice4+type,
		// Correct Answer, Correct
		// Position
		QA.append("T1S" + stimuli + "R" + representation + "," + question + ","
				+ data[choiceRowIndex[0]][answerColIndex] + ","
				+ getChoiceType(choicesType[0]) + ","
				+ data[choiceRowIndex[1]][answerColIndex] + ","
				+ getChoiceType(choicesType[1]) + ","
				+ data[choiceRowIndex[2]][answerColIndex] + ","
				+ getChoiceType(choicesType[2]) + ","
				+ data[choiceRowIndex[3]][answerColIndex] + ","
				+ getChoiceType(choicesType[3]) + ","
				+ data[answerRowIndex][answerColIndex] + ","
				+ (correctChoiceIndex + 1) + "\n");
	}

	protected void writeTablesAndQuestions(int task, int stimuli,
			int[][] standard, String question, int[][] choices,
			int[] choicesType, int correctChoiceIndex, boolean writeID) {

		// Assign value
		this.standard = standard;

		// Write tables
		writeAllTables(task, stimuli, writeID);

		// Write question string
		writeAllQuestion(task, stimuli, question, choices, choicesType,
				correctChoiceIndex);
	}

	protected void writeAllQuestion(int task, int stimuli, String question,
			int[][] choices, int[] choicesType, int correctChoiceIndex) {
		writeQuestion(task, stimuli, 1, question, choices, choicesType,
				correctChoiceIndex);
		writeQuestion(task, stimuli, 2, question, advanceChoice(choices, 3),
				choicesType, correctChoiceIndex);
		writeQuestion(task, stimuli, 3, question, advanceChoice(choices, 3),
				choicesType, correctChoiceIndex);
	}

	private void writeQuestion(int task, int stimuli, int representation,
			String question, int[][] choices, int[] choicesType,
			int correctChoiceIndex) {

		// Question
		QA.append("T" + task + "S" + stimuli + "R" + representation + ","
				+ question + ",");

		// Choices
		for (int i = 0; i < choices.length; i++) {
			QA.append(writeChoices(choices[i]) + ",");
			QA.append(getChoiceType(choicesType[i]) + ",");
		}

		// Correct Answer, Correct Position
		QA.append(writeChoices(choices[correctChoiceIndex]) + ",");
		QA.append((correctChoiceIndex + 1) + "\n");
	}

	private String getChoiceType(int type) {
		if (type == 0) {
			return "Correct Answer";
		} else if (type == 1) {
			return "Easy Distractor";
		} else if (type == 2) {
			return "Medium Distractor";
		} else if (type == 3) {
			return "Hard Distractor";
		}
		return "";
	}

	private int[][] advanceChoice(int[][] choices, int advance) {
		int[][] newChoices = new int[choices.length][];
		int[] choice;
		for (int i = 0; i < choices.length; i++) {
			choice = choices[i];
			for (int j = 0; j < choice.length; j++) {
				choice[j] = (choice[j] + advance) % 8;
			}
			newChoices[i] = choice;
		}
		return newChoices;
	}

	protected int[] createRandomData() {
		int[] rows = new int[8];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = randomData(rows, 0, 100);
		}
		return rows;
	}

	protected int[] createRandomData(int min, int max) {
		return createRandomData(min, max, 8);
	}

	protected int[] createRandomData(int min, int max, int number) {
		int[] rows = new int[number];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = randomData(rows, min, max);
		}
		return rows;
	}

	protected int randomData(int[] rows, int min, int max) {
		int number = 0;
		while (!isValid(number) || !satisfyDistanceReq(rows, number)) {
			number = randomInt(min, max);
		}
		return number;
	}

	protected boolean isValid(int number) {
		// Rule 2: Avoid 0-10
		if (number <= 10) {
			return false;
		}
		// Avoid 100
		else if (number >= 100) {
			return false;
		}
		// Avoid x0, x1, x4, x5, x6, x9
		else if (number % 10 == 0 || number % 10 == 1 || number % 10 == 4
				|| number % 10 == 5 || number % 10 == 6 || number % 10 == 9) {
			return false;
		}
		return true;
	}

	protected boolean satisfyDistanceReq(int[] array, int value) {
		// Rule 4: Minimal distance > 1
		for (int i = 0; i < array.length; i++) {
			if (array[i] == 0) {
				continue;
			} else if (Math.abs(array[i] - value) <= 1) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean isDuplicate(String[] array, String value){
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				return false;
			} else if (array[i].equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isDuplicate(String[][] array, String value) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if (array[i][j] == null) {
					return false;
				} else if (array[i][j].equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean isDuplicate(char[] array, char value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == '\u0000') {
				return false;
			} else if (array[i] == value) {
				return true;
			}
		}
		return false;
	}

	protected boolean isDuplicate(char[][] array, char value) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if (array[i][j] == '\u0000') {
					return false;
				} else if (array[i][j] == value) {
					return true;
				}
			}
		}
		return false;
	}

	protected int[] randomTwoIntExcept(int min, int max, int[] except) {
		int value1 = randomIntExcept(min, max, except);
		int[] newExcept = Arrays.copyOf(except, except.length + 1);
		newExcept[newExcept.length - 1] = value1;
		int value2 = randomIntExcept(min, max, newExcept);
		return new int[] { value1, value2 };
	}

	protected int randomIntExcept(int min, int max, int[] except) {
		int value = randomInt(min, max);
		while (contain(except, value)) {
			value = randomInt(min, max);
		}
		return value;
	}

	protected int[] randomTwoInt(int min, int max) {
		int num1 = randomInt(min, max);
		int num2 = num1;
		while (num2 == num1) {
			num2 = randomInt(0, 7);
		}
		return new int[] { num1, num2 };
	}

	protected int randomInt(int min, int max) {
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	protected char randomChar() {
		char c = (char) (rand.nextInt(26) + 'A');
		return c;
	}

	protected int randomLinkedList(LinkedList<Integer> list, boolean remove) {
		int randomIndex = rand.nextInt(list.size());
		int value;
		if (remove) {
			value = list.remove(randomIndex);
		} else {
			value = list.get(randomIndex);
		}
		return (Integer) value;
	}

	protected int getRandomIndex(int length) {
		int randomIndex = rand.nextInt(length);
		return randomIndex;
	}
	
	protected int findMinValue(int[] rows){
		int min = 100;
		for (int i = 0; i < rows.length; i++) {
			if (rows[i] == 0){
				continue;
			}
			if (rows[i] < min) {
				min = rows[i];
			}
		}
		return min;
	}
	
	protected int findMaxValue(int[] rows){
		int max = 0;
		for (int i = 0; i < rows.length; i++) {
			if (rows[i] == 0){
				continue;
			}
			if (rows[i] > max) {
				max = rows[i];
			}
		}
		return max;
	}

	private void writeAllTables(int task, int stimuli, boolean writeID) {
		// Assign variation as a whole table
		LinkedList<Integer> variation = new LinkedList<Integer>(Arrays.asList(
				-1, 0, 1));
		int ssVariation = randomLinkedList(variation, true);
		int scpVariation = randomLinkedList(variation, true);
		int pcpVariation = variation.getFirst();

		for (int i = 0; i < 8; i++) {
			ssData[i] = getVariation(standard[i], ssVariation);
			scpData[i] = getVariation(standard[i], scpVariation);
			pcpData[i] = getVariation(standard[i], pcpVariation);
		}

		writeTable(task, stimuli, 1, ssData, true);
		writeTable(task, stimuli, 2, scpData, writeID);
		writeTable(task, stimuli, 3, pcpData, writeID);
	}

	private int[] getVariation(int[] standard, int variation) {
		int[] variationArray = new int[4];
		for (int i = 0; i < variationArray.length; i++) {
			variationArray[i] = standard[i] + variation;
		}
		return variationArray;
	}

	private void writeTable(int task, int stimuli, int representation,
			int[][] data, boolean writeID) {
		StringBuilder s = new StringBuilder();

		String[] c = createHeader(task);
		header[representation - 1] = c;

		// If writeID is true, add another column
		if (writeID) {
			// shuffle(data);
			// Write header row
			s.append(c[0] + "," + c[1] + "," + c[2] + "," + c[3] + ",ID\n");
			// Write data for each row
			for (int i = 0; i < 8; i++) {
				s.append(data[i][0] + "," + data[i][1] + "," + data[i][2] + ","
						+ data[i][3] + "," + (i + 1) + "\n");
			}
		} else {
			// Write header row
			s.append(c[0] + "," + c[1] + "," + c[2] + "," + c[3] + "\n");
			// Write data for each row
			for (int i = 0; i < 8; i++) {
				s.append(data[i][0] + "," + data[i][1] + "," + data[i][2] + ","
						+ data[i][3] + "\n");
			}
		}

		// Write file
		writeFile("T" + task + "S" + stimuli + "R" + representation + ".csv", s
				.toString().trim());

	}

	private String[] createHeader(int task) {
		String[] c = new String[4];
		String value = randomChar() + "";
		if (task == 4) {
			while (isDuplicate(header, value)) {
				value = randomChar() + "";
			}
			for (int i = 0; i < 4; i++) {
				c[i] = value + (i+1);
			}
		} else {
			for (int i = 0; i < c.length; i++) {
				while (isDuplicate(c, value) || isDuplicate(header, value)) {
					value = randomChar() + "";
				}
				c[i] = value;
			}
			Arrays.sort(c);
		}
		return c;
	}

	protected void writeFile(String filename, String content) {
		System.out.println("Writing a file: " + filename);
		String task = filename.substring(0, 2);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					writeDirectory + task + "/" + filename));
			try {
				bw.write(content);
			} finally {
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void writeFile(String filename, int[][] array, String columnName) {
		System.out.println("Writing a file: " + filename);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					writeDirectory + filename));
			try {
				int[] line;
				StringBuilder sb = new StringBuilder();
				sb.append(columnName + "\n");
				for (int i = 0; i < array.length; i++) {
					line = array[i];
					for (int j = 0; j < line.length; j++) {
						sb.append(line[j]);
						if (j < line.length - 1) {
							sb.append(",");
						}
					}
					sb.append("\n");
				}
				bw.write(sb.toString().trim());
			} finally {
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void writeFile(String filename, int[][] array) {
		writeFile(filename, array, "");
	}

	protected void shuffle(int[] array) {
		ArrayList<Integer> list = new ArrayList<Integer>();

		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}

		Collections.shuffle(list);

		for (int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
	}

	protected void shuffle(int[][] array) {
		ArrayList<int[]> list = new ArrayList<int[]>();

		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}

		Collections.shuffle(list);

		for (int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
	}

	protected void swapArray(int[][] arrays, int index1, int index2) {
		int[] temp = arrays[index1];
		arrays[index1] = arrays[index2];
		arrays[index2] = temp;
	}

	protected int[][] rowToColumn(int[][] array) {
		int[][] pivot = new int[array[0].length][];
		for (int row = 0; row < array[0].length; row++) {
			pivot[row] = new int[array.length];
		}

		for (int row = 0; row < array.length; row++) {
			for (int col = 0; col < array[row].length; col++) {
				pivot[col][row] = array[row][col];
			}
		}
		return pivot;
	}

	protected boolean contain(int[] array, int number) {
		for (int i = 0; i < array.length; i++) {
			if (number == array[i]) {
				return true;
			}
		}
		return false;
	}

	protected int[] kMeanClustering(int[][] data, int[][] initialCentroids) {
		// centroid for cluster, variables
		int[][] oldCentroids = initialCentroids;
		// binary function for data point, cluster
		int[][] clusters = assignClusters(data, oldCentroids);
		int[][] newCentroids = computeCentroids(data, clusters);
		while (isChanged(oldCentroids, newCentroids)) {
			oldCentroids = newCentroids;
			clusters = assignClusters(data, oldCentroids);
			newCentroids = computeCentroids(data, clusters);
		}
		// cluster for each data
		int[] dataClusters = getDataCluster(clusters);
		return dataClusters;
	}

	protected LinkedList<int[]> getNewIndexInNewArray(int[] oldArray,
			LinkedList<int[]> oldIndex, int[] newArray) {
		LinkedList<int[]> newIndex = new LinkedList<int[]>();

		// Convert new array from int to String
		String[] newDataArray = Arrays.toString(newArray).split("[\\[\\]]")[1]
				.split(", ");
		List<String> newDataList = Arrays.asList(newDataArray);

		for (int i = 0; i < oldIndex.size(); i++) {
			newIndex.add(getNewIndex(oldArray, oldIndex.get(i), newDataList));
		}
		return newIndex;
	}

	protected int[] getNewIndexInNewArray(int[] oldArray, int[] oldIndex,
			int[] newArray) {
		// Convert new array from int to String
		String[] newDataArray = Arrays.toString(newArray).split("[\\[\\]]")[1]
				.split(", ");
		List<String> newDataList = Arrays.asList(newDataArray);
		return getNewIndex(oldArray, oldIndex, newDataList);
	}

	private int[] getNewIndex(int[] oldArray, int[] oldIndex,
			List<String> newDataList) {
		int[] newIndex = new int[oldIndex.length];

		// Get value from old data
		int[] oldValue = getValueFrom(oldArray, oldIndex);

		// Convert old value from int to String
		String[] oldValueArray = Arrays.toString(oldValue).split("[\\[\\]]")[1]
				.split(", ");

		for (int i = 0; i < oldValueArray.length; i++) {
			newIndex[i] = newDataList.indexOf(oldValueArray[i]);
		}

		return newIndex;
	}

	private int[] getValueFrom(int[] array, int[] index) {
		int[] value = new int[index.length];
		for (int i = 0; i < index.length; i++) {
			value[i] = array[index[i]];
		}
		return value;
	}

	private int[] getDataCluster(int[][] clusters) {
		int[] dataCluster = new int[clusters.length];
		int cluster = 0;
		for (int i = 0; i < clusters.length; i++) {
			for (int j = 0; j < clusters[i].length; j++) {
				if (clusters[i][j] == 1) {
					cluster = j;
				}
			}
			dataCluster[i] = cluster;
		}
		return dataCluster;
	}

	private boolean isChanged(int[][] oldCentroids, int[][] newCentroids) {
		for (int i = 0; i < oldCentroids.length; i++) {
			for (int j = 0; j < oldCentroids[i].length; j++) {
				if (oldCentroids[i][j] != newCentroids[i][j]) {
					return true;
				}
			}
		}
		return false;
	}

	private int[][] computeCentroids(int[][] data, int[][] clusters) {
		int clusterNumber = clusters[0].length;
		int variableNumber = data[0].length;
		int[][] centroids = new int[clusterNumber][variableNumber];
		int[][] sum = new int[clusterNumber][variableNumber];
		int[] clusterPoints = new int[clusterNumber];

		// Sum over all data in the cluster
		for (int i = 0; i < data.length; i++) {
			for (int k = 0; k < clusterPoints.length; k++) {
				if (clusters[i][k] == 1) {
					clusterPoints[k]++;
					for (int j = 0; j < variableNumber; j++) {
						sum[k][j] += data[i][j];
					}
				}
			}
		}

		// Calculate new centroids
		for (int k = 0; k < clusterPoints.length; k++) {
			for (int j = 0; j < variableNumber; j++) {
				centroids[k][j] = sum[k][j] / clusterPoints[k];
			}
		}
		return centroids;
	}

	private int[][] assignClusters(int[][] data, int[][] centroids) {
		int clusterNumber = centroids.length;
		int[][] clusters = new int[data.length][clusterNumber];
		int[] variables;
		double[] diff = new double[clusterNumber];
		double[] distance = new double[clusterNumber];
		int minIndex;

		for (int i = 0; i < data.length; i++) {
			variables = data[i];
			// Euclidean distance
			for (int k = 0; k < clusterNumber; k++) {
				for (int j = 0; j < variables.length; j++) {
					diff[k] += Math.pow(variables[j] - centroids[k][j], 2);

				}
				distance[k] = Math.sqrt(diff[k]);
			}
			diff = new double[clusterNumber];
			minIndex = getMinDistanceIndex(distance);
			clusters[i] = assignCluster(clusterNumber, minIndex);
		}
		return clusters;
	}

	private int[] assignCluster(int clusterNumber, int minIndex) {
		int[] cluster = new int[clusterNumber];
		cluster[minIndex] = 1;
		return cluster;
	}

	private int getMinDistanceIndex(double[] distance) {
		int index = 0;
		for (int i = 1; i < distance.length; i++) {
			if (distance[i] < distance[index]) {
				index = i;
			}
		}
		return index;
	}

	protected void printArray(int[][] array) {
		int[] s;
		for (int i = 0; i < array.length; i++) {
			s = array[i];
			for (int j = 0; j < s.length; j++) {
				System.out.print(s[j]);
				if (j < s.length - 1) {
					System.out.print(", ");
				}
			}
			System.out.println();
		}
	}

	protected void printArray(int[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i]);
			if (i < array.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println();
	}

	protected void printList(List<int[]> list) {
		for (int i = 0; i < list.size(); i++) {
			printArray(list.get(i));
			System.out.println();
		}
	}

	private String writeChoices(int[] array) {
		Arrays.sort(array);
		String s = "\"";
		s += writeArray(array, 1);
		s += "\"";
		return s;
	}

	protected String writeArray(int[] array, int add) {
		String s = "";
		for (int i = 0; i < array.length; i++) {
			s += (array[i] + add);
			if (i < array.length - 1) {
				s += ",     ";
			}
		}
		return s;
	}

	protected void printArray(double[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i]);
			if (i < array.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println();
	}

}
