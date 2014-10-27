package gui.functionalAreas.workers;

import gui.functionalAreas.AbstractFunctionalArea;
import gui.functionalAreas.downloadAreas.AdvancedUploadArea;
import gui.functionalAreas.downloadAreas.Creation;
import gui.functionalAreas.textAreas.BasicAddCreditsArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * This class represents a Swing Worker that adds a 'creation' to the database
 * by first updating the local copy of the log, then appending the new creation
 * to the end, and this edited file is then fed back to the server.
 * 
 * @author fsta657
 * 
 */

public class UploadChanges extends SwingWorker<Void, Creation> {
	private AdvancedUploadArea aa;
	private int exitcode;
	private String desc;
	private String url;
	private int test;

	public UploadChanges(String d, String u, int test) {
		this.desc = d;
		this.url = u;
		this.test = test;
	}

	@Override
	protected Void doInBackground() {

		ProcessBuilder builder2;
		// -y forces override.
		builder2 = new ProcessBuilder("./dropbox_uploader.sh", "download",
				"thelog.txt", ".local.txt");
		builder2.redirectErrorStream(true);
		try {
			Process process2 = builder2.start();
			InputStream stdout = process2.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(
					new InputStreamReader(stdout));
			String line = null;
			while ((line = stdoutBuffered.readLine()) != null) {
				System.out.println(line);
			}
			process2.waitFor();
			stdoutBuffered.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(".local.txt", true)))) {
			out.println(desc + "|" + url);
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}

		ProcessBuilder builder;
		// -y forces override.
		builder = new ProcessBuilder("./dropbox_uploader.sh", "upload",
				".local.txt", "thelog.txt");
		builder.redirectErrorStream(true);
		try {
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(
					new InputStreamReader(stdout));
			String line = null;
			while ((line = stdoutBuffered.readLine()) != null) {
				System.out.println(line);
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
		JOptionPane.showMessageDialog(null, "File successfully uploaded",
				"VAMIX Success", JOptionPane.INFORMATION_MESSAGE);
	}
}
