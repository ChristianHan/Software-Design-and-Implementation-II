/* 
 * This file is how you might test out your code.  Don't submit this, and don't 
 * have a main method in SortTools.java.
 */

package assignment1;
public class Main {
	public static void main(String [] args) {

		//int a;

		//int v = 2;
		//int testarray[] = new int[n+1];

		int n = 5;

//		int[] x = {1, 2, 3, 5, 6, 7, 8, 9};
		int[] x = {3,2,7,5,1,4,15,11};



		SortTools.insertSort(x, n);

		System.out.println(java.util.Arrays.toString(x));












		// call your test methods here
		// SortTools.isSorted() etc.
	}
}
