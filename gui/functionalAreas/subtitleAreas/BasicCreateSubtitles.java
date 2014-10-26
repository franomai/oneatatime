package gui.functionalAreas.subtitleAreas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import defaults.Defaults;
import gui.VideoControlArea;
import gui.functionalAreas.AbstractFunctionalArea;
import gui.functionalAreas.audioAreas.AdvancedOverlayAudioArea;
import gui.functionalAreas.workers.AddSubtitlesWorker;
import gui.functionalAreas.workers.CreateWorker;
import gui.functionalAreas.workers.LoadWorker;
import gui.functionalAreas.workers.StreamWorker;
import gui.functionalAreas.workers.TrimWorker;

/*
 BASIC:
 URL field 
 Download Button
 Cancel Button
 Error message (file exists, failed etc)
 */

public class BasicCreateSubtitles extends AbstractFunctionalArea implements
		ActionListener {
	private TimeField _startTime;
	private TimeField _stopTime;
	private JButton _add;
	private JButton _preview;
	private JButton _view;
	private JButton _help;
	private JButton _load;
	private JTextField _text;
	private JTextField _startsTime;
	private static TableModel model;
	private static JFrame Sales;
	// Worker Fields
	private CreateWorker _worker;
	private LoadWorker _worker2;
	// Boolean Fields
	private boolean _canTrim = true;
	private static ArrayList<Subtitle> list;
	private AdvancedCreateSubtitles _ao;

	public BasicCreateSubtitles(AdvancedCreateSubtitles ao) {
		super();
		initViewPane();
		_ao = ao;
	}

	protected static void initViewPane() {
		Sales = new JFrame("Current subtitles");
		Sales.setSize(453, 230);
		Sales.setTitle("Current subtitles to add");
		Sales.setResizable(false);
		Sales.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		Sales.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Subtitles for this file");
		lblNewLabel.setFont(Defaults.DefaultLabelFont);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(120, 11, 200, 24);
		panel.add(lblNewLabel);

		list = new ArrayList<Subtitle>();
		// create the model
		model = new TableModel(list);
		// create the table
		final JTable table = new JTable(model);
		table.setSelectionModel(new ForcedListSelectionModel());
		table.setBounds(37, 46, 368, 112);
		JScrollPane pane = new JScrollPane(table);
		pane.setLocation(28, 45);
		pane.setSize(385, 106);
		panel.add(pane);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (list.size() > 0) {
					int toRemove = table.getSelectedRow();
					if (toRemove >= 0) {
						Object[] options = { "OK", "Cancel" };
						int selected = JOptionPane
								.showOptionDialog(
										Sales,
										"Warning: deleting the subtitle will delete the subtitle.",
										"Deletion confirmation",
										JOptionPane.DEFAULT_OPTION,
										JOptionPane.WARNING_MESSAGE, null,
										options, options[0]);

						if (selected == 0) {
							list.remove(toRemove);
							model.fireTableDataChanged();
						}
					}

					else {
						JOptionPane.showMessageDialog(Sales,
								"No subtitle selected", "VAMIX Warning",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(Sales,
							"There are no current subtitles to delete",
							"VAMIX Warning", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnDelete.setBounds(175, 168, 89, 23);
		panel.add(btnDelete);
	}

	@Override
	protected JPanel createAreaSpecific() {

		// Set up area title
		JLabel areaTitle = new JLabel("Basic Create Subtitles");
		areaTitle.setFont(Defaults.DefaultTitleFont);
		areaTitle.setForeground(Defaults.DefaultWritingColour);
		areaTitle.setHorizontalAlignment(JLabel.CENTER);
		areaTitle.setOpaque(false);
		// Set start time
		JLabel label5 = new JLabel("Start: ");
		label5.setBackground(Defaults.DefaultDownloadColour);
		label5.setFont(Defaults.DefaultLabelFont);
		label5.setForeground(Defaults.DefaultWritingColour);
		label5.setHorizontalAlignment(JLabel.CENTER);
		// Create start time area
		_startTime = new TimeField("00:00:00");
		_startTime.setFont(Defaults.DefaultButtonFont);
		_startTime.setHorizontalAlignment(JLabel.CENTER);
		_startTime.setBackground(Defaults.DefaultTextFieldColour);
		_startTime.setForeground(Defaults.DefaultLoadColour);
		_startTime.addActionListener(this);
		_startTime.setPreferredSize(new Dimension(70, 30));
		// Combine into sub panel
		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
		p3.add(label5);
		p3.add(_startTime);
		p3.setOpaque(false);
		// Set start time
		JLabel label6 = new JLabel("Stop: ");
		label6.setBackground(Defaults.DefaultDownloadColour);
		label6.setFont(Defaults.DefaultLabelFont);
		label6.setForeground(Defaults.DefaultWritingColour);
		label6.setHorizontalAlignment(JLabel.CENTER);
		// Create start time area
		_stopTime = new TimeField("00:00:00");
		_stopTime.setFont(Defaults.DefaultButtonFont);
		_stopTime.setHorizontalAlignment(JLabel.CENTER);
		_stopTime.setBackground(Defaults.DefaultTextFieldColour);
		_stopTime.setForeground(Defaults.DefaultLoadColour);
		_stopTime.addActionListener(this);
		_stopTime.setPreferredSize(new Dimension(70, 30));
		// Combine into sub panel
		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		p4.add(label6);
		p4.add(_stopTime);
		p4.setOpaque(false);
		// Combine into all sub panel
		JPanel p5 = new JPanel();
		p5.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		p5.add(p3);
		p5.add(p4);
		p5.setOpaque(false);

		_text = new JTextField("Enter text here");
		_text.setFont(Defaults.DefaultTextFieldFont);
		_text.setHorizontalAlignment(JLabel.CENTER);
		_text.setBackground(Defaults.DefaultTextFieldColour);
		_text.setForeground(Defaults.DefaultLoadColour);
		_text.addActionListener(this);
		_text.setPreferredSize(new Dimension(200, 30));
		_text.setDocument(new LimitedCharModel());

		JLabel textLabel = new JLabel("Text (LIMIT "
				+ Defaults.DefaultCharLimit + " CHARS): ");
		textLabel.setFont(Defaults.DefaultLabelFont);
		textLabel.setForeground(Defaults.DefaultWritingColour);
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		textLabel.setOpaque(false);

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		p1.add(textLabel);
		p1.add(_text);
		p1.setOpaque(false);

		// Combine into trim panel
		JLabel trimPanel = new JLabel();
		trimPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
		trimPanel.add(p5);
		trimPanel.add(p1);
		trimPanel.setOpaque(false);

		_add = new JButton("Create");
		_add.setOpaque(false);
		_add.setFont(Defaults.DefaultButtonFont);
		_add.setAlignmentX(Component.CENTER_ALIGNMENT);
		_add.addActionListener(this);
		_add.setBackground(Defaults.DefaultGoButtonColour);

		// Set up preview text area
		_preview = new JButton("Add");
		_preview.setOpaque(false);
		_preview.setFont(Defaults.DefaultButtonFont);
		_preview.setAlignmentX(Component.CENTER_ALIGNMENT);
		_preview.addActionListener(this);

		_load = new JButton("Load");
		_load.setOpaque(false);
		_load.setFont(Defaults.DefaultButtonFont);
		_load.setAlignmentX(Component.CENTER_ALIGNMENT);
		_load.addActionListener(this);

		_view = new JButton("View");
		_view.setOpaque(false);
		_view.setFont(Defaults.DefaultButtonFont);
		_view.setAlignmentX(Component.CENTER_ALIGNMENT);
		_view.addActionListener(this);

		_help = new JButton("Get current time");
		_help.setOpaque(false);
		_help.setFont(Defaults.DefaultButtonFont);
		_help.setAlignmentX(Component.CENTER_ALIGNMENT);
		_help.addActionListener(this);
		
		_startsTime = new JTextField("00:00:00");
		_startsTime.setFont(Defaults.DefaultButtonFont);
		_startsTime.setHorizontalAlignment(JLabel.CENTER);
		_startsTime.setBackground(Defaults.DefaultTextFieldColour);
		_startsTime.setForeground(Defaults.DefaultLoadColour);
		_startsTime.setPreferredSize(new Dimension(70, 30));
		_startsTime.setEditable(false);

		JPanel trimssPanel = new JPanel();
		trimssPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
		trimssPanel.add(_add);
		trimssPanel.add(_preview);
		trimssPanel.add(_load);
		trimssPanel.add(_help);
		trimssPanel.add(_startsTime);
		JPanel test = new JPanel();
		test.setPreferredSize(new Dimension(140, 30));
		test.setOpaque(false);
		trimssPanel.add(test);
		trimssPanel.add(_view);
		trimssPanel.setOpaque(false);

		// Add panels
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(Defaults.DefaultFuncAreaWidth,
				Defaults.DefaultFuncAreaHeight));
		panel.setLayout(new GridLayout(4, 1, 0, 0));
		panel.add(areaTitle);
		panel.add(trimPanel);
		panel.add(trimssPanel);

		return panel;
	}

	@Override
	// This method takes actions performed on the GUI and translates them into
	// events
	public void actionPerformed(ActionEvent e) {

		// If source is extract button, start extraction
		if (e.getSource().equals(_preview)) {

			StreamWorker strim = new StreamWorker(VideoControlArea.getPath());
			strim.execute();
			String test = "0,0";
			try {
				test = strim.get();
			} catch (InterruptedException | ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if ((VideoControlArea.getPath()).equals("none")) {
				JOptionPane.showMessageDialog(null,
						"No video file currently selected", "VAMIX Error",
						JOptionPane.ERROR_MESSAGE);
			}
			

			// Check times are the right length (Special Document handles format
			// and content)
			else if (!(_startTime.getText().length() == 8 && _stopTime
					.getText().length() == 8)) {
				// Tell user they have to enter a time
				JOptionPane.showMessageDialog(this,
						"Complete start/stop times", "VAMIX Warning",
						JOptionPane.ERROR_MESSAGE);
			}
			// Double check time formats
			else if (!_startTime.getText().matches(
					"[0-9][0-9][:][0-9][0-9][:][0-9][0-9]")
					|| !_stopTime.getText().matches(
							"[0-9][0-9][:][0-9][0-9][:][0-9][0-9]")) {
				// Tell user they have to enter a time correctly
				JOptionPane.showMessageDialog(this,
						"Complete start/stop times correctly", "VAMIX Warning",
						JOptionPane.ERROR_MESSAGE);
			} else if (_text.getText().equals("")) {
				// Tell user they are dumb
				JOptionPane.showMessageDialog(this, "Please enter text to add",
						"VAMIX Warning", JOptionPane.ERROR_MESSAGE);
			} else if (test.equals("0,0") | test.equals("1,0")) {
				JOptionPane.showMessageDialog(null,
						"No video stream to add subtitles to", "VAMIX Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				String[] arr = _startTime.split(":");
				int st = convert(arr[0], arr[1], arr[2]);
				String[] arr2 = _stopTime.split(":");
				int sst = convert(arr2[0], arr2[1], arr2[2]);
				String[] arr3 = VideoControlArea._showDur.getText().split(":");
				int dur = convert(arr3[0], arr3[1], arr3[2]);

				if (st >= sst) {
					JOptionPane
							.showMessageDialog(
									this,
									"Start time comes after or is the same as the end time",
									"VAMIX Warning", JOptionPane.ERROR_MESSAGE);
				} else if (st > dur || sst > dur) {
					JOptionPane.showMessageDialog(this,
							"Times outside of duration", "VAMIX Warning",
							JOptionPane.ERROR_MESSAGE);
				} else {
					int fail = 0;
					for (int i = 0; i < list.size(); i++) {
						Subtitle toprobe = list.get(i);
						String[] arr4 = toprobe.getStart().split(":");
						int tps = convert(arr4[0], arr4[1], arr4[2]);
						String[] arr5 = toprobe.getEnd().split(":");
						int tpss = convert(arr5[0], arr5[1], arr5[2]);
						if ((st < tpss) && (sst > tps)) {
							Object[] options = { "Yes", "No" };
							int selected = JOptionPane
									.showOptionDialog(
											Sales,
											"The subtitle you are trying to add has a time clash with the subtitle \n\""
													+ toprobe.getText()
													+ "\" starting at "
													+ toprobe.getStart()
													+ " and ending at "
													+ toprobe.getEnd()
													+ ".\n Do you want to delete the conflicting subtitle?"

											, "Subtitle clash",
											JOptionPane.DEFAULT_OPTION,
											JOptionPane.WARNING_MESSAGE, null,
											options, options[0]);

							if (selected == 0) {
								list.remove(i);
								model.fireTableDataChanged();
							} else {
								fail = 1;
							}

						}
					}

					if (fail == 0) {
						list.add(new Subtitle(_startTime.getText(), _stopTime
								.getText(), _text.getText()));
						model.fireTableDataChanged();
						JOptionPane.showMessageDialog(null,
								"Subtitle added to list", "VAMIX Success",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane
								.showMessageDialog(
										null,
										"Due to subtitle clash the subtitle was not added",
										"VAMIX Error",
										JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		} else if (e.getSource().equals(_view)) {
			if (!Sales.isVisible()) {
				Sales.setVisible(true);
			} else {
				Sales.toFront();
			}
		} else if (e.getSource().equals(_help)) {
			_startsTime.setText(VideoControlArea.getCurrent());
		} else if (e.getSource().equals(_add)) {
			_add.setEnabled(false);
			_preview.setEnabled(false);
			_view.setEnabled(false);
			_load.setEnabled(false);
			if (list.size() == 0) {
				JOptionPane.showMessageDialog(null, "No subtitles in list",
						"VAMIX Error", JOptionPane.ERROR_MESSAGE);
			} else if (_ao.getOutputName().trim().equals("")){
				JOptionPane.showMessageDialog(this,
						"Please enter an output file name", "VAMIX Warning",
						JOptionPane.ERROR_MESSAGE);
			}else {
				String outSub;
				String ext = ".ass";
				if (_ao.getOutputName().trim().equals("Enter in new output file name")) {
					outSub = "subs" + ext;
				} else {
					outSub = _ao.getOutputName().trim() + ext;
				}
				if (!(_ao.getOutputLocation().equals("No location selected"))) {
					outSub = _ao.getOutputLocation() + "/" + outSub;
				}
				File f = new File(outSub);
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
						_worker = new CreateWorker(outSub, list, this);
						_worker.execute();
					}
				}else{
					_worker = new CreateWorker(outSub, list, this);
					_worker.execute();
				}
				}
			}
		}

		else if (e.getSource().equals(_load)) {

			_add.setEnabled(false);
			_preview.setEnabled(false);
			_view.setEnabled(false);
			_load.setEnabled(false);

			JFileChooser _fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					".ass subtitle files", "ass");
			_fileChooser.setFileFilter(filter);
			if (!VideoControlArea.location.equals("none")){
				File f = new File(VideoControlArea.location);
				_fileChooser.setCurrentDirectory(f);
			}
			_fileChooser.showOpenDialog(this);
			if (_fileChooser.getSelectedFile() != null) {
				VideoControlArea.location = _fileChooser.getSelectedFile().getPath();
				_worker2 = new LoadWorker(_fileChooser.getSelectedFile()
						.getPath());
				_worker2.execute();
				try {
					ArrayList<Subtitle> toswap = _worker2.get();
					list.clear();
					for (int i = 0; i < toswap.size(); i++) {
						list.add(toswap.get(i));
					}
					model.fireTableDataChanged();
					JOptionPane.showMessageDialog(null,
							"File was successfully loaded", "VAMIX Success",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (InterruptedException | ExecutionException e1) {
					JOptionPane.showMessageDialog(null,
							"Error encountered while loading file",
							"VAMIX Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		}
		_add.setEnabled(true);
		_preview.setEnabled(true);
		_view.setEnabled(true);
		_load.setEnabled(true);

	}

	public void processWorkerResults(int exitStatus) {

		if (exitStatus != 0) {
			JOptionPane.showMessageDialog(null,
					"Subtitle creation failed unexpectedly", "VAMIX Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			// Otherwise success
			JOptionPane.showMessageDialog(null,
					"File was successfully created", "VAMIX Success",
					JOptionPane.INFORMATION_MESSAGE);
		}
		_add.setEnabled(true);
		_preview.setEnabled(true);
		_view.setEnabled(true);
		_load.setEnabled(true);
	}

	public static int convert(String a, String b, String c) {
		int a2 = Integer.parseInt(a);
		int b2 = Integer.parseInt(b);
		int c2 = Integer.parseInt(c);
		int num = a2 * 60 * 60 + b2 * 60 + c2;
		return num;
	}

	@SuppressWarnings("serial")
	// This special JTextField ensures its model is a special TimeFormayDocument
	class TimeField extends JTextField {
		public TimeField() {
			super();
		}

		public String[] split(String string) {
			return this.getText().split(string);
		}

		public TimeField(String s) {
			super(s);
		}

		public TimeField(int cols) {
			super(cols);
		}

		@Override
		protected Document createDefaultModel() {
			return new TimeFormatDocument();
		}

		// This Document ensures the textfield contains numbers in the format
		// NN:NN:NN
		class TimeFormatDocument extends PlainDocument {

			// Checks inserted strings are the right format
			public void insertString(int offs, String str, AttributeSet a)
					throws BadLocationException {
				if (str == null) {
					return;
				}

				char[] chars = str.toCharArray();
				boolean ok = true;
				int length = this.getLength();

				if ((length == 2) || (length == 5)) {
					if (chars[0] != ':') {
						ok = false;
					}
				} else if (length > 7) {
					ok = false;
				} else {
					if (!Character.isDigit(chars[0])) {
						ok = false;
					}
				}

				if (ok)
					super.insertString(offs, new String(chars), a);

			}
		}
	}

	class LimitedCharModel extends PlainDocument {

		public void insertString(int offset, String str, AttributeSet attr)
				throws BadLocationException {
			if (str == null)
				return;

			// Check if less than or equal to default character limit
			if ((getLength() + str.length()) <= Defaults.DefaultCharLimit) {
				super.insertString(offset, str, attr);
			}
		}
	}
}
