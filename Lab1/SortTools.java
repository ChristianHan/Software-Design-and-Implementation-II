// SortTools.java 
/*
 * EE422C Project 1 submission by
 * Replace <...> with your actual data.
 * Christian Han
 * cjh3752
 * 76085
 * Summer 2018
 * Slip days used: 0
 */

package assignment1;
public class SortTools {


	public static boolean isSorted(int[] x, int n){ // returns true if array is sorted else false.
		int i;

		if (x.length == 0 || n == 0){
			return false;
		}

		for (i = 0; i < n-1; i++){
			if (x[i] > x[i + 1]){
				return false;
			}
		}
		return true;
	}

	public static int find(int[] x, int n, int v) { //finds the index of a number in an array

		int start = 0;
		int finish = n - 1;

		while (start <= finish){
			int middle = start + (finish - start) / 2;
			if (x[middle] == v){
				return middle;
			}
			if (x[middle] > v){
				finish = middle - 1;
			}
			else{
				start = middle + 1;
			}
		}
		return -1; //returns -1 if v is not within first n elements of x
	}

	public static int[] insertGeneral(int[] x, int n, int v){

		int newnums [] = new int[n+1];
		int newnums1 [] = new int[n];
		int i = 0;
		int k = 0;
		int remaining = 0;

		int index = SortTools.find(x, n, v);//returns index of v
		if(index == -1){						// condition when v is missing from array

			while (x[k] < v){   //while v is
				newnums[k] = x[k];
				k = k + 1;
			}

			remaining = n - k;
			newnums[k] = v;

			while (remaining > 0){
				newnums[k + 1] = x[k];
				k = k + 1;
				remaining = remaining - 1;
			}
			return newnums;
		}

		else{                     //condition for when v is present in x
			for (i = 0; i < n; i++){
				newnums1[i] = x[i];
			}
		}
		return newnums1;
	}

	public static int insertInPlace(int[] x, int n, int v) {

		int index = SortTools.find(x, n, v);//returns index of v
		if(index == -1){						//condition when v is missing from array

			x = SortTools.insertGeneral(x, n, v);
			return n+1;

		}
		return n; //condition when v is already in array
	}

	public static void insertSort(int[] x, int n) {

		int i, j;
		int temp;

		for (i = 1; i < n; i++) {
			for (j = i; j > 0; j--) {
				if (x[j] < x[j - 1]) {
					temp = x[j];
					x[j] = x[j - 1];
					x[j - 1] = temp;
				}
			}
		}
	}
}
