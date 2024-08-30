package aura_game.app.IA_Behavior;

import aura_game.app.GameManager.Game;
import aura_game.app.rework.AbstractEntity;
import aura_game.app.rework.IAActorEntity;
import aura_game.app.rework.Player;
import aura_game.app.rework.Region;

/**Logique de sélection du comportement approprié pour l'entité*/
public class BehaviorManager {




    /**Logique de sélection du comportement approprié pour l'entité*/
    public Behavior selectBehavior(IAActorEntity entity) {
        // Logique de sélection du comportement approprié pour l'entité
        // Peut être basée sur l'état de l'entité, son environnement, les interactions, etc.
        
        // Exemple :
        Player player = Game.getInstance().getPlayer();
        Region map = Game.getInstance().getRegion();
        //Grid quadtree = map.getGridItem();
        AbstractEntity closestThreat = player;//entity.findClosestEntity(quadtree); // Méthode pour trouver la menace la plus proche
        ///TODO TODO tempo TODO TODO tempo TODO TODOTODO TODOTODO TODO
        // Choix du comportement en fonction des conditions
        if (closestThreat != null && entity.isInDanger()) {
            return new FleeBehavior(closestThreat);
        } else if (player != null && entity.canSee()/*player*/) {
            return new ChaseBehavior(player);//Ou ClosestThreat
        } else {
            return new WanderBehavior(3.0f); // Interval de 3 secondes pour l'errance
        }
    }




}