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

// Value Retrieval Task

public class Task1_GenerateData {

	// static String writeDirectory = "assets/excel/";
	static String writeDirectory = "/Users/Firenized/Dropbox/Ploy/Workspace/Dissertation_Visualization/WebContent/assets/data/";
	static CommonMethods method = new CommonMethods(writeDirectory);

	// Standard Data: 8 rows, 4 variables
	static int[][] standard = new int[8][4];
	// Data for each variable: 4 variables, 8 rows
	static int[][] variables = new int[4][8];

	// QA for each picture
	static int questionColIndex, answerColIndex, answerRowIndex,
			correctChoiceIndex;
	static int[] choiceRowIndex;
	// 0 = correct answer, 1 = easy distractor, 2 = medium, 3 = hard
	static int[] choicesType;

	// Choice position
	static LinkedList<Integer> correctChoicePosition = new LinkedList<Integer>(
			Arrays.asList(0, 1, 2, 3, 0, 1));

	public static void main(String[] args) {
		// For 6 stimuli
		for (int i = 1; i <= 6; i++) {
			createTable(i);
			method.writeTablesAndQuestionsTask1(i, standard, questionColIndex,
					answerColIndex, answerRowIndex, correctChoiceIndex,
					choiceRowIndex, choicesType, false);
		}
		// Write T1 question and answer
		String QA = "Stimuli,Question,"
				+ "Choice1,Choice1 Type,Choice2,Choice2 Type,Choice3,Choice3 Type,Choice4,Choice4 Type,"
				+ "Correct Answer,Correct Position\n";
		QA += method.getQA().toString().trim();
		method.writeFile("T1.csv", QA);
	}

	private static void createTable(int stimuli) {
		// Create data for each variable
		variables = new int[4][8];
		for (int i = 0; i < 4; i++) {
			variables[i] = method.createRandomData();
		}

		// Assign value to standard data
		standard = method.rowToColumn(variables);
		
		createQuestion(stimuli);
	}

	private static void createQuestion(int stimuli) {

		// Select question and answer variables
		// Easy
		if (stimuli == 1) {
			// forward, 2 jump from first variable
			questionColIndex = 0;
			answerColIndex = 2;
		} else if (stimuli == 2) {
			// forward, 3 jump from first variable
			questionColIndex = 0;
			answerColIndex = 3;
		}

		// Medium
		else if (stimuli == 3) {
			// backward, 2 jump to first variable
			questionColIndex = 2;
			answerColIndex = 0;
		} else if (stimuli == 4) {
			// backward, 3 jump to first variable
			questionColIndex = 3;
			answerColIndex = 0;
		}

		// Hard
		else if (stimuli == 5) {
			// forward, 2 jump
			questionColIndex = 1;
			answerColIndex = 3;
		} else if (stimuli == 6) {
			// backward, 2 jump
			questionColIndex = 3;
			answerColIndex = 1;
		}

		// Select correct answer row
		LinkedList<Integer> rowIndex = new LinkedList<Integer>(Arrays.asList(0,
				1, 2, 3, 4, 5, 6, 7));
		answerRowIndex = method.randomLinkedList(rowIndex, true);

		// Select choice position
		choiceRowIndex = new int[4];
		choicesType = new int[4];
		LinkedList<Integer> choicePosition = new LinkedList<Integer>(
				Arrays.asList(0, 1, 2, 3));

		// choice position for correct answer
		correctChoiceIndex = method.randomLinkedList(correctChoicePosition,
				true);
		choicePosition.remove(correctChoiceIndex);
		choiceRowIndex[correctChoiceIndex] = answerRowIndex;
		choicesType[correctChoiceIndex] = 0;

		// choice position for distractors
		int index;
		for (int i = 1; i <= 3; i++) {
			index = method.randomLinkedList(choicePosition, true);
			choiceRowIndex[index] = selectChoice(i, rowIndex);
			choicesType[index] = i;
		}
	}

	private static int selectChoice(int range, LinkedList<Integer> rowIndex) {
		LinkedList<Integer> tempRowIndex = new LinkedList<Integer>();
		tempRowIndex.addAll(rowIndex);

		// distractor range: 1 = easy, 2 = medium, 3 = hard
		int midRange = 0;
		if (range == 1) {
			midRange = 50;
		} else if (range == 2) {
			midRange = 27;
		} else if (range == 3) {
			midRange = 7;
		}

		int answer = standard[answerRowIndex][answerColIndex];
		int choiceIndex = -1;
		int choice = -1;
		int minRange, maxRange;
		int[] col;

		while (!satisfyDistractionRange(range, answer, choice)) {
			if (!tempRowIndex.isEmpty()) {
				choiceIndex = method.randomLinkedList(tempRowIndex, true);
				choice = standard[choiceIndex][answerColIndex];
			} else {
				// No value is satisfy >> random new value
				col = variables[answerColIndex];
				// Random row index
				choiceIndex = method.randomLinkedList(rowIndex, false);
				// Reset value
				col[choiceIndex] = 0;
				// Set value range
				minRange = answer - midRange;
				minRange = (minRange < 0) ? 0 : minRange;
				maxRange = answer + midRange;
				maxRange = (maxRange > 100) ? 99 : maxRange;

				// Random new value in the range
				while (!method.isValid(choice)
						|| !method.satisfyDistanceReq(col, choice)
						|| !satisfyDistractionRange(range, answer, choice)) {
					choice = method.randomInt(minRange, maxRange);
				}

				// Replace the old value in standard data
				col[choiceIndex] = choice;
				standard[choiceIndex][answerColIndex] = choice;

			}
		}

		rowIndex.remove(new Integer(choiceIndex));
		return choiceIndex;
	}

	private static boolean satisfyDistractionRange(int range, int answer,
			int value) {

		if (value == -1) {
			return false;
		}

		// Range: 1 = first range, 2 = second range, 3 = third range
		int minRange = 0, maxRange = 0;

		// assign range value
		if (range == 1) {
			minRange = 40;
			maxRange = 80;
		} else if (range == 2) {
			minRange = 15;
			maxRange = 35;
		} else if (range == 3) {
			minRange = 5;
			maxRange = 10;
		}

		// check range
		if (Math.abs(answer - value) >= minRange
				&& Math.abs(answer - value) <= maxRange) {
			return true;
		}
		return false;
	}

}
