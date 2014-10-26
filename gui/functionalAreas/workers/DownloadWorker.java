package gui.functionalAreas.workers;

import gui.functionalAreas.downloadAreas.BasicDownloadArea;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class DownloadWorker extends SwingWorker<Integer, Integer> {

	private String _url;
	private String _filename;
	private JProgressBar _progress;
	private boolean _cancel;
	private BasicDownloadArea _pane;
	private String out;

	public DownloadWorker(String url, JProgressBar progress,
			BasicDownloadArea pane, String out) {
		_url = url.trim();
		// Edit url into a filename
		_filename = _url.substring(_url.lastIndexOf("/") + 1, _url.length())
				.trim();
		_cancel = false;
		_progress = progress;
		_pane = pane;
		this.out = out;
	}

	public void cancel() {
		_cancel = true;
	}

	// Done method passes exit status to the GUI download pane
	protected void done() {
		// Create variable outside try/catch to ensure it exists
		Integer exitStatus = 1;
		try {
			exitStatus = get();
		} catch (InterruptedException | ExecutionException e) {
			// Deal with exceptions by leaving exit status as 1 - error
		}
		if (_cancel == true){
			exitStatus = 9001;
		}
		_pane.processWorkerResults(exitStatus);
	}

	protected void publish(Integer i) {
		super.publish(i);
	}

	@Override
	protected Integer doInBackground() throws Exception {

		/*
		 * FILE CHECKING SECTION
		 */
		// Set up variables to act globally over this method
		int overWrite = 0;
		int exitStatus = 1;

		// First check if the file exists
		String[] test = out.split("/");
		File f = new File(test[test.length-1]);
		if (f.exists()) {
			overWrite = JOptionPane
					.showConfirmDialog(
							null,
							"File already exists. Should VAMIX overwrite? (Answering no will continue any partially download files)",
							"VAMIX File Exists", JOptionPane.YES_NO_OPTION);
		}

		/*
		 * DOWNLOAD SECTION
		 */
		// Create correct process depending on whether user wants to
		// overwrite or not
		ProcessBuilder builder;
		if (overWrite == 0) {
			f.delete();
			builder = new ProcessBuilder("wget","-O",out, _url);
		} else {
			builder = new ProcessBuilder("wget","-O",out, "-c", _url);
		}

		// Redirect error to output
		builder.redirectErrorStream(true);
		try {
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(
					new InputStreamReader(stdout));
			// Set up progress reading
			String line = null;
			publish(0);
			// Extract progress from output
			while ((line = stdoutBuffered.readLine()) != null) {
				if (line.contains("%")) {
					int endPos = line.indexOf("%");
					String percent = line.substring(endPos - 3, endPos).trim();
					publish(Integer.parseInt(percent));
				}
				if (_cancel) {
					process.destroy();
				}
			}
			// Wait for process to finish to get the exit status
			exitStatus = process.waitFor();
			// Close all streams
			stdout.close();
			stdoutBuffered.close();

		} catch (IOException i) {
			exitStatus = 2;
		}
		// Return exit status of process to done method
		return exitStatus;
	}

	protected void process(List<Integer> chunks) {
		for (Integer i : chunks) {
			_progress.setValue(i);
			;
		}
	}

}