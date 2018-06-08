
package polyvdw;

import java.util.Arrays;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.io.IOException;

public class VdwLib {

  public static long sq(long n) {
    return(n*n);
  }

  public static boolean isSquare(long n) {
    return(sq((long)Math.sqrt(n)) == n);
  }

  // Return true if all elements of ary are odd
  public static boolean odd(long[] ary) {
    for(long i : ary) {
      if((i % 2) == 0) {
        return(false);
      }
    }
    return(true);
  }

  public static String rowToString(long[] kthree) {
    String str = Arrays.toString(kthree);
    return(str.substring(1,str.length()-1));
  }

  public static void write(ArrayList<long[]> kthrees, String filename) throws IOException {
    ArrayList<String> output = new ArrayList<String>(kthrees.size());
    for(long[] kthree : kthrees) {
      output.add(rowToString(kthree));
    }
    Files.write(Paths.get(filename), output, Charset.forName("UTF-8"));
  }
}
