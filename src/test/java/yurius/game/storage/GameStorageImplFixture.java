package yurius.game.storage;

public class GameStorageImplFixture {
    public static void cleanStorage() {
        GameStorage instance = GameStorageProvider.getInstance();
        if (instance instanceof GameStorageImpl)
            ((GameStorageImpl) instance).clear();
    }
}
