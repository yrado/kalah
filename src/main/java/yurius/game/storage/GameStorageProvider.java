package yurius.game.storage;

public class GameStorageProvider {
    private static GameStorage instance = new GameStorageImpl();

    public static GameStorage getInstance() {
        return instance;
    }

    private GameStorageProvider() {
    }
}
