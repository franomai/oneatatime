package gui.functionalAreas.workers;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class SubtitlesWorker extends SwingWorker<Integer, Void> {
	private int exitcode;
	private String inFile;
	private int strim = -1;
	private int setter = 0;
	// Takes in -->address<-- of video file

	public SubtitlesWorker(String inFile) {
		this.inFile = inFile;
	}

	@Override
	protected Integer doInBackground() {
		ProcessBuilder builder;
		builder = new ProcessBuilder(
				"avprobe",
				inFile);
		builder.redirectErrorStream(true);
		try {
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			
			BufferedReader stdoutBuffered = new BufferedReader(
					new InputStreamReader(stdout));
			String line = null;
			Pattern p = Pattern.compile("\\d");
			Matcher m;
			while ((line = stdoutBuffered.readLine()) != null) {
				System.out.println(line);
if (line.contains("bitrate: ")){
	setter = 1;
}
if (setter != 0){
if (line.contains("Subtitle")){
m = p.matcher(line);
if(m.find()){
	if(m.find()){
strim = Integer.parseInt(m.group(0));
	}
}
}	
}
			}
			exitcode = process.waitFor();
			stdoutBuffered.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
return strim;
	}
}
