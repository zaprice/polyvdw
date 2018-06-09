
package polyvdw;

import java.util.ArrayList;
import static polyvdw.VdwLib.*;

public class Check {

  static final int MAX_SQUARE = 100000;

  public static void main(String[] args) throws Exception {

    ArrayList<long[]> kthrees = read("/Users/zach/Documents/polyvdw/kthrees.csv");
    long[] squares = getSquares(MAX_SQUARE);

    for(long[] kthree : kthrees) {
      for(long square : squares) {
        check(kthree, square);
      }
    }
  }

  public static long[] getSquares(int max) {
    long[] squares = new long[max];
    for(int i = 0; i < MAX_SQUARE; i++) {
      squares[i] = sq(i+1);
    }
    return(squares);
  }

  // Add @square to the largest value (@kthree[2])
  // Then check if other differences are also square
  public static void check(long[] kthree, long square) {
    long w = sq(kthree[2]) + square;
    if(isSquare(w-sq(kthree[0])) && isSquare(w-sq(kthree[1]))) {
      String printstr = rowToString(kthree) + ", " + w;
      System.out.println(printstr);
      if(isSquare(w)) {
        System.out.println("^ is a K4");
      }
    }
  }

}
