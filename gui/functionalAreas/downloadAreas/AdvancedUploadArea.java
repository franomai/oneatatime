package gui.functionalAreas.downloadAreas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import defaults.Defaults;
import gui.MyVLCPlayer;
import gui.VideoControlArea;
import gui.functionalAreas.AbstractFunctionalArea;
import gui.functionalAreas.subtitleAreas.ForcedListSelectionModel;
import gui.functionalAreas.subtitleAreas.Subtitle;
import gui.functionalAreas.subtitleAreas.TableModel;
import gui.functionalAreas.textAreas.AdvancedAddTextArea;
import gui.functionalAreas.workers.GetCreationsWorker;
import gui.functionalAreas.workers.OverlayWorker;
import gui.functionalAreas.workers.StreamWorker;
import gui.functionalAreas.workers.StripAudioWorker;
import gui.functionalAreas.workers.UploadWorker;

/*
 BASIC:
 URL field 
 Download Button
 Cancel Button
 Error message (file exists, failed etc)
 */

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
	private boolean _canStrip = true;
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
				JLabel label4 = new JLabel("See what other VAMIX users have been making");
				label4.setBackground(Defaults.DefaultDownloadColour);
				label4.setFont(Defaults.DefaultLabelFont);
				label4.setForeground(Defaults.DefaultWritingColour);
				label4.setHorizontalAlignment(JLabel.CENTER);
		// Set up progress panel
		// Create strip button
		_see = new JButton("here!");
		_see.setOpaque(false);
		_see.setFont(Defaults.DefaultButtonFont);
		_see.setAlignmentX(Component.CENTER_ALIGNMENT);
		_see.addActionListener(this);
		
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
		// If source was strip button, start strip
		if (e.getSource().equals(_see)) {
			if (_canStrip) {
				if (!Sasles.isVisible()) {
					Sasles.setVisible(true);
				} else {
					Sasles.toFront();
				}
				}
				}
			}


	public void processWorkerResults(int exitStatus) {

	}

}
