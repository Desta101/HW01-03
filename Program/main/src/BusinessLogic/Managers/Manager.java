package BusinessLogic.Managers;

import java.util.Vector;

import BusinessLogic.Classes.Question;

public class Manager {
	private Vector<Question> questions;

	public Manager() {
		questions = new Vector<Question>();
	}
	
	public Vector<Question> getQuestions()
	{
		return questions;
	}
	
	public int correctQuestions() {
		int counter,size;

		counter = 0;
		size = questions.size();
		for (int select = 0; select < size; select++)
			if (questions.get(select).getFalseCounter() >= 3 && questions.get(select).getsolutionList().size() >= 4)
				counter++;
		return counter;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((questions == null) ? 0 : questions.hashCode());
		return result;
	}
	
	public boolean equals(Object other) {
		Manager collection;

		if (!(other instanceof Manager))
			return false;
		collection = (Manager) other;
		return collection.getQuestions().equals(questions);
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

	public String saveCollection(String delimiter) {
		int questionAmount;
		StringBuilder line;
		
		questionAmount = questions.size();
		line = new StringBuilder(questionAmount + "\r\n");
		for (int select = 0; select < questionAmount; select++)
			line.append(questions.get(select).saveQuestion(delimiter) + "\r\n");
		return line.toString();
	}
}
