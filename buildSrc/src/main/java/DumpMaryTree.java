import marytts.cart.io.*;
import marytts.cart.*;
import java.io.PrintWriter;
import java.io.IOException;

public class DumpMaryTree
{

    public static void dump(Node n, int level, int max_level)
    {
        if ((level > max_level) && (max_level > 0))
            return;

        try {
            DecisionNode dn = (DecisionNode) n;
            for (int i=0; i<level; i++)
                System.out.print("  ");
            System.out.println("- " + dn.getFeatureName());

            for (int i=0; i<dn.getNumberOfDaugthers(); i++)
                dump(dn.getDaughter(i), level+1, max_level);
        } catch (Exception ex) {
        }
    }

    public static void dump_dot_rec(Node n, int level, PrintWriter dot_file_writer)
        throws IOException
    {
        try {
            DecisionNode dn = (DecisionNode) n;
            for (int i=0; i<dn.getNumberOfDaugthers(); i++)
            {
                DecisionNode daught = (DecisionNode) dn.getDaughter(i);
                dot_file_writer.println("\t\"(" + level + ") " + dn.getNodeDefinition() +
                                        "\" -> \"(" + (level+1) + ") " +
                                        daught.getNodeDefinition() + "\";");
                dump_dot_rec(daught, level+1, dot_file_writer);
            }
        } catch (ClassCastException ex) {
        }
    }


    public static void dump_dot(Node root, String dot_filename)
        throws IOException
    {
        PrintWriter dot_file_writer = new PrintWriter(dot_filename, "UTF-8");
        dot_file_writer.println("digraph dec_tree {");
        // Header
        dot_file_writer.println("\t node [shape=box, color=grey];");
        dot_file_writer.println("");

        // Content
        dump_dot_rec(root, 0, dot_file_writer);

        dot_file_writer.println("}");
        dot_file_writer.close();
    }
}
