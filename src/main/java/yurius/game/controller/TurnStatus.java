package yurius.game.controller;

import yurius.game.model.Player;
import yurius.game.model.Status;

public enum TurnStatus {
    FIRST_PLAYER_TURN(Status.FIRST_PLAYER_TURN),
    SECOND_PLAYER_TURN(Status.SECOND_PLAYER_TURN),
    GAME_OVER(Status.GAME_OVER);

    private final Status status;

    TurnStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public static TurnStatus get(Player player) {
        return player == Player.FIRST ? FIRST_PLAYER_TURN : SECOND_PLAYER_TURN;
    }
}
