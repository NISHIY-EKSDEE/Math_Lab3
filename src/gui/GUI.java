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
import java.util.ArrayList;

public class GUI {

    public void start(){
        generateView();
    }

    private void generateView(){
        JFrame mainFrame = new JFrame("Lab 2");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        //mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        mainFrame.setSize(width / 2, height / 2);
        //mainFrame.setResizable(false);
        GridLayout gl = new GridLayout(1, 2);
        gl.setHgap(5);
        gl.setVgap(5);
        mainFrame.setLayout(gl);

        JPanel graphicPanel = new JPanel();
        graphicPanel.setLayout(new BoxLayout(graphicPanel, BoxLayout.Y_AXIS));



        Dimension prSize = new Dimension(width/2, height);
        CoordinateRect rect = new CoordinateRect(-10, 10, -20, 20);
        DisplayCanvas canvas = new DisplayCanvas(rect);
        rect.add(new Axes("X", "Y"));
        rect.add(new Panner());
        //canvas.setPreferredSize(prSize);
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
        JTextArea jta = new JTextArea("Используйте ПКМ для перемещения графика и\nколесико мыши для приближения или отдаления.");
        jta.setEditable(false);
        jta.setMaximumSize(new Dimension(width / 2, height / 8));
        graphicPanel.add(jta);
        ControlPanel controlPanel = new ControlPanel(canvas, gr);

        mainFrame.add(graphicPanel);
        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);
    }

}
