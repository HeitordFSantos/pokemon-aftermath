package com.aftermath.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class AftermathGame extends Game {

    public static final int V_WIDTH  = 1920;
    public static final int V_HEIGHT = 1080;

    public SpriteBatch        batch;
    public ShapeRenderer      shapeRenderer;
    public FontManager        fontManager;
    public GameSettings       settings;
    public OrthographicCamera camera;
    public Viewport           viewport;

    @Override
    public void create() {
        camera   = new OrthographicCamera();
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, camera);
        viewport.apply();
        camera.position.set(V_WIDTH / 2f, V_HEIGHT / 2f, 0);
        camera.update();

        batch         = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        fontManager   = new FontManager();
        settings      = new GameSettings();

        setScreen(new TitleScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        fontManager.dispose();
    }
}