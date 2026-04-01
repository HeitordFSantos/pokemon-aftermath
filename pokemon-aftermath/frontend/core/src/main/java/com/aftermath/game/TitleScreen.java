package com.aftermath.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class TitleScreen implements Screen {

    private final AftermathGame game;
    private Texture background;
    private GlyphLayout layout;

    private float blinkTimer = 0f;
    private boolean showBlink = true;
    private float fadeAlpha = 1f;
    private boolean fadingIn = true;

    public TitleScreen(AftermathGame game) {
        this.game  = game;
        background = new Texture(Gdx.files.internal("tittle_screen.png"));
        layout     = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.camera.combined);
        game.shapeRenderer.setProjectionMatrix(game.camera.combined);

        int w = AftermathGame.V_WIDTH;
        int h = AftermathGame.V_HEIGHT;

        // Fade in
        if (fadingIn) {
            fadeAlpha -= delta * 0.8f;
            if (fadeAlpha <= 0f) {
                fadeAlpha = 0f;
                fadingIn  = false;
            }
        }

        // Piscar texto
        blinkTimer += delta;
        if (blinkTimer > 0.65f) {
            blinkTimer = 0f;
            showBlink  = !showBlink;
        }

        // Input
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
            return;
        }

        // Limpa tela
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // Fundo
        game.batch.setColor(1f, 1f, 1f, 1f);
        game.batch.draw(background, 0, 0, w, h);

        // Texto piscando
        if (showBlink) {
            String prompt = "Pressione ENTER para continuar";
            layout.setText(game.fontManager.smallFont, prompt);
            float promptX = (w - layout.width) / 2f;
            float promptY = h * 0.10f + layout.height;
            game.fontManager.smallFont.setColor(new Color(1f, 1f, 0.6f, 1f));
            game.fontManager.smallFont.draw(game.batch, prompt, promptX, promptY);
        }

        // Fade overlay
        if (fadeAlpha > 0f) {
            game.batch.setColor(0f, 0f, 0f, fadeAlpha);
            game.batch.draw(background, 0, 0, w, h);
            game.batch.setColor(1f, 1f, 1f, 1f);
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override public void show()   {}
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        background.dispose();
    }
}