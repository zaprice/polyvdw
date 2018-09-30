
package polyvdw;

import java.util.HashMap;
import java.util.ArrayList;

public class FactoringQuadGenerator {

  long MAX;
  long currentMax;
  HashMap<Long, ArrayList<long[]>> factors;
  long a;
  long d;
  int index;
  ArrayList<long[]> currentFactors;
  long B;


  public FactoringQuadGenerator(long B, long MAX, HashMap<Long, ArrayList<long[]>> factors) {
    this.MAX = MAX;
    currentMax = 1;
    this.factors = factors;
    // Param from the polynomial
    // Determine what number we're factoring
    this.B = B;
    // To deal with the case where we try to factorize 0*1 + 0
    a = (B == 0) ? 1 : 0;
    d = 1;
    // Setup for the first loop iteration
    index = Integer.MAX_VALUE-1;
    currentFactors = new ArrayList<long[]>();
  }

  public long[] nextQuad() {
    // If there are more factorizations of the current ad+B, try them
    if(currentFactors.size() > index+1) {
      index++;
      long[] quad = new long[] {a, currentFactors.get(index)[0], currentFactors.get(index)[1], d};
      return(quad);
    } else {
      // Otherwise, move on!
      nextPair();
      currentFactors = factors.get(a*d+B);
      index = 0;
      long[] quad = new long[] {a, currentFactors.get(index)[0], currentFactors.get(index)[1], d};
      return(quad);
    }
  }

  public boolean isDone() {
    return((d == MAX) && (a == MAX));
  }

  public void nextPair() {
    if(d == currentMax) {
      d = 1;
      if(a == currentMax) {
        currentMax++;
        a = 1;
        d = currentMax;
      } else {
        a++;
      }
    } else {
      d++;
    }

    // If no digit is currentmax, set lowest order digit to currentmax
    if((a != currentMax) && (d != currentMax)) {
      d = currentMax;
    }
  }

  public static HashMap<Long, ArrayList<long[]>> initFactors(long max, long maxB) {
    HashMap<Long, ArrayList<long[]>> factors = new HashMap<Long, ArrayList<long[]>>((int)max*2);
    // max*max+B because a,d range up to MAX and we want to pull ad+B from the hash
    for(long i = 1; i <= max*max+maxB; i++) {
      for(long j = i; j <= ((max*max+maxB)/i)+1; j++) {
        if(!factors.containsKey(i*j)) {
          ArrayList<long[]> tmp = new ArrayList<long[]>();
          tmp.add(new long[] {i, j});
          factors.put(i*j, tmp);
        } else {
          factors.get(i*j).add(new long[] {i, j});
        }
      }
    }
    return(factors);
  }

}
