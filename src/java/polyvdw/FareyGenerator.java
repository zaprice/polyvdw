
package polyvdw;

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

    return(new long[] {fareyArgs[4], fareyArgs[5]});
  }

  public boolean isDone() {
    return(fareyArgs[5] == 1);
  }
}
