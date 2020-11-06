package GUI;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	private static CardLayout cardLayout;
	private static JPanel mainPanel;

	public MainFrame() {
		setTitle("Mini Tester");
		setSize(450, 500);
		setMinimumSize(new Dimension(450,500));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (mainPanel.getComponentCount() <= 1)
					setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				else
					switchBackPanel();
			}
		});
		mainPanel = new JPanel();
		cardLayout = new CardLayout();
		mainPanel.setLayout(cardLayout);
		add(mainPanel);
		addPanel(new MainMenuPanel());
	}

	public static void addPanel(JPanel panel) {
		String name;

		name = panel.getClass().getSimpleName();
		mainPanel.add(panel, name);
		cardLayout.show(mainPanel, name);
	}

	public static void switchBackPanel() {
		int lastPanel;

		lastPanel = mainPanel.getComponentCount() - 1;
		mainPanel.remove(lastPanel);
		lastPanel--;
		cardLayout.show(mainPanel, mainPanel.getComponent(lastPanel).getClass().getSimpleName());
	}
}