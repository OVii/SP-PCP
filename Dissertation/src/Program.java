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

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.Timer;

import java.awt.BorderLayout;

import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JProgressBar;

public class Program {

	private JFrame frame;
	private JPanel center;
	private int width;
	private int height;

	// Time
	private int maxTime = 90;
	private JProgressBar progressBar;
	private Timer timer;
	private JLabel time, lblTotalTimeLeft;

	// Font
	private Font nextButtonFont = new Font("Calibri", Font.PLAIN, 30);
	private Font titleFont = new Font("Calibri", Font.PLAIN, 40);
	private Font formHeaderFont = new Font("Calibri", Font.PLAIN, 25);
	private Font headerFont = new Font("Calibri", Font.PLAIN, 30);
	private Font elementFont = new Font("Calibri", Font.PLAIN, 22);
	private Font sectionHeaderFont = new Font("Calibri", Font.PLAIN, 55);
	private Font sectionContentFont = new Font("Calibri", Font.PLAIN, 35);
	private Font task1ButtonFont = new Font("Calibri", Font.PLAIN, 40);
	private Font choiceButtonFont = new Font("Calibri", Font.PLAIN, 30);

	// Information
	private String id = "", gender = "", age = "", occupation = "",
			colorBlind = "", colorBlindType = "-";
	private String tableFamiliar, scpFamiliar, pcpFamiliar;
	private final ButtonGroup genderButton = new ButtonGroup();
	private JTextField idField;
	private final ButtonGroup ageButton = new ButtonGroup();
	private final ButtonGroup occupationButton = new ButtonGroup();
	private final ButtonGroup ColorBlind = new ButtonGroup();
	private final ButtonGroup ColorBlindChoice = new ButtonGroup();
	private final JButton infoNext = new JButton("Next");

	// Trial
	private String readDirectory = "assets/excel/";
	private String picDirectory = "assets/picture/";
	private String answer, answerPosition;
	private Timestamp startTimeDT, readyTimeDT, answerTimeDT, clickNextTimeDT;
	private String startTimeT, readyTimeT, answerTimeT, clickNextTimeT;
	private long startTime, readyTime, answerTime, clickNextTime;
	// readingTime = readyTime - startTime, responseTime = answerTime -
	// readyTime
	private long readingTime, responseTime;
	// Map picture name to QA
	// String[7]: Question, Choice1, Choice2, Choice3, Choice4, Correct Answer,
	// Correct Position
	private HashMap<String, String[]> QA = new HashMap<String, String[]>();
	// String[21]: taskName, correctAnswer, correctPosition,
	// selectedAnswer, selectedPosition, selectedChoiceType, correct,
	// startTime (date with time), startTime (time), startTime (ms),
	// readyTime *3, answerTime * 3, clickNextTime * 3, readingTime,
	// responseTime
	private LinkedList<String[]> trainingResponse = new LinkedList<String[]>();
	private LinkedList<String[]> testingResponse = new LinkedList<String[]>();

	// Feedback
	// String[4][3]: 4 tasks, 3 visualizations
	// Tasks: value retrieval, clustering, outlier, change
	private String[][] feedback = new String[4][3];

	// Output
	private String writeDirectory = "result/";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Program window = new Program();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Program() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.width;
		height = screenSize.height;
		frame = new JFrame();
		frame.setBounds(0, 0, width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);

		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		// North Panel
		JPanel north = new JPanel();
		north.setPreferredSize(new Dimension(this.width, this.height / 12 * 2));
		north.setLayout(new BoxLayout(north, 2));
		north.setBorder(BorderFactory.createEmptyBorder(100, 220, 10, 100));
		frame.getContentPane().add(north, BorderLayout.NORTH);

		// Progress Bar
		progressBar = new JProgressBar(0, maxTime);
		// progressBar.setStringPainted(true);
		progressBar.setVisible(false);
		north.add(Box.createHorizontalGlue());
		north.add(progressBar);

		lblTotalTimeLeft = new JLabel("Time Left:");
		lblTotalTimeLeft.setHorizontalAlignment(SwingConstants.TRAILING);
		lblTotalTimeLeft.setFont(new Font("Calibri", Font.PLAIN, 20));
		lblTotalTimeLeft.setVisible(false);
		north.add(Box.createHorizontalGlue());
		north.add(lblTotalTimeLeft);

		time = new JLabel("time");
		time.setFont(new Font("Calibri", Font.PLAIN, 30));
		time.setVisible(false);
		north.add(Box.createRigidArea(new Dimension(10, 0)));
		north.add(time);

		// Center Panel
		center = new JPanel(new CardLayout(0, 0));
		center.setPreferredSize(new Dimension(this.width - 100,
				this.height / 12 * 9));
		frame.getContentPane().add(center, BorderLayout.CENTER);

		// First Page: Information
		addInfoPage();

		// Second Page: Representation Familiarity
		addScalePage(
				"familiar",
				"training",
				"Please rate how familiar are you with each type of visualization.",
				"Familiar");

		// Load File
		readFile("training.csv");
		readFile("T1.csv");
		readFile("T2.csv");
		readFile("T3.csv");
		readFile("T4.csv");

		String trialType;

		// Training Section
		trialType = "Training";
		addSection("training", "training1", trialType + " Section",
				getSectionContent(0, trialType));
		addQuestionSection("training1", "training2", "Training 1", 1,
				"training1R1", "training1R2", "training1R3", trialType);
		addQuestionSection("training2", "training3", "Training 2", 2,
				"training2R1", "training2R2", "training2R3", trialType);
		addQuestionSection("training3", "training4", "Training 3", 3,
				"training3R1", "training3R2", "training3R3", trialType);
		addQuestionSection("training4", "testing", "Training 4", 4,
				"training4R1", "training4R2", "training4R3", trialType);

		// readFile("presentation.csv");
		// addQuestionSection("training1", "training2", "Training 1", 1,
		// "presentation1R1", "presentation1R2", "presentation1R3",
		// trialType);
		// addQuestionSection("training2", "training3", "Training 2", 2,
		// "presentation2R1", "presentation2R2", "presentation2R3",
		// trialType);
		// addQuestionSection("training3", "training4", "Training 3", 3,
		// "presentation3R1", "presentation3R2", "presentation3R3",
		// trialType);
		// addQuestionSection("training4", "testing", "Training 4", 4,
		// "presentation4R1", "presentation4R2", "presentation4R3",
		// trialType);

		// Testing Section
		trialType = "Testing";
		addSection("testing", "testing1", trialType + " Section",
				getSectionContent(5, trialType));

		// String nextPanel;
		// int count = 1;
		// for (int i = 1; i <= 4; i++) {
		// for (int j = 1; j <= 6; j++) {
		// if (i * j == 24) {
		// nextPanel = "feedback";
		// } else {
		// nextPanel = "testing" + (count + 1);
		// }
		// addQuestionSection("testing" + count, nextPanel, "Section "
		// + count, i, "T" + i + "S" + j + "R1", "T" + i + "S" + j
		// + "R2", "T" + i + "S" + j + "R3", trialType);
		// count++;
		// }
		// }

		// 1: Section 1 - 4
		addQuestionSection("testing1", "testing2", "Section 1", 1, "T1S2R3",
				"T1S3R1", "T1S5R2", trialType);
		addQuestionSection("testing2", "testing3", "Section 2", 2, "T2S2R2",
				"T2S3R3", "T2S5R1", trialType);
		addQuestionSection("testing3", "testing4", "Section 3", 3, "T3S1R1",
				"T3S3R2", "T3S6R3", trialType);
		addQuestionSection("testing4", "testing5", "Section 4", 4, "T4S1R3",
				"T4S3R1", "T4S6R2", trialType);

		// 2: Section 5 - 8
		addQuestionSection("testing5", "testing6", "Section 5", 2, "T2S1R1",
				"T2S4R3", "T2S5R2", trialType);
		addQuestionSection("testing6", "testing7", "Section 6", 4, "T4S2R3",
				"T4S3R2", "T4S5R1", trialType);
		addQuestionSection("testing7", "testing8", "Section 7", 1, "T1S1R2",
				"T1S4R1", "T1S6R3", trialType);
		addQuestionSection("testing8", "testing9", "Section 8", 3, "T3S2R1",
				"T3S4R3", "T3S5R2", trialType);

		// 3: Section 9 - 12
		addQuestionSection("testing9", "testing10", "Section 9", 4, "T4S1R2",
				"T4S4R1", "T4S6R3", trialType);
		addQuestionSection("testing10", "testing11", "Section 10", 2, "T2S2R3",
				"T2S3R2", "T2S6R1", trialType);
		addQuestionSection("testing11", "testing12", "Section 11", 3, "T3S1R3",
				"T3S3R1", "T3S6R2", trialType);
		addQuestionSection("testing12", "testing13", "Section 12", 1, "T1S1R3",
				"T1S4R2", "T1S6R1", trialType);

		// 4: Section 13 - 16
		addQuestionSection("testing13", "testing14", "Section 13", 4, "T4S1R1",
				"T4S4R3", "T4S5R2", trialType);
		addQuestionSection("testing14", "testing15", "Section 14", 3, "T3S2R2",
				"T3S4R1", "T3S5R3", trialType);
		addQuestionSection("testing15", "testing16", "Section 15", 1, "T1S2R2",
				"T1S3R3", "T1S5R1", trialType);
		addQuestionSection("testing16", "testing17", "Section 16", 2, "T2S2R1",
				"T2S4R2", "T2S6R3", trialType);

		// 5: Section 17 - 20
		addQuestionSection("testing17", "testing18", "Section 17", 3, "T3S2R3",
				"T3S4R2", "T3S5R1", trialType);
		addQuestionSection("testing18", "testing19", "Section 18", 4, "T4S2R2",
				"T4S3R3", "T4S6R1", trialType);
		addQuestionSection("testing19", "testing20", "Section 19", 2, "T2S1R2",
				"T2S3R1", "T2S5R3", trialType);
		addQuestionSection("testing20", "testing21", "Section 20", 1, "T1S1R1",
				"T1S3R2", "T1S5R3", trialType);

		// 6: Section 21 - 24
		addQuestionSection("testing21", "testing22", "Section 21", 2, "T2S1R3",
				"T2S4R1", "T2S6R2", trialType);
		addQuestionSection("testing22", "testing23", "Section 22", 1, "T1S2R1",
				"T1S4R3", "T1S6R2", trialType);
		addQuestionSection("testing23", "testing24", "Section 23", 4, "T4S2R1",
				"T4S4R2", "T4S5R3", trialType);
		addQuestionSection("testing24", "feedback", "Section 24", 3, "T3S1R2",
				"T3S3R3", "T3S6R1", trialType);

		// Feedback
		addSection("feedback", "feedback1", "Feedback",
				"For each task, please rate how each type of visualization has helped you.");
		addScalePage("feedback1", "feedback2", "Value Retrieval Task",
				"Effective");
		addScalePage("feedback2", "feedback3", "Clustering Task", "Effective");
		addScalePage("feedback3", "feedback4", "Outlier Detection Task",
				"Effective");
		addScalePage("feedback4", "thankYou", "Change Detection Task",
				"Effective");

		// Ending
		addThankYouPage();

		// West Panel
		JPanel west = new JPanel();
		west.setPreferredSize(new Dimension(50, height));
		frame.getContentPane().add(west, BorderLayout.WEST);

		// East Panel
		JPanel east = new JPanel();
		east.setPreferredSize(new Dimension(50, this.height));
		frame.getContentPane().add(east, BorderLayout.EAST);

		// South Panel
		JPanel south = new JPanel();
		south.setPreferredSize(new Dimension(this.width, this.height / 12));
		frame.getContentPane().add(south, BorderLayout.SOUTH);

	}

	private void addInfoPage() {
		JPanel info = new JPanel();
		center.add(info, "info");

		info.setLayout(new GridBagLayout());

		GridBagConstraints titleConstraints = new GridBagConstraints();
		titleConstraints.fill = 1;
		titleConstraints.anchor = 19;
		titleConstraints.gridwidth = 7;
		titleConstraints.weighty = 0.1D;
		titleConstraints.insets = new Insets(0, 0, 60, 0);
		GridBagConstraints formHeaderConstraints = new GridBagConstraints();
		formHeaderConstraints.anchor = 24;
		formHeaderConstraints.weighty = 0.1D;
		formHeaderConstraints.insets = new Insets(0, 0, 20, 50);
		GridBagConstraints formElementConstraints = new GridBagConstraints();
		formElementConstraints.anchor = 23;
		formElementConstraints.weighty = 0.1D;
		formElementConstraints.insets = new Insets(0, 0, 20, 40);
		GridBagConstraints nextButtonConstraints = new GridBagConstraints();
		nextButtonConstraints.ipadx = 100;
		nextButtonConstraints.ipady = 20;
		nextButtonConstraints.anchor = 26;
		nextButtonConstraints.gridwidth = 2;

		JLabel lblNewLabel = new JLabel(
				"An Empirical Study on Parallel Coordinates and Scatter Plots");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setFont(titleFont);
		lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		titleConstraints.gridx = 0;
		titleConstraints.gridy = 0;
		info.add(lblNewLabel, titleConstraints);

		// ID
		JLabel lblId = new JLabel("ID:");
		lblId.setFont(formHeaderFont);
		formHeaderConstraints.gridx = 0;
		formHeaderConstraints.gridy = 1;
		info.add(lblId, formHeaderConstraints);

		idField = new JTextField();
		idField.setFont(elementFont);
		idField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				id = idField.getText();
				if (selectAllInfo()) {
					infoNext.setVisible(true);
				} else {
					infoNext.setVisible(false);
				}
			}
		});
		idField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				id = idField.getText();
				if (selectAllInfo()) {
					infoNext.setVisible(true);
				} else {
					infoNext.setVisible(false);
				}
			}
		});
		formElementConstraints.gridx = 1;
		formElementConstraints.gridy = 1;
		formElementConstraints.gridwidth = 2;
		formElementConstraints.fill = 2;
		info.add(this.idField, formElementConstraints);

		// Gender
		JLabel lblGender = new JLabel("Gender:");
		lblGender.setFont(formHeaderFont);
		formHeaderConstraints.gridx = 0;
		formHeaderConstraints.gridy = 2;
		info.add(lblGender, formHeaderConstraints);

		JRadioButton rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setFont(elementFont);
		rdbtnMale.addActionListener(new GenderListener());
		genderButton.add(rdbtnMale);
		formElementConstraints.gridx = 1;
		formElementConstraints.gridy = 2;
		formElementConstraints.gridwidth = 1;
		formElementConstraints.fill = 0;
		info.add(rdbtnMale, formElementConstraints);

		JRadioButton rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setFont(elementFont);
		rdbtnFemale.addActionListener(new GenderListener());
		genderButton.add(rdbtnFemale);
		formElementConstraints.gridx = 2;
		formElementConstraints.gridy = 2;
		formElementConstraints.gridwidth = 1;
		info.add(rdbtnFemale, formElementConstraints);

		// Age
		JLabel lblAgeGroup = new JLabel("Age Group:");
		lblAgeGroup.setFont(formHeaderFont);
	    formHeaderConstraints.gridx = 0;
	    formHeaderConstraints.gridy = 3;
	    info.add(lblAgeGroup, formHeaderConstraints);

		JRadioButton rdbtnOrLess = new JRadioButton("19 or less");
		rdbtnOrLess.setFont(elementFont);
	    rdbtnOrLess.addActionListener(new AgeListener());
	    ageButton.add(rdbtnOrLess);
	    formElementConstraints.gridx = 1;
	    formElementConstraints.gridy = 3;
	    formElementConstraints.gridwidth = 1;
	    formElementConstraints.insets = new Insets(0, 0, 20, 80);
	    info.add(rdbtnOrLess, formElementConstraints);

		JRadioButton radioButton = new JRadioButton("20-29");
		radioButton.setFont(elementFont);
	    radioButton.addActionListener(new AgeListener());
	    ageButton.add(radioButton);
	    formElementConstraints.gridx = 2;
	    formElementConstraints.gridy = 3;
	    formElementConstraints.gridwidth = 1;
	    formElementConstraints.insets = new Insets(0, 0, 20, 115);
	    info.add(radioButton, formElementConstraints);

		JRadioButton radioButton_1 = new JRadioButton("30-39");
		radioButton_1.setFont(elementFont);
	    radioButton_1.addActionListener(new AgeListener());
	    ageButton.add(radioButton_1);
	    formElementConstraints.gridx = 3;
	    formElementConstraints.gridy = 3;
	    formElementConstraints.gridwidth = 1;
	    formElementConstraints.insets = new Insets(0, 0, 20, 110);
	    info.add(radioButton_1, formElementConstraints);

		JRadioButton radioButton_2 = new JRadioButton("40-49");
		radioButton_2.setFont(elementFont);
	    radioButton_2.addActionListener(new AgeListener());
	    ageButton.add(radioButton_2);
	    formElementConstraints.gridx = 4;
	    formElementConstraints.gridy = 3;
	    formElementConstraints.gridwidth = 1;
	    formElementConstraints.insets = new Insets(0, 0, 20, 110);
	    info.add(radioButton_2, formElementConstraints);

		JRadioButton radioButton_3 = new JRadioButton("50-59");
		radioButton_3.setFont(elementFont);
	    radioButton_3.addActionListener(new AgeListener());
	    ageButton.add(radioButton_3);
	    formElementConstraints.gridx = 5;
	    formElementConstraints.gridy = 3;
	    formElementConstraints.gridwidth = 1;
	    formElementConstraints.insets = new Insets(0, 0, 20, 110);
	    info.add(radioButton_3, formElementConstraints);

		JRadioButton rdbtnAndAbove = new JRadioButton("60 or above");
		rdbtnAndAbove.setFont(elementFont);
	    rdbtnAndAbove.addActionListener(new AgeListener());
	    ageButton.add(rdbtnAndAbove);
	    formElementConstraints.gridx = 6;
	    formElementConstraints.gridy = 3;
	    formElementConstraints.gridwidth = 1;
	    formElementConstraints.insets = new Insets(0, 0, 20, 40);
	    info.add(rdbtnAndAbove, formElementConstraints);

		// Occupation
		JLabel lblOccupation = new JLabel("Occupation:");
		lblOccupation.setFont(formHeaderFont);
	    formHeaderConstraints.gridx = 0;
	    formHeaderConstraints.gridy = 4;
	    info.add(lblOccupation, formHeaderConstraints);

		JRadioButton rdbtnUniversityStudent = new JRadioButton(
				"University Student");
		rdbtnUniversityStudent.setFont(elementFont);
	    rdbtnUniversityStudent.addActionListener(new OccupationListener());
	    occupationButton.add(rdbtnUniversityStudent);
	    formElementConstraints.gridx = 1;
	    formElementConstraints.gridy = 4;
	    formElementConstraints.gridwidth = 2;
	    info.add(rdbtnUniversityStudent, formElementConstraints);

		JRadioButton rdbtnUniversityStaff = new JRadioButton("University Staff");
		rdbtnUniversityStaff.setFont(elementFont);
	    rdbtnUniversityStaff.addActionListener(new OccupationListener());
	    occupationButton.add(rdbtnUniversityStaff);
	    formElementConstraints.gridx = 3;
	    formElementConstraints.gridy = 4;
	    formElementConstraints.gridwidth = 2;
	    info.add(rdbtnUniversityStaff, formElementConstraints);

		JRadioButton rdbtnOthers = new JRadioButton("Others");
		rdbtnOthers.setFont(elementFont);
	    rdbtnOthers.addActionListener(new OccupationListener());
	    occupationButton.add(rdbtnOthers);
	    formElementConstraints.gridx = 5;
	    formElementConstraints.gridy = 4;
	    formElementConstraints.gridwidth = 2;
	    info.add(rdbtnOthers, formElementConstraints);

		// Color Blindness
		JLabel lblColorBlind = new JLabel("Color Blindness:");
		lblColorBlind.setHorizontalAlignment(SwingConstants.RIGHT);
	    lblColorBlind.setFont(formHeaderFont);
	    formHeaderConstraints.gridx = 0;
	    formHeaderConstraints.gridy = 5;
	    formHeaderConstraints.gridheight = 3;
	    info.add(lblColorBlind, formHeaderConstraints);

		final JLabel lblPleaseSelect = new JLabel("Type:");
		lblPleaseSelect.setVisible(false);
		lblPleaseSelect.setHorizontalAlignment(SwingConstants.RIGHT);
	    lblPleaseSelect.setFont(elementFont);
	    formHeaderConstraints.gridx = 1;
	    formHeaderConstraints.gridy = 6;
	    formHeaderConstraints.gridheight = 1;
	    formHeaderConstraints.insets = new Insets(0, 0, 20, 0);
	    info.add(lblPleaseSelect, formHeaderConstraints);

		final JRadioButton rdbtnGreenBlue = new JRadioButton("Green / Blue");
		rdbtnGreenBlue.setVisible(false);
	    ColorBlindChoice.add(rdbtnGreenBlue);
	    rdbtnGreenBlue.setFont(elementFont);
	    rdbtnGreenBlue.addActionListener(new ColorBlindTypeListener());
	    formElementConstraints.gridx = 2;
	    formElementConstraints.gridy = 6;
	    formElementConstraints.gridwidth = 1;
	    info.add(rdbtnGreenBlue, formElementConstraints);

		final JRadioButton rdbtnRedGreen = new JRadioButton("Red / Green");
		rdbtnRedGreen.setVisible(false);
	    ColorBlindChoice.add(rdbtnRedGreen);
	    rdbtnRedGreen.setFont(elementFont);
	    rdbtnRedGreen.addActionListener(new ColorBlindTypeListener());
	    formElementConstraints.gridx = 3;
	    formElementConstraints.gridy = 6;
	    formElementConstraints.gridwidth = 1;
	    info.add(rdbtnRedGreen, formElementConstraints);

		final JRadioButton rdbtnBlueRed = new JRadioButton("Blue / Red");
		rdbtnBlueRed.setVisible(false);
	    ColorBlindChoice.add(rdbtnBlueRed);
	    rdbtnBlueRed.setFont(elementFont);
	    rdbtnBlueRed.addActionListener(new ColorBlindTypeListener());
	    formElementConstraints.gridx = 4;
	    formElementConstraints.gridy = 6;
	    formElementConstraints.gridwidth = 1;
	    info.add(rdbtnBlueRed, formElementConstraints);

		final JRadioButton rdbtnNotSure = new JRadioButton("Not sure");
		rdbtnNotSure.setVisible(false);
		ColorBlindChoice.add(rdbtnNotSure);
	    rdbtnNotSure.setFont(elementFont);
	    rdbtnNotSure.addActionListener(new ColorBlindTypeListener());
	    formElementConstraints.gridx = 5;
	    formElementConstraints.gridy = 6;
	    formElementConstraints.gridwidth = 1;
	    info.add(rdbtnNotSure, formElementConstraints);

		JRadioButton rdbtnNo = new JRadioButton("No");
		ColorBlind.add(rdbtnNo);
		rdbtnNo.setFont(elementFont);
		rdbtnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JRadioButton radioButton = (JRadioButton) e.getSource();
				if (radioButton.isSelected()) {
					colorBlind = radioButton.getText();
					lblPleaseSelect.setVisible(false);
					rdbtnGreenBlue.setVisible(false);
					rdbtnRedGreen.setVisible(false);
					rdbtnBlueRed.setVisible(false);
					rdbtnNotSure.setVisible(false);
					if (selectAllInfo()) {
						infoNext.setVisible(true);
					} else {
						infoNext.setVisible(false);
					}
				}
			}
		});
		formElementConstraints.gridx = 1;
	    formElementConstraints.gridy = 5;
	    formElementConstraints.gridwidth = 1;
	    info.add(rdbtnNo, formElementConstraints);

		JRadioButton rdbtnYes = new JRadioButton("Yes");
		ColorBlind.add(rdbtnYes);
		rdbtnYes.setFont(elementFont);
		rdbtnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JRadioButton radioButton = (JRadioButton) e.getSource();
				if (radioButton.isSelected()) {
					colorBlind = radioButton.getText();
					lblPleaseSelect.setVisible(true);
					rdbtnGreenBlue.setVisible(true);
					rdbtnRedGreen.setVisible(true);
					rdbtnBlueRed.setVisible(true);
					rdbtnNotSure.setVisible(true);
					if (selectAllInfo()) {
						infoNext.setVisible(true);
					} else {
						infoNext.setVisible(false);
					}
				}
			}
		});
		formElementConstraints.gridx = 2;
	    formElementConstraints.gridy = 5;
	    formElementConstraints.gridwidth = 1;
	    info.add(rdbtnYes, formElementConstraints);

		// Next Button
		infoNext.setVisible(false);
		infoNext.setFont(this.nextButtonFont);
		infoNext.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				CardLayout cl = (CardLayout) (center.getLayout());
				cl.show(center, "familiar");

				// Print Info
				System.out.println("ID: " + id);
				System.out.println("Gender: " + gender);
				System.out.println("Age Group: " + age);
				System.out.println("Occupation: " + occupation);
				System.out.println("Color Blindness: " + colorBlind);
				System.out.println("Color Blindness Type: " + colorBlindType);
			}
		});
		nextButtonConstraints.gridx = 5;
	    nextButtonConstraints.gridy = 7;
	    info.add(this.infoNext, nextButtonConstraints);
	    
	    JLabel blankLabelRow6 = new JLabel("");
	    formHeaderConstraints.gridx = 0;
	    formHeaderConstraints.gridy = 6;
	    formHeaderConstraints.gridheight = 1;
	    formHeaderConstraints.insets = new Insets(0, 0, 90, 0);
	    info.add(blankLabelRow6, formHeaderConstraints);

	    JLabel blankLabelRow7 = new JLabel("");
	    formHeaderConstraints.gridx = 0;
	    formHeaderConstraints.gridy = 7;
	    formHeaderConstraints.gridheight = 1;
	    info.add(blankLabelRow7, formHeaderConstraints);
	}

	private void addScalePage(final String panelName, final String nextPanel,
			String header, String scaleLabel) {
		JPanel scale = new JPanel();
		center.add(scale, panelName);
		ButtonGroup tableScale = new ButtonGroup();
		ButtonGroup scpScale = new ButtonGroup();
		ButtonGroup pcpScale = new ButtonGroup();
		scale.setLayout(new GridBagLayout());
		
		GridBagConstraints titleConstraints = new GridBagConstraints();
	    titleConstraints.fill = 1;
	    titleConstraints.anchor = 19;
	    titleConstraints.gridwidth = 5;
	    titleConstraints.weighty = 0.1D;
	    titleConstraints.insets = new Insets(0, 0, 60, 0);
	    GridBagConstraints formHeaderConstraints = new GridBagConstraints();
	    formHeaderConstraints.anchor = 20;
	    formHeaderConstraints.gridwidth = 5;
	    formHeaderConstraints.weighty = 0.1D;
	    GridBagConstraints formElementConstraints = new GridBagConstraints();
	    formElementConstraints.anchor = 19;
	    formElementConstraints.weighty = 0.1D;
	    formElementConstraints.insets = new Insets(0, 45, 40, 45);
	    GridBagConstraints nextButtonConstraints = new GridBagConstraints();
	    nextButtonConstraints.ipadx = 90;
	    nextButtonConstraints.ipady = 20;
	    nextButtonConstraints.insets = new Insets(30, 0, 0, 0);
	    nextButtonConstraints.anchor = 26;

		JLabel label_4 = new JLabel(header);
		label_4.setHorizontalTextPosition(SwingConstants.CENTER);
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setFont(titleFont);
	    titleConstraints.gridx = 0;
	    titleConstraints.gridy = 0;
	    scale.add(label_4, titleConstraints);

		// Next Button
		JButton scaleNext = new JButton("Next");
		scaleNext.setFont(this.nextButtonFont);
		scaleNext.setVisible(false);
		scaleNext.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				if (nextPanel.equals("thankYou")) {
					// Record value
					recordValues();
				}
				// Change panel
				CardLayout cl = (CardLayout) (center.getLayout());
				cl.show(center, nextPanel);

				// Print Info
				if (panelName.equals("familiar")) {
					System.out.println("Data Table Familiarity: "
							+ tableFamiliar);
					System.out.println("SCP Familiarity: " + scpFamiliar);
					System.out.println("PCP Familiarity: " + pcpFamiliar);
				}
			}
		});
		nextButtonConstraints.gridx = 4;
	    nextButtonConstraints.gridy = 7;
	    scale.add(scaleNext, nextButtonConstraints);
	    
	    JLabel blankLabelRow7 = new JLabel("");
	    formHeaderConstraints.gridx = 0;
	    formHeaderConstraints.gridy = 7;
	    formHeaderConstraints.insets = new Insets(110, 0, 0, 0);
	    scale.add(blankLabelRow7, formHeaderConstraints);

		// Data Table
		JLabel lblDataTable = new JLabel("Data Table");
		lblDataTable.setHorizontalAlignment(SwingConstants.CENTER);
		lblDataTable.setFont(this.headerFont);
	    formHeaderConstraints.gridx = 0;
	    formHeaderConstraints.gridy = 1;
	    formHeaderConstraints.insets = new Insets(0, 0, 10, 0);
	    scale.add(lblDataTable, formHeaderConstraints);

		JRadioButton table1 = new JRadioButton("Not At All " + scaleLabel);
		table1.setHorizontalAlignment(SwingConstants.CENTER);
		table1.setHorizontalTextPosition(SwingConstants.CENTER);
		table1.setVerticalTextPosition(JRadioButton.BOTTOM);
		tableScale.add(table1);
	    table1.setFont(elementFont);
	    formElementConstraints.gridx = 0;
	    formElementConstraints.gridy = 2;
	    scale.add(table1, formElementConstraints);

		JRadioButton table2 = new JRadioButton("Slightly " + scaleLabel);
		table2.setHorizontalAlignment(SwingConstants.CENTER);
		table2.setHorizontalTextPosition(SwingConstants.CENTER);
		table2.setVerticalTextPosition(JRadioButton.BOTTOM);
		tableScale.add(table2);
		table2.setFont(elementFont);
	    formElementConstraints.gridx = 1;
	    formElementConstraints.gridy = 2;
	    scale.add(table2, formElementConstraints);

		JRadioButton table3 = new JRadioButton("Moderately " + scaleLabel);
		table3.setHorizontalAlignment(SwingConstants.CENTER);
		table3.setHorizontalTextPosition(SwingConstants.CENTER);
		table3.setVerticalTextPosition(JRadioButton.BOTTOM);
		tableScale.add(table3);
		table3.setFont(elementFont);
	    formElementConstraints.gridx = 2;
	    formElementConstraints.gridy = 2;
	    scale.add(table3, formElementConstraints);

		JRadioButton table4 = new JRadioButton("Very " + scaleLabel);
		table4.setHorizontalAlignment(SwingConstants.CENTER);
		table4.setHorizontalTextPosition(SwingConstants.CENTER);
		table4.setVerticalTextPosition(JRadioButton.BOTTOM);
		tableScale.add(table4);
		table4.setFont(elementFont);
	    formElementConstraints.gridx = 3;
	    formElementConstraints.gridy = 2;
	    scale.add(table4, formElementConstraints);

		JRadioButton table5 = new JRadioButton("Extremely " + scaleLabel);
		table5.setHorizontalAlignment(SwingConstants.CENTER);
		table5.setHorizontalTextPosition(SwingConstants.CENTER);
		table5.setVerticalTextPosition(JRadioButton.BOTTOM);
		tableScale.add(table5);
		table5.setFont(elementFont);
	    formElementConstraints.gridx = 4;
	    formElementConstraints.gridy = 2;
	    scale.add(table5, formElementConstraints);

		// SCP
		JLabel lblScatterPlots = new JLabel("Scatter Plots");
		lblScatterPlots.setHorizontalAlignment(SwingConstants.CENTER);
		lblScatterPlots.setFont(headerFont);
	    formHeaderConstraints.gridx = 0;
	    formHeaderConstraints.gridy = 3;
	    scale.add(lblScatterPlots, formHeaderConstraints);

		JRadioButton scp1 = new JRadioButton("Not At All " + scaleLabel);
		scpScale.add(scp1);
		scp1.setHorizontalAlignment(SwingConstants.CENTER);
		scp1.setHorizontalTextPosition(SwingConstants.CENTER);
		scp1.setVerticalTextPosition(JRadioButton.BOTTOM);
		scp1.setFont(elementFont);
	    formElementConstraints.gridx = 0;
	    formElementConstraints.gridy = 4;
	    scale.add(scp1, formElementConstraints);

		JRadioButton scp2 = new JRadioButton("Slightly " + scaleLabel);
		scpScale.add(scp2);
		scp2.setHorizontalAlignment(SwingConstants.CENTER);
		scp2.setHorizontalTextPosition(SwingConstants.CENTER);
		scp2.setVerticalTextPosition(JRadioButton.BOTTOM);
		scp2.setFont(elementFont);
	    formElementConstraints.gridx = 1;
	    formElementConstraints.gridy = 4;
	    scale.add(scp2, formElementConstraints);

		JRadioButton scp3 = new JRadioButton("Moderately " + scaleLabel);
		scpScale.add(scp3);
		scp3.setHorizontalAlignment(SwingConstants.CENTER);
		scp3.setHorizontalTextPosition(SwingConstants.CENTER);
		scp3.setVerticalTextPosition(JRadioButton.BOTTOM);
		scp3.setFont(elementFont);
	    formElementConstraints.gridx = 2;
	    formElementConstraints.gridy = 4;
	    scale.add(scp3, formElementConstraints);

		JRadioButton scp4 = new JRadioButton("Very " + scaleLabel);
		scpScale.add(scp4);
		scp4.setHorizontalAlignment(SwingConstants.CENTER);
		scp4.setHorizontalTextPosition(SwingConstants.CENTER);
		scp4.setVerticalTextPosition(JRadioButton.BOTTOM);
		scp4.setFont(elementFont);
	    formElementConstraints.gridx = 3;
	    formElementConstraints.gridy = 4;
	    scale.add(scp4, formElementConstraints);

		JRadioButton scp5 = new JRadioButton("Extremely " + scaleLabel);
		scpScale.add(scp5);
		scp5.setHorizontalAlignment(SwingConstants.CENTER);
		scp5.setHorizontalTextPosition(SwingConstants.CENTER);
		scp5.setVerticalTextPosition(JRadioButton.BOTTOM);
		scp5.setFont(elementFont);
	    formElementConstraints.gridx = 4;
	    formElementConstraints.gridy = 4;
	    scale.add(scp5, formElementConstraints);

		// PCP
		JLabel lblParallelCoordinatesPlots = new JLabel(
				"Parallel Coordinates Plots");
		lblParallelCoordinatesPlots
				.setHorizontalAlignment(SwingConstants.CENTER);
		lblParallelCoordinatesPlots
	      .setFont(headerFont);
	    formHeaderConstraints.gridx = 0;
	    formHeaderConstraints.gridy = 5;
	    scale.add(lblParallelCoordinatesPlots, formHeaderConstraints);

		JRadioButton pcp1 = new JRadioButton("Not At All " + scaleLabel);
		pcpScale.add(pcp1);
		pcp1.setHorizontalAlignment(SwingConstants.CENTER);
		pcp1.setHorizontalTextPosition(SwingConstants.CENTER);
		pcp1.setVerticalTextPosition(JRadioButton.BOTTOM);
		pcp1.setFont(elementFont);
	    formElementConstraints.gridx = 0;
	    formElementConstraints.gridy = 6;
	    scale.add(pcp1, formElementConstraints);

		JRadioButton pcp2 = new JRadioButton("Slightly " + scaleLabel);
		pcpScale.add(pcp2);
		pcp2.setHorizontalAlignment(SwingConstants.CENTER);
		pcp2.setHorizontalTextPosition(SwingConstants.CENTER);
		pcp2.setVerticalTextPosition(JRadioButton.BOTTOM);
		pcp2.setFont(elementFont);
	    formElementConstraints.gridx = 1;
	    formElementConstraints.gridy = 6;
	    scale.add(pcp2, formElementConstraints);

		JRadioButton pcp3 = new JRadioButton("Moderately " + scaleLabel);
		pcpScale.add(pcp3);
		pcp3.setHorizontalAlignment(SwingConstants.CENTER);
		pcp3.setHorizontalTextPosition(SwingConstants.CENTER);
		pcp3.setVerticalTextPosition(JRadioButton.BOTTOM);
		pcp3.setFont(elementFont);
	    formElementConstraints.gridx = 2;
	    formElementConstraints.gridy = 6;
	    scale.add(pcp3, formElementConstraints);

		JRadioButton pcp4 = new JRadioButton("Very " + scaleLabel);
		pcpScale.add(pcp4);
		pcp4.setHorizontalAlignment(SwingConstants.CENTER);
		pcp4.setHorizontalTextPosition(SwingConstants.CENTER);
		pcp4.setVerticalTextPosition(JRadioButton.BOTTOM);
		pcp4.setFont(elementFont);
	    formElementConstraints.gridx = 3;
	    formElementConstraints.gridy = 6;
	    scale.add(pcp4, formElementConstraints);

		JRadioButton pcp5 = new JRadioButton("Extremely " + scaleLabel);
		pcpScale.add(pcp5);
		pcp5.setHorizontalAlignment(SwingConstants.CENTER);
		pcp5.setHorizontalTextPosition(SwingConstants.CENTER);
		pcp5.setVerticalTextPosition(JRadioButton.BOTTOM);
		pcp5.setFont(elementFont);
	    formElementConstraints.gridx = 4;
	    formElementConstraints.gridy = 6;
	    scale.add(pcp5, formElementConstraints);

		// Button Listener

		// table
		table1.addActionListener(new TableScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));
		table2.addActionListener(new TableScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));
		table3.addActionListener(new TableScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));
		table4.addActionListener(new TableScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));
		table5.addActionListener(new TableScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));

		// scp
		scp1.addActionListener(new SCPScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));
		scp2.addActionListener(new SCPScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));
		scp3.addActionListener(new SCPScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));
		scp4.addActionListener(new SCPScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));
		scp5.addActionListener(new SCPScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));

		// pcp
		pcp1.addActionListener(new PCPScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));
		pcp2.addActionListener(new PCPScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));
		pcp3.addActionListener(new PCPScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));
		pcp4.addActionListener(new PCPScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));
		pcp5.addActionListener(new PCPScaleListener(panelName, tableScale,
				scpScale, pcpScale, scaleNext));

	}

	private boolean selectAllInfo() {
		boolean typeID = !id.equals("");
		boolean selectGender = !gender.equals("");
		boolean selectAge = !age.equals("");
		boolean selectOccupation = !occupation.equals("");
		boolean selectColorBlind = !colorBlind.equals("");
		boolean selectColorBlindType = !colorBlindType.equals("-");
		return (typeID && selectGender && selectAge && selectOccupation
				&& selectColorBlind && (colorBlind.equals("No") || selectColorBlindType));
	}

	private boolean selectAll(ButtonGroup tableScale, ButtonGroup scpScale,
			ButtonGroup pcpScale) {
		boolean selectTableScale = tableScale.getSelection() != null;
		boolean selectSCPScale = scpScale.getSelection() != null;
		boolean selectPCPScale = pcpScale.getSelection() != null;
		return (selectTableScale && selectSCPScale && selectPCPScale);
	}

	private void addQuestionSection(String panelName, String nextPanel,
			String sectionHeader, int task, String pic1, String pic2,
			String pic3, String trialType) {
		addSection(panelName, panelName + "_1", sectionHeader,
				getSectionContent(task, trialType));
		String[] qa1, qa2, qa3;
		qa1 = QA.get(pic1);
		qa2 = QA.get(pic2);
		qa3 = QA.get(pic3);
		addQuestion(panelName + "_1", panelName + "_2", getPicDirectory(pic1),
				getTaskTitle(task) + qa1[0], qa1[1], qa1[3], qa1[5], qa1[7],
				trialType);
		addQuestion(panelName + "_2", panelName + "_3", getPicDirectory(pic2),
				getTaskTitle(task) + qa2[0], qa2[1], qa2[3], qa2[5], qa2[7],
				trialType);
		addQuestion(panelName + "_3", nextPanel, getPicDirectory(pic3),
				getTaskTitle(task) + qa3[0], qa3[1], qa3[3], qa3[5], qa3[7],
				trialType);
	}

	private String getSectionContent(int task, String trialType) {
		String breakText = "";
		String training = "";
		if (trialType.equalsIgnoreCase("testing")) {
			breakText = "You are advised to have a break.<br><br><br><br><br>";
		}else {
			training = "training ";
 		}
		if (task == 0) {
			return "In the following training trials, feedback will be given.";
		} else if (task == 1) {
			return "<html><center>"
					+ breakText
					+ "The following three " + training + "trials are for value retrieval tasks."
					+ "</center> </html>";
		} else if (task == 2) {
			return "<html><center>" + breakText
					+ "The following three " + training + "trials are for clustering tasks."
					+ "</center> </html>";
		} else if (task == 3) {
			return "<html><center>"
					+ breakText
					+ "The following three " + training + "trials are for outlier detection tasks."
					+ "</center> </html>";
		} else if (task == 4) {
			return "<html><center>"
					+ breakText
					+ "The following three " + training + "trials are for change detection tasks."
					+ "</center> </html>";
		} else if (task == 5) {
			return "<html><center>Now the study will begin.<br><br>"
					+ "Trials for the four different tasks will appear in random order.<br><br>"
					+ "No feedback will be given." + "</center> </html>";
		}
		return "";
	}

	private String getTaskTitle(int task) {
		if (task == 1) {
			return "Values: ";
		} else if (task == 2) {
			return "Clusters: ";
		} else if (task == 3) {
			return "Outliers: ";
		} else if (task == 4) {
			return "Changes: ";
		}
		return "";
	}

	private void addSection(String panelName, final String nextPanel,
			String header, String content) {
		JPanel section = new JPanel();
		center.add(section, panelName);
		section.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = 1;
	    constraints.anchor = 19;
	    constraints.weighty = 0.1D;
	    constraints.insets = new Insets(0, 0, 20, 0);

		JLabel lblSection_1 = new JLabel(header);
		lblSection_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblSection_1.setFont(sectionHeaderFont);
	    constraints.gridx = 0;
	    constraints.gridy = 0;
	    constraints.gridwidth = 2;
	    section.add(lblSection_1, constraints);

		JLabel lblTheFollowingThree = new JLabel(content);
		lblTheFollowingThree.setHorizontalAlignment(SwingConstants.CENTER);
		lblTheFollowingThree.setFont(sectionContentFont);
	    constraints.gridx = 0;
	    constraints.gridy = 1;
	    constraints.gridwidth = 2;
	    section.add(lblTheFollowingThree, constraints);

		// Start Button
		JButton btnNext = new JButton("Start");
		btnNext.setFont(nextButtonFont);
	    btnNext.setBounds(530, 523, 120, 40);
		btnNext.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				CardLayout cl = (CardLayout) (center.getLayout());
				cl.show(center, nextPanel);

				// Record time
				recordTime("start");
			}
		});
		constraints.gridx = 1;
	    constraints.gridy = 2;
	    constraints.gridwidth = 1;
	    constraints.ipadx = 100;
	    constraints.ipady = 20;
	    constraints.insets = new Insets(30, 0, 0, 0);
	    constraints.fill = 0;
	    section.add(btnNext, constraints);
	}

	private void recordTime(String recordType) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.S");

		if (recordType.equalsIgnoreCase("start")) {
			startTimeDT = new Timestamp(date.getTime());
			startTimeT = sdf.format(date);
			startTime = System.currentTimeMillis();
		}

		else if (recordType.equalsIgnoreCase("ready")) {
			readyTimeDT = new Timestamp(date.getTime());
			readyTimeT = sdf.format(date);
			readyTime = System.currentTimeMillis();
			readingTime = readyTime - startTime;
		}

		else if (recordType.equalsIgnoreCase("answer")) {
			answerTimeDT = new Timestamp(date.getTime());
			answerTimeT = sdf.format(date);
			answerTime = System.currentTimeMillis();
			responseTime = answerTime - readyTime;
		}

		else if (recordType.equalsIgnoreCase("clickNext")) {
			clickNextTimeDT = new Timestamp(date.getTime());
			clickNextTimeT = sdf.format(date);
			clickNextTime = System.currentTimeMillis();
		}
	}

	private void addQuestion(String panelName, final String nextPanel,
			String pictureFile, String question, String choice1,
			String choice2, String choice3, String choice4,
			final String trialType) {
		int firstIndex = pictureFile.lastIndexOf("/") + 1;
		int lastIndex = pictureFile.indexOf(".png");
		final String stimuliName = pictureFile.substring(firstIndex, lastIndex);

		JPanel trial = new JPanel();
		center.add(trial, panelName);
		trial.setLayout(new GridBagLayout());
		
		GridBagConstraints pictureConstraints = new GridBagConstraints();
	    pictureConstraints.fill = 1;
	    pictureConstraints.gridwidth = 4;
	    pictureConstraints.weighty = 0.1D;
	    pictureConstraints.insets = new Insets(0, 0, 10, 0);
	    GridBagConstraints questionConstraints = new GridBagConstraints();
	    questionConstraints.gridwidth = 4;
	    questionConstraints.weighty = 0.1D;
	    GridBagConstraints choiceConstraints = new GridBagConstraints();
	    choiceConstraints.fill = 1;
	    choiceConstraints.weighty = 0.1D;
	    choiceConstraints.insets = new Insets(0, 30, 0, 30);
	    GridBagConstraints feedbackConstraints = new GridBagConstraints();
	    feedbackConstraints.weighty = 0.05D;
	    feedbackConstraints.insets = new Insets(0, 20, 100, 20);
	    GridBagConstraints nextConstraints = new GridBagConstraints();
	    nextConstraints.weighty = 0.05D;
	    nextConstraints.ipadx = 90;
	    nextConstraints.ipady = 20;
	    nextConstraints.insets = new Insets(30, 20, 0, 20);
	    nextConstraints.anchor = 26;
	    GridBagConstraints blankConstraints = new GridBagConstraints();
	    blankConstraints.weighty = 0.1D;
	    blankConstraints.gridheight = 2;
	    blankConstraints.insets = new Insets(0, 200, 0, 200);

		JLabel lblTimeOut = new JLabel(
				"<html><center>The time for this trial has expired.<br><br>"
						+ "Please click next to go to the next trial.</center></html>");
		lblTimeOut.setFont(new Font("Calibri", Font.PLAIN, 40));
		lblTimeOut.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeOut.setVisible(false);
		pictureConstraints.gridx = 0;
	    pictureConstraints.gridy = 0;
	    trial.add(lblTimeOut, pictureConstraints);

		java.net.URL imageURL = getClass().getResource(pictureFile);
		Image scaledImage = null;
	    try {
	      scaledImage = ImageIO.read(imageURL);
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    int newHeight = 450;
	    scaledImage = scaledImage.getScaledInstance(-1, newHeight, 4);
	    ImageIcon image = new ImageIcon(scaledImage);
	    JLabel lblPicture = new JLabel("", image, JLabel.CENTER);
	    lblPicture.setVisible(false);
	    pictureConstraints.gridx = 0;
	    pictureConstraints.gridy = 0;
	    trial.add(lblPicture, pictureConstraints);

		JLabel lblQuestion = new JLabel(question);
		lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuestion.setFont(sectionContentFont);
	    questionConstraints.gridx = 0;
	    questionConstraints.gridy = 1;
	    trial.add(lblQuestion, questionConstraints);

		ButtonGroup choice = new ButtonGroup();

		JButton button1 = createButton(stimuliName, choice1);
		choice.add(button1);
		choiceConstraints.gridx = 0;
	    choiceConstraints.gridy = 2;
	    trial.add(button1, choiceConstraints);

		JButton button2 = createButton(stimuliName, choice2);
		choice.add(button2);
	    choiceConstraints.gridx = 1;
	    choiceConstraints.gridy = 2;
	    trial.add(button2, choiceConstraints);

		JButton button3 = createButton(stimuliName, choice3);
		choice.add(button3);
	    choiceConstraints.gridx = 2;
	    choiceConstraints.gridy = 2;
	    trial.add(button3, choiceConstraints);

		JButton button4 = createButton(stimuliName, choice4);
		choice.add(button4);
	    choiceConstraints.gridx = 3;
	    choiceConstraints.gridy = 2;
	    trial.add(button4, choiceConstraints);

		JButton btnNext = new JButton("Next");
		btnNext.setVisible(false);
		btnNext.setFont(nextButtonFont);
		btnNext.addMouseListener(new QuestionNextListener(trialType,
				stimuliName, lblPicture, nextPanel));
		nextConstraints.gridx = 3;
	    nextConstraints.gridy = 4;
	    trial.add(btnNext, nextConstraints);

		JButton btnImReady = new JButton("Show Picture");
		btnImReady.setFont(sectionContentFont);
		btnImReady.addMouseListener(new ReadyButtonListener(lblPicture,
				lblQuestion, button1, button2, button3, button4, btnNext,
				btnImReady, lblTimeOut));
		pictureConstraints.gridx = 0;
	    pictureConstraints.gridy = 0;
	    pictureConstraints.ipadx = 30;
	    pictureConstraints.ipady = 30;
	    pictureConstraints.insets = new Insets(188, 300, 190, 300);
	    pictureConstraints.fill = 0;
	    trial.add(btnImReady, pictureConstraints);

		// Feedback label

		JLabel feedback1 = new JLabel("Correct Answer");
		feedback1.setHorizontalAlignment(SwingConstants.CENTER);
		feedback1.setForeground(Color.BLUE);
	    feedback1.setFont(elementFont);
	    feedback1.setVisible(false);
	    feedbackConstraints.gridx = 0;
	    feedbackConstraints.gridy = 4;
	    trial.add(feedback1, feedbackConstraints);

		JLabel feedback2 = new JLabel("Correct Answer");
		feedback2.setHorizontalAlignment(SwingConstants.CENTER);
		feedback2.setForeground(Color.BLUE);
	    feedback2.setFont(elementFont);
	    feedback2.setVisible(false);
	    feedbackConstraints.gridx = 1;
	    feedbackConstraints.gridy = 4;
	    trial.add(feedback2, feedbackConstraints);

		JLabel feedback3 = new JLabel("Correct Answer");
		feedback3.setHorizontalAlignment(SwingConstants.CENTER);
		feedback3.setForeground(Color.BLUE);
	    feedback3.setFont(elementFont);
	    feedback3.setVisible(false);
	    feedbackConstraints.gridx = 2;
	    feedbackConstraints.gridy = 4;
	    trial.add(feedback3, feedbackConstraints);

		JLabel feedback4 = new JLabel("Correct Answer");
		feedback4.setHorizontalAlignment(SwingConstants.CENTER);
		feedback4.setForeground(Color.BLUE);
	    feedback4.setFont(elementFont);
	    feedback4.setVisible(false);
	    feedbackConstraints.gridx = 3;
	    feedbackConstraints.gridy = 4;
	    trial.add(feedback4, feedbackConstraints);
	    
	    blankConstraints.gridheight = 2;
	    JLabel blankLabel1 = new JLabel("");
	    blankConstraints.gridx = 0;
	    blankConstraints.gridy = 3;
	    trial.add(blankLabel1, blankConstraints);

	    JLabel blankLabel2 = new JLabel("");
	    blankConstraints.gridx = 1;
	    blankConstraints.gridy = 3;
	    trial.add(blankLabel2, blankConstraints);

	    JLabel blankLabel3 = new JLabel("");
	    blankConstraints.gridx = 2;
	    blankConstraints.gridy = 3;
	    trial.add(blankLabel3, blankConstraints);

	    JLabel blankLabel4 = new JLabel("");
	    blankConstraints.gridx = 3;
	    blankConstraints.gridy = 3;
	    blankConstraints.insets = new Insets(0, 200, 130, 200);
	    trial.add(blankLabel4, blankConstraints);

	    JLabel blankLabel5 = new JLabel("");
	    blankConstraints.gridx = 3;
	    blankConstraints.gridy = 2;
	    blankConstraints.insets = new Insets(50, 0, 50, 0);
	    trial.add(blankLabel5, blankConstraints);

		// Add choice button listener
		button1.addMouseListener(new ChoiceListener(btnImReady, button1,
				button2, button3, button4, btnNext, trialType, stimuliName,
				feedback1, feedback2, feedback3, feedback4));
		button2.addMouseListener(new ChoiceListener(btnImReady, button1,
				button2, button3, button4, btnNext, trialType, stimuliName,
				feedback1, feedback2, feedback3, feedback4));
		button3.addMouseListener(new ChoiceListener(btnImReady, button1,
				button2, button3, button4, btnNext, trialType, stimuliName,
				feedback1, feedback2, feedback3, feedback4));
		button4.addMouseListener(new ChoiceListener(btnImReady, button1,
				button2, button3, button4, btnNext, trialType, stimuliName,
				feedback1, feedback2, feedback3, feedback4));

	}

	private void startTiming(JLabel lblPicture, JLabel lblQuestion,
			JButton button1, JButton button2, JButton button3, JButton button4,
			JButton btnNext, JLabel lblTimeOut) {
		// Timer (execute every 1 sec)
		progressBar.setValue(0);
		progressBar.setVisible(true);
		lblTotalTimeLeft.setVisible(true);
		time.setText(maxTime + "");
		time.setVisible(true);
		timer = new Timer(1000, new TimingListener(lblPicture, lblQuestion,
				button1, button2, button3, button4, btnNext, lblTimeOut));
		timer.start();
	}

	private JButton createButton(String stimuliName, String choice) {
		int task, representation;
		int length;
		if (stimuliName.length() == 6) {
			task = Integer.parseInt(stimuliName.substring(1, 2));
			representation = Integer.parseInt(stimuliName.substring(5));
		} else {
			length = stimuliName.length();
			task = Integer.parseInt(stimuliName.substring(length - 3,
					length - 2));
			representation = Integer
					.parseInt(stimuliName.substring(length - 1));
		}

		String[] choices = choice.split(",");
		Color[] color;
		JButton button;

		// Task 1
		if (task == 1) {
			button = new JButton(choice);
			button.setFont(task1ButtonFont);
		}

		// Task 2,3,4
		// representation: 1 = table, 2 = SCP, 3 = PCP
		else {
			if (representation == 1) {
				color = getColor(choices, true);
				button = new JButton(choice, new BoxIcon(color));
			} else if (representation == 2) {
				color = getColor(choices, false);
				button = new JButton(choice, new ShapeIcon(color, choices));
			} else {
				color = getColor(choices, false);
				button = new JButton(choice, new LineIcon(color));
				button.setIconTextGap(25);
				if (color.length == 4) {
					button.setVerticalAlignment(SwingConstants.TOP);
					button.setIconTextGap(17);
				}
			}
			button.setFont(choiceButtonFont);
		}

		button.setVerticalTextPosition(SwingConstants.TOP);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setEnabled(false);
		return button;
	}

	private Color[] getColor(String[] choices, boolean light) {
		Color[] color = new Color[choices.length];
		if (light) {
			for (int i = 0; i < choices.length; i++) {
				if (choices[i].trim().equals("1")) {
					// Dark Green
					color[i] = new Color(179, 226, 205);
				} else if (choices[i].trim().equals("2")) {
					// Orange
					color[i] = new Color(253, 205, 172);
				} else if (choices[i].trim().equals("3")) {
					// Blue
					color[i] = new Color(203, 213, 232);
				} else if (choices[i].trim().equals("4")) {
					// Pink
					color[i] = new Color(244, 202, 228);
				} else if (choices[i].trim().equals("5")) {
					// Light Green
					color[i] = new Color(230, 245, 201);
				} else if (choices[i].trim().equals("6")) {
					// Yellow
					color[i] = new Color(255, 242, 174);
				} else if (choices[i].trim().equals("7")) {
					// Brown
					color[i] = new Color(241, 226, 204);
				} else if (choices[i].trim().equals("8")) {
					// Grey
					color[i] = new Color(204, 204, 204);
				}
			}
		} else {
			for (int i = 0; i < choices.length; i++) {
				if (choices[i].trim().equals("1")) {
					// Dark Green
					color[i] = new Color(0, 180, 150);
				} else if (choices[i].trim().equals("2")) {
					// Red
					color[i] = new Color(255, 1, 1);
				} else if (choices[i].trim().equals("3")) {
					// Purple
					color[i] = new Color(157, 157, 243);
				} else if (choices[i].trim().equals("4")) {
					// Pink
					color[i] = new Color(255, 105, 230);
				} else if (choices[i].trim().equals("5")) {
					// Light Green
					color[i] = new Color(197, 228, 152);
				} else if (choices[i].trim().equals("6")) {
					// Yellow
					color[i] = new Color(240, 229, 0);
				} else if (choices[i].trim().equals("7")) {
					// Brown
					color[i] = new Color(228, 166, 132);
				} else if (choices[i].trim().equals("8")) {
					// Grey
					color[i] = new Color(128, 128, 128);
				}
			}
		}
		return color;
	}

	private void recordValues() {
		// Get current timestamp
		Date date = new Date();
		Timestamp time = new Timestamp(date.getTime());

		// Create content
		StringBuilder s = new StringBuilder();
		s.append("ID,Gender,Age Group,Occupation,Color Blindness,Color Blindness Type,"
				+ "Table Familiarity,Table Familiarity Rating Number,"
				+ "SCP Familiarity,SCP Familiarity Rating Number,"
				+ "PCP Familiarity,PCP Familiarity Rating Number,"
				+ "Trial Type,Shown Index,Stimuli Name,Task,Difficulty,Visualization Technique,"
				+ "Choice1,Choice1 Type,Choice2,Choice2 Type,Choice3,Choice3 Type,Choice4,Choice4 Type,"
				+ "Correct Answer,Correct Position,Selected Answer,Selected Position,Selected Choice Type,Answer Correctly,"
				+ "Start Time (Date and Time),Start Time (Time),Start Time (ms),"
				+ "Ready Time (Date and Time),Ready Time (Time),Ready Time (ms),"
				+ "Answer Time (Date and Time),Answer Time (Time),Answer Time (ms),"
				+ "Click Next Time (Date and Time),Click Next Time (Time),Click Next Time (ms),"
				+ "Reading Time (ms),Response Time (ms),"
				+ "Value Retrieval Task: Table Rating,Value Retrieval Task: Table Rating Number,"
				+ "Value Retrieval Task: SCP Rating,Value Retrieval Task: SCP Rating Number,"
				+ "Value Retrieval Task: PCP Rating,Value Retrieval Task: PCP Rating Number,"
				+ "Clustering Task: Table Rating,Clustering Task: Table Rating Number,"
				+ "Clustering Task: SCP Rating,Clustering Task: SCP Rating Number,"
				+ "Clustering Task: PCP Rating,Clustering Task: PCP Rating Number,"
				+ "Outlier Detection Task: Table Rating,Outlier Detection Task: Table Rating Number,"
				+ "Outlier Detection Task: SCP Rating,Outlier Detection Task: SCP Rating Number,"
				+ "Outlier Detection Task: PCP Rating,Outlier Detection Task: PCP Rating Number,"
				+ "Change Detection Task: Table Rating,Change Detection Task: Table Rating Number,"
				+ "Change Detection Task: SCP Rating,Change Detection Task: SCP Rating Number,"
				+ "Change Detection Task: PCP Rating,Change Detection Task: PCP Rating Number\n");

		// Training
		s.append(recordResponse("Training"));
		// Testing
		s.append(recordResponse("Testing"));
		writeFile(id + "_" + time + ".csv", s.toString().trim());
	}

	private String recordResponse(String trialType) {
		LinkedList<String[]> response = (trialType.equalsIgnoreCase("training")) ? trainingResponse
				: testingResponse;
		String s = "";
		String[] values;
		String[] qa;
		String stimuli;
		for (int i = 0; i < response.size(); i++) {

			// Record personal information
			s += (id + "," + gender + "," + age + "," + occupation + ","
					+ colorBlind + "," + colorBlindType + "," + tableFamiliar
					+ "," + getRatingNumber(tableFamiliar) + "," + scpFamiliar
					+ "," + getRatingNumber(scpFamiliar) + "," + pcpFamiliar
					+ "," + getRatingNumber(pcpFamiliar));

			// Record trial
			values = response.get(i);
			stimuli = values[0];
			s += ("," + trialType + "," + i + "," + stimuli);
			s += ("," + getTask(stimuli) + "," + getDifficulty(stimuli) + "," + getRepresentation(stimuli));

			// Record choices
			qa = QA.get(stimuli);
			for (int j = 1; j <= 8; j++) {
				s += (",\"" + qa[j] + "\"");
			}

			// Record trial response
			for (int j = 1; j < 21; j++) {
				s += (",\"" + values[j] + "\"");
			}

			// Record task feedback
			String[] f;
			for (int j = 0; j < feedback.length; j++) {
				f = feedback[j];
				for (int k = 0; k < f.length; k++) {
					s += ("," + f[k] + "," + getRatingNumber(f[k]));
				}
			}
			s += ("\n");
		}
		return s;
	}

	private String getTask(String stimuliName) {
		String task;
		int length;
		if (stimuliName.length() == 6) {
			task = stimuliName.substring(1, 2);
		} else {
			length = stimuliName.length();
			task = stimuliName.substring(length - 3, length - 2);
		}
		if (task.equals("1")) {
			return "Value Retrieval Task";
		} else if (task.equals("2")) {
			return "Clustering Task";
		} else if (task.equals("3")) {
			return "Outlier Detection Task";
		} else if (task.equals("4")) {
			return "Change Detection Task";
		}
		return task;
	}

	private String getDifficulty(String stimuliName) {
		String difficulty;
		if (stimuliName.length() == 6) {
			difficulty = stimuliName.substring(3, 4);
		} else {
			return "Easy";
		}
		if (difficulty.equals("1") || difficulty.equals("2")) {
			return "Easy";
		} else if (difficulty.equals("3") || difficulty.equals("4")) {
			return "Medium";
		} else if (difficulty.equals("5") || difficulty.equals("6")) {
			return "Hard";
		}
		return difficulty;
	}

	private String getRepresentation(String stimuliName) {
		String representation;
		int length;
		if (stimuliName.length() == 6) {
			representation = stimuliName.substring(5);
		} else {
			length = stimuliName.length();
			representation = stimuliName.substring(length - 1);
		}
		if (representation.equals("1")) {
			return "Data Table";
		} else if (representation.equals("2")) {
			return "Scatter Plots";
		} else if (representation.equals("3")) {
			return "Parallel Coordinates Plots";
		}
		return representation;
	}

	private String getRatingNumber(String feedback) {
		int index = feedback.lastIndexOf(" ");
		String rating = feedback.substring(0, index);
		String ratingNumber = "";
		if (rating.equalsIgnoreCase("Not At All")) {
			ratingNumber = "1";
		} else if (rating.equalsIgnoreCase("Slightly")) {
			ratingNumber = "2";
		} else if (rating.equalsIgnoreCase("Moderately")) {
			ratingNumber = "3";
		} else if (rating.equalsIgnoreCase("Very")) {
			ratingNumber = "4";
		} else if (rating.equalsIgnoreCase("Extremely")) {
			ratingNumber = "5";
		}
		return ratingNumber;
	}

	private void addThankYouPage() {
		JPanel thankYou = new JPanel();
		center.add(thankYou, "thankYou");
		thankYou.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = 1;
	    constraints.anchor = 19;
	    constraints.weighty = 0.1D;
	    constraints.insets = new Insets(0, 0, 20, 0);

		JLabel lblTheFollowingThree = new JLabel(
				"Thank you for your participation.");
		lblTheFollowingThree.setHorizontalAlignment(SwingConstants.CENTER);
		lblTheFollowingThree.setFont(sectionHeaderFont);
	    constraints.gridx = 0;
	    constraints.gridy = 0;
	    constraints.gridwidth = 2;
	    thankYou.add(lblTheFollowingThree, constraints);

		// Close button
		JButton btnClose = new JButton("Close");
		btnClose.setFont(nextButtonFont);
		btnClose.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				System.exit(0);
			}
		});
		constraints.gridx = 1;
	    constraints.gridy = 2;
	    constraints.gridwidth = 1;
	    constraints.ipadx = 100;
	    constraints.ipady = 20;
	    constraints.insets = new Insets(30, 0, 0, 0);
	    constraints.fill = 0;
	    thankYou.add(btnClose, constraints);
	}

	// Other methods
	private String getPicDirectory(String name) {
		if (name.length() == 6) {
			String task = name.substring(0, 2);
			return getPicDirectory(task, name);
		} else {
			String folder = name.substring(0, name.length() - 3);
			return getPicDirectory(folder, name);
		}
	}

	private String getPicDirectory(String folder, String name) {
		return picDirectory + folder + "/" + name + ".png";
	}

	private void readFile(String filename) {
		int index = filename.indexOf(".csv");
		String name = filename.substring(0, index);
		readFile(name, filename);
	}

	private void readFile(String folder, String filename) {
		System.out.println("Reading a file: " + filename);
		try {
			InputStream input = getClass().getResourceAsStream(
					readDirectory + folder + "/" + filename);
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			// BufferedReader br = new BufferedReader(new
			// FileReader(readDirectory
			// + folder + "/" + filename));

			try {
				String line;
				int index1;
				String key;
				String[] value;
				// skip header
				br.readLine();
				while ((line = br.readLine()) != null) {
					index1 = line.indexOf(",");
					key = line.substring(0, index1);
					value = line.substring(index1 + 1).split(
							",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					for (int i = 0; i < value.length; i++) {
						// Remove double quote
						value[i] = value[i].replaceAll("\"", "");
					}
					QA.put(key, value);
				}
			} finally {
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFile(String filename, String content) {
		System.out.println("Writing a file: " + filename);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					writeDirectory + filename));
			try {
				bw.write(content);
			} finally {
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Info listener
	public class GenderListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton radioButton = (JRadioButton) e.getSource();
			if (radioButton.isSelected()) {
				gender = radioButton.getText();
				if (selectAllInfo()) {
					infoNext.setVisible(true);
				} else {
					infoNext.setVisible(false);
				}
			}
		}
	}

	public class AgeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton radioButton = (JRadioButton) e.getSource();
			if (radioButton.isSelected()) {
				age = radioButton.getText();
				if (selectAllInfo()) {
					infoNext.setVisible(true);
				} else {
					infoNext.setVisible(false);
				}
			}
		}
	}

	public class OccupationListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton radioButton = (JRadioButton) e.getSource();
			if (radioButton.isSelected()) {
				occupation = radioButton.getText();
				if (selectAllInfo()) {
					infoNext.setVisible(true);
				} else {
					infoNext.setVisible(false);
				}
			}
		}
	}

	public class ColorBlindTypeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton radioButton = (JRadioButton) e.getSource();
			if (radioButton.isSelected()) {
				colorBlindType = radioButton.getText();
				if (selectAllInfo()) {
					infoNext.setVisible(true);
				} else {
					infoNext.setVisible(false);
				}
			}
		}
	}

	// Scale Listener

	public class TableScaleListener implements ActionListener {

		String panelName;
		ButtonGroup tableScale, scpScale, pcpScale;
		JButton next;

		public TableScaleListener(String panelName, ButtonGroup tableScale,
				ButtonGroup scpScale, ButtonGroup pcpScale, JButton next) {
			this.panelName = panelName;
			this.tableScale = tableScale;
			this.scpScale = scpScale;
			this.pcpScale = pcpScale;
			this.next = next;
		}

		public void actionPerformed(ActionEvent e) {
			JRadioButton source = (JRadioButton) e.getSource();
			if (panelName.equals("familiar")) {
				tableFamiliar = source.getText();
			} else {
				int i = Integer.parseInt(panelName.substring(8));
				feedback[i - 1][0] = source.getText();
			}
			if (selectAll(tableScale, scpScale, pcpScale)) {
				next.setVisible(true);
			}
		}
	}

	public class SCPScaleListener implements ActionListener {

		String panelName;
		ButtonGroup tableScale, scpScale, pcpScale;
		JButton next;

		public SCPScaleListener(String panelName, ButtonGroup tableScale,
				ButtonGroup scpScale, ButtonGroup pcpScale, JButton next) {
			this.panelName = panelName;
			this.tableScale = tableScale;
			this.scpScale = scpScale;
			this.pcpScale = pcpScale;
			this.next = next;
		}

		public void actionPerformed(ActionEvent e) {
			JRadioButton source = (JRadioButton) e.getSource();
			if (panelName.equals("familiar")) {
				scpFamiliar = source.getText();
			} else {
				int i = Integer.parseInt(panelName.substring(8));
				feedback[i - 1][1] = source.getText();
			}
			if (selectAll(tableScale, scpScale, pcpScale)) {
				next.setVisible(true);
			}
		}

	}

	public class PCPScaleListener implements ActionListener {

		String panelName;
		ButtonGroup tableScale, scpScale, pcpScale;
		JButton next;

		public PCPScaleListener(String panelName, ButtonGroup tableScale,
				ButtonGroup scpScale, ButtonGroup pcpScale, JButton next) {
			this.panelName = panelName;
			this.tableScale = tableScale;
			this.scpScale = scpScale;
			this.pcpScale = pcpScale;
			this.next = next;
		}

		public void actionPerformed(ActionEvent e) {
			JRadioButton source = (JRadioButton) e.getSource();
			if (panelName.equals("familiar")) {
				pcpFamiliar = source.getText();
			} else {
				int i = Integer.parseInt(panelName.substring(8));
				feedback[i - 1][2] = source.getText();
			}
			if (selectAll(tableScale, scpScale, pcpScale)) {
				next.setVisible(true);
			}
		}

	}

	// Trial Listener

	public class QuestionNextListener extends MouseAdapter {

		String trialType, stimuliName, nextPanel;
		JLabel lblPicture;

		public QuestionNextListener(String trialType, String stimuliName,
				JLabel lblPicture, String nextPanel) {
			this.trialType = trialType;
			this.stimuliName = stimuliName;
			this.nextPanel = nextPanel;
			this.lblPicture = lblPicture;
		}

		public void mousePressed(MouseEvent arg0) {
			LinkedList<String[]> response = (trialType
					.equalsIgnoreCase("training")) ? trainingResponse
					: testingResponse;

			// Record time
			recordTime("clickNext");

			// Record response
			String[] qa = QA.get(stimuliName);
			String correctAnswer = qa[9];
			String correctPosition = qa[10];
			String answerType, correct, answerTimeDTString, answerTimeString, responseTimeString;
			int answerPos;
			if (lblPicture.isVisible()) {
				answerPos = Integer.parseInt(answerPosition);
				answerType = qa[answerPos * 2];
				correct = (answer.equals(correctAnswer)) ? "yes" : "no";
				answerTimeDTString = String.valueOf(answerTimeDT);
				answerTimeString = String.valueOf(answerTime);
				responseTimeString = String.valueOf(responseTime);
			} else {
				answer = "no answer";
				answerPosition = "-";
				answerType = "-";
				correct = "-";
				answerTimeDTString = "-";
				answerTimeT = "-";
				answerTimeString = "-";
				responseTimeString = "-";
			}

			response.add(new String[] { stimuliName, correctAnswer,
					correctPosition, answer, answerPosition, answerType,
					correct, String.valueOf(startTimeDT), startTimeT,
					String.valueOf(startTime), String.valueOf(readyTimeDT),
					readyTimeT, String.valueOf(readyTime), answerTimeDTString,
					answerTimeT, answerTimeString,
					String.valueOf(clickNextTimeDT), clickNextTimeT,
					String.valueOf(clickNextTime), String.valueOf(readingTime),
					responseTimeString });

			// Change panel
			progressBar.setVisible(false);
			lblTotalTimeLeft.setVisible(false);
			time.setVisible(false);
			CardLayout cl = (CardLayout) (center.getLayout());
			cl.show(center, nextPanel);

			// Record time
			recordTime("start");
		}
	}

	public class ReadyButtonListener extends MouseAdapter {

		JLabel lblPicture, lblQuestion, lblTimeOut;
		JButton button1, button2, button3, button4, btnNext, btnImReady;

		public ReadyButtonListener(JLabel lblPicture, JLabel lblQuestion,
				JButton button1, JButton button2, JButton button3,
				JButton button4, JButton btnNext, JButton btnImReady,
				JLabel lblTimeOut) {
			this.lblPicture = lblPicture;
			this.lblQuestion = lblQuestion;
			this.button1 = button1;
			this.button2 = button2;
			this.button3 = button3;
			this.button4 = button4;
			this.btnNext = btnNext;
			this.btnImReady = btnImReady;
			this.lblTimeOut = lblTimeOut;
		}

		public void mousePressed(MouseEvent arg0) {

			// Record time
			recordTime("ready");
			startTiming(lblPicture, lblQuestion, button1, button2, button3,
					button4, btnNext, lblTimeOut);

			// Set visible/enable
			lblPicture.setVisible(true);
			btnImReady.setVisible(false);
			button1.setEnabled(true);
			button2.setEnabled(true);
			button3.setEnabled(true);
			button4.setEnabled(true);
		}
	}

	public class ChoiceListener extends MouseAdapter {

		JButton btnImReady, button1, button2, button3, button4, btnNext;
		String trialType, stimuliName;
		JLabel feedback1, feedback2, feedback3, feedback4;

		public ChoiceListener(JButton btnImReady, JButton button1,
				JButton button2, JButton button3, JButton button4,
				JButton btnNext, String trialType, String stimuliName,
				JLabel feedback1, JLabel feedback2, JLabel feedback3,
				JLabel feedback4) {
			this.btnImReady = btnImReady;
			this.button1 = button1;
			this.button2 = button2;
			this.button3 = button3;
			this.button4 = button4;
			this.btnNext = btnNext;
			this.trialType = trialType;
			this.stimuliName = stimuliName;
			this.feedback1 = feedback1;
			this.feedback2 = feedback2;
			this.feedback3 = feedback3;
			this.feedback4 = feedback4;
		}

		public void mousePressed(MouseEvent e) {

			if (!btnImReady.isVisible() && !btnNext.isVisible()) {
				JButton button = (JButton) e.getSource();

				// Record time
				recordTime("answer");

				// Stop timer
				timer.stop();

				// Set visible/enable
				answer = button.getText();

				// Training
				if (trialType.equalsIgnoreCase("training")) {
					String[] qa = QA.get(stimuliName);
					String correctPosition = qa[10];

					if (button == button1) {
						answerPosition = "1";
					} else if (button == button2) {
						answerPosition = "2";
					} else if (button == button3) {
						answerPosition = "3";
					} else if (button == button4) {
						answerPosition = "4";
					}

					giveFeedback(answerPosition, correctPosition);
					button1.setEnabled(false);
					button2.setEnabled(false);
					button3.setEnabled(false);
					button4.setEnabled(false);
				}

				// Testing
				else {
					if (button == button1) {
						answerPosition = "1";
						button1.setEnabled(false);
						button2.setVisible(false);
						button3.setVisible(false);
						button4.setVisible(false);
					} else if (button == button2) {
						answerPosition = "2";
						button1.setVisible(false);
						button2.setEnabled(false);
						button3.setVisible(false);
						button4.setVisible(false);
					} else if (button == button3) {
						answerPosition = "3";
						button1.setVisible(false);
						button2.setVisible(false);
						button3.setEnabled(false);
						button4.setVisible(false);
					} else if (button == button4) {
						answerPosition = "4";
						button1.setVisible(false);
						button2.setVisible(false);
						button3.setVisible(false);
						button4.setEnabled(false);
					}
				}
				btnNext.setVisible(true);
			}
		}

		private void giveFeedback(String answerPosition, String correctPosition) {
			int answer = Integer.parseInt(answerPosition);
			int correct = Integer.parseInt(correctPosition);
			if (answer == 1) {
				if (answer != correct) {
					feedback1.setText("Wrong!");
					feedback1.setForeground(Color.RED);
					displayCorrectAnswer(correctPosition);
				}
				feedback1.setVisible(true);
			} else if (answer == 2) {
				if (answer != correct) {
					feedback2.setText("Wrong!");
					feedback2.setForeground(Color.RED);
					displayCorrectAnswer(correctPosition);
				}
				feedback2.setVisible(true);
			} else if (answer == 3) {
				if (answer != correct) {
					feedback3.setText("Wrong!");
					feedback3.setForeground(Color.RED);
					displayCorrectAnswer(correctPosition);
				}
				feedback3.setVisible(true);
			} else if (answer == 4) {
				if (answer != correct) {
					feedback4.setText("Wrong!");
					feedback4.setForeground(Color.RED);
					displayCorrectAnswer(correctPosition);
				}
				feedback4.setVisible(true);
			}

		}

		private void displayCorrectAnswer(String correctPosition) {
			int correct = Integer.parseInt(correctPosition);
			if (correct == 1) {
				feedback1.setVisible(true);
			} else if (correct == 2) {
				feedback2.setVisible(true);
			} else if (correct == 3) {
				feedback3.setVisible(true);
			} else if (correct == 4) {
				feedback4.setVisible(true);
			}
		}
	}

	// Timing Listener

	public class TimingListener implements ActionListener {

		int currentTime = 1;
		int timeLeft = maxTime - 1;
		JLabel picture, question;
		JButton choice1, choice2, choice3, choice4, next;
		JLabel timeOut;

		public TimingListener(JLabel lblPicture, JLabel lblQuestion,
				JButton button1, JButton button2, JButton button3,
				JButton button4, JButton btnNext, JLabel lblTimeOut) {
			this.picture = lblPicture;
			this.question = lblQuestion;
			this.choice1 = button1;
			this.choice2 = button2;
			this.choice3 = button3;
			this.choice4 = button4;
			this.next = btnNext;
			this.timeOut = lblTimeOut;
		}

		public void actionPerformed(ActionEvent ae) {
			if (currentTime == maxTime) {
				timer.stop();
				progressBar.setVisible(false);
				lblTotalTimeLeft.setVisible(false);
				time.setVisible(false);
				picture.setVisible(false);
				question.setVisible(false);
				choice1.setVisible(false);
				choice2.setVisible(false);
				choice3.setVisible(false);
				choice4.setVisible(false);
				timeOut.setVisible(true);
				next.setVisible(true);
			}
			progressBar.setValue(currentTime);
			currentTime++;
			time.setText(timeLeft + "");
			timeLeft--;
		}

	}

	// Choice Icon

	public class BoxIcon implements Icon {
		private Color[] color;
		private int borderWidth = 1;
		private int gap = 7;
		private int offset = 25;

		BoxIcon(Color[] color) {
			this.color = color;
		}

		public int getIconWidth() {
			return 45;
		}

		public int getIconHeight() {
			return 20;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {

			// 1 choice
			if (color.length == 1) {
				g.setColor(Color.BLACK);
				g.fillRect(x, y, getIconWidth(), getIconHeight());
				g.setColor(color[0]);
				g.fillRect(x + borderWidth, y + borderWidth, getIconWidth() - 2
						* borderWidth, getIconHeight() - 2 * borderWidth);
			}

			// 2 choices
			else if (color.length == 2) {
				// First box
				g.setColor(Color.BLACK);
				g.fillRect(x - getIconWidth() - gap + offset, y,
						getIconWidth(), getIconHeight());
				g.setColor(color[0]);
				g.fillRect(x - getIconWidth() - gap + offset + borderWidth, y
						+ borderWidth, getIconWidth() - 2 * borderWidth,
						getIconHeight() - 2 * borderWidth);

				// Second box
				g.setColor(Color.BLACK);
				g.fillRect(x + offset, y, getIconWidth(), getIconHeight());
				g.setColor(color[1]);
				g.fillRect(x + offset + borderWidth, y + borderWidth,
						getIconWidth() - 2 * borderWidth, getIconHeight() - 2
								* borderWidth);
			}

			// 4 choices
			else if (color.length == 4) {
				// First box
				g.setColor(Color.BLACK);
				g.fillRect(x - getIconWidth() * 2 - gap * 2 + offset, y,
						getIconWidth(), getIconHeight());
				g.setColor(color[0]);
				g.fillRect(x - getIconWidth() * 2 - gap * 2 + offset
						+ borderWidth, y + borderWidth, getIconWidth() - 2
						* borderWidth, getIconHeight() - 2 * borderWidth);

				// Second box
				g.setColor(Color.BLACK);
				g.fillRect(x - getIconWidth() - gap + offset, y,
						getIconWidth(), getIconHeight());
				g.setColor(color[1]);
				g.fillRect(x - getIconWidth() - gap + offset + borderWidth, y
						+ borderWidth, getIconWidth() - 2 * borderWidth,
						getIconHeight() - 2 * borderWidth);

				// Third box
				g.setColor(Color.BLACK);
				g.fillRect(x + offset, y, getIconWidth(), getIconHeight());
				g.setColor(color[2]);
				g.fillRect(x + offset + borderWidth, y + borderWidth,
						getIconWidth() - 2 * borderWidth, getIconHeight() - 2
								* borderWidth);

				// Fourth box
				g.setColor(Color.BLACK);
				g.fillRect(x + getIconWidth() + gap + offset, y,
						getIconWidth(), getIconHeight());
				g.setColor(color[3]);
				g.fillRect(x + getIconWidth() + gap + offset + borderWidth, y
						+ borderWidth, getIconWidth() - 2 * borderWidth,
						getIconHeight() - 2 * borderWidth);
			}
		}
	}

	public class ShapeIcon implements Icon {
		private Color[] color;
		private String[] choices;
		private int border = 7;

		ShapeIcon(Color[] color, String[] choices) {
			this.color = color;
			this.choices = choices;
		}

		public int getIconWidth() {
			return 45;
		}

		public int getIconHeight() {
			return 20;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;

			// 1 choice
			if (color.length == 1) {
				// White background
				g.setColor(Color.WHITE);
				g.fillRect(x + 12 - border, y - border, 33, 33);
				// First shape
				g2.setColor(color[0]);
				g2.fill(getShape(choices[0], x + 12, y));
			}

			// 2 choices
			else if (color.length == 2) {
				// White background
				g.setColor(Color.WHITE);
				g.fillRect(x - 17 - border, y - border, 33, 33);
				// First shape
				g.setColor(color[0]);
				g2.fill(getShape(choices[0], x - 17, y));

				// White background
				g.setColor(Color.WHITE);
				g.fillRect(x + 40 - border, y - border, 33, 33);
				// Second shape
				g.setColor(color[1]);
				g2.fill(getShape(choices[1], x + 40, y));
			}

			// 4 choices
			else if (color.length == 4) {

				// White background
				g.setColor(Color.WHITE);
				g.fillRect(x - 72 - border, y - border, 33, 33);
				// First shape
				g2.setColor(color[0]);
				g2.fill(getShape(choices[0], x - 72, y));

				// White background
				g.setColor(Color.WHITE);
				g.fillRect(x - 17 - border, y - border, 33, 33);
				// Second shape
				g.setColor(color[1]);
				g2.fill(getShape(choices[1], x - 17, y));

				// White background
				g.setColor(Color.WHITE);
				g.fillRect(x + 40 - border, y - border, 33, 33);
				// Third shape
				g.setColor(color[2]);
				g2.fill(getShape(choices[2], x + 40, y));

				// White background
				g.setColor(Color.WHITE);
				g.fillRect(x + 95 - border, y - border, 33, 33);
				// Fourth shape
				g.setColor(color[3]);
				g2.fill(getShape(choices[3], x + 95, y));
			}
		}
	}

	private Shape getShape(String choice, int startX, int startY) {
		Shape shape = null;
		if (choice.trim().equals("1")) { // circle
			shape = new Ellipse2D.Double(startX, startY, 20, 20);
		} else if (choice.trim().equals("2")) { // cross
			shape = createCross(startX, startY, 20);
		} else if (choice.trim().equals("3")) { // diamond
			shape = createDiamond(startX, startY - 5, 28);
		} else if (choice.trim().equals("4")) { // square
			shape = new Rectangle(startX, startY, 20, 20);
		} else if (choice.trim().equals("5")) { // triangle-down
			shape = createDownTriangle(startX - 2, startY, 20);
		} else if (choice.trim().equals("6")) { // triangle-up
			shape = createUpTriangle(startX - 2, startY, 20);
		} else if (choice.trim().equals("7")) { // heart
			shape = createHeart(startX - 13, startY + 2, 45);
		} else if (choice.trim().equals("8")) { // oval
			shape = new Ellipse2D.Double(startX + 3, startY - 5, 13, 26);
		}
		return shape;
	}

	private Shape createHeart(int startX, int startY, int size) {
		GeneralPath path = new GeneralPath();
		path.moveTo(startX + size / 2, startY + size * 0.4);
		path.curveTo(startX + size, startY - size * 0.13, startX + size / 2,
				startY - size * 0.13, startX + size / 2, startY + size * 0.13
						- 4);
		path.curveTo(startX + size / 2, startY - size * 0.13, startX, startY
				- size * 0.13, startX + size / 2, startY + size * 0.4);
		path.closePath();
		return path;
	}

	private Shape createDiamond(int startX, int startY, int size) {
		GeneralPath path = new GeneralPath();
		path.moveTo(startX + size / 3, startY);
		path.lineTo(startX + size * 2 / 3, startY + size / 2);
		path.lineTo(startX + size / 3, startY + size);
		path.lineTo(startX, startY + size / 2);
		path.closePath();
		return path;
	}

	private Shape createUpTriangle(int startX, int startY, int size) {
		GeneralPath path = new GeneralPath();
		path.moveTo(startX + size * 0.6, startY);
		path.lineTo(startX + size * 1.2, startY + size);
		path.lineTo(startX, startY + size);
		path.closePath();
		return path;
	}

	private Shape createDownTriangle(int startX, int startY, int size) {
		GeneralPath path = new GeneralPath();
		path.moveTo(startX, startY);
		path.lineTo(startX + size * 1.2, startY);
		path.lineTo(startX + size * 0.6, startY + size);
		path.closePath();
		return path;
	}

	private Shape createCross(int startX, int startY, int size) {
		GeneralPath path = new GeneralPath();
		path.moveTo(startX + size / 3, startY);
		path.lineTo(startX + size * 2 / 3, startY);
		path.lineTo(startX + size * 2 / 3, startY + size / 3);
		path.lineTo(startX + size, startY + size / 3);
		path.lineTo(startX + size, startY + size * 2 / 3);
		path.lineTo(startX + size * 2 / 3, startY + size * 2 / 3);
		path.lineTo(startX + size * 2 / 3, startY + size);
		path.lineTo(startX + size / 3, startY + size);
		path.lineTo(startX + size / 3, startY + size * 2 / 3);
		path.lineTo(startX, startY + size * 2 / 3);
		path.lineTo(startX, startY + size / 3);
		path.lineTo(startX + size / 3, startY + size / 3);
		path.closePath();
		return path;
	}

	public class LineIcon implements Icon {
		private Color[] color;
		private int borderWidth = 1;
		private int gap = 4;
		private int offset = 15;

		LineIcon(Color[] color) {
			this.color = color;
		}

		public int getIconWidth() {
			return 200;
		}

		public int getIconHeight() {
			return 8;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {

			// 1 choice
			if (color.length == 1) {
				// White background
				g.setColor(Color.WHITE);
				g.fillRect(x - gap, y - offset - gap, getIconWidth() + gap * 2,
						getIconHeight() + gap * 2);

				// First line
				g.setColor(color[0]);
				g.fillRect(x + borderWidth, y + borderWidth - offset,
						getIconWidth() - 2 * borderWidth, getIconHeight() - 2
								* borderWidth);
			}

			// 2 choices
			else if (color.length == 2) {
				// White background
				g.setColor(Color.WHITE);
				g.fillRect(x - gap, y - offset - gap, getIconWidth() + gap * 2,
						(getIconHeight() + gap) * 2 + gap);

				// First line
				g.setColor(color[0]);
				g.fillRect(x + borderWidth, y + borderWidth - offset,
						getIconWidth() - 2 * borderWidth, getIconHeight() - 2
								* borderWidth);

				// Second line
				g.setColor(color[1]);
				g.fillRect(x + borderWidth, y + borderWidth + getIconHeight()
						+ gap - offset, getIconWidth() - 2 * borderWidth,
						getIconHeight() - 2 * borderWidth);
			}

			// 4 choices
			else if (color.length == 4) {
				// White background
				g.setColor(Color.WHITE);
				g.fillRect(x - gap, y - offset - gap, getIconWidth() + gap * 2,
						(getIconHeight() + gap) * 4 + gap);

				// First line
				g.setColor(color[0]);
				g.fillRect(x + borderWidth, y + borderWidth - offset,
						getIconWidth() - 2 * borderWidth, getIconHeight() - 2
								* borderWidth);

				// Second line
				g.setColor(color[1]);
				g.fillRect(x + borderWidth, y + borderWidth + getIconHeight()
						+ gap - offset, getIconWidth() - 2 * borderWidth,
						getIconHeight() - 2 * borderWidth);

				// Third line
				g.setColor(color[2]);
				g.fillRect(x + borderWidth, y + borderWidth
						+ (getIconHeight() + gap) * 2 - offset, getIconWidth()
						- 2 * borderWidth, getIconHeight() - 2 * borderWidth);

				// Fourth line
				g.setColor(color[3]);
				g.fillRect(x + borderWidth, y + borderWidth
						+ (getIconHeight() + gap) * 3 - offset, getIconWidth()
						- 2 * borderWidth, getIconHeight() - 2 * borderWidth);
			}
		}
	}
}
