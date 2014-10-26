package gui.functionalAreas.workers;

import gui.functionalAreas.subtitleAreas.BasicCreateSubtitles;
import gui.functionalAreas.subtitleAreas.Subtitle;
import gui.functionalAreas.textAreas.AdvancedAddTextArea;
import gui.functionalAreas.textAreas.BasicAddCreditsArea;
import gui.functionalAreas.textAreas.BasicAddTextArea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class CreateWorker extends SwingWorker<Integer, Void> {

	private List<Subtitle> list;
	private String out;
	private BasicCreateSubtitles ao;

	public CreateWorker(String out, List<Subtitle> list, BasicCreateSubtitles basicCreateSubtitles) {
		this.out = out;
		this.list = list;
		this.ao = basicCreateSubtitles;
	}

	protected void done() {
try {
	ao.processWorkerResults(this.get());
} catch (InterruptedException | ExecutionException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}

	@Override
	protected Integer doInBackground() throws Exception {

		File f = new File(out);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// Inform user of error
				return 1;
			}}
			else{
				f.delete();
				f.createNewFile();
			}
		
		PrintWriter ss;
		try {
			ss = new PrintWriter(new BufferedWriter(new FileWriter(out, true)));
			ss.println("[Script Info]\nScriptType: v4.00+\n\n[V4+ Styles]\nFormat: Name, Fontname, Fontsize, PrimaryColour, SecondaryColour, OutlineColour, BackColour, Bold, Italic, Underline, BorderStyle, Outline, Shadow, Alignment, MarginL, MarginR, MarginV, AlphaLevel, Encoding\nStyle: Default,Arial,16,&Hffffff,&Hffffff,&H0,&H0,0,0,0,1,1,0,2,10,10,10,0,0");
			ss.println("\n[Events]\nFormat: Layer, Start, End, Text");
			for (int i = 0; i < list.size(); i++) {
				ss.println("Dialogue: 0,"+ list.get(i).getStart()+".01,"+ list.get(i).getEnd()+".00,"+ list.get(i).getText());
			}
		} catch (IOException e) {
			// Exit with status 1 to indicate error
			return 1;
		}
		// Exit with status 0 to indicate success
		ss.close();
		return 0;

	}
}