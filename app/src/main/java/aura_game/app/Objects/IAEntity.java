package aura_game.app.Objects;
import aura_game.app.IA_Behavior.Behavior;
import aura_game.app.IA_Behavior.BehaviorManager;
import aura_game.app.Type.EntityType;
//TODO: hitbox profil et hitbox face a changer en fonction de la direction/action (2 types)
/**
 * Une IAEntity se déplace et agit selon Behavior
 */
public class IAEntity extends Entity{
    /**On utilise un compteur behaviorTimer pour contrôler l'intervalle de temps entre le changement de direction aléatoire de l'entité*/
    private float behaviorTimer;
    /**Défini à quelle fréquence on update l'action/direction de l'entité */
    private float behaviorInterval;

    Behavior behavior;

    /**
     * Permet de gérer le comportement de l'IA
     */
    private BehaviorManager behaviorManager;

    public IAEntity(EntityType typeSheet, int speed,float behaviorInterval, int initX, int initY) {
        super(typeSheet, speed, initX, initY);
                behaviorManager = new BehaviorManager();
                this.behaviorInterval = behaviorInterval;
                behaviorTimer = 0.0f;
                behavior = behaviorManager.selectBehavior(this); 
    }

    public void update(float deltaTime) {
        behaviorTimer += deltaTime;
        if (behaviorTimer >= behaviorInterval) {
            //Mise à jour si besoin de behavior
            behavior = behaviorManager.selectBehavior(this); // Sélectionne le comportement approprié
            behaviorTimer = 0.0f; // Réinitialise le compteur
        }
        int old_y = this.getPosC_Y();
        int old_x = this.getPosC_X();
        behavior.act(this, deltaTime); // Exécute l'action correspondante au comportement

        if(old_y != getPosC_Y()){
            //this.updateEntityPositionInRenderList();
            if(!getActualRegion().getBasicObjectsOnRegionNeedSort()){
                //Il y a eu au moins un mouvement: un sort() de la liste sera nécessaire avant de render
                getActualRegion().setBasicObjectsOnRegionNeedSort(true);
            }
        }
        /*if(old_x != getPosC_X() || old_y != getPosC_Y()){
            getActualRegion().getGridIA().update(this);
        }*/
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
