package gui.functionalAreas.workers;

import gui.functionalAreas.downloadAreas.BasicTrimArea;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class TrimWorker extends SwingWorker<Integer, Integer> {

	private String _startTime;
	private String _endTime;
	private String _fileName;
	private String _outputName;
	private BasicTrimArea _pane;

	public TrimWorker(String fileName, String startTime, String endTime,
			String outputName, BasicTrimArea pane) {
		_startTime = startTime.trim();
		_endTime = endTime.trim();
		_fileName = fileName.trim();
		_outputName = outputName.trim();
		_pane = pane;
	}

	// Done method interprets exit statuses from doInBackground into
	// meaningful messages
	protected void done() {
		// Create variable outside try/catch to ensure it exists
		Integer exitStatus = 1;
		try {
			exitStatus = get();
		} catch (InterruptedException | ExecutionException e) {
			// Deal with exceptions by leaving exit status as 1 - error
		}
		_pane.processWorkerResults(exitStatus);
	}

	@Override
	protected Integer doInBackground() throws Exception {

		/*
		 * FILE CHECKING SECTION
		 */
		// Set up exit status variable to act globally over this method
		int exitStatus = 1;

		// First check if the file exists
		File f = new File(_fileName);
		if (!f.exists()) {
			JOptionPane.showMessageDialog(null,
					"File does not exist. Specify an existing file",
					"VAMIX Error", JOptionPane.ERROR_MESSAGE);
			return 200;
		}
		ProcessBuilder builder;

		// Then check if output file exists
		f = new File("./" + _outputName);
		if (f.exists()) {
			int overWrite = JOptionPane.showConfirmDialog(null,
					"Output file exists. Should VAMIX overwrite?",
					"VAMIX File Exists", JOptionPane.YES_NO_OPTION);
			// If user says no, end thread
			if (overWrite == 1) {
				return 200;
			} else {
				// Otherwise delete the file
				f.delete();
			}
		}

		/*
		 * EXTRACTION SECTION
		 */
		// If all correct, start extraction process
		builder = new ProcessBuilder("avconv", "-i", _fileName,"-c","copy", "-ss",
				_startTime, "-t", _endTime, _outputName);
		try {
			// Start process
			Process process = builder.start();
			// Wait for process to finish to get exit status
			exitStatus = process.waitFor();
		} catch (IOException i) {
			exitStatus = 1;
		}
		// Return exit status of process
		return exitStatus;
	}

}