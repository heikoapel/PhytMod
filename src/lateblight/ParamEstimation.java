package lateblight;

/**
 * Title:        PhytMod - Parameter estimation class
 * Description:  Modelling Phytophthora infestans and general epidemics
 * Copyright:    Copyright Heiko Apel (c) 2002
 * Company:      Institute of Geoecology, TU Braunschweig
 * @author Heiko Apel
 * @version 1.0
 */

import optimization.*;
import java.lang.*;



public class ParamEstimation extends Object implements optimization.Lmdif_fcn {

//   public int tmax, lat, inf;
//   public double R, y0, dt, W, spint;

    // epsmch is the machine precision
   static final double epsmch = 2.22044604926e-16;

    // for counting function and Jacobian evaluations
   int nfev = 0;
   int njev = 0;

   static PhytKernel model = new PhytKernel();

   static double[][] modelData;
   static double[][] measData = {{0., 0.135},{7., 0.27},{14., 0.065},{21., 0.105},{28., 0.16},
                            {35., 0.78},{42., 0.8},{50., 0.935},{59., 0.88},{66., 1.},{73., 1.}};

   // constructor 1
   public ParamEstimation(PhytKernel kernel) {
    this.model = kernel;
   }

   // constructor 2
   public ParamEstimation() {
    this.model = new PhytKernel();
    model.latD = 3.0;
    model.lat = (int) model.latD;
    model.infD = 11.0;
    model.inf = (int) model.infD;
    model.R = 0.33;
    model.y0 = 0.07;
    model.tmax = 90;
    model.dt = 1.;
   }

   public void runEstimation(ParamEstimation lmdiftest) {

       int i,k,m,n,nread,ntries,nwrite;

       int info[] = new int[2];

       double factor,fnorm1,fnorm2,tol;

       tol = Math.sqrt(epsmch);

       n = 3;
       m = 11;
       ntries = 1;

       // array 1 size larger than m and n respectively
       // (because of Fortrun notation of arrays starting count at 1)
       double fvec[] = new double[m+1];
       double x[] = new double[n+1];

//       ParamEstimation lmdiftest = new ParamEstimation(model);

       factor = 1;

       for (k = 1; k <= ntries; k++) {

          ParamEstimation.initpt_f77(n,x,factor);

          ParamEstimation.ssqfcn_f77(m,n,x,fvec);

          fnorm1 = Minpack_f77.enorm_f77(m,fvec);

          System.out.print("\n\n PhytMod optimization"+
                           ", dimensions:  " + n + "  " + m + "\n");


          lmdiftest.nfev = 0;
          lmdiftest.njev = 0;

          Minpack_f77.lmdif1_f77(lmdiftest,m,n,x,fvec,tol,info);

          ParamEstimation.ssqfcn_f77(m,n,x,fvec);

          fnorm2 = Minpack_f77.enorm_f77(m,fvec);

          System.out.print("\n Initial L2 norm of the residuals: " + fnorm1 +
                           "\n Final L2 norm of the residuals: " + fnorm2 +
                           "\n Number of function evaluations: " + lmdiftest.nfev +
                           "\n Number of Jacobian evaluations: " + lmdiftest.njev/n +
                           "\n Info value: " + info[1] +
                           "\n\n Final approximate solution: \n\n");


          for (i = 1; i <= n; i++) {

             System.out.print(" parameter "+i+" = "+x[i] + "\n");

          }

          System.out.print(" all parameters:\n "+"l = "+model.latD+"  "+"i = "+model.infD+"  "+"R = "+model.R+"  "+"y0 = "+model.y0);

          factor *= 10;

       }

   }


// fcn - is required by lmdif in Minpack
   public void fcn(int m, int n, double x[], double fvec[],
                   int iflag[]) {

       // ruft ssqfcn und zählt hier nummer der functions and Jacobian evaluations hoch
       // wird von Minpack.lmdif aufgerufen
      ParamEstimation.ssqfcn_f77(m,n,x,fvec);
      if (iflag[1] == 1) this.nfev++;
      if (iflag[1] == 2) this.njev++;

      return;

   }

// constructing of least squares functions
    public static void ssqfcn_f77(int m, int n, double x[], double fvec[]) {

      model.R = x[1];
      if (model.R > 1) {
        model.R = 1;
      }
      model.latD = java.lang.Math.abs(x[2]);
      model.infD = java.lang.Math.abs(x[3]);
      if (model.infD > 20) {
        model.infD = 20;
      }
//      model.y0 = x[4];
//      model.errorMsg(Double.toString(x[1])+"\n"+Double.toString(x[2])+"\n"+Double.toString(x[3])+"\n"+Double.toString(x[4]));
//      model.errorMsg(Double.toString(x[1])+"\n"+Double.toString(x[2]));
      modelData = model.executeKernel();
      int i, temp;
      double tmp1,tmp2;

      for (i = 0; i < measData.length; i++) {

         temp = (int) measData[i][0];
         tmp1 = modelData[2][temp+(int) (model.latD+model.infD)];
         tmp2 = modelData[3][temp+(int) (model.latD+model.infD)];
         fvec[i+1] = measData[i][1] - (tmp1+tmp2);
      }

      return;
   }



   public static void initpt_f77(int n, double x[], double factor) {

      int j;

      // starting estimations for ssqfcn_f77

            x[1] = 0.8;
            x[2] = 7.;
            x[3] = 20.;
//            x[4] = 0.05;

     // Compute multiple of initial point.

      if (factor == 1) return;

         for (j = 1; j <= n; j++) {

            x[j] *= factor;

         }

      return;

   }



}
