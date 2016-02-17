package yurius.game.model;


public class TurnResult {
    private final TurnStatus status;
    private final String extraMessage;

    public TurnResult(TurnStatus status, String message) {
        this.status = status;
        this.extraMessage = message;
    }

    public TurnResult(TurnStatus turnStatus) {
        this(turnStatus, null);
    }

    public TurnStatus getStatus() {
        return status;
    }

    public String getMessage() {

        StringBuilder prefix = new StringBuilder();
        switch (status) {
            case GAME_OVER:
                prefix.append("Game over");
                break;
            case NEXT_FIRST_PLAYER:
                prefix.append("First player's turn");
                break;
            case NEXT_SECOND_PLAYER:
                prefix.append("Second player's turn");
                break;
            default:
                prefix.append(status.name());
        }

        if (extraMessage != null)
            prefix.append(": ").append(extraMessage);
        else
            prefix.append(".");
        return prefix.toString();
    }

}
