package gui.functionalAreas;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import defaults.Defaults;

/**
 * This code is mainly from Assignment Three and was completed by my partner
 * Jacob Holden. However, my understanding is is that this code provides a
 * template for creating component and paints it with the given background and
 * determines its foreground on a class by class basis, i.e it will paint it
 * however the extending class has said to paint itself. This class also ensures
 * that all panels implement behavior for interpreting worker results and has a
 * foreground to display.
 * 
 * @author fsta657
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractFunctionalArea extends JPanel {

	protected Image _background;
	protected JPanel _frontPanel;

	/**Sets up foreground and background for given panel.
	 * This set up will be consistent with the L&F of the rest of the GUI.
	 */
	public AbstractFunctionalArea() {
		setPreferredSize(new Dimension(Defaults.DefaultFuncAreaWidth,
				Defaults.DefaultFuncAreaHeight));
		ImageIcon icon = new ImageIcon(
				AbstractFunctionalArea.class
						.getResource("/gui/assets/funcMetal.jpg"));
		_background = icon.getImage();
		_frontPanel = createAreaSpecific();
		_frontPanel.setOpaque(false);
		this.add(_frontPanel);
	}
    
	// Must be implemented in extending classes to ensure correct behavior
	// throughout the GUI.
	/**
	 * Takes in an exit status from a worker and generally speaking
	 * should be implemented so as to display a message to the user indicating
	 * the success/failure of the given operation.
	 * @param exitStatus
	 */
	public abstract void processWorkerResults(int exitStatus);

	protected abstract JPanel createAreaSpecific();

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(_background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
