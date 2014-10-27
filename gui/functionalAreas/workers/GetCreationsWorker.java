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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * This class represents a Swing Worker that gets the current log of user
 * creations from the server and updates the GUI based on the contents of the
 * file downloaded.
 * 
 * @author fsta657
 * 
 */
public class GetCreationsWorker extends SwingWorker<Void, Creation> {
	private AdvancedUploadArea aa;
	private int exitcode;

	public GetCreationsWorker() {
	}

	@Override
	protected Void doInBackground() {
		ProcessBuilder builder;
		// -y forces override.
		builder = new ProcessBuilder("./dropbox_uploader.sh", "download",
				"thelog.txt", ".local.txt");
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
		File file = new File(".local.txt");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				String[] arr = line.split("\\|");
				for (int i = 0; i < arr.length; i++) {
					System.out.println(arr[i]);
				}
				Creation temp = new Creation(arr[0], arr[1]);
				publish(temp);
			}
			br.close();
		} catch (IOException e) {
			exitcode = 1;
		}

		return null;
	}

	@Override
	protected void process(List<Creation> chunks) {
		for (Creation number : chunks) {
			aa.list.add(number);
			aa.model.fireTableDataChanged();
		}
	}

	@Override
	protected void done() {
		aa.btnDelete.setEnabled(true);
	}
}
