package br.com.varchar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

/**
 * Created by josevieira on 12/11/17.
 */
public class GameScreen implements Screen, GestureDetector.GestureListener {

    private Game game;
    private Viewport viewport;
    private SpriteBatch batch;

    private Texture textureBody;
    private Texture textureBackground;
    private Texture textureFood;

    private boolean[][] body;
    private Array<Vector2> partes;

    private int direction; // 1 - up 2- right - 3 down - 4 lef

    private float timeToMove;

    private Vector2 toque;

    private Array<Vector2> pontos;
    private float timeToNext;

    private Random random;

    private int state;

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

        toque = new Vector2();
        random = new Random();

        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    private void init() {
        body = new boolean[20][20];
        partes = new Array<Vector2>();

        partes.add(new Vector2(6, 5));
        body[6][5] = true;

        partes.add(new Vector2(5, 5));
        body[5][5] = true;

        direction = 2;
        timeToMove = 0.4f;

        pontos = new Array<Vector2>();
        timeToNext = 3f;

        state = 0;
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
        update(delta);

        batch.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glClearColor(0.29f, 0.894f, 0.373f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(textureBackground, 0, 0, 100, 100);

        for (Vector2 vector : partes) {
             batch.draw(textureBody, vector.x * 5, vector.y * 5, 5, 5);
        }

        for (Vector2 vector : pontos) {
            batch.draw(textureFood, vector.x * 5, vector.y * 5, 5, 5);
        }

        batch.end();
    }

    private void update(float delta) {
       if (state == 0){
           timeToMove -= delta;
           if (timeToMove <= 0) {
               timeToMove = 0.4f;

               Gdx.app.log("MOVE", "MOVE");

               int x1, x2;
               int y1, y2;

               x1 = (int) partes.get(0).x;
               y1 = (int) partes.get(0).y;

               body[x1][y1] = false;

               x2 = x1;
               y2 = y1;

               switch (direction) {
                   case 1:
                       y1 ++;
                       break;
                   case 2:
                       x1 ++;
                       break;
                   case 3:
                       y1 --;
                       break;
                   case 4:
                       x1 --;
                       break;
               }

               if (x1 < 0 || y1 < 0 || x1 > 19 || y1 > 19 || body[x1][y1]) {
                   state = 1;
                   return;
               }


               for (int j = 0; j < pontos.size; j++) {
                   if (pontos.get(j).x == x1 && pontos.get(j).y == y1) {
                       pontos.removeIndex(j);
                       partes.insert(0, new Vector2(x1, y1));
                       body[x1][y1] = true;
                       body[x2][y2] = true;
                       return;
                   }
               }

               partes.get(0).set(x1, y1);
               body[x1][y1] = true;

               for (int i = 1; i < partes.size; i ++) {
                   x1 = (int) partes.get(i).x;
                   y1 = (int) partes.get(i).y;
                   body[x1][y1] = false;

                   partes.get(i).set(x2, y2);
                   body[x2][y2] = true;

                   x2 = x1;
                   y2 = y1;
               }

           }

           timeToNext -= delta;
           if (timeToNext <= 0) {
               int x = random.nextInt(20);
               int y = random.nextInt(20);

               if (!body[x][y]) {
                   pontos.add(new Vector2(x, y));
                   timeToNext = 5f;
               }

           }
       }
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

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        if (state == 1) {
            game.setScreen(new MainScreen(game));
        }
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        viewport.unproject(toque.set(velocityX, velocityY));

        if (state == 0) {
            if (Math.abs(toque.x) > Math.abs(toque.y)) toque.y = 0;
            else toque.x = 0;

            if (toque.x > 50 && direction != 4) {
                direction = 2;
            } else if (toque.y > 50 && direction != 3) {
                direction = 1;
            } else if (toque.x < -50 && direction != 2) {
                direction = 4;
            } else if (toque.y < -50 && direction != 1) {
                direction = 3;
            }
        }

        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
