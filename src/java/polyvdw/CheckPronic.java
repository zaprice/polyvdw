
package polyvdw;

import java.util.ArrayList;
import static polyvdw.VdwLib.*;

public class CheckPronic {

  static final int MAX = 100000;

  public static void main(String[] args) throws Exception {

    ArrayList<long[]> kthrees = read("/Users/zach/Documents/polyvdw/pronic_kthrees.csv");
    long[] pronics = getPronics(MAX);

    for(long[] kthree : kthrees) {
      for(long pronic : pronics) {
        check(kthree, pronic);
      }
    }
  }

  public static long[] getPronics(int max) {
    long[] pronics = new long[max];
    for(int i = 0; i < MAX; i++) {
      pronics[i] = sq(i+1) + i+1;
    }
    return(pronics);
  }

  // Add @pronic to the largest value (@kthree[2])
  // Then check if other differences are also pronic
  public static void check(long[] kthree, long pronic) {
    long w = sq(kthree[2]) + kthree[2] + pronic;
    if(isPronic(w-sq(kthree[0])-kthree[0]) && isPronic(w-sq(kthree[1])-kthree[1])) {
      String printstr = rowToString(kthree) + ", " + w;
      System.out.println(printstr);
      if(isPronic(w)) {
        System.out.println("^ is a K4");
      }
    }
  }

}
