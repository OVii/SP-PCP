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

// Change Detection Task

public class Task4_GenerateData {

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
		String question = "Which data point(s) has(have) the most changes?";
		// For 6 stimuli
		for (int i = 1; i <= 6; i++) {
			createTable(i);
			method.writeTablesAndQuestions(4, i, standard, question, choices,
					choicesType, correctChoiceIndex, false);
		}
		// Write T4 question and answer
		String QA = "Stimuli,Question,"
				+ "Choice1,Choice1 Type,Choice2,Choice2 Type,Choice3,Choice3 Type,Choice4,Choice4 Type,"
				+ "Correct Answer,Correct Position\n";
		QA += method.getQA().toString().trim();
		method.writeFile("T4.csv", QA);
	}

	private static void createTable(int stimuli) {
		// Create data for each variable
		int[] outlierIndex = null;
		boolean change = true;
		variables = new int[4][8];
		variables[0] = method.createRandomData(15, 90);
		LinkedList<int[]> oldChoiceIndex = new LinkedList<int[]>();

		// Easy
		if (stimuli == 1) {
			outlierIndex = new int[] { method.randomInt(0, 7) };
			replace(variables[0], outlierIndex, 35, 75);
			for (int i = 1; i < 4; i++) {
				variables[i] = createVaryData(variables[0], 1, outlierIndex,
						change);
				change = !change;
			}

			// correct answer
			oldChoiceIndex.add(outlierIndex);
			// easy choice
			oldChoiceIndex.add(new int[] { method.randomIntExcept(0, 7,
					outlierIndex) });
			// medium choice
			oldChoiceIndex.add(getTwoMostChangeIndex(variables, outlierIndex));
			// hard choice
			oldChoiceIndex.add(new int[] { outlierIndex[0],
					method.randomIntExcept(0, 7, outlierIndex) });

		} else if (stimuli == 2) {
			outlierIndex = method.randomTwoInt(0, 7);
			replace(variables[0], outlierIndex, 35, 75);
			for (int i = 1; i < 4; i++) {
				variables[i] = createVaryData(variables[0], 1, outlierIndex,
						change);
				change = !change;
			}

			// correct answer
			oldChoiceIndex.add(outlierIndex);
			// easy choice
			oldChoiceIndex.add(method.randomTwoIntExcept(0, 7, outlierIndex));
			// medium choice
			oldChoiceIndex.add(new int[] { getMostChangeIndex(variables,
					outlierIndex) });
			// hard choice
			oldChoiceIndex.add(new int[] { outlierIndex[0] });
		}

		// Medium
		if (stimuli == 3) {
			outlierIndex = new int[] { method.randomInt(0, 7) };
			replace(variables[0], outlierIndex, 35, 75);
			for (int i = 1; i < 4; i++) {
				variables[i] = createVaryData(variables[0], 2, outlierIndex,
						change);
				change = !change;
			}

			// correct answer
			oldChoiceIndex.add(outlierIndex);
			// easy choice
			oldChoiceIndex.add(new int[] { method.randomIntExcept(0, 7,
					outlierIndex) });
			// medium choice
			oldChoiceIndex.add(getTwoMostChangeIndex(variables, outlierIndex));
			// hard choice
			oldChoiceIndex.add(new int[] { outlierIndex[0],
					method.randomIntExcept(0, 7, outlierIndex) });

		} else if (stimuli == 4) {
			outlierIndex = method.randomTwoInt(0, 7);
			replace(variables[0], outlierIndex, 35, 75);
			for (int i = 1; i < 4; i++) {
				variables[i] = createVaryData(variables[0], 2, outlierIndex,
						change);
				change = !change;
			}

			// correct answer
			oldChoiceIndex.add(outlierIndex);
			// easy choice
			oldChoiceIndex.add(method.randomTwoIntExcept(0, 7, outlierIndex));
			// medium choice
			oldChoiceIndex.add(new int[] { getMostChangeIndex(variables,
					outlierIndex) });
			// hard choice
			oldChoiceIndex.add(new int[] { outlierIndex[0] });
		}

		// Hard
		if (stimuli == 5) {
			outlierIndex = new int[] { method.randomInt(0, 7) };
			replace(variables[0], outlierIndex, 35, 75);
			for (int i = 1; i < 4; i++) {
				variables[i] = createVaryData(variables[0], 3, outlierIndex,
						change);
				change = !change;
			}

			// correct answer
			oldChoiceIndex.add(outlierIndex);
			// easy choice
			oldChoiceIndex.add(new int[] { method.randomIntExcept(0, 7,
					outlierIndex) });
			// medium choice
			oldChoiceIndex.add(getTwoMostChangeIndex(variables, outlierIndex));
			// hard choice
			oldChoiceIndex.add(new int[] { outlierIndex[0],
					method.randomIntExcept(0, 7, outlierIndex) });

		} else if (stimuli == 6) {
			outlierIndex = method.randomTwoInt(0, 7);
			replace(variables[0], outlierIndex, 35, 75);
			for (int i = 1; i < 4; i++) {
				variables[i] = createVaryData(variables[0], 3, outlierIndex,
						change);
				change = !change;
			}

			// correct answer
			oldChoiceIndex.add(outlierIndex);
			// easy choice
			oldChoiceIndex.add(method.randomTwoIntExcept(0, 7, outlierIndex));
			// medium choice
			oldChoiceIndex.add(new int[] { getMostChangeIndex(variables,
					outlierIndex) });
			// hard choice
			oldChoiceIndex.add(new int[] { outlierIndex[0] });
		}

		// Assign value to standard data
		standard = method.rowToColumn(variables);

		createChoices(oldChoiceIndex);
	}

	private static void createChoices(LinkedList<int[]> oldChoiceIndex) {
		// Select choice position
		choices = new int[4][];
		choicesType = new int[4];
		LinkedList<Integer> choicePosition = new LinkedList<Integer>(
				Arrays.asList(0, 1, 2, 3));

		// choice position for correct answer
		correctChoiceIndex = method.randomLinkedList(correctChoicePosition,
				true);
		choicePosition.remove(correctChoiceIndex);
		choices[correctChoiceIndex] = oldChoiceIndex.get(0);
		choicesType[correctChoiceIndex] = 0;

		// choice position for distractors
		int index;
		for (int i = 1; i <= 3; i++) {
			index = method.randomLinkedList(choicePosition, true);
			choices[index] = oldChoiceIndex.get(i);
			choicesType[index] = i;
		}
	}

	private static int[] getTwoMostChangeIndex(int[][] array, int[] except) {
		int mostChange1 = getMostChangeIndex(array, except);
		int[] index = Arrays.copyOf(except, except.length + 1);
		index[index.length - 1] = mostChange1;
		int mostChange2 = getMostChangeIndex(array, index);
		return new int[] { mostChange1, mostChange2 };
	}

	private static int getMostChangeIndex(int[][] array, int[] except) {
		int[][] row = method.rowToColumn(array);
		int mostChange = 0;
		int change;
		int mostChangeIndex = -1;
		for (int i = 0; i < row.length; i++) {
			if (method.contain(except, i)) {
				continue;
			} else {
				change = 0;
				for (int j = 1; j < row[i].length; j++) {
					change += Math.abs(row[i][j] - row[i][j - 1]);
				}
				if (change > mostChange) {
					mostChange = change;
					mostChangeIndex = i;
				}
			}
		}
		return mostChangeIndex;
	}

	private static int[] createVaryData(int[] original, int level,
			int[] outlierIndex, boolean change) {

		// Range value for easy, medium, hard level
		int range = 0;
		int outlierRange = 0;
		if (level == 1) {
			range = 2;
			outlierRange = 12;
		} else if (level == 2) {
			range = 6;
			outlierRange = 16;
		} else if (level == 3) {
			range = 10;
			outlierRange = 20;
		}

		// Create data
		int data, index;
		int[] rows = new int[8];
		for (int i = 0; i < rows.length; i++) {
			data = original[i];
			if (method.contain(outlierIndex, i)) {
				continue;
			} else {
				rows[i] = varyData(rows, data, range);
			}
		}

		// outlier
		for (int j = 0; j < outlierIndex.length; j++) {
			index = outlierIndex[j];
			data = original[index];
			rows[index] = varyOutlier(rows, data, outlierRange, change,
					isIncrease(data));
		}

		return rows;
	}

	private static boolean isIncrease(int value) {
		if (value % 10 == 2 || value % 10 == 7) {
			return false;
		} else {
			return true;
		}
	}

	private static int varyOutlier(int[] rows, int original, int range,
			boolean change, boolean increase) {
		int value;
		if (!change) {
			if (increase) {
				value = decrease(rows, original, range);
			} else {
				value = increase(rows, original, range);
			}
		} else {
			if (increase) {
				value = increase(rows, original, range);
			} else {
				value = decrease(rows, original, range);
			}
		}
		if (Math.abs(value - original) > range + range / 4) {
			System.out.println("range = " + Math.abs(value - original));
		}
		return value;
	}

	private static int increase(int[] rows, int original, int range) {
		int value = original + range;
		while (!method.isValid(value)
				|| !method.satisfyDistanceReq(rows, value)) {
			if (value - original < range - range / 4) {
				break;
			}
			value--;
		}
		while (!method.isValid(value)
				|| !method.satisfyDistanceReq(rows, value)) {
			if (value - original > range + range / 4) {
				System.out.println("No Value");
			}
			value++;
		}
		return value;
	}

	private static int decrease(int[] rows, int original, int range) {
		int value = original - range;
		while (!method.isValid(value)
				|| !method.satisfyDistanceReq(rows, value)) {
			if (original - value < range - range / 4) {
				break;
			}
			value++;
		}
		while (!method.isValid(value)
				|| !method.satisfyDistanceReq(rows, value)) {
			if (original - value > range + range / 4) {
				System.out.println("No Value");
			}
			value--;
		}
		return value;
	}

	private static int varyData(int[] rows, int original, int range) {
		int min = original - range;
		min = (min > 0) ? min : 0;
		int max = original + range;
		max = (max < 100) ? max : 99;
		return method.randomData(rows, min, max);
	}

	private static void replace(int[] arrays, int[] index, int min, int max) {
		int value;
		for (int i = 0; i < index.length; i++) {
			arrays[index[i]] = 0;
			if (i > 0) {
				value = arrays[index[i - 1]];
				while (Math.abs(value - arrays[index[i - 1]]) < 15
						|| Math.abs(value - arrays[index[i - 1]]) % 10 == 0) {
					value = method.randomData(arrays, min, max);
				}
				arrays[index[i]] = value;
			} else {
				arrays[index[i]] = method.randomData(arrays, min, max);
			}
		}
	}

}
