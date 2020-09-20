package jlox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

enum TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // One or two character tokens.
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

    EOF
}

public class Lox {
    private static boolean s_had_error = false;

    private static void report (int line, String where, String message)
    {
        System.err.println(String.format("%d:%s: %s", line, where, message));
    }

    static void error (int line, String message)
    {
        report(line, "", message);
        s_had_error = true;
    }

    private static void run (String source)
    {
        LoxScanner scanner = new LoxScanner(source);
        scanner.scan().forEach(System.out::println);
    }

    private static void run_script (String script)
    {
        try {
            run(Files.readString(Paths.get(script)));
            if (s_had_error) System.exit(65);
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    private static void run_prompt ()
    {
        var scanner = new Scanner(System.in);
        String line;
        do {
            System.out.print("> ");
            line = scanner.nextLine();
            run(line);
            s_had_error = false;
        } while (line != null);
    }

    public static void main (String... args)
    {
        if (args.length < 1) {
            System.out.println("Usage lox <script>");
            System.exit(64);
        }

        run_script(args[0]);
    }
}