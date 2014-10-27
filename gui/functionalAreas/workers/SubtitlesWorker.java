package gui.functionalAreas.workers;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingWorker;

/**
 * This class represents a Swing Worker that figures out whether an input
 * contains a subtitle stream by passing the input into avprobe and parsing
 * these results. If a stream is found the ID of the stream is returned,
 * otherwise the return value is -1.
 * 
 * @author fsta657
 * 
 */
public class SubtitlesWorker extends SwingWorker<Integer, Void> {
	@SuppressWarnings("unused")
	private int exitcode;
	private String inFile;
	private int strim = -1;
	private int setter = 0;

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
