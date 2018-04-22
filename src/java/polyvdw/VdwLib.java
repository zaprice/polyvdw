
package polyvdw;

import java.util.Arrays;

public class VdwLib {

  public static long sq(long n) {
    return(n*n);
  }

  public static boolean isSquare(long n) {
    return(sq((long)Math.sqrt(n)) == n);
  }

  public static String rowToString(long[] kthree) {
    String str = Arrays.toString(kthree);
    return(str.substring(1,str.length()-1));
  }
}
