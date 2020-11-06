package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import Application.Program;
import BusinessLogic.Classes.Test;
import BusinessLogic.Managers.Manager;

public class MainMenuPanel extends JPanel {
	private static JTextArea viewArea;
	private JButton saveButton, testButton;

	public MainMenuPanel() {
		setLayout(new BorderLayout());
		add(createScrollPanel(), BorderLayout.CENTER);
		add(createButtonPanel(), BorderLayout.SOUTH);
	}

	public static void updateViewArea() {
		viewArea.setText(Program.getCollection().toString());
	}

	public JScrollPane createScrollPanel() {
		JPanel panel;
		JScrollPane scrollPanel;

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		viewArea = new JTextArea(Program.getCollection().toString());
		viewArea.setBackground(getBackground());
		viewArea.setEditable(false);
		viewArea.getDocument().addDocumentListener(new checkText());
		panel.add(viewArea);
		scrollPanel = new JScrollPane(panel);
		scrollPanel.setBorder(BorderFactory.createTitledBorder("Your Collection : "));
		return scrollPanel;
	}

	public JPanel createButtonPanel() {
		JPanel buttonPanel;
		JButton exitButton;

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3, 1));
		buttonPanel.add(createUpperButtonPanel());
		testButton = new JButton("Create Test");
		testButton.addActionListener(new OpenTestPanel());
		testButton.setEnabled(false);
		buttonPanel.add(testButton);
		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ExitProgram());
		buttonPanel.add(exitButton);
		return buttonPanel;
	}

	public JPanel createUpperButtonPanel() {
		JPanel upperButtonPanel;
		JButton loadButton, editButton;

		upperButtonPanel = new JPanel();
		upperButtonPanel.setLayout(new GridLayout(1, 3));
		saveButton = new JButton("Save");
		saveButton.addActionListener(new SaveFile());
		saveButton.setEnabled(false);
		upperButtonPanel.add(saveButton);
		loadButton = new JButton("Load");
		loadButton.addActionListener(new LoadFile());
		upperButtonPanel.add(loadButton);
		editButton = new JButton("Edit Collection");
		editButton.addActionListener(new EditCollection());
		upperButtonPanel.add(editButton);
		return upperButtonPanel;
	}

	class checkText implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
		}

		public void insertUpdate(DocumentEvent e) {
			if (!Program.getCollection().toString().isEmpty()) {
				saveButton.setEnabled(true);
				testButton.setEnabled(true);
			}
		}

		public void removeUpdate(DocumentEvent e) {
			if (Program.getCollection().toString().isEmpty()) {
				saveButton.setEnabled(false);
				testButton.setEnabled(false);
			}
		}
	}

	class SaveFile implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser;
			int returnValue;

			fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt"));
			fileChooser.setAcceptAllFileFilterUsed(false);
			returnValue = fileChooser.showSaveDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				try {
					Program.saveCollection(fileChooser.getSelectedFile().toString());
					JOptionPane.showMessageDialog(null, "Saved your collection successfully", "Success",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException error) {
					JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

	class LoadFile implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser;
			int returnValue;

			fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt"));
			fileChooser.setAcceptAllFileFilterUsed(false);
			returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				try {
					Program.loadCollection(fileChooser.getSelectedFile());
					viewArea.setText(Program.getCollection().toString());
				} catch (Exception error) {
					JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

	class EditCollection implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			MainFrame.addPanel(new EditCollectionPanel());
		}
	}

	class OpenTestPanel implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int manualQuestionAmount, autoQuestionAmount, option, maxSelection;
			Vector<Object> parts;
			Manager collection;
			boolean checkBoxClicked;

			collection = Program.getCollection();
			manualQuestionAmount = collection.getQuestions().size();
			autoQuestionAmount = collection.correctQuestions();
			parts = createOptionPanelParts(manualQuestionAmount, autoQuestionAmount);
			option = JOptionPane.showConfirmDialog(null, parts.toArray(), "Question's Amount",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (option == JOptionPane.OK_OPTION) {
				try {
					maxSelection = Integer.parseInt(((JFormattedTextField) (parts.get(1))).getText());
					if (maxSelection >= 1) {
						checkBoxClicked = ((JCheckBox) (parts.get(2))).isSelected();
						if (checkBoxClicked && maxSelection <= autoQuestionAmount)
							createAutoTest(maxSelection);
						else if (!checkBoxClicked && maxSelection <= manualQuestionAmount)
							MainFrame.addPanel(new CreateManualTestPanel(maxSelection));
						else
							throw new Exception();
					} else
						throw new Exception();
				} catch (IOException error) {
					JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (NumberFormatException error) {
					JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (Exception error) {
					JOptionPane.showMessageDialog(null, "Not within range", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		public Vector<Object> createOptionPanelParts(int manualQuestionAmount, int autoQuestionAmount) {
			String optionAuto, optionManual;
			JFormattedTextField textField;
			JCheckBox checkBox;
			JLabel questionLine;
			Vector<Object> parts;

			optionManual = "Enter how many questions in your test (max = " + manualQuestionAmount + ") : ";
			optionAuto = "Enter how many questions in your test (max = " + autoQuestionAmount + ") : ";
			checkBox = new JCheckBox("Auto");
			if (autoQuestionAmount == 0)
				checkBox.setEnabled(false);
			questionLine = new JLabel(optionManual);
			textField = new JFormattedTextField();
			checkBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					textField.setValue(null);
					if (checkBox.isSelected())
						questionLine.setText(optionAuto);
					else
						questionLine.setText(optionManual);
				}
			});
			parts = new Vector<Object>();
			parts.add(questionLine);
			parts.add(textField);
			parts.add(checkBox);
			return parts;
		}
	}

	public void createAutoTest(int maxSelection) throws IOException {
		JFileChooser fileChooser;
		Test test;
		int returnValue;

		test = new Test(0);
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		returnValue = fileChooser.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			test.createAutomaticTest(Program.getCollection().getQuestions(), maxSelection);
			Program.saveTest(test, fileChooser.getSelectedFile().toString());
			JOptionPane.showMessageDialog(null, "Saved your Test successfully", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	class ExitProgram implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}
