package com.aftermath.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.aftermath.game.AftermathGame;

public class DesktopLauncher {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Pokémon Aftermath");
        config.setWindowedMode(960, 640);
        config.setForegroundFPS(60);

        new Lwjgl3Application(new AftermathGame(), config);
    }
}