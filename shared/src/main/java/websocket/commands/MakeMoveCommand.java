package websocket.commands;

public class MakeMoveCommand extends UserGameCommand {
    String move;

    public MakeMoveCommand(String authToken, int gameID, String move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }

    public String getMove() {
        return move;
    }
}
