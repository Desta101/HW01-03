package BusinessLogic.Classes;

public class Solution{
	private String line;
	private boolean answer;

	public Solution(String line, boolean answer) {
		this.line = line;
		this.answer = answer;
	}
	
	public void setLine(String line) {
		this.line = line;
	}

	public String getLine() {
		return line;
	}

	public void setAnswer(boolean answer) {
		this.answer = answer;
	}

	public boolean getAnswer() {
		return answer;
	}
	
	public boolean equals(Object other)   {
		Solution checkSolution;
		
        if (!(other instanceof Solution))
            return false;
        checkSolution = (Solution)other;
        return checkSolution.line.equals(line) && checkSolution.answer== answer;
   }
   
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (answer ? 1231 : 1237);
		result = prime * result + ((line == null) ? 0 : line.hashCode());
		return result;
	}

	public String toString() {
		return line + ". Answer => " + answer;
	}

	public String saveSolution(String delimiter) {
		return line + delimiter + answer;
	}
}
