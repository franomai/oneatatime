package gui;

import gui.functionalAreas.AbstractFunctionalArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import defaults.Defaults;

/**
 * This class represents the entirety of the Video Control Area, i.e, all the
 * buttons that control the VLC player. Although there are many components the
 * layout of this class is relatively simple - create component, define the
 * behavior for it and add it. Some public methods are defined to allow for ease
 * of access from other classes which rely on information about the currently
 * playing media.
 * 
 * @author fsta657
 * 
 */
@SuppressWarnings("serial")
public class VideoControlArea extends JPanel implements ActionListener {

	// GUI Fields
	private JFileChooser _chooser;
	private ToggleImageButton _play;
	private ImageButton _ff;
	private ImageButton _rw;
	private ImageButton _choose;
	private ImageButton _replay;
	private ToggleImageButton _sound;
	private MyVLCPlayer _player;
	private JSlider _time;
	private JSlider _volume;
	private static TimePanel _showTime;
	public static TimePanel _showDur;
	private SpeedPanel _showSpd;
	// Image Fields
	private Image _background;
	private static String videoPath = "none";
	public static String location = "none";
	private int ffrate = -1;
	private int rwrate = -1;
	// Timer fields
	private Timer time1 = null;
	private static long duration = 0;
	private Timer updateTimerDisplay = null;
	private boolean isChangingTime = false;
	private final long timeLength = 9001;
	private int isMutePushed = 0;
	// Flags
	private boolean _isDone = false;
	private boolean _iscalling = false;

	/**
	 * Sets the video to control, and creates and shows the buttons that will
	 * control it.
	 * 
	 * @param player
	 */
	public VideoControlArea(MyVLCPlayer player) {
		_player = player;
		createAndShowGUI();
	}

	/**
	 * Returns a String containing the path to the current video being played.
	 * Can be used for error checking or sourcing for use in functionality.
	 * 
	 * @return String containing the absolute path to the currently playing
	 *         media
	 */
	public static String getPath() {
		return videoPath;
	}

	/**
	 * Sets the String provided to be the path to the video playing. What this
	 * effectively entails is that whatever path is set will correspond to the
	 * media played if a refresh is invoked. The method is generally unused
	 * however if one wished to play an output of a function it could be done
	 * with help from this command.
	 * 
	 * @param path
	 */
	public static void setPath(String path) {
		videoPath = path;
	}

	/**
	 * Returns the duration of the current media. This method is mainly used for
	 * error checking of functionality.
	 * 
	 * @return Duration in milliseconds of the currently playing media
	 */
	public static long getDuration() {
		return duration;
	}

	/**
	 * Returns the current play back time as a formatted String by getting the
	 * text of the updating label.
	 * 
	 * @return Current time as a ##:##:## formatted string.
	 */
	public static String getCurrent() {
		return _showTime.getText();
	}

	private void createAndShowGUI() {
		// Icon set up
		this.setPreferredSize(new Dimension(Defaults.DefaultControlWidth,
				Defaults.DefaultControlHeight));
		// Get background icons
		ImageIcon backgroundIcon = new ImageIcon(
				AbstractFunctionalArea.class
						.getResource("/gui/assets/control.png"));
		_background = backgroundIcon.getImage();
		// Get play button icons
		ImageIcon playIcon = new ImageIcon(
				AbstractFunctionalArea.class
						.getResource("/gui/assets/play.png"));
		ImageIcon pauseIcon = new ImageIcon(
				AbstractFunctionalArea.class
						.getResource("/gui/assets/pause.png"));
		// Get ff icon
		ImageIcon ffIcon = new ImageIcon(
				AbstractFunctionalArea.class.getResource("/gui/assets/ff.png"));
		// Get ff icon
		ImageIcon rwIcon = new ImageIcon(
				AbstractFunctionalArea.class.getResource("/gui/assets/rw.png"));
		// Get dl icon
		ImageIcon downloadIcon = new ImageIcon(
				AbstractFunctionalArea.class
						.getResource("/gui/assets/open.png"));
		// Get replay icon
		ImageIcon replayIcon = new ImageIcon(
				AbstractFunctionalArea.class
						.getResource("/gui/assets/replay.png"));
		// Get sound icon
		ImageIcon soundIcon = new ImageIcon(
				AbstractFunctionalArea.class
						.getResource("/gui/assets/sound2.png"));
		ImageIcon muteIcon = new ImageIcon(
				AbstractFunctionalArea.class
						.getResource("/gui/assets/mute.png"));

		// Create transparent panel to add buttons to
		JPanel contentPanel = new JPanel();
		contentPanel.setOpaque(false);
		contentPanel.setLayout(new BorderLayout());

		// Create front panel for non-slider things
		JPanel frontPanel = new JPanel();
		frontPanel.setOpaque(false);
		frontPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));
		frontPanel.setPreferredSize(new Dimension(Defaults.DefaultControlWidth,
				Defaults.DefaultControlHeight - 20));

		// Make open file area
		_choose = new ImageButton(downloadIcon);
		_choose.addActionListener(this);
		_choose.setPreferredSize(new Dimension(40, 40));

		// Make replay button
		_replay = new ImageButton(replayIcon);
		_replay.addActionListener(this);
		_replay.setPreferredSize(new Dimension(40, 40));
		_replay.setEnabled(false);

		// Make video time control area
		// Make play button
		_play = new ToggleImageButton(playIcon, pauseIcon);
		_play.addActionListener(this);
		_play.setPreferredSize(new Dimension(40, 40));
		_play.setEnabled(false);
		// Make fast forward button
		_ff = new ImageButton(ffIcon);
		_ff.addActionListener(this);
		_ff.setPreferredSize(new Dimension(35, 35));
		_ff.setEnabled(false);
		// Make fast forward button
		_rw = new ImageButton(rwIcon);
		_rw.addActionListener(this);
		_rw.setPreferredSize(new Dimension(35, 35));
		_rw.setEnabled(false);
		// Make fast forward button
		_showSpd = new SpeedPanel();
		// Combine into sub panel
		JPanel p1 = new JPanel();
		p1.setOpaque(false);
		p1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		p1.add(_rw);
		p1.add(_play);
		p1.add(_ff);
		p1.add(_showSpd);

		// Make sound control area
		_sound = new ToggleImageButton(soundIcon, muteIcon);
		_sound.addActionListener(this);
		_sound.setPreferredSize(new Dimension(40, 40));
		_sound.setEnabled(false);
		// Make slider
		_volume = new JSlider(0, 125, 100);
		_volume.putClientProperty("JSlider.isFilled", Boolean.TRUE);
		_volume.setPreferredSize(new Dimension(50, 15));
		_volume.setEnabled(false);
		_volume.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				EmbeddedMediaPlayer p = _player.getMediaPlayer();
				_sound.setSelected(false);
				p.setVolume(_volume.getValue());
				if (p.isMute()) {
					p.mute();
				}
				if (_volume.getValue() == 0) {
					_sound.setSelected(true);
					if (!p.isMute()) {
						p.mute();
					}
				}
			}

		});
		// Combine into sub panel
		JPanel p2 = new JPanel();
		p2.setOpaque(false);
		p2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		p2.add(_sound);
		p2.add(_volume);

		// Combine into front panel
		frontPanel.add(_choose);
		frontPanel.add(_replay);
		frontPanel.add(p1);
		frontPanel.add(p2);

		// Make time slider
		_time = new JSlider(0, (int) timeLength, 0);
		_time.putClientProperty("JSlider.isFilled", Boolean.TRUE);
		_time.setEnabled(false);
		_time.setPreferredSize(new Dimension(375, 13));
		_time.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (_time.isEnabled() && !_isDone) {
					isChangingTime = true;
					Point p = e.getPoint();
					double percent = p.x / ((double) 375);// getWidth());
					int range = _time.getMaximum() - _time.getMinimum();
					double newVal = range * percent;
					int result = (int) (_time.getMinimum() + newVal);
					_time.setValue(result);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (_time.isEnabled() && !_isDone) {
					EmbeddedMediaPlayer p = _player.getMediaPlayer();
					p.setTime((long) (((double) (_time.getValue()) / timeLength) * duration));
					_showTime.updateTime(_player.getMediaPlayer().getTime());

					isChangingTime = false;
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// Unrequired and hence left blank.

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// Unrequired and hence left blank.

			}

		});

		// Make show time area
		_showTime = new TimePanel();
		_showDur = new TimePanel();
		// Combine into time panel
		JPanel timePanel = new JPanel();
		timePanel.setOpaque(false);
		timePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, -1));
		timePanel.add(_showTime);
		timePanel.add(_time);
		timePanel.add(_showDur);

		// Combine into content panel
		contentPanel.add(frontPanel, BorderLayout.NORTH);
		contentPanel.add(timePanel, BorderLayout.SOUTH);

		this.setOpaque(false);
		this.add(contentPanel);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(_background, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		EmbeddedMediaPlayer p = _player.getMediaPlayer();
		// The below essentially handles ff/rw by setting timers to perform skips frequently, and
		// the GUI is updated to reflect the changes brought upon by these skips. 
		// Flags are set to indicate to other components if ff/rw is occuring.
		if (e.getSource().equals(_ff)) {
			if (!p.isMute()) {
				p.mute();
			}
			if (ffrate == 1) {
				if (time1 != null) {
					time1.cancel();
				}
				time1 = new Timer();
				time1.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						if (!isChangingTime) {
							_player.getMediaPlayer().skip(400);
							_time.setValue((int) ((double) (_player
									.getMediaPlayer().getTime()) / duration * timeLength));
							_showTime.updateTime(_player.getMediaPlayer()
									.getTime());
							_showSpd.updateSpeed("x3");
						}

					}
				}, 0, 100);
				ffrate = 2;
			} else if (ffrate == 2) {
				if (time1 != null) {
					time1.cancel();
				}
				time1 = new Timer();
				time1.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						if (!isChangingTime) {
							_player.getMediaPlayer().skip(600);
							_time.setValue((int) ((double) (_player
									.getMediaPlayer().getTime()) / duration * timeLength));
							_showTime.updateTime(_player.getMediaPlayer()
									.getTime());
							_showSpd.updateSpeed("x4");
						}

					}
				}, 0, 100);
				ffrate = 0;
			} else {

				if (time1 != null) {
					time1.cancel();
					rwrate = -1;
				}
				if (ffrate == -1) {
					_player.getMediaPlayer().pause();
					_play.setSelected(false);
				}
				time1 = new Timer();
				time1.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						if (!isChangingTime) {
							_player.getMediaPlayer().skip(200);
							_time.setValue((int) ((double) (_player
									.getMediaPlayer().getTime()) / duration * timeLength));
							_showTime.updateTime(_player.getMediaPlayer()
									.getTime());
							_showSpd.updateSpeed("x2");
						}

					}
				}, 0, 100);
				ffrate = 1;
			}

		}

		if (e.getSource().equals(_rw)) {
			if (!p.isMute()) {
				p.mute();
			}
			if (rwrate == 1) {
				if (time1 != null) {
					time1.cancel();
				}
				time1 = new Timer();
				time1.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						if (!isChangingTime) {
							_player.getMediaPlayer().skip(-400);
							_time.setValue((int) ((double) (_player
									.getMediaPlayer().getTime()) / duration * timeLength));
							_showTime.updateTime(_player.getMediaPlayer()
									.getTime());
							_showSpd.updateSpeed("x3");
						}
					}
				}, 0, 100);
				rwrate = 2;
			} else if (rwrate == 2) {
				if (time1 != null) {
					time1.cancel();
				}
				time1 = new Timer();
				time1.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						if (!isChangingTime) {
							_player.getMediaPlayer().skip(-600);
							_time.setValue((int) ((double) (_player
									.getMediaPlayer().getTime()) / duration * timeLength));
							_showTime.updateTime(_player.getMediaPlayer()
									.getTime());
							_showSpd.updateSpeed("x4");
						}

					}
				}, 0, 100);
				rwrate = 0;
			} else {

				if (time1 != null) {
					time1.cancel();
					ffrate = -1;
				}
				if (rwrate == -1) {
					_player.getMediaPlayer().pause();
					_play.setSelected(false);
				}
				time1 = new Timer();
				time1.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						if (!isChangingTime) {
							_player.getMediaPlayer().skip(-200);
							_time.setValue((int) ((double) (_player
									.getMediaPlayer().getTime()) / duration * timeLength));
							_showTime.updateTime(_player.getMediaPlayer()
									.getTime());
							_showSpd.updateSpeed("x2");
						}

					}
				}, 0, 100);
				rwrate = 1;
			}

		}

		// If source is play/pause button
		if (e.getSource().equals(_play)) {
			_showSpd.updateSpeed("x1");
			if (time1 != null) {
				time1.cancel();
				if (p.isMute() && (isMutePushed == 0)) {
					p.mute();
				}
				ffrate = -1;
				rwrate = -1;
			}
			if (videoPath != null) {
				if (p.isPlaying()) {
					if (p.canPause()) {
						p.pause();
						_play.setSelected(false);
					}
				} else {
					// Start the media player
					p.play();
					_play.setSelected(true);
				}
			}
		}

		if (e.getSource().equals(_sound)) {
			p.mute();
			if (isMutePushed == 0) {
				isMutePushed = 1;
			} else {
				isMutePushed = 0;
			}
		}

		// If source is open file button
		if (e.getSource().equals(_choose)) {
			// Check if file chooser not already open
			if (_chooser != null) {
				// If open, do nothing
			} else {
				// Otherwise, open file chooser
				_chooser = new JFileChooser();
				if (p.isPlaying()) {
					p.pause();
					_play.setSelected(false);
				}
				// Set filter
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Media", "mp4", "mpeg", "mov", "wmv", "mpg", "mp3",
						"wav", "mkv", "avi");
				_chooser.setFileFilter(filter);
				if (!location.equals("none")) {
					File f = new File(location);
					_chooser.setCurrentDirectory(f);
				}
				// Get users return
				int returnVal = _chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// If chosen file is okay, get path
					VideoControlArea.setPath((_chooser.getSelectedFile()
							.getPath()));
					videoPath = VideoControlArea.getPath();
					location = VideoControlArea.getPath();
					p.playMedia(videoPath);
					_replay.setEnabled(true);
					_play.setEnabled(true);
					_ff.setEnabled(true);
					_rw.setEnabled(true);
					_sound.setEnabled(true);
					_volume.setEnabled(true);
					_play.setSelected(true);
					_time.setEnabled(true);
					_time.setValue(0);
					_isDone = false;
					if (time1 != null) {
						time1.cancel();
						_showSpd.updateSpeed("x1");
						if (p.isMute() && (isMutePushed == 0)) {
							p.mute();
						}
						ffrate = -1;
						rwrate = -1;
					}
					updateTimerDisplay = new Timer();
					updateTimerDisplay.scheduleAtFixedRate(new TimerTask() {

						@Override
						public void run() {
							if (!isChangingTime
									&& _player.getMediaPlayer().isPlaying()) {
								duration = _player.getMediaPlayer().getLength();
								_time.setValue((int) ((double) (_player
										.getMediaPlayer().getTime()) / duration * timeLength));
								_showTime.updateTime(_player.getMediaPlayer()
										.getTime());
								_iscalling = true;
								_showDur.updateTime(duration);
								_iscalling = false;
							}
						}
					}, 0, 250);
				}
			}
			// Set chooser back to null
			_chooser = null;
		} else if (e.getSource().equals(_replay)) {
			if (!_player.getMediaPlayer().isPlaying()) {
				_play.setSelected(true);
			}
			if (time1 != null) {
				time1.cancel();
				_showSpd.updateSpeed("x1");
				if (p.isMute() && (isMutePushed == 0)) {
					p.mute();
				}
				ffrate = -1;
				rwrate = -1;
				;
			}
			_isDone = false;
			_play.setEnabled(true);
			_ff.setEnabled(true);
			_rw.setEnabled(true);
			_sound.setEnabled(true);
			_volume.setEnabled(true);
			_time.setEnabled(true);
			_player.getMediaPlayer().playMedia(videoPath);
		}

	}

	class ToggleImageButton extends JToggleButton {

		public ToggleImageButton(ImageIcon icon, ImageIcon setIcon) {
			setRolloverEnabled(false);
			setIcon(icon);
			setSelectedIcon(setIcon);
			setBackground(new Color(0, 0, 0, 0));
		}
	}

	class ImageButton extends JButton {

		public ImageButton(ImageIcon icon) {
			setIcon(icon);
			setBackground(new Color(0, 0, 0, 0));
		}
	}

	public class TimePanel extends JLabel {

		public TimePanel() {
			setText("00:00:00");
			setFont(Defaults.DefaultTextFieldFont);
			setForeground(Defaults.DefaultWritingColour);
			setOpaque(false);
			setHorizontalAlignment(JLabel.CENTER);
			// setSize(new Dimension(40,20));
		}

		public void updateTime(long milliseconds) {
			if (milliseconds >= _player.getMediaPlayer().getLength()
					- (long) 500
					&& _iscalling != true) {
				milliseconds = duration;
				_isDone = true;
				_play.setEnabled(false);
				_ff.setEnabled(false);
				_rw.setEnabled(false);
				_sound.setEnabled(false);
				_volume.setEnabled(false);
				_time.setEnabled(false);

				// Convert to correct ints
				int seconds = (int) (milliseconds / 1000) % 60;
				int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
				int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
				// Turn to strings
				String update = "";
				if (hours < 10) {
					update = update + "0";
				}
				update = update + hours + ":";
				if (minutes < 10) {
					update = update + "0";
				}
				update = update + minutes + ":";
				if (seconds < 10) {
					update = update + "0";
				}
				update = update + seconds;
				setText(update);
				revalidate();
				repaint();
			}
			if (!_isDone) {
				// Convert to correct ints
				int seconds = (int) (milliseconds / 1000) % 60;
				int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
				int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
				// Turn to strings
				String update = "";
				if (hours < 10) {
					update = update + "0";
				}
				update = update + hours + ":";
				if (minutes < 10) {
					update = update + "0";
				}
				update = update + minutes + ":";
				if (seconds < 10) {
					update = update + "0";
				}
				update = update + seconds;
				setText(update);
				revalidate();
				repaint();
			}
		}
	}

	class SpeedPanel extends JLabel {

		public SpeedPanel() {
			setText("x1");
			setFont(Defaults.DefaultLabelFont);
			setForeground(Defaults.DefaultWritingColour);
			setOpaque(false);
			setHorizontalAlignment(JLabel.CENTER);
			setSize(new Dimension(40, 20));
		}

		public void updateSpeed(String s) {
			setText(s);
			revalidate();
			repaint();
		}
	}
}