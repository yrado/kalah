package yurius.game;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class GameLauncher {
    public static void main(String[] args) throws Exception {

        int port = Integer.parseInt(System.getProperty("port", "8080"));
        Server server = new Server(port);

        WebAppContext webAppContext = new WebAppContext("src/main/webapp", "/");
        webAppContext.setLogUrlOnStart(true);
        webAppContext.setWelcomeFiles(new String[]{"index.html"});
        webAppContext.configure();

        server.setHandler(webAppContext);
        server.start();
        server.join();
    }
}
