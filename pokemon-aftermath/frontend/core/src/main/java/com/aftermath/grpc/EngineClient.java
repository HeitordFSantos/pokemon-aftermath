package com.aftermath.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.badlogic.gdx.Gdx;

import java.util.concurrent.TimeUnit;

public class EngineClient {

    private final ManagedChannel channel;
    private final GameEngineGrpc.BlockingStub stub;

    private static final String HOST = "localhost";
    private static final int    PORT = 50051;

    public EngineClient() {
        channel = ManagedChannelBuilder
            .forAddress(HOST, PORT)
            .usePlaintext()
            .build();
        stub = GameEngineGrpc.newBlockingStub(channel);
        Gdx.app.log("gRPC", "Conectado ao servidor Python em " + HOST + ":" + PORT);
    }

    // ── Movimento ──────────────────────────────────────────────────────
    public GameProto.PlayerState movePlayer(String playerId, GameProto.Direction direction) {
        try {
            GameProto.MoveRequest request = GameProto.MoveRequest.newBuilder()
                .setPlayerId(playerId)
                .setDirection(direction)
                .build();
            return stub.movePlayer(request);
        } catch (Exception e) {
            Gdx.app.error("gRPC", "Erro ao mover jogador: " + e.getMessage());
            return null;
        }
    }

    // ── Estado do mundo ────────────────────────────────────────────────
    public GameProto.WorldState getWorldState(String playerId) {
        try {
            GameProto.WorldStateRequest request = GameProto.WorldStateRequest.newBuilder()
                .setPlayerId(playerId)
                .build();
            return stub.getWorldState(request);
        } catch (Exception e) {
            Gdx.app.error("gRPC", "Erro ao buscar mundo: " + e.getMessage());
            return null;
        }
    }

    // ── Spawns ─────────────────────────────────────────────────────────
    public GameProto.SpawnResponse getSpawns(String region, String habitat, String weather) {
        try {
            GameProto.SpawnRequest request = GameProto.SpawnRequest.newBuilder()
                .setRegion(region)
                .setHabitat(habitat)
                .setWeather(weather)
                .build();
            return stub.getSpawns(request);
        } catch (Exception e) {
            Gdx.app.error("gRPC", "Erro ao buscar spawns: " + e.getMessage());
            return null;
        }
    }

    // ── Encerrar conexão ───────────────────────────────────────────────
    public void shutdown() {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            Gdx.app.log("gRPC", "Conexao encerrada.");
        } catch (InterruptedException e) {
            Gdx.app.error("gRPC", "Erro ao encerrar conexao: " + e.getMessage());
        }
    }
}