package lateblight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.html.*;
import java.net.URL;
import java.io.*;
import javax.swing.text.*;
import java.util.jar.*;
//import java.util.zip.*;

/**
 * Title:        PhytMod
 * Description:  Modelling Phytophthora infestans and general epidemics
 * Copyright:    Copyright Heiko Apel (c) 2002
 * Company:      Institute of Geoecology, TU Braunschweig
 * @author Heiko Apel
 * @version 1.0
 */

public class HelpFrame extends JFrame {
  BorderLayout borderLayout1 = new BorderLayout();
  JToolBar jToolBar_Help = new JToolBar();
  JButton jButtonBack = new JButton();
  JButton jButtonForward = new JButton();
  //HTMLDocument help = new HTMLDocument();
  JScrollPane jScrollPane_Help = new JScrollPane();
  //JTextPane jTextPane_Help = new JTextPane();
  JEditorPane HelpPane = new JEditorPane();
  //public URL helpURL;
  //public String htmlText;

  //HTMLEditorKit htmlKit = new HTMLEditorKit();
  //HTMLDocument htmlDoc = (HTMLDocument)(htmlKit.createDefaultDocument());
  //StyleSheet styleSheet = htmlDoc.getStyleSheet();


  //public HelpFrame(JEditorPane pane) {
  public HelpFrame(String tt) {
    try {
      jbInit(tt);
      jScrollPane_Help.getViewport().add(HelpPane, null);
    }
    catch (FileNotFoundException e) {

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit(String doc) throws Exception {
    this.setTitle("PhytMod - Help");
    this.getContentPane().setLayout(borderLayout1);
    jButtonBack.setMaximumSize(new Dimension(80, 25));
    jButtonBack.setMinimumSize(new Dimension(80, 25));
    jButtonBack.setPreferredSize(new Dimension(80, 25));
    jButtonBack.setText("Back");
    jButtonForward.setMaximumSize(new Dimension(80, 25));
    jButtonForward.setMinimumSize(new Dimension(80, 25));
    jButtonForward.setPreferredSize(new Dimension(80, 25));
    jButtonForward.setText("Forward");
    this.getContentPane().add(jToolBar_Help, BorderLayout.NORTH);
    jToolBar_Help.add(jButtonBack, null);
    jToolBar_Help.add(jButtonForward, null);
    this.getContentPane().add(jScrollPane_Help, BorderLayout.CENTER);

    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    // PhytMod.jar must be in the classpath
    //java.net.URL url = loader.getResource("jar:resource:PhytMod.jar!lateblight/docs/help.html");
    // if in the current jar:
    java.net.URL url = loader.getResource("lateblight/docs/"+doc);
    HelpPane.setPage(url);
  }

}