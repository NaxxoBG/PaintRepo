package paintui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MainWindow
{
	private JFrame frame;
	private JTextPane txtPaneError;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				MainWindow window = new MainWindow();
				window.frame.setVisible(true);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the application.
	 */
	private MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1100, 600);
		frame.setTitle("GraphR");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(1000, 550));

		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);

		JTextArea scriptEditor = new JTextArea();
		scriptEditor.setFont(new Font("Monospaced", Font.PLAIN, 14));
		springLayout.putConstraint(SpringLayout.NORTH, scriptEditor, 10, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scriptEditor, -108, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scriptEditor, -10, SpringLayout.EAST, frame.getContentPane());

		scriptEditor.setLineWrap(true);
		scriptEditor.setWrapStyleWord(true);

		frame.getContentPane().add(scriptEditor);

		PanelCanvas panelCanvas = new PanelCanvas();
		springLayout.putConstraint(SpringLayout.WEST, scriptEditor, 13, SpringLayout.EAST, panelCanvas);
		springLayout.putConstraint(SpringLayout.EAST, panelCanvas, 729, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, panelCanvas, 10, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, panelCanvas, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panelCanvas, -108, SpringLayout.SOUTH, frame.getContentPane());
		panelCanvas.setBackground(Color.WHITE);
		frame.getContentPane().add(panelCanvas);

		JButton btnDraw = new JButton("Draw");
		btnDraw.setFocusable(false);
		btnDraw.addActionListener(e ->
				SwingUtilities.invokeLater(() -> {
					String editorText = scriptEditor.getText();
					if (!Objects.isNull(editorText) && !editorText.isEmpty()) {
						panelCanvas.setCommands(editorText);
						panelCanvas.repaint();
					}
				})
		);

		springLayout.putConstraint(SpringLayout.NORTH, btnDraw, 35, SpringLayout.SOUTH, scriptEditor);
		springLayout.putConstraint(SpringLayout.SOUTH, btnDraw, -26, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnDraw, -41, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(btnDraw);

		txtPaneError = new JTextPane();
		springLayout.putConstraint(SpringLayout.NORTH, txtPaneError, 16, SpringLayout.SOUTH, panelCanvas);
		springLayout.putConstraint(SpringLayout.WEST, txtPaneError, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, txtPaneError, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, txtPaneError, -229, SpringLayout.EAST, frame.getContentPane());
		txtPaneError.setFont(new Font("Tahoma", Font.PLAIN, 14));
		springLayout.putConstraint(SpringLayout.WEST, btnDraw, 49, SpringLayout.EAST, txtPaneError);
		txtPaneError.setEditable(false);
		txtPaneError.setBackground(SystemColor.menu);
		frame.getContentPane().add(txtPaneError);
	}

	public void reportErrors(String errors) {
		this.txtPaneError.setText(errors);
	}
}
