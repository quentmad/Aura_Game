package aura_game.app.IA_Behavior;

import aura_game.app.rework.AbstractEntity;
import aura_game.app.rework.IAActorEntity;

/**Logique de poursuite de la cible la plus proche*/
public class ChaseBehavior implements Behavior {
    /**On utilise la position de la cible (target) pour calculer la direction vers celle-ci, puis déplace l'entité dans cette direction.*/
    private AbstractEntity target;

    /**Logique de poursuite de la cible la plus proche*/
    public ChaseBehavior(AbstractEntity target) {
        this.target = target;
    }

    @Override
    public void act(IAActorEntity entity, float deltaTime) {
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

    public AbstractEntity getTarget() {
        return target;
    }



}
