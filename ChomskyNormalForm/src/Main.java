import java.io.*;

public class Main {
    public static void main(String args[]) throws IOException {
        if (args.length != 2) {
            System.out.println("Need two txt files");
            throw new IOException();
        } else {

            // 1.convert to CNF
            Grammar grammar = new Grammar(args[0]);
            grammar.convert();
            // 2.CYK algorithm
            BufferedReader br = new BufferedReader(new FileReader(args[1]));
            try (FileWriter fw = new FileWriter("string_output.txt");
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {

                while (true) {

                    String input = br.readLine();
                    if (input==null)
                        break;
                    else {
                        boolean result = grammar.CYK(input);
                        if (result) {
                            out.print(input + ":Yes\n");

                        } else {
                            out.print(input + ":No\n");
                        }
                    }
                }
                br.close();
            } catch (IOException e) {
                // exception handling left as an exercise for the reader
            }
        }		
    }
}
