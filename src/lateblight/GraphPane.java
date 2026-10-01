package lateblight;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * Title:        Phytophthora infestans model
 * Description:  Class constructing the Graphics Panel
 * Copyright:    Copyright (c) 2001
 * Company:      Institute of Geoecology, TU Braunschweig
 * @author Heiko Apel
 * @version 1.0
 */

/**
 * Graphic Package for drawing of Graphics in PhytMod
 */


public class GraphPane extends javax.swing.JPanel {

  int height, width, pad_x, pad_y, length_x, length_y, tickL, tmax, x, y;
  double latD, infD, incr_x, dt;
  double[][] dat, points;
  String[] legText;
  String sel;
  Color colors[] = {new Color(0,255,0), new Color(0,0,255), new Color(255,0,0), new Color(0,0,0)};
  int legFontSize = 12;
  boolean impDataFormat = true;
  JLabel coords;
  boolean cross = false;

  public GraphPane() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public GraphPane(String choice, double[][] data, int t, double latent, double infect, double delta, String[] legend, double[][] pp, boolean format) {
    try {
      jbInit();
      tmax = t;
      dat = data;
      latD = latent;
      infD = infect;
      dt = delta;
      legText = legend;
      sel = choice;
      points = pp;
      impDataFormat = format;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  // find status bar for display of coordinates with cross
  private void findStatusBar() {
    Component[] comps = this.getParent().getParent().getParent().getComponents();
    for (int i = 0; i < comps.length; i++) {
      if (comps[i].getName() == "bottomPanel") {
        coords = (JLabel) comps[i].getComponentAt(0,0);
      }
    }
  }

  // draws the graphic
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    height = getHeight();
    width = getWidth();
    Rectangle2D e = new Rectangle2D.Double(0, 0, width, height);
    g2d.setColor(Color.white);
    g2d.fill(e);

    if (dat != null) {
      pad_x = width/10;
      pad_y = height/10;
      length_x = width-2*pad_x;
      length_y = height-2*pad_y;
      tickL = 5;
      int axesFontSize = 12;
      incr_x = (double) length_x/(double) tmax;
      int x_intervals, multiplier;
      int i, j;
      if (incr_x >= 2.5) {
        x_intervals = tmax/10;
        multiplier = 10;
      }
      else {
        x_intervals = tmax/20;
        multiplier = 20;
      }
      g2d.setColor(Color.black);
      g2d.drawLine(pad_x, pad_y, pad_x, height-pad_y); // y-axis
      g2d.drawLine(pad_x, height-pad_y, width-pad_x, height-pad_y); // x-axis
      // ticks y
      for (i = 0; i <= 10 ; i++) {
        g2d.drawLine(pad_x, (int) (height-pad_y-i*length_y/10),pad_x-tickL, (int) (height-pad_y-i*length_y/10));
      }
      // ticks x
      for (i = 0; i <= x_intervals; i++) {
        g2d.drawLine((int) (pad_x+i*incr_x*multiplier), height-pad_y, (int) (pad_x+i*incr_x*multiplier), height-pad_y+tickL);
      }
      Font axesFont = new Font("Helvetica", Font.PLAIN, 12);
      g2d.setFont(axesFont);
      // labels y
      for (i = 0; i <= 10; i++) {
        g2d.drawString(Float.toString((float) i/10), pad_x-tickL-axesFontSize*2, height-pad_y-i*length_y/10+axesFontSize/2);
      }
      // labels x
      for (i = 0; i <= x_intervals; i++) {
        g2d.drawString(Integer.toString(i*multiplier), (int) (pad_x-axesFontSize/2+i*incr_x*multiplier), height-pad_y+axesFontSize+tickL);
      }
      g2d.drawString("t [d]", width-pad_x+axesFontSize, height-pad_y+axesFontSize/2);

      //draw data
      if (sel == "all") {
        int nPoints = (int) (tmax/dt)+1; //dat[0].length-(int) ((latD+infD-1)/dt);
        int[] xPoints = new int[nPoints];
        int[][] yPoints = new int[dat.length][nPoints];

        // extract point coordinates from solution data for polylines
        for (i = 0; i < nPoints; i++) {
          xPoints[i] = (int) (pad_x + i*dt*incr_x);
        }
        for (j = 0; j < dat.length; j++) {
          for (i = 0; i < nPoints; i++) {
            yPoints[j][i] = (int) (pad_y + length_y - length_y*dat[j][i+(int) ((latD+infD)/dt)]);
          }
        }

        // draw line graphs
        for (j = 0; j < dat.length; j++) {
          g2d.setColor(colors[j]);
          g2d.drawPolyline(xPoints, yPoints[j], nPoints);
        }
        // draw legend
        for (j = 0; j < dat.length; j++) {
          g2d.setColor(Color.black);
          g2d.setFont(new Font("Helvetica", Font.PLAIN, legFontSize));
          g2d.drawString(legText[j], pad_x+j*pad_x*2+pad_y+legFontSize/2, pad_y/2+legFontSize/2);
          g2d.setColor(colors[j]);
          g2d.drawLine(pad_x+j*pad_x*2+pad_y/2, pad_y/2, pad_x+j*pad_x*2+pad_y, pad_y/2);
        }
      }
      else if (sel == "visible") {
        int nPoints = dat[0].length-(int) ((latD+infD)/dt);
        int[] xPoints = new int[nPoints];
        int[] yPoints = new int[nPoints];
        // extract points for polylines from solution data
        for (i = 0; i < nPoints; i++) {
          xPoints[i] = (int) (pad_x + i*dt*incr_x);
        }
        for (i = 0; i < nPoints; i++) {
          yPoints[i] = (int) (pad_y + length_y - length_y*(dat[2][i+(int) ((latD+infD)/dt)]+dat[3][i+(int) ((latD+infD)/dt)]));
        }
        // draw line and legend
        g2d.setColor(Color.black);
        g2d.drawPolyline(xPoints, yPoints, nPoints);
        g2d.setFont(new Font("Helvetica", Font.PLAIN, legFontSize));
        g2d.drawString(legText[4], pad_x+pad_x*2+pad_y+legFontSize/2, pad_y/2+legFontSize/2);
        g2d.drawLine(pad_x+pad_x*2+pad_y/2, pad_y/2, pad_x+pad_x*2+pad_y, pad_y/2);
        //draw data points, if present
        if (impDataFormat) {
          g2d.setColor(Color.red);
          for (i = 0; i < points.length; i++) {
            if (points[i][0] <= tmax) {
              g2d.fillOval(pad_x + (int) (points[i][0]*incr_x-3), pad_y + (int) (length_y - length_y*points[i][1]-3), 6, 6);          }
          }
        }
      }
    }
    if (cross) {
      g2d.setColor(Color.gray);
      g2d.drawLine(0, y, width, y);
      g2d.drawLine(x, 0, x, height);
    }
  }


  private void jbInit() throws Exception {
    this.addMouseMotionListener(new GraphPane_this_mouseMotionAdapter(this));
    this.addMouseListener(new GraphPane_this_mouseAdapter(this));
  }

  void getCross(MouseEvent e) {
    x = e.getX();
    y = e.getY();
    float t = (float) ((x-pad_x)/(dt*incr_x));
    float y_t = ((float) (length_y+pad_y-y)/(float) length_y);
    if (coords == null) {
      findStatusBar();
    }
    coords.setText("x = "+Float.toString(t)+" |  y = "+Float.toString(y_t));
//    Graphics g = this.getGraphics();
//    g.setColor(Color.gray);
//    g.drawLine(0,y,width,y);
//    g.drawLine(x,0,x,height);
  }

  void this_mousePressed(MouseEvent e) {
    getCross(e);
    cross = true;
    repaint();
  }

  void this_mouseReleased(MouseEvent e) {
    coords.setText("Ready.");
    cross = false;
    repaint();
  }

  void this_mouseEntered(MouseEvent e) {
    if (coords == null) {
      findStatusBar();
    }
    coords.setText("Press and drag mouse button to show coordinates in status bar.");
    cross = false;
  }

  void this_mouseExited(MouseEvent e) {
    coords.setText("Ready.");
    cross = false;
  }

  void this_mouseDragged(MouseEvent e) {
    getCross(e);
    repaint();
//    drawCross(e);
  }

  public void errorMsg(String msg) {
    JOptionPane.showMessageDialog(null, msg, "PhytMod message", 1);
  }

} //end class

class GraphPane_this_mouseAdapter extends java.awt.event.MouseAdapter {
  GraphPane adaptee;

  GraphPane_this_mouseAdapter(GraphPane adaptee) {
    this.adaptee = adaptee;
  }
  public void mousePressed(MouseEvent e) {
    adaptee.this_mousePressed(e);
  }
  public void mouseReleased(MouseEvent e) {
    adaptee.this_mouseReleased(e);
  }
  public void mouseEntered(MouseEvent e) {
    adaptee.this_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e) {
    adaptee.this_mouseExited(e);
  }
}

class GraphPane_this_mouseMotionAdapter extends java.awt.event.MouseMotionAdapter {
  GraphPane adaptee;

  GraphPane_this_mouseMotionAdapter(GraphPane adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseDragged(MouseEvent e) {
    adaptee.this_mouseDragged(e);
  }
}