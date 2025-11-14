package client;

import model.LoginResult;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class middleRepl {

    private final ServerFacade server;
    private final LoginResult user;

    public middleRepl(ServerFacade server, LoginResult user) {
        this.server = server;
        this.user = user;
    }

    public void run() {
        System.out.println("Logged in as " + user.username());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            shared.printNew();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_GREEN + result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
    }

    private String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }

    private String create(String[] params) {
        if (params.length != 1) {
            return "Invalid game info";
        }
        try {
            server.create(params[0]);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "successfully created game";
    }

    private String list() {
        try {
            server.list();
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "";
    }

    private String join(String[] params) {
        if (params.length != 2) {
            return "Invalid arguments";
        }
        try {
            server.join(params[0], params[1]);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "successfully joined game";
    }

    private String observe(String[] params) {
        if (params.length != 1) {
            return "Invalid arguments";
        }
        return "";
    }

    private String logout() {
        try {
            server.logout();
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "quit";
    }

    private String help() {
        return """
                create <NAME> - a game
                list - games
                join <ID> - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess
                help - list possible commands
                """;
    }
}
