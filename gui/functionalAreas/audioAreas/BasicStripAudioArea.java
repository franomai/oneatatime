package gui.functionalAreas.audioAreas;

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
import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import defaults.Defaults;
import gui.MyVLCPlayer;
import gui.VideoControlArea;
import gui.functionalAreas.AbstractFunctionalArea;
import gui.functionalAreas.textAreas.AdvancedAddTextArea;
import gui.functionalAreas.workers.OverlayWorker;
import gui.functionalAreas.workers.StreamWorker;
import gui.functionalAreas.workers.StripAudioWorker;

/*
 BASIC:
 URL field 
 Download Button
 Cancel Button
 Error message (file exists, failed etc)
 */

public class BasicStripAudioArea extends AbstractFunctionalArea implements
		ActionListener {

	private JButton _cancel;
	private JProgressBar _progressBar;
	private JButton _strip;
	
	// Worker Fields
	private StripAudioWorker _worker;
	// Boolean Fields
	private boolean _canStrip = true;
	// Reference Fields
	private AdvancedStripAudioArea _as;

	public BasicStripAudioArea(AdvancedStripAudioArea as) {
		super();
		_as = as;
	}

	@Override
	protected JPanel createAreaSpecific() {

		// Set up area title
		JLabel areaTitle = new JLabel("Basic Strip Audio");
		areaTitle.setFont(Defaults.DefaultTitleFont);
		areaTitle.setForeground(Defaults.DefaultWritingColour);
		areaTitle.setHorizontalAlignment(JLabel.CENTER);
		areaTitle.setOpaque(false);

		// Set up progress panel
		// Create strip button
		_strip = new JButton("Strip");
		_strip.setOpaque(false);
		_strip.setFont(Defaults.DefaultButtonFont);
		_strip.setAlignmentX(Component.CENTER_ALIGNMENT);
		_strip.addActionListener(this);
		_strip.setBackground(Defaults.DefaultGoButtonColour);
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
		progressPanel.add(_strip);
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
		panel.add(progressPanel);

		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// If source was strip button, start strip
		if (e.getSource().equals(_strip)) {
			if (_canStrip) {
				String inVid;
				if ((inVid = VideoControlArea.getPath()).equals("none")) {
					JOptionPane.showMessageDialog(null,
							"No video file currently selected", "VAMIX Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (_as.getOutputName().trim().equals("")){
					JOptionPane.showMessageDialog(this,
							"Please enter an output file name", "VAMIX Warning",
							JOptionPane.ERROR_MESSAGE);
				}else {
					String outVideo;
					String[] arr = inVid.split("\\.");
					String ext = "." + arr[arr.length - 1];
					if (_as.getOutputName().trim().equals(
							"Enter in new output file name")) {
						outVideo = "output.mp3";
					} else {
						outVideo = _as.getOutputName().trim() + ".mp3";
					}
					if (!(_as.getOutputLocation()
							.equals("No location selected"))) {
						outVideo = _as.getOutputLocation() + "/" + outVideo;
					}
				StreamWorker strim = new StreamWorker(inVid);
				strim.execute();
				String test = "0,0";
				try {
					test = strim.get();
				} catch (InterruptedException | ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (test.equals("0,0")|test.equals("0,1")){
					JOptionPane.showMessageDialog(null,
							"No audio stream to extract", "VAMIX Error",
							JOptionPane.ERROR_MESSAGE);	
				}
				else{
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
							_canStrip = false;
							_strip.setEnabled(false);
							_progressBar.setIndeterminate(true);
							_cancel.setEnabled(true);
							_worker = new StripAudioWorker(inVid,
									outVideo, this);
							_worker.execute();
						}
					}else{
						_canStrip = false;
						_strip.setEnabled(false);
						_progressBar.setIndeterminate(true);
						_cancel.setEnabled(true);
						_worker = new StripAudioWorker(inVid,
								outVideo, this);
						_worker.execute();
					}
				}
				}
				}
			}
		}
		// If source is cancel button, tell worker to stop
		else if (e.getSource().equals(_cancel)) {
			_worker.cancel(true);
		}

	}

	public void processWorkerResults(int exitStatus) {
		// If non-zero, failure
		if (exitStatus == 9001){
			JOptionPane.showMessageDialog(null, "Stripping audio successfully cancelled",
					"VAMIX Success", JOptionPane.INFORMATION_MESSAGE);
		}
	else if (exitStatus != 0) {
			JOptionPane.showMessageDialog(null,
					"Stripping audio failed due to system error",
					"VAMIX Error", JOptionPane.ERROR_MESSAGE);
		} else {
			// Otherwise success
			JOptionPane.showMessageDialog(null, "Audio successfully stripped",
					"VAMIX Success", JOptionPane.INFORMATION_MESSAGE);
		}
		// Enable stripping
		_canStrip = true;
		_strip.setEnabled(true);
		_progressBar.setIndeterminate(false);
		_cancel.setEnabled(false);
	}

}
