package Application;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import BusinessLogic.Classes.Question;
import BusinessLogic.Classes.Solution;
import BusinessLogic.Classes.Test;
import BusinessLogic.Managers.Manager;
import GUI.MainFrame;

public class Program {
	private final static String DELIMITER = "<>";
	private static Manager collection;

	public static void main(String[] args) {
			try {
				play();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
	}

	public static void play() throws IOException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				collection = new Manager();
				MainFrame mainFrame = new MainFrame();
				mainFrame.setVisible(true);
			}
		});
	}

	public static Manager getCollection() {
		return collection;
	}

	public static void loadCollection(File collectionFile) throws Exception {
		Scanner reader;

		reader = new Scanner(collectionFile);
		loader(reader, DELIMITER);
		reader.close();
	}

	public static void loader(Scanner reader, String delimiter) throws Exception {
		Question newQuestion;
		Solution newSolution;
		List<String> solutionParts;
		int maxSolutions, maxQuestions, select;

		collection = new Manager();
		select = 0;
		if (reader.hasNext()) {
			maxQuestions = reader.nextInt();
			while (select < maxQuestions) {
				reader.nextLine();
				newQuestion = new Question(reader.nextLine());
				maxSolutions = reader.nextInt();
				reader.nextLine();
				for (int index = 0; index < maxSolutions; index++) {
					solutionParts = new ArrayList<String>(Arrays.asList(reader.nextLine().split("\\" + delimiter)));
					newSolution = new Solution(solutionParts.get(0), Boolean.parseBoolean(solutionParts.get(1)));
					newQuestion.addSolution(newSolution);
				}
				collection.getQuestions().add(newQuestion);
				select++;
			}
		}
	}

	public static void saveCollection(String questionsFile) throws IOException {
		PrintWriter writer;

		if (!questionsFile.contains(".txt"))
			questionsFile += ".txt";
		writer = new PrintWriter(questionsFile);
		writer.println(collection.saveCollection(DELIMITER));
		writer.close();
	}

	public static void saveTest(Test test, String Location) throws IOException {
		File questionFile, solutionFile;
		LocalDateTime now;
		PrintWriter writer;

		now = LocalDateTime.now();
		questionFile = new File(Location + "\\exam_" + now.getYear() + "_" + now.getMonthValue() + "_"
				+ now.getDayOfMonth() + "_" + now.getHour() + "_" + now.getMinute() + ".txt");
		solutionFile = new File(Location + "\\solution_" + now.getYear() + "_" + now.getMonthValue() + "_"
				+ now.getDayOfMonth() + "_" + now.getHour() + "_" + now.getMinute() + ".txt");
		writer = new PrintWriter(questionFile);
		writer.println("Test :\r\n" + test.saveTest());
		writer.close();
		writer = new PrintWriter(solutionFile);
		writer.println(test.toString());
		writer.close();
	}
}
