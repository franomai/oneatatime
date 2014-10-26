package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.filechooser.FileNameExtensionFilter;

import defaults.Defaults;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

@SuppressWarnings("serial")
public class MyVLCPlayer extends EmbeddedMediaPlayerComponent {

	private JFileChooser _chooser;
	private JLabel _overlay;

	public MyVLCPlayer() {
		super();
		// Set size
		setPreferredSize(new Dimension(Defaults.DefaultWindowHeight,
				Defaults.DefaultMediaPlayerHeight));
	}

}