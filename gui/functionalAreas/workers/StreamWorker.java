package gui.functionalAreas.workers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.SwingWorker;

/**
 * This class represents a Swing Worker that figures out whether an input
 * contains video or audio streams by passing the input into avprobe and parsing
 * these results. A 1 in a position indicates a stream has been found, otherwise
 * it remains at 0. The format is audio stream, video stream.
 * 
 * @author fsta657
 * 
 */
public class StreamWorker extends SwingWorker<String, Void> {
	private int exitcode;
	private String inFile;
	private String a = "0,";
	private String b = "0";
	private int setter = 0;

	// Takes in -->address<-- of video file

	public StreamWorker(String inFile) {
		this.inFile = inFile;
	}

	@Override
	protected String doInBackground() {
		ProcessBuilder builder;
		builder = new ProcessBuilder("avprobe", inFile);
		builder.redirectErrorStream(true);
		try {
			Process process = builder.start();
			InputStream stdout = process.getInputStream();

			BufferedReader stdoutBuffered = new BufferedReader(
					new InputStreamReader(stdout));
			String line = null;
			while ((line = stdoutBuffered.readLine()) != null) {
				System.out.println(line);
				if (line.contains("bitrate: ")) {
					setter = 1;
				}
				if (setter != 0) {
					if (line.contains("Audio:")) {
						a = "1,";
					}
					if (line.contains("Video:") && (!line.contains("png"))
							&& (!line.contains("jpg"))) {
						b = "1";
					}
				}
			}
			exitcode = process.waitFor();
			stdoutBuffered.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		a = a + b;
		System.out.println(a);
		// FORMAT IS "HAS AUDIO,HAS VIDEO"
		return a;
	}
}