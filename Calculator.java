import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Calculator extends JFrame {
    private JTextField textField;
    private double result = 0;
    private String operator = "";
    private boolean start = true;

    public Calculator() {
        JPanel panel = new JPanel() {
            // Set an image as the background of the panel
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage img = ImageIO.read(new File("bg.jpg"));
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        
        panel.setLayout(new GridLayout(0, 3));
        textField = new JTextField(20);
        textField.setEditable(false);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setFont(new Font("Arial", Font.BOLD, 32));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(textField, BorderLayout.NORTH);

        String[] buttonLabels = {
            "C", ".",
            "7", "8", "9",
            "4", "5", "6",
            "1", "2", "3",
            "-", "0", "+",
            "/", "=", "x"
        };

        ButtonListener buttonListener = new ButtonListener();
        int row = 1;
        int col = 0;
        
        for (String label : buttonLabels) {
        	JButton button = new JButton(label);
            button.addActionListener(buttonListener);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 32));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = col;
            gbc.gridy = row;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.ipadx = 50;
            gbc.ipady = 50;
            panel.add(button, gbc);
            col++;
            
            if (col > 3) {
                col = 0;
                row++;
            }
        }

        add(panel);
        setTitle("Calculator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    class ButtonListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
            String buttonText = ((JButton) e.getSource()).getText();

            if (buttonText.matches("[0-9\\.]")) {
                if (start) {
                    textField.setText("");
                    start = false;
                }
                textField.setText(textField.getText() + buttonText);
            } else if (buttonText.matches("[+\\-x/]")) {
                if (!operator.isEmpty() && !start) {
                    performCalculation(Double.parseDouble(textField.getText()));
                }
                operator = buttonText;
                if (result == 0) { // fix for first input being 0
                    result = Double.parseDouble(textField.getText());
                }
                textField.setText(textField.getText() + operator);
                start = true;
            } else if (buttonText.equals("=")) {
                if (!operator.isEmpty() && !start) {
                    performCalculation(Double.parseDouble(textField.getText()));
                    textField.setText(String.valueOf(result));
                    operator = "";
                    start = true;
                    result = 0; // reset result for next calculation
                }
            } else if (buttonText.equals("C")) {
                result = 0;
                operator = "";
                start = true;
                textField.setText("");
            }
        }

        private void performCalculation(double secondNum) {
            switch (operator) {
                case "+":
                    result += secondNum;
                    break;
                case "-":
                    result -= secondNum;
                    break;
                case "x":
                    result *= secondNum;
                    break;
                case "/":
                    result /= secondNum;
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new Calculator();
    }
}