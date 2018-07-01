
package polyvdw;

import java.util.HashMap;
import java.util.ArrayList;
import static polyvdw.VdwLib.*;

public class Pronic {

  static final long MAX = 100;
  static final String OUT_PATH = "/Users/zach/Documents/polyvdw/pronic_kthrees.csv";

  public static void main(String[] args)  throws Exception {

    // Initialize output vars
    HashMap<Long, ArrayList<long[]>> triples = new HashMap<Long, ArrayList<long[]>>(30000);
    ArrayList<long[]> kthrees = new ArrayList<long[]>();

    QuadGenerator params = new QuadGenerator(MAX);

    while(!params.isDone()) {
      long[] p = params.nextQuad();
      if(bad(p[0], p[1], p[2], p[3])) {
        continue;
      }
      long[] newTriple = triple(p);
      Long ind = new Long(newTriple[0]);

      if(!triples.containsKey(ind)) {
        ArrayList<long[]> tmp = new ArrayList<long[]>();
        tmp.add(newTriple);
        triples.put(ind, tmp);
      } else {
        for(long[] over : triples.get(ind)) {
          if(over[1] == newTriple[1]) {
            continue;
          }
          if(isPronic(sq(newTriple[1]) + newTriple[1] + sq(over[2]) + over[2])) {
            // Found a new K3
                                                                       // Truncate on purpose; its floor * ceil
            long[] newKthree = new long[] {newTriple[1], newTriple[2], (long)Math.sqrt(sq(newTriple[1]) + newTriple[1] + sq(over[2]) + over[2])};
            kthrees.add(newKthree);
            // And another one by symmetry
            newKthree = new long[] {over[1], over[2], (long)Math.sqrt(sq(newTriple[1]) + newTriple[1] + sq(over[2]) + over[2])};
            kthrees.add(newKthree);
            // Write out results
            write(kthrees, OUT_PATH);
          }
        }
        triples.get(ind).add(newTriple);
      }
    }
  }

  public static long[] triple(long[] params) {
    long x = (params[1]*params[2] + params[0]*params[3] - 1)/2;
    long y = (params[0]*params[2] - params[1]*params[3] - 1)/2;
    long z = (long)(-1/2 + Math.sqrt(sq(x)+x+sq(y)+y+1/4));
    if(x < y) {
      return(new long[] {x, y, z});
    } else {
      return(new long[] {y, x, z});
    }
  }

  private static boolean bad(long a, long b, long c, long d) {
    return((a*c <= b*d +1) || ((b*c+a*d)*(a*c-b*d) % 2 == 0));
  }

  public static boolean isPronic(long n) {
    return(Math.floor(Math.sqrt(n))*Math.ceil(Math.sqrt(n)) == n);
  }
}
