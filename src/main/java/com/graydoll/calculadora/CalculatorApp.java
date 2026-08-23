package com.graydoll.calculadora;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Interfaz gráfica de la calculadora. La lógica vive en {@link Calculator}.
 */
public class CalculatorApp extends JFrame {

    private final Calculator calculator = new Calculator();
    private final JTextField display = new JTextField("0");
    private final DecimalFormat format;

    private Double leftOperand;
    private String pendingOperation;
    private boolean startNewNumber = true;

    public CalculatorApp() {
        super("Calculadora Graydoll");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        format = new DecimalFormat("0.########", symbols);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(360, 480);
        setLocationRelativeTo(null);
        setResizable(false);

        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Segoe UI", Font.BOLD, 28));
        display.setBackground(new Color(32, 32, 32));
        display.setForeground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));

        JPanel buttons = new JPanel(new GridBagLayout());
        buttons.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        buttons.setBackground(new Color(24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(4, 4, 4, 4);

        addButton(buttons, "C", new Color(180, 70, 70), this::clear, gbc, 0, 0, 1);
        addButton(buttons, "DEL", new Color(70, 70, 80), this::backspace, gbc, 1, 0, 1);
        addButton(buttons, "/", new Color(70, 90, 140), e -> setOperation("/"), gbc, 2, 0, 1);
        addButton(buttons, "*", new Color(70, 90, 140), e -> setOperation("*"), gbc, 3, 0, 1);

        addDigit(buttons, "7", gbc, 0, 1);
        addDigit(buttons, "8", gbc, 1, 1);
        addDigit(buttons, "9", gbc, 2, 1);
        addButton(buttons, "-", new Color(70, 90, 140), e -> setOperation("-"), gbc, 3, 1, 1);

        addDigit(buttons, "4", gbc, 0, 2);
        addDigit(buttons, "5", gbc, 1, 2);
        addDigit(buttons, "6", gbc, 2, 2);
        addButton(buttons, "+", new Color(70, 90, 140), e -> setOperation("+"), gbc, 3, 2, 1);

        addDigit(buttons, "1", gbc, 0, 3);
        addDigit(buttons, "2", gbc, 1, 3);
        addDigit(buttons, "3", gbc, 2, 3);
        addButton(buttons, "+/-", new Color(50, 50, 55), this::toggleSign, gbc, 3, 3, 1);

        addDigit(buttons, "0", gbc, 0, 4);
        addButton(buttons, ".", new Color(50, 50, 55), this::decimalPressed, gbc, 1, 4, 1);
        addButton(buttons, "=", new Color(50, 130, 90), this::equalsPressed, gbc, 2, 4, 2);

        getContentPane().setBackground(new Color(24, 24, 24));
        add(display, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);
    }

    private void addDigit(JPanel parent, String digit, GridBagConstraints gbc, int x, int y) {
        addButton(parent, digit, new Color(50, 50, 55), e -> appendDigit(digit), gbc, x, y, 1);
    }

    private void addButton(JPanel parent, String text, Color background,
                           java.awt.event.ActionListener listener,
                           GridBagConstraints gbc, int x, int y, int width) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addActionListener(listener);
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        parent.add(button, gbc);
    }

    private void appendDigit(String digit) {
        if (startNewNumber) {
            display.setText(digit);
            startNewNumber = false;
            return;
        }
        String current = display.getText();
        if ("0".equals(current)) {
            display.setText(digit);
        } else {
            display.setText(current + digit);
        }
    }

    private void decimalPressed(ActionEvent event) {
        if (startNewNumber) {
            display.setText("0.");
            startNewNumber = false;
            return;
        }
        if (!display.getText().contains(".")) {
            display.setText(display.getText() + ".");
        }
    }

    private void toggleSign(ActionEvent event) {
        display.setText(format.format(-currentValue()));
        startNewNumber = false;
    }

    private void setOperation(String operation) {
        if (pendingOperation != null && !startNewNumber) {
            equalsPressed(null);
        }
        leftOperand = currentValue();
        pendingOperation = operation;
        startNewNumber = true;
    }

    private void equalsPressed(ActionEvent event) {
        if (pendingOperation == null || leftOperand == null) {
            return;
        }
        double right = currentValue();
        try {
            double result = apply(leftOperand, right, pendingOperation);
            display.setText(format.format(result));
            leftOperand = result;
            pendingOperation = null;
            startNewNumber = true;
        } catch (DivisionByZeroException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            clear(null);
        }
    }

    private double apply(double left, double right, String operation) {
        return switch (operation) {
            case "+" -> calculator.add(left, right);
            case "-" -> calculator.subtract(left, right);
            case "*" -> calculator.multiply(left, right);
            case "/" -> calculator.divide(left, right);
            default -> right;
        };
    }

    private void clear(ActionEvent event) {
        display.setText("0");
        leftOperand = null;
        pendingOperation = null;
        startNewNumber = true;
    }

    private void backspace(ActionEvent event) {
        if (startNewNumber) {
            return;
        }
        String current = display.getText();
        if (current.length() <= 1 || (current.startsWith("-") && current.length() == 2)) {
            display.setText("0");
            startNewNumber = true;
            return;
        }
        display.setText(current.substring(0, current.length() - 1));
    }

    private double currentValue() {
        return Double.parseDouble(display.getText());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculatorApp().setVisible(true));
    }
}
