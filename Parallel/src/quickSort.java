// parallel quick sort

import java.io.*;
import mpi.*;
import java.util.Random;

public class quickSort {
       public static void main(String args[]) throws IOException{
        MPI.Init(args); 
        int rank = MPI.COMM_WORLD.Rank(); 
        int size = MPI.COMM_WORLD.Size(); 
       
        if(rank == 0){
            Random x= new Random();
            int[] arr = new int[100000];
            int[] arr0 = new int[50000];
            int[] arr1 = new int[50000];
            int[] arr2= new int[100000];
            
            for(int i=0 ; i<100000 ; i++)
                arr[i]= x.nextInt()%10000;
        
            MPI.COMM_WORLD.Send(arr, 0, 50000 , MPI.INT, 1, 10);  
            MPI.COMM_WORLD.Send(arr, 50000 , 50000 , MPI.INT, 2, 10);
            MPI.COMM_WORLD.Recv(arr0, 0, 50000 , MPI.INT, 1, 10);
            MPI.COMM_WORLD.Recv(arr1, 0 ,50000 , MPI.INT,2, 10);
            
            arr2= merge(arr0, arr1);
            for(int i=0 ; i< arr.length ; i++)
                System.out.print(arr[i]+"    ");
            System.out.println("");
            for(int i=0 ; i< arr2.length ; i++)
                System.out.print(arr2[i]+"    ");
            
            write("file.txt", arr2);
                     
        }
        else if(rank == 1){
            int[] array0 = new int[50000];
            MPI.COMM_WORLD.Recv(array0, 0, 50000 , MPI.INT, 0, 10);
            quicksort(array0 ,0 ,(array0.length-1));
             MPI.COMM_WORLD.Send(array0, 0,50000 , MPI.INT, 0, 10); 
           
            
            
            
        }
        else if(rank == 2){
            int[] array1 = new int[50000];
            MPI.COMM_WORLD.Recv(array1, 0,50000 , MPI.INT, 0, 10);
            quicksort(array1 ,0 ,(array1.length-1));
            MPI.COMM_WORLD.Send(array1, 0,50000, MPI.INT, 0, 10);  
            
            
        }
       }
       
       static void quicksort(int arr[] , int low , int high){
	if (low < high){
		int p = partition(arr , low ,high) ;
                quicksort(arr ,low , p-1) ;
		quicksort(arr , p+1 , high);
	}
}

    static int partition(int a[] , int low , int high){
	
        int x= a[low];
        int i= low;
        for(int j=low+1 ; j<=high ; j++){
       
            if(a[j]<=x){
               i++;
               int temp = a[i];
               a[i]=a[j];
               a[j]=temp;}
            
             
        }
          int temp = a[low];
               a[low]=a[i];
               a[i]=temp;   
               
              
        return i;
        }
    
    public static int[] merge(int[] a, int[] b) {

    int[] answer = new int[a.length + b.length];
    int i = 0, j = 0, k = 0;
    while (i < a.length && j < b.length)
    {
        if (a[i] < b[j])
        {
            answer[k] = a[i];
            i++;
        }
        else
        {
            answer[k] = b[j];
            j++;
        }
        k++;
    }

    while (i < a.length)
    {
        answer[k] = a[i];
        i++;
        k++;
    }

    while (j < b.length)
    {
        answer[k] = b[j];
        j++;
        k++;
         }

    return answer;
}
    public static void write (String filename, int[]x) throws IOException{
  BufferedWriter outputWriter = null;
  outputWriter = new BufferedWriter(new FileWriter(filename));
  for (int i = 0; i < x.length; i++) {
    // Maybe:
    outputWriter.write(x[i]+"       ");
    
    outputWriter.newLine();
  }
  outputWriter.flush();  
  outputWriter.close();  
}
}



