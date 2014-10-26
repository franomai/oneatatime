package gui.functionalAreas.workers;

import gui.functionalAreas.AbstractFunctionalArea;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class AddSubtitlesWorker extends SwingWorker<Void, Integer> {
	private int exitcode;
	private String inSub;
	private String inFile;
	private String outFile;
	private AbstractFunctionalArea area;

	// Takes in -->address<-- of the audio file and the -->address<-- of video
	// file and output file name -->with the extension<---.

	public AddSubtitlesWorker(String inFile, String inSub, String outFile, AbstractFunctionalArea area) {
		this.inSub = inSub;
		this.inFile = inFile;
		this.outFile = outFile;
		this.area = area;
	}
	@Override
	protected Void doInBackground() {
		ProcessBuilder builder;
		// -y forces override.
		builder = new ProcessBuilder("avconv", "-i", inFile, "-i", inSub, "-map", "0", "-map", "1", "-c", "copy", "-y", outFile);
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
