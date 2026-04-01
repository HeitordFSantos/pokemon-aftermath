package com.aftermath.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class CharacterCreationScreen implements Screen {

    private final AftermathGame game;
    private Texture background;
    private GlyphLayout layout;

    // Passos: 0=sexo, 1=origem, 2=nome, 3=confirmacao
    private int step = 0;

    // Escolhas
    private int     sexoIndex   = 0; // 0=Masculino 1=Feminino
    private int     origemIndex = 0; // 0=Rico 1=Pobre
    private String  nome        = "";

    // Digitação do nome
    private float   cursorBlink = 0f;
    private boolean showCursor  = true;

    // Transição entre passos
    private float   fadeAlpha   = 1f;
    private boolean fadingIn    = true;
    private boolean fadingOut   = false;
    private int     nextStep    = -1;

    private final String[] sexoOpcoes   = {"Masculino", "Feminino"};
    private final String[] origemOpcoes = {"Rico", "Pobre"};

    public CharacterCreationScreen(AftermathGame game) {
        this.game  = game;
        background = new Texture(Gdx.files.internal("char_creation.png"));
        layout     = new GlyphLayout();

        // Ativa input de texto para o nome
        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
            @Override
            public boolean keyTyped(char character) {
                if (step == 2) {
                    if (character == '\b') {
                        if (nome.length() > 0)
                            nome = nome.substring(0, nome.length() - 1);
                    } else if (character == '\r' || character == '\n') {
                        if (nome.length() > 0) goToStep(3);
                    } else if (nome.length() < 12 && character >= 32) {
                        nome += character;
                    }
                }
                return true;
            }
        });
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
            fadeAlpha -= delta * 2.5f;
            if (fadeAlpha <= 0f) {
                fadeAlpha = 0f;
                fadingIn  = false;
            }
        }

        // Fade out → muda step
        if (fadingOut) {
            fadeAlpha += delta * 2.5f;
            if (fadeAlpha >= 1f) {
                fadeAlpha = 1f;
                fadingOut = false;
                fadingIn  = true;
                step      = nextStep;
            }
        }

        // Cursor do nome piscando
        cursorBlink += delta;
        if (cursorBlink > 0.5f) {
            cursorBlink = 0f;
            showCursor  = !showCursor;
        }

        // Input (apenas quando não está em transição)
        if (!fadingIn && !fadingOut) {
            handleInput();
        }

        // Limpa tela
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // Fundo — imagem limpa sem escurecer
        game.batch.setColor(1f, 1f, 1f, 1f);
        game.batch.draw(background, 0, 0, w, h);

        // Indicador de progresso
        String progresso = "Passo " + (step + 1) + " de 4";
        game.fontManager.smallFont.setColor(new Color(0.6f, 0.6f, 0.6f, 1f));
        layout.setText(game.fontManager.smallFont, progresso);
        game.fontManager.smallFont.draw(game.batch, progresso,
            (w - layout.width) / 2f, h * 0.82f);

        // Conteúdo do passo atual
        switch (step) {
            case 0: renderSexo(w, h);         break;
            case 1: renderOrigem(w, h);        break;
            case 2: renderNome(w, h);          break;
            case 3: renderConfirmacao(w, h);   break;
        }

        // Overlay de fade
        if (fadeAlpha > 0f) {
            game.batch.setColor(0f, 0f, 0f, fadeAlpha);
            game.batch.draw(background, 0, 0, w, h);
            game.batch.setColor(1f, 1f, 1f, 1f);
        }

        game.batch.end();
    }

    private void renderSexo(int w, int h) {
        // Título
        game.fontManager.titleFont.setColor(new Color(1f, 0.85f, 0.1f, 1f));
        layout.setText(game.fontManager.titleFont, "Quem é você?");
        game.fontManager.titleFont.draw(game.batch, "Quem é você?",
            (w - layout.width) / 2f, h * 0.75f);

        // Opções
        float startX = w * 0.32f;
        float spacing = w * 0.18f;
        for (int i = 0; i < sexoOpcoes.length; i++) {
            boolean sel = (i == sexoIndex);
            game.fontManager.menuFont.setColor(sel ?
                new Color(1f, 0.9f, 0.1f, 1f) : Color.WHITE);
            float x = startX + (i * spacing);
            layout.setText(game.fontManager.menuFont, sexoOpcoes[i]);
            game.fontManager.menuFont.draw(game.batch, sexoOpcoes[i],
                x - layout.width / 2f, h * 0.55f);
            if (sel) {
                game.fontManager.menuFont.setColor(new Color(1f, 0.9f, 0.1f, 1f));
                game.fontManager.menuFont.draw(game.batch, "▼",
                    x - layout.width / 2f, h * 0.58f);
            }
        }

        // Dica
        game.fontManager.smallFont.setColor(new Color(0.6f, 0.6f, 0.6f, 1f));
        layout.setText(game.fontManager.smallFont, "< > para escolher   ENTER para confirmar");
        game.fontManager.smallFont.draw(game.batch, "< > para escolher   ENTER para confirmar",
            (w - layout.width) / 2f, h * 0.28f);
    }

    private void renderOrigem(int w, int h) {
        game.fontManager.titleFont.setColor(new Color(1f, 0.85f, 0.1f, 1f));
        layout.setText(game.fontManager.titleFont, "Sua origem?");
        game.fontManager.titleFont.draw(game.batch, "Sua origem?",
            (w - layout.width) / 2f, h * 0.75f);

        // Descrições
        String[] descricoes = {
            "Familia nobre de Aurora Verde.\nComeca com mais recursos.",
            "Criado nas ruas de Kharalis.\nComeca com mais determinacao."
        };

        float startX = w * 0.30f;
        float spacing = w * 0.22f;
        for (int i = 0; i < origemOpcoes.length; i++) {
            boolean sel = (i == origemIndex);
            float x = startX + (i * spacing);

            game.fontManager.menuFont.setColor(sel ?
                new Color(1f, 0.9f, 0.1f, 1f) : Color.WHITE);
            layout.setText(game.fontManager.menuFont, origemOpcoes[i]);
            game.fontManager.menuFont.draw(game.batch, origemOpcoes[i],
                x - layout.width / 2f, h * 0.60f);

            // Descrição
            game.fontManager.smallFont.setColor(sel ?
                new Color(0.9f, 0.9f, 0.9f, 1f) :
                new Color(0.5f, 0.5f, 0.5f, 1f));
            game.fontManager.smallFont.draw(game.batch, descricoes[i],
                x - 140f, h * 0.52f, 280f,
                com.badlogic.gdx.utils.Align.center, true);
        }

        game.fontManager.smallFont.setColor(new Color(0.6f, 0.6f, 0.6f, 1f));
        layout.setText(game.fontManager.smallFont, "< > para escolher   ENTER para confirmar");
        game.fontManager.smallFont.draw(game.batch, "< > para escolher   ENTER para confirmar",
            (w - layout.width) / 2f, h * 0.28f);
    }

    private void renderNome(int w, int h) {
        game.fontManager.titleFont.setColor(new Color(1f, 0.85f, 0.1f, 1f));
        layout.setText(game.fontManager.titleFont, "Seu nome?");
        game.fontManager.titleFont.draw(game.batch, "Seu nome?",
            (w - layout.width) / 2f, h * 0.75f);

        // Campo do nome
        String nomeDisplay = nome + (showCursor ? "|" : " ");
        game.fontManager.menuFont.setColor(Color.WHITE);
        layout.setText(game.fontManager.menuFont, nomeDisplay);
        game.fontManager.menuFont.draw(game.batch, nomeDisplay,
            (w - layout.width) / 2f, h * 0.55f);

        // Linha embaixo do nome
        game.fontManager.smallFont.setColor(new Color(0.5f, 0.5f, 0.8f, 1f));
        game.fontManager.smallFont.draw(game.batch,
            "____________________",
            (w - 320f) / 2f, h * 0.50f);

        game.fontManager.smallFont.setColor(new Color(0.6f, 0.6f, 0.6f, 1f));
        layout.setText(game.fontManager.smallFont, "Digite seu nome   ENTER para confirmar");
        game.fontManager.smallFont.draw(game.batch, "Digite seu nome   ENTER para confirmar",
            (w - layout.width) / 2f, h * 0.28f);
    }

    private void renderConfirmacao(int w, int h) {
        game.fontManager.titleFont.setColor(new Color(1f, 0.85f, 0.1f, 1f));
        layout.setText(game.fontManager.titleFont, "Tudo certo?");
        game.fontManager.titleFont.draw(game.batch, "Tudo certo?",
            (w - layout.width) / 2f, h * 0.75f);

        // Resumo
        String[] infos = {
            "Nome:    " + nome,
            "Sexo:    " + sexoOpcoes[sexoIndex],
            "Origem:  " + origemOpcoes[origemIndex]
        };

        float startY = h * 0.62f;
        for (String info : infos) {
            game.fontManager.menuFont.setColor(Color.WHITE);
            layout.setText(game.fontManager.menuFont, info);
            game.fontManager.menuFont.draw(game.batch, info,
                (w - layout.width) / 2f, startY);
            startY -= 70f;
        }

        // Botões
        game.fontManager.menuFont.setColor(new Color(0.4f, 1f, 0.4f, 1f));
        layout.setText(game.fontManager.menuFont, "ENTER - Confirmar");
        game.fontManager.menuFont.draw(game.batch, "ENTER - Confirmar",
            (w - layout.width) / 2f, h * 0.35f);

        game.fontManager.menuFont.setColor(new Color(1f, 0.4f, 0.4f, 1f));
        layout.setText(game.fontManager.menuFont, "ESC - Recomecar");
        game.fontManager.menuFont.draw(game.batch, "ESC - Recomecar",
            (w - layout.width) / 2f, h * 0.28f);
    }

    private void handleInput() {
        switch (step) {
            case 0:
                if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
                    Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                    sexoIndex = sexoIndex == 0 ? 1 : 0;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) goToStep(1);
                break;

            case 1:
                if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
                    Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                    origemIndex = origemIndex == 0 ? 1 : 0;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) goToStep(2);
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) goToStep(0);
                break;

            case 2:
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) goToStep(1);
                break;

            case 3:
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    // Salva personagem e vai para o jogo
                    game.settings.save();
                    Gdx.app.log("Personagem", nome + " | " +
                        sexoOpcoes[sexoIndex] + " | " + origemOpcoes[origemIndex]);
                    // game.setScreen(new GameScreen(game)); // próximo passo
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    // Recomeça
                    nome        = "";
                    sexoIndex   = 0;
                    origemIndex = 0;
                    goToStep(0);
                }
                break;
        }
    }

    private void goToStep(int target) {
        fadingOut = true;
        nextStep  = target;
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
        Gdx.input.setInputProcessor(null);
    }
}