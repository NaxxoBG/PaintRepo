package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

public class MainWindow
{

   private JFrame frame;

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               MainWindow window = new MainWindow();
               window.frame.setVisible(true);
            }
            catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

   /**
    * Create the application.
    */
   public MainWindow() {
      initialize();
   }

   /**
    * Initialize the contents of the frame.
    */
   private void initialize() {
      frame = new JFrame();
      frame.setBounds(100, 100, 1000, 600);
      frame.setTitle("GraphR");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setMinimumSize(new Dimension(1000, 550));

      SpringLayout springLayout = new SpringLayout();
      frame.getContentPane().setLayout(springLayout);

      JTextArea scriptEditor = new JTextArea();
      springLayout.putConstraint(SpringLayout.NORTH, scriptEditor, 10, SpringLayout.NORTH, frame.getContentPane());
      springLayout.putConstraint(SpringLayout.SOUTH, scriptEditor, -108, SpringLayout.SOUTH, frame.getContentPane());
      springLayout.putConstraint(SpringLayout.EAST, scriptEditor, -10, SpringLayout.EAST, frame.getContentPane());
      
      scriptEditor.setLineWrap(true);
      scriptEditor.setWrapStyleWord(true);
      
      frame.getContentPane().add(scriptEditor);

      JButton btnDraw = new JButton("Draw");
      springLayout.putConstraint(SpringLayout.NORTH, btnDraw, 35, SpringLayout.SOUTH, scriptEditor);
      springLayout.putConstraint(SpringLayout.SOUTH, btnDraw, -26, SpringLayout.SOUTH, frame.getContentPane());
      springLayout.putConstraint(SpringLayout.EAST, btnDraw, -41, SpringLayout.EAST, frame.getContentPane());
      frame.getContentPane().add(btnDraw);

      JPanel panelCanvas = new JPanel();
      springLayout.putConstraint(SpringLayout.WEST, scriptEditor, 13, SpringLayout.EAST, panelCanvas);
      springLayout.putConstraint(SpringLayout.EAST, panelCanvas, 729, SpringLayout.WEST, frame.getContentPane());
      springLayout.putConstraint(SpringLayout.NORTH, panelCanvas, 10, SpringLayout.NORTH, frame.getContentPane());
      springLayout.putConstraint(SpringLayout.WEST, panelCanvas, 10, SpringLayout.WEST, frame.getContentPane());
      springLayout.putConstraint(SpringLayout.SOUTH, panelCanvas, -108, SpringLayout.SOUTH, frame.getContentPane());
      panelCanvas.setBackground(Color.WHITE);
      frame.getContentPane().add(panelCanvas);

      JTextPane txtpnSampleErrorHere = new JTextPane();
      springLayout.putConstraint(SpringLayout.NORTH, txtpnSampleErrorHere, 16, SpringLayout.SOUTH, panelCanvas);
      springLayout.putConstraint(SpringLayout.WEST, txtpnSampleErrorHere, 10, SpringLayout.WEST, frame.getContentPane());
      springLayout.putConstraint(SpringLayout.SOUTH, txtpnSampleErrorHere, -10, SpringLayout.SOUTH, frame.getContentPane());
      springLayout.putConstraint(SpringLayout.EAST, txtpnSampleErrorHere, -229, SpringLayout.EAST, frame.getContentPane());
      txtpnSampleErrorHere.setFont(new Font("Tahoma", Font.PLAIN, 14));
      springLayout.putConstraint(SpringLayout.WEST, btnDraw, 49, SpringLayout.EAST, txtpnSampleErrorHere);
      txtpnSampleErrorHere.setEditable(false);
      txtpnSampleErrorHere.setBackground(SystemColor.menu);
      txtpnSampleErrorHere.setText("Sample error here");
      frame.getContentPane().add(txtpnSampleErrorHere);
   }
}
