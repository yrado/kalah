package yurius.game.model;

public enum Player {
    FIRST(0, "First"),
    SECOND(1, "Second");

    private int index;
    private final String text;

    Player(int index, String text) {
        this.index = index;
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public String getText() {
        return text;
    }

    public Player getOther() {
        return this == FIRST ? SECOND : FIRST;
    }


}
