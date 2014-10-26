package gui.functionalAreas.subtitleAreas;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import defaults.Defaults;
import gui.VideoControlArea;
import gui.functionalAreas.AbstractFunctionalArea;

public class AdvancedCreateSubtitles extends AbstractFunctionalArea implements
		ActionListener {

	private JFileChooser _fileChooser;
	private JTextField _outputName;
	private JButton _locationChooser;
	private JTextField _outputLoc;

	public AdvancedCreateSubtitles() {
		super();
	}

	@Override
	protected JPanel createAreaSpecific() {

		// Set up file chooser
		_fileChooser = new JFileChooser();
		_fileChooser.addActionListener(this);
		_fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		_fileChooser.setAcceptAllFileFilterUsed(false);

		// Set up area title
		JLabel areaTitle = new JLabel("Advanced Create Subtitles");
		areaTitle.setFont(Defaults.DefaultTitleFont);
		areaTitle.setForeground(Defaults.DefaultWritingColour);
		areaTitle.setHorizontalAlignment(JLabel.CENTER);
		areaTitle.setOpaque(false);

		// Set up output filename area
		JLabel outputLabel = new JLabel("Enter output filename: ");
		outputLabel.setFont(Defaults.DefaultLabelFont);
		outputLabel.setForeground(Defaults.DefaultWritingColour);
		outputLabel.setHorizontalAlignment(JLabel.CENTER);
		outputLabel.setOpaque(false);

		_outputName = new JTextField("Enter in new output file name");
		_outputName.setFont(Defaults.DefaultTextFieldFont);
		_outputName.setHorizontalAlignment(JLabel.CENTER);
		_outputName.setBackground(Defaults.DefaultTextFieldColour);
		_outputName.setForeground(Defaults.DefaultLoadColour);
		_outputName.addActionListener(this);
		_outputName.setPreferredSize(new Dimension(
				Defaults.DefaultFuncAreaWidth - 300, 25));

		JPanel outputPanel = new JPanel();
		outputPanel.setLayout(new FlowLayout());
		outputPanel.add(outputLabel);
		outputPanel.add(_outputName);
		outputPanel.setOpaque(false);

		// Set up file choosing area
		JLabel locLabel = new JLabel("Select output file location: ");
		locLabel.setFont(Defaults.DefaultLabelFont);
		locLabel.setForeground(Defaults.DefaultWritingColour);
		locLabel.setHorizontalAlignment(JLabel.CENTER);
		locLabel.setOpaque(false);
		// Create chooser button
		_locationChooser = new JButton("Choose");
		_locationChooser.setOpaque(false);
		_locationChooser.setFont(Defaults.DefaultButtonFont);
		_locationChooser.setAlignmentX(Component.CENTER_ALIGNMENT);
		_locationChooser.addActionListener(this);
		// Combine into sub panel
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		p1.add(locLabel);
		p1.add(_locationChooser);
		p1.setOpaque(false);
		// Add show text place
		_outputLoc = new JTextField("No location selected");
		_outputLoc.setFont(Defaults.DefaultTextFieldFont);
		_outputLoc.setHorizontalAlignment(JLabel.CENTER);
		_outputLoc.setBackground(Defaults.DefaultTextFieldColour);
		_outputLoc.setForeground(Defaults.DefaultLoadColour);
		_outputLoc.addActionListener(this);
		_outputLoc.setPreferredSize(new Dimension(300, 25));
		_outputLoc.setEditable(false);
		// Combine into location panel
		JPanel locPanel = new JPanel();
		locPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 55, 0));
		locPanel.add(p1);
		locPanel.add(_outputLoc);
		locPanel.setOpaque(false);

		// Combine into final panel
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(Defaults.DefaultFuncAreaWidth,
				Defaults.DefaultFuncAreaHeight));
		panel.setLayout(new GridLayout(4, 1, 3, 3));

		panel.add(areaTitle);
		panel.add(outputPanel);
		panel.add(locPanel);

		return panel;
	}

	public void processWorkerResults(int exitStatus) {
		// Not needed in this.
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// If source is button to open file chooser, open the chooser
		if (e.getSource().equals(_locationChooser)) {
			FileFilter filter = new FileFilter() {
				public boolean accept(File file) {
					return file.isDirectory();
				}

				@Override
				public String getDescription() {
					return null;
				}
			};
			_fileChooser.setFileFilter(filter);
			if (!VideoControlArea.location.equals("none")){
				File f = new File(VideoControlArea.location);
				_fileChooser.setCurrentDirectory(f);
			}
			// NEEDS GOOD TEXT
			_fileChooser.showOpenDialog(this);
		}
		// If source is file chooser, update text field
		else if (e.getSource().equals(_fileChooser)) {
			if (_fileChooser.getSelectedFile() != null) {
				_outputLoc.setText(_fileChooser.getSelectedFile().getPath());
				VideoControlArea.location = _fileChooser.getSelectedFile().getPath();
			}
		}
	}

	public String getOutputName() {
		return _outputName.getText();
	}

	public String getOutputLocation() {
		return _outputLoc.getText();
	}

}
