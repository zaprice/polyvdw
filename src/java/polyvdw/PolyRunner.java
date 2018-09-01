
package polyvdw;

import java.util.HashMap;
import java.util.ArrayList;
import static polyvdw.VdwLib.*;
import static polyvdw.TikzLib.*;
import java.util.Arrays;

public class PolyRunner {

  static final long MAX = 200;
  static final long PARAM_MAX = 1000;
  static final long CHECK_MAX = 10000;
  static final String OUT_PATH = "/Users/zach/Documents/polyvdw/bounds.tex";
  static long[] min;
  static ArrayList<String> outputList;
  static int count;
  static boolean next;

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
        min = new long[] {0, 0, 0, Long.MAX_VALUE};
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
    ArrayList<long[]> kthrees = new ArrayList<long[]>();
    next = false;

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
            // TODO: this should be break?
            continue;
          }
          if(poly.isNumber(poly.val(newTriple[1]) + poly.val(over[2]))) {
            // Found a new K3
            kthrees = addNewKthree(kthrees, poly, newTriple, over);
            if(next) { break ;}
          }
        }
        triples.get(ind).add(newTriple);
      }
      if(next) {
        System.out.println(Arrays.toString(p));
        break;
      }
    }
    String output = poly.toString() + " : " + rowToString(min);
    System.out.println(output);
    if(!(min[3] == Long.MAX_VALUE)) {
      outputList.add("$ PW(4, " + poly.toString() + ") < " + poly.toBound(min[3]) + "$\n\\vspace{10pt}\n");
      outputList.addAll(configToTikz(poly, min));
      write(outputList, OUT_PATH);
      count++;
    }
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
        return;
      }
    }
  }

  public static void minCandidate(long[] kthreew) {
    if(kthreew[3] < min[3]) {
      min = kthreew;
      next = true;
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
