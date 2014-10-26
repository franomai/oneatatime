package gui.functionalAreas.textAreas;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import defaults.Defaults;
import gui.VideoControlArea;
import gui.functionalAreas.AbstractFunctionalArea;
import gui.functionalAreas.workers.UpdateFieldsWorker;
import gui.functionalAreas.workers.WriteSaveWorker;

public class AdvancedAddTextArea extends AbstractFunctionalArea implements
		ActionListener {

	// Gui Fields
	private JFileChooser _fileChooser;
	private JFileChooser _saveChooser;
	private JComboBox<String> _font;
	private JComboBox<String> _fontSize;
	private JButton _openChooser;
	private JButton _saveState;
	private JButton _loadState;
	private JColorChooser _colourChooser;
	private ColourDisplay _displayColour;
	// Colour Fields
	private Color _currentColour = Color.BLACK;
	private JButton _locationChooser;
	private JTextField _outputLoc;
	private JTextField _saveName;
	// Reference Fields
	private BasicAddCreditsArea _bc;
	private BasicAddTextArea _bt;
	// Worker Fields
	private WriteSaveWorker _writer;
	private UpdateFieldsWorker _reader;

	// Read worker

	public AdvancedAddTextArea() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected JPanel createAreaSpecific() {

		// Set up file chooser
		_fileChooser = new JFileChooser();
		_fileChooser.addActionListener(this);
		_fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		_fileChooser.setAcceptAllFileFilterUsed(false);
		_saveChooser = new JFileChooser();
		_saveChooser.addActionListener(this);

		// Set up area title
		JLabel areaTitle = new JLabel("Advanced Add Text");
		areaTitle.setFont(Defaults.DefaultTitleFont);
		areaTitle.setForeground(Defaults.DefaultWritingColour);
		areaTitle.setHorizontalAlignment(JLabel.CENTER);
		areaTitle.setOpaque(false);

		// Set up font area
		JLabel fColLabel = new JLabel("Font colour:");
		fColLabel.setFont(Defaults.DefaultLabelFont);
		fColLabel.setForeground(Defaults.DefaultWritingColour);
		fColLabel.setHorizontalAlignment(JLabel.CENTER);
		fColLabel.setOpaque(false);

		_openChooser = new JButton("Choose Colour");
		_openChooser.setFont(Defaults.DefaultButtonFont);
		_openChooser.setForeground(Defaults.DefaultWritingColour);
		_openChooser.setOpaque(false);
		_openChooser.addActionListener(this);

		_displayColour = new ColourDisplay();
		_displayColour.setPreferredSize(new Dimension(20, 20));
		_displayColour.setBackground(_currentColour);
		_displayColour.setOpaque(true);

		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		p3.add(fColLabel);
		p3.add(_openChooser);
		p3.add(_displayColour);
		p3.setOpaque(false);

		JLabel fontLabel = new JLabel("Choose font: ");
		fontLabel.setFont(Defaults.DefaultLabelFont);
		fontLabel.setForeground(Defaults.DefaultWritingColour);
		fontLabel.setHorizontalAlignment(JLabel.CENTER);
		fontLabel.setOpaque(false);

		_font = new JComboBox<String>(Defaults.DefaultFonts);
		_font.setFont(Defaults.DefaultButtonFont);
		_font.setForeground(Defaults.DefaultWritingColour);
		_font.setOpaque(false);

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		p1.add(fontLabel);
		p1.add(_font);
		p1.setOpaque(false);

		// Add save button
		_saveState = new JButton("Save");
		_saveState.setFont(Defaults.DefaultButtonFont);
		_saveState.setForeground(Defaults.DefaultWritingColour);
		_saveState.setOpaque(false);
		_saveState.setBackground(Defaults.DefaultGoButtonColour);
		_saveState.addActionListener(this);

		// Add save name
		_saveName = new JTextField("settings");
		_saveName.setFont(Defaults.DefaultTextFieldFont);
		_saveName.setHorizontalAlignment(JLabel.CENTER);
		_saveName.setBackground(Defaults.DefaultTextFieldColour);
		_saveName.setForeground(Defaults.DefaultLoadColour);
		_saveName.addActionListener(this);
		_saveName.setPreferredSize(new Dimension(80, 30));

		// Add load button
		_loadState = new JButton("Load");
		_loadState.setFont(Defaults.DefaultButtonFont);
		_loadState.setForeground(Defaults.DefaultWritingColour);
		_loadState.setOpaque(false);
		_loadState.setBackground(Defaults.DefaultGoButtonColour);
		_loadState.addActionListener(this);

		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		p4.add(_saveState);
		p4.add(_saveName);
		p4.add(_loadState);
		p4.setOpaque(false);

		JPanel fontPanel = new JPanel();
		fontPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
		fontPanel.add(p1);
		fontPanel.add(p3);
		fontPanel.add(p4);
		fontPanel.setOpaque(false);

		// Set up output filename area
		JLabel sizeLabel = new JLabel("Choose font size: ");
		sizeLabel.setFont(Defaults.DefaultLabelFont);
		sizeLabel.setForeground(Defaults.DefaultWritingColour);
		sizeLabel.setHorizontalAlignment(JLabel.CENTER);
		sizeLabel.setOpaque(false);

		_fontSize = new JComboBox<String>(Defaults.DefaultFontSizes);
		_fontSize.setFont(Defaults.DefaultButtonFont);
		_fontSize.setForeground(Defaults.DefaultWritingColour);
		_fontSize.setOpaque(false);

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		p2.add(sizeLabel);
		p2.add(_fontSize);
		p2.setOpaque(false);

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
		JPanel p5 = new JPanel();
		p5.setLayout(new FlowLayout());
		p5.add(locLabel);
		p5.add(_locationChooser);
		p5.setOpaque(false);
		// Add show text place
		_outputLoc = new JTextField("No location selected");
		_outputLoc.setFont(Defaults.DefaultTextFieldFont);
		_outputLoc.setHorizontalAlignment(JLabel.CENTER);
		_outputLoc.setBackground(Defaults.DefaultTextFieldColour);
		_outputLoc.setForeground(Defaults.DefaultLoadColour);
		_outputLoc.addActionListener(this);
		_outputLoc.setPreferredSize(new Dimension(180, 25));
		_outputLoc.setEditable(false);
		// Combine into location panel
		JPanel locPanel = new JPanel();
		locPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		locPanel.add(p5);
		locPanel.add(_outputLoc);
		locPanel.setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
		colPanel.add(p2);
		colPanel.add(locPanel);
		colPanel.setOpaque(false);

		// Add panels
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(Defaults.DefaultFuncAreaWidth,
				Defaults.DefaultFuncAreaHeight));
		panel.setLayout(new GridLayout(4, 1, 0, 0));
		panel.add(areaTitle);
		panel.add(fontPanel);
		panel.add(colPanel);

		return panel;

	}

	public void processWorkerResults(int exitStatus) {
		_saveState.setEnabled(true);
		_loadState.setEnabled(true);
		_saveName.setEditable(true);
		if (exitStatus > 0) {
			JOptionPane.showMessageDialog(null,
					"Settings failed due to system error", "VAMIX Error",
					JOptionPane.ERROR_MESSAGE);
		} else if (exitStatus == 0) {
			// Otherwise success
			JOptionPane.showMessageDialog(null, "Settings successfully saved",
					"VAMIX Success", JOptionPane.INFORMATION_MESSAGE);
		} else if (exitStatus == -1) {
			// Otherwise success
			JOptionPane.showMessageDialog(null, "Settings successfully loaded",
					"VAMIX Success", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(_openChooser)) {
			getCurrentFontColour();
			// If colourChooser is null, open dialog
			if (_colourChooser == null) {
				// Open dialog
				_currentColour = JColorChooser.showDialog(null,
						"Choose Background Color", _currentColour);
				 //if current colour null, make black
				if (_currentColour == null){
					_currentColour = Color.black;
				}
				_displayColour.revalidate();
				_displayColour.repaint();
				_openChooser.setSelected(false);
			} else {
				// Do nothing if window alread open
			}
		}
		// If source is button to open file chooser, open the chooser
		else if (e.getSource().equals(_locationChooser)) {
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
		} else if (e.getSource().equals(_saveState)) {
			if (!this._saveName.getText().contains(".vamSets")) {
				_saveName.setText(_saveName.getText() + ".vamSets");
			}
			// Disable buttons
			_saveState.setEnabled(false);
			_loadState.setEnabled(false);
			_saveName.setEditable(false);
			_writer = new WriteSaveWorker(_bc, _bt, this);
			_writer.execute();
		} else if (e.getSource().equals(_loadState)) {
			// Disable buttons
			_saveState.setEnabled(false);
			_loadState.setEnabled(false);
			_saveName.setEditable(false);
			// Open dialog
			FileFilter filter = new FileNameExtensionFilter("VAMIX Settings",
					"vamSets");
			_saveChooser.setFileFilter(filter);
			_saveChooser.showOpenDialog(this);
			_saveState.setEnabled(true);
			_loadState.setEnabled(true);
			_saveName.setEditable(true);
		} else if (e.getSource().equals(_saveChooser)) {
			// Update fields
			if (_saveChooser.getSelectedFile() != null) {
				_reader = new UpdateFieldsWorker(_bc, _bt, this, _saveChooser
						.getSelectedFile().getPath());
				_reader.execute();
			}
			else {
				_saveState.setEnabled(true);
				_loadState.setEnabled(true);
				_saveName.setEditable(true);
			}
		}
	}

	public String getCurrentFont() {
		return _font.getSelectedItem().toString();
	}

	public String getCurrentFontSize() {
		return _fontSize.getSelectedItem().toString();
	}

	public String getCurrentFontColour() {
		int r = _currentColour.getRed();
		int g = _currentColour.getGreen();
		int b = _currentColour.getBlue();
		return String.format("#%02X%02X%02X", r, g, b);
	}

	public String getOutputLocation() {
		return _outputLoc.getText();
	}

	/*
	 * UPDATING SECTION Order of returned items is: 1) _font; 2)_fontSize; 3)R
	 * 4)G 5)B 6) _outputLoc;
	 */

	public ArrayList<String> getFields() {

		// Makes list
		ArrayList<String> list = new ArrayList<String>();
		Integer i = _font.getSelectedIndex();
		list.add(i.toString());
		i = _fontSize.getSelectedIndex();
		list.add(i.toString());
		// RGB format
		i = _currentColour.getRed();
		list.add(i.toString());
		i = _currentColour.getGreen();
		list.add(i.toString());
		i = _currentColour.getBlue();
		list.add(i.toString());
		list.add(_outputLoc.getText());

		return list;
	}

	public void setFields(ArrayList<String> list) {

		// Update fields
		_font.setSelectedIndex(Integer.parseInt(list.get(0)));
		_fontSize.setSelectedIndex(Integer.parseInt(list.get(1)));
		_currentColour = new Color(Integer.parseInt(list.get(2)),
				Integer.parseInt(list.get(3)), Integer.parseInt(list.get(4)));
		_displayColour.revalidate();
		_displayColour.repaint();
		_outputLoc.setText(list.get(5));
	}

	public void setReferences(BasicAddCreditsArea bc, BasicAddTextArea bt) {
		_bc = bc;
		_bt = bt;
	}

	public String getSaveName() {
		return _saveName.getText();
	}

	class ColourDisplay extends JLabel {

		protected void paintComponent(Graphics g) {
			setBackground(_currentColour);
			super.paintComponent(g);
		}

	}

}