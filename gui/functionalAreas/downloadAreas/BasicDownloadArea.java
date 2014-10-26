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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import defaults.Defaults;
import gui.MyVLCPlayer;
import gui.functionalAreas.AbstractFunctionalArea;
import gui.functionalAreas.workers.DownloadWorker;
import gui.functionalAreas.workers.DurationWorker;

/*
 BASIC:
 URL field 
 Download Button
 Cancel Button
 Error message (file exists, failed etc)
 */

public class BasicDownloadArea extends AbstractFunctionalArea implements
		ActionListener {

	private JTextField _url;
	private JProgressBar _progressBar;
	private JButton _cancel;
	private JButton _download;
	// Worker Fields
	private DownloadWorker _worker;
	// Boolean Fields
	private boolean _canDownload = true;
	// Reference Fields
	private AdvancedDownloadArea _ad;

	public BasicDownloadArea(AdvancedDownloadArea ad) {
		super();
		_ad=ad;
	}

	@Override
	protected JPanel createAreaSpecific() {

		// Set up area title
		JLabel areaTitle = new JLabel("Basic Download");
		areaTitle.setFont(Defaults.DefaultTitleFont);
		areaTitle.setForeground(Defaults.DefaultWritingColour);
		areaTitle.setHorizontalAlignment(JLabel.CENTER);
		areaTitle.setOpaque(false);

		// Set up url panel
		_url = new JTextField("http://ccmixter.org/content/Zapac/Zapac_-_Test_Drive.mp3");
		_url.setFont(Defaults.DefaultTextFieldFont);
		_url.setHorizontalAlignment(JLabel.CENTER);
		_url.setBackground(Defaults.DefaultTextFieldColour);
		_url.setForeground(Defaults.DefaultLoadColour);
		_url.addActionListener(this);
		_url.setPreferredSize(new Dimension(
				Defaults.DefaultFuncAreaWidth - 140, 30));

		JLabel urlLabel = new JLabel("Enter URL:");
		urlLabel.setFont(Defaults.DefaultLabelFont);
		urlLabel.setForeground(Defaults.DefaultWritingColour);
		urlLabel.setHorizontalAlignment(JLabel.CENTER);
		urlLabel.setOpaque(false);

		JPanel urlPanel = new JPanel();
		urlPanel.setLayout(new FlowLayout());
		urlPanel.add(urlLabel);
		urlPanel.add(_url);
		urlPanel.setOpaque(false);

		// Set up progress panel
		// Create cancel button
		_download = new JButton("Download");
		_download.setOpaque(false);
		_download.setFont(Defaults.DefaultButtonFont);
		_download.setAlignmentX(Component.CENTER_ALIGNMENT);
		_download.setBackground(Defaults.DefaultGoButtonColour);
		_download.addActionListener(this);
		// Add label
		JLabel label3 = new JLabel("Progress:");
		label3.setBackground(Defaults.DefaultDownloadColour);
		label3.setFont(Defaults.DefaultLabelFont);
		label3.setForeground(Defaults.DefaultWritingColour);
		label3.setHorizontalAlignment(JLabel.CENTER);
		// Create progress bar
		_progressBar = new JProgressBar();
		_progressBar.setStringPainted(true);
		_progressBar.setForeground(Defaults.DefaultLoadColour);
		_progressBar.setBackground(Defaults.DefaultProgressColour);
		_progressBar.setPreferredSize(new Dimension(
				Defaults.DefaultFuncAreaWidth - 280, 30));
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
		progressPanel.add(_download);
		progressPanel.add(label3);
		progressPanel.add(_progressBar);
		progressPanel.add(_cancel);
		progressPanel.setOpaque(false);

		// Add panels
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(Defaults.DefaultFuncAreaWidth,
				Defaults.DefaultFuncAreaHeight));
		panel.setLayout(new GridLayout(4, 1, 0, 0));

		panel.add(areaTitle);
		panel.add(urlPanel);
		panel.add(progressPanel);


		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// If source is url text area
		if (e.getSource().equals(_download) && _canDownload == true) {
			// Check open source
			int answer = JOptionPane
					.showConfirmDialog(null, "Is this file open-source?",
							"VAMIX Open-Source Confirmation",
							JOptionPane.YES_NO_OPTION);
			// If open source start download, otherwise warn user
			if (answer == 0) {
				// Disable entering new url
				String outVideo;
				String[] arr = _url.getText().split("\\.");
				String ext = "." + arr[arr.length - 1];
				if (_ad.getOutputName().trim().equals("")){
					JOptionPane.showMessageDialog(this,
							"Please enter an output file name", "VAMIX Warning",
							JOptionPane.ERROR_MESSAGE);
				}else{
				if (_ad.getOutputName().equals(
						"Enter in new output file name")) {
					outVideo = "output" + ext;
				}			
				else {
					String[] test = _ad.getOutputName().split("\\.");
					outVideo = test[0].trim() + ext;
				}
				if (!(_ad.getOutputLocation()
						.equals("No location selected"))) {
					outVideo = _ad.getOutputLocation() + "/" + outVideo;
				}else{
					outVideo = "./" + outVideo;
				}
				_canDownload = false;
				_download.setEnabled(false);
				_cancel.setEnabled(true);
				_worker = new DownloadWorker(_url.getText(), _progressBar, this, outVideo);
				_worker.execute();
			} }else {
				
				JOptionPane.showMessageDialog(null,
						"This software will only operate on open-source files",
						"VAMIX Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		// Otherwise source is cancel button
		else if (e.getSource().equals(_cancel)) {
			if (_worker != null) {
				_worker.cancel();
			}
		}
	}

	public void processWorkerResults(int exitStatus) {
		// If successful, inform user
		if (exitStatus == 0) {
			JOptionPane.showMessageDialog(null,
					"File was successfully download", "VAMIX Success",
					JOptionPane.INFORMATION_MESSAGE);
		}
		// If unsuccessful, inform user
		// Exit statuses 1 - 3 are system errors
		else if (exitStatus < 4) {
			JOptionPane.showMessageDialog(null,
					"Download failed due to system error", "VAMIX Error",
					JOptionPane.ERROR_MESSAGE);
		}
		// Exit statuses 4 - 8 are network errors
		else if (exitStatus == 9001){
			JOptionPane.showMessageDialog(null,
					"Download successfully cancelled", "VAMIX Success",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			JOptionPane
					.showMessageDialog(
							null,
							"Download failed due to network error. Check your internet connection",
							"VAMIX Error", JOptionPane.ERROR_MESSAGE);
		}
		// Then enable new downloads
		_download.setEnabled(true);
		_canDownload = true;
		_cancel.setEnabled(false);
	}

}