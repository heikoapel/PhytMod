package lateblight;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Title:        Phytophthora infestans model
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Institute of Geoecology, TU Braunschweig
 * @author Heiko Apel
 * @version 1.0
 */

public class Phyt_Frame_AboutBox extends JDialog implements ActionListener {

  JPanel panel1 = new JPanel();
  JPanel panel2 = new JPanel();
  JPanel insetsPanel1 = new JPanel();
  JPanel insetsPanel2 = new JPanel();
  JPanel insetsPanel3 = new JPanel();
  JButton button1 = new JButton();
  JLabel imageLabel = new JLabel();
  JLabel label1 = new JLabel();
  JLabel label2 = new JLabel();
  JLabel label3 = new JLabel();
  JLabel label4 = new JLabel();
  JLabel label5 = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  GridLayout gridLayout1 = new GridLayout();
  String product = "PhyMod - Simulating general and late blight epidemics";
  String version = "Version 1.1";
  String copyright = "Copyright Heiko Apel (c) 2002";
  String comments = "Institute of Geoecology, Technical University of Braunschweig, Germany";
  String contact = "contact, comment, bug report: h.apel@tu-bs.de";
  public Phyt_Frame_AboutBox(Frame parent) {
    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    pack();
  }
  /**Component initialization*/
  private void jbInit() throws Exception  {
    //imageLabel.setIcon(new ImageIcon(Phyt_Frame_AboutBox.class.getResource("[Your Image]")));
    this.setTitle("About PhytMod");
    setResizable(false);
    panel1.setLayout(borderLayout1);
    panel2.setLayout(borderLayout2);
    insetsPanel1.setLayout(flowLayout1);
    insetsPanel2.setLayout(flowLayout1);
    insetsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    gridLayout1.setRows(5);
    gridLayout1.setColumns(1);
    label1.setForeground(Color.black);
    label1.setText(product);
    label2.setForeground(Color.black);
    label2.setText(version);
    label3.setForeground(Color.black);
    label3.setText(copyright);
    label4.setForeground(Color.black);
    label4.setText(comments);
    label5.setForeground(Color.black);
    label5.setText(contact);
    insetsPanel3.setLayout(gridLayout1);
    insetsPanel3.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
    button1.setText("Ok");
    button1.addActionListener(this);
    insetsPanel2.add(imageLabel, null);
    panel2.add(insetsPanel2, BorderLayout.WEST);
    this.getContentPane().add(panel1, null);
    insetsPanel3.add(label1, null);
    insetsPanel3.add(label2, null);
    insetsPanel3.add(label3, null);
    insetsPanel3.add(label4, null);
    insetsPanel3.add(label5, null);
    this.getContentPane().add(insetsPanel1, BorderLayout.SOUTH);
    insetsPanel1.add(button1, null);
    panel2.add(insetsPanel3, BorderLayout.CENTER);
    panel1.add(panel2, BorderLayout.NORTH);
  }
  /**Overridden so we can exit when window is closed*/
  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      cancel();
    }
    super.processWindowEvent(e);
  }
  /**Close the dialog*/
  void cancel() {
    dispose();
  }
  /**Close the dialog on a button event*/
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == button1) {
      cancel();
    }
  }
}
