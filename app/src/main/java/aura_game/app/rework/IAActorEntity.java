package aura_game.app.rework;

import aura_game.app.GameManager.Game;
import aura_game.app.IA_Behavior.Behavior;
import aura_game.app.IA_Behavior.BehaviorManager;
import aura_game.app.rework.TypeEnum.ActorEntityType;

import java.util.List;

public class IAActorEntity extends  ActorEntity{
    /**On utilise un compteur behaviorTimer pour contrôler l'intervalle de temps entre le changement de direction aléatoire de l'entité*/
    private float behaviorTimer;
    /**Défini à quelle fréquence on update l'action/direction de l'entité */
    private float behaviorInterval;

    Behavior behavior;

    BehaviorManager behaviorManager;


    /*public IAActorEntity(int life, int maxLife, Hitbox hitbox, PhysicsMovableComponent physics, int stature, List<Triplet<String,Integer,Integer>> deathLoot) {
        super(life, maxLife, hitbox, physics, stature, deathLoot, false);
    }*/



    public IAActorEntity(ActorEntityType type, int posX, int posY,      float behaviorInterval) {
        super(type, posX, posY);
        this.behaviorInterval = behaviorInterval;
        this.behaviorManager = new BehaviorManager();
        behaviorTimer = 0.0f;
        behavior = behaviorManager.selectBehavior(this);
    }


    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);
        physics().gridActorEntity().update(this);
    }


    public void update(float deltaTime) {
        behaviorTimer += deltaTime;
        if (behaviorTimer >= behaviorInterval) {
            //Mise à jour si besoin de behavior
            behavior = behaviorManager.selectBehavior(this); // Sélectionne le comportement approprié
            behaviorTimer = 0.0f; // Réinitialise le compteur
        }
        int old_y = this.posC().y();
        int old_x = this.posC().x();
        behavior.act(this, deltaTime); // Exécute l'action correspondante au comportement

        if(old_y != posC().y()){
            Game.getInstance().getRegion().interactionComponent().abstractObjectsOnGround().setNeedSort(true);
        }
    }

    /**
     * TODO: tempo: a terme direct mettre un randomIndex dans le tableau des noms d'actions possible (en fct de l'arme...) pour avoir changeAction (String action)
     * @param number
     * @return
     */
    public String getActionNameFromNumber(int number){
        return switch (number) {
            case 1 -> "Walk_U";
            case 2 -> "Walk_L";
            case 3 -> "Walk_R";
            case 4 -> "Walk_D";
            default -> null;//Pas de mouvement
        };

    }

}
