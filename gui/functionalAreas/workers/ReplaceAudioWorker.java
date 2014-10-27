package gui.functionalAreas.workers;

import gui.functionalAreas.AbstractFunctionalArea;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.SwingWorker;

/**
 * This class represents a Swing Worker that replaces the audio of a given file
 * by mapping the audio stream from the audio file with the video stream of the
 * input to replace.
 * 
 * @author fsta657
 * 
 */
public class ReplaceAudioWorker extends SwingWorker<Void, Integer> {
	private int exitcode;
	private String inAudio;
	private String inFile;
	private String outFile;
	private AbstractFunctionalArea area;

	public ReplaceAudioWorker(String inFile, String inAudio, String outFile,
			AbstractFunctionalArea area) {
		this.inAudio = inAudio;
		this.inFile = inFile;
		this.outFile = outFile;
		this.area = area;
	}

	@Override
	protected Void doInBackground() {
		ProcessBuilder builder;
		// -y forces override.
		builder = new ProcessBuilder("avconv", "-i", inFile, "-i", inAudio,
				"-map", "0:0", "-map", "1:0", "-c", "copy", "-strict",
				"experimental", "-y", outFile);
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
