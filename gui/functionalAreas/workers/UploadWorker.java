package gui.functionalAreas.workers;

import gui.functionalAreas.AbstractFunctionalArea;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class UploadWorker extends SwingWorker<String, Integer> {
	private int exitcode = 1;
	private String inFile;
	private String out = "Error encountered!";
	private AbstractFunctionalArea area;

	// Takes in -->address<-- of video file and output file name -->with the
	// extension<---.

	public UploadWorker(String inFile, AbstractFunctionalArea area) {
		this.inFile = inFile;
		this.area = area;
	}

	@Override
	protected String doInBackground() {
		ProcessBuilder builder;
		//PRAY TO GOD
		builder = new ProcessBuilder("curl", "http://pomf.se/upload.php", "-F", "files[]=@"+inFile);
		builder.redirectErrorStream(true);
		try {
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(
					new InputStreamReader(stdout));
			String line = null;
			while ((line = stdoutBuffered.readLine()) != null) {
				System.out.println(line);
				if (line.contains("\"success\":true")){
					exitcode = 0;
					String[] arr = line.split(",");
					for (int i = 0; i < arr.length; i++){
						if(arr[i].contains("url") && !arr[i].contains("name")){
					String[] arr2 = arr[i].split("\"");
			out = arr2[3];				
				}
			}
				}
			}
			process.waitFor();
			stdoutBuffered.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	@Override
	protected void done() {
		area.processWorkerResults(exitcode);
	}
}
