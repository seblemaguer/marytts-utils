import marytts.cart.io.*;
import marytts.cart.*;
import java.io.PrintWriter;
import java.io.IOException;

public class DumpMaryTree
{
    /**
     *  "Fake" leaf build for the visualisation purpose
     */
    public static void dump_leaf(int level, int max_level)
    {
        if ((level > max_level) && (max_level > 0))
            return;

        for (int i=0; i<level; i++)
            System.out.print("  ");
        System.out.println("- <LEAF>");

    }
    public static void dump(Node n, int level, int max_level)
    {
        if ((level > max_level) && (max_level > 0))
            return;

        try {
            DecisionNode dn = (DecisionNode) n;
            for (int i=0; i<level; i++)
                System.out.print("  ");
            System.out.println("- " + dn.getNodeDefinition().replaceAll("\"", "<QUOTES>"));

            for (int i=0; i<dn.getNumberOfDaugthers(); i++)
            {
                dump(dn.getDaughter(i), level+1, max_level);
            }

        } catch (Exception ex) {
            dump_leaf(level, max_level);
        }
    }

    public static void dump_dot_rec(Node n, int level, int max_level, PrintWriter dot_file_writer)
        throws IOException
    {
        if ((level > (max_level-1)) && (max_level > 0))
            return;

        DecisionNode dn = (DecisionNode) n;
        System.out.println("dn = " + dn.getNodeDefinition() + ", i = " + dn.getNumberOfDaugthers() + ", level = " + level + ", max_level = " + max_level);
        int i=0;
        int nb_nodes = 0;
        for (; i<dn.getNumberOfDaugthers(); i++)
        {

            try {
                DecisionNode daught = (DecisionNode) dn.getDaughter(i);
                dot_file_writer.println("\t\"(" + level + ") " +
                                        dn.getNodeDefinition().replaceAll("\"", "<QUOTES>") +
                                        "\" -> \"(" + (level+1) + ") " +
                                        daught.getNodeDefinition().replaceAll("\"", "<QUOTES>") + "\";");
                dump_dot_rec(daught, level+1, max_level, dot_file_writer);
                nb_nodes++;
            } catch (ClassCastException ex) {
            }
        }


        for (; nb_nodes<2; nb_nodes++)
        {

            dot_file_writer.println("\t\"(" + level + ") " +
                                    dn.getNodeDefinition().replaceAll("\"", "<QUOTES>") +
                                    "\" -> \"(" + (level+1) + ") " +
                                    dn.getNodeDefinition().replaceAll("\"", "<QUOTES>") + "_LEAF" + i + "\";");
        }
    }

    public static void dump_dot_rec(Node n, int level, PrintWriter dot_file_writer)
        throws IOException
    {
        dump_dot_rec(n, level, -1, dot_file_writer);
    }


    public static void dump_dot(Node root, String dot_filename, int max_level)
        throws IOException
    {
        PrintWriter dot_file_writer = new PrintWriter(dot_filename, "UTF-8");
        dot_file_writer.println("digraph dec_tree {");
        // Header
        dot_file_writer.println("\t node [shape=box, color=grey];");
        dot_file_writer.println("");

        // Content
        System.out.println("max_level = " + max_level);
        dump_dot_rec(root, 0, max_level, dot_file_writer);

        dot_file_writer.println("}");
        dot_file_writer.close();
    }

    public static void dump_dot(Node root, String dot_filename)
        throws IOException
    {
        dump_dot(root, dot_filename, -1);
    }
}
