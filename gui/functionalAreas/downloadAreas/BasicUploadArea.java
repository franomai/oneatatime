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
import gui.functionalAreas.workers.GetCreationsWorker;
import gui.functionalAreas.workers.OverlayWorker;
import gui.functionalAreas.workers.StreamWorker;
import gui.functionalAreas.workers.StripAudioWorker;
import gui.functionalAreas.workers.UploadChanges;
import gui.functionalAreas.workers.UploadWorker;

/*
 BASIC:
 URL field 
 Download Button
 Cancel Button
 Error message (file exists, failed etc)
 */

public class BasicUploadArea extends AbstractFunctionalArea implements
		ActionListener {

	private JProgressBar _progressBar;
	private JButton _strip;
	private JTextField _text;
	
	// Worker Fields
	private UploadWorker _worker;
	private UploadChanges _workers;
	private JButton _add;
	private String last = "";
	// Boolean Fields
	private boolean _canStrip = true;
	private GetCreationsWorker ohsnap;

	public BasicUploadArea() {
	}

	@Override
	protected JPanel createAreaSpecific() {

		// Set up area title
		JLabel areaTitle = new JLabel("Basic Upload");
		areaTitle.setFont(Defaults.DefaultTitleFont);
		areaTitle.setForeground(Defaults.DefaultWritingColour);
		areaTitle.setHorizontalAlignment(JLabel.CENTER);
		areaTitle.setOpaque(false);

		// Set up progress panel
		// Create strip button
		_strip = new JButton("Upload");
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
		// Combine into progress panel
		JLabel progressPanel = new JLabel();
		progressPanel.setLayout(new FlowLayout());
		progressPanel.add(_strip);
		progressPanel.add(label4);
		progressPanel.add(_progressBar);
		progressPanel.setOpaque(false);
		
		JLabel textLabel = new JLabel("Output URL:");
		textLabel.setFont(Defaults.DefaultLabelFont);
		textLabel.setForeground(Defaults.DefaultWritingColour);
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		textLabel.setOpaque(false);
		_text = new JTextField("This might take a while...");
		_text.setFont(Defaults.DefaultTextFieldFont);
		_text.setHorizontalAlignment(JLabel.CENTER);
		_text.setBackground(Defaults.DefaultTextFieldColour);
		_text.setForeground(Defaults.DefaultLoadColour);
		_text.addActionListener(this);
		_text.setPreferredSize(new Dimension(
				Defaults.DefaultFuncAreaWidth - 450, 30));
		_text.setEditable(false);
		_add = new JButton("Share!");
		_add.setOpaque(false);
		_add.setFont(Defaults.DefaultButtonFont);
		_add.setAlignmentX(Component.CENTER_ALIGNMENT);
		_add.addActionListener(this);
		JLabel outputPanel = new JLabel();
		outputPanel.setLayout(new FlowLayout());
		outputPanel.add(textLabel);
		outputPanel.add(_text);
		outputPanel.add(_add);
		outputPanel.setOpaque(false);

		// Add panels
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(Defaults.DefaultFuncAreaWidth,
				Defaults.DefaultFuncAreaHeight));
		panel.setLayout(new GridLayout(4, 1, 0, 0));

		panel.add(areaTitle);
		panel.add(progressPanel);
		panel.add(outputPanel);

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
				}
				else{
					_canStrip = false;
					_strip.setEnabled(false);
					_progressBar.setIndeterminate(true);
				_worker = new UploadWorker(inVid,this);
				_worker.execute();
				}
				}
			}
		else if (e.getSource().equals(_add)){
			if (_text.getText().equals("This might take a while...")){
				JOptionPane.showMessageDialog(null,
						"Please upload a video file BEFORE you try to share it.",
						"VAMIX Error", JOptionPane.ERROR_MESSAGE);	
			}else if(last.equals(_text.getText())){
				JOptionPane.showMessageDialog(null,
						"You have already shared this file.",
						"VAMIX Error", JOptionPane.ERROR_MESSAGE);
			}else{
			String s = (String)JOptionPane.showInputDialog(
                    this,
                    "Enter a description for this file");
			last = _text.getText();
			_workers = new UploadChanges(s,_text.getText(),AdvancedUploadArea.isRun);
			_workers.execute();
			}
			
		}
		}


	public void processWorkerResults(int exitStatus) {
		// If non-zero, failure
		if (exitStatus != 0) {
			JOptionPane.showMessageDialog(null,
					"Upload failed due to system error, check your internet connection",
					"VAMIX Error", JOptionPane.ERROR_MESSAGE);
		} else {
			try {
				_text.setText("http://a.pomf.se/" + _worker.get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Otherwise success
			JOptionPane.showMessageDialog(null, "File successfully uploaded",
					"VAMIX Success", JOptionPane.INFORMATION_MESSAGE);
		}
		// Enable stripping
		_canStrip = true;
		_strip.setEnabled(true);
		_progressBar.setIndeterminate(false);
	}

}
