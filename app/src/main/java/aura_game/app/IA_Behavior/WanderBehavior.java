package aura_game.app.IA_Behavior;
import aura_game.app.*;
//import aura_game.app.VisualGDX.Direction;
import aura_game.app.Objects.IAEntity;

/**Logique de déplacement aléatoire pour l'errance*/
public class WanderBehavior implements Behavior {
    /**On utilise un compteur wanderTimer pour contrôler l'intervalle de temps entre le changement de direction aléatoires de l'entité*/
    private float wanderTimer;
    /**Défini a quelle fréquence on update l'action/direction de l'entité */
    private float wanderInterval;
    
    /**Logique de déplacement aléatoire pour l'errance*/
    public WanderBehavior(float wanderInterval) {
        this.wanderInterval = wanderInterval;
        wanderTimer = 0.0f;
    }


    @Override
    public void act(IAEntity entity, float deltaTime) {
       wanderTimer += deltaTime;
        if (wanderTimer >= wanderInterval) {
            // Logique de déplacement aléatoire pour l'errance
            // Utilisez les méthodes de l'entité pour modifier sa position, sa direction, etc.
            //random puis link a une direction/action
            //System.out.println((int) (Math.random() * 5) + 1);
            entity.changeAction(entity.getActionNameFromNumber((int) (Math.random() * 6) + 1));//Dans le cas ou le nom est null alors l'action sera "null"
            wanderTimer = 0.0f; // Réinitialise le compteur
        }
        //entity.setDirection(Direction.UP);
        entity.doAction();
        //System.out.println(wanderTimer);
    }


}