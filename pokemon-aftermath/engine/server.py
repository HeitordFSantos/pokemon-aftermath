import sys
import os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

import grpc
import time
from concurrent import futures
from loguru import logger

from generated import game_pb2, game_pb2_grpc

# ── Estado inicial do jogador (simples por enquanto) ──
players = {}

class GameEngineServicer(game_pb2_grpc.GameEngineServicer):

    def MovePlayer(self, request, context):
        player_id = request.player_id

        # Inicializa jogador se for a primeira vez
        if player_id not in players:
            players[player_id] = {"x": 10, "y": 10, "region": "aurora_verde"}

        player = players[player_id]
        x, y = player["x"], player["y"]

        # Move baseado na direção
        direction = request.direction
        if direction == game_pb2.UP:
            y -= 1
        elif direction == game_pb2.DOWN:
            y += 1
        elif direction == game_pb2.LEFT:
            x -= 1
        elif direction == game_pb2.RIGHT:
            x += 1

        # Salva novo estado
        player["x"] = x
        player["y"] = y

        logger.info(f"[MOVE] {player_id} → ({x}, {y})")

        return game_pb2.PlayerState(
            player_id=player_id,
            x=x,
            y=y,
            region=player["region"],
            blocked=False
        )

    def GetWorldState(self, request, context):
        logger.info(f"[WORLD] pedido de estado por {request.player_id}")

        return game_pb2.WorldState(
            weather="clear",
            time_of_day="morning",
            reputation=0,
            nearby_npcs=[]
        )

    def GetSpawns(self, request, context):
        logger.info(f"[SPAWN] região={request.region} habitat={request.habitat}")

        # Por enquanto retorna um spawn fixo de teste
        spawn = game_pb2.PokemonSpawn(
            pokedex_number=1,
            name="Bulbasaur",
            encounter_prob=0.15,
        )
        return game_pb2.SpawnList(spawns=[spawn])
