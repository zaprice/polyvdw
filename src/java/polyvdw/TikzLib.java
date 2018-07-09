
package polyvdw;

import java.util.ArrayList;

public class TikzLib {

  public static final String preamble = "\\documentclass{article}\n\n\\usepackage[margin=.7in]{geometry}\n\n\\usepackage{tikz}\n\n\\begin{document}\n\n\\tikzstyle{vertex} = [circle,draw,fill=white!20]\n\n";
  public static final String end = "\\end{document}";

  public static ArrayList<String> configToTikz(DegreeTwoPoly poly, long[] kthreew) throws Exception {
    ArrayList<String> output = new ArrayList<String>();
    if(kthreew[3] == Long.MAX_VALUE) {
      output.add("\n");
      return(output);
    }

    // Begin graph, add 0 node
    output.add("\\begin{tikzpicture}\n[scale=.8,auto=left]\n\n\\node [vertex] (n1) at (1,7) {0};\n");

    // Add nodes for other numbers
    output.add("\\node [vertex] (n2) at (7,13) {" + poly.val(kthreew[0]) + "};\n");
    output.add("\\node [vertex] (n3) at (7,1) {" + poly.val(kthreew[1]) + "};\n");
    output.add("\\node [vertex] (n4) at (13,7) {" + poly.val(kthreew[2]) + "};\n");
    output.add("\\node [vertex] (n5) at (21,7) {" + kthreew[3] + "};\n");

    for(int i = 0; i < kthreew.length-1; i++) {
      for(int j = i+1; j < kthreew.length-1; j++) {
        if(i == 0 && j == 1) {
          String text = poly.plugInValue(poly.inv(poly.val(kthreew[j]) - poly.val(kthreew[i])));
          output.add("\\draw (n" + (i+2) + ") -- node [pos=0.35] {$" + text + "$}(n" + (j+2) + ");\n");
        } else {
          String text = poly.plugInValue(poly.inv(poly.val(kthreew[j]) - poly.val(kthreew[i])));
          output.add("\\draw [{sloped,anchor=south,auto=false}] (n" + (i+2) + ") -- node {$" + text + "$}(n" + (j+2) + ");\n");
        }
      }
      // Add edge from 0 to kthreew[i]
      if(i == 0 || i == 1) {
        output.add("\\draw [{sloped,anchor=south,auto=false}] (n1) -- node {$" + poly.plugInValue(kthreew[i]) + "$}(n" + (i+2) + ");\n");
      } else {
        output.add("\\draw [{sloped,anchor=south,auto=false}] (n1) -- node [near start] {$" + poly.plugInValue(kthreew[i]) + "$}(n" + (i+2) + ");\n");
      }

      // Add edge from kthreew[i] to kthreew[length-1]
      if(i == 2) {
        output.add("\\draw [{sloped,anchor=south,auto=false}] (n" + (i+2) + ") -- node {$" + poly.plugInValue(poly.inv(kthreew[kthreew.length-1] - poly.val(kthreew[i]))) + "$}(n5);\n");
      } else {
        output.add("\\draw [{sloped,anchor=south,auto=false}] (n" + (i+2) + ") -- node [near end]{$" + poly.plugInValue(poly.inv(kthreew[kthreew.length-1] - poly.val(kthreew[i]))) + "$}(n5);\n");
      }
    }

    output.add("\\end{tikzpicture}\n\\vspace{10pt}\n");
    return(output);
  }

}
