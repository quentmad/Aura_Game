package aura_game.app.IA_Behavior;

import aura_game.app.*;
import aura_game.app.Objects.Entity;
import aura_game.app.Objects.IAEntity;

/**Logique de fuite en fonction d'une menace*/
public class FleeBehavior implements Behavior {
    /**On utilise la position de la menace (threat) pour calculer la direction opposée à celle-ci, puis déplace l'entité dans cette direction*/
    private Entity threat;
    

    /**Logique de fuite en fonction d'une menace*/
    public FleeBehavior(Entity threat) {
        this.threat = threat;
    }

    @Override
    public void act(IAEntity entity, float deltaTime) {
        // Logique de fuite en fonction d'une menace
        // Utilisez les méthodes de l'entité pour déterminer la menace et ajuster le mouvement en conséquence
        // Par exemple, calculez la direction opposée à la menace et déplacez-vous dans cette direction
        
        // Exemple :
        /*
        Vector2 threatPosition = threat.getPosition();
        Vector2 currentPosition = entity.getPosition();
        Vector2 direction = currentPosition.sub(threatPosition).nor();
        entity.move(direction);*/
    }

    /**Menace de l'entité*/
    public Entity getThreat() {
        return threat;
    }
}