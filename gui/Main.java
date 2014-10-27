package gui;

import gui.functionalAreas.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.InputStream;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import com.sun.jna.Native;
import defaults.Defaults;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * This is the main class, in that it initializes the GUI. It gets the look and
 * feel and associated libraries, and then constructs the GUI bit my bit,
 * initializing each element and adding it to the GUI. Once each element has
 * been built and every element added to the frame the frame is packed and
 * displayed.
 * 
 * @author fsta657
 * 
 */
@SuppressWarnings("serial")
public class Main extends JFrame {

	private MyVLCPlayer _vlcPlayer;
	private VideoControlArea _control;

	public static void main(final String[] args) {

		// Get font to use
		try {
			InputStream is = Main.class.getResourceAsStream("gothic_0.TTF");
			Font font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(10f);
			Defaults.setDefaultFont(font);
		} catch (Exception ex) {
			Defaults.setDefaultFont(new Font("Lucida Sans", Font.BOLD, 11));
		}

		// Set look & feel
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}

		// Load VLC library
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

		// Create new Main
		final Main main = new Main();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				main.createAndShowGUI();
			}
		});
	}

	private void createAndShowGUI() {

		/*
		 * SELF SET-UP
		 */

		// Set up window
		setTitle("VAMIX");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(Defaults.DefaultWindowWidth,
				Defaults.DefaultWindowHeight));
		setResizable(false);
		// Set up layout
		this.setLayout(new BorderLayout());
		
		/*
		 * FIELD SET-UP
		 */
		
		// VLC player set up
		_vlcPlayer = new MyVLCPlayer();
		JPanel bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(Defaults.DefaultWindowWidth,
				Defaults.DefaultBufferHeight));
		bottomPanel.setBackground(Defaults.DefaultBufferColour);
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 8));
		_control = new VideoControlArea(_vlcPlayer);
		bottomPanel.add(_control);
		// Combine into panel
		JPanel videoPanel = new JPanel();
		videoPanel.setPreferredSize(new Dimension(Defaults.DefaultWindowWidth,
				Defaults.DefaultBufferHeight*2
						+ Defaults.DefaultMediaPlayerHeight));
		videoPanel.setLayout(new BoxLayout(videoPanel, BoxLayout.Y_AXIS));
		videoPanel.add(_vlcPlayer);
		videoPanel.add(bottomPanel);
		this.add(videoPanel, BorderLayout.NORTH);
		// Function area of window set up
		JPanel functionalArea = new JPanel();
		functionalArea.setPreferredSize(new Dimension(
				Defaults.DefaultWindowWidth, Defaults.DefaultFuncAreaHeight));
		functionalArea.setLayout(new BorderLayout());
		// Set up functional fields
		FunctionalAreaSwitcher _funcArea = new FunctionalAreaSwitcher();
		FunctionalAreaChooser _funcChooser = new FunctionalAreaChooser(
				_funcArea);
		functionalArea.add(_funcChooser, BorderLayout.WEST);
		functionalArea.add(_funcArea, BorderLayout.EAST);
		this.add(functionalArea, BorderLayout.SOUTH);
		// Display the window.
		pack();
		setVisible(true);
	}
}
