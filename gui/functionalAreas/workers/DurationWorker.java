package gui.functionalAreas.workers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class DurationWorker extends SwingWorker<Integer, Void> {
	private int exitcode;
	private String inFile;
	private String dur = "";
	private int num = 0;
	// Takes in -->address<-- of video file

	public DurationWorker(String inFile) {
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
			Pattern p = Pattern.compile("Duration: \\d\\d:\\d\\d:\\d\\d");
			Matcher m;
			while ((line = stdoutBuffered.readLine()) != null) {
				System.out.println(line);
				m = p.matcher(line);
				if (m.find())
				dur = m.group(0);
			}
			exitcode = process.waitFor();
			stdoutBuffered.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dur = dur.replace("Duration: ","");
		String [] arr = dur.split(":");
		int a = Integer.parseInt(arr[0]);
		int b = Integer.parseInt(arr[1]);
		int c = Integer.parseInt(arr[2]);
		num = a*60*60 + b*60 + c;
		return num;
	}
}