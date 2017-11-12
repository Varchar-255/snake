package br.com.varchar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by josevieira on 12/11/17.
 */
public class GameScreen implements Screen {

    private Game game;
    private Viewport viewport;
    private SpriteBatch batch;

    private Texture textureBody;
    private Texture textureBackground;
    private Texture textureFood;

    private boolean[][] body;
    private Array<Vector2> partes;

    private int direction; // 1 - up 2- right - 3 down - 4 lef

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        this.batch = new SpriteBatch();
        this.viewport = new FitViewport(100, 100);
        this.viewport.apply();

        generateTexture();
        init();
    }

    private void init() {
        body = new boolean[20][20];
        partes = new Array<Vector2>();

        partes.add(new Vector2(6, 5));
        body[6][5] = true;

        partes.add(new Vector2(5, 5));
        body[5][5] = true;

        direction = 2;
    }

    private void generateTexture() {
        /**
         * Snake
         */
        Pixmap pixMapSnake = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixMapSnake.setColor(1f, 1f, 1f, 1f);
        pixMapSnake.fillRectangle(0, 0, 64, 64);

        textureBody = new Texture(pixMapSnake);
        pixMapSnake.dispose();

        /**
         * Map
         */
        Pixmap pixMap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixMap.setColor(0.29f, 0.784f, 0.373f, 0.5f);
        pixMap.fillRectangle(0, 0, 64, 64);

        textureBackground = new Texture(pixMap);
        pixMap.dispose();

        /**
         * food snake
         */
        Pixmap pixMapFood = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixMapFood.setColor(1f, 1f, 1f, 1f);
        pixMapFood.fillCircle(32, 32, 32);
        textureFood = new Texture(pixMapFood);
        pixMapFood.dispose();

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glClearColor(0.29f, 0.894f, 0.373f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(textureBackground, 0, 0, 100, 100);

        for (Vector2 vector : partes) {
             batch.draw(textureBody, vector.x * 5, vector.y * 5, 5, 5);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
