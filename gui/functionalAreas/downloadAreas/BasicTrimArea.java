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
import java.io.IOException;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import defaults.Defaults;
import gui.functionalAreas.AbstractFunctionalArea;
import gui.functionalAreas.workers.TrimWorker;

/*
 BASIC:
 URL field 
 Download Button
 Cancel Button
 Error message (file exists, failed etc)
 */

public class BasicTrimArea extends AbstractFunctionalArea implements
		ActionListener {

	private JFileChooser _fileChooser;
	private JButton _trim;
	private JTextField _currentFile;
	private TimeField _startTime;
	private TimeField _stopTime;
	private JTextField _outputName;
	private JButton _choose;
	private JButton _preview;
	// Worker Fields
	private TrimWorker _worker;
	// Boolean Fields
	private boolean _canTrim = true;

	public BasicTrimArea() {
		super();
	}

	@Override
	protected JPanel createAreaSpecific() {
		
		//Set up file chooser
		_fileChooser = new JFileChooser();
		_fileChooser.addActionListener(this);

		// Set up area title
		JLabel areaTitle = new JLabel("Basic Trim");
		areaTitle.setFont(Defaults.DefaultTitleFont);
		areaTitle.setForeground(Defaults.DefaultWritingColour);
		areaTitle.setHorizontalAlignment(JLabel.CENTER);
		areaTitle.setOpaque(false);

		// Set up start panel
		JLabel label3 = new JLabel("Select a file: ");
		label3.setBackground(Defaults.DefaultDownloadColour);
		label3.setFont(Defaults.DefaultLabelFont);
		label3.setForeground(Defaults.DefaultWritingColour);
		label3.setHorizontalAlignment(JLabel.CENTER);
		// Create choose button
		_choose = new JButton("Select");
		_choose.setOpaque(false);
		_choose.setFont(Defaults.DefaultButtonFont);
		_choose.setAlignmentX(Component.CENTER_ALIGNMENT);
		_choose.addActionListener(this);
		
		_preview = new JButton("Preview");
		_preview.setOpaque(false);
		_preview.setFont(Defaults.DefaultButtonFont);
		_preview.setAlignmentX(Component.CENTER_ALIGNMENT);
		_preview.addActionListener(this);
		
		// Combine into sub panel
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
		p1.add(label3);
		p1.add(_choose);
		p1.add(_preview);
		p1.setOpaque(false);
		// Create show file area
		_currentFile = new JTextField("No file selected");
		_currentFile.setFont(Defaults.DefaultTextFieldFont);
		_currentFile.setHorizontalAlignment(JLabel.CENTER);
		_currentFile.setBackground(Defaults.DefaultTextFieldColour);
		_currentFile.setForeground(Defaults.DefaultLoadColour);
		_currentFile.addActionListener(this);
		_currentFile.setPreferredSize(new Dimension(
				Defaults.DefaultFuncAreaWidth - 340, 30));
		_currentFile.setEditable(false);
		// Combine into show file panel
		JLabel filePanel = new JLabel();
		filePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		filePanel.add(p1);
		filePanel.add(_currentFile);
		filePanel.setOpaque(false);

		// Set up trim panel
		_trim = new JButton("Trim");
		_trim.setOpaque(false);
		_trim.setFont(Defaults.DefaultButtonFont);
		_trim.setAlignmentX(Component.CENTER_ALIGNMENT);
		_trim.addActionListener(this);
		_trim.setBackground(Defaults.DefaultGoButtonColour);
		// Set output file name
		JLabel label4 = new JLabel("Output Filename: ");
		label4.setBackground(Defaults.DefaultDownloadColour);
		label4.setFont(Defaults.DefaultLabelFont);
		label4.setForeground(Defaults.DefaultWritingColour);
		label4.setHorizontalAlignment(JLabel.CENTER);
		// Create output file area
		_outputName = new JTextField("Enter output name");
		_outputName.setFont(Defaults.DefaultTextFieldFont);
		_outputName.setHorizontalAlignment(JLabel.CENTER);
		_outputName.setBackground(Defaults.DefaultTextFieldColour);
		_outputName.setForeground(Defaults.DefaultLoadColour);
		_outputName.addActionListener(this);
		_outputName.setPreferredSize(new Dimension(130, 30));
		// Combine into sub panel
		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		p2.add(label4);
		p2.add(_outputName);
		p2.setOpaque(false);
		// Set start time
		JLabel label5 = new JLabel("Start: ");
		label5.setBackground(Defaults.DefaultDownloadColour);
		label5.setFont(Defaults.DefaultLabelFont);
		label5.setForeground(Defaults.DefaultWritingColour);
		label5.setHorizontalAlignment(JLabel.CENTER);
		// Create start time area
		_startTime = new TimeField("00:00:00");
		_startTime.setFont(Defaults.DefaultButtonFont);
		_startTime.setHorizontalAlignment(JLabel.CENTER);
		_startTime.setBackground(Defaults.DefaultTextFieldColour);
		_startTime.setForeground(Defaults.DefaultLoadColour);
		_startTime.addActionListener(this);
		_startTime.setPreferredSize(new Dimension(70, 30));
		// Combine into sub panel
		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
		p3.add(label5);
		p3.add(_startTime);
		p3.setOpaque(false);
		// Set start time
		JLabel label6 = new JLabel("Duration: ");
		label6.setBackground(Defaults.DefaultDownloadColour);
		label6.setFont(Defaults.DefaultLabelFont);
		label6.setForeground(Defaults.DefaultWritingColour);
		label6.setHorizontalAlignment(JLabel.CENTER);
		// Create start time area
		_stopTime = new TimeField("00:00:00");
		_stopTime.setFont(Defaults.DefaultButtonFont);
		_stopTime.setHorizontalAlignment(JLabel.CENTER);
		_stopTime.setBackground(Defaults.DefaultTextFieldColour);
		_stopTime.setForeground(Defaults.DefaultLoadColour);
		_stopTime.addActionListener(this);
		_stopTime.setPreferredSize(new Dimension(70, 30));
		// Combine into sub panel
		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		p4.add(label6);
		p4.add(_stopTime);
		p4.setOpaque(false);
		// Combine into all sub panel
		JPanel p5 = new JPanel();
		p5.setLayout(new FlowLayout(FlowLayout.LEFT,0, 0));
		p5.add(p2);
		p5.add(p3);
		p5.add(p4);
		p5.setOpaque(false);
		// Combine into trim panel
		JLabel trimPanel = new JLabel();
		trimPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 60, 0));
		trimPanel.add(_trim);
		trimPanel.add(p5);
		trimPanel.setOpaque(false);

		// Add panels
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(Defaults.DefaultFuncAreaWidth,
				Defaults.DefaultFuncAreaHeight));
		panel.setLayout(new GridLayout(4, 1, 0, 0));

		panel.add(areaTitle);
		panel.add(filePanel);
		panel.add(trimPanel);

		return panel;
	}

	@Override
	// This method takes actions performed on the GUI and translates them into
	// events
	public void actionPerformed(ActionEvent e) {
		// If source is extract button, start extraction
		if (e.getSource().equals(_trim)) {
			// Check all required fields are filled (if not inform user of
			// issue)
			// Check chosen file has something in it
			_outputName.setText(_outputName.getText().trim());
			if (_currentFile.getText().equals("No file selected")
					|| _currentFile.getText().isEmpty()) {
				// Tell user they have to enter a file
				JOptionPane.showMessageDialog(this, "Enter a file",
						"VAMIX Warning", JOptionPane.ERROR_MESSAGE);
			}
			// Check times are the right length (Special Document handles format
			// and content)
			else if (_outputName.getText().equals("")){
				JOptionPane.showMessageDialog(this,
						"Please enter an output file name", "VAMIX Warning",
						JOptionPane.ERROR_MESSAGE);
			}
			else if (!(_startTime.getText().length() == 8 && _stopTime.getText()
					.length() == 8)) {
				// Tell user they have to enter a time
				JOptionPane.showMessageDialog(this,
						"Complete start/stop times", "VAMIX Warning",
						JOptionPane.ERROR_MESSAGE);
			}
			// Double check time formats
			else if (!_startTime.getText().matches("[0-9][0-9][:][0-9][0-9][:][0-9][0-9]")
					|| !_stopTime.getText().matches("[0-9][0-9][:][0-9][0-9][:][0-9][0-9]")) {
				// Tell user they have to enter a time correctly
				JOptionPane.showMessageDialog(this,
						"Complete start/stop times correctly", "VAMIX Warning",
						JOptionPane.ERROR_MESSAGE);
			}
			// Check output file & input file dont have the same name
			else if (this._outputName
					.getText()
					.equals(_currentFile
							.getText()
							.substring(
									_currentFile.getText().lastIndexOf("/") + 1,
									_currentFile.getText().length()).trim())) {
				// Tell user to enter new output
				JOptionPane
						.showMessageDialog(
								this,
								"Output file name equals input filename - please rename",
								"VAMIX Warning", JOptionPane.ERROR_MESSAGE);
			}

			else {
				// Prevent user from starting extraction again
				
				
				_trim.setEnabled(false);
				_canTrim=false;
				String[] arr = _currentFile.getText().split("\\.");
				String ext = "." + arr[arr.length - 1];
				String[] arr2 = _outputName.getText().split("\\.");
				// Then create worker and start process
				_worker = new TrimWorker(_currentFile.getText(),
						_startTime.getText(), _stopTime.getText(),
						arr2[0] + ext, this);
				_worker.execute();
			}
		}
		// If source is button to open file chooser, open the chooser
		else if (e.getSource().equals(_choose)) {
			FileFilter filter = new FileNameExtensionFilter("Media", "mp4", "mpeg", "mov", "wmv", "mpg", "mp3",
					"wav","mkv", "avi");
			_fileChooser.setFileFilter(filter);
			_fileChooser.showOpenDialog(this);
		}
		// If source is file chooser, update text field
		else if (e.getSource().equals(_fileChooser)) {
			if (_fileChooser.getSelectedFile() != null) {
				_currentFile.setText(_fileChooser.getSelectedFile()
						.getPath());
			}
		}
		else if (e.getSource().equals(_preview)) {
			if (_currentFile.getText().equals("No file selected")
					|| _currentFile.getText().isEmpty()) {
				// Tell user they have to enter a file
				JOptionPane.showMessageDialog(this, "Enter a file",
						"VAMIX Warning", JOptionPane.ERROR_MESSAGE);
			}else{
				ProcessBuilder builder = new ProcessBuilder("vlc", _currentFile.getText());
				try {
					builder.start();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	public void processWorkerResults(int exitStatus) {
		// If exit status is 200, user input is erroneous
		if (exitStatus == 200) {
			JOptionPane.showMessageDialog(null,
					"Extract failed due to user error", "VAMIX Error",
					JOptionPane.ERROR_MESSAGE);
		}
		// Otherwise if exit status is not 0, avconv failed
		else if (exitStatus != 0) {
			JOptionPane.showMessageDialog(null,
					"Extract failed due to system error", "VAMIX Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			// Otherwise success
			JOptionPane.showMessageDialog(null,
					"File was successfully extracted", "VAMIX Success",
					JOptionPane.INFORMATION_MESSAGE);
		}
		// Enable extraction
		_canTrim=true;
		_trim.setEnabled(true);
	}

}

@SuppressWarnings("serial")
// This special JTextField ensures its model is a special TimeFormayDocument
class TimeField extends JTextField {
	public TimeField() {
		super();
	}

	public TimeField(String s) {
		super(s);
	}

	public TimeField(int cols) {
		super(cols);
	}

	@Override
	protected Document createDefaultModel() {
		return new TimeFormatDocument();
	}

	// This Document ensures the textfield contains numbers in the format
	// NN:NN:NN
	class TimeFormatDocument extends PlainDocument {

		// Checks inserted strings are the right format
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			if (str == null) {
				return;
			}

			char[] chars = str.toCharArray();
			boolean ok = true;
			int length = this.getLength();

			if ((length == 2) || (length == 5)) {
				if (chars[0] != ':') {
					ok = false;
				}
			} else if (length > 7) {
				ok = false;
			} else {
				if (!Character.isDigit(chars[0])) {
					ok = false;
				}
			}

			if (ok)
				super.insertString(offs, new String(chars), a);

		}
	}
}
