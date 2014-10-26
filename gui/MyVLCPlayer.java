package gui;

import java.awt.Dimension;
import defaults.Defaults;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

/**
 * Trivial class used to create an EmbeddedMediaPlayerComponent and set its
 * size. The object create from the class as such represents the specific player
 * in use in VAMIX.
 * 
 * @author fsta657
 */
@SuppressWarnings("serial")
public class MyVLCPlayer extends EmbeddedMediaPlayerComponent {

	public MyVLCPlayer() {
		super();
		// Set size
		setPreferredSize(new Dimension(Defaults.DefaultWindowHeight,
				Defaults.DefaultMediaPlayerHeight));
	}

}