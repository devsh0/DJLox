package jlox;

public class Lox {
    private static void run_script (String script)
    {
        System.out.println("Running script now...");
    }

    public static void main (String[] args)
    {
        if (args.length < 1) {
            System.out.println("Usage lox <script>");
            System.exit(64);
        }
        run_script(args[0]);
    }
}