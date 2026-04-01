package com.aftermath.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontManager {

    public BitmapFont titleFont;    // grande, para títulos
    public BitmapFont menuFont;     // médio, para opções de menu
    public BitmapFont smallFont;    // pequeno, para textos e diálogos

    private FreeTypeFontGenerator generator;

    // Caracteres suportados (inclui português)
    private static final String CHARS =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
        "0123456789!?.,;:'\"-_()[]{}/@#$%&*+ " +
        "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝß" +
        "àáâãäåæçèéêëìíîïðñòóôõöøùúûüýÿ" +
        "►▼▲◄";

    public FontManager() {
        generator = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/Pokemon_classic.ttf")
        );

        titleFont = createFont(30, Color.WHITE);
        menuFont  = createFont(22, Color.WHITE);
        smallFont = createFont(16, Color.WHITE);
    }

    private BitmapFont createFont(int size, Color color) {
        FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.size      = size;
        param.color     = color;
        param.characters = CHARS;
        param.borderWidth = 1.5f;
        param.borderColor = Color.BLACK;
        param.shadowOffsetX = 2;
        param.shadowOffsetY = -2;
        param.shadowColor = new Color(0, 0, 0, 0.5f);
        return generator.generateFont(param);
    }

    public void dispose() {
        generator.dispose();
        titleFont.dispose();
        menuFont.dispose();
        smallFont.dispose();
    }
}