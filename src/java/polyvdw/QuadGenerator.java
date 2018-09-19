
package polyvdw;

public class QuadGenerator {

  long MAX;
  long currentMax;
  long a;
  long b;
  long c;
  long d;


  public QuadGenerator(long MAX) {
    this.MAX = MAX;
    currentMax = 1;
    // First working parameter set
    a = 0;
    b = 0;
    c = 0;
    d = 0;
  }

  public long[] nextQuad() {
    // Add one to (a,b,c,d), the usual way
    if(d == currentMax) {
      d = 1;
      if(c == currentMax) {
        c = 1;
        if(b == currentMax) {
          b = 1;
          if(a == currentMax) {
            currentMax++;
            a = 1;
          } else {
            a++;
          }
        } else {
          b++;
        }
      } else {
        c++;
      }
    } else {
      d++;
    }

    // If no digit is currentmax, set lowest order digit to currentmax
    if((a != currentMax) && (b != currentMax) && (c != currentMax) && (d != currentMax)) {
      d = currentMax;
    }

    return(new long[] {a,b,c,d});
  }

  public boolean isDone() {
    return((d == MAX) && (c == MAX) && (b == MAX) && (a == MAX));
  }

}
