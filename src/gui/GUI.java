package gui;

import edu.hws.jcm.data.Expression;
import edu.hws.jcm.data.Parser;
import edu.hws.jcm.data.SimpleFunction;
import edu.hws.jcm.data.Variable;
import edu.hws.jcm.draw.*;
import gui.components.ControlPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class GUI {

    public void start(){
        generateView();
    }

    private void generateView(){
        JFrame mainFrame = new JFrame("Lab 2");
        mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        mainFrame.setResizable(false);
        mainFrame.setLayout(new GridLayout(1, 2));

        JPanel graphicPanel = new JPanel();

        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        //CoordinateRect rect = new CoordinateRect(-10, 10, -30, 30);
        Dimension prSize = new Dimension(width/2, height);
        CoordinateRect rect = new CoordinateRect(-10, 10, -20, 20);
        DisplayCanvas canvas = new DisplayCanvas(rect);
        rect.add(new Axes("X", "Y"));
        rect.add(new Panner());
        canvas.setSize(prSize);
        canvas.setPreferredSize(prSize);
        rect.add(new DrawBorder());
        canvas.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
                if(mouseWheelEvent.getWheelRotation() < 0)
                    rect.zoomInOnPixel(mouseWheelEvent.getX(), mouseWheelEvent.getY());
                else rect.zoomOutFromPixel(mouseWheelEvent.getX(), mouseWheelEvent.getY());
            }
        });
        Graph1D gr = new Graph1D();
        rect.add(gr);
        graphicPanel.add(canvas);
        ControlPanel controlPanel = new ControlPanel(canvas, gr);

        mainFrame.add(graphicPanel);
        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);
    }

}
