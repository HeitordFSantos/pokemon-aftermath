package com.aftermath.grpc;

import java.util.ArrayList;
import java.util.List;

public final class GameProto {

    // ── Direction ──────────────────────────────────────────────────────
    public enum Direction {
        NONE(0), UP(1), DOWN(2), LEFT(3), RIGHT(4);

        public final int value;
        Direction(int value) { this.value = value; }
    }

    // ── MoveRequest ────────────────────────────────────────────────────
    public static final class MoveRequest {
        public final String    playerId;
        public final Direction direction;

        private MoveRequest(String playerId, Direction direction) {
            this.playerId  = playerId;
            this.direction = direction;
        }

        public String    getPlayerId()  { return playerId; }
        public Direction getDirection() { return direction; }

        public static Builder newBuilder() { return new Builder(); }

        public static final class Builder {
            private String    playerId  = "";
            private Direction direction = Direction.NONE;

            public Builder setPlayerId(String id)      { this.playerId  = id;  return this; }
            public Builder setDirection(Direction dir) { this.direction = dir; return this; }
            public MoveRequest build() { return new MoveRequest(playerId, direction); }
        }
    }

    // ── PlayerState ────────────────────────────────────────────────────
    public static final class PlayerState {
        public final String  playerId;
        public final int     x, y;
        public final String  region;
        public final boolean blocked;

        private PlayerState(String playerId, int x, int y, String region, boolean blocked) {
            this.playerId = playerId;
            this.x        = x;
            this.y        = y;
            this.region   = region;
            this.blocked  = blocked;
        }

        public String  getPlayerId() { return playerId; }
        public int     getX()        { return x; }
        public int     getY()        { return y; }
        public String  getRegion()   { return region; }
        public boolean getBlocked()  { return blocked; }

        public static Builder newBuilder() { return new Builder(); }

        public static final class Builder {
            private String  playerId = "";
            private int     x = 0, y = 0;
            private String  region  = "";
            private boolean blocked = false;

            public Builder setPlayerId(String id)   { this.playerId = id;     return this; }
            public Builder setX(int x)              { this.x        = x;      return this; }
            public Builder setY(int y)              { this.y        = y;      return this; }
            public Builder setRegion(String region) { this.region   = region; return this; }
            public Builder setBlocked(boolean b)    { this.blocked  = b;      return this; }
            public PlayerState build() {
                return new PlayerState(playerId, x, y, region, blocked);
            }
        }
    }

    // ── WorldStateRequest ──────────────────────────────────────────────
    public static final class WorldStateRequest {
        public final String playerId;

        private WorldStateRequest(String playerId) { this.playerId = playerId; }

        public String getPlayerId() { return playerId; }

        public static Builder newBuilder() { return new Builder(); }

        public static final class Builder {
            private String playerId = "";
            public Builder setPlayerId(String id) { this.playerId = id; return this; }
            public WorldStateRequest build() { return new WorldStateRequest(playerId); }
        }
    }

    // ── WorldState ─────────────────────────────────────────────────────
    public static final class WorldState {
        public final String weather;
        public final String timeOfDay;
        public final int    reputation;

        private WorldState(String weather, String timeOfDay, int reputation) {
            this.weather    = weather;
            this.timeOfDay  = timeOfDay;
            this.reputation = reputation;
        }

        public String getWeather()    { return weather; }
        public String getTimeOfDay()  { return timeOfDay; }
        public int    getReputation() { return reputation; }

        public static Builder newBuilder() { return new Builder(); }

        public static final class Builder {
            private String weather    = "clear";
            private String timeOfDay  = "morning";
            private int    reputation = 0;

            public Builder setWeather(String w)   { this.weather    = w; return this; }
            public Builder setTimeOfDay(String t) { this.timeOfDay  = t; return this; }
            public Builder setReputation(int r)   { this.reputation = r; return this; }
            public WorldState build() {
                return new WorldState(weather, timeOfDay, reputation);
            }
        }
    }

    // ── SpawnRequest ───────────────────────────────────────────────────
    public static final class SpawnRequest {
        public final String region, habitat, weather;

        private SpawnRequest(String region, String habitat, String weather) {
            this.region  = region;
            this.habitat = habitat;
            this.weather = weather;
        }

        public String getRegion()  { return region; }
        public String getHabitat() { return habitat; }
        public String getWeather() { return weather; }

        public static Builder newBuilder() { return new Builder(); }

        public static final class Builder {
            private String region = "", habitat = "", weather = "";
            public Builder setRegion(String r)  { this.region  = r; return this; }
            public Builder setHabitat(String h) { this.habitat = h; return this; }
            public Builder setWeather(String w) { this.weather = w; return this; }
            public SpawnRequest build() {
                return new SpawnRequest(region, habitat, weather);
            }
        }
    }

    // ── PokemonSpawn ───────────────────────────────────────────────────
    public static final class PokemonSpawn {
        public final int    pokedexNumber;
        public final String name;
        public final float  encounterProb;
        public final String rarity;

        public PokemonSpawn(int pokedexNumber, String name,
                            float encounterProb, String rarity) {
            this.pokedexNumber = pokedexNumber;
            this.name          = name;
            this.encounterProb = encounterProb;
            this.rarity        = rarity;
        }

        public int    getPokedexNumber() { return pokedexNumber; }
        public String getName()          { return name; }
        public float  getEncounterProb() { return encounterProb; }
        public String getRarity()        { return rarity; }
    }

    // ── SpawnResponse ──────────────────────────────────────────────────
    public static final class SpawnResponse {
        public final List<PokemonSpawn> spawns;

        private SpawnResponse(List<PokemonSpawn> spawns) {
            this.spawns = spawns;
        }

        public List<PokemonSpawn> getSpawnsList() { return spawns; }

        public static Builder newBuilder() { return new Builder(); }

        public static final class Builder {
            private List<PokemonSpawn> spawns = new ArrayList<>();
            public Builder addSpawns(PokemonSpawn s) { this.spawns.add(s); return this; }
            public SpawnResponse build() { return new SpawnResponse(spawns); }
        }
    }
}