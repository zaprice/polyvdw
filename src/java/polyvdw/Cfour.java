
package polyvdw;

import java.util.HashMap;
import java.util.ArrayList;
import static polyvdw.VdwLib.*;

public class Cfour {

  static final long FAREY_MAX = 2500;
  static final long K_MAX = 100;
  static final String OUT_PATH = "/Users/zach/Documents/polyvdw/kthrees.csv";
  static int count = 1;

  public static void main(String[] args) throws Exception {

    // Initialize output vars
    HashMap<Long, ArrayList<long[]>> triples = new HashMap<Long, ArrayList<long[]>>(30000);
    ArrayList<long[]> kthrees = new ArrayList<long[]>();

    // Farey var setup
    FareyGenerator fareys = new FareyGenerator(FAREY_MAX);

    // Farey loop
    while(!fareys.isDone()) {
      // Generate next Farey pair
      long[] newFarey = fareys.nextFarey();

      // Skip rest if both are odd
      if(odd(newFarey)) {
        continue;
      }

      // Loop over K param for Euclid's formula
      for(long k = 1; k <= K_MAX; k++) {
        // Generate a new triple
        long[] newTriple = triple(k, newFarey[1], newFarey[0]);

        // Check against previous triples for K3s
        // You only need to check the first element for reasons
        Long ind = new Long(newTriple[0]);
        if(!triples.containsKey(ind)) {
          ArrayList<long[]> tmp = new ArrayList<long[]>();
          tmp.add(newTriple);
          triples.put(ind, tmp);
        } else {
          for(long[] over : triples.get(ind)) {
            if(isSquare(sq(newTriple[1]) + sq(over[2]))) {
              // Found a new K3
              long[] newKthree = new long[] {newTriple[1], newTriple[2], (long)Math.sqrt(sq(newTriple[1]) + sq(over[2]))};
              kthrees.add(newKthree);
              // And another one by symmetry
              newKthree = new long[] {over[1], over[2], (long)Math.sqrt(sq(newTriple[1]) + sq(over[2]))};
              kthrees.add(newKthree);
            }
          }
          triples.get(ind).add(newTriple);
        }
      }

      if((count % 100000) == 0) {
        writeKthrees(kthrees, OUT_PATH);
      }
      count++;
    }
    // Write out results
    writeKthrees(kthrees, OUT_PATH);
  }

  /////////////////////////////////
  // Functions
  /////////////////////////////////

  // Generate the next term in the Farey sequence
  public static long[] nextFarey(long[] fareyArgs) {
    fareyArgs[4] = (long)((fareyArgs[1]+FAREY_MAX)/fareyArgs[3])*fareyArgs[2] - fareyArgs[0];
    fareyArgs[5] = (long)((fareyArgs[1]+FAREY_MAX)/fareyArgs[3])*fareyArgs[3] - fareyArgs[1];
    fareyArgs[0] = fareyArgs[2];
    fareyArgs[1] = fareyArgs[3];
    fareyArgs[2] = fareyArgs[4];
    fareyArgs[3] = fareyArgs[5];

    return(fareyArgs);
  }

  // Generates a pythagorean triple; m n are coprime, m > n, not both odd
  // In sorted order
  public static long[] triple(long k, long m, long n) {
    if(k*2*m*n < k*(sq(m)-sq(n))) {
      return(new long[] {k*2*m*n, k*(sq(m)-sq(n)), k*(sq(m)+sq(n))});
    } else {
      return(new long[] {k*(sq(m)-sq(n)), k*2*m*n, k*(sq(m)+sq(n))});
    }
  }

}
