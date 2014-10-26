package gui.functionalAreas.subtitleAreas;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import defaults.Defaults;
import gui.VideoControlArea;
import gui.functionalAreas.AbstractFunctionalArea;
import gui.functionalAreas.workers.ExtractWorker;
import gui.functionalAreas.workers.SubtitlesWorker;


/**
 * This class represents the basic extract subtitles pane. It contains the create method for painting this pane,
 * allowing for a file to be selected and the operation to be carried out. This operation is carried out
 * by grabbing the values of the fields of AdvancedExtractSubtitlesArea and passing them into a worker with fields
 * from this class and the player. Upon completion error/success is reported via processWorkerResults.
 * @author fsta657
 *
 */
@SuppressWarnings("serial")
public class BasicExtractSubtitlesArea extends AbstractFunctionalArea implements
		ActionListener {

	private JButton _cancel;
	private JProgressBar _progressBar;
	private JButton _strip;
	
	// Worker Fields
	private ExtractWorker _worker;
	// Boolean Fields
	private boolean _canStrip = true;
	// Reference Fields
	private AdvancedExtractSubtitlesArea _as;

	public BasicExtractSubtitlesArea(AdvancedExtractSubtitlesArea as) {
		super();
		_as = as;
	}

	@Override
	protected JPanel createAreaSpecific() {

		// Set up area title
		JLabel areaTitle = new JLabel("Basic Extract Subtitles");
		areaTitle.setFont(Defaults.DefaultTitleFont);
		areaTitle.setForeground(Defaults.DefaultWritingColour);
		areaTitle.setHorizontalAlignment(JLabel.CENTER);
		areaTitle.setOpaque(false);

		// Set up progress panel
		// Create strip button
		_strip = new JButton("Extract");
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
				}else if (_as.getOutputName().trim().equals("")){
					JOptionPane.showMessageDialog(this,
							"Please enter an output file name", "VAMIX Warning",
							JOptionPane.ERROR_MESSAGE);
				} else {
					String outVideo;
					if (_as.getOutputName().trim().equals(
							"Enter in new output file name") || _as.getOutputName().equals("")) {
						outVideo = "extracted.ass";
					} else {
						String [] arr = _as.getOutputName().trim().split("\\.");
						outVideo = arr[0] + ".ass";
					}
					if (!(_as.getOutputLocation()
							.equals("No location selected"))) {
						outVideo = _as.getOutputLocation() + "/" + outVideo;
					}
				SubtitlesWorker strim = new SubtitlesWorker(inVid);
				strim.execute();
				int test = -1;
				try {
					test = strim.get();
				} catch (InterruptedException | ExecutionException e1) {
					e1.printStackTrace();
				}
				if (test == -1){
					JOptionPane.showMessageDialog(null,
							"No subtitles stream to extract (subtitles may be hardcoded)", "VAMIX Error",
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
							System.out.print(test);
						_canStrip = false;
						_strip.setEnabled(false);
						_progressBar.setIndeterminate(true);
						_cancel.setEnabled(true);
						_worker = new ExtractWorker(inVid,
								outVideo,test, this);
						_worker.execute();
						}
					}else{
						System.out.print(test);
					_canStrip = false;
					_strip.setEnabled(false);
					_progressBar.setIndeterminate(true);
					_cancel.setEnabled(true);
					_worker = new ExtractWorker(inVid,
							outVideo,test, this);
					_worker.execute();
					}
				}
				}
				}
			}
		}
		else if (e.getSource().equals(_cancel)) {
			_worker.cancel(true);
		}

	}

	public void processWorkerResults(int exitStatus) {
		_progressBar.setIndeterminate(false);
		// If non-zero, failure
		if (exitStatus == 9001){
			JOptionPane.showMessageDialog(null, "Extracting subtitles successfully cancelled",
					"VAMIX Success", JOptionPane.INFORMATION_MESSAGE);
		}
	else if (exitStatus != 0) {
			JOptionPane.showMessageDialog(null,
					"Extracting subtitles failed due to system error",
					"VAMIX Error", JOptionPane.ERROR_MESSAGE);
		} else {
			// Otherwise success
			JOptionPane.showMessageDialog(null, "Subtitles successfully added",
					"VAMIX Success", JOptionPane.INFORMATION_MESSAGE);
		}
		// Enable stripping
		_canStrip = true;
		_strip.setEnabled(true);
		_cancel.setEnabled(false);
	}

}

