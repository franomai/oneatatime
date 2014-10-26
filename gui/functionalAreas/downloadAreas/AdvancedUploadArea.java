package gui.functionalAreas.downloadAreas;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;


import defaults.Defaults;
import gui.functionalAreas.AbstractFunctionalArea;
import gui.functionalAreas.subtitleAreas.ForcedListSelectionModel;
import gui.functionalAreas.workers.GetCreationsWorker;


/**
 * This class represents the advanced upload pane. This class is unique in that
 * it does not rely on and is not used by any other classes. Rather, this pane
 * has a sole button that opens another window. From this window the current
 * user Creations can be loaded in from a file, and this loading in and
 * downloading of said file is handled by the GetCreationsWorker.
 * 
 * @author fsta657
 * 
 */
@SuppressWarnings("serial")
public class AdvancedUploadArea extends AbstractFunctionalArea implements
		ActionListener {

	private JButton _see;

	// Worker Fields
	private static GetCreationsWorker ohsnap;
	public static ArrayList<Creation> list = new ArrayList<Creation>();
	public static OtherTable model = new OtherTable(list);
	private static JFrame Sasles;
	public static JButton btnDelete;
	public static int isRun = 0;

	// Boolean Fields
	private boolean _notBusy = true;

	public AdvancedUploadArea() {
		initViewPane();
	}

	protected static void initViewPane() {
		Sasles = new JFrame("Current videos");
		Sasles.setSize(453, 230);
		Sasles.setTitle("Current user videos");
		Sasles.setResizable(false);
		Sasles.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		Sasles.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Links to others creations!");
		lblNewLabel.setFont(Defaults.DefaultLabelFont);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(120, 11, 200, 24);
		panel.add(lblNewLabel);
		final JTable table = new JTable(model);
		table.setSelectionModel(new ForcedListSelectionModel());
		table.setBounds(37, 46, 368, 112);
		JScrollPane pane = new JScrollPane(table);
		pane.setLocation(28, 45);
		pane.setSize(385, 106);
		panel.add(pane);

		btnDelete = new JButton("Refresh");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				list.clear();
				model.fireTableDataChanged();
				ohsnap = new GetCreationsWorker();
				ohsnap.execute();
				isRun = 1;
				btnDelete.setEnabled(false);
			}
		});
		btnDelete.setBounds(175, 168, 89, 23);
		panel.add(btnDelete);
	}

	@Override
	protected JPanel createAreaSpecific() {

		// Set up area title
		JLabel areaTitle = new JLabel("Advanced Upload");
		areaTitle.setFont(Defaults.DefaultTitleFont);
		areaTitle.setForeground(Defaults.DefaultWritingColour);
		areaTitle.setHorizontalAlignment(JLabel.CENTER);
		areaTitle.setOpaque(false);
		// Create label
		JLabel label4 = new JLabel(
				"See what other VAMIX users have been making");
		label4.setBackground(Defaults.DefaultDownloadColour);
		label4.setFont(Defaults.DefaultLabelFont);
		label4.setForeground(Defaults.DefaultWritingColour);
		label4.setHorizontalAlignment(JLabel.CENTER);
		// Create view button
		_see = new JButton("here!");
		_see.setOpaque(false);
		_see.setFont(Defaults.DefaultButtonFont);
		_see.setAlignmentX(Component.CENTER_ALIGNMENT);
		_see.addActionListener(this);

		// Combine elements
		JLabel progressPanel = new JLabel();
		progressPanel.setLayout(new FlowLayout());
		progressPanel.add(label4);
		progressPanel.add(_see);
		progressPanel.setOpaque(false);

		// Add panels
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(Defaults.DefaultFuncAreaWidth,
				Defaults.DefaultFuncAreaHeight));
		panel.setLayout(new GridLayout(4, 1, 0, 0));

		panel.add(areaTitle);
		panel.add(progressPanel);

		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// If source was the only button on the pane, open the window or bring it to the front.
		if (e.getSource().equals(_see)) {
			if (_notBusy) {
				if (!Sasles.isVisible()) {
					Sasles.setVisible(true);
				} else {
					Sasles.toFront();
				}
			}
		}
	}

	public void processWorkerResults(int exitStatus) {
		// Not needed in this.
	}

}
