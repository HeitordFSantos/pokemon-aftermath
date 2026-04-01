package com.aftermath.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameSettings {

    private static final String PREFS_NAME = "pokemon_aftermath_settings";
    private Preferences prefs;

    // ── Audio ──────────────────────────────
    public int musicVolume  = 75;
    public int sfxVolume    = 60;

    // ── Gameplay ───────────────────────────
    public int  textSpeed       = 1;  // 0=Lenta 1=Normal 2=Rapida
    public boolean animatedBattle = true;
    public int  difficulty      = 1;  // 0=Facil 1=Normal 2=Dificil

    // ── Tela ───────────────────────────────
    public boolean fullscreen = false;
    public int  brightness    = 50;

    // ── Controles ──────────────────────────
    public int keyConfirm = com.badlogic.gdx.Input.Keys.Z;
    public int keyCancel  = com.badlogic.gdx.Input.Keys.X;
    public int keyMenu    = com.badlogic.gdx.Input.Keys.ENTER;

    public GameSettings() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
        load();
    }

    public void load() {
        musicVolume     = prefs.getInteger("musicVolume",  75);
        sfxVolume       = prefs.getInteger("sfxVolume",    60);
        textSpeed       = prefs.getInteger("textSpeed",     1);
        animatedBattle  = prefs.getBoolean("animatedBattle", true);
        difficulty      = prefs.getInteger("difficulty",    1);
        fullscreen      = prefs.getBoolean("fullscreen",  false);
        brightness      = prefs.getInteger("brightness",   50);
        keyConfirm      = prefs.getInteger("keyConfirm",  com.badlogic.gdx.Input.Keys.Z);
        keyCancel       = prefs.getInteger("keyCancel",   com.badlogic.gdx.Input.Keys.X);
        keyMenu         = prefs.getInteger("keyMenu",     com.badlogic.gdx.Input.Keys.ENTER);
    }

    public void save() {
        prefs.putInteger("musicVolume",   musicVolume);
        prefs.putInteger("sfxVolume",     sfxVolume);
        prefs.putInteger("textSpeed",     textSpeed);
        prefs.putBoolean("animatedBattle", animatedBattle);
        prefs.putInteger("difficulty",    difficulty);
        prefs.putBoolean("fullscreen",    fullscreen);
        prefs.putInteger("brightness",    brightness);
        prefs.putInteger("keyConfirm",    keyConfirm);
        prefs.putInteger("keyCancel",     keyCancel);
        prefs.putInteger("keyMenu",       keyMenu);
        prefs.flush();
    }

    // ── Helpers de texto ───────────────────
    public String getTextSpeedLabel() {
        switch (textSpeed) {
            case 0: return "Lenta";
            case 2: return "Rapida";
            default: return "Normal";
        }
    }

    public String getDifficultyLabel() {
        switch (difficulty) {
            case 0: return "Facil";
            case 2: return "Dificil";
            default: return "Normal";
        }
    }

    public String getKeyName(int keycode) {
        return com.badlogic.gdx.Input.Keys.toString(keycode);
    }
}