package gui.functionalAreas.subtitleAreas;


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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import defaults.Defaults;
import gui.VideoControlArea;
import gui.functionalAreas.AbstractFunctionalArea;
import gui.functionalAreas.workers.AddSubtitlesWorker;

/**
 * This class represents the basic add subtitles pane. It contains the create method for painting this pane,
 * allowing for a file to be selected and the operation to be carried out. This operation is carried out
 * by grabbing the values of the fields of AdvancedAddSubtitlesArea and passing them into a worker with fields
 * from this class and the player. Upon completion error/success is reported via processWorkerResults.
 * @author fsta657
 *
 */
@SuppressWarnings("serial")
public class BasicAddSubtitlesArea extends AbstractFunctionalArea implements
		ActionListener {

	private JFileChooser _fileChooser;
	private JButton _replace;
	private JTextField _currentFile;
	private JButton _choose;
	private JButton _cancel;
	private JProgressBar _progressBar;
	// Worker Fields
	private AddSubtitlesWorker _worker;
	// Boolean Fields
	private boolean _canAdd = true;
	// Reference Fields
	private AdvancedAddSubtitlesArea _as;

	public BasicAddSubtitlesArea(AdvancedAddSubtitlesArea as) {
		super();
		_as = as;
	}

	@Override
	protected JPanel createAreaSpecific() {

		// Set up file chooser
		_fileChooser = new JFileChooser();
		_fileChooser.addActionListener(this);

		// Set up area title
		JLabel areaTitle = new JLabel("Basic Add Subtitles");
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
		// Combine into sub panel
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
		p1.add(label3);
		p1.add(_choose);
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
		// Combine into show file panel
		JLabel filePanel = new JLabel();
		filePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 45, 0));
		filePanel.add(p1);
		filePanel.add(_currentFile);
		filePanel.setOpaque(false);

		// Set up progress panel
		// Create add button
		_replace = new JButton("Add");
		_replace.setOpaque(false);
		_replace.setFont(Defaults.DefaultButtonFont);
		_replace.setAlignmentX(Component.CENTER_ALIGNMENT);
		_replace.addActionListener(this);
		_replace.setBackground(Defaults.DefaultGoButtonColour);
		// Create label
		JLabel label4 = new JLabel("Progress:");
		label4.setBackground(Defaults.DefaultDownloadColour);
		label4.setFont(Defaults.DefaultLabelFont);
		label4.setForeground(Defaults.DefaultWritingColour);
		label4.setHorizontalAlignment(JLabel.CENTER);
		// Create progress bar
		_progressBar = new JProgressBar();
		_progressBar.setForeground(Defaults.DefaultLoadColour);
		_progressBar.setBackground(Defaults.DefaultProgressColour);
		_progressBar.setPreferredSize(new Dimension(
				Defaults.DefaultFuncAreaWidth - 300, 30));
		// Create cancel button
		_cancel = new JButton("Cancel");
		_cancel.setOpaque(false);
		_cancel.setFont(Defaults.DefaultButtonFont);
		_cancel.setAlignmentX(Component.CENTER_ALIGNMENT);
		_cancel.addActionListener(this);
		_cancel.setEnabled(false);
		// Combine into progress panel
		JLabel progressPanel = new JLabel();
		progressPanel.setLayout(new FlowLayout());
		progressPanel.add(_replace);
		progressPanel.add(label4);
		progressPanel.add(_progressBar);
		progressPanel.add(_cancel);
		progressPanel.setOpaque(false);

		// Add panels
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(Defaults.DefaultFuncAreaWidth,
				Defaults.DefaultFuncAreaHeight));
		panel.setLayout(new GridLayout(4, 1, 0, 0));

		panel.add(areaTitle);
		panel.add(filePanel);
		panel.add(progressPanel);

		return panel;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// If source was add button, start add
		if (e.getSource().equals(_replace)) {
			if (_canAdd) {
				String inVid;
				if (_currentFile.getText().equals("No file selected")) {
					JOptionPane.showMessageDialog(null,
							"No subtitles file selected to add", "VAMIX Error",
							JOptionPane.ERROR_MESSAGE);
				} else if ((inVid = VideoControlArea.getPath()).equals("none")) {
					JOptionPane.showMessageDialog(null,
							"No video file currently selected", "VAMIX Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (_as.getOutputName().trim().equals("")){
					JOptionPane.showMessageDialog(this,
							"Please enter an output file name", "VAMIX Warning",
							JOptionPane.ERROR_MESSAGE);
				}else {
					String outVideo;
					String ext = ".mkv";
					if (_as.getOutputName().trim().equals(
							"Enter in new output file name")
							|| _as.getOutputName().equals("")) {
						outVideo = "output" + ext;
					} else {
						String[] arr = _as.getOutputName().trim().split("\\.");
						outVideo = arr[0] + ".mkv";
					}
					if (!(_as.getOutputLocation()
							.equals("No location selected"))) {
						outVideo = _as.getOutputLocation() + "/" + outVideo;
					}
					File f = new File(outVideo);
					File q = new File(VideoControlArea.getPath());
					if (q.getAbsolutePath().equals(f.getAbsolutePath())){
						JOptionPane.showMessageDialog(null,
								"Input cannot have the same name as output, please rename.",
								"VAMIX Error", JOptionPane.ERROR_MESSAGE);
					}else{
					if (f.exists()){
						Object[] options = { "OK", "Cancel" };
						int selected = JOptionPane
								.showOptionDialog(
										null,
										"Warning: the file to be created will overwrite an existing file. Continue?",
										"Overwrite warning",
										JOptionPane.DEFAULT_OPTION,
										JOptionPane.WARNING_MESSAGE, null,
										options, options[0]);

						if (selected == 0) {
							// Ready to rock
							_canAdd = false;
							_progressBar.setIndeterminate(true);
							_replace.setEnabled(false);
							_cancel.setEnabled(true);
							_worker = new AddSubtitlesWorker(inVid,
									_currentFile.getText(), outVideo, this);
							_worker.execute();
						}
					}else{
						// Ready to rock
						_canAdd = false;
						_progressBar.setIndeterminate(true);
						_replace.setEnabled(false);
						_cancel.setEnabled(true);
						_worker = new AddSubtitlesWorker(inVid,
								_currentFile.getText(), outVideo, this);
						_worker.execute();
					}
					}
				}
			}
		}
		// If source is button to open file chooser, open the chooser
		else if (e.getSource().equals(_choose)) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					".ass subtitle files", "ass");
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
				_currentFile.setText(_fileChooser.getSelectedFile().getPath());
				VideoControlArea.location = _fileChooser.getSelectedFile().getPath();
			}
		}else if (e.getSource().equals(_cancel)) {
			_worker.cancel(true);
		}

	}

	public void processWorkerResults(int exitStatus) {
		_progressBar.setIndeterminate(false);
		if (exitStatus == 9001){
			JOptionPane.showMessageDialog(null, "Adding subtitles successfully cancelled",
					"VAMIX Success", JOptionPane.INFORMATION_MESSAGE);
		}
	else if (exitStatus != 0) {
			JOptionPane.showMessageDialog(null,
					"Adding subtitles failed due to system error",
					"VAMIX Error", JOptionPane.ERROR_MESSAGE);
		} else {
			// Otherwise success
			JOptionPane.showMessageDialog(null, "Subtitles successfully added",
					"VAMIX Success", JOptionPane.INFORMATION_MESSAGE);
		}
		// Enable adding
		_canAdd = true;
		_replace.setEnabled(true);
		_cancel.setEnabled(false);
	}

}
