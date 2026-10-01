package lateblight;

import java.lang.Math;
import javax.swing.*;

/**
 * Title:        PhytMod - Phytophthora infestans model and general epidemics
 * Description:  Class containing the calculating kernel
 * Copyright:    Copyright (c) 2001
 * Company:      Institute of Geoecology, TU Braunschweig
 * @author Heiko Apel
 * @version 1.0
 */

public class PhytKernel {

  public double latD, infD;
  public int tmax, appInterval, appStart, psm, lat, inf;
  public int vars = 4; //number of variables, i.e. uninfected, latent, infectious, dead
  public double R, y0, dt, W, spint;
  public int[] appDays;
  public double[][] erg, hist;
  public double kwirkpro = 1;
  public double kwirkcur = 1;
  public double kwirkera = 1;



  /* interpolation function; receives data array, x = actual time required,
     u = lower nearest array value, o = upper nearest array value */
  public double ip(double data[], double x, int u, int o){
    double val = data[u] + (data[o]-data[u])*(x-u);
    return val;
  }

  public double[][] executeKernel() {

    erg = new double[vars][(int) ((tmax+infD+latD+1)/dt)];
    int i;
    double x1, x2;   //delay times for non-integer delays
    int fl1, ce1, fl2, ce2;  //nearest integers of delays for interpolation
    // initialize delay history
    for(i = 0; i <= (int) ((latD+infD)/dt); i++) {
     erg[0][i]=1;
     erg[1][i]=0;
     erg[2][i]=0;
     erg[3][i]=0;
    }
    // define intial values
    erg[1][(int) ((latD+infD)/dt)]=y0/2;
    erg[2][(int) ((latD+infD)/dt)]=y0/2;
    erg[0][(int) ((latD+infD)/dt)]=1-y0;
    // do the calculation
    for(i = (int) ((latD+infD)/dt); i < (int) ((tmax+latD+infD+1)/dt)-1; i++){
     x1 = i-latD/dt;
     x2 = i-(latD+infD)/dt;
     if (x2 < 0) {x2 = 0;};
     fl1 = (int) java.lang.Math.floor(x1);   // nearest array values to delays
     ce1 = (int) java.lang.Math.ceil(x1);
     fl2 = (int) java.lang.Math.floor(x2);
     ce2 = (int) java.lang.Math.ceil(x2);
     erg[1][i+1] = erg[1][i]+dt*R*erg[0][i]*erg[2][i]-dt*R*ip(erg[0],x1,fl1,ce1)*ip(erg[2],x1,fl1,ce1);
     erg[2][i+1] = erg[2][i]+dt*R*ip(erg[0],x1,fl1,ce1)*ip(erg[2],x1,fl1,ce1)-dt*R*ip(erg[0],x2,fl2,ce2)*ip(erg[2],x2,fl2,ce2);
     erg[3][i+1] = erg[3][i]+dt*R*ip(erg[0],x2,fl2,ce2)*ip(erg[2],x2,fl2,ce2);
     erg[0][i+1] = 1-erg[1][i+1]-erg[2][i+1]-erg[3][i+1];
    }
    return erg;
  } //end method executeKernel


  //substitution function for infection process; with general sporulation function slower than standard model although it should be equal, i.e. I(t) = erg[0][i] is not equal to sum1 !!
  public double FY(double t, double[][] res) {
    int j,k,u,o;
    double RY, sum1, sum2;
    if (t < (latD+infD)/dt) {
      RY = 0;
    }
    else {
      sum1 = 0; sum2 = 0;
      for (j = 1; j <= (int) (infD/dt)-1; j++) {
        for (k = 1; k <= j; k++) {
          u = (int) java.lang.Math.floor(t-k);
          o = (int) java.lang.Math.ceil(t-k);
          sum2 = sum2 + dt * ip(res[0],t-k,u,o);
        }
        u = (int) java.lang.Math.floor(t-j-latD/dt);
        o = (int) java.lang.Math.ceil(t-j-latD/dt);
        sum1 = sum1 + dt * R * ip(res[0],t-j-latD/dt,u,o) * ip(res[2],t-j-latD/dt,u,o) * (1 + dt * W * sum2) * sp(t-j-(int) latD/dt, t);
      }
      u = (int) java.lang.Math.floor(t);
      o = (int) java.lang.Math.ceil(t);
      RY = R*ip(res[0],t,u,o)*sum1;
    }
    return RY;
  }


  // Phytophthora sporulation function
  public double sp(double s, double t) {
    double spor;
    if ((t-s) < (latD/dt)){
      spor = 0;
    }
    else if ((t-s) > ((latD+1)/dt)) {
      spor = 0;
    }
    else {
      spor = spint;
    }
    return spor;
  }

   public double[][] executePhytKernel() {

    erg = new double[vars][(int) ((tmax+infD+latD+1)/dt)];
    double growthSum1, growthSum2;
    int i,j;
    // initialize delay history
    for(i = 0; i <= (int) ((latD+infD)/dt); i++) {
     erg[0][i]=1;
     erg[1][i]=0;
     erg[2][i]=0;
     erg[3][i]=0;
    }
    // define initial values for t = 0
    erg[1][(int) ((latD+infD)/dt)]=y0/2;
    erg[2][(int) ((latD+infD)/dt)]=y0/2;
    erg[0][(int) ((latD+infD)/dt)]=1-y0;

    // do the calculation
    for(i = (int) ((latD+infD)/dt); i < (int) ((tmax+latD+infD+1)/dt)-1; i++){
      growthSum1 = 0; growthSum2 = 0;
      for (j = 1; j <= (int) (infD/dt-1); j++) {
        growthSum1 = growthSum1 + dt*FY(i-j-latD/dt, erg);
        growthSum2 = growthSum2 + dt*erg[0][i-j];
      }
      erg[1][i+1] = erg[1][i] + dt*FY(i, erg) - dt*FY(i-latD/dt, erg);
      erg[2][i+1] = erg[2][i] + dt*FY(i-latD/dt, erg) + dt*W*erg[0][i]*growthSum1 - dt*FY(i-(latD+infD)/dt, erg)*(1 + W*growthSum2);
      erg[3][i+1] = erg[3][i] + dt*FY(i-(latD+infD)/dt, erg)*(1 + W*growthSum2);
      erg[0][i+1] = 1 - erg[1][i+1] - erg[2][i+1] - erg[3][i+1];
    }
    return erg;
  } //end method executePhytKernel

  // protective efficiency of Mancozeb, Gutsche 1994
  public double kwirkPro(double t) {
    double k;
    k = kwirkpro*(1 - 0.182707*Math.pow(t*dt, 0.2778)*Math.pow(1.0398, t*dt));
    return k;
  }

//  public double PW(int t) {
//    double PW = 0;
//    if (t < (appStart+latD+infD)/dt) {
//      PW = 0;
//    }
//    else if (t >= (appStart+latD+infD)/dt) {
//      PW = kwirkPro(t-(int) ((appStart+latD+infD)/dt));
//    }
//    return PW;
//  }

  //substitory function for protective pesticide application
  public double FYPro(double t, double[][] res, double[] PW) {
    int j,k,u,o;
    double RY, sum1, sum2;
    if (t < (latD+infD)/dt) {
      RY = 0;
    }
    else {
      sum1 = 0; sum2 = 0;
      for (j = 1; j <= (int) (infD/dt)-1; j++) {
        for (k = 1; k <= j; k++) {
          u = (int) Math.floor(t-k);
          o = (int) Math.ceil(t-k);
          sum2 = sum2 + dt * ip(res[0],t-k,u,o);
        }
        u = (int) Math.floor(t-j-latD/dt);
        o = (int) Math.ceil(t-j-latD/dt);
        sum1 = sum1 + dt * R * (1-PW[(int) (t-j-latD/dt)]) * ip(res[0],t-j-latD/dt,u,o) * ip(res[2],t-j-latD/dt,u,o) * (1 + dt * W * (1-PW[(int) (t-j-latD/dt)]) * sum2) * sp(t-j-(int) latD/dt, t);
      }
      u = (int) Math.floor(t);
      o = (int) Math.ceil(t);
      RY = R*(1-PW[(int) t])*ip(res[0],t,u,o)*sum1;
    }
    return RY;
  }

  public double[][] executePhytKernelPro() {
    int i, j;
    erg = new double[vars][(int) ((tmax+infD+latD+1)/dt)];
    double growthSum1, growthSum2;
    // initializing pesticide effect array, i.e. effect not as time dependend function, but as array with precalculated values
    double PW[] = new double[(int) ((tmax+infD+latD)/dt)];
    for (i = 0; i < (int) ((latD+infD+tmax)/dt); i++) {
      PW[i] = 0;
    }
    if (appDays.length == 1) {
      if (tmax-21 > appStart) {
              for (i = (int) ((latD+infD+appStart)/dt); i < (int) ((latD+infD+appStart+21)/dt); i++) {
                  PW[i] = kwirkPro(i-(int) ((appStart+latD+infD)/dt));
              }
      }
      else {
              for (i = (int) ((latD+infD+appStart)/dt); i < (int) (tmax/dt); i++) {
                  PW[i] = kwirkPro(i-(int) ((appStart+latD+infD)/dt));
              }
      }
    }
    else {
       for(j = 0 ; j < appDays.length; j++) {
          if(j < appDays.length-1) {
              if(appDays[j+1]-appDays[j] <= 21) {
                  for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j+1]+latD+infD)/dt); i++) {
                      PW[i] = kwirkPro(i-((int) ((appDays[j]+latD+infD)/dt)));
                  }
              }
              else {
                   for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j]+21+latD+infD)/dt); i++) {
                       PW[i] = kwirkPro(i-((int) ((appDays[j]+latD+infD)/dt)));
                   }
              }
          }
          else if(j == appDays.length-1) {
              if(tmax-21 > appDays[j]) {
                  for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j]+21+latD+infD)/dt); i++) {
                      PW[i] = kwirkPro(i-((int) ((appDays[j]+latD+infD)/dt)));
                  }
              }
              else {
                   for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((tmax+latD+infD)/dt); i++) {
                       PW[i] = kwirkPro(i-((int) ((appDays[j]+latD+infD)/dt)));
                   }
              }
          }
       }
    }

    for(i = 0; i <= (int) ((latD+infD)/dt); i++) {
     erg[0][i]=1;
     erg[1][i]=0;
     erg[2][i]=0;
     erg[3][i]=0;
    }
    erg[1][(int) ((latD+infD)/dt)] = y0/2;
    erg[2][(int) ((latD+infD)/dt)] = y0/2;
    erg[0][(int) ((latD+infD)/dt)] = 1-y0;
    for(i = (int) ((latD+infD)/dt); i < (int) ((tmax+latD+infD+1)/dt)-1; i++){
      growthSum1 = 0; growthSum2 = 0;
      for (j = 1; j <= (int) (infD/dt-1); j++) {
        growthSum1 = growthSum1 + dt*FYPro(i-j-latD/dt, erg, PW);
        growthSum2 = growthSum2 + dt*W*(1-PW[i-j])*erg[0][i-j];
      }
      erg[1][i+1] = erg[1][i] + dt*FYPro(i, erg, PW) - dt*FYPro(i-latD/dt, erg, PW);
      erg[2][i+1] = erg[2][i] + dt*FYPro(i-latD/dt, erg, PW) + dt*W*(1-PW[i])*erg[0][i]*growthSum1 - dt*FYPro(i-(latD+infD)/dt, erg, PW)*(1 + growthSum2);
      erg[3][i+1] = erg[3][i] + dt*FYPro(i-(latD+infD)/dt, erg, PW)*(1 + growthSum2);
      erg[0][i+1] = 1 - erg[1][i+1] - erg[2][i+1] - erg[3][i+1];
    }
    return erg;
  }

  // protective efficiency of Mancozeb+Metalxyl formulation, Gutsche 1994
  public double kwirkProMet(double t) {
    double k;
    if (t*dt <= 1) {
      k = kwirkpro*(1 - 0.118061*t*dt);
    }
    else {
      k = kwirkpro*(1 - 0.103826*Math.pow(t*dt, -0.1505)*Math.pow(1.1371, t*dt));
    }
    return k;
  }

  //substitory function for curative pesticide application; here identical to FYPro
  public double FYCur(double t, double[][] res, double[] PW) {
    int j,k,u,o;
    double RY, sum1, sum2;
    if (t < (int)((latD+infD)/dt)) {
      RY = 0;
    }
    else {
      sum1 = 0; sum2 = 0;
      for (j = 1; j <= (int) (infD/dt)-1; j++) {
        for (k = 1; k <= j; k++) {
          u = (int) Math.floor(t-k);
          o = (int) Math.ceil(t-k);
          sum2 = sum2 + dt * ip(res[0],t-k,u,o);
        }
        u = (int) Math.floor(t-j-latD/dt);
        o = (int) Math.ceil(t-j-latD/dt);
        sum1 = sum1 + dt * R * (1-PW[(int) (t-j-latD/dt)]) * ip(res[0],t-j-latD/dt,u,o) * ip(res[2],t-j-latD/dt,u,o) * (1 +  dt * W * (1-PW[(int) (t-j-latD/dt)]) * sum2) * sp(t-j-(int) (latD/dt), t);
      }
      u = (int) Math.floor(t);
      o = (int) Math.ceil(t);
      RY = R*(1-PW[(int) t])*ip(res[0],t,u,o)*sum1;
    }
    return RY;
  }

//  public double PWproCur(double t) {
//    int i;
//    double res = 0;
//    for (i = 0; i < appDays.length; i++) {
//      if(t*dt >= appDays[i]+(int) (latD+infD) && t*dt < appDays[i]+21+(int) (latD+infD)) {
//        res = kwirkProMet(t-(int) ((appStart+latD+infD)/dt));
//        }
//    }
//    return res;
//  }

  public double PWcur(double t) {
    int i;
    double erg = 0;
    for(i = 0; i < appDays.length; i++) {
      if(t*dt >= appDays[i]+(int) (latD+infD) && t*dt < appDays[i]+1+(int) (latD+infD)) {
        erg = kwirkcur;
        }
    }
    return erg;
  }

  public double PWcurDelay(double t) {
    int i;
    double erg = 0;
    for(i = 0; i < appDays.length; i++) {
      if(t*dt >= appDays[i]+1+(int) (latD+infD) && t*dt <= appDays[i]+latD+(int) (latD+infD)) {
        erg = kwirkcur;
        }
    }
    return erg;
  }

  public double[][] executePhytKernelCur() {
    int i, j;
//    errorMsg(Double.toString((tmax+infD+latD)/dt)+"\n"+Integer.toString((int) ((tmax+infD+latD)/dt))+"\n"+Double.toString(Math.floor((tmax+infD+latD)/dt)));
    erg = new double[vars][(int) ((tmax+infD+latD+1)/dt)];
    double growthSum1, growthSum2;
    // initializing pesticide effect arrays, i.e. effect not as time dependend function, but as array with precalculated values
    // protective pesticide effect
    double PW[] = new double[(int) ((tmax+infD+latD)/dt)];
    // curative pesticide effect (PW1) + time delays (PW2 + PW3)
    double PW1[] = new double[(int) ((tmax+infD+latD)/dt)];
    double PW2[] = new double[(int) ((tmax+infD+latD)/dt)];
    double PW3[] = new double[(int) ((tmax+infD+latD)/dt)];

    // PW[] initialization
    for (i = 0; i < (int) ((latD+infD+tmax)/dt); i++) {
      PW[i] = 0; PW1[i] = 0; PW2[i] = 0; PW3[i] = 0;
    }

    if (appDays.length == 1) {
      if (tmax-21 > appStart) {
        for (i = (int) ((latD+infD+appStart)/dt); i < (int) ((latD+infD+appStart+21)/dt); i++) {
          PW[i] = kwirkProMet(i-(int) ((appStart+latD+infD)/dt));
        }
      }
      else {
        for (i = (int) ((latD+infD+appStart)/dt); i < (int) (tmax/dt); i++) {
          PW[i] = kwirkProMet(i-(int) ((appStart+latD+infD)/dt));
        }
      }
    }
    else {
       for(j = 0 ; j < appDays.length; j++) {
          if(j < appDays.length-1) {
              if(appDays[j+1]-appDays[j] <= 21) {
                  for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j+1]+latD+infD)/dt); i++) {
                      PW[i] = kwirkProMet(i-((int) ((appDays[j]+latD+infD)/dt)));
                  }
              }
              else {
                for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j]+21+latD+infD)/dt); i++) {
                  PW[i] = kwirkProMet(i-((int) ((appDays[j]+latD+infD)/dt)));
                }
              }
          }
          else if(j == appDays.length-1) {
              if(tmax-21 > appDays[j]) {
                  for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j]+21+latD+infD)/dt); i++) {
                      PW[i] = kwirkProMet(i-((int) ((appDays[j]+latD+infD)/dt)));
                  }
              }
              else {
                   for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((tmax+latD+infD)/dt); i++) {
                       PW[i] = kwirkProMet(i-((int) ((appDays[j]+latD+infD)/dt)));
                   }
              }
          }
       }
    }

    // PW1[] initialization
    for(j = 0; j < appDays.length; j++) {
      if (appDays[j] < tmax) {
        for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j]+latD+infD+1)/dt); i++) {
          PW1[i] = kwirkcur;
        }
      }
      else {
        PW1[(int) ((tmax+latD+infD)/dt-1)] = kwirkcur;
      }
    }

    // PW2[] intitialization
    for(j = 0; j < (int) (tmax+latD+infD)/dt; j++) {
      PW2[j] = PWcurDelay(j);
    }
//    for(j = 0; j < appDays.length; j++) {
//      if ((tmax-latD) > appDays[j]) {
//        for(i = (int) ((appDays[j]+latD+infD+1)/dt); i < (int) ((appDays[j]+latD+infD+latD)/dt); i++) {
//          PW2[i] = kwirkcur;
//        }
//      }
//      else {
//        for(i = (int) ((appDays[j]+latD+infD+1)/dt); i < (int) ((tmax+infD+latD)/dt); i++) {
//            PW2[i] = kwirkcur;
//        }
//      }
//    }

    // PW3[] initialization
    for(i = (int) ((latD+infD)/dt); i < (int) ((tmax+latD+infD)/dt); i++) {
      PW3[i] = PW2[i-(int) (infD/dt)];
    }
//    for(j = 0; j < appDays.length; j++) {
//      if ((tmax-latD-infD) > appDays[j]) {
//        for(i = (int) ((appDays[j]+latD+infD+infD+1)/dt); i < (int) ((appDays[j]+latD+infD+infD+latD)/dt); i++) {
//          PW3[i] = kwirkcur;
//        }
//      }
//      else {
//        for(i = (int) ((appDays[j]+latD+infD+infD+1)/dt); i < (int) ((tmax+infD+latD)/dt); i++) {
//            PW3[i] = kwirkcur;
//        }
//      }
//    }

    for(i = 0; i <= (int) ((latD+infD)/dt); i++) {
      erg[0][i]=1;
      erg[1][i]=0;
      erg[2][i]=0;
      erg[3][i]=0;
    }
    erg[1][(int) ((latD+infD)/dt)]=y0/2;
    erg[2][(int) ((latD+infD)/dt)]=y0/2;
    erg[0][(int) ((latD+infD)/dt)]=1-y0;
    for(i = (int) ((latD+infD)/dt); i < (int) ((tmax+latD+infD+1)/dt)-1; i++){
      growthSum1 = 0; growthSum2 = 0;
      for (j = 1; j <= (int) (infD/dt-1); j++) {
        growthSum1 = growthSum1 + dt*FYCur(i-j-latD/dt, erg, PW);
        growthSum2 = growthSum2 + dt*W*(1-PW[i-j])*erg[0][i-j];
      }
      erg[1][i+1] = (erg[1][i] + dt*FYCur(i, erg, PW) - dt*FYCur(i-latD/dt, erg, PW)*(1-PW2[i]))*(1-PW1[i]);
      erg[2][i+1] = erg[2][i] + dt*FYCur(i-latD/dt, erg, PW)*(1-PW2[i]) + dt*W*(1-PW[i])*erg[0][i]*growthSum1 - dt*FYCur(i-(latD+infD)/dt, erg, PW)*((1-PW3[i]) + growthSum2);
      erg[3][i+1] = erg[3][i] + dt*FYCur(i-(latD+infD)/dt, erg, PW)*((1-PW3[i]) + growthSum2);
      erg[0][i+1] = 1 - erg[1][i+1] - erg[2][i+1] - erg[3][i+1];
    }
    return erg;
  }

  public double[][] executePhytKernelCurTest() {
    int i, j, k;
//    errorMsg(Double.toString((tmax+infD+latD)/dt)+"\n"+Integer.toString((int) ((tmax+infD+latD)/dt))+"\n"+Double.toString(Math.floor((tmax+infD+latD)/dt)));
    erg = new double[vars][(int) ((tmax+infD+latD+1)/dt)];
    hist = new double[vars][(int) ((tmax+infD+latD)/dt)];
    double growthSum1, growthSum2;
    // initializing pesticide effect arrays, i.e. effect not as time dependend function, but as array with precalculated values
    // protective pesticide effect
    double PW[] = new double[(int) ((tmax+infD+latD)/dt)];
    // curative pesticide effect (PW1) + time delays (PW2 + PW3)
    double PW1[] = new double[(int) ((tmax+infD+latD)/dt)];
    double PW2[] = new double[(int) ((tmax+infD+latD)/dt)];
    double PW3[] = new double[(int) ((tmax+infD+latD)/dt)];

    // PW[] initialization
    for (i = 0; i < (int) ((latD+infD+tmax)/dt); i++) {
      PW[i] = 0; PW1[i] = 0; PW2[i] = 0; PW3[i] = 0;
    }

    if (appDays.length == 1) {
      if (tmax-21 > appStart) {
        for (i = (int) ((latD+infD+appStart)/dt); i < (int) ((latD+infD+appStart+21)/dt); i++) {
          PW[i] = kwirkProMet(i-(int) ((appStart+latD+infD)/dt));
        }
      }
      else {
        for (i = (int) ((latD+infD+appStart)/dt); i < (int) (tmax/dt); i++) {
          PW[i] = kwirkProMet(i-(int) ((appStart+latD+infD)/dt));
        }
      }
    }
    else {
       for(j = 0 ; j < appDays.length; j++) {
          if(j < appDays.length-1) {
              if(appDays[j+1]-appDays[j] <= 21) {
                  for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j+1]+latD+infD)/dt); i++) {
                      PW[i] = kwirkProMet(i-((int) ((appDays[j]+latD+infD)/dt)));
                  }
              }
              else {
                for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j]+21+latD+infD)/dt); i++) {
                  PW[i] = kwirkProMet(i-((int) ((appDays[j]+latD+infD)/dt)));
                }
              }
          }
          else if(j == appDays.length-1) {
              if(tmax-21 > appDays[j]) {
                  for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j]+21+latD+infD)/dt); i++) {
                      PW[i] = kwirkProMet(i-((int) ((appDays[j]+latD+infD)/dt)));
                  }
              }
              else {
                   for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((tmax+latD+infD)/dt); i++) {
                       PW[i] = kwirkProMet(i-((int) ((appDays[j]+latD+infD)/dt)));
                   }
              }
          }
       }
    }

    // PW1[] initialization
    for(j = 0; j < appDays.length; j++) {
      if (appDays[j] < tmax) {
        for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j]+latD+infD+1)/dt); i++) {
          PW1[i] = kwirkcur;
        }
      }
      else {
        PW1[(int) ((tmax+latD+infD)/dt-1)] = kwirkcur;
      }
    }

    // PW2[] intitialization
    for(j = 0; j < (int) (tmax+latD+infD)/dt; j++) {
      PW2[j] = PWcurDelay(j);
    }
//    for(j = 0; j < appDays.length; j++) {
//      if ((tmax-latD) > appDays[j]) {
//        for(i = (int) ((appDays[j]+latD+infD+1)/dt); i < (int) ((appDays[j]+latD+infD+latD)/dt); i++) {
//          PW2[i] = kwirkcur;
//        }
//      }
//      else {
//        for(i = (int) ((appDays[j]+latD+infD+1)/dt); i < (int) ((tmax+infD+latD)/dt); i++) {
//            PW2[i] = kwirkcur;
//        }
//      }
//    }

    // PW3[] initialization
    for(i = (int) ((latD+infD)/dt); i < (int) ((tmax+latD+infD)/dt); i++) {
      PW3[i] = PW2[i-(int) (infD/dt)];
    }
//    for(j = 0; j < appDays.length; j++) {
//      if ((tmax-latD-infD) > appDays[j]) {
//        for(i = (int) ((appDays[j]+latD+infD+infD+1)/dt); i < (int) ((appDays[j]+latD+infD+infD+latD)/dt); i++) {
//          PW3[i] = kwirkcur;
//        }
//      }
//      else {
//        for(i = (int) ((appDays[j]+latD+infD+infD+1)/dt); i < (int) ((tmax+infD+latD)/dt); i++) {
//            PW3[i] = kwirkcur;
//        }
//      }
//    }

    for(i = 0; i <= (int) ((latD+infD)/dt); i++) {
      erg[0][i]=1;
      erg[1][i]=0;
      erg[2][i]=0;
      erg[3][i]=0;
      hist[0][i]=1;
      hist[1][i]=0;
      hist[2][i]=0;
      hist[3][i]=0;
    }
    erg[1][(int) ((latD+infD)/dt)]=y0/2;
    erg[2][(int) ((latD+infD)/dt)]=y0/2;
    erg[0][(int) ((latD+infD)/dt)]=1-y0;
    hist[1][(int) ((latD+infD)/dt)]=y0/2;
    hist[2][(int) ((latD+infD)/dt)]=y0/2;
    hist[0][(int) ((latD+infD)/dt)]=1-y0;
    for(i = (int) ((latD+infD)/dt); i < (int) ((tmax+latD+infD+1)/dt)-1; i++){
      growthSum1 = 0; growthSum2 = 0;
      for (j = 1; j <= (int) (infD/dt-1); j++) {
        growthSum1 = growthSum1 + dt*FYCur(i-j-latD/dt, hist, PW);
        growthSum2 = growthSum2 + dt*W*(1-PW[i-j])*hist[0][i-j];
      }
      erg[1][i+1] = (hist[1][i] + dt*FYCur(i, hist, PW) - dt*FYCur(i-latD/dt, hist, PW))*(1-PW1[i]);
      erg[2][i+1] = hist[2][i] + dt*FYCur(i-latD/dt, hist, PW) + dt*W*(1-PW[i])*hist[0][i]*growthSum1 - dt*FYCur(i-(latD+infD)/dt, hist, PW)*(1 + growthSum2);
      erg[3][i+1] = hist[3][i] + dt*FYCur(i-(latD+infD)/dt, hist, PW)*(1 + growthSum2);
      erg[0][i+1] = 1 - erg[1][i+1] - erg[2][i+1] - erg[3][i+1];
      hist[0][i+1] = erg[0][i+1];
      hist[1][i+1] = erg[1][i+1];
      hist[2][i+1] = erg[2][i+1];
      hist[3][i+1] = erg[3][i+1];
      for (j = 0; j < appDays.length; j++) {
        if((appDays[j]/dt+(int) ((latD+infD)/dt)) == i) {
          for (k = 1; k <= (int) ((latD)/dt); k++) {
            hist[0][i-k] = erg[0][i-k]+erg[1][i-k]*kwirkcur;
            hist[1][i-k] = erg[1][i-k]*(1-kwirkcur);
          }
          for (k = 1; k <= (int) ((latD)/dt); k++) {
            hist[2][i-(int) (latD/dt)-k] = erg[2][i-(int) (latD/dt)-k]*(1-kwirkcur);
          }
          }
      }
    }
    return erg;
  }

  //substitory function for eradicant pesticide application
  public double FYEra(double t, double[][] res, double[] PW, double[] PWera) {
    int j,k,u,o;
    double RY, sum1, sum2;
    if (t < (int)((latD+infD)/dt)) {
      RY = 0;
    }
    else {
      sum1 = 0; sum2 = 0;
      for (j = 1; j <= (int) (infD/dt)-1; j++) {
        for (k = 1; k <= j; k++) {
          u = (int) Math.floor(t-k);
          o = (int) Math.ceil(t-k);
          sum2 = sum2 + dt * ip(res[0],t-k,u,o);
        }
        u = (int) Math.floor(t-j-latD/dt);
        o = (int) Math.ceil(t-j-latD/dt);
        sum1 = sum1 + dt * R * (1-PW[(int) (t-j-latD/dt)]) * ip(res[0],t-j-latD/dt,u,o) * ip(res[2],t-j-latD/dt,u,o) * (1-PWera[(int) (t-j-latD/dt)]) * (1 +  dt * W * (1-PW[(int) (t-j-latD/dt)]) * sum2) * sp(t-j-(int) (latD/dt), t);
      }
      u = (int) Math.floor(t);
      o = (int) Math.ceil(t);
      RY = R*(1-PW[(int) t])*ip(res[0],t,u,o)*sum1;
    }
    return RY;
  }


  public double PWeraDelay(double t) {
    int i;
    double erg = 0;
    for(i = 0; i < appDays.length; i++) {
      if(t*dt >= appDays[i]+1+(int) (latD+infD) && t*dt <= appDays[i]+infD+(int) (latD+infD)) {
        erg = kwirkera;
        }
    }
    return erg;
  }


  public double[][] executePhytKernelEra() {
    int i, j;
    erg = new double[vars][(int) ((tmax+infD+latD+1)/dt)];
    double growthSum1, growthSum2;
    // initializing pesticide effect arrays, i.e. effect not as time dependend function, but as array with precalculated values
    // protective pesticide effect
    double PW[] = new double[(int) ((tmax+infD+latD)/dt)];
    // curative pesticide effect + time delays
    double PW1[] = new double[(int) ((tmax+infD+latD)/dt)]; // application days for curative fungicide
    double PW2[] = new double[(int) ((tmax+infD+latD)/dt)]; // delay latD
    double PW3[] = new double[(int) ((tmax+infD+latD)/dt)]; // delay latD+infD
    // eradicant pesticide effect
    double PW4[] = new double[(int) ((tmax+infD+latD)/dt)]; // application days for eradicant fungicide
    double PW5[] = new double[(int) ((tmax+infD+latD)/dt)]; // delay infD

    // PW[] initialization
    for (i = 0; i < (int) ((latD+infD+tmax)/dt); i++) {
      PW[i] = 0; PW1[i] = 0; PW2[i] = 0; PW3[i] = 0; PW4[i] = 0; PW5[i] = 0;
    }

    if (appDays.length == 1) {
      if (tmax-21 > appStart) {
        for (i = (int) ((latD+infD+appStart)/dt); i < (int) ((latD+infD+appStart+21)/dt); i++) {
          PW[i] = kwirkProMet(i-(int) ((appStart+latD+infD)/dt));
        }
      }
      else {
        for (i = (int) ((latD+infD+appStart)/dt); i < (int) (tmax/dt); i++) {
          PW[i] = kwirkProMet(i-(int) ((appStart+latD+infD)/dt));
        }
      }
    }
    else {
       for(j = 0 ; j < appDays.length; j++) {
          if(j < appDays.length-1) {
              if(appDays[j+1]-appDays[j] <= 21) {
                  for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j+1]+latD+infD)/dt); i++) {
                      PW[i] = kwirkProMet(i-((int) ((appDays[j]+latD+infD)/dt)));
                  }
              }
              else {
                for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j]+21+latD+infD)/dt); i++) {
                  PW[i] = kwirkProMet(i-((int) ((appDays[j]+latD+infD)/dt)));
                }
              }
          }
          else if(j == appDays.length-1) {
              if(tmax-21 > appDays[j]) {
                  for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j]+21+latD+infD)/dt); i++) {
                      PW[i] = kwirkProMet(i-((int) ((appDays[j]+latD+infD)/dt)));
                  }
              }
              else {
                   for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((tmax+latD+infD)/dt); i++) {
                       PW[i] = kwirkProMet(i-((int) ((appDays[j]+latD+infD)/dt)));
                   }
              }
          }
       }
    }

    // PW1[] & PW4[] initialization
    for(j = 0; j < appDays.length; j++) {
      if (appDays[j] < tmax) {
        for(i = (int) ((appDays[j]+latD+infD)/dt); i < (int) ((appDays[j]+latD+infD+1)/dt); i++) {
          PW1[i] = kwirkcur;
          PW4[i] = kwirkera;
        }
      }
      else {
        PW1[(int) ((tmax+latD+infD)/dt-1)] = kwirkcur;
        PW4[(int) ((tmax+latD+infD)/dt-1)] = kwirkera;
      }
    }

    // PW2[] intitialization
    for(j = 0; j < (int) (tmax+latD+infD)/dt; j++) {
      PW2[j] = PWcurDelay(j);
    }


    // PW3[] initialization
    for(i = (int) ((latD+infD)/dt); i < (int) ((tmax+latD+infD)/dt); i++) {
      PW3[i] = PW2[i-(int) (infD/dt)];
    }

    // PW5[] intitialization
    for(j = 0; j < (int) (tmax+latD+infD)/dt; j++) {
      PW5[j] = PWeraDelay(j);
    }
//    for(j = 0; j < appDays.length; j++) {
//      if ((tmax-infD) > appDays[j]) {
//        for(i = (int) ((appDays[j]+latD+infD+1)/dt); i <= (int) ((appDays[j]+latD+infD+infD)/dt); i++) {
//          PW5[i] = kwirkera;
//        }
//      }
//      else {
//        for(i = (int) ((appDays[j]+latD+infD+1)/dt); i < (int) ((tmax+infD+latD)/dt); i++) {
//            PW5[i] = kwirkera;
//        }
//      }
//    }

    // actual algorithm
    for(i = 0; i <= (int) ((latD+infD)/dt); i++) {
      erg[0][i]=1;
      erg[1][i]=0;
      erg[2][i]=0;
      erg[3][i]=0;
    }
    erg[1][(int) ((latD+infD)/dt)]=y0/2;
    erg[2][(int) ((latD+infD)/dt)]=y0/2;
    erg[0][(int) ((latD+infD)/dt)]=1-y0;
    for(i = (int) ((latD+infD)/dt); i < (int) ((tmax+latD+infD+1)/dt)-1; i++){
      growthSum1 = 0; growthSum2 = 0;
      for (j = 1; j <= (int) (infD/dt-1); j++) {
        growthSum1 = growthSum1 + dt*FYCur(i-j-latD/dt, erg, PW);
        growthSum2 = growthSum2 + dt*W*(1-PW[i-j])*erg[0][i-j];
      }
      erg[1][i+1] = (erg[1][i] + dt*FYEra(i, erg, PW, PW5) - dt*FYEra(i-latD/dt, erg, PW, PW5)*(1-PW2[i]))*(1-PW1[i]);
      erg[2][i+1] = (erg[2][i] + dt*FYEra(i-latD/dt, erg, PW, PW5)*(1-PW2[i]) + dt*W*(1-PW[i])*erg[0][i]*growthSum1 - dt*FYEra(i-(latD+infD)/dt, erg, PW, PW5)*((1-PW3[i]) + growthSum2)*(1-PW5[i]))*(1-PW4[i]);
      erg[3][i+1] = erg[3][i] + dt*FYEra(i-(latD+infD)/dt, erg, PW, PW5)*((1-PW3[i]) + growthSum2)*(1-PW5[i])+erg[2][i]*PW4[i];
      erg[0][i+1] = 1 - erg[1][i+1] - erg[2][i+1] - erg[3][i+1];
    }
    return erg;
  }


  public void errorMsg(String msg) {
    JOptionPane.showMessageDialog(null, msg, "PhytMod message", 1);
  }
} //end class