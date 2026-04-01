package com.aftermath.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

public class IntroScreen implements Screen {

    private final AftermathGame game;
    private Texture background;
    private GlyphLayout layout;

    private final String[] lines = {
    "Humano...",
    "Bem-vindo a este mundo...",
    "Parte moldado pela natureza...",
    "...parte, pelo progresso.",
    "Um lugar onde acreditam viver em harmonia...",
    "...e aventura.",
    "Mas ha muito tempo, esta terra foi partida pela Grande Ruptura...",
    "...e dois mundos nasceram:",
    "um guiado pela tradicao...",
    "...e outro pelo avanco.",
    "Hoje, cidades florescem... rotas se conectam...",
    "...mas as cicatrizes ainda sangram sob a terra.",
    "Eu vejo o que voces fingem esquecer.",
    "Ha lendas sobre uma guerra esquecida...",
    "...quando deuses foram forcados a lutar...",
    "...e o mundo quase foi destruido.",
    "Verdade...",
    "...ou mentira em que os homens escolheram acreditar?",
    "Isto, jovem...",
    "...voce tera de descobrir.",
    "Sua jornada...",
    "...comeca agora."
};

    private int     currentLine  = 0;
    private float   lineTimer    = 0f;
    private float   lineDuration = 3f;

    private float   typeTimer    = 0f;
    private float   typeSpeed    = 0.07f;
    private int     visibleChars = 0;

    private float   fadeAlpha    = 0f;
    private boolean fadingIn     = true;
    private boolean fadingOut    = false;

    private float   exitFade     = 0f;
    private boolean exiting      = false;

    public IntroScreen(AftermathGame game) {
        this.game  = game;
        background = new Texture(Gdx.files.internal("intro_text.png"));
        layout     = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.camera.combined);
        game.shapeRenderer.setProjectionMatrix(game.camera.combined);

        int w = AftermathGame.V_WIDTH;
        int h = AftermathGame.V_HEIGHT;

        // Texto atual — sem \n para evitar bugs no substring
        String fullLine = (currentLine < lines.length) ? lines[currentLine] : "";
visibleChars = Math.min(visibleChars, fullLine.length());

        // Garante que visibleChars nunca ultrapassa o tamanho real
        if (visibleChars > fullLine.length()) {
            visibleChars = fullLine.length();
        }

        // Pular introdução
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            startExit();
        }

        // Avançar frase
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            if (visibleChars < fullLine.length()) {
                visibleChars = fullLine.length();
            } else {
                nextLine();
            }
        }

        // Animação de digitação
        if (!fadingOut && !exiting) {
            typeTimer += delta;
            if (typeTimer >= typeSpeed) {
                typeTimer = 0f;
                if (visibleChars < fullLine.length()) {
                    visibleChars++;
                }
            }
        }

        // Timer automático
        if (visibleChars >= fullLine.length() && !fadingOut && !exiting) {
            lineTimer += delta;
            if (lineTimer >= lineDuration) {
                nextLine();
            }
        }

        // Fade in
        if (fadingIn) {
            fadeAlpha += delta * 2f;
            if (fadeAlpha >= 1f) {
                fadeAlpha = 1f;
                fadingIn  = false;
            }
        }

        // Fade out
        if (fadingOut) {
            fadeAlpha -= delta * 2f;
            if (fadeAlpha <= 0f) {
                fadeAlpha    = 0f;
                fadingOut    = false;
                fadingIn     = true;
                visibleChars = 0;
                typeTimer    = 0f;
                lineTimer    = 0f;
            }
        }

        // Fade de saída
        if (exiting) {
            exitFade += delta * 1.2f;
            if (exitFade >= 1f) {
                game.setScreen(new CharacterCreationScreen(game));
                dispose();
                return;
            }
        }

        // Limpa tela
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // Fundo
        game.batch.setColor(0.6f, 0.6f, 0.6f, 1f);
        game.batch.draw(background, 0, 0, w, h);
        game.batch.setColor(1f, 1f, 1f, 1f);

        // Texto do Mewtwo
        int safeChars = Math.min(visibleChars, fullLine.length());
        String displayText = fullLine.substring(0, safeChars);
        game.fontManager.menuFont.setColor(new Color(0.15f, 0.05f, 0.25f, fadeAlpha));
        game.fontManager.menuFont.draw(game.batch, displayText,
            w * 0.17f, h * 0.62f, w * 0.65f, Align.center, true);

        // Assinatura
        game.fontManager.smallFont.setColor(new Color(0.3f, 0.1f, 0.4f, fadeAlpha * 0.8f));
        game.fontManager.smallFont.draw(game.batch, "— Mewtwo", w * 0.58f, h * 0.35f);

        // Dica piscando
        if (visibleChars >= fullLine.length() && !fadingOut && !exiting) {
            float blink = (float)(Math.sin(Gdx.graphics.getFrameId() * 0.08f) * 0.5f + 0.5f);
            game.fontManager.smallFont.setColor(new Color(0.3f, 0.1f, 0.4f, blink));
            game.fontManager.smallFont.draw(game.batch,
                "ENTER  |  ESC para pular", w * 0.38f, h * 0.22f);
        }

        game.batch.end();

        // Overlay de saída
        if (exiting && exitFade > 0f) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(0f, 0f, 0f, Math.min(exitFade, 1f));
            game.shapeRenderer.rect(0, 0, w, h);
            game.shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    private void nextLine() {
    if (currentLine >= lines.length - 1) {
        startExit();
        return;
    }
    fadingOut    = true;
    visibleChars = 0;
    typeTimer    = 0f;
    lineTimer    = 0f;
    currentLine++;

    }

    private void startExit() {
        exiting  = true;
        exitFade = 0f;
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