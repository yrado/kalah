package yurius.game.storage;

public class GameStorageProvider {
	private volatile static GameStorage instance = new GameStorageImpl();

	private GameStorageProvider() {
	}

	public static GameStorage getInstance() {
		return instance;
	}

}
