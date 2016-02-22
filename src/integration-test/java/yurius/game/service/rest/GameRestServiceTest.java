package yurius.game.service.rest;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import yurius.game.model.Player;
import yurius.game.model.Status;
import yurius.game.service.rest.dto.GamePlayer;
import yurius.game.service.rest.dto.GameState;
import yurius.game.service.rest.dto.Move;
import yurius.game.storage.GameStorageImplFixture;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class GameRestServiceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(GameRestService.class);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        GameStorageImplFixture.cleanStorage();
    }

    @Test
    public void testPing() {
        final String response = target("/ping").request().get(String.class);

        assertThat(response, is("OK"));
    }

    @Test
    public void testNewGameId() {
        // WHEN
        final GamePlayer game1_1 = target("/createOrJoin").request().get(GamePlayer.class);

        // THEN
        assertThat(game1_1.getPlayer(), is(Player.FIRST));

        // WHEN
        final GamePlayer game1_2 = target("/createOrJoin").request().get(GamePlayer.class);

        //THEN
        assertThat(game1_2.getPlayer(), is(Player.SECOND));
        assertThat(game1_2.getGameId(), is(game1_1.getGameId()));

        // WHEN
        final GamePlayer game2 = target("/createOrJoin").request().get(GamePlayer.class);

        //THEN
        assertThat(game2.getPlayer(), is(Player.FIRST));
        assertThat(game2.getGameId(), is(not(game1_1.getGameId())));
    }

    @Test
    public void testGetGame_idAndStatus() {
        // GIVEN
        final GamePlayer gameId = target("/createOrJoin").request(MediaType.APPLICATION_JSON_TYPE).get(GamePlayer.class);

        // WHEN
        GameState gameState = target("/" + gameId.getGameId()).request(MediaType.APPLICATION_JSON_TYPE).get(GameState.class);

        // THEN
        assertThat(gameState.getGameId(), is(gameId.getGameId()));
        assertThat(gameState.getStatus(), is(Status.WAITING_FOR_SECOND_PLAYER));

        // WHEN
        gameState = target("/" + gameId.getGameId()).request(MediaType.APPLICATION_JSON_TYPE).get(GameState.class);

        // THEN
        assertThat(gameState.getGameId(), is(gameId.getGameId()));
        assertThat(gameState.getStatus(), is(Status.WAITING_FOR_SECOND_PLAYER));
    }

    @Test
    public void testGetGame_boardState() {
        // GIVEN
        final GamePlayer gameId = target("/createOrJoin").request(MediaType.APPLICATION_JSON_TYPE).get(GamePlayer.class);

        // WHEN
        GameState gameState = target("/" + gameId.getGameId()).request(MediaType.APPLICATION_JSON_TYPE).get(GameState.class);

        // THEN
        assertThat(gameState.getBoardState().getFirstPlayer(), contains(6, 6, 6, 6, 6, 6, 0));
        assertThat(gameState.getBoardState().getSecondPlayer(), contains(6, 6, 6, 6, 6, 6, 0));
    }

    @Test
    public void testGetGame_message() {
        // GIVEN
        final GamePlayer gameId = target("/createOrJoin").request(MediaType.APPLICATION_JSON_TYPE).get(GamePlayer.class);

        // WHEN
        GameState gameState = target("/" + gameId.getGameId()).request(MediaType.APPLICATION_JSON_TYPE).get(GameState.class);

        // THEN
        assertThat(gameState.getMessage(), is(notNullValue()));
        assertThat(gameState.getBoardState().getSecondPlayer(), contains(6, 6, 6, 6, 6, 6, 0));
    }

    @Test
    public void testGetGame_gameNotPresent() {
        // WHEN
        Response response = target("/not-existing-id").request(MediaType.APPLICATION_JSON_TYPE).get();

        // THEN
        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void testMakeMove_turnStays() {
        // GIVEN
        final GamePlayer newGame = target("/createOrJoin").request().get(GamePlayer.class);
        final GamePlayer secondPlayerJoined = target("/createOrJoin").request().get(GamePlayer.class);

        // WHEN
        Move move = new Move(Player.FIRST, 1);
        Response response = target("/" + newGame.getGameId() + "/makeMove")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(move, MediaType.APPLICATION_JSON_TYPE));

        // THEN
        assertThat(response.readEntity(String.class), response.getStatus(), is(200));

        GameState gameState = target("/" + newGame.getGameId()).request().get(GameState.class);
        assertThat(gameState.getStatus(), is(Status.FIRST_PLAYER_TURN));
        assertThat(gameState.getBoardState().getFirstPlayer(), contains(0, 7, 7, 7, 7, 7, 1));
        assertThat(gameState.getBoardState().getSecondPlayer(), contains(6, 6, 6, 6, 6, 6, 0));
    }

    @Test
    public void testMakeMove_turnChanges() {
        // GIVEN
        final GamePlayer newGame = target("/createOrJoin").request().get(GamePlayer.class);
        final GamePlayer secondPlayerJoined = target("/createOrJoin").request().get(GamePlayer.class);

        // WHEN
        Move move = new Move(Player.FIRST, 2);
        Response response = target("/" + newGame.getGameId() + "/makeMove")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(move, MediaType.APPLICATION_JSON_TYPE));

        // THEN
        assertThat(response.readEntity(String.class), response.getStatus(), is(200));

        GameState gameState = target("/" + newGame.getGameId()).request().get(GameState.class);
        assertThat(gameState.getStatus(), is(Status.SECOND_PLAYER_TURN));
        assertThat(gameState.getBoardState().getFirstPlayer(), contains(6, 0, 7, 7, 7, 7, 1));
        assertThat(gameState.getBoardState().getSecondPlayer(), contains(7, 6, 6, 6, 6, 6, 0));
    }

    @Test
    public void testMakeMove_wrongPlayer() {
        // GIVEN
        final GamePlayer newGame = target("/createOrJoin").request().get(GamePlayer.class);
        final GamePlayer secondPlayerJoined = target("/createOrJoin").request().get(GamePlayer.class);

        // WHEN
        Move move = new Move(Player.SECOND, 2);
        Response response = target("/" + newGame.getGameId() + "/makeMove")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(move, MediaType.APPLICATION_JSON_TYPE));

        // THEN
        assertThat(response.readEntity(String.class), response.getStatus(), is(400));
    }

    @Test
    public void testMakeMove_secondPlayerNotJoinedFirstMakesMove() {
        // GIVEN
        final GamePlayer newGame = target("/createOrJoin").request().get(GamePlayer.class);

        // WHEN
        Move move = new Move(Player.FIRST, 2);
        Response response = target("/" + newGame.getGameId() + "/makeMove")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(move, MediaType.APPLICATION_JSON_TYPE));

        // THEN
        assertThat(response.readEntity(String.class), response.getStatus(), is(400));
    }

}
