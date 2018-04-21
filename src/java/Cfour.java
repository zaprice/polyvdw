
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.io.IOException;

public class Cfour {

  static final long FAREY_MAX = 10000;
  static final long K_MAX = 100;
  static int count = 1;

  public static void main(String[] args) throws IOException {

    // Initialize output vars
    HashMap<Long, ArrayList<long[]>> triples = new HashMap<Long, ArrayList<long[]>>(30000);
    ArrayList<long[]> kfours = new ArrayList<long[]>();

    // Farey var setup
    long[] fareyArgs = new long[] {0,1,1,FAREY_MAX,0,0};

    // Farey loop
    while(fareyArgs[5] != 1) {
      // Generate next Farey pair
      fareyArgs = nextFarey(fareyArgs);
      long[] newFarey = new long[] {fareyArgs[4], fareyArgs[5]};

      // Skip rest if both are odd
      if(odd(newFarey)) {
        continue;
      }

      // Loop over K param for Euclid's formula
      for(long k = 1; k <= K_MAX; k++) {
        // Generate a new triple
        long[] newTriple = triple(k, newFarey[1], newFarey[0]);

        // Check against previous triples for K4s
        // You only need to check the first element for reasons
        Long ind = new Long(newTriple[0]);
        if(!triples.containsKey(ind)) {
          ArrayList<long[]> tmp = new ArrayList<long[]>();
          tmp.add(newTriple);
          triples.put(ind, tmp);
        } else {
          for(long[] over : triples.get(ind)) {
            if(isSquare(sq(newTriple[1]) + sq(over[2]))) {
              // Found a new K4
              long[] newKfour = {newTriple[1], newTriple[2], (long)Math.sqrt(sq(newTriple[1]) + sq(over[2]))};
              kfours.add(newKfour);
            }
          }
          triples.get(ind).add(newTriple);
        }
      }

      if((count % 100000) == 0) {
        write(kfours);
      }
      count++;
    }
    // Write out results
    write(kfours);
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

  public static long sq(long n) {
    return(n*n);
  }

  public static boolean isSquare(long n) {
    return(sq((long)Math.sqrt(n)) == n);
  }

  // Return true if all elements of ary are odd
  public static boolean odd(long[] ary) {
    for(long i : ary) {
      if((i % 2) == 0) {
        return(false);
      }
    }
    return(true);
  }

  public static void write(ArrayList<long[]> kfours) throws IOException {
    ArrayList<String> output = new ArrayList<String>(kfours.size());
    for(long[] kfour : kfours) {
      output.add(Arrays.toString(kfour));
    }
    Files.write(Paths.get("/Users/zach/Documents/polyvdw/kfours.csv"), output, Charset.forName("UTF-8"));
  }
}
