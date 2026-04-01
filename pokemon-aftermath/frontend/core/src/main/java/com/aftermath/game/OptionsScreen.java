package com.aftermath.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class OptionsScreen implements Screen {

    private final AftermathGame game;
    private Texture background;
    private GlyphLayout layout;

    private final String[] labels = {
        "-- AUDIO --",
        "Volume da Musica",
        "Volume dos Efeitos",
        "-- GAMEPLAY --",
        "Velocidade do Texto",
        "Batalha Animada",
        "Dificuldade",
        "-- TELA --",
        "Modo",
        "Brilho",
        "-- CONTROLES --",
        "Confirmar",
        "Cancelar",
        "Menu",
        "-- --",
        "Voltar"
    };

    private final int[] navigable = {1,2,4,5,6,8,9,11,12,13,15};
    private int navIndex    = 0;
    private int selectedRow = navigable[0];

    private float cursorTimer = 0f;
    private boolean showCursor = true;
    private float fadeAlpha = 1f;

    private boolean waitingForKey = false;
    private int remapTarget = -1;

    public OptionsScreen(AftermathGame game) {
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
            fadeAlpha -= delta * 1.5f;
            if (fadeAlpha < 0f) fadeAlpha = 0f;
        }

        // Piscar cursor
        cursorTimer += delta;
        if (cursorTimer > 0.5f) {
            cursorTimer = 0f;
            showCursor  = !showCursor;
        }

        if (waitingForKey) {
            handleRemap();
        } else {
            handleInput();
        }

        // Limpa tela
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // Fundo escurecido
        game.batch.setColor(0.3f, 0.3f, 0.3f, 1f);
        game.batch.draw(background, 0, 0, w, h);
        game.batch.setColor(1f, 1f, 1f, 1f);

        // Painel escuro
        game.batch.setColor(0f, 0f, 0f, 0.80f);
        game.batch.draw(background, 0, 0, w * 0.55f, h);
        game.batch.setColor(1f, 1f, 1f, 1f);

        // Título
        game.fontManager.titleFont.setColor(new Color(1f, 0.85f, 0.1f, 1f));
        game.fontManager.titleFont.draw(game.batch, "OPCOES", 80f, h - 60f);

        // Opções
        float startY  = h - 140f;
        float spacing = 55f;
        float labelX  = 100f;
        float valueX  = w * 0.38f;

        for (int i = 0; i < labels.length; i++) {
            float y          = startY - (i * spacing);
            boolean selected = (i == selectedRow);
            boolean isSep    = labels[i].startsWith("--");

            if (isSep) {
                game.fontManager.smallFont.setColor(new Color(0.5f, 0.8f, 1f, 1f));
                game.fontManager.smallFont.draw(game.batch, labels[i], labelX - 20f, y);
                continue;
            }

            // Cursor
            if (selected && showCursor) {
                game.fontManager.menuFont.setColor(new Color(1f, 0.9f, 0.1f, 1f));
                game.fontManager.menuFont.draw(game.batch, ">", labelX - 40f, y);
            }

            // Label
            game.fontManager.menuFont.setColor(
                selected ? new Color(1f, 0.9f, 0.1f, 1f) : Color.WHITE
            );
            game.fontManager.menuFont.draw(game.batch, labels[i], labelX, y);

            // Valor
            String value = getValueFor(i);
            if (value != null) {
                if (waitingForKey && i == remapTarget) {
                    game.fontManager.menuFont.setColor(new Color(1f, 0.3f, 0.3f, 1f));
                    value = "Pressione...";
                } else {
                    game.fontManager.menuFont.setColor(new Color(0.6f, 1f, 0.6f, 1f));
                }
                game.fontManager.menuFont.draw(game.batch, value, valueX, y);
            }
        }

        // Fade overlay
        if (fadeAlpha > 0f) {
            game.batch.setColor(0f, 0f, 0f, fadeAlpha);
            game.batch.draw(background, 0, 0, w, h);
            game.batch.setColor(1f, 1f, 1f, 1f);
        }

        game.batch.end();
    }

    private String getValueFor(int index) {
        GameSettings s = game.settings;
        switch (index) {
            case 1:  return String.valueOf(s.musicVolume);
            case 2:  return String.valueOf(s.sfxVolume);
            case 4:  return s.getTextSpeedLabel();
            case 5:  return s.animatedBattle ? "Sim" : "Nao";
            case 6:  return s.getDifficultyLabel();
            case 8:  return s.fullscreen ? "Tela Cheia" : "Janela";
            case 9:  return String.valueOf(s.brightness);
            case 11: return s.getKeyName(s.keyConfirm);
            case 12: return s.getKeyName(s.keyCancel);
            case 13: return s.getKeyName(s.keyMenu);
            default: return null;
        }
    }

    private void handleInput() {
        GameSettings s = game.settings;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            navIndex    = (navIndex - 1 + navigable.length) % navigable.length;
            selectedRow = navigable[navIndex];
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            navIndex    = (navIndex + 1) % navigable.length;
            selectedRow = navigable[navIndex];
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))  changeValue(selectedRow, -1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) changeValue(selectedRow, +1);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            if (selectedRow == 15) {
                s.save();
                game.setScreen(new MainMenuScreen(game));
                dispose();
            } else if (selectedRow == 11 || selectedRow == 12 || selectedRow == 13) {
                waitingForKey = true;
                remapTarget   = selectedRow;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            s.save();
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    private void changeValue(int row, int dir) {
        GameSettings s = game.settings;
        switch (row) {
            case 1: s.musicVolume   = clamp(s.musicVolume  + dir * 5, 0, 100); break;
            case 2: s.sfxVolume     = clamp(s.sfxVolume    + dir * 5, 0, 100); break;
            case 4: s.textSpeed     = clamp(s.textSpeed    + dir,     0, 2);   break;
            case 5: s.animatedBattle = !s.animatedBattle;                      break;
            case 6: s.difficulty    = clamp(s.difficulty   + dir,     0, 2);   break;
            case 8: s.fullscreen    = !s.fullscreen;                           break;
            case 9: s.brightness    = clamp(s.brightness   + dir * 5, 0, 100); break;
        }
    }

    private void handleRemap() {
        for (int k = 0; k < 256; k++) {
            if (Gdx.input.isKeyJustPressed(k)) {
                if (k == Input.Keys.ESCAPE) {
                    waitingForKey = false;
                    remapTarget   = -1;
                    return;
                }
                switch (remapTarget) {
                    case 11: game.settings.keyConfirm = k; break;
                    case 12: game.settings.keyCancel  = k; break;
                    case 13: game.settings.keyMenu    = k; break;
                }
                waitingForKey = false;
                remapTarget   = -1;
                return;
            }
        }
    }

    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
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