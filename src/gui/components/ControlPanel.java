package gui.components;

import edu.hws.jcm.data.Expression;
import edu.hws.jcm.data.Parser;
import edu.hws.jcm.data.SimpleFunction;
import edu.hws.jcm.data.Variable;
import edu.hws.jcm.draw.DisplayCanvas;
import edu.hws.jcm.draw.Graph1D;
import exceptions.NoRootException;
import logic.Answer;
import logic.CustomSimpleFunction;
import logic.NonLinearEquationsSolver;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ControlPanel extends JPanel {

    private Parser parser;
    private Variable varX;
    private Expression exp;
    private CustomSimpleFunction func;
    private boolean isExpValid = false, isLeftBorderValid = false, isRightBorderValid = false, isAccValid = false;
    private double leftBorder, rightBorder, acc;
    private Methods selectedMethod = Methods.NONE;
    private static Border greenBorder = new LineBorder(Color.GREEN);
    private static Border redBorder = new LineBorder(Color.RED);
    private JTextArea output;
    private DisplayCanvas canvas;
    private Graph1D graph;
    public ControlPanel(DisplayCanvas canv, Graph1D graphic) {
        super();
        this.canvas = canv;
        this.graph = graphic;
        parser = new Parser(Parser.STANDARD_FUNCTIONS);
        varX = new Variable("x");
        parser.add(varX);
        this.setLayout(new GridLayout(9, 1));
        JLabel expLabel = new JLabel("Уравнение");
        JTextField expTextField = new JTextField();
        JLabel methodLabel = new JLabel("Способ решения");
        JButton firstMethodButton = new JButton("Метод половинного деления");
        JButton secMethodButton = new JButton("Метод Ньютона");
        JButton thirdMethodButton = new JButton("Метод итераций");
        JPanel methodPanel = new JPanel();
        methodPanel.setVisible(false);
        methodPanel.setLayout(new GridLayout(3, 3));
        methodPanel.add(new JLabel("Левая граница"));
        methodPanel.add(new JLabel("Правая граница"));
        methodPanel.add(new JLabel("Точность"));
        JTextField leftBorderTextField = new JTextField();
        JTextField rightBorderTextField = new JTextField();
        JTextField accTextField = new JTextField();
        JButton solveButton = new JButton("Решить");
        methodPanel.add(leftBorderTextField);
        methodPanel.add(rightBorderTextField);
        methodPanel.add(accTextField);
        methodPanel.add(solveButton);

        JLabel outputLabel = new JLabel("Вывод");
        output = new JTextArea();
        output.setEditable(false);
        this.add(expLabel);
        this.add(expTextField);
        this.add(methodLabel);
        this.add(firstMethodButton);
        this.add(secMethodButton);
        this.add(thirdMethodButton);
        this.add(methodPanel);
        this.add(outputLabel);
        this.add(output);
        ActionListener methodButtonsAL = e -> {
            methodPanel.setVisible(isExpValid);
            firstMethodButton.setBackground(null);
            secMethodButton.setBackground(null);
            thirdMethodButton.setBackground(null);
            if (isExpValid) ((JButton)e.getSource()).setBackground(Color.GREEN);
        };
        firstMethodButton.addActionListener(methodButtonsAL);
        secMethodButton.addActionListener(methodButtonsAL);
        thirdMethodButton.addActionListener(methodButtonsAL);
        firstMethodButton.addActionListener(e -> {
            if(isExpValid) selectedMethod = Methods.MID_DIVISION;
            else selectedMethod = Methods.NONE;
        });
        secMethodButton.addActionListener(e -> {
            if(isExpValid) selectedMethod = Methods.NEWTON;
            else selectedMethod = Methods.NONE;
        });
        thirdMethodButton.addActionListener(e -> {
            if(isExpValid) selectedMethod = Methods.SIMPLE_ITERATIONS;
            else selectedMethod = Methods.NONE;
        });
        expTextField.addActionListener(e -> expTextChanged(expTextField, methodPanel));
        expTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                expTextField.setBorder(null);
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                expTextChanged(expTextField, methodPanel);
            }
        });
        leftBorderTextField.addActionListener(e -> leftBorderTextChanged(leftBorderTextField));
        leftBorderTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                leftBorderTextField.setBorder(null);
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                leftBorderTextChanged(leftBorderTextField);
            }
        });
        rightBorderTextField.addActionListener(e -> rightBorderTextChanged(rightBorderTextField));
        rightBorderTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                rightBorderTextField.setBorder(null);
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                rightBorderTextChanged(rightBorderTextField);
            }
        });
        accTextField.addActionListener(e -> accTextChanged(accTextField));
        accTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                accTextField.setBorder(null);
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                accTextChanged(accTextField);
            }
        });
        solveButton.addActionListener(e -> solve());
    }

    private void solve()  {
        if(isLeftBorderValid && isRightBorderValid && isAccValid && isExpValid){
            func = new CustomSimpleFunction(exp, varX);
            Answer answer = null;
            try {
                switch(selectedMethod){
                    case MID_DIVISION:
                        answer = NonLinearEquationsSolver.solveWithMiddleDivision(func, leftBorder, rightBorder, acc);
                        break;
                    case NEWTON:
                        answer = NonLinearEquationsSolver.solveWithNewtonMethod(func, leftBorder, rightBorder, acc);
                        break;
                    case SIMPLE_ITERATIONS:
                        answer = NonLinearEquationsSolver.solveWithIterationMethod(func, leftBorder, rightBorder, acc);
                        break;
                    case NONE:
                        new ToastMessage("Метод не выбран", 3000);
                        break;
                }
                if(answer != null) {
                    output.setText("X = " + (Math.round(answer.x * 1000.0) / 1000.0) +
                            "\nf(X) = " + Math.round(answer.fX * 1000.0) / 1000.0 +
                            "\nN = " + answer.n);
                    graph.setFunction(func);
                    canvas.getCoordinateRect().setLimits(leftBorder - 3, rightBorder + 3, -20, 20);
                }else{
                    new ToastMessage("Несколько корней", 3000).setVisible(true);
                }
            } catch (Exception e) {
                new ToastMessage(e.getMessage(), 3000);
            }
        }else{
            new ToastMessage("Введите все данные корректно", 3000).setVisible(true);
        }
    }

    private void expTextChanged(JTextField expTextField, JPanel methodPanel){
        try {
            exp = parser.parse(expTextField.getText());
            func = new CustomSimpleFunction(exp, varX);
            isExpValid = true;
            expTextField.setBorder(greenBorder);
            graph.setFunction(func);
            canvas.doRedraw();
        } catch (Exception ex) {
            new ToastMessage("Неверно введено уравнение", 3000).setVisible(true);
            isExpValid = false;
            expTextField.setBorder(redBorder);
            methodPanel.setVisible(false);
        }
    }

    private void leftBorderTextChanged(JTextField leftBorderTextField){
        try {
            leftBorder = Double.parseDouble(leftBorderTextField.getText());
            isLeftBorderValid = true;
            leftBorderTextField.setBorder(greenBorder);
        } catch (NumberFormatException ex) {
            new ToastMessage("Неверно введена левая граница", 3000).setVisible(true);
            isLeftBorderValid = false;
            leftBorderTextField.setBorder(redBorder);
        }
    }

    private void rightBorderTextChanged(JTextField rightBorderTextField){
        try {
            rightBorder = Double.parseDouble(rightBorderTextField.getText());
            isRightBorderValid = true;
            rightBorderTextField.setBorder(greenBorder);
        } catch (NumberFormatException ex) {
            new ToastMessage("Неверно введена правая граница", 3000).setVisible(true);
            isRightBorderValid = false;
            rightBorderTextField.setBorder(redBorder);
        }
    }

    private void accTextChanged(JTextField accTextField){
        try {
            acc = Double.parseDouble(accTextField.getText());
            isAccValid = true;
            accTextField.setBorder(greenBorder);
        } catch (NumberFormatException ex) {
            new ToastMessage("Неверно введена точность", 3000).setVisible(true);
            isAccValid = false;
            accTextField.setBorder(redBorder);
        }
    }
    private enum Methods{
        MID_DIVISION,
        NEWTON,
        SIMPLE_ITERATIONS,
        NONE
    }
}
