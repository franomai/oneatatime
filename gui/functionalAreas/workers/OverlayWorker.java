package gui.functionalAreas.workers;

import gui.functionalAreas.AbstractFunctionalArea;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.SwingWorker;

/**
 * This class represents a Swing Worker that overlays an audio file with another file,
 * by first taking the audio streams from both inputs and combining them into one.
 * This combined stream is then passed into the output a long with the video stream
 * if it so exists. 
 * @author fsta657
 * 
 */
public class OverlayWorker extends SwingWorker<Void, Integer> {
	private int exitcode;
	private String inAudio;
	private String inFile;
	private String outFile;
	private AbstractFunctionalArea area;

	public OverlayWorker(String inFile, String inAudio, String outFile,
			AbstractFunctionalArea area) {
		this.inAudio = inAudio;
		this.inFile = inFile;
		this.outFile = outFile;
		this.area = area;
	}

	@Override
	protected Void doInBackground() {
		ProcessBuilder builder;
		// -vn says disregard audio strim, -y forces override.
		builder = new ProcessBuilder("avconv", "-i", inFile, "-i", inAudio,
				"-ac", "2", "-filter_complex", "amix=inputs=2", "-strict", "experimental", "-y",  outFile);
		builder.redirectErrorStream(true);
		try {
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(
					new InputStreamReader(stdout));
			String line = null;
			while ((line = stdoutBuffered.readLine()) != null) {
				if (this.isCancelled()){
					process.destroy();
				}else{
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
		if (this.isCancelled()){
			exitcode = 9001;
		}
		area.processWorkerResults(exitcode);
	}
}
