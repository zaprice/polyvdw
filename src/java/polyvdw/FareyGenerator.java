
package polyvdw;

import java.util.Comparator;

public class FareyGenerator {

  long[] fareyArgs;
  long FAREY_MAX;

  public FareyGenerator(long FAREY_MAX) {
    fareyArgs = new long[] {0,1,1,FAREY_MAX,0,0};
    this.FAREY_MAX = FAREY_MAX;
  }

  public long[] nextFarey() {
    fareyArgs[4] = (long)((fareyArgs[1]+FAREY_MAX)/fareyArgs[3])*fareyArgs[2] - fareyArgs[0];
    fareyArgs[5] = (long)((fareyArgs[1]+FAREY_MAX)/fareyArgs[3])*fareyArgs[3] - fareyArgs[1];
    fareyArgs[0] = fareyArgs[2];
    fareyArgs[1] = fareyArgs[3];
    fareyArgs[2] = fareyArgs[4];
    fareyArgs[3] = fareyArgs[5];
    return(new long[] {fareyArgs[0], fareyArgs[1]});
  }

  public boolean isDone() {
    return(fareyArgs[5] == 1);
  }
}

// Lexical sort comparator for FareyGenerator outputs (pairs)
class LexicalComparator implements Comparator<long[]> {

  @Override
  public int compare(long[] one, long[] two) {
    if(one[0] < two[0]) {
      return(-1);
    } else if(one[0] > two[0]) {
      return(1);
    } else {
      if(one[1] < two[1]) {
        return(-1);
      } else if(one[1] > two[1]) {
        return(1);
      } else {
        return(0);
      }
    }
  }

}
