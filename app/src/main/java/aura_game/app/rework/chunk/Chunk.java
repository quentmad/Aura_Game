package aura_game.app.rework.chunk;

import aura_game.app.rework.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
    static final int CHUNK_SIZE = 16;
    private Tile[][] tiles;
    private List<AbstractEntity> entities;

    public Chunk() {
        tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
        entities = new ArrayList<>();
        // Initialiser les tuiles et entités
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public void addEntity(AbstractEntity entity) {
        entities.add(entity);
    }

    public List<AbstractEntity> getEntities() {
        return entities;
    }

    class Tile{}
}

