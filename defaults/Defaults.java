package defaults;

import java.awt.Color;
import java.awt.Font;

/**
 * This class contains all the defaults used by the rest of the GUI. This
 * includes the size of various components, the colors of said components, what
 * font type and size of font is used by certain components and some defaults
 * used by certain functionality such as the add text customization. Whenever a
 * class that uses these needs a value this class is referred to statically and
 * the value obtained. Note that some issues with resizing on certain monitors
 * can sometimes be resolved by editing the {@link Defaults#DefaultWindowHeight}
 * and {@link Defaults#DefaultBufferHeight} to the value commented beside each.
 * 
 * @author fsta657
 * 
 */
public class Defaults {

	// Misc. Numbers
	public static final int DefaultCharLimit = 40;
	// Sizes
	public static final int DefaultWindowWidth = 1050;
	public static final int DefaultWindowHeight = 750; // 750 OR 675
	public static final int DefaultMediaPlayerHeight = 475;
	public static final int DefaultFuncAreaHeight = 125;
	public static final int DefaultFuncChooserWidth = 300;
	public static final int DefaultFuncAreaWidth = 750;
	public static final int DefaultControlWidth = 500;
	public static final int DefaultControlHeight = 60;
	public static final int DefaultBufferHeight = 75 * 2; // x2 or x1
	// Colours
	public static final Color DefaultWritingColour = new Color(0, 0, 0);
	public static final Color DefaultDownloadColour = new Color(153, 204, 255);
	public static final Color DefaultTextColour = new Color(102, 178, 255);
	public static final Color DefaultAudioColour = new Color(40, 123, 220);
	public static final Color DefaultSubsColour = new Color(0, 100, 200);
	public static final Color DefaultBasicColour = new Color(200, 200, 200);
	public static final Color DefaultAdvColour = new Color(200, 200, 200);
	public static final Color DefaultTextFieldColour = new Color(0, 0, 0);
	public static final Color DefaultProgressColour = new Color(0, 0, 0);
	public static final Color DefaultLoadColour = new Color(255, 255, 255);
	public static final Color DefaultGoButtonColour = new Color(153, 255, 204);
	public static final Color DefaultBufferColour = Color.BLACK;
	// Fonts
	public static Font BaseFont = new Font("Lucida Sans", Font.BOLD, 11);
	public static Font DefaultButtonFont = new Font("Lucida Sans", Font.BOLD,
			11);
	public static Font DefaultTextFieldFont = new Font("Lucida Sans",
			Font.BOLD, 13);
	public static Font DefaultLabelFont = new Font("Lucida Sans", Font.BOLD, 17);
	public static Font DefaultTitleFont = new Font("Lucida Sans", Font.BOLD, 19);
	// Lists
	public static final String[] DefaultFontColours = { "White", "Black" };
	public static final String[] DefaultBackColours = { "Black", "White" };
	public static final String[] DefaultFonts = { "Free Serif",
			"Free Serif Plus", "Free Sans Serif" };
	public static final String[] DefaultFontSizes = { "15", "20", "25", "30" };
	public static final String[] DefaultPositions = { "Top", "Middle", "Bottom" };
	public static final String[] DefaultAudioFileTypes = { "mp3", "mpeg", "wav" };
	public static final String[] DefaultVideoFileTypes = { "mp4", "mov" };

	/**
	 * Static method for initializing fonts to be used by the rest of the GUI.
	 * Allows for a font to be swapped in, in place of the current font.
	 * 
	 * @param font
	 */
	public static void setDefaultFont(Font font) {
		BaseFont = font;
		DefaultButtonFont = BaseFont;
		DefaultTextFieldFont = BaseFont.deriveFont(BaseFont.getSize() + 2f);
		DefaultLabelFont = BaseFont.deriveFont(BaseFont.getSize() + 6f);
		DefaultTitleFont = BaseFont.deriveFont(BaseFont.getSize() + 8f);
	}
}
