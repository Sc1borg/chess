package client;

import model.GameData;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_YELLOW;

public class ObserverRepl {
    private final GameData game;

    public ObserverRepl(GameData game) {
        this.game = game;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            Shared.printNew();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_YELLOW + result);
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
                case "leave" -> "quit";
                case "redraw" -> Shared.redraw(game.game().getBoard(), "white");
                case "highlight" -> Shared.highlight(game.game().getBoard(), params);
                default -> help();
            };
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }

    private String help() {
        return """
                leave - exit the game
                redraw - redraws the board
                highlight <position> - highlights legal moves for piece at that position
                help - list possible commands
                """;
    }
}
