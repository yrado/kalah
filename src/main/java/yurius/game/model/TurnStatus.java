package yurius.game.model;

import yurius.game.service.rest.dto.Status;

public enum TurnStatus {
    NEXT_FIRST_PLAYER(Status.FIRST_PLAYER_TURN),
    NEXT_SECOND_PLAYER(Status.SECOND_PLAYER_TURN),
    GAME_OVER(Status.GAME_OVER);

    private final Status status;

    TurnStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public static TurnStatus get(Player player) {
        return player == Player.FIRST ? NEXT_FIRST_PLAYER : NEXT_SECOND_PLAYER;
    }
}
