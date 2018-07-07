
package polyvdw;

public class QuadGenerator {

  long MAX;
  long a;
  long b;
  long c;
  long d;


  public QuadGenerator(long MAX) {
    this.MAX = MAX;
    // First working parameter set
    a = 0;
    b = 0;
    c = 0;
    d = 0;
  }

  public long[] nextQuad() {
    if(a == MAX) {
      a = 1;
      if(b == MAX) {
        b = 1;
        if(c == MAX) {
          c = 1;
          d++;
        } else {
          c++;
        }
      } else {
        b++;
      }
    } else {
      a++;
    }

    return(new long[] {a,b,c,d});
  }

  public boolean isDone() {
    return(d == MAX);
  }

}
