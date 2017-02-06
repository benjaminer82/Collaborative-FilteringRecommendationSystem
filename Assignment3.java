package assignment3;
import java.util.*;
import java.io.*;
import assignment3.Matrix;

public class Assignment3 {
    

    public static void main(String[] args) throws IOException {
        
        int U = 0, I = 0, selection = 0,  K = 500;
        double D[] = new double[500];
        double Distance[] = new double[500];
        double KNN[] = new double[K];
        double Weight[] = new double[K];
        double r[] = new double[500];
        double Relation[] = new double[500];
        double Ave[] = new double[500];
        int UNo[] = new int[500];
double Prediction, Tempsum = 0, ActualR = 0, error = 0;


        
// Here's how to read a matrix from a CSV file

	BufferedReader breader = new BufferedReader(new FileReader("movielens-250x500.csv"));
	Matrix matrix = new Matrix(breader);
	breader.close();
	System.out.println("* here is the matrix: " + matrix);

//Here¡¯s the interface of the system

        System.out.println("Please choose the method you want to use to make a prediction.\n1 for MSD Algorithm\n2 for Pearson¡¯s Algorithm\n");
	InputStreamReader input = new InputStreamReader(System.in);
	BufferedReader bufread = new BufferedReader(input);
	try{
	 	selection = Integer.parseInt(bufread.readLine());
	    }
	catch(Exception e){};
        System.out.println("Please enter the parameter K(1~499) you want to define as the KNN in the two methods: ");
	InputStreamReader input2 = new InputStreamReader(System.in);
        BufferedReader bufread2 = new BufferedReader(input2);
        try{
                 K = Integer.parseInt(bufread2.readLine());
            }
            catch(Exception e){};
            input.close();
            bufread.close();
            input2.close();
            bufread2.close();
        
        
      if ( K > 0 && K < 500 && (selection == 1 || selection == 2 ))
       {
        if (selection == 1)
        {
              
//---------------------------------------------Here's MSD algorithm--------------------------------------------              
          
          for( int ab = 0; ab < 1000; ab++)
          {

// Here's how to calculate the distances between the chosen random user and other users

            for ( int i = 0; i < 499; i++)
            {   
                U = (int) (Math.random()*matrix.nusers());       
                I = (int) (Math.random()*matrix.nitems());
                ActualR = matrix.get(U,I);
                if (ActualR == Matrix.MISSING)
                   continue;
                else
                    break;
            }
        
        for (int u = 0; u < matrix.nusers(); u++)
        {   
            UNo[u] = u;

 /* Here's how to get the average rating for some random user*/ 
            //
            double sum = 0;
            int nrated = 0;
            for (int ti = 0; ti < matrix.nitems(); ti++) {
                double RU = matrix.get(u,ti);
                if (RU != Matrix.MISSING) {
                    sum += RU;
                    nrated++;
                }
            }
            Ave[u] = sum/nrated;
             // 
        
            if (u != U){                
            int N = 0;
            double Dissum = 0 ;                                       
            for(int i = 0; i < matrix.nitems(); i++)
            {
                double T = matrix.get(U,i);
                double R = matrix.get(u,i);
                if (T != Matrix.MISSING && R != Matrix.MISSING){
                Dissum += ((T-R)*(T-R));
                N++;               
                }
               }
             if (N == 0){
                D[u] = 100;                
                }
             else    D[u] = Dissum/N;
             Distance[u] = D[u];
             }  
            }

//Here's how to sort the distance 

        double t = 0;
        int tu = 0;        
        for( int l = 0; l < matrix.nusers() - 2; l++){
            for( int i = 0; i < matrix.nusers() - 2 - l; i++){
                if(D[i] > D[i+1]){
                   t = D[i];
                   tu = UNo[i];
                   D[i] = D[i+1];
                   UNo[i] = UNo[i+1];
                   D[i+1] = t;
                   UNo[i+1] = tu;
                  }
               }
           }
        

//Here's how to find the "k" nearest neighbors and make a prediction       

        for( int m=0; m < K; m++){
            int No = UNo[m];
            KNN[m] = Distance[No];
            Weight[m] = 1 - KNN[m];
            double pr = matrix.get(No,I);
            if ( pr == Matrix.MISSING)
                continue;
            Tempsum += (Weight[m]*(pr-Ave[No]));
        }
        Prediction = Ave[U]+Tempsum/K;
        
        if (Prediction<0.5)
            Prediction=0;
        else 
            Prediction=1;
        
        if (Prediction != ActualR)
          { error++;
            System.out.println("Prediction = "+Prediction+" which is wrong.");
          }
         else
            System.out.println("Prediction = "+Prediction+" which is right.");
          }
        System.out.println("average error when using MSD algorithm is "+error/1000);
          }

//--------------------------------------------Here's Pearson's algorithm-----------------------------------------          
        
       else
         {

// Here's how to calculate the "r"s between the chosen random user and other users

         for( int ab = 0; ab < 1000; ab++)
         {
            for ( int i = 0; i < 499; i++)
            {   
                U = (int) (Math.random()*matrix.nusers());       
                I = (int) (Math.random()*matrix.nitems());
                ActualR = matrix.get(U,I);
                if (ActualR == Matrix.MISSING)
                   continue;
                else
                    break;
             }
        
             for (int u = 0; u < matrix.nusers(); u++)
             {   
                UNo[u] = u;

        /* Here's how to get the average rating for some random user*/ 
                //
                double sum = 0;
                int nrated = 0;
                for (int ti = 0; ti < matrix.nitems(); ti++) {
                    double RU = matrix.get(u,ti);
                    if (RU != Matrix.MISSING){
                        sum += RU;
                        nrated++;
                    }
                }
                Ave[u] = sum/nrated;
                 // 
        
            if (u != U){                
            int N = 0;
            double rsum1 = 0, rsum2 = 0, rsum3 = 0, rsum4 = 0, rsum5 = 0;                                       
            for(int i = 0; i < matrix.nitems(); i++)
            {
                double T = matrix.get(U,i);
                double R = matrix.get(u,i);
                if (T != Matrix.MISSING){
                    rsum2 += T;
                    rsum3 += T * T;
                }
                if (R != Matrix.MISSING){
                    rsum4 += R;
                    rsum5 += R * R;
                }
                if (T != Matrix.MISSING && R != Matrix.MISSING){
                    rsum1 += T * R;
                    N++;
                }
                 r[u] = (rsum1 - rsum2 * rsum4 / matrix.nitems()) / (Math.sqrt(rsum3 - (rsum2 * rsum2 / matrix.nitems()))*Math.sqrt(rsum5 -(rsum4 * rsum4/matrix.nitems())));
                 if (N == 0)
                 {
                     r[u] = 100;
                 }
                 Relation[u] = r[u];
             }  
            }
           } 
                
//Here's how to sort the relations 

        double t = 0;
        int tu = 0;        
        for( int l = 0; l < matrix.nusers() - 2; l++){
            for( int i = 0; i < matrix.nusers() - 2 - l; i++){
                if(r[i] > r[i+1]){
                   t = r[i];
                   tu = UNo[i];
                   r[i] = r[i+1];
                   UNo[i] = UNo[i+1];
                   r[i+1] = t;
                   UNo[i+1] = tu;
                  }
               }
           }

//Here's how to find the "k" nearest neighbors and make a prediction 

        for( int m=0; m < K; m++){
            int No = UNo[m];
            KNN[m] = Relation[No];
            double Rr = matrix.get(No,I);
            if ( Rr == Matrix.MISSING)
                continue;
            else Tempsum += (KNN[m]*(Rr-Ave[No]));
        }
        Prediction = Ave[U]+Tempsum/K;
        
        if (Prediction<0.5)
            Prediction=0;
        else 
            Prediction=1;
        
        if (Prediction != ActualR)
          { error++;
            System.out.println("Prediction = "+Prediction+" which is wrong.");
          }
         else
            System.out.println("Prediction = "+Prediction+" which is right.");
          }
          System.out.println("average error when using Pearson's algorithm is "+error/1000);
    }
    }    
         else
             System.out.println("\nSorry, You didn't choose neither of the motheds or the K you gave was out of range, Best Luck next time!\n");

    }
}
