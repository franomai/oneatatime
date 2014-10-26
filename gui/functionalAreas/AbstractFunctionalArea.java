package gui.functionalAreas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import defaults.Defaults;

public abstract class AbstractFunctionalArea extends JPanel {
	
	protected Image _background;
	protected JPanel _frontPanel;
	
	public AbstractFunctionalArea(){
		setPreferredSize(new Dimension(Defaults.DefaultFuncAreaWidth, Defaults.DefaultFuncAreaHeight));
		ImageIcon icon = new ImageIcon(
				AbstractFunctionalArea.class.getResource("/gui/assets/funcMetal.jpg"));
		_background = icon.getImage();
		_frontPanel = createAreaSpecific();
		_frontPanel.setOpaque(false);
		this.add(_frontPanel);
	}

	public abstract void processWorkerResults(int exitStatus);
	
	protected abstract JPanel createAreaSpecific();

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(_background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
