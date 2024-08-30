package aura_game.app.GameManager;

import aura_game.app.rework.AnimatedBlockEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Gère les entités de blocs animés
 */
public class AnimatedBlockEntityManager {

    List<AnimatedBlockEntity> animatedBlockEntitiesOnMap;

    public AnimatedBlockEntityManager() {
        animatedBlockEntitiesOnMap = new ArrayList<>();
    }

    public void update(float dt) {
        for (AnimatedBlockEntity animatedBlockEntity : animatedBlockEntitiesOnMap) {
            animatedBlockEntity.update(dt);
        }
    }

    public void add(AnimatedBlockEntity animatedBlockEntity) {
        animatedBlockEntitiesOnMap.add(animatedBlockEntity);
    }

}
