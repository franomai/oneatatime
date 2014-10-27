package gui.functionalAreas.workers;

import gui.functionalAreas.subtitleAreas.Subtitle;
import gui.functionalAreas.textAreas.AdvancedAddTextArea;
import gui.functionalAreas.textAreas.BasicAddCreditsArea;
import gui.functionalAreas.textAreas.BasicAddTextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingWorker;

public class LoadWorker extends SwingWorker<ArrayList<Subtitle>, Void> {

	/**
	 * This class represents a Swing Worker that reads in a .ass file and
	 * converts each found subtitle to a subtitle object, and these objects are
	 * collated into a list and passed back to the main.
	 * 
	 * @author fsta657
	 * 
	 */
	private String _path;

	public LoadWorker(String path) {
		_path = path;
	}

	protected void done() {
	}

	@Override
	protected ArrayList<Subtitle> doInBackground() throws Exception {
		ArrayList<Subtitle> array = new ArrayList<Subtitle>();
		File file = new File(_path);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("Dialogue")) {
					String[] list = line.split(",");
					String one = list[1].substring(0, list[1].lastIndexOf('.'));
					String two = list[2].substring(0, list[2].lastIndexOf('.'));

					String[] test = one.split(":");
					if (test[0].length() < 2) {
						one = "0" + one;
					}
					String[] test2 = two.split(":");
					if (test2[0].length() < 2) {
						two = "0" + two;
					}

					array.add(new Subtitle(one, two, list[3]));

				}
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Oops.");
		}

		// Exit with status 0 to indicate success
		br.close();
		System.out.println(array);
		return array;
	}
}