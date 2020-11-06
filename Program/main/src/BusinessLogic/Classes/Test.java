package BusinessLogic.Classes;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;

public class Test {
	private Vector<Question> questions;

	public Test(int size) {
		questions = new Vector<Question>();
		questions.setSize(size);
	}

	public Vector<Question> getQuestions() {
		return questions;
	}

	public void createTestQuestion(int index, Question collectionQuestion, Vector<Solution> solutionSelection) throws Exception {
		Question newQuestion;
		Solution selectedSolution, newSolution;
		int size;
		
		
		size = solutionSelection.size();
		for(int select =0; select<size;select++)
			if(solutionSelection.get(select)==null)
			{
				solutionSelection.removeElementAt(select);
				size--;
				select--;
			}
		newQuestion = new Question(collectionQuestion.getLine());
		for (int select = 0; select < size; select++) {
			selectedSolution = solutionSelection.get(select);
			newSolution = new Solution(selectedSolution.getLine(), selectedSolution.getAnswer());
			newQuestion.addSolution(newSolution);
		}
		newQuestion.trueFalseCounter();
		questions.setElementAt(newQuestion, index);
	}

	public void createAutomaticTest(Vector<Question> collection, int maxSelection) {
		final int MAXSOLUTIONS = 4;
		Question newQuestion, selectedQuestion;
		Vector<Question> questionSelection;
		int select;

		questionSelection = new Vector<Question>();
		questionSelection.addAll(randomQuestionsSelection(collection, maxSelection));
		select = 0;
		while (select < maxSelection) {
			selectedQuestion = questionSelection.get(select);
			newQuestion = new Question(selectedQuestion.getLine());
			newQuestion.getsolutionList()
					.addAll(randomSolutionSelection(selectedQuestion.getsolutionList(), MAXSOLUTIONS));
			newQuestion.falseCounter();
			questions.add(newQuestion);
			select++;
		}
	}

	public Set<Question> randomQuestionsSelection(Vector<Question> collection, int maxSelection) {
		int select, randomNumber, oldSize, questionAmount;
		Set<Question> questionSelection;
		Question collectionQuestion;

		select = 0;
		oldSize = 0;
		questionAmount = collection.size();
		questionSelection = new LinkedHashSet<Question>();
		while (select < maxSelection) {
			randomNumber = (int) (Math.random() * (questionAmount));
			collectionQuestion = collection.get(randomNumber);
			if (collectionQuestion.getFalseCounter() >= 3 && collectionQuestion.getsolutionList().size() >= 4) {
				questionSelection.add(collectionQuestion);
				if (questionSelection.size() > oldSize) {
					oldSize = questionSelection.size();
					select++;
				}
			}
		}
		return questionSelection;
	}

	public Set<Solution> randomSolutionSelection(Vector<Solution> questionCollection, int maxSelection) {
		int select, randomNumber, trueCounter, solutionAmount, oldSize;
		Set<Solution> solutionSelection;
		Solution questionSolution;

		select = 0;
		trueCounter = 0;
		oldSize = 0;
		solutionAmount = questionCollection.size();
		solutionSelection = new LinkedHashSet<Solution>();
		while (select < maxSelection) {
			randomNumber = (int) (Math.random() * (solutionAmount));
			questionSolution = questionCollection.get(randomNumber);
			if (trueCounter == 0) {
				if (questionSolution.getAnswer() == true)
					trueCounter++;
				solutionSelection.add(questionSolution);
			} else if (questionSolution.getAnswer() == false)
				solutionSelection.add(questionSolution);
			if (solutionSelection.size() > oldSize) {
				oldSize = solutionSelection.size();
				select++;
			}
		}
		return solutionSelection;
	}

	public boolean equals(Object other) {
		Test checkTest;

		if (!(other instanceof Test))
			return false;
		checkTest = (Test) other;
		return checkTest.questions.equals(questions);
	}

	public String toString() {
		int questionAmount;
		StringBuilder line;
		
		questionAmount = questions.size();
		line = new StringBuilder();
		for (int select = 0; select < questionAmount; select++)
			line.append(select + "> " + questions.get(select) + "\r\n");
		return line.toString();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((questions == null) ? 0 : questions.hashCode());
		return result;
	}

	public String saveTest() {
		StringBuilder line;

		line = new StringBuilder();
		for (int select = 0; select < questions.size(); select++)
			line.append(select + "> " + questions.elementAt(select).saveTest() + "\r\n");
		return line.toString();
	}
}
