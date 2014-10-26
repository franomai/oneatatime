package gui.functionalAreas.workers;

import gui.functionalAreas.textAreas.AdvancedAddTextArea;
import gui.functionalAreas.textAreas.BasicAddCreditsArea;
import gui.functionalAreas.textAreas.BasicAddTextArea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class WriteSaveWorker extends SwingWorker<Integer, Void> {

	private BasicAddCreditsArea _bc;
	private BasicAddTextArea _bt;
	private AdvancedAddTextArea _at;

	public WriteSaveWorker(BasicAddCreditsArea bc, BasicAddTextArea bt,
			AdvancedAddTextArea at) {
		_bc = bc;
		_bt = bt;
		_at = at;
	}
	
	protected void done(){
		try {
			_at.processWorkerResults(get());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected Integer doInBackground() throws Exception {
		ArrayList<String> bigList = new ArrayList<String>();
		bigList.addAll(_bc.getFields());
		bigList.addAll(_bt.getFields());
		bigList.addAll(_at.getFields());

		// Create the file
		String fileName = _at.getSaveName();

		File f = new File(fileName);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// Inform user of error
				return 1;
			}
		}else{
			f.delete();
			f.createNewFile();
		}
		PrintWriter out;
		try {
			out = new PrintWriter(new BufferedWriter(
					new FileWriter(fileName, true)));
			// Create the string
			for (int i = 0; i < bigList.size(); i++) {
				out.println(bigList.get(i));
			}
		} catch (IOException e) {
			// Exit with status 1 to indicate error
			return 1;
		}
		// Exit with status 0 to indicate success
		out.close();
		return 0;
		
	}
}