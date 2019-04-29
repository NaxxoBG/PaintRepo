package au.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DrView
{
	private JFrame frame;
	private JTextPane txtPaneError;

	/**
	 * Launch the application.
	 */
	public void launch() {
		EventQueue.invokeLater(() -> {
			try {
				DrView window = new DrView();
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
	public DrView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1100, 600);
		frame.setTitle("GraphR");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(1000, 600));

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

		PanelCanvas panelCanvas = new PanelCanvas(this);
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
						txtPaneError.setText("");
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
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.menu);
		frame.setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("File");
		mnMenu.setBackground(SystemColor.menu);
		mnMenu.setHorizontalAlignment(SwingConstants.CENTER);
		mnMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(mnMenu);
		
		JMenuItem menuItemSave = new JMenuItem("Save");
		menuItemSave.addActionListener(e -> {
			JFileChooser saveDialog = new JFileChooser();
			saveDialog.setDialogTitle("Specify a file to save");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
			saveDialog.setFileFilter(filter);
			if (saveDialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File file = saveDialog.getSelectedFile();
				if (!saveDialog.getSelectedFile().getAbsolutePath().endsWith(".png")) {
					file = new File(saveDialog.getSelectedFile() + ".png");
				}
				try {
					BufferedImage bi = new BufferedImage(panelCanvas.getWidth(), panelCanvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
					panelCanvas.paint(bi.getGraphics());
					ImageIO.write(bi, "png", new File(file.getAbsolutePath()));
				} catch (IOException ex) {
					System.out.println("Failed to save image!");
				}
				System.out.println("File saved at path: " + file.getAbsolutePath());
			}
		});
		menuItemSave.setBackground(UIManager.getColor("CheckBox.light"));
		menuItemSave.setIcon(new ImageIcon(DrView.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		menuItemSave.setSelectedIcon(null);
		mnMenu.add(menuItemSave);
	}

	public void reportErrors(String errors) {
		SwingUtilities.invokeLater(() -> {
			if(!Objects.isNull(txtPaneError)) {
				txtPaneError.setText(errors);
			}
		});
	}
}
