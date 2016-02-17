package yurius.game;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class GameLauncher {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8081);

        WebAppContext webAppContext = new WebAppContext("src/main/webapp", "/");
        webAppContext.setLogUrlOnStart(true);
        webAppContext.setWelcomeFiles(new String[]{"index.html"});
        webAppContext.configure();

        server.setHandler(webAppContext);
        server.start();
        server.join();
    }
}
