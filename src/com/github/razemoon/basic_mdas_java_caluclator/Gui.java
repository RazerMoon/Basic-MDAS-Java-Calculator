package com.github.razemoon.basic_mdas_java_caluclator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Gui extends JFrame {
	
	private String title = "Basic MDAS Calculator";
	private int currentNumber;
    private JLabel displayLabel = new JLabel(String.valueOf(currentNumber), JLabel.RIGHT);
    private JPanel panel = new JPanel();
    
    private boolean isClear = true;
    private boolean currentIsNull = true;
    private boolean doubleOperator = false;
    
    final String[] ops = new String[] {"+", "-", "x", "/"};
    
    private List<Integer> numHistory = new ArrayList<Integer>();
    private List<String> opHistory = new ArrayList<String>();
    

	public Gui() {
        setPanel();
        setFrame();
	}
	
	private void setFrame() {
        this.setTitle(title);
        this.add(panel, BorderLayout.CENTER);
        this.setBounds(10,10,500,700); 
        this.setResizable(true);
	}
	
	private void setPanel() {
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new GridLayout(0, 1));
        
        displayLabel.setFont(new Font("Verdana", Font.PLAIN, 42));
        panel.add(displayLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 0)));
        
        createButtons();
	}
	
	private void createButtons() {
		
		// 0-9
		for (int number = 0; number <= 9; number++) {
			final int num = number;
			
		    JButton button = new JButton( new AbstractAction(String.valueOf(number)) { 

				@Override
				public void actionPerformed(ActionEvent e) {
					
					
					// If somebody presses "=" and then types a number, start a new equation instead
					// of adding that number to the end like usual
					if (!isClear && !currentIsNull) {
						currentNumber = 0;
						isClear = true;
						currentIsNull = true;
					}
					
					if (currentIsNull) {
						currentIsNull = false;
					}
					
					if (currentNumber == 0) {
						currentNumber = num;
					} else {
						currentNumber = currentNumber * 10 + num;
					}
					
					displayLabel.setText(String.valueOf(currentNumber));
				}
		    });
		    
			panel.add(button);
		}
		
		// +, -, x, /
		for (String op : ops) {
			
		    JButton button = new JButton( new AbstractAction(op) { 

				@Override
				public void actionPerformed(ActionEvent e) {
					numHistory.add(currentNumber);
					currentNumber = 0;
					
					if (!currentIsNull) {
						currentIsNull = true;
					} else {
						doubleOperator = true;
					}
					
					opHistory.add(op);
					displayLabel.setText(op);
				}
		    });
		    
			panel.add(button);
		}
		
		// =
	    JButton button = new JButton( new AbstractAction("=") { 
			private int i;

			@Override
			public void actionPerformed(ActionEvent e) {			
				if (!currentIsNull && !doubleOperator) {
					// Display result
					numHistory.add(currentNumber);
					
					boolean divideByZero = false;
					
					while (opHistory.size() > 0 && !divideByZero) {
						
						if (opHistory.contains("x")) {
							i = opHistory.indexOf("x");
							numHistory.set(i, numHistory.get(i) * numHistory.get(i+1));
						} else if (opHistory.contains("/")) {
							i = opHistory.indexOf("/");
							
							if (numHistory.get(i+1) == 0) {
								divideByZero = true;
							} else {
								numHistory.set(i, numHistory.get(i) / numHistory.get(i+1));							
							}
						} else if (opHistory.contains("+")) {
							i = opHistory.indexOf("+");
							numHistory.set(i, numHistory.get(i) + numHistory.get(i+1));
						} else if (opHistory.contains("-")) {
							i = opHistory.indexOf("-");
							numHistory.set(i, numHistory.get(i) - numHistory.get(i+1));
						}
						
						opHistory.remove(i);
						numHistory.remove(i+1);
					}
					
					if (!divideByZero) {					
						displayLabel.setText(String.valueOf(numHistory.get(0)));
						currentNumber = numHistory.get(0);
						
						if (isClear) {
							isClear = false;
						}
					} else {
						displayLabel.setText("Cannot divide by zero!");
						currentNumber = 0;
						
						if (!isClear) {
							isClear = true;
						}
						
						currentIsNull = true;
					}
				} else {
					displayLabel.setText("Cannot operate on operator/nothing!");
					currentNumber = 0;
					
					if (!isClear) {
						isClear = true;
					}
					
					currentIsNull = true;
					
					doubleOperator = false;
				}
				
				numHistory.clear();
				opHistory.clear();
			}
	    });
	    
		panel.add(button);
	}

	public static void main(String[] args) {
		Gui gui = new Gui();
        gui.setVisible(true);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
