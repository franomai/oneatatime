package gui.functionalAreas.workers;

import gui.functionalAreas.AbstractFunctionalArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * This class represents a Swing Worker that adds text to a given video file.
 * It takes many parameters, and these essentially boil down to the input file,
 * output file name, and a variety of customization options. The last flag however, whichEnd,
 * determines whichEnd the text is added to, 1 for the start, 2 for the end, and a preview otherwise.
 * @author fsta657
 */
public class AddTextWorker extends SwingWorker<Void, Integer> {
	private int exitcode;
	private String inFile;
	private String outFile;
	private String font;
	private String text;
	private String pos;
	private String fontsize;
	private String colour;
	private String time;
	private int whichEnd; // USE ONE FOR START
	private AbstractFunctionalArea area;

	public AddTextWorker(String inFile, String outFile,
			AbstractFunctionalArea area, String font, String text, String pos,
			String fontsize, String colour, String time, int whichEnd) {
		this.whichEnd = whichEnd;
		this.inFile = inFile;
		this.outFile = outFile;
		this.font = font;
		this.text = text;
		if (pos.equals("Top")){
		this.pos = "x=(main_w-text_w)/2:y=(main_h-text_h)/8";	
		}else if (pos.equals("Middle")){
			this.pos = "x=(main_w-text_w)/2:y=(main_h-text_h)/2";	
		}else{
			this.pos = "x=(main_w-text_w)/2:y=((main_h-text_h)/8)*7";
		}
		this.fontsize = fontsize;
		this.colour = colour;
		this.time = time;
		this.area = area;
	}

	@Override
	protected Void doInBackground() {
		ProcessBuilder builder;

		if (whichEnd == 1) {
			builder = new ProcessBuilder("avconv", "-i", inFile, "-vf",
					"drawtext=fontfile=" + font + ":text='" + text + "':"
							+ pos + ":fontsize=" + fontsize + ":fontcolor="
							+ colour + ":draw='lt(t," + time + ")'",
					 "-y","-strict", "experimental", outFile);
		} else if (whichEnd == 2) {
			builder = new ProcessBuilder("avconv", "-i", inFile, "-vf",
					"drawtext=fontfile=" + font + ":text='" + text + "':"
							+ pos + ":fontsize=" + fontsize + ":fontcolor="
							+ colour + ":draw='gt(t," + time + ")'",
"-y","-strict", "experimental", outFile);
		} else{
			builder = new ProcessBuilder("avconv", "-i", inFile, "-vf",
					"drawtext=fontfile=" + font + ":text='" + text + "':"
							+ pos + ":fontsize=" + fontsize + ":fontcolor="
							+ colour + ":draw='lt(t," + time + ")'",
					 "-y","-t",
					"5" ,"-strict", "experimental", ".PREVIEW.avi");
		}
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
		if((whichEnd > 2)){
			ProcessBuilder builder2;
			builder2 = new ProcessBuilder("vlc",".PREVIEW.avi");
			builder2.redirectErrorStream(true);
			try {
				Process process2 = builder2.start();
				process2.waitFor();
				File file = new File(".PREVIEW.avi");
				file.delete();
				exitcode = -1;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return null;
	}

	@Override
	protected void done() {
		area.processWorkerResults(exitcode);
	}
}
