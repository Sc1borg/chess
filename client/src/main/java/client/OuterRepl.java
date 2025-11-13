package client;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.SET_BG_COLOR_DARK_GREEN;
import static ui.EscapeSequences.SET_BG_COLOR_GREEN;

public class OuterRepl {

    public OuterRepl(String serverUrl) {
    }

    public void run() {
        System.out.println("Welcome to 240 chess. Type Help to get started");

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            printNew();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_BG_COLOR_DARK_GREEN + result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
    }

    private void printNew() {
        System.out.print("\n" + ">>>" + SET_BG_COLOR_GREEN);
    }

    private String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (cmd) {
                case "login" -> login(params);
                case "quit" -> "quit";
                case "register" -> register(params);
                default -> help();
            };
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }

    public String login(String[] params) {
        return "";
    }

    public String register(String[] params) {
        server.register(params[0], params[1], params[2]);
        return "";
    }

    public String help() {
        return """
                - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                - login <USERNAME> <PASSWORD> - to play chess
                - quit - to exit
                - help - see possible commands
                """;
    }
}
