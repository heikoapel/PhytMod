package lateblight;

import javax.swing.UIManager;
import java.awt.*;

/**
 * Title:        PhytMod
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Institute of Geoecology, TU Braunschweig
 * @author Heiko Apel
 * @version 1.0
 */

public class Phyt_model {
  boolean packFrame = false;

  /**Construct the application*/
  public Phyt_model() {
    Phyt_Frame frame = new Phyt_Frame();
    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame) {
      frame.pack();
    }
    else {
      frame.validate();
    }
    //Center the window
    frame.setSize(1000, 700);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
  }
  /**Main method*/
  public static void main(String[] args) {
    try {
      //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

    }
    catch(Exception e) {
      e.printStackTrace();
    }
    new Phyt_model();
  }
}