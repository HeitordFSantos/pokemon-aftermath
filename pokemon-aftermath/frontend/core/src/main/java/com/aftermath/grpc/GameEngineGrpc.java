package com.aftermath.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractBlockingStub;
import io.grpc.MethodDescriptor;
import io.grpc.ServiceDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.CallOptions;
import io.grpc.Channel;

public class GameEngineGrpc {

    public static final String SERVICE_NAME = "aftermath.GameEngine";

    // ── Métodos do serviço ─────────────────────────────────────────────
    public static io.grpc.MethodDescriptor<GameProto.MoveRequest, GameProto.PlayerState>
        getMovePlayerMethod() {
        return io.grpc.MethodDescriptor.<GameProto.MoveRequest, GameProto.PlayerState>newBuilder()
            .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
            .setFullMethodName("aftermath.GameEngine/MovePlayer")
            .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                GameProto.MoveRequest.getDefaultInstance()))
            .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                GameProto.PlayerState.getDefaultInstance()))
            .build();
    }

    public static io.grpc.MethodDescriptor<GameProto.WorldStateRequest, GameProto.WorldState>
        getGetWorldStateMethod() {
        return io.grpc.MethodDescriptor.<GameProto.WorldStateRequest, GameProto.WorldState>newBuilder()
            .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
            .setFullMethodName("aftermath.GameEngine/GetWorldState")
            .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                GameProto.WorldStateRequest.getDefaultInstance()))
            .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                GameProto.WorldState.getDefaultInstance()))
            .build();
    }

    public static io.grpc.MethodDescriptor<GameProto.SpawnRequest, GameProto.SpawnResponse>
        getGetSpawnsMethod() {
        return io.grpc.MethodDescriptor.<GameProto.SpawnRequest, GameProto.SpawnResponse>newBuilder()
            .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
            .setFullMethodName("aftermath.GameEngine/GetSpawns")
            .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                GameProto.SpawnRequest.getDefaultInstance()))
            .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                GameProto.SpawnResponse.getDefaultInstance()))
            .build();
    }

    // ── Stub bloqueante (síncrono) ─────────────────────────────────────
    public static BlockingStub newBlockingStub(Channel channel) {
        return new BlockingStub(channel, CallOptions.DEFAULT);
    }

    public static final class BlockingStub extends
        AbstractBlockingStub<BlockingStub> {

        private BlockingStub(Channel channel, CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected BlockingStub build(Channel channel, CallOptions callOptions) {
            return new BlockingStub(channel, callOptions);
        }

        public GameProto.PlayerState movePlayer(GameProto.MoveRequest request) {
            return ClientCalls.blockingUnaryCall(
                getChannel(), getMovePlayerMethod(), getCallOptions(), request);
        }

        public GameProto.WorldState getWorldState(GameProto.WorldStateRequest request) {
            return ClientCalls.blockingUnaryCall(
                getChannel(), getGetWorldStateMethod(), getCallOptions(), request);
        }

        public GameProto.SpawnResponse getSpawns(GameProto.SpawnRequest request) {
            return ClientCalls.blockingUnaryCall(
                getChannel(), getGetSpawnsMethod(), getCallOptions(), request);
        }
    }
}