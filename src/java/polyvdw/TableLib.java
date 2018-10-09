
package polyvdw;

import java.util.ArrayList;
import static polyvdw.VdwLib.*;

public class TableLib {

  public static final String preamble = "\\documentclass{article}\n\n\\usepackage[margin=.7in]{geometry}\n\n\\begin{document}\n\n\\begin{center}\\begin{tabular}{ | c | r | r | r | r | r | }\n\\hline\n\n"
  + "$g$ & $x$ & $y$ & $z$ & $w$ & $PW(4, \{g\}) <$ \\\\ \\hline";
  public static final String newPage = "\n\\end{tabular}\\pagebreak\n\n\\begin{tabular}{ | c | r | r | r | r | r | }\n\\hline\n\n"
  + "$g$ & $x$ & $y$ & $z$ & $w$ & $PW(4, \{g\}) <$ \\\\ \\hline";
  public static final String end = "\n\\hline\n\\end{tabular}\n\\end{center}\\end{document}";

  public static ArrayList<String> configToTable(DegreeTwoPoly poly, long[] kthreew) {
    ArrayList<String> output = new ArrayList<String>();
    if(kthreew == null || kthreew[3] == Long.MAX_VALUE) {
      output.add("\n");
      return(output);
    }

    String row = "$" + poly.toString() + "$ & " +
      commas(kthreew[0]) + " & " +
      commas(kthreew[1]) + " & " +
      commas(kthreew[2]) + " & " +
      commas(kthreew[3]) + " & " +
      commas(poly.val(kthreew[3])+1) + " \\\\ \\hline";

    output.add(row);
    return(output);


  }

}
