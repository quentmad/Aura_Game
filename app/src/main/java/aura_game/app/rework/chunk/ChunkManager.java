package aura_game.app.rework.chunk;

import aura_game.app.rework.Player;
import aura_game.app.rework.Point;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChunkManager {
    //tableau de tt les chunks  de la map

    private Map<Point, Chunk> loadedChunks;
    private int viewDistance;
    private Player player;

    public ChunkManager(Player player, int viewDistance) {
        this.player = player;
        this.viewDistance = viewDistance;
        loadedChunks = new HashMap<>();
    }

    public void updateChunks() {
        int playerChunkX = player.posC().x() / Chunk.CHUNK_SIZE;
        int playerChunkY = player.posC().y() / Chunk.CHUNK_SIZE;

        for (int x = playerChunkX - viewDistance; x <= playerChunkX + viewDistance; x++) {
            for (int y = playerChunkY - viewDistance; y <= playerChunkY + viewDistance; y++) {
                Point chunkPoint = new Point(x, y);
                if (!loadedChunks.containsKey(chunkPoint)) {
                    loadedChunks.put(chunkPoint, loadChunk(x, y));
                }
            }
        }

        // Décharger les chunks en dehors de la vue
        Iterator<Map.Entry<Point, Chunk>> iterator = loadedChunks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Point, Chunk> entry = iterator.next();
            Point chunkPoint = entry.getKey();
            if (Math.abs(chunkPoint.x() - playerChunkX) > viewDistance || Math.abs(chunkPoint.y() - playerChunkY) > viewDistance) {
                iterator.remove();
            }
        }
    }

    private Chunk loadChunk(int x, int y) {
        // Logique de chargement du chunk, pourrait inclure la lecture depuis un fichier
        return new Chunk();
    }

    public Chunk getChunk(int x, int y) {
        return loadedChunks.get(new Point(x, y));
    }

    public void render(float delta) {
        // Mettre à jour la position du joueur
        //player.update(delta);

        // Mettre à jour les chunks chargés
        updateChunks();

        // Dessiner les chunks visibles
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                Chunk chunk = getChunk(player.posC().x() / Chunk.CHUNK_SIZE + x, player.posC().y() / Chunk.CHUNK_SIZE + y);
                if (chunk != null) {
                    renderChunk(chunk);
                }
            }
        }
    }

    private void renderChunk(Chunk chunk) {
        // Logique de rendu des tuiles et entités du chunk
    }
}

