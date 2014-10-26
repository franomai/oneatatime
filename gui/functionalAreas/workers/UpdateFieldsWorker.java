package gui.functionalAreas.workers;

import gui.functionalAreas.textAreas.AdvancedAddTextArea;
import gui.functionalAreas.textAreas.BasicAddCreditsArea;
import gui.functionalAreas.textAreas.BasicAddTextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class UpdateFieldsWorker extends SwingWorker<Integer, Void> {

	private BasicAddCreditsArea _bc;
	private BasicAddTextArea _bt;
	private AdvancedAddTextArea _at;
	private String _path;

	public UpdateFieldsWorker(BasicAddCreditsArea bc, BasicAddTextArea bt,
			AdvancedAddTextArea at, String path) {
		_bc = bc;
		_bt = bt;
		_at = at;
		_path=path;
	}
	
	protected void done(){
		try {
			_at.processWorkerResults(get());
		} catch (InterruptedException | ExecutionException e) {
			_at.processWorkerResults(1);
		}
	}

	@Override
	protected Integer doInBackground() throws Exception {
		ArrayList<String> array = new ArrayList<String>();
		File file = new File(_path);
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine())!=null){
				array.add(line);
			}
			br.close();
		} catch (IOException e) {
			// Exit with status 1 to indicate error
			return 1;
		}
		
		ArrayList<String> bc = new ArrayList<String>(array.subList(0, 4));
		_bc.setFields(bc);
		ArrayList<String> bt = new ArrayList<String>(array.subList(4, 8));
		_bt.setFields(bt);
		ArrayList<String> at = new ArrayList<String>(array.subList(8, 14));
		_at.setFields(at);

		// Exit with status -1 to indicate success
		br.close();
		return -1;
	}
}