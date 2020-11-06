package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Application.Program;
import BusinessLogic.Classes.Question;
import BusinessLogic.Classes.Solution;

public class EditQuestionPanel extends JPanel {

	private DefaultListModel<String> solutionLines;
	private Question selectedQuestion;
	private JButton deleteButton, editButton;
	private int solutionIndex;

	public EditQuestionPanel(int index) {
		selectedQuestion = Program.getCollection().getQuestions().get(index);
		solutionLines = new DefaultListModel<String>();
		setLayout(new BorderLayout());
		add(createScrollPanel(), BorderLayout.CENTER);
		add(createButtonPanel(), BorderLayout.SOUTH);
	}

	public JScrollPane createScrollPanel() {
		JPanel panel;
		JList<String> solutionList;
		JScrollPane scrollPanel;
		int size;

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		size = selectedQuestion.getsolutionList().size();
		for (int select = 0; select < size; select++) {
			solutionLines.addElement(selectedQuestion.getsolutionList().get(select).toString());
		}
		solutionList = new JList<String>(solutionLines);
		solutionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		solutionList.setBackground(getBackground());
		solutionList.addListSelectionListener(new ListSelect());
		panel.add(solutionList);
		scrollPanel = new JScrollPane(panel);
		scrollPanel.setBorder(BorderFactory.createTitledBorder("Question : " + selectedQuestion.getLine()));
		return scrollPanel;
	}

	public JPanel createButtonPanel() {
		JPanel buttonPanel;
		JButton doneButton;

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 1));
		buttonPanel.add(createUpperButtonPanel());
		doneButton = new JButton("Done");
		doneButton.addActionListener(new DoneAction());
		buttonPanel.add(doneButton);
		if (solutionLines.isEmpty()) {
			deleteButton.setEnabled(false);
			editButton.setEnabled(false);
		}
		return buttonPanel;
	}

	public JPanel createUpperButtonPanel() {
		JPanel upperButtonPanel;
		JButton addButton;

		upperButtonPanel = new JPanel();
		upperButtonPanel.setLayout(new GridLayout(1, 3));
		addButton = new JButton("Add");
		addButton.addActionListener(new AddAction());
		upperButtonPanel.add(addButton);
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new DeleteAction());
		upperButtonPanel.add(deleteButton);
		editButton = new JButton("Edit");
		editButton.addActionListener(new EditAction());
		upperButtonPanel.add(editButton);
		return upperButtonPanel;
	}

	class ListSelect implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			int selectFirstIndex;

			if (!e.getValueIsAdjusting()) {
				selectFirstIndex = e.getFirstIndex();
				if (solutionIndex != selectFirstIndex)
					solutionIndex = selectFirstIndex;
				else
					solutionIndex = e.getLastIndex();
			}
		}
	}

	class AddAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JCheckBox checkBox;
			JTextField textField;
			Vector<Object> parts;
			int option;
			String line;
			Solution newSolution;

			checkBox = new JCheckBox("true?");
			textField = new JTextField();
			parts = new Vector<Object>();
			parts.add("Enter a solution");
			parts.add(textField);
			parts.add(checkBox);
			option = JOptionPane.showConfirmDialog(null, parts.toArray(), "Add solution", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			line = textField.getText();
			if (option == JOptionPane.OK_OPTION)
				try {
					if (!line.equals("")) {
						if (!solutionLines.contains(line + ". Answer => " + "true")
								&& !solutionLines.contains(line + ". Answer => " + "false")) {
							newSolution = new Solution(textField.getText(), checkBox.isSelected());
							selectedQuestion.addSolution(newSolution);
							solutionLines.addElement(newSolution.toString());
							MainMenuPanel.updateViewArea();
							if (solutionLines.size() == 1) {
								deleteButton.setEnabled(true);
								editButton.setEnabled(true);
							}
						} else
							throw new Exception("You already have this solution");
					} else
						throw new Exception("No input");
				} catch (Exception error) {
					JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
		}
	}

	class DeleteAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			selectedQuestion.deleteSolution(solutionIndex);
			solutionLines.remove(solutionIndex);
			if (solutionLines.size() == 0) {
				deleteButton.setEnabled(false);
				editButton.setEnabled(false);
			}
			MainMenuPanel.updateViewArea();
		}
	}

	class EditAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JCheckBox checkBox;
			String oldSolutionLine;
			JTextField textField;
			Vector<Object> parts;
			int option;
			String line;
			Solution selectedSolution;

			checkBox = new JCheckBox("true?");
			selectedSolution = selectedQuestion.getsolutionList().get(solutionIndex);
			oldSolutionLine = selectedSolution.getLine();
			textField = new JTextField(oldSolutionLine);
			parts = new Vector<Object>();
			parts.add("Enter a solution");
			parts.add(textField);
			parts.add(checkBox);
			option = JOptionPane.showConfirmDialog(null, parts.toArray(), "Add a solution",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			line = textField.getText();
			if (option == JOptionPane.OK_OPTION) {
				if (line.equals(""))
					line = oldSolutionLine;
				try {
					if ((selectedSolution.getLine().equals(line)
							&& selectedSolution.getAnswer() != checkBox.isSelected())
							|| (!selectedSolution.getLine().equals(line)
									&& (!solutionLines.contains(line + ". Answer => " + "true")
											&& !solutionLines.contains(line + ". Answer => " + "false")))) {
						selectedQuestion.updateSolution(line, checkBox.isSelected(), solutionIndex);
						solutionLines.setElementAt(selectedSolution.toString(), solutionIndex);
						MainMenuPanel.updateViewArea();
					} else
						throw new Exception("You already have this solution");
				} catch (Exception error) {
					JOptionPane.showMessageDialog(null, error.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	class DoneAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			MainFrame.switchBackPanel();
		}
	}
}
