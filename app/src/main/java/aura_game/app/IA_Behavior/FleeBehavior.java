package aura_game.app.IA_Behavior;

import aura_game.app.rework.AbstractEntity;
import aura_game.app.rework.IAActorEntity;

/**Logique de fuite en fonction d'une menace*/
public class FleeBehavior implements Behavior {
    /**On utilise la position de la menace (threat) pour calculer la direction opposée à celle-ci, puis déplace l'entité dans cette direction*/
    private AbstractEntity threat;
    

    /**Logique de fuite en fonction d'une menace*/
    public FleeBehavior(AbstractEntity threat) {
        this.threat = threat;
    }


    //generate a method to do the flee behavior
    @Override
    public void act(IAActorEntity entity, float deltaTime) {
        /*
        // Logique de fuite en fonction d'une menace
        if (threat != null) {
            // Calcule la direction opposée à la menace
            float angle = MathUtils.atan2(threat.getPosC_Y() - entity.getPosC_X(), threat.getCenterX() - entity.getCenterX());
            angle = angle * MathUtils.radiansToDegrees;
            angle += 180;
            angle = angle % 360;
            // Déplace l'entité dans cette direction
            entity.setDirection(Direction.getDirectionFromAngle(angle));
            entity.doAction();
        }
        */

    }

    /**
     * Calcule la direction opposée à la menace
     * @param angle
     * @return
     */
    /*
    public static Direction getDirectionFromAngle(float angle) {
        if (angle >= 45 && angle < 135) {
            return Direction.UP;
        } else if (angle >= 135 && angle < 225) {
            return Direction.LEFT;
        } else if (angle >= 225 && angle < 315) {
            return Direction.DOWN;
        } else {
            return Direction.RIGHT;
        }
    }
    */



    /**Menace de l'entité*/
    public AbstractEntity getThreat() {
        return threat;
    }
}