package gui.functionalAreas.textAreas;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import defaults.Defaults;
import gui.VideoControlArea;
import gui.functionalAreas.AbstractFunctionalArea;
import gui.functionalAreas.subtitleAreas.BasicCreateSubtitles;
import gui.functionalAreas.workers.AddTextWorker;
import gui.functionalAreas.workers.DownloadWorker;
import gui.functionalAreas.workers.StreamWorker;

public class BasicAddCreditsArea extends AbstractFunctionalArea implements
		ActionListener {

	// WILL USE TFFS AS REFERENCE
	// GUI Fields
	private JTextField _text;
	private JTextField _time;
	private JTextField _outputName;
	private JComboBox<String> _pos;
	private JButton _add;
	private JButton _preview;
	// Worker Fields
	private AddTextWorker _worker;
	// Boolean Fields
	private boolean _canAdd = true;
	// Reference Fields
	private AdvancedAddTextArea _at;

	public BasicAddCreditsArea(AdvancedAddTextArea at) {
		super();
		_at = at;
	}

	@Override
	protected JPanel createAreaSpecific() {

		// Set up area title
		JLabel areaTitle = new JLabel("Basic Add Credits");
		areaTitle.setFont(Defaults.DefaultTitleFont);
		areaTitle.setForeground(Defaults.DefaultWritingColour);
		areaTitle.setHorizontalAlignment(JLabel.CENTER);
		areaTitle.setOpaque(false);

		// Set up time area
		JLabel outputLabel = new JLabel("Output name:");
		outputLabel.setFont(Defaults.DefaultLabelFont);
		outputLabel.setForeground(Defaults.DefaultWritingColour);
		outputLabel.setHorizontalAlignment(JLabel.CENTER);
		outputLabel.setOpaque(false);

		_outputName = new JTextField("Enter output file name (no ext)");
		_outputName.setFont(Defaults.DefaultTextFieldFont);
		_outputName.setHorizontalAlignment(JLabel.CENTER);
		_outputName.setBackground(Defaults.DefaultTextFieldColour);
		_outputName.setForeground(Defaults.DefaultLoadColour);
		_outputName.addActionListener(this);
		_outputName.setPreferredSize(new Dimension(200, 25));

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		p1.add(outputLabel);
		p1.add(_outputName);
		p1.setOpaque(false);

		JLabel timeLabel = new JLabel("Display Time:");
		timeLabel.setFont(Defaults.DefaultLabelFont);
		timeLabel.setForeground(Defaults.DefaultWritingColour);
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		timeLabel.setOpaque(false);

		_time = new JTextField("10");
		_time.setFont(Defaults.DefaultTextFieldFont);
		_time.setHorizontalAlignment(JLabel.CENTER);
		_time.setBackground(Defaults.DefaultTextFieldColour);
		_time.setForeground(Defaults.DefaultLoadColour);
		_time.addActionListener(this);
		_time.setPreferredSize(new Dimension(
				Defaults.DefaultFuncAreaWidth - 700, 30));

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout());
		p2.add(timeLabel);
		p2.add(_time);
		p2.setOpaque(false);

		JLabel posLabel = new JLabel("Position:");
		posLabel.setFont(Defaults.DefaultLabelFont);
		posLabel.setForeground(Defaults.DefaultWritingColour);
		posLabel.setHorizontalAlignment(JLabel.CENTER);
		posLabel.setOpaque(false);

		_pos = new JComboBox<String>(Defaults.DefaultPositions);
		_pos.setFont(Defaults.DefaultButtonFont);
		_pos.setForeground(Defaults.DefaultWritingColour);
		_pos.setOpaque(false);

		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout());
		p3.add(posLabel);
		p3.add(_pos);
		p3.setOpaque(false);

		JPanel timePanel = new JPanel();
		timePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
		timePanel.add(p1);
		timePanel.add(p2);
		timePanel.add(p3);
		timePanel.setOpaque(false);

		// Set up add text area
		_add = new JButton("Add Text");
		_add.setOpaque(false);
		_add.setFont(Defaults.DefaultButtonFont);
		_add.setAlignmentX(Component.CENTER_ALIGNMENT);
		_add.addActionListener(this);
		_add.setBackground(Defaults.DefaultGoButtonColour);

		// Set up preview text area
		_preview = new JButton("Preview");
		_preview.setOpaque(false);
		_preview.setFont(Defaults.DefaultButtonFont);
		_preview.setAlignmentX(Component.CENTER_ALIGNMENT);
		_preview.addActionListener(this);
		_add.setBackground(Defaults.DefaultGoButtonColour);

		_text = new JTextField("Enter text here");
		_text.setFont(Defaults.DefaultTextFieldFont);
		_text.setHorizontalAlignment(JLabel.CENTER);
		_text.setBackground(Defaults.DefaultTextFieldColour);
		_text.setForeground(Defaults.DefaultLoadColour);
		_text.addActionListener(this);
		_text.setPreferredSize(new Dimension(
				Defaults.DefaultFuncAreaWidth - 450, 30));
		_text.setDocument(new LimitedCharModel());

		JLabel textLabel = new JLabel("Text (LIMIT "
				+ Defaults.DefaultCharLimit + " CHARS): ");
		textLabel.setFont(Defaults.DefaultLabelFont);
		textLabel.setForeground(Defaults.DefaultWritingColour);
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		textLabel.setOpaque(false);

		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout());
		p4.add(textLabel);
		p4.add(_text);
		p4.setOpaque(false);

		JPanel textPanel = new JPanel();
		textPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
		textPanel.add(_add);
		textPanel.add(_preview);
		textPanel.add(p4);
		textPanel.setOpaque(false);

		// Add panels
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(Defaults.DefaultFuncAreaWidth,
				Defaults.DefaultFuncAreaHeight));
		panel.setLayout(new GridLayout(4, 1, 0, 0));

		panel.add(areaTitle);
		panel.add(timePanel);
		panel.add(textPanel);

		return panel;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(_add)) {
			if (_canAdd) {
				String inVid;
				_outputName.setText(_outputName.getText().trim());
				if ((inVid = VideoControlArea.getPath()).equals("none")) {
					JOptionPane.showMessageDialog(null,
							"No video file currently selected", "VAMIX Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (_outputName.getText().equals("")){
					JOptionPane.showMessageDialog(this,
							"Please enter an output file name", "VAMIX Warning",
							JOptionPane.ERROR_MESSAGE);
				}else {
					String outVideo;
					String[] arr = inVid.split("\\.");
					String ext = "." + arr[arr.length - 1];
					if (_outputName.getText().equals(
							"Enter output file name (no ext)")) {
						outVideo = "output" + ext;
					} else {
						outVideo = _outputName.getText() + ext;
					}
					if (!(_at.getOutputLocation()
							.equals("No location selected"))) {
						outVideo = _at.getOutputLocation() + "/" + outVideo;
					}

					StreamWorker strim = new StreamWorker(inVid);
					strim.execute();
					String test = "0,0";
					try {
						test = strim.get();
					} catch (InterruptedException | ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (test.equals("0,0")|test.equals("1,0")){
						JOptionPane.showMessageDialog(null,
								"No video stream to write to", "VAMIX Error",
								JOptionPane.ERROR_MESSAGE);	
					}
					else{
						
						File f = new File(outVideo);
						File q = new File(VideoControlArea.getPath());
						if (q.getAbsolutePath().equals(f.getAbsolutePath())){
							JOptionPane.showMessageDialog(null,
									"Input cannot have the same name as output, please rename.",
									"VAMIX Error", JOptionPane.ERROR_MESSAGE);
						}else{
						if (f.exists()){
							Object[] options = { "OK", "Cancel" };
							int selected = JOptionPane
									.showOptionDialog(
											null,
											"Warning: the file to be created will overwrite an existing file. Continue?",
											"Overwrite warning",
											JOptionPane.DEFAULT_OPTION,
											JOptionPane.WARNING_MESSAGE, null,
											options, options[0]);

							if (selected == 0) {
								String [] arr3 = VideoControlArea._showDur.getText().split(":");
								int dur = BasicCreateSubtitles.convert(arr3[0],arr3[1],arr3[2]);
								
							_worker = new AddTextWorker(
									inVid,
									outVideo,
									this,
									"/usr/share/fonts/truetype/freefont/FreeSerif.ttf",
									_text.getText(), (String) _pos.getSelectedItem(),
									_at.getCurrentFontSize(), _at
											.getCurrentFontColour(), Integer
											.toString(dur - Integer
													.parseInt(_time.getText())), 2);
							_canAdd = false;
							_preview.setEnabled(false);
							_add.setEnabled(false);
							_worker.execute();
							}
						}else{
							String [] arr3 = VideoControlArea._showDur.getText().split(":");
							int dur = BasicCreateSubtitles.convert(arr3[0],arr3[1],arr3[2]);
							
						_worker = new AddTextWorker(
								inVid,
								outVideo,
								this,
								"/usr/share/fonts/truetype/freefont/FreeSerif.ttf",
								_text.getText(), (String) _pos.getSelectedItem(),
								_at.getCurrentFontSize(), _at
										.getCurrentFontColour(), Integer
										.toString(dur - Integer
												.parseInt(_time.getText())), 2);
						_canAdd = false;
						_preview.setEnabled(false);
						_add.setEnabled(false);
						_worker.execute();
						}
						}
				}
				}
			}
		}
		if (e.getSource().equals(_preview)) {
			if (_canAdd) {
				String inVid;
				_outputName.setText(_outputName.getText().trim());
				if ((inVid = VideoControlArea.getPath()).equals("none")) {
					JOptionPane.showMessageDialog(null,
							"No video file currently selected", "VAMIX Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (_outputName.getText().equals("")){
					JOptionPane.showMessageDialog(this,
							"Please enter an output file name", "VAMIX Warning",
							JOptionPane.ERROR_MESSAGE);
				}else {
					String outVideo;
					String[] arr = inVid.split("\\.");
					String ext = "." + arr[arr.length - 1];
					if (_outputName.getText().equals(
							"Enter output file name (no ext)")) {
						outVideo = "output" + ext;
					} else {
						outVideo = _outputName.getText() + ext;
					}
					if (!(_at.getOutputLocation()
							.equals("No location selected"))) {
						outVideo = _at.getOutputLocation() + "/" + outVideo;
					}
					StreamWorker strim = new StreamWorker(inVid);
					strim.execute();
					String test = "0,0";
					try {
						test = strim.get();
					} catch (InterruptedException | ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (test.equals("0,0")|test.equals("1,0")){
						JOptionPane.showMessageDialog(null,
								"No video stream to write to", "VAMIX Error",
								JOptionPane.ERROR_MESSAGE);	
					}
					else{
					_worker = new AddTextWorker(
							inVid,
							outVideo,
							this,
							"/usr/share/fonts/truetype/freefont/FreeSerif.ttf",
							_text.getText(), (String) _pos.getSelectedItem(),
							_at.getCurrentFontSize(), _at
									.getCurrentFontColour(), _time.getText(), 3);
					_canAdd = false;
					_add.setEnabled(false);
					_preview.setEnabled(false);
					_worker.execute();
				}
				}
			}
		}

	}

	public void processWorkerResults(int exitStatus) {
		if (exitStatus == -1) {

		} else if (exitStatus != 0) {
			JOptionPane.showMessageDialog(null,
					"Adding credits failed due to system error", "VAMIX Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			// Otherwise success
			JOptionPane.showMessageDialog(null, "Credits successfully added",
					"VAMIX Success", JOptionPane.INFORMATION_MESSAGE);
		}
		// Enable extraction
		_canAdd = true;
		_add.setEnabled(true);
		_preview.setEnabled(true);
	}
	
	/* 
	 * UPDATING SECTION
	   Order of returned items is:
	   1) _text;
	   2)_time;
       3) _outputName;
       4) _pos;
	 */
	
	public ArrayList<String> getFields(){
		
		// Makes list
		ArrayList<String> list = new ArrayList<String>();
		list.add(_text.getText());
		list.add(_time.getText());
		list.add(_outputName.getText());
		Integer i = _pos.getSelectedIndex();
		list.add(i.toString());
		
		return list;
	}
	
	public void setFields(ArrayList<String> list){
		
		//Update fields
		_text.setText(list.get(0));
		_time.setText(list.get(1));
		_outputName.setText(list.get(2));
		_pos.setSelectedIndex(Integer.parseInt(list.get(3)));
	}

}
