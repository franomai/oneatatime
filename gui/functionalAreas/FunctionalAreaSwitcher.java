package gui.functionalAreas;

import gui.functionalAreas.audioAreas.*;
import gui.functionalAreas.downloadAreas.*;
import gui.functionalAreas.subtitleAreas.AdvancedAddSubtitlesArea;
import gui.functionalAreas.subtitleAreas.AdvancedCreateSubtitles;
import gui.functionalAreas.subtitleAreas.AdvancedExtractSubtitlesArea;
import gui.functionalAreas.subtitleAreas.BasicAddSubtitlesArea;
import gui.functionalAreas.subtitleAreas.BasicCreateSubtitles;
import gui.functionalAreas.subtitleAreas.BasicExtractSubtitlesArea;
import gui.functionalAreas.textAreas.*;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import defaults.Defaults;

@SuppressWarnings("serial")
/**
 * This class contains the logic for functionality panel display. The panels are all created,
 * and a basic string assigned that matches the text on the buttons that should lead to said option.
 * For example, the AdvancedDownloadArea should be reached first by pressing Download and then Advanced, and
 * as such the string assigned is "AdvancedDownload".
 * @author fsta657
 *
 */
public class FunctionalAreaSwitcher extends JPanel {
	
	public FunctionalAreaSwitcher(){
		setPreferredSize(new Dimension(Defaults.DefaultFuncAreaWidth, Defaults.DefaultFuncAreaHeight));
		setLayout(new CardLayout());
		
		// Add sections with required dependencies.
		
		AdvancedDownloadArea ad = new AdvancedDownloadArea();
		add(ad, "AdvancedDownload");
		add(new BasicDownloadArea(ad), "BasicDownload");
		
		AdvancedAddTextArea at = new AdvancedAddTextArea();
		add(at, "AdvancedTitle");
		BasicAddTextArea bt = new BasicAddTextArea(at);
		add(bt, "BasicTitle");
		BasicAddCreditsArea bc = new BasicAddCreditsArea(at);
		add(bc, "BasicCredits");
		at.setReferences(bc,bt);
		
		AdvancedStripAudioArea as = new AdvancedStripAudioArea();
		add(as, "AdvancedStrip");
		add(new BasicStripAudioArea(as), "BasicStrip");
		
		AdvancedReplaceAudioArea ar = new AdvancedReplaceAudioArea();
		add(ar, "AdvancedReplace");
		add(new BasicReplaceAudioArea(ar), "BasicReplace");
		
		AdvancedExtractSubtitlesArea es = new AdvancedExtractSubtitlesArea();
		add(es, "AdvancedExtract");
		add(new BasicExtractSubtitlesArea(es), "BasicExtract");
		
		AdvancedAddSubtitlesArea ass = new AdvancedAddSubtitlesArea();
		add(ass, "AdvancedAdd");
		add(new BasicAddSubtitlesArea(ass), "BasicAdd");
		
		AdvancedOverlayAudioArea ao = new AdvancedOverlayAudioArea();
		add(ao, "AdvancedOverlay");
		
		add(new BasicOverlayAudioArea(ao), "BasicOverlay");
		
		AdvancedCreateSubtitles ac = new AdvancedCreateSubtitles();
		add(ac, "AdvancedCreate");
		add(new BasicCreateSubtitles(ac), "BasicCreate");
		
		add(new BasicTrimArea(), "BasicTrim");
		
		add(new BasicUploadArea(), "BasicUpload");
		add(new AdvancedUploadArea(), "AdvancedUpload");
		
		add(new HelpArea(), "BasicHelp");
		
		
		// Switch to initial panel.
		switchPanel("Help","Basic");
	}
	
	/**
	 * This method takes in the option and level and displays the panel associated with that string combination.
	 * These associations are initially done when the object is constructed.
	 * @param option
	 * @param level
	 */
	public void switchPanel(String option, String level){
	CardLayout cl = (CardLayout) (this.getLayout());
	// If statement required to accommodate for shared advanced panel.
	if (option.equals("Credits")&&level.equals("Advanced")){
		option="Title";
	}
	cl.show(this, (level + option).trim());
	}

}