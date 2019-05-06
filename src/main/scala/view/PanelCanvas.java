package au.view;

import au.controller.DrawingEngine;
import scala.Tuple2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class PanelCanvas extends JPanel {

    private String commands;
    private DrView parent;

    PanelCanvas(DrView parent) {
        this.commands = null;
        this.parent = parent;
    }

    void setCommands(String commands) {
        this.commands = commands;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        if (!Objects.isNull(this.commands) && !this.commands.isEmpty()) {
            System.out.println("Editor pane text: \n" + this.commands);
            try {
                Tuple2<BufferedImage, String> imgRes = DrawingEngine.drawSyntaxTree(this.commands, this.getWidth(), this.getHeight());
                g2.translate(0, this.getHeight());
                g2.scale(1, -1);
                g2.drawImage(imgRes._1, 0, 0, this);
                if (!imgRes._2.equals("No errors")) {
                    this.parent.reportErrors(imgRes._2);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                this.parent.reportErrors(e.getMessage());
            }
        g2.dispose();
        }
    }
}
