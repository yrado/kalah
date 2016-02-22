package yurius.game.model;

import java.util.ArrayList;
import java.util.List;

import yurius.game.controller.Constants;

public class BoardState {
	private List<Integer> firstPlayer  = new ArrayList<>(Constants.PLAYER_CELLS_COUNT);
	private List<Integer> secondPlayer = new ArrayList<>(Constants.PLAYER_CELLS_COUNT);

	public BoardState() {
	}

	public BoardState(List<Integer> firstPlayer, List<Integer> secondPlayer) {
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
	}

	public BoardState(BoardState boardState) {
		this(new ArrayList<>(boardState.getFirstPlayer()), new ArrayList<>(boardState.getSecondPlayer()));
	}

	public List<Integer> getFirstPlayer() {
		return firstPlayer;
	}

	public void setFirstPlayer(List<Integer> firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	public List<Integer> getSecondPlayer() {
		return secondPlayer;
	}

	public void setSecondPlayer(List<Integer> secondPlayer) {
		this.secondPlayer = secondPlayer;
	}

	public static BoardState createDefault() {

		return new BoardState(createDefaultLayout(), createDefaultLayout());
	}

	private static List<Integer> createDefaultLayout() {

		ArrayList<Integer> integers = new ArrayList<>(Constants.PLAYER_CELLS_COUNT);
		for (int i = 0; i < Constants.HOUSE_CELLS_COUNT; i++) {
			integers.add(Constants.DEFAULT_HOUSE_STONE_COUNT);
		}
		integers.add(0);
		return integers;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BoardState that = (BoardState) o;

		if (!firstPlayer.equals(that.firstPlayer)) return false;
		return secondPlayer.equals(that.secondPlayer);

	}

	@Override
	public int hashCode() {
		int result = firstPlayer.hashCode();
		result = 31 * result + secondPlayer.hashCode();
		return result;
	}
}
