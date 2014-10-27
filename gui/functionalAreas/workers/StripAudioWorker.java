package gui.functionalAreas.workers;

import gui.functionalAreas.AbstractFunctionalArea;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * This class represents a Swing Worker that strips the audio from a video file
 * by discarding all video streams and passing whatever is left to an output
 * file. Note this method has not been tested for videos containing subtitles.
 * 
 * @author fsta657
 * 
 */
public class StripAudioWorker extends SwingWorker<Void, Integer> {
	private int exitcode;
	private String inFile;
	private String outFile;
	private AbstractFunctionalArea area;

	public StripAudioWorker(String inFile, String outFile,
			AbstractFunctionalArea area) {
		this.inFile = inFile;
		this.outFile = outFile;
		this.area = area;
	}

	@Override
	protected Void doInBackground() {
		ProcessBuilder builder;
		// -vn says disregard audio strim, -y forces override.
		builder = new ProcessBuilder("avconv", "-i", inFile, "-ac", "2", "-vn",
				"-y", "-strict", "experimental", outFile);
		builder.redirectErrorStream(true);
		try {
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(
					new InputStreamReader(stdout));
			String line = null;
			while ((line = stdoutBuffered.readLine()) != null) {
				if (this.isCancelled()) {
					process.destroy();
				} else {
					System.out.println(line);
				}
			}
			exitcode = process.waitFor();
			stdoutBuffered.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void done() {
		if (this.isCancelled()) {
			exitcode = 9001;
		}
		area.processWorkerResults(exitcode);
	}
}
