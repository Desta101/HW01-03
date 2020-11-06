package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Application.Program;
import BusinessLogic.Classes.Question;
import BusinessLogic.Classes.Test;

public class CreateManualTestPanel extends JPanel {
	private DefaultListModel<String> leftSideList, rightSideList;
	private JList<String> questionList, testList;
	private int leftSelection, rightSelection;
	private int maxSelection, questionCounter;
	private JButton doneButton, shiftLeftButton, shiftRighttButton;
	private Vector<Question> questions;
	private Test test;

	public CreateManualTestPanel(int maxSelection) {
		int size;

		test = new Test(maxSelection);
		leftSideList = new DefaultListModel<String>();
		rightSideList = new DefaultListModel<String>();
		questions = Program.getCollection().getQuestions();
		test = new Test(questions.size());
		this.maxSelection = maxSelection;
		questionCounter = 0;
		setLayout(new GridLayout(1, 3));
		add(createLeftScrollPanel());
		add(createButtonPanel());
		add(createRightScrollPanel());
		size = questions.size();
		for (int select = 0; select < size; select++) {
			leftSideList.addElement(questions.get(select).getLine());
			rightSideList.addElement("");
		}
	}

	public JScrollPane createLeftScrollPanel() {
		JPanel panel;
		JScrollPane leftScrollPanel;

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		questionList = new JList<String>(leftSideList);
		questionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		questionList.setBackground(getBackground());
		questionList.addListSelectionListener(new QuestionListSelect());
		panel.add(questionList);
		leftScrollPanel = new JScrollPane(panel);
		leftScrollPanel.setBorder(BorderFactory.createTitledBorder("Available questions : "));
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
		rightScrollPanel.setBorder(BorderFactory.createTitledBorder("Test questions : "));
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

	class QuestionListSelect implements ListSelectionListener {
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
				MainFrame.addPanel(new ChooseSolutionsPanel(leftSelection, test, questions.get(leftSelection)));
				rightSideList.setElementAt(leftSideList.getElementAt(leftSelection), leftSelection);
				leftSideList.setElementAt("", leftSelection);
				questionCounter++;
				if (questionCounter == maxSelection) {
					doneButton.setEnabled(true);
					shiftRighttButton.setEnabled(false);
					questionList.setEnabled(false);
				}
			}
		}
	}

	class MoveLeftAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!rightSideList.getElementAt(rightSelection).isEmpty()) {
				leftSideList.setElementAt(rightSideList.getElementAt(rightSelection), rightSelection);
				rightSideList.setElementAt("", rightSelection);
				test.getQuestions().setElementAt(null, rightSelection);
				questionCounter--;
				doneButton.setEnabled(false);
				questionList.setEnabled(true);
				shiftRighttButton.setEnabled(true);
				if (questionCounter == 0) {
					shiftLeftButton.setEnabled(false);
					testList.setEnabled(false);
				}
			}
		}
	}

	class DoneAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int size, returnValue;
			Vector<Question> questions;
			JFileChooser fileChooser;

			questions = test.getQuestions();
			size = questions.size();
			for (int select = 0; select < size; select++)
				if (questions.get(select) == null) {
					questions.removeElementAt(select);
					size--;
					select--;
				}
			fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			returnValue = fileChooser.showSaveDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				try {
					Program.saveTest(test, fileChooser.getSelectedFile().toString());
					JOptionPane.showMessageDialog(null, "Saved your Test successfully", "Success",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException error) {
					JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			MainFrame.switchBackPanel();
		}
	}
}
