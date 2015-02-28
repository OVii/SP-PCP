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

// Anomaly Detection Task

public class Task3_GenerateData {

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
			Arrays.asList(0, 1, 2, 3, 0, 1));

	public static void main(String[] args) {
		String question = "Which data point(s) is(are) outlier(s)?";
		// For 6 stimuli
		for (int i = 1; i <= 6; i++) {
			createTable(i);
			method.writeTablesAndQuestions(3, i, standard, question, choices,
					choicesType, correctChoiceIndex, false);
		}
		// Write T3 question and answer
		String QA = "Stimuli,Question,"
				+ "Choice1,Choice1 Type,Choice2,Choice2 Type,Choice3,Choice3 Type,Choice4,Choice4 Type,"
				+ "Correct Answer,Correct Position\n";
		QA += method.getQA().toString().trim();
		method.writeFile("T3.csv", QA);
	}

	private static void createTable(int stimuli) {
		boolean increase;
		int[] outlierIndex;
		LinkedList<int[]> oldChoiceIndex = new LinkedList<int[]>();
		int index1, index2;

		// Easy
		if (stimuli == 1) {
			increase = true;
			index1 = method.randomInt(0, 7);
			outlierIndex = new int[] { index1 };

			variables = createData(increase, 30, 70, outlierIndex, true, true,
					13);

			// correct answer
			oldChoiceIndex.add(outlierIndex);
			// easy choice
			oldChoiceIndex.add(new int[] { 4 });
			// medium choice
			oldChoiceIndex.add(new int[] { 0, index1 });
			// hard choice
			oldChoiceIndex.add(new int[] { 7, index1 });

		} else if (stimuli == 2) {
			increase = false;
			index1 = method.randomInt(0, 3);
			index2 = method.randomInt(4, 7);
			outlierIndex = new int[] { index1, index2 };

			variables = createData(increase, 40, 80, outlierIndex, true, true,
					13);

			// correct answer
			oldChoiceIndex.add(outlierIndex);
			// easy choice
			oldChoiceIndex.add(new int[] { 4, 5 });
			// medium choice
			oldChoiceIndex.add(new int[] { 7 });
			// hard choice
			oldChoiceIndex.add(new int[] { index1 });
		}

		// Medium
		else if (stimuli == 3) {
			increase = false;
			index1 = method.randomInt(0, 7);
			outlierIndex = new int[] { index1 };

			variables = createData(increase, 30, 75, outlierIndex, false, true,
					15);

			// correct answer
			oldChoiceIndex.add(outlierIndex);
			// easy choice
			oldChoiceIndex.add(new int[] { 3 });
			// medium choice
			oldChoiceIndex.add(new int[] { 0, 1 });
			// hard choice
			oldChoiceIndex.add(new int[] { index1, 3 });

		} else if (stimuli == 4) {
			increase = true;
			index1 = method.randomInt(0, 3);
			index2 = method.randomInt(4, 7);
			outlierIndex = new int[] { index1, index2 };

			variables = createData(increase, 40, 80, outlierIndex, false, true,
					15);

			// correct answer
			oldChoiceIndex.add(outlierIndex);
			// easy choice
			oldChoiceIndex.add(new int[] { 4, 5 });
			// medium choice
			oldChoiceIndex.add(new int[] { 7 });
			// hard choice
			oldChoiceIndex.add(new int[] { index1 });
		}

		// Hard
		else if (stimuli == 5) {
			increase = true;
			index1 = method.randomInt(0, 7);
			outlierIndex = new int[] { index1 };

			variables = createData(increase, 30, 75, outlierIndex, true, false,
					1);

			// correct answer
			oldChoiceIndex.add(outlierIndex);
			// easy choice
			oldChoiceIndex.add(new int[] { 4 });
			// medium choice
			oldChoiceIndex.add(new int[] { 0, 1 });
			// hard choice
			oldChoiceIndex.add(new int[] { index1, 0 });

		} else if (stimuli == 6) {
			increase = false;
			index1 = method.randomInt(0, 3);
			index2 = method.randomInt(4, 7);
			outlierIndex = new int[] { index1, index2 };

			variables = createData(increase, 40, 80, outlierIndex, true, false,
					0);

			// correct answer
			oldChoiceIndex.add(outlierIndex);
			// easy choice
			oldChoiceIndex.add(new int[] { 2, 5 });
			// medium choice
			oldChoiceIndex.add(new int[] { 0 });
			// hard choice
			oldChoiceIndex.add(new int[] { index1 });

		}

		// Shuffle data
		standard = method.rowToColumn(variables);
		// method.shuffle(standard);
		// variables = method.rowToColumn(standard);
		//
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

	private static int[][] createData(boolean increase, int minStart,
			int maxStart, int[] outlierIndex, boolean diffCorrelation,
			boolean diffRange, int outlierOffset) {

		int[][] variables = new int[4][8];
		boolean outlierBelow = increase;

		// First Variable
		if (diffRange && diffCorrelation) {
			variables[0] = method.createRandomData(minStart, maxStart);
		} else {
			int[] rows = new int[8];

			// Outliers
			if (diffCorrelation) {
				int average = (maxStart + minStart) / 2;
				for (int i = 0; i < outlierIndex.length; i++) {
					rows[outlierIndex[i]] = method.randomData(rows,
							average - 10*(1-outlierOffset), average + 10*outlierOffset);
				}
			}
			// Other Data
			for (int i = 0; i < rows.length; i++) {
				if (method.contain(outlierIndex, i)) {
					continue;
				} else {
					rows[i] = method.randomData(rows, minStart, maxStart);
				}
			}

			// Outliers
			if (diffRange) {
				addDiffRangeOutliers(rows, outlierIndex, outlierOffset,
						outlierBelow);
			}

			variables[0] = rows;
		}

		// Other variables
		for (int i = 1; i < 4; i++) {
			variables[i] = createTrendData(variables[i - 1], increase,
					outlierIndex, diffCorrelation, diffRange, outlierOffset,
					outlierBelow);
			increase = !increase;
		}
		return variables;
	}

	private static int[] createTrendData(int[] previousData, boolean increase,
			int[] outlierIndex, boolean diffCorrelation, boolean diffRange,
			int outlierOffset, boolean outlierBelow) {
		int minRange = 12, maxRange = 25;

		// 8 rows
		int[] rows = new int[8];

		// Same Range & Diff Correlation
		if (diffCorrelation && !diffRange) {
			addDiffCorOutliers(rows, previousData, increase, outlierIndex);
		}

		for (int i = 0; i < rows.length; i++) {
			if (method.contain(outlierIndex, i)) {
				continue;
			}
			// Other data
			else {
				rows[i] = randomTrend(rows, previousData[i], increase,
						minRange, maxRange);
			}
		}

		// Different Value Range & Different Correlation
		if (diffRange && diffCorrelation) {
			addDiffRangeDiffCorOutliers(rows, previousData, increase,
					outlierIndex, outlierOffset);
		}

		// Diff Range & Same Correlation
		else if (diffRange) {
			addDiffRangeOutliers(rows, outlierIndex, outlierOffset,
					outlierBelow);
		}

		return rows;
	}

	private static void addDiffCorOutliers(int[] rows, int[] previousData,
			boolean increase, int[] outlierIndex) {

		boolean outliersIncrease = !increase;

		for (int i = 0; i < outlierIndex.length; i++) {
			rows[outlierIndex[i]] = randomTrend(rows,
					previousData[outlierIndex[i]], outliersIncrease, 0, 10);
		}
	}

	private static void addDiffRangeOutliers(int[] rows, int[] outlierIndex,
			int outlierOffset, boolean outlierBelow) {

		int min = 10, max = 100;

		if (outlierBelow) {
			max = method.findMinValue(rows) - outlierOffset;
			min = max - 10;
			min = (min < 10) ? 10 : min;
		} else {
			min = method.findMaxValue(rows) + outlierOffset;
			max = min + 10;
			max = (max > 100) ? 100 : max;
		}

		for (int i = 0; i < outlierIndex.length; i++) {
			rows[outlierIndex[i]] = method.randomData(rows, min, max);
		}

	}

	private static void addDiffRangeDiffCorOutliers(int[] rows,
			int[] previousData, boolean increase, int[] outlierIndex,
			int outlierOffset) {

		int min = 10, max = 100;
		int value1 = 0, value2 = 0;
		boolean outliersIncrease = !increase;

		if (outliersIncrease) {
			value1 = method.findMaxValue(rows) + outlierOffset;
		} else {
			value1 = method.findMinValue(rows) - outlierOffset;
		}

		for (int i = 0; i < outlierIndex.length; i++) {
			if (outliersIncrease) {
				value2 = previousData[outlierIndex[i]];
				min = (value1 > value2) ? value1 : value2;
			} else {
				value2 = previousData[outlierIndex[i]];
				max = (value1 < value2) ? value1 : value2;
			}
			rows[outlierIndex[i]] = method.randomData(rows, min, max);
		}
	}

	private static int randomTrend(int[] rows, int previousData,
			boolean increase, int minRange, int maxRange) {
		int min, max;
		if (increase) {
			min = previousData + minRange;
			max = previousData + maxRange;
			max = (max < 100) ? max : 99;
			return method.randomData(rows, min, max);
		} else {
			max = previousData - minRange;
			min = previousData - maxRange;
			min = (min > 0) ? min : 0;
			return method.randomData(rows, min, max);
		}
	}

}
