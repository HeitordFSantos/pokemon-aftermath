package com.aftermath.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class MainMenuScreen implements Screen {

    private final AftermathGame game;
    private Texture background;
    private GlyphLayout layout;

    private final String[] options = {"Novo Jogo", "Continuar", "Opcoes", "Creditos", "Sair"};
    private int selectedIndex = 0;

    private float cursorTimer = 0f;
    private boolean showCursor = true;
    private float fadeAlpha = 1f;

    public MainMenuScreen(AftermathGame game) {
        this.game  = game;
        background = new Texture(Gdx.files.internal("menu_screen.png"));
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
        if (fadeAlpha > 0f) {
            fadeAlpha -= delta * 1.2f;
            if (fadeAlpha < 0f) fadeAlpha = 0f;
        }

        // Navegação
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + options.length) % options.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % options.length;
        }

        // Confirmar
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            handleSelection();
        }

        // Piscar cursor
        cursorTimer += delta;
        if (cursorTimer > 0.5f) {
            cursorTimer = 0f;
            showCursor  = !showCursor;
        }

        // Limpa tela
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // Fundo escurecido
        game.batch.setColor(0.4f, 0.4f, 0.4f, 1f);
        game.batch.draw(background, 0, 0, w, h);
        game.batch.setColor(1f, 1f, 1f, 1f);

        // Painel escuro esquerdo
        game.batch.setColor(0f, 0f, 0f, 0.75f);
        game.batch.draw(background, 0, 0, w * 0.35f, h);
        game.batch.setColor(1f, 1f, 1f, 1f);

        // Título MENU
        game.fontManager.titleFont.setColor(new Color(1f, 0.85f, 0.1f, 1f));
        game.fontManager.titleFont.draw(game.batch, "MENU", 80f, h - 80f);

        // Separador
        game.fontManager.smallFont.setColor(new Color(0.6f, 0.6f, 0.6f, 1f));
        game.fontManager.smallFont.draw(game.batch, "--------------------", 70f, h - 140f);

        // Opções
        float startY  = h - 200f;
        float spacing = 100f;

        for (int i = 0; i < options.length; i++) {
            float itemY      = startY - (i * spacing);
            boolean selected = (i == selectedIndex);

            if (selected) {
                game.fontManager.menuFont.setColor(new Color(1f, 0.9f, 0.1f, 1f));
                if (showCursor) {
                    game.fontManager.menuFont.draw(game.batch, ">", 55f, itemY);
                }
            } else {
                game.fontManager.menuFont.setColor(Color.WHITE);
            }

            game.fontManager.menuFont.draw(game.batch, options[i], 100f, itemY);
        }

        // Fade overlay
        if (fadeAlpha > 0f) {
            game.batch.setColor(0f, 0f, 0f, fadeAlpha);
            game.batch.draw(background, 0, 0, w, h);
            game.batch.setColor(1f, 1f, 1f, 1f);
        }

        game.batch.end();
    }

    private void handleSelection() {
        switch (selectedIndex) {
            case 0:
                game.setScreen(new IntroScreen(game));
                dispose();
                break;
            case 1:
                Gdx.app.log("Menu", "Continuar selecionado");
                break;
            case 2:
                game.setScreen(new OptionsScreen(game));
                dispose();
                break;
            case 3:
                Gdx.app.log("Menu", "Creditos selecionado");
                break;
            case 4:
                Gdx.app.exit();
                break;
        }
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