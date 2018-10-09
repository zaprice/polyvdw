
package polyvdw;

import java.util.HashMap;
import java.util.ArrayList;
import static polyvdw.VdwLib.*;
import static polyvdw.TableLib.*;
import java.util.Arrays;

public class PolyRunner {

  // As big as it gets without overflowing
  // TODO: switch to BigInteger?
  static final long MAX = 1577;
  static final long PARAM_MAX = 2000;
  static final long CHECK_MAX = 10000;
  static final String OUT_PATH = "/Users/zach/Documents/polyvdw/bounds.tex";
  static ArrayList<String> outputList;
  static int count;

  public static void main(String[] args)  throws Exception {
    count = 0;
    outputList = new ArrayList<String>();
    outputList.add(TableLib.preamble);
    // Init factors once to save time
    HashMap<Long, ArrayList<long[]>> factors = FactoringQuadGenerator.initFactors(MAX, PARAM_MAX);

    ArrayList<long[]> params = getParams(PARAM_MAX);
    for(long[] p : params) {
      minBound(p[0], p[1], MAX, CHECK_MAX, factors);
      if(count % 53 == 0 & count != 0) {
        outputList.add(TableLib.newPage);
      }
    }
    outputList.add(TableLib.end);
    write(outputList, OUT_PATH);
  }

  public static void minBound(long a, long b, long max, long check_max, HashMap<Long, ArrayList<long[]>> factors) throws Exception {
    // Initialize output vars
    HashMap<Long, ArrayList<long[]>> triples = new HashMap<Long, ArrayList<long[]>>(30000);
    long[] min = new long[] {0, 0, 0, Long.MAX_VALUE};

    FactoringQuadGenerator params = new FactoringQuadGenerator(b, max, factors);
    DegreeTwoPoly poly = new DegreeTwoPoly(a,b);

    while(!params.isDone()) {
      long[] p = params.nextQuad();
      if(poly.bad(p)) {
        continue;
      }
      long[] newTriple = poly.triple(p);
      Long ind = new Long(newTriple[0]);

      if(!triples.containsKey(ind)) {
        ArrayList<long[]> tmp = new ArrayList<long[]>();
        tmp.add(newTriple);
        triples.put(ind, tmp);
      } else {
        triples.get(ind).add(newTriple);
        for(long[] over : triples.get(ind)) {
          // Skip if this triple is already present
          if((over[1] == newTriple[1]) && (over[2] == newTriple[2])) {
            break;
          }
          if(poly.isNumber(poly.val(newTriple[1]) + poly.val(over[2]))) {
            // Found a new K3
            // If it is a bound, move on
            long[] bound = newKthree(poly, newTriple, over);
            if(bound != null && bound[3] < min[3]) {
              min = bound;
            }
          }
        }
      }
    }
    if(min[3] == Long.MAX_VALUE) {
      // No bound found
      finish(poly, null);
    } else {
      finish(poly, min);
    }
  }

  public static void finish(DegreeTwoPoly poly, long[] bound) throws Exception {
    String output = poly.toString() + " : " + rowToString(bound);
    System.out.println(output);
    if(bound != null) {
      outputList.addAll(configToTable(poly, bound));
      write(outputList, OUT_PATH);
      count++;
    }
    if(!doubleCheck(poly, bound)) {
      throw new Exception("configuration does not work");
    }
  }

  public static long[] newKthree(DegreeTwoPoly poly, long[] newTriple, long[] over) throws Exception {
    long[] newKthree = new long[] {newTriple[1], newTriple[2], poly.inv(poly.val(newTriple[1]) + poly.val(over[2]))};
    long[] bound = check(newKthree, poly);
    if(bound != null) {
      return(bound);
    }
    // And another one by symmetry
    newKthree = new long[] {over[1], over[2], poly.inv(poly.val(newTriple[1]) + poly.val(over[2]))};
    return(check(newKthree, poly));
  }

  // Add to the largest value (@kthree[2])
  // Then check if other differences are also solutions to @poly
  public static long[] check(long[] kthree, DegreeTwoPoly poly) {
    for(int i = 1; i < CHECK_MAX; i++) {
      long w = poly.val(kthree[2]) + poly.val(i);
      // Make sure w is not large enough to roll over
      if(poly.val(w) < 0) {
        return(null);
      }
      if(poly.isNumber(w-poly.val(kthree[0])) && poly.isNumber(w-poly.val(kthree[1]))) {
        // New bound configuration; compare to previous smallest
        return(new long[] {kthree[0], kthree[1], kthree[2], w});
      }
    }
    return(null);
  }

  public static boolean doubleCheck(DegreeTwoPoly poly, long[] kthreew) {
    // Skip check if no candidate was found
    if(kthreew == null) {
      return(true);
    }
    for(int i = 0; i < kthreew.length-1; i++) {
      for(int j = i+1; j < kthreew.length-1; j++) {
        if(!poly.isNumber(poly.val(kthreew[j]) - poly.val(kthreew[i]))) {
          System.out.println(poly.val(kthreew[j]) - poly.val(kthreew[i]));
          return(false);
        }
      }
      if(!poly.isNumber(kthreew[kthreew.length-1] - poly.val(kthreew[i]))) {
        return(false);
      }
    }
    return(true);
  }

}
