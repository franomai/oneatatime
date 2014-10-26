package gui.functionalAreas;

import gui.Main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import defaults.Defaults;
import defaults.Strings;

public class FunctionalAreaChooser extends JPanel {

	// ADD SOME TOOL TIPS TO BUTTONS

	/*
	 * STRING FIELDS
	 */
	// This string describes which section the user is in
	private String _section;
	// This string describes which option for the section the user is in
	private String _option;
	// This string describes which level for the option the user is in
	private String _level;
	/*
	 * GUI FIELDS
	 */
	private SectionButton _fileSection;
	private SectionButton _textSection;
	private SectionButton _audioSection;
	private SectionButton _subSection;
	private JPanel _sectionPanel;
	private ArrayList<SectionButton> _sectionList;

	private OptionButton _downloadOption;
	private OptionButton _trimOption;
	private OptionButton _uploadOption;
	private OptionButton _createTitleOption;
	private OptionButton _createCreditsOption;
	private OptionButton _stripOption;
	private OptionButton _replaceOption;
	private OptionButton _overlayOption;
	private OptionButton _extractOption;
	private OptionButton _addOption;
	private OptionButton _editOption;
	private JPanel _optionPanel;
	private ArrayList<OptionButton> _optionList;

	private LevelButton _basicLevel;
	private LevelButton _advancedLevel;
	private JPanel _levelPanel;
	private ArrayList<LevelButton> _levelList;

	private Image _background;

	/*
	 * NUMBER FIELDS
	 */
	// Numbers
	private static final int _numSections = 4;
	// Ratios
	private static final double _sectionWidth = 0.4;
	private static final double _optionWidth = 0.35;
	private static final double _levelWidth = 0.25;

	/*
	 * OTHER FIELDS
	 */
	// Reference Field
	private final FunctionalAreaSwitcher _s;
	// Constant Field
	private final List<String> _noAdvanced = Arrays
			.asList(new String[] { "Trim" });

	public FunctionalAreaChooser(FunctionalAreaSwitcher s) {

		/*
		 * SELF SET-UP
		 */

		setPreferredSize(new Dimension(Defaults.DefaultFuncChooserWidth,
				Defaults.DefaultFuncAreaHeight));
		ImageIcon icon = new ImageIcon(
				AbstractFunctionalArea.class
						.getResource("/gui/assets/chooserMetal.jpg"));
		_background = icon.getImage();

		/*
		 * FIELD SET-UP
		 */

		// Give reference to switcher
		_s = s;

		// String fields
		_section = "File";
		_option = "Download";
		_level = "Basic";

		// Array fields
		_sectionList = new ArrayList<SectionButton>();
		SectionButton[] otherListA = new SectionButton[] { _fileSection,
				_textSection, _audioSection };
		_sectionList.addAll(Arrays.asList(otherListA));

		_optionList = new ArrayList<OptionButton>();
		OptionButton[] otherListB = new OptionButton[] { _downloadOption,
				_trimOption, _createTitleOption, _stripOption, _replaceOption,
				_overlayOption };
		_optionList.addAll(Arrays.asList(otherListB));

		_levelList = new ArrayList<LevelButton>();
		LevelButton[] otherListC = new LevelButton[] { _basicLevel,
				_advancedLevel };
		_levelList.addAll(Arrays.asList(otherListC));

		// Section set-up
		int sectionWidth = (int) ((Defaults.DefaultFuncChooserWidth) * _sectionWidth);
		// Set up section buttons
		_fileSection = new SectionButton("File", Defaults.DefaultDownloadColour);
		_fileSection.setToolTipText(Strings.FileTooltip);
		_fileSection.setMnemonic('F');
		_textSection = new SectionButton("Text", Defaults.DefaultTextColour);
		_textSection.setToolTipText(Strings.TextTooltip);
		_textSection.setMnemonic('T');
		_audioSection = new SectionButton("Audio", Defaults.DefaultAudioColour);
		_audioSection.setToolTipText(Strings.AudioTooltip);
		_audioSection.setMnemonic('A');
		_subSection = new SectionButton("Subtitles", Defaults.DefaultSubsColour);
		_subSection.setToolTipText(Strings.SubTooltip);
		_subSection.setMnemonic('S');

		// Add to panel
		_sectionPanel = new JPanel();
		_sectionPanel.setLayout(new GridLayout(_numSections, 1, 5, 1));
		_sectionPanel.setPreferredSize(new Dimension(sectionWidth,
				Defaults.DefaultFuncAreaHeight));
		_sectionPanel.add(_fileSection);
		_sectionPanel.add(_textSection);
		_sectionPanel.add(_audioSection);
		_sectionPanel.add(_subSection);
		_sectionPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK), "Section",
				TitledBorder.CENTER, TitledBorder.TOP,
				Defaults.DefaultTextFieldFont));
		_sectionPanel.setOpaque(false);

		// Option set-upAudio
		int optionHeight = Defaults.DefaultFuncAreaHeight;
		int optionWidth = (int) (Defaults.DefaultFuncChooserWidth * _optionWidth);
		// Make file card
		JPanel fileCard = new JPanel();
		fileCard.setLayout(new GridLayout(3, 1, 5, 5));
		_downloadOption = new OptionButton("Download",
				Defaults.DefaultDownloadColour);
		_downloadOption.setPreferredSize(new Dimension(optionWidth,
				optionHeight));
		_downloadOption.setToolTipText(Strings.DownloadTooltip);
		KeyListener a9 = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if ((keyEvent.getKeyChar() == 'H')) {
					JOptionPane
							.showMessageDialog(
									null,
									"The download panel provides the tools to download a file from the internet, which can then be used in VAMIX.\n"
									+"Pressing the DOWNLOAD button will attempt to download whatever is currently entered in the enter URL field.\n"
											+"As such, please ensure the URL entered is a valid one. An error message will be displayed if an error does occur.\n"
									+"The progress of the current download will be shown in the progress bar, and the process can be cancelled at any time using the CANCEL button.\n"
											+"Options for editing the output file name and location can be found in the advanced options.",
									"VAMIX Help", JOptionPane.QUESTION_MESSAGE);
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};
		_downloadOption.addKeyListener(a9);
		_downloadOption.setEnabled(false);
		fileCard.add(_downloadOption);
		_trimOption = new OptionButton("Trim", Defaults.DefaultDownloadColour);
		KeyListener a10 = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if ((keyEvent.getKeyChar() == 'H')) {
					JOptionPane
							.showMessageDialog(
									null,
									"The trim panel provides the tools to cut a given media file to a given duration, which can then be used in VAMIX.\n"
									+"To select a media file for trimming use the SELECT button. This will open a file chooser where the desired file can be selected.\n" +
									"To preview the selected audio file the PREVIEW button, and the file will play in a seperate window .\n"
									+"Durations are specified in the start and duration fields. An output file name can also be specified in the appropriate field.\n"
									+ "After all these fields have been filled in correctly the TRIM button can be pressed and the process will be carried out.\n"
									+"A success or failure message will be displayed on screen when the operation is completed.",
									"VAMIX Help", JOptionPane.QUESTION_MESSAGE);
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};
		_trimOption.addKeyListener(a10);
		_trimOption.setPreferredSize(new Dimension(optionWidth, optionHeight));
		_trimOption.setToolTipText(Strings.TrimTooltip);
		_trimOption.setEnabled(false);
		fileCard.add(_trimOption);
		_uploadOption = new OptionButton("Upload",
				Defaults.DefaultDownloadColour);
		KeyListener a11 = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if ((keyEvent.getKeyChar() == 'H')) {
					JOptionPane
							.showMessageDialog(
									null,
									"The upload panel provides the tools to upload the currently playing media (preferably media that you've edited) to the internet and share it with other VAMIX users.\n"
									+"Pressing the UPLOAD button will take whatever is playing and upload it to the VAMIX server. The time taken can vary depending on your internet connection.\n" +
									"Once the upload is completed a URL will be generated and this URL can be used to access the uploaded media from any browser or via the download option of VAMIX.\n"+
									"Pressing the SHARE button will share this link with other VAMIX users, and shared links can be viewed from the advanced options.",
									"VAMIX Help", JOptionPane.QUESTION_MESSAGE);
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};
		_uploadOption.addKeyListener(a11);
		_uploadOption
				.setPreferredSize(new Dimension(optionWidth, optionHeight));
		_uploadOption.setToolTipText(Strings.UploadTooltip);
		_uploadOption.setEnabled(false);
		fileCard.add(_uploadOption);
		fileCard.setOpaque(false);
		// Make text card
		JPanel textCard = new JPanel();
		textCard.setLayout(new GridLayout(2, 1, 5, 5));
		optionHeight = (int) Defaults.DefaultFuncAreaHeight / 2;
		_createTitleOption = new OptionButton("Title",
				Defaults.DefaultTextColour);
		KeyListener a7 = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if ((keyEvent.getKeyChar() == 'H')) {
					JOptionPane
							.showMessageDialog(
									null,
									"The add title panel provides the tools to add text to the opening of the currently playing video.\n"
											+ "Entering text in the output name field will send the ouput to a file with the name provided. If this is left as is a default will be selected.\n"
											+ "Entering a value in the Display Time field determines how long the text will be displayed. Leaving this blank will cause the operation to fail.\n "
											+"The position selector will determine where on the screen the text will be displayed, either 1/8 of the way down, midway, or 7/8ths of the way.\n"
											+"The ADD TEXT button will carry out the operation, whereas the PREIVEW button will display a sample in a seperate window.\n"
											+"Finally, the text field will determine what text will be displayed. Leaving this empty is okay, but nothing will show up.\n"
											+"More options for customisation are available in the advanced options, including functionality that allows for a vamsets file to be\n"
											+"created from the current settings or imported, and this can be done using the SAVE and LOAD buttons respecitviely.",
									"VAMIX Help", JOptionPane.QUESTION_MESSAGE);
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};
		_createTitleOption.addKeyListener(a7);
		textCard.add(_createTitleOption);
		_createTitleOption.setPreferredSize(new Dimension(optionWidth,
				optionHeight));
		
		_createTitleOption.setToolTipText(Strings.TitleTooltip);
		_createCreditsOption = new OptionButton("Credits",
				Defaults.DefaultTextColour);
		KeyListener a8 = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if ((keyEvent.getKeyChar() == 'H')) {
					JOptionPane
							.showMessageDialog(
									null,
									"The add credits panel provides the tools to add text to the end of the currently playing video.\n"
									+"This panel works similarly to add title panel bar when the text displays on the screen.\n"
											+ "Entering text in the output name field will send the ouput to a file with the name provided. If this is left as is a default will be selected.\n"
											+ "Entering a value in the Display Time field determines how long the text will be displayed. Leaving this blank will cause the operation to fail.\n "
											+"The position selector will determine where on the screen the text will be displayed, either 1/8 of the way down, midway, or 7/8ths of the way.\n"
											+"The ADD TEXT button will carry out the operation, whereas the PREIVEW button will display a sample in a seperate window.\n"
											+"Finally, the text field will determine what text will be displayed. Leaving this empty is okay, but nothing will show up.\n"
											+"More options for customisation are available in the advanced options, including functionality that allows for a vamsets file to be\n"
											+"created from the current settings or imported, and this can be done using the SAVE and LOAD buttons respecitviely.",
									"VAMIX Help", JOptionPane.QUESTION_MESSAGE);
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};
		_createCreditsOption.addKeyListener(a8);
		textCard.add(_createCreditsOption);
		_createCreditsOption.setPreferredSize(new Dimension(optionWidth,
				optionHeight));
		_createCreditsOption.setToolTipText(Strings.CreditsTooltip);
		textCard.setOpaque(false);
		// Make audio card
		JPanel audioCard = new JPanel();
		audioCard.setLayout(new GridLayout(3, 1, 5, 5));
		optionHeight = (int) Defaults.DefaultFuncAreaHeight / 3;
		_stripOption = new OptionButton("Strip", Defaults.DefaultAudioColour);
		KeyListener a4 = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if ((keyEvent.getKeyChar() == 'H')) {
					JOptionPane
							.showMessageDialog(
									null,
									"The strip audio panel provides the tools to remove the audio from the current video if there is any.\n"
											+ "This operation can be run on audio files and will be successful however the output will be the same as the input.\n"
											+ "The STRIP button will check for audio and export any if it is found.\n"
											+"This process can be cancelled using the CANCEL button. If it is not cancelled a mp3 will be created with the desired name in the desired location.",
									"VAMIX Help", JOptionPane.QUESTION_MESSAGE);
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};
		_stripOption.addKeyListener(a4);
		audioCard.add(_stripOption);
		_stripOption.setPreferredSize(new Dimension(optionWidth, optionHeight));
		_stripOption.setToolTipText(Strings.StripTooltip);
		_replaceOption = new OptionButton("Replace",
				Defaults.DefaultAudioColour);
		KeyListener a5 = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if ((keyEvent.getKeyChar() == 'H')) {
					JOptionPane
							.showMessageDialog(
									null,
									"The replace panel provides the tools to replace the audio of the current video with that from another file.\n"
											+ "To select an audio file to replace the current audio with use the SELECT button. This will open a file chooser where the desired file can be selected.\nTo preview the selected audio use the PREVIEW button, and the audio will play in a seperate window .\n"
											+ "The REPLACE button will take the currently selected audio and begin the swap.\n"
											+"This process can be cancelled using the CANCEL button. If it is not cancelled a video file will be created with the desired name in the desired location.",
									"VAMIX Help", JOptionPane.QUESTION_MESSAGE);
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};
		_replaceOption.addKeyListener(a5);
		audioCard.add(_replaceOption);
		_replaceOption
				.setPreferredSize(new Dimension(optionWidth, optionHeight));
		_replaceOption.setToolTipText(Strings.ReplaceTooltip);
		_overlayOption = new OptionButton("Overlay",
				Defaults.DefaultAudioColour);
		KeyListener a6 = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if ((keyEvent.getKeyChar() == 'H')) {
					JOptionPane
							.showMessageDialog(
									null,
									"The overlay panel provides the tools to overlay the audio of the current video with that from another file.\n"
									+ "This works similarly to the replace option however this will merge the current audio in instead of discarding it.\n"
											+ "To select an audio file to replace the current audio with use the SELECT button. This will open a file chooser where the desired file can be selected.\nTo preview the selected audio use the PREVIEW button, and the audio will play in a seperate window .\n"
											+ "The OVERLAY button will take the currently selected audio and begin the merge.\n"
											+"This process can be cancelled using the CANCEL button. If it is not cancelled a video file will be created with the desired name in the desired location.",
									"VAMIX Help", JOptionPane.QUESTION_MESSAGE);
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};
		_overlayOption.addKeyListener(a6);
		audioCard.add(_overlayOption);
		audioCard.setOpaque(false);
		_overlayOption
				.setPreferredSize(new Dimension(optionWidth, optionHeight));
		_overlayOption.setToolTipText(Strings.OverlayTooltip);
		// Make subs card
		JPanel subsCard = new JPanel();
		subsCard.setLayout(new GridLayout(3, 1, 5, 5));
		optionHeight = (int) Defaults.DefaultFuncAreaHeight / 3;
		_extractOption = new OptionButton("Extract", Defaults.DefaultSubsColour);
		KeyListener a3 = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if ((keyEvent.getKeyChar() == 'H')) {
					JOptionPane
							.showMessageDialog(
									null,
									"The extract subtitles panel provides the tools to remove the subtitles from the current video if they so exist.\n"
											+ "The EXTRACT button will check for subtitles in the current video that are extractable.\n"
											+ "If none are found a warning will printed to the screen."
											+"\nOtherwise the subtitles found will be extracted and saved in a .ass file either in the default location or the one selected.",
									"VAMIX Help", JOptionPane.QUESTION_MESSAGE);
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};
		_extractOption.addKeyListener(a3);
		subsCard.add(_extractOption);
		_extractOption
				.setPreferredSize(new Dimension(optionWidth, optionHeight));
		_extractOption.setToolTipText(Strings.ExtractTooltip);
		_addOption = new OptionButton("Add", Defaults.DefaultSubsColour);
		KeyListener a2 = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if ((keyEvent.getKeyChar() == 'H')) {
					JOptionPane
							.showMessageDialog(
									null,
									"The add subtitles panel provides the tools to add a pre-existing subtitle file"
											+ " to the currently playing video.\n The SELECT button brings up a file chooser that can only access .ass subtitle files.\n "
											+ "The ADD button adds the currently selected .ass file, or one is not selected will prompt for one.\n"
											+"Once pressed the process will execute, and it can be CANCELed at any time.",
									"VAMIX Help", JOptionPane.QUESTION_MESSAGE);
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};
		_addOption.addKeyListener(a2);
		subsCard.add(_addOption);
		_addOption.setPreferredSize(new Dimension(optionWidth, optionHeight));
		_addOption.setToolTipText(Strings.AddTooltip);
		_editOption = new OptionButton("Create", Defaults.DefaultSubsColour);
		KeyListener a1 = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if ((keyEvent.getKeyChar() == 'H')) {
					JOptionPane
							.showMessageDialog(
									null,
									"The create subtitles panel provides the tools to create a subtitle file"
											+ " that can then be added to a video.\n All subtitles for the current operation are stored"
											+ " in a list, and this list can be viewed using the VIEW button.\n The LOAD button allows"
											+ " for a .ass subtitle file to be swapped in for the current list.\n The ADD button takes in"
											+ " the start and the end times for the text provided and adds them to the list.\n Note this is different"
											+ " to the add option in the options menu which adds subtitles to the current video.\nOnce you"
											+ " are happy with the subtitle collection the CREATE button will create a file with these subtitles.\n"
											+ "To add these subtitles to the video you are viewing please use the add subtitles panel.",
									"VAMIX Help", JOptionPane.QUESTION_MESSAGE);
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};
		_editOption.addKeyListener(a1);
		subsCard.add(_editOption);
		subsCard.setOpaque(false);
		_editOption.setPreferredSize(new Dimension(optionWidth, optionHeight));
		_editOption.setToolTipText(Strings.CreateTooltip);
		// Combine
		_optionPanel = new JPanel(new CardLayout());
		_optionPanel.add(fileCard, "File");
		_optionPanel.add(textCard, "Text");
		_optionPanel.add(audioCard, "Audio");
		_optionPanel.add(subsCard, "Subtitles");
		_optionPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK), "Option",
				TitledBorder.CENTER, TitledBorder.TOP,
				Defaults.DefaultTextFieldFont));
		_optionPanel.setOpaque(false);

		// Level set-up
		int levelHeight = Defaults.DefaultFuncAreaHeight / 2;
		int levelWidth = (int) (Defaults.DefaultFuncChooserWidth * _levelWidth);
		_basicLevel = new LevelButton("Basic", Defaults.DefaultBasicColour);
		_basicLevel.setPreferredSize(new Dimension(levelWidth, levelHeight));
		_basicLevel.setToolTipText(Strings.BasicTooltip);
		_basicLevel.setEnabled(false);
		_advancedLevel = new LevelButton("Advanced", Defaults.DefaultAdvColour);
		_advancedLevel.setPreferredSize(new Dimension(levelWidth, levelHeight));
		_advancedLevel.setToolTipText(Strings.AdvancedTooltip);
		_advancedLevel.setEnabled(false);
		// Add to panel
		_levelPanel = new JPanel();
		_levelPanel.setLayout(new GridLayout(2, 1, 5, 5));
		_levelPanel.setPreferredSize(new Dimension(levelWidth,
				Defaults.DefaultFuncAreaHeight));
		_levelPanel.add(_basicLevel);
		_levelPanel.add(_advancedLevel);
		_levelPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK), "Level",
				TitledBorder.CENTER, TitledBorder.TOP,
				Defaults.DefaultTextFieldFont));
		_levelPanel.setOpaque(false);

		// Finalize
		setLayout(new GridLayout(1, 3, 5, 5));
		add(_sectionPanel);
		add(_optionPanel);
		add(_levelPanel);
		// Update selections
		// editSelections();
		this.setBackground(new Color(255, 255, 255));
		this.setOpaque(true);

	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(_background, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	private void editSelections() {
		// Fix up section selections
		toggleButton(_fileSection, _section);
		toggleButton(_textSection, _section);
		toggleButton(_audioSection, _section);
		toggleButton(_subSection, _section);
		// Fix up option selections
		toggleButton(_downloadOption, _option);
		toggleButton(_uploadOption, _option);
		toggleButton(_trimOption, _option);
		toggleButton(_createTitleOption, _option);
		toggleButton(_createCreditsOption, _option);
		toggleButton(_stripOption, _option);
		toggleButton(_replaceOption, _option);
		toggleButton(_overlayOption, _option);
		toggleButton(_extractOption, _option);
		toggleButton(_addOption, _option);
		toggleButton(_editOption, _option);
		// Fix up level selections
		if (_noAdvanced.contains(_option)) {
			_advancedLevel.setEnabled(false);
		} else {
			_advancedLevel.setEnabled(true);
		}
		toggleButton(_basicLevel, _level);
		toggleButton(_advancedLevel, _level);
	}

	private void toggleButton(ChooserButton c, String s) {
		if (!c.getText().equals(s)) {
			c.setSelected(false);
		} else {
			c.setSelected(true);
		}
	}

	// When selections change, go through buttons and set unselected if
	// name/=field

	abstract class ChooserButton extends JToggleButton {

		public ChooserButton(String name, Color c) {
			super(name);
			// Display colour
			setBackground(c);
			setForeground(Defaults.DefaultWritingColour);
			setFont(Defaults.DefaultButtonFont);
		}
	}

	class SectionButton extends ChooserButton implements ActionListener {

		public SectionButton(String name, Color c) {
			super(name, c);
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// Change section text and option panel
			_section = getText();
			CardLayout cl = (CardLayout) (_optionPanel.getLayout());
			cl.show(_optionPanel, this.getText());
			// Set default option for this section
			if (getText().equals("File")) {
				_option = "Download";
			} else if (getText().equals("Text")) {
				_option = "Title";
			} else if (getText().equals("Audio")) {
				_option = "Strip";
			} else if (getText().equals("Subtitles")) {
				_option = "Extract";
			}
			_level = "Basic";
			_downloadOption.setEnabled(true);
			_trimOption.setEnabled(true);
			_uploadOption.setEnabled(true);
			_basicLevel.setEnabled(true);
			_advancedLevel.setEnabled(true);
			// Fix up selections
			FunctionalAreaChooser.this.editSelections();
			// Tell s to change functional area
			_s.switchPanel(_option, _level);
		}
	}

	class OptionButton extends ChooserButton implements ActionListener {

		public OptionButton(String name, Color c) {
			super(name, c);
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			_option = getText();
			// Set level to basic
			_level = "Basic";
			// Fix up selections
			FunctionalAreaChooser.this.editSelections();
			// Tell s to change functional area
			_s.switchPanel(_option, _level);
		}
	}

	class LevelButton extends ChooserButton implements ActionListener {

		public LevelButton(String name, Color c) {
			super(name, c);
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			_level = getText();
			// Fix up selections
			FunctionalAreaChooser.this.editSelections();
			// Tell s to change functional area
			_s.switchPanel(_option, _level);
		}
	}
}
