package yurius.game.controller;

import yurius.game.controller.exceptions.EmptyCellException;
import yurius.game.model.BoardState;
import yurius.game.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Board {

    private int[] cells;

    public Board(int[] initialPits) {
        if (initialPits.length != Constants.TOTAL_CELLS_COUNT)
            throw new IllegalArgumentException(String.format(
                    "Board has to be populated with %d values (%d per player, %d house values and one store value). Got %d value(s)",
                    Constants.TOTAL_CELLS_COUNT,
                    Constants.PLAYER_CELLS_COUNT,
                    Constants.HOUSE_CELLS_COUNT,
                    initialPits.length));

        cells = Arrays.copyOf(initialPits, initialPits.length);
    }

    public static Board create(BoardState boardState) {
        checkPlayer(boardState.getFirstPlayer(), Player.FIRST);
        checkPlayer(boardState.getSecondPlayer(), Player.SECOND);

        int[] array = new int[Constants.TOTAL_CELLS_COUNT];
        for (int i = 0; i < Constants.PLAYER_CELLS_COUNT; i++)
            array[i] = boardState.getFirstPlayer().get(i);
        for (int i = 0; i < Constants.PLAYER_CELLS_COUNT; i++)
            array[i + Constants.PLAYER_CELLS_COUNT] = boardState.getSecondPlayer().get(i);

        return new Board(array);
    }

    private static void checkPlayer(List<Integer> playerCells, Player player) {
        if (playerCells.size() != Constants.PLAYER_CELLS_COUNT)
            throw new RuntimeException(String.format(
                    "Player %s has %d elements on the boards instead of %d",
                    player,
                    playerCells.size(),
                    Constants.PLAYER_CELLS_COUNT));
    }

    public BoardState asBoardState() {
        return new BoardState(getPlayerPits(Player.FIRST), getPlayerPits(Player.SECOND));
    }

    /**
     * This method moves stones from specified house of the player to the other pits on the board according to the
     * rules of the game.
     *
     * @param player      Player that takes turn
     * @param houseNumber house number that move starts from
     * @return true if last stone was placed in the player's house, otherwise - false.
     */
    public boolean moveStones(final Player player, final int houseNumber) throws EmptyCellException {
        if (houseNumber < 1 || houseNumber > Constants.HOUSE_CELLS_COUNT)
            throw new IllegalArgumentException(
                    String.format(
                            "House number must be between %d and %d. Got: %s.",
                            1,
                            Constants.HOUSE_CELLS_COUNT, houseNumber));

        int startCellIndex = player.getIndex() * Constants.PLAYER_CELLS_COUNT + houseNumber - 1;
        int numStones = cells[startCellIndex];

        if (numStones == 0)
            throw new EmptyCellException("Cannot move stones from empty cell");

        cells[startCellIndex] = 0;
        for (int i = 1; i <= numStones; i++) {
            incrementCell(getCellIndex(startCellIndex, i, player));
        }

        int lastCellIndex = getCellIndex(startCellIndex, numStones, player);
        if (finishedInEmptyCell(lastCellIndex) && isPlayerHouse(lastCellIndex, player))
            moveStonesFromThisCellAndOpponentOppositeCellToStore(lastCellIndex, player);

        if (isGameOver())
            moveHousesToStores();

        return isPlayerStore(player, lastCellIndex);
    }

    private void incrementCell(int cellIndex) {
        cells[cellIndex] = cells[cellIndex] + 1;
    }

    private int getCellIndex(int startCellIndex, int numStones, Player player) {
        int storeShift = 0;
        for (int i = 1; i <= numStones; i++) {
            if (isPlayerStore(player.getOther(), (startCellIndex + i + storeShift) % cells.length)) {
                storeShift++;
            }
        }
        return (startCellIndex + numStones + storeShift) % cells.length;
    }

    private void moveStonesFromThisCellAndOpponentOppositeCellToStore(int playerCell, Player player) {
        int storeIndex = getStoreIndex(player);

        int oppositeCellIndex = Constants.TOTAL_CELLS_COUNT - 2 - playerCell;
        cells[storeIndex] = cells[storeIndex] + cells[playerCell] + cells[oppositeCellIndex];

        cells[playerCell] = 0;
        cells[oppositeCellIndex] = 0;
    }

    private void moveHousesToStores() {
        moveHousesToStore(Player.FIRST);
        moveHousesToStore(Player.SECOND);
    }

    static boolean isPlayerHouse(int cellIndex, Player player) {
        int houseStartIndex = player.getIndex() * Constants.PLAYER_CELLS_COUNT;
        return houseStartIndex <= cellIndex && cellIndex < houseStartIndex + Constants.HOUSE_CELLS_COUNT;
    }

    private void moveHousesToStore(Player player) {
        for (int i = player.getIndex() * Constants.PLAYER_CELLS_COUNT; i < player.getIndex() * Constants.PLAYER_CELLS_COUNT + Constants.PLAYER_CELLS_COUNT - 1; i++) {
            cells[player.getIndex() * Constants.PLAYER_CELLS_COUNT + Constants.PLAYER_CELLS_COUNT - 1] = cells[player.getIndex() * Constants.PLAYER_CELLS_COUNT + Constants.PLAYER_CELLS_COUNT - 1] + cells[i];
            cells[i] = 0;
        }
    }

    private boolean isGameOver(Player player) {
        int sum = 0;
        for (int i = player.getIndex() * Constants.PLAYER_CELLS_COUNT; i < player.getIndex() * Constants.PLAYER_CELLS_COUNT + Constants.HOUSE_CELLS_COUNT; i++)
            sum += cells[i];

        return sum == 0;
    }

    private boolean finishedInEmptyCell(int cellIndex) {
        return cells[cellIndex] == 1;
    }

    private boolean isPlayerStore(Player player, final int index) {
        return (index % cells.length) == getStoreIndex(player);
    }

    private int getStoreIndex(Player player) {
        return player.getIndex() * Constants.PLAYER_CELLS_COUNT + (Constants.PLAYER_CELLS_COUNT - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        return Arrays.equals(cells, board.cells);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cells);
    }

    @Override
    public String toString() {
        return "Board{" + Arrays.toString(cells) + '}';
    }

    List<Integer> getPlayerPits(Player player) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < Constants.PLAYER_CELLS_COUNT; i++)
            result.add(cells[player.getIndex() * Constants.PLAYER_CELLS_COUNT + i]);

        return result;
    }

    public boolean isGameOver() {
        return isGameOver(Player.FIRST) || isGameOver(Player.SECOND);
    }

    public int getStore(Player first) {
        return cells[first.getIndex() * Constants.PLAYER_CELLS_COUNT + Constants.HOUSE_CELLS_COUNT];
    }
}
