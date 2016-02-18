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
        int[] array = new int[Constants.TOTAL_CELLS_COUNT];

        // TODO IR join for loops to one
        for (int i = 0; i < Constants.PLAYER_CELLS_COUNT; i++)
            // TODO IR add range checks
            array[i] = boardState.getFirstPlayer().get(i);

        for (int i = 0; i < Constants.PLAYER_CELLS_COUNT; i++)
            array[i + Constants.PLAYER_CELLS_COUNT] = boardState.getSecondPlayer().get(i);

        return new Board(array);
    }

    public LastSeedPlacement moveStones(final Player player, final int houseNumber) throws EmptyCellException {
        if (houseNumber < 1 || houseNumber > Constants.HOUSE_CELLS_COUNT)
            throw new IllegalArgumentException(
                    String.format("House number must be between %d and %d. Got: %s.", 1, Constants.HOUSE_CELLS_COUNT, houseNumber));

        int startCellIndex = player.getIndex() * Constants.PLAYER_CELLS_COUNT + houseNumber - 1;
        int numStones = cells[startCellIndex];

        if (numStones == 0)
            throw new EmptyCellException("Cannot move stones from empty cell");

        cells[startCellIndex] = 0;

        int houseShift = 0;
        int lastCellIndex = 0;
        for (int i = 1; i <= numStones; i++) {
            int cellIndex = (startCellIndex + i + houseShift) % cells.length;
            if (houseOfOtherPlayer(player, cellIndex)) {
                cellIndex = (cellIndex + 1) % cells.length;
                houseShift++;
            }

            cells[cellIndex] = cells[cellIndex] + 1;

            if (isLastCell(numStones, i)) {
                lastCellIndex = cellIndex;

                if (finishedInEmptyCell(lastCellIndex) && isPlayerHouse(player, lastCellIndex))
                    grabStonesFromThisCellAndOpponnentsOppositeCell(player, lastCellIndex);
            }
        }

        moveStonesIfEmptyHouses();

        return getLastStonePlacement(player, lastCellIndex);
    }

    private void grabStonesFromThisCellAndOpponnentsOppositeCell(Player player, int lastCellIndex) {
        int storeIndex = player.getIndex() * Constants.PLAYER_CELLS_COUNT + Constants.PLAYER_CELLS_COUNT - 1;

        int oppositeCellIndex = Constants.TOTAL_CELLS_COUNT - 2 - lastCellIndex;
        cells[storeIndex] = cells[storeIndex] + cells[lastCellIndex] + cells[oppositeCellIndex];

        cells[lastCellIndex] = 0;
        cells[oppositeCellIndex] = 0;
    }

    private boolean moveStonesIfEmptyHouses() {
        for (Player player : Player.values()) {
            if (playerHousesEmpty(player)) {
                moveHouseToStore(player.getOther());
                return true;
            }
        }
        return false;
    }

    static boolean isPlayerHouse(Player player, int cellIndex) {
        int houseStartIndex = player.getIndex() * Constants.PLAYER_CELLS_COUNT;
        return houseStartIndex <= cellIndex && cellIndex < houseStartIndex + Constants.HOUSE_CELLS_COUNT;
    }

    private void moveHouseToStore(Player player) {
        for (int i = player.getIndex() * Constants.PLAYER_CELLS_COUNT; i < player.getIndex() * Constants.PLAYER_CELLS_COUNT + Constants.PLAYER_CELLS_COUNT - 1; i++) {
            cells[player.getIndex() * Constants.PLAYER_CELLS_COUNT + Constants.PLAYER_CELLS_COUNT - 1] = cells[player.getIndex() * Constants.PLAYER_CELLS_COUNT + Constants.PLAYER_CELLS_COUNT - 1] + cells[i];
            cells[i] = 0;
        }
    }

    private boolean playerHousesEmpty(Player player) {
        int sum = 0;
        for (int i = player.getIndex() * Constants.PLAYER_CELLS_COUNT; i < player.getIndex() * Constants.PLAYER_CELLS_COUNT + Constants.HOUSE_CELLS_COUNT; i++)
            sum += cells[i];

        return sum == 0;
    }

    private boolean finishedInEmptyCell(int cellIndex) {
        return cells[cellIndex] == 1;
    }

    private boolean isLastCell(int numStones, int i) {
        return i == numStones;
    }

    private LastSeedPlacement getLastStonePlacement(Player player, int lastCellIndex) {

        if (Player.SECOND == player)
            lastCellIndex = (lastCellIndex + Constants.PLAYER_CELLS_COUNT) % Constants.TOTAL_CELLS_COUNT;

        int playerStoreIndex = Constants.PLAYER_CELLS_COUNT - 1;

        if (lastCellIndex < playerStoreIndex)
            return LastSeedPlacement.PLAYER_HOUSE;
        if (lastCellIndex > playerStoreIndex)
            return LastSeedPlacement.OPPONENT_HOUSE;
        return LastSeedPlacement.PLAYER_STORE;
    }

    private boolean houseOfOtherPlayer(final Player player, final int index) {
        return index == getStoreIndex(player.getOther());
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

    // TODO IR
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

    public boolean allHousesEmpty() {
        int sum = 0;
        for (Player player : Player.values())
            for (int i = 0; i < Constants.HOUSE_CELLS_COUNT; i++)
                sum += cells[player.getIndex() * Constants.PLAYER_CELLS_COUNT + i];

        return sum == 0;
    }

    public int getStore(Player first) {
        return cells[first.getIndex() * Constants.PLAYER_CELLS_COUNT + Constants.HOUSE_CELLS_COUNT];
    }
}
