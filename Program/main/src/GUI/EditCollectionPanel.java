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

import Application.Program;
import BusinessLogic.Classes.Question;

public class EditCollectionPanel extends JPanel {

	private DefaultListModel<String> questionLines;
	private Vector<Question> questions;
	private int selectedQuestion;
	private JButton deleteButton, rewriteButton, editButton;

	public EditCollectionPanel() {
		questions = Program.getCollection().getQuestions();
		questionLines = new DefaultListModel<String>();
		setLayout(new BorderLayout());
		add(createScrollPanel(), BorderLayout.CENTER);
		add(createButtonPanel(), BorderLayout.SOUTH);
	}

	public JScrollPane createScrollPanel() {
		JPanel panel;
		JList<String> questionList;
		JScrollPane scrollPanel;
		int size;

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		size = questions.size();
		for (int select = 0; select < size; select++)
			questionLines.addElement(questions.get(select).getLine());
		questionList = new JList<String>(questionLines);
		questionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		questionList.setBackground(getBackground());
		questionList.addListSelectionListener(new ListSelect());
		panel.add(questionList);
		scrollPanel = new JScrollPane(panel);
		scrollPanel.setBorder(BorderFactory.createTitledBorder("Questions : "));
		return scrollPanel;
	}

	public JPanel createButtonPanel() {
		JPanel buttonPanel;
		JButton doneButton;

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3, 1));
		buttonPanel.add(createUpperButtonPanel());
		editButton = new JButton("Edit Question's Solutions");
		editButton.addActionListener(new EditAction());
		buttonPanel.add(editButton);
		doneButton = new JButton("Done");
		doneButton.addActionListener(new DoneAction());
		buttonPanel.add(doneButton);
		if (questionLines.isEmpty()) {
			deleteButton.setEnabled(false);
			rewriteButton.setEnabled(false);
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
		rewriteButton = new JButton("Rewrite");
		rewriteButton.addActionListener(new RewriteAction());
		upperButtonPanel.add(rewriteButton);
		return upperButtonPanel;
	}

	class ListSelect implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			int selectFirstIndex;

			if (!e.getValueIsAdjusting()) {
				selectFirstIndex = e.getFirstIndex();
				if (selectedQuestion != selectFirstIndex)
					selectedQuestion = selectFirstIndex;
				else
					selectedQuestion = e.getLastIndex();
			}
		}
	}

	class AddAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String line;

			line = JOptionPane.showInputDialog("Enter a question : ", "");
			if (line != null)
				try {
					if (!line.equals("")) {
						if (!questionLines.contains(line)) {
							questionLines.addElement(line);
							questions.add(new Question(line));
							if (questionLines.size() == 1) {
								deleteButton.setEnabled(true);
								rewriteButton.setEnabled(true);
								editButton.setEnabled(true);
							}
							MainMenuPanel.updateViewArea();
						} else
							throw new Exception("You already have this question");
					} else
						throw new Exception("No input");
				} catch (Exception error) {
					JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
		}
	}

	class DeleteAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			questions.remove(selectedQuestion);
			questionLines.remove(selectedQuestion);
			if (questionLines.size() == 0) {
				deleteButton.setEnabled(false);
				rewriteButton.setEnabled(false);
				editButton.setEnabled(false);
			}
			MainMenuPanel.updateViewArea();
		}
	}

	class RewriteAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String line;

			line = JOptionPane.showInputDialog("Enter a question : ", questionLines.get(selectedQuestion));
			if (line != null)
				try {
					if (!line.equals("")) {
						if (!questionLines.contains(line)) {
							questions.elementAt(selectedQuestion).setLine(line);
							questionLines.setElementAt(line, selectedQuestion);
							MainMenuPanel.updateViewArea();
						} else
							throw new Exception("You already have this question");
					} else
						throw new Exception("No input");
				} catch (Exception error) {
					JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
		}
	}

	class EditAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			MainFrame.addPanel(new EditQuestionPanel(selectedQuestion));
		}
	}

	class DoneAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			MainFrame.switchBackPanel();
		}
	}
}
