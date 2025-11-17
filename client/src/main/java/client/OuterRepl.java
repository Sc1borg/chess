package client;

import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class OuterRepl {
    private final ServerFacade server;

    public OuterRepl(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to 240 chess. Type Help to get started");

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            Shared.printNew();
            String line = scanner.nextLine();

            try {
                String help = "This is a cry for help";
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
        if (params.length != 2) {
            return "Invalid login info";
        }
        LoginRequest user = new LoginRequest(params[0], params[1]);
        try {
            LoginResult loginResult = server.login(user);
            new MiddleRepl(server, loginResult).run();
        } catch (Exception ex) {
            return "Failed to login: " + ex.getMessage();
        }
        return "\r\n" + user.username() + " has been logged out";
    }

    public String register(String[] params) {
        if (params.length != 3) {
            return "Invalid registration info";
        }
        RegisterRequest user = new RegisterRequest(params[0], params[1], params[2]);

        try {
            LoginResult loginResult = server.register(user);
            new MiddleRepl(server, loginResult).run();
        } catch (Exception ex) {
            return "Failed to register: " + ex.getMessage();
        }
        return "\r\n" + user.username() + " has been logged out";
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
