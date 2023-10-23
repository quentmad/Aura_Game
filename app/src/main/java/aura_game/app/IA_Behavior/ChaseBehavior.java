package aura_game.app.IA_Behavior;

import aura_game.app.*;
import aura_game.app.Objects.Entity;
import aura_game.app.Objects.IAEntity;
/**Logique de poursuite de la cible la plus proche*/
public class ChaseBehavior implements Behavior {
    /**On utilise la position de la cible (target) pour calculer la direction vers celle-ci, puis déplace l'entité dans cette direction.*/
    private Entity target;
    
    /**Logique de poursuite de la cible la plus proche*/
    public ChaseBehavior(Entity target) {
        this.target = target;
    }

    @Override
    public void act(IAEntity entity, float deltaTime) {
        // Logique de poursuite de la cible la plus proche
        // Utilisez les méthodes de l'entité pour déterminer la cible et ajuster le mouvement en conséquence
        // Par exemple, calculez la direction vers la cible et déplacez-vous dans cette direction
        
        // Exemple :
        /*
        Vector2 targetPosition = target.getPosition();
        Vector2 currentPosition = entity.getPosition();
        Vector2 direction = targetPosition.sub(currentPosition).nor();
        entity.move(direction);
        */
    }

    public Entity getTarget() {
        return target;
    }



}