
package polyvdw;

import java.util.HashMap;
import java.util.ArrayList;
import static polyvdw.VdwLib.*;

public class PolyRunner {

  static final long MAX = 600;
  static final long CHECK_MAX = 100000;

  public static void main(String[] args)  throws Exception {

    // Initialize output vars
    HashMap<Long, ArrayList<long[]>> triples = new HashMap<Long, ArrayList<long[]>>(30000);
    ArrayList<long[]> kthrees = new ArrayList<long[]>();

    QuadGenerator params = new QuadGenerator(MAX);
     // TODO: Automate parameter selection
    DegreeTwoPoly poly = new DegreeTwoPoly(1,2);

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

  // Add @pronic to the largest value (@kthree[2])
  // Then check if other differences are also pronic
  public static void check(long[] kthree, DegreeTwoPoly poly) {
    for(int i = 1; i < CHECK_MAX; i++) {
      long w = poly.val(kthree[2]) + poly.val(i);
      if(poly.isNumber(w-poly.val(kthree[0])) && poly.isNumber(w-poly.val(kthree[1]))) {
        String printstr = rowToString(kthree) + ", " + w;
        System.out.println(printstr);
        if(poly.isNumber(w)) {
          System.out.println("^ is a K4");
        }
      }
    }
  }
}
