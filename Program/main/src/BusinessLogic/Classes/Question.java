package BusinessLogic.Classes;

import java.util.Vector;

public class Question {
	private static final int MAXSOLUTIONS = 10;
	private String line;
	private Vector<Solution> solutions;
	private int falseCounter;

	public Question(String line) {
		this.line = line;
		solutions = new Vector<Solution>();
	}

	public static int getMaxSolutions() {
		return MAXSOLUTIONS;
	}

	public Vector<Solution> getsolutionList() {
		return solutions;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getLine() {
		return line;
	}

	public int getFalseCounter() {
		return falseCounter;
	}

	public void addSolution(Solution newSolution) throws Exception {
		if (solutions.size() < MAXSOLUTIONS) {
			solutions.add(newSolution);
			if (newSolution.getAnswer() == false)
				falseCounter++;
		} else
			throw new Exception("You already have " + MAXSOLUTIONS + " solutions");
	}

	public void updateSolution(String line, boolean answer, int index) {
		Solution selectedSolution;
		boolean currentAnswer;

		selectedSolution = solutions.get(index);
		currentAnswer = selectedSolution.getAnswer();
		if (currentAnswer == false && answer == true)
			falseCounter--;
		else if (currentAnswer == true && answer == false)
			falseCounter++;
		selectedSolution.setLine(line);
		selectedSolution.setAnswer(answer);
	}

	public void deleteSolution(int index) {
		if (solutions.get(index).getAnswer() == false)
			falseCounter--;
		solutions.removeElementAt(index);
	}

	public void trueFalseCounter() {
		Solution questionSolution, someTrue;
		int counter, vectorSize;

		someTrue = new Solution("More than one Answer True", false);
		vectorSize = solutions.size();
		counter = falseCounter();
		if (counter == 2) {
			for (int select = 0; select < vectorSize; select++) {
				questionSolution = solutions.get(select);
				if (questionSolution.getAnswer() == true)
					questionSolution.setAnswer(false);
			}
			someTrue.setAnswer(true);
		}
		solutions.add(someTrue);
	}

	public int falseCounter() {
		Solution questionSolution, allFalse;
		int counter, select, size;

		counter = 0;
		select = 0;
		size = solutions.size();
		allFalse = new Solution("All false", true);
		while (counter < 2 && select < size) {
			questionSolution = solutions.get(select);
			if (questionSolution.getAnswer() == true)
				counter++;
			select++;
		}
		if (counter >= 1)
			allFalse.setAnswer(false);
		solutions.add(allFalse);
		return counter;
	}

	public boolean equals(Object other) {
		Question question;

		if (!(other instanceof Question))
			return false;
		question = (Question) other;
		return question.line.equals(line) && question.solutions.equals(solutions);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((line == null) ? 0 : line.hashCode());
		result = prime * result + ((solutions == null) ? 0 : solutions.hashCode());
		return result;
	}

	public String saveTest() {
		StringBuilder testPart;

		testPart = new StringBuilder(line + "\r\n");
		for (int select = 0; select < solutions.size(); select++)
			testPart.append(select + ") " + solutions.get(select).getLine() + "\r\n");
		return testPart.toString();
	}

	public String toString() {
		StringBuilder toPrint;

		toPrint = new StringBuilder(line + "\r\n");
		for (int select = 0; select < solutions.size(); select++)
			toPrint.append(select + ") " + solutions.get(select) + "\r\n");
		return toPrint.toString();
	}

	public String saveQuestion(String delimiter) {
		StringBuilder save;

		save = new StringBuilder(line + "\r\n" + solutions.size() + "\r\n");
		for (int select = 0; select < solutions.size(); select++)
			save.append(solutions.get(select).saveSolution(delimiter) + "\r\n");
		return save.toString();
	}
}
