package lateblight;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Title:        Phytophthora infestans model
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Institute of Geoecology, TU Braunschweig
 * @author Heiko Apel
 * @version 1.0
 */

/**
 * for RunButtonSame
 * actually first, imperfect version of GraphPane for Graphics
 */

public class GraphDraw {

  int height, width, pad_x, pad_y, length_x, length_y, tickL;
  double incr_x;
  JPanel pan;

  public GraphDraw() {
  }

  public GraphDraw(JPanel panel) {
    pan = panel;
  }

  public void getDimension() {
    try {
      height = pan.getHeight();
      width = pan.getWidth();
      pad_x = width/10;
      pad_y = height/10;
      length_x = width-2*pad_x;
      length_y = height-2*pad_y;
//    JOptionPane.showMessageDialog(null, Integer.toString(height)+"\n"+Integer.toString(width), "PhytMod message", 1);
    }
    catch (NullPointerException e){
      JOptionPane.showMessageDialog(null, "NullPointerException!!", "PhytMod message", 1);
    }
  }

  public void drawAxes(int tmax) {
    tickL = 5;
    int axesFontSize = 12;
    incr_x = (double) length_x/(double) tmax;
    int x_intervals, multiplier, i;
    if (incr_x >= 2.5) {
      x_intervals = tmax/10;
      multiplier = 10;
    }
    else {
      x_intervals = tmax/20;
      multiplier = 20;
    }
    Graphics g;
    g = pan.getGraphics();
    Graphics2D g2d = (Graphics2D) g;
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
  }

  public void drawData(double[][] dat, double lat, double inf, double dt, String[] legText) {
    int i,j;
    Graphics g;
    g = pan.getGraphics();
    Graphics2D g2d = (Graphics2D) g;
    Color colors[] = new Color[dat.length];
    colors[0] = new Color(0,255,0);
    colors[1] = new Color(0,0,255);
    colors[2] = new Color(255,0,0);
    colors[3] = new Color(0,0,0);
    int legFontSize = 12;
//    String legText[] = new String[dat.length];
//    legText[0] = "uninfected";
//    legText[1] = "latent";
//    legText[2] = "infectious";
//    legText[3] = "dead";
    int nPoints = dat[0].length-(int) ((lat+inf)/dt);
    int[] xPoints = new int[nPoints];
    int[][] yPoints = new int[dat.length][nPoints];
//    JOptionPane.showMessageDialog(null,"nPoints = "+Integer.toString(nPoints)+"\nlength xpoints = "+xPoints.length+"\nlength ypoints = "+xPoints.length, "PhytMod message", 1);
    // extract point coordinates from solution data for polylines
    for (i = 0; i < nPoints; i++) {
      xPoints[i] = pad_x + (int) (i*dt*incr_x);
    }
    for (j = 0; j < dat.length; j++) {
      for (i = 0; i < nPoints; i++) {
        yPoints[j][i] = pad_y + (int) (length_y - length_y*dat[j][i+(int) ((lat+inf)/dt)]);
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
  } // end drawData

  public void drawVis(double[][] dat, double lat, double inf, double dt) {
    int i;
    Graphics g;
    g = pan.getGraphics();
    Graphics2D g2d = (Graphics2D) g;
    int legFontSize = 12;
    String legText = "visible infection (I + D)";
    int nPoints = dat[0].length-(int) ((lat+inf)/dt);
    int[] xPoints = new int[nPoints];
    int[] yPoints = new int[nPoints];
    // extract points for polylines from solution data
    for (i = 0; i < nPoints; i++) {
      xPoints[i] = pad_x + (int) (i*dt*incr_x);
    }
    for (i = 0; i < nPoints; i++) {
      yPoints[i] = pad_y + (int) (length_y - length_y*(dat[2][i+(int) ((lat+inf)/dt)]+dat[3][i+(int) ((lat+inf)/dt)]));
    }
    // draw line and legend
    g2d.drawPolyline(xPoints, yPoints, nPoints);
    g2d.setColor(Color.black);
    g2d.setFont(new Font("Helvetica", Font.PLAIN, legFontSize));
    g2d.drawString(legText, pad_x+pad_x*2+pad_y+legFontSize/2, pad_y/2+legFontSize/2);
    g2d.drawLine(pad_x+pad_x*2+pad_y/2, pad_y/2, pad_x+pad_x*2+pad_y, pad_y/2);
  } // end drawVis

} //end class