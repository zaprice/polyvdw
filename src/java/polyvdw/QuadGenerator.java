
package polyvdw;

import java.util.HashMap;
import java.util.ArrayList;

public class QuadGenerator {

  long MAX;
  HashMap<Long, ArrayList<long[]>> factors;
  long a;
  long d;
  int index;
  ArrayList<long[]> currentFactors;


  public QuadGenerator(long MAX) {
    this.MAX = MAX;
    factors = initFactors(MAX);
    a = 0;
    d = 1;
    // Setup for the first loop iteration
    index = Integer.MAX_VALUE-1;
    currentFactors = new ArrayList<long[]>();
  }

  public long[] nextQuad() {
    // If there are more factorizations of the current ad+1, try them
    if(currentFactors.size() > index+1) {
      index++;
      long[] quad = new long[] {a, currentFactors.get(index)[0], currentFactors.get(index)[1], d};
      return(quad);
    } else {
      // Otherwise, move on!
      if(d == MAX) {
        d = 1;
        a++;
      } else {
        d++;
      }
      currentFactors = factors.get(a*d+1);
      index = 0;
      long[] quad = new long[] {a, currentFactors.get(index)[0], currentFactors.get(index)[1], d};
      return(quad);
    }
  }

  public boolean isDone() {
    return((d == MAX) && (a == MAX));
  }

  public HashMap<Long, ArrayList<long[]>> initFactors(long max) {
    HashMap<Long, ArrayList<long[]>> factors = new HashMap<Long, ArrayList<long[]>>((int)max*2);
    // max*max+1 because a,d range up to MAX and we want to pull ad+1 from the hash
    for(long i = 1; i <= max*max+1; i++) {
      for(long j = i; j <= max*max+1; j++) {
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
