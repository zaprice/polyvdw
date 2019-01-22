
package polyvdw;

import java.util.ArrayList;
import static polyvdw.VdwLib.*;

public class TableLib {

  public static final String preamble = "\\documentclass{article}\n\n\\usepackage[margin=.7in]{geometry}\n\\usepackage{longtable}\n\n\\begin{document}\n\n"
  + "\\begin{longtable}{ | l | r | r | r | r | r | }\n\\hline\n\n"
  + "\\multicolumn{1}{|c|}{$g$} &\n\\multicolumn{1}{|c|}{$x$} &\n\\multicolumn{1}{|c|}{$y$} &\n\\multicolumn{1}{|c|}{$z$} &\n\\multicolumn{1}{|c|}{$w$} &\n\\multicolumn{1}{|c|}{$PW(4, \\{g\\}) <$} \\\\ \\hline\n\\endhead\n\n";

  public static final String preamble3 = "\\documentclass{article}\n\n\\usepackage[margin=.7in]{geometry}\n\\usepackage{longtable}\n\n\\begin{document}\n\n"
  + "\\begin{longtable}{ | l | r | r | r | }\n\\hline\n\n"
  + "\\multicolumn{1}{|c|}{$g$} &\n\\multicolumn{1}{|c|}{$x$} &\n\\multicolumn{1}{|c|}{$y$} &\n\\multicolumn{1}{|c|}{$PW(3, \\{g\\}) <$} \\\\ \\hline\n\\endhead\n\n";

  public static final String end = "\n\n\\end{longtable}\n\\end{document}";

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

  public static ArrayList<String> config3ToTable(DegreeTwoPoly poly, long[] bound) {
    ArrayList<String> output = new ArrayList<String>();
    String row = "$" + poly.toString() + "$ & " +
      commas(bound[1]) + " & " +
      commas(bound[2]) + " & " +
      commas(poly.val(poly.val(bound[1]) + poly.val(bound[2]))+1) + " \\\\ \\hline";

    output.add(row);
    return(output);
  }

}
