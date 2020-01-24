
package polyvdw;

import java.util.HashMap;
import java.util.ArrayList;
import static polyvdw.VdwLib.*;
import static polyvdw.TableLib.*;
import java.util.Arrays;

public class ThreeColorRunner {

  static final long MAX = 1000;
  static final long PARAM_MAX = 50;
  static final String OUT_PATH = "/Users/zach/Documents/polyvdw/3bounds.tex";
  static ArrayList<String> outputList;
  static int count;

  public static void main(String[] args)  throws Exception {
    outputList = new ArrayList<String>();
    outputList.add(TableLib.preamble3);
    ArrayList<long[]> params = getParams(PARAM_MAX);

    for(long[] p : params) {
      minBound(p[0], p[1], MAX);
    }
    outputList.add(TableLib.end);
    write(outputList, OUT_PATH);
  }

  public static void minBound(long a, long b, long max) throws Exception {

    QuadGenerator params = new QuadGenerator(max);
    DegreeTwoPoly poly = new DegreeTwoPoly(a,b);

    while(!params.isDone()) {
      long[] p = params.nextQuad();
      if(poly.bad(p)) {
        continue;
      }
      // First triple gives a bound
      long[] newTriple = poly.triple(p);
      finish(poly, newTriple);
      return;
    }
    // No bound found
    System.out.println("no bound");
    finish(poly, null);
  }

  public static void finish(DegreeTwoPoly poly, long[] bound) throws Exception {
    String output = poly.toString() + " : " + rowToString(bound);
    System.out.println(output);
    if(bound != null) {
      long boundValue = poly.val(bound[1]) + poly.val(bound[2]);
      outputList.addAll(config3ToTable(poly, bound));
      write(outputList, OUT_PATH);
    }
    if(!doubleCheck(poly, bound)) {
      throw new Exception("configuration does not work");
    }
  }

  public static boolean doubleCheck(DegreeTwoPoly poly, long[] bound) {
    // Skip check if no candidate was found
    if(bound == null) {
      return(true);
    }
    return(poly.val(bound[2])-poly.val(bound[1]) == poly.val(bound[0]));
  }

}
