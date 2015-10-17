package com.myselia.myapp.arduinosensorplant.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BarChart extends JPanel{
	
	public static JFrame frame = new JFrame();;
	
	private Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
	private int[] bars = new int[4];

	public void update(int one, int two, int three, int four){
		removeAll();
		updateUI();
		bars[0] = one;
		bars[1] = two;
		bars[2] = three;
		bars[3] = four;
		
		repaint();
		
	}

	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		int max = 100;

		int width = (getWidth() / bars.length) - 2;

		int x = 1;

		for (int i = 0; i < 4; i++){
			int value = bars[i];
			int height = (int)((getHeight()-5) * ((double)value / max));
			
			g.setColor(colors[i]);
			g.fillRect(x, getHeight() - height, width, height);
			g.setColor(Color.black);
			g.drawRect(x, getHeight() - height, width, height);
			x += (width + 2);
		}
	}

	@Override
    public Dimension getPreferredSize() {
		return new Dimension(bars.length * 10 + 2, 50);
	}

	public void setFrame() {
		BarChart.frame.getContentPane().add(this);
		BarChart.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BarChart.frame.pack();
		BarChart.frame.setSize(600, 600);
		BarChart.frame.setLocationRelativeTo(null);
		BarChart.frame.setVisible(true);
		
	}
}