
package polyvdw;

import java.util.HashMap;
import java.util.ArrayList;
import static polyvdw.VdwLib.*;

public class PolyRunner {

  static final long MAX = 600;
  static final long PARAM_MAX = 10;
  static final long CHECK_MAX = 100000;
  static long[] min;

  public static void main(String[] args)  throws Exception {
    for(long a = 1; a <= PARAM_MAX; a++) {
      for(long b = 0; b <= PARAM_MAX; b++) {
        min = new long[] {0, 0, 0, Long.MAX_VALUE};
        minBound(a, b, MAX, CHECK_MAX);
      }
    }
  }

  public static void minBound(long a, long b, long max, long check_max) throws Exception {
    // Initialize output vars
    HashMap<Long, ArrayList<long[]>> triples = new HashMap<Long, ArrayList<long[]>>(30000);
    ArrayList<long[]> kthrees = new ArrayList<long[]>();

    QuadGenerator params = new QuadGenerator(max);
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
        for(long[] over : triples.get(ind)) {
          if(over[1] == newTriple[1]) {
            continue;
          }
          if(poly.isNumber(poly.val(newTriple[1]) + poly.val(over[2]))) {
            // Found a new K3
            kthrees = addNewKthree(kthrees, poly, newTriple, over);
          }
        }
        triples.get(ind).add(newTriple);
      }
    }
    System.out.println(poly.toString());
    System.out.println(rowToString(min));
    if(!doubleCheck(poly, min)) {
      throw new Exception("configuration does not work");
    }
  }

  public static ArrayList<long[]> addNewKthree(ArrayList<long[]> kthrees, DegreeTwoPoly poly, long[] newTriple, long[] over) throws Exception {
    long[] newKthree = new long[] {newTriple[1], newTriple[2], poly.inv(poly.val(newTriple[1]) + poly.val(over[2]))};
    kthrees.add(newKthree);
    check(newKthree, poly);
    // And another one by symmetry
    newKthree = new long[] {over[1], over[2], poly.inv(poly.val(newTriple[1]) + poly.val(over[2]))};
    kthrees.add(newKthree);
    check(newKthree, poly);
    return(kthrees);
  }

  // Add to the largest value (@kthree[2])
  // Then check if other differences are also solutions to @poly
  public static void check(long[] kthree, DegreeTwoPoly poly) {
    for(int i = 1; i < CHECK_MAX; i++) {
      long w = poly.val(kthree[2]) + poly.val(i);
      if(poly.isNumber(w-poly.val(kthree[0])) && poly.isNumber(w-poly.val(kthree[1]))) {
        // New bound configuration; compare to previous smallest
        minCandidate(new long[] {kthree[0], kthree[1], kthree[2], w});
        if(poly.isNumber(w)) {
          String printstr = rowToString(kthree) + ", " + w;
          //System.out.println(printstr);
          //System.out.println("^ is a K4");
        }
      }
    }
  }

  public static void minCandidate(long[] kthreew) {
    if(kthreew[3] < min[3]) {
      min = kthreew;
    }
  }

  public static boolean doubleCheck(DegreeTwoPoly poly, long[] kthreew) {
    // Skip check if no candidate was found
    if(kthreew[kthreew.length-1] == Long.MAX_VALUE) {
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
