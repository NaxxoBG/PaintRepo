package au.view;

import au.controller.DrawingEngine;

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
        if (!Objects.isNull(this.commands) && !this.commands.isEmpty()) {
            System.out.println("Editor pane text: " + this.commands);
            try {
                BufferedImage img = DrawingEngine.drawSyntaxTree(this.commands, this.getWidth(), this.getHeight());
                g.translate(0, this.getHeight());
                ((Graphics2D) g).scale(1, -1);
                g.drawImage(img, 0, 0, this);
            } catch (ArrayIndexOutOfBoundsException e) {
                this.parent.reportErrors(e.getMessage());
            }

        }
    }
}
