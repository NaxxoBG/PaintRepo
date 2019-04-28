package paintui;

import engine.DrawingEngine$;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class PanelCanvas extends JPanel {

    private String commands;

    PanelCanvas() {
        this.commands = null;
    }

    void setCommands(String commands) {
        this.commands = commands;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!Objects.isNull(this.commands) && !this.commands.isEmpty()) {
            System.out.println("Editor pane text: " + this.commands);
            BufferedImage img = DrawingEngine$.MODULE$.drawSyntaxTree(this.commands);
            g.drawImage(img, 0, 0, this);

        }
    }
}
