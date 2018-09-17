
package polyvdw;

import java.util.HashMap;
import java.util.ArrayList;
import static polyvdw.VdwLib.*;
import static polyvdw.TikzLib.*;
import java.util.Arrays;

public class PolyRunner {

  static final long MAX = 1000;
  static final long PARAM_MAX = 10000;
  static final long CHECK_MAX = 10000;
  static final String OUT_PATH = "/Users/zach/Documents/polyvdw/bounds.tex";
  static ArrayList<String> outputList;
  static int count;

  public static void main(String[] args)  throws Exception {
    count = 0;
    outputList = new ArrayList<String>();
    outputList.add(TikzLib.preamble);
    for(long a = 1; a <= PARAM_MAX; a++) {
      for(long b = 0; b <= PARAM_MAX; b++) {
        // Skip iterations if they are a multiple of (1, n)
        // TODO: skip if they are multiples of any previous iteration
        // TODO: algorithm does not find bounds for (a,b) with a > 1, a does not divide b
        if(b % a == 0 && a != 1) {
          continue;
        }
        minBound(a, b, MAX, CHECK_MAX);
        if(count % 2 == 0 & count != 0) {
          outputList.add("\\pagebreak");
        }
      }
    }
    outputList.add(TikzLib.end);
    write(outputList, OUT_PATH);
  }

  public static void minBound(long a, long b, long max, long check_max) throws Exception {
    // Initialize output vars
    HashMap<Long, ArrayList<long[]>> triples = new HashMap<Long, ArrayList<long[]>>(30000);

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
          // Skip if this triple is already present
          if((over[1] == newTriple[1]) && (over[2] == newTriple[2])) {
            break;
          }
          if(poly.isNumber(poly.val(newTriple[1]) + poly.val(over[2]))) {
            // Found a new K3
            // If it is a bound, move on
            // TODO: the first bound found may not be the best one
            long[] bound = newKthree(poly, newTriple, over);
            if(bound != null) {
              finish(poly, bound);
              return;
            }
          }
        }
        triples.get(ind).add(newTriple);
      }
    }
    // No bound found
    finish(poly, null);
  }

  public static void finish(DegreeTwoPoly poly, long[] bound) throws Exception {
    String output = poly.toString() + " : " + rowToString(bound);
    System.out.println(output);
    if(bound != null) {
      outputList.add("$ PW(4, " + poly.toString() + ") < " + poly.toBound(bound[3]) + "$\n\\vspace{10pt}\n");
      outputList.addAll(configToTikz(poly, bound));
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
