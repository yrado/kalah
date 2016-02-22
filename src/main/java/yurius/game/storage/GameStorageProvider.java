package yurius.game.storage;

public class GameStorageProvider {
    private final static GameStorage instance = new GameStorageImpl();

    public static GameStorage getInstance() {
        return instance;
    }

    private GameStorageProvider() {
    }
}
