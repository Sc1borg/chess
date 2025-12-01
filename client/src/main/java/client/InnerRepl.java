package client;

import chess.ChessBoard;
import model.GameData;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public class InnerRepl {
    private final GameData game;
    private final String persp;

    public InnerRepl(GameData game, String persp) {
        this.game = game;
        this.persp = persp;
    }

    public void run() {
        PrintBoard.printDaBoard(game.game().getBoard(), persp);
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            Shared.printNew();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                String help = "This is a cry for help";
                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + result);
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
                case "redraw" -> Shared.redraw(board, color);
                case "leave" -> "quit";
                case "move" -> move();
                case "resign" -> resign();
                case "highlight" -> Shared.highlight(params, );
                default -> help();
            };
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }

    private String help() {
        return """
                highlight <position> - highlights legal moves from the piece at that position
                redraw - redraws chess board
                leave - exit the game
                make move <start position> <end position> - move piece at start position
                resign - give up
                help - list possible commands
                """;
    }
}
