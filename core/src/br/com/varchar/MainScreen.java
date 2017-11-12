package br.com.varchar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by josevieira on 11/11/17.
 */
public class MainScreen implements Screen {

    private Game game;
    private Viewport viewport;
    private SpriteBatch spriteBatch;
    private Texture[] menuTexture;
    private float time;

    private boolean touching;

    public MainScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        time = 0;
        touching = false;

        spriteBatch = new SpriteBatch();

        viewport = new FillViewport(1000, 1500);
        viewport.apply();

        menuTexture = new Texture[2];

        menuTexture[0] = new Texture("fundo0.png");
        menuTexture[1] = new Texture("fundo1.png");

        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        time += delta;

        input();

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glClearColor(0.29f, 0.894f, 0.373f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();

        spriteBatch.draw(menuTexture[(int)time%2], 0, 0, 1000, 1500);

        spriteBatch.end();
    }

    private void input() {
        if (Gdx.input.justTouched()) {
            touching = true;
        } else if (!Gdx.input.justTouched() && touching == true) {
            touching = false;
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        spriteBatch.dispose();
        menuTexture[0].dispose();
        menuTexture[1].dispose();
    }
}
