package calculator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private String title = "Basic MDAS Calculator";
	private int currentNumber;
    private JLabel displayLabel = new JLabel(String.valueOf(currentNumber), JLabel.RIGHT);
    private JPanel panel = new JPanel();
    
    private boolean isClear = true;
    
    final String[] ops = new String[] {"+", "-", "x", "/"};
    
    private ArrayList<Integer> numHistory = new ArrayList<Integer>();
    private ArrayList<String> opHistory = new ArrayList<String>();
    

	public GUI() {
        setPanel();
        setFrame();
	}
	
	private void setFrame() {
        this.setTitle(title);
        this.add(panel, BorderLayout.CENTER);
        this.setBounds(10,10,300,700); 
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		for (int i = 0; i < 10; i++) {
			final int num = i;
			
		    JButton button = new JButton( new AbstractAction(String.valueOf(i)) { 
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					if (!isClear) {
						currentNumber = 0;
						isClear = true;
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
		for (int i = 0; i < ops.length; i++) {
			
			final String op = ops[i];
			
		    JButton button = new JButton( new AbstractAction(op) { 
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					numHistory.add(currentNumber);
					currentNumber = 0;
					
					opHistory.add(op);
					displayLabel.setText(op);
				}
		    });
		    
			panel.add(button);
		}
		
		// =
	    JButton button = new JButton( new AbstractAction("=") { 
			private static final long serialVersionUID = 1L;
			private int i;

			@Override
			public void actionPerformed(ActionEvent e) {	
				// Display result
				numHistory.add(currentNumber);
				
				while (opHistory.size() > 0) {
					
					if (opHistory.contains("x")) {
						i = opHistory.indexOf("x");
						numHistory.set(i, numHistory.get(i) * numHistory.get(i+1));
					} else if (opHistory.contains("/")) {
						i = opHistory.indexOf("/");
						numHistory.set(i, numHistory.get(i) / numHistory.get(i+1));
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
				
				displayLabel.setText(String.valueOf(numHistory.get(0)));
				
				currentNumber = numHistory.get(0);
				numHistory.clear();
				
				if (isClear) {
					isClear = false;
				}
			}
	    });
	    
		panel.add(button);
	}

	public static void main(String[] args) {
		new GUI();
	}

}
