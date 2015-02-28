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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;

public class createQuestion {

	// static String writeDirectory = "assets/excel/";
	static String writeDirectory = "/Users/Firenized/Dropbox/Ploy/Workspace/Dissertation_Visualization/WebContent/assets/data/";
	static CommonMethods method = new CommonMethods(writeDirectory);

	// Question and choices
	static String[] question = new String[4];
	static int correctChoiceIndex;
	static int[][] choices;
	static int[] choicesType;

	// Choice position
	static LinkedList<Integer> correctChoicePosition;

	public static void main(String[] args) throws IOException {

		// Question 2-4
		question[1] = "Which 4 data points form the most appropriate cluster?";
		question[2] = "Which data point(s) is(are) outlier(s)?";
		question[3] = "Which data point(s) has(have) the most changes?";

		// Read input data
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter Task (2-4) : ");
		int task = Integer.parseInt(br.readLine());

		// Choice position
		if (task == 2 || task == 4) {
			correctChoicePosition = new LinkedList<Integer>(Arrays.asList(2, 3,
					0, 1, 2, 3));
		} else {
			correctChoicePosition = new LinkedList<Integer>(Arrays.asList(0, 1,
					2, 3, 0, 1));
		}
		int representation, point;
		int[] choice;
		int value;

		for (int i = 1; i <= 6; i++) {
			System.out.println("Stimuli " + i);
			System.out.print("Enter preferred representation: ");
			representation = Integer.parseInt(br.readLine());
			LinkedList<int[]> oldIndex = new LinkedList<int[]>();
			System.out.println("Enter choices for T" + task + "S" + i + "R"
					+ representation);
			System.out.println("Correct Answer, Easy, Medium, Hard");
			for (int j = 0; j < 4; j++) {
				System.out.println("choice " + (j + 1));
				if (task == 2) {
					System.out.println("4 points");
					point = 4;
				} else {
					System.out.print("How many data points? ");
					point = Integer.parseInt(br.readLine());
				}
				choice = new int[point];
				for (int k = 0; k < point; k++) {
					System.out.print("Enter point" + (k + 1) + " : ");
					value = Integer.parseInt(br.readLine());
					if (representation == 2) {
						value = ((value - 1) + 5) % 8;
					} else if (representation == 3) {
						value = ((value - 1) + 2) % 8;
					}
					choice[k] = value;
				}
				oldIndex.add(choice);
			}

			// Select choice position
			choices = new int[4][];
			choicesType = new int[4];
			LinkedList<Integer> choicePosition = new LinkedList<Integer>(
					Arrays.asList(0, 1, 2, 3));

			// choice position for correct answer
			correctChoiceIndex = method.randomLinkedList(correctChoicePosition,
					true);
			choicePosition.remove(correctChoiceIndex);
			choices[correctChoiceIndex] = oldIndex.get(0);
			choicesType[correctChoiceIndex] = 0;

			// choice position for distractors
			int index;
			for (int k = 1; k <= 3; k++) {
				index = method.randomLinkedList(choicePosition, true);
				choices[index] = oldIndex.get(k);
				choicesType[index] = k;
			}

			method.writeAllQuestion(task, i, question[task - 1], choices,
					choicesType, correctChoiceIndex);

		}
		String QA = "Stimuli,Question,"
				+ "Choice1,Choice1 Type,Choice2,Choice2 Type,Choice3,Choice3 Type,Choice4,Choice4 Type,"
				+ "Correct Answer,Correct Position\n";
		QA += method.getQA().toString().trim();
		method.writeFile("T" + task + ".csv", QA);
	}
}
