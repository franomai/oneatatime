package gui.functionalAreas;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import defaults.Defaults;
import gui.functionalAreas.AbstractFunctionalArea;


public class HelpArea extends AbstractFunctionalArea implements ActionListener {
	public HelpArea() {

	}

	@Override
	protected JPanel createAreaSpecific() {

		// Set up area title
		JLabel line1 = new JLabel("Welcome to VAMIX!");
		line1.setFont(Defaults.DefaultTitleFont);
		line1.setForeground(Defaults.DefaultWritingColour);
		line1.setHorizontalAlignment(JLabel.CENTER);
		line1.setOpaque(false);

		JLabel line2 = new JLabel(
				"To get started, click the button above to choose a file.");
		line2.setFont(Defaults.DefaultTextFieldFont);
		line2.setForeground(Defaults.DefaultWritingColour);
		line2.setHorizontalAlignment(JLabel.CENTER);
		line2.setOpaque(false);

		JLabel line3 = new JLabel(
				"When you're ready to start editing, choose one of the operation groups in the section pane to the left. ");
		line3.setFont(Defaults.DefaultTextFieldFont);
		line3.setForeground(Defaults.DefaultWritingColour);
		line3.setHorizontalAlignment(JLabel.CENTER);
		line3.setOpaque(false);

		JLabel line4 = new JLabel(
				"If you need help with one of the particular options press the option button associated with it, followed by shift-h.");
		line4.setFont(Defaults.DefaultTextFieldFont);
		line4.setForeground(Defaults.DefaultWritingColour);
		line4.setHorizontalAlignment(JLabel.CENTER);
		line4.setOpaque(false);

		// Add panels
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(Defaults.DefaultFuncAreaWidth,
				Defaults.DefaultFuncAreaHeight));
		panel.setLayout(new GridLayout(4, 1, 0, 0));

		panel.add(line1);
		panel.add(line2);
		panel.add(line3);
		panel.add(line4);

		return panel;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
	// Unrequired and hence left blank.
		
	}

	@Override
	public void processWorkerResults(int exitStatus) {
	// Unrequired and hence left blank.
		
	}

}
