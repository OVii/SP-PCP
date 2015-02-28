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

import java.util.Arrays;
import java.util.LinkedList;

// Clustering Task

public class Task2_GenerateData {

	// static String writeDirectory = "assets/excel/";
	static String writeDirectory = "/Users/Firenized/Dropbox/Ploy/Workspace/Dissertation_Visualization/WebContent/assets/data/";
	static CommonMethods method = new CommonMethods(writeDirectory);

	// Standard Data: 8 rows, 4 variables
	static int[][] standard = new int[8][4];
	// Data for each variable: 4 variables, 8 rows
	static int[][] variables = new int[4][8];

	// QA for each picture
	static int correctChoiceIndex;
	static int[][] choices;
	// 0 = correct answer, 1 = easy distractor, 2 = medium, 3 = hard
	static int[] choicesType;

	// Choice position
	static LinkedList<Integer> correctChoicePosition = new LinkedList<Integer>(
			Arrays.asList(2, 3, 0, 1, 2, 3));

	public static void main(String[] args) {
		String question = "Which 4 data points form the most appropriate cluster?";
		// For 6 stimuli
		for (int i = 1; i <= 6; i++) {
			createTable(i);
			method.writeTablesAndQuestions(2, i, standard, question, choices,
					choicesType, correctChoiceIndex, false);
		}
		// Write T2 question and answer
		String QA = "Stimuli,Question,"
				+ "Choice1,Choice1 Type,Choice2,Choice2 Type,Choice3,Choice3 Type,Choice4,Choice4 Type,"
				+ "Correct Answer,Correct Position\n";
		QA += method.getQA().toString().trim();
		method.writeFile("T2.csv", QA);
	}

	private static void createTable(int stimuli) {
		// Create data for each variable
		variables = new int[4][8];
		int[][] seeds = new int[2][4];
		int min = 20, max = 80;
		int seedRange = 40, valueRange = 12;
		seeds[0] = createSeed1(min, max);

		// Easy
		if (stimuli == 1) {
			int[] differentVariable = new int[] { 0, 1, 2 };
			seeds[1] = createSeed2(seeds[0], differentVariable, seedRange);
			for (int i = 0; i < 4; i++) {
				variables[i] = createDataFromSeed(seeds[0][i], seeds[1][i],
						valueRange);
			}

		} else if (stimuli == 2) {
			int[] differentVariable = new int[] { 1, 2, 3 };
			seeds[1] = createSeed2(seeds[0], differentVariable, seedRange);
			for (int i = 0; i < 4; i++) {
				variables[i] = createDataFromSeed(seeds[0][i], seeds[1][i],
						valueRange);
			}
		}

		// Medium
		else if (stimuli == 3) {
			int[] differentVariable = new int[] { 1, 3 };
			seeds[1] = createSeed2(seeds[0], differentVariable, seedRange);
			for (int i = 0; i < 4; i++) {
				variables[i] = createDataFromSeed(seeds[0][i], seeds[1][i],
						valueRange);
			}

		} else if (stimuli == 4) {
			int[] differentVariable = new int[] { 2, 3 };
			seeds[1] = createSeed2(seeds[0], differentVariable, seedRange);
			for (int i = 0; i < 4; i++) {
				variables[i] = createDataFromSeed(seeds[0][i], seeds[1][i],
						valueRange);
			}
		}

		// Hard
		else if (stimuli == 5) {
			int[] differentVariable = new int[] { 2 };
			seeds[1] = createSeed2(seeds[0], differentVariable, seedRange);
			for (int i = 0; i < 4; i++) {
				variables[i] = createDataFromSeed(seeds[0][i], seeds[1][i],
						valueRange);
			}

		} else if (stimuli == 6) {
			int[] differentVariable = new int[] { 3 };
			seeds[1] = createSeed2(seeds[0], differentVariable, seedRange);
			for (int i = 0; i < 4; i++) {
				variables[i] = createDataFromSeed(seeds[0][i], seeds[1][i],
						valueRange);
			}
		}

		// Shuffle data
		standard = method.rowToColumn(variables);
		method.shuffle(standard);
		variables = method.rowToColumn(standard);

		createChoices(seeds);

	}

	private static void createChoices(int[][] seeds) {
		// Find cluster
		int[] cluster = method.kMeanClustering(standard, seeds);
		int[][] group = getClusterGroup(cluster);

		// Sort first variable
		int[] sortVariable0 = Arrays.copyOf(variables[0], variables[0].length);
		Arrays.sort(sortVariable0);
		int[][] sortGroup = new int[2][4];
		sortGroup[0] = method.getNewIndexInNewArray(variables[0], group[0],
				sortVariable0);
		Arrays.sort(sortGroup[0]);
		sortGroup[1] = method.getNewIndexInNewArray(variables[0], group[1],
				sortVariable0);
		Arrays.sort(sortGroup[1]);

		// Select choice position
		choices = new int[4][];
		choicesType = new int[4];
		LinkedList<Integer> choicePosition = new LinkedList<Integer>(
				Arrays.asList(0, 1, 2, 3));

		// choice position for correct answer
		correctChoiceIndex = method.randomLinkedList(correctChoicePosition,
				true);
		choicePosition.remove(correctChoiceIndex);
		choices[correctChoiceIndex] = group[0]; // correct answer
		choicesType[correctChoiceIndex] = 0;

		// choice position for distractors
		int index;

		// easy choice
		index = method.randomLinkedList(choicePosition, true);
		choices[index] = method.getNewIndexInNewArray(sortVariable0, new int[] {
				group[0][2], group[0][3], group[1][0], group[1][1] },
				variables[0]);
		choicesType[index] = 1;

		// medium choice
		index = method.randomLinkedList(choicePosition, true);
		choices[index] = method.getNewIndexInNewArray(sortVariable0, new int[] {
				group[0][0], group[1][3], group[0][2], group[0][3] },
				variables[0]);
		choicesType[index] = 2;

		// hard choice
		index = method.randomLinkedList(choicePosition, true);
		choices[index] = method.getNewIndexInNewArray(sortVariable0, new int[] {
				group[1][3], group[0][1], group[0][2], group[0][3] },
				variables[0]);
		choicesType[index] = 3;
	}

	private static int[][] getClusterGroup(int[] cluster) {
		int[][] group = new int[2][4];
		int count0 = 0, count1 = 0;
		for (int i = 0; i < cluster.length; i++) {
			if (cluster[i] == 0) {
				group[0][count0] = i;
				count0++;
			} else {
				group[1][count1] = i;
				count1++;
			}
		}
		return group;
	}

	private static int[] createDataFromSeed(int seed1, int seed2, int range) {
		if (seed1 == seed2) {
			range = 38;
		}
		int[] rows = new int[8];
		for (int i = 0; i < 4; i++) {
			rows[i] = randomDataFromSeed(rows, seed1, range);
		}
		for (int i = 4; i < 8; i++) {
			rows[i] = randomDataFromSeed(rows, seed2, range);
		}
		return rows;
	}

	private static int randomDataFromSeed(int[] rows, int seed, int range) {
		int min = seed - range;
		min = (min < 0) ? 0 : min;
		int max = seed + range;
		max = (max > 100) ? 95 : max;
		return method.randomData(rows, min, max);
	}

	private static int[] createSeed2(int[] seed1, int[] differentVariable,
			int range) {
		int[] seed2 = new int[seed1.length];
		int min, max;
		int value;
		for (int i = 0; i < seed1.length; i++) {
			if (method.contain(differentVariable, i)) {
				if (seed1[i] > 55) {
					min = 20;
					max = seed1[i] - range;
				} else {
					min = seed1[i] + range;
					max = 85;
				}
				value = 0;
				while (!method.isValid(value)) {
					value = method.randomInt(min, max);
				}
				seed2[i] = value;
			} else {
				seed2[i] = seed1[i];
			}
		}
		return seed2;
	}

	private static int[] createSeed1(int min, int max) {
		int[] seed1 = new int[4];
		int minAvoid = 45, maxAvoid = 65;
		int value;
		for (int i = 0; i < seed1.length; i++) {
			value = 45;
			while (value >= minAvoid && value <= maxAvoid) {
				value = method.randomInt(min, max);
			}
			seed1[i] = value;
		}
		return seed1;
	}

}
