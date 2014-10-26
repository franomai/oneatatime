package defaults;

import java.awt.Color;
import java.awt.Font;

public class Defaults {
	
	//Misc. Numbers
	public static final int DefaultCharLimit = 40;
	//Sizes
	public static final int DefaultWindowWidth = 1050;
	public static final int DefaultWindowHeight = 750; //750 OR 675
	public static final int DefaultMediaPlayerHeight = 475;
	public static final int DefaultFuncAreaHeight = 125;//150
	public static final int DefaultFuncChooserWidth = 300;
	public static final int DefaultFuncAreaWidth = 750;
	public static final int DefaultControlWidth = 500;
	public static final int DefaultControlHeight = 60;
	public static final int DefaultBufferHeight = 75;
	//Colours
	public static final Color DefaultWritingColour = new Color(0,0,0);
	public static final Color DefaultDownloadColour = new Color(153,204,255);
	public static final Color DefaultTextColour = new Color(102,178, 255);
	public static final Color DefaultAudioColour = new Color(40,123,220);
	public static final Color DefaultSubsColour = new Color(0,100,200);
	public static final Color DefaultBasicColour = new Color(200, 200, 200);
	public static final Color DefaultAdvColour = new Color(200, 200, 200);
	public static final Color DefaultTextFieldColour = new Color(0,0,0);
	public static final Color DefaultProgressColour = new Color(0,0,0);
	public static final Color DefaultLoadColour = new Color(255,255,255);
	public static final Color DefaultGoButtonColour = new Color(153,255,204);
	public static final Color DefaultBufferColour = Color.BLACK;
	//Fonts
	public static Font BaseFont = new Font("Lucida Sans",Font.BOLD,11);
	public static Font DefaultButtonFont = new Font("Lucida Sans",Font.BOLD,11);
	public static Font DefaultTextFieldFont = new Font("Lucida Sans",Font.BOLD,13);
	public static Font DefaultLabelFont = new Font("Lucida Sans",Font.BOLD,17);
	public static Font DefaultTitleFont = new Font("Lucida Sans",Font.BOLD,19);
	// Lists
	public static final String[] DefaultFontColours = {"White", "Black"};
	public static final String[] DefaultBackColours = {"Black", "White"};
	public static final String[] DefaultFonts = {"Free Serif","Free Serif Plus","Free Sans Serif"};
	public static final String[] DefaultFontSizes = {"15","20","25","30"};
	public static final String[] DefaultPositions = {"Top","Middle","Bottom"};
	public static final String[] DefaultAudioFileTypes = {"mp3","mpeg","wav"};
	public static final String[] DefaultVideoFileTypes = {"mp4","mov"};
	
	public static void setDefaultFont(Font font){
		BaseFont = font;
		DefaultButtonFont = BaseFont;
		DefaultTextFieldFont = BaseFont.deriveFont(BaseFont.getSize()+2f);
		DefaultLabelFont = BaseFont.deriveFont(BaseFont.getSize()+6f);
		DefaultTitleFont = BaseFont.deriveFont(BaseFont.getSize()+8f);
	}
}
