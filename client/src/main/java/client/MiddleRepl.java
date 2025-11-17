package client;

import com.google.gson.Gson;
import model.CreateGameRequest;
import model.GameData;
import model.JoinGameRequest;
import model.LoginResult;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class MiddleRepl {

    private final ServerFacade server;
    private final LoginResult user;

    public MiddleRepl(ServerFacade server, LoginResult user) {
        this.server = server;
        this.user = user;
    }

    public void run() {
        System.out.println("Logged in as " + user.username());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            Shared.printNew();
            String line = scanner.nextLine();
            String ok = "Am I ok?";

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
                case "quit" -> quit();
                default -> help();
            };
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }

    private String quit() {
        System.exit(0);
        return "This should never be output";
    }

    private String create(String[] params) {
        if (params.length != 1) {
            return "Invalid game info";
        }
        try {
            CreateGameRequest createGameRequest = new CreateGameRequest(params[0]);
            server.create(createGameRequest, user);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "successfully created game";
    }

    private String list() {
        Map<String, ArrayList> myMap;
        ArrayList games;
        StringBuilder gameList = new StringBuilder();
        try {
            myMap = server.list(user);
            games = myMap.get("games");
        } catch (Exception ex) {
            return ex.getMessage();
        }
        int counter = 0;
        for (var game : games) {
            var realGame = new Gson().fromJson(game.toString(), GameData.class);
            counter++;
            gameList.append(" ").append(counter).append(" Name: ").append(realGame.gameName()).append(" White: ")
                    .append(realGame.whiteUsername()).append(" Black: ")
                    .append(realGame.blackUsername()).append("\n");
        }
        return gameList.toString();
    }

    private String join(String[] params) {
        if (params.length != 2) {
            return "Invalid arguments";
        }
        try {
            Integer.parseInt(params[0]);
        } catch (Exception e) {
            return "Input must be an integer";
        }
        try {
            GameData game = getGame(params[0]);
            int id = game.gameID();
            JoinGameRequest joinReq = new JoinGameRequest(params[1].toUpperCase(), id);
            server.join(joinReq, user);
            PrintBoard.printDaBoard(game.game().getBoard(), params[1]);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "";
    }

    private GameData getGame(String num) throws Exception {
        Map<String, ArrayList> gamesMap = server.list(user);
        ArrayList games = gamesMap.get("games");
        if (Integer.parseInt(num) - 1 >= games.size() || Integer.parseInt(num) - 1 < 0) {
            throw new Exception(num + " is not a valid game");
        }
        return new Gson().fromJson(games.get(Integer.parseInt(num) - 1).toString(), GameData.class);
    }

    private String observe(String[] params) {
        if (params.length != 1) {
            return "Invalid arguments";
        }
        try {
            Integer.parseInt(params[0]);
        } catch (Exception e) {
            return "Input must be an integer";
        }
        try {
            GameData game = getGame(params[0]);
            PrintBoard.printDaBoard(game.game().getBoard(), "white");
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }

    private String logout() {
        try {
            server.logout(user);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "quit";
    }

    private String help() {
        return """
                create <NAME> - a game
                list - games
                join <ID> [white]|[black] - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess
                help - list possible commands
                """;
    }
}
