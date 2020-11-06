package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import BusinessLogic.Classes.Question;
import BusinessLogic.Classes.Solution;
import BusinessLogic.Classes.Test;

public class ChooseSolutionsPanel extends JPanel {
	private static final int MAXSELECTION = 8;
	private JButton doneButton, shiftLeftButton, shiftRighttButton;
	private DefaultListModel<String> leftSideList, rightSideList;
	private JList<String> solutionList, testList;
	private int leftSelection, rightSelection;
	private Test test;
	private Question selectedQuestion;
	private Vector<Solution> solutionSelection, solutions;
	private int solutionCounter, questionIndex;

	public ChooseSolutionsPanel(int index, Test test, Question selectedQuestion) {
		int size;

		questionIndex = index;
		solutionCounter = 0;
		solutions = selectedQuestion.getsolutionList();
		solutionSelection = new Vector<Solution>();
		solutionSelection.setSize(selectedQuestion.getsolutionList().size());
		this.test = test;
		this.selectedQuestion = selectedQuestion;
		leftSideList = new DefaultListModel<String>();
		rightSideList = new DefaultListModel<String>();
		setLayout(new GridLayout(1, 3));
		add(createLeftScrollPanel());
		add(createButtonPanel());
		add(createRightScrollPanel());
		size = solutions.size();
		for (int select = 0; select < size; select++) {
			leftSideList.addElement(solutions.get(select).toString());
			rightSideList.addElement("");
		}
		setBorder(BorderFactory.createTitledBorder("Question : " + selectedQuestion.getLine()));
	}

	public JScrollPane createLeftScrollPanel() {
		JPanel panel;
		JScrollPane leftScrollPanel;

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		solutionList = new JList<String>(leftSideList);
		solutionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		solutionList.setBackground(getBackground());
		solutionList.addListSelectionListener(new SolutionListSelect());
		panel.add(solutionList);
		leftScrollPanel = new JScrollPane(panel);
		leftScrollPanel.setBorder(BorderFactory.createTitledBorder("Available solutions : "));
		return leftScrollPanel;
	}

	public JScrollPane createRightScrollPanel() {
		JPanel panel;
		JScrollPane rightScrollPanel;

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		testList = new JList<String>(rightSideList);
		testList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		testList.setBackground(getBackground());
		testList.addListSelectionListener(new TestListSelect());
		testList.setEnabled(false);
		panel.add(testList);
		rightScrollPanel = new JScrollPane(panel);
		rightScrollPanel.setBorder(BorderFactory.createTitledBorder("Selected solutions : "));
		return rightScrollPanel;
	}

	public JPanel createButtonPanel() {
		JPanel buttonsPanel;

		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(3, 1));
		doneButton = new JButton("Done");
		doneButton.setEnabled(false);
		doneButton.addActionListener(new DoneAction());
		shiftLeftButton = new JButton("<=");
		shiftLeftButton.setEnabled(false);
		shiftLeftButton.addActionListener(new MoveLeftAction());
		shiftRighttButton = new JButton("=>");
		shiftRighttButton.addActionListener(new MoveRightAction());
		buttonsPanel.add(shiftRighttButton);
		buttonsPanel.add(shiftLeftButton);
		buttonsPanel.add(doneButton);
		return buttonsPanel;
	}

	class SolutionListSelect implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			int selectFirstIndex;

			if (!e.getValueIsAdjusting()) {
				selectFirstIndex = e.getFirstIndex();
				if (leftSelection != selectFirstIndex)
					leftSelection = selectFirstIndex;
				else
					leftSelection = e.getLastIndex();
			}
		}
	}

	class TestListSelect implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			int selectFirstIndex;

			if (!e.getValueIsAdjusting()) {
				selectFirstIndex = e.getFirstIndex();
				if (rightSelection != selectFirstIndex)
					rightSelection = selectFirstIndex;
				else
					rightSelection = e.getLastIndex();
			}
		}
	}

	class MoveRightAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!leftSideList.getElementAt(leftSelection).isEmpty()) {
				testList.setEnabled(true);
				shiftLeftButton.setEnabled(true);
				solutionSelection.set(leftSelection, solutions.get(leftSelection));
				rightSideList.setElementAt(leftSideList.getElementAt(leftSelection), leftSelection);
				leftSideList.setElementAt("", leftSelection);
				solutionCounter++;
				if (solutionCounter == 1)
					doneButton.setEnabled(true);
				if (solutionCounter == MAXSELECTION) {
					shiftRighttButton.setEnabled(false);
					solutionList.setEnabled(false);
				}
			}
		}
	}

	class MoveLeftAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!rightSideList.getElementAt(rightSelection).isEmpty()) {
				solutionSelection.set(rightSelection, null);
				leftSideList.setElementAt(rightSideList.getElementAt(rightSelection), rightSelection);
				rightSideList.setElementAt("", rightSelection);
				solutionCounter--;
				solutionList.setEnabled(true);
				shiftRighttButton.setEnabled(true);
				if (solutionCounter == 0) {
					doneButton.setEnabled(false);
					shiftLeftButton.setEnabled(false);
					testList.setEnabled(false);
				}
			}
		}
	}

	class DoneAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				test.createTestQuestion(questionIndex, selectedQuestion, solutionSelection);
			} catch (Exception error) {
				JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			MainFrame.switchBackPanel();
		}
	}

}
