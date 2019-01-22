
package polyvdw;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.io.IOException;
import java.lang.Long;

public class VdwLib {

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

  public static boolean isPronic(long n) {
    return(Math.floor(Math.sqrt(n))*Math.ceil(Math.sqrt(n)) == n);
  }

  public static String rowToString(long[] kthree) {
    String str = Arrays.toString(kthree);
    return(str.substring(1,str.length()-1));
  }

  public static void write(Iterable<? extends CharSequence> output, String filename) throws IOException {
    Files.write(Paths.get(filename), output, Charset.forName("UTF-8"));
  }

  public static void writeKthrees(ArrayList<long[]> kthrees, String filename) throws IOException {
    ArrayList<String> output = new ArrayList<String>(kthrees.size());
    for(long[] kthree : kthrees) {
      output.add(rowToString(kthree));
    }
    write(output, filename);
  }

  public static ArrayList<long[]> read(String filename) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get(filename));
    ArrayList<long[]> output = new ArrayList<long[]>(lines.size());

    for(String line : lines) {
      String[] parts = line.split(", ");
      long[] kthree = new long[] {Long.parseLong(parts[0]), Long.parseLong(parts[1]), Long.parseLong(parts[2])};
      output.add(kthree);
    }
    return(output);
  }

  public static String commas(long input) {
    return(String.format("%,d", input).replaceAll(",", "{,}"));
  }

  public static ArrayList<long[]> getParams(long max) {
    // Using the Farey sequence so we only compute bounds for Ax^2+Bx that are not multiples of previous bounds
    FareyGenerator paramsGen = new FareyGenerator(max);
    ArrayList<long[]> params = new ArrayList<long[]>((int)max);
    while(!paramsGen.isDone()) {
      long[] t = paramsGen.nextFarey();
      params.add(t);
      // Add swapped version too, to account for coprime (A,B) with B < A
      // This will add duplicates
      params.add(swap(t));
    }
    // Need to deduplicate
    Set<long[]> set = new HashSet<long[]>(params);
    params.clear();
    params.addAll(set);

    // Then add x^2 and sort
    params.add(new long[] {1, 0});
    params.sort(new LexicalComparator());
    return(params);
  }

  public static long[] swap(long[] input) {
    return(new long[] {input[1], input[0]});
  }

}
