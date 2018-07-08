
package polyvdw;

import static polyvdw.VdwLib.*;

public class DegreeTwoPoly {

  long A;
  long B;

  public DegreeTwoPoly(long a, long b) {
    A = a;
    B = b;
  }

  public long val(long x) {
    return(A*sq(x)+B*x);
  }

  // Check if @y is p(x) for some integer x
  public boolean isNumber(long y) {
    double inv = (-B + Math.sqrt(sq(B)+4*A*y))/(2.0*A);
    return(Math.floor(inv) == Math.ceil(inv));
  }

  public long inv(long y) throws Exception {
    if(isNumber(y)) {
      return((long)((-B + Math.sqrt(sq(B)+4*A*y))/(2.0*A)));
    } else {
      throw new Exception();
    }
  }

  public String plugInValue(long x) {
    return(A + "*" + x + "^2 + " + B + "*" + x);
  }

  public String toString() {
    return(A + "x^2 + " + B + "x");
  }

  public long[] triple(long[] params) {
    long w1 = params[1]*params[2] + params[0]*params[3];
    long w2 = params[0]*params[2] - params[1]*params[3];
    long w3 = params[0]*params[2] + params[1]*params[3];
    long x = (long) ((w1 - B)/(2.0*A));
    long y = (long) ((w2 - B)/(2.0*A));
    long z = (long) ((w3 - B)/(2.0*A));
    if(x < y) {
      return(new long[] {x, y, z});
    } else {
      return(new long[] {y, x, z});
    }
  }

  public boolean bad(long[] params) {
    long w1 = params[1]*params[2] + params[0]*params[3];
    long w2 = params[0]*params[2] - params[1]*params[3];
    long w3 = params[0]*params[2] + params[1]*params[3];
    return((w2 == w3) || (w1 == w3) ||
           (params[1]*params[2] - params[0]*params[3] != B) ||
           (params[0]*params[2] <= params[1]*params[3] + B) ||
           ((w1-B) % (2*A) != 0) ||
           ((w2-B) % (2*A) != 0) ||
           ((w3-B) % (2*A) != 0));
  }
}
