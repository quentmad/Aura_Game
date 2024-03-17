package aura_game.app.GameManager;

import aura_game.app.Grid;
import aura_game.app.LootManager;
import aura_game.app.Region;
import aura_game.app.WheelMenus;
import aura_game.app.Objects.BasicObject;
import aura_game.app.Objects.IAEntity;
import aura_game.app.Objects.PlayableEntity;


//TODO: creer une classe SaveManager pour gérer la sauvegarde et le chargement du jeu
/**
 * Responsable des mises à jour du jeu, y compris la gestion des entités et de l'inventaire.
 */
public class UpdateManager {

    private Region region;
    private PlayableEntity player;
    private String activeMenu;
    private WheelMenus wheelMenus;
    //RETIRER
    private final Grid gridTest = new Grid(500);

    public UpdateManager(){
        this.activeMenu = "game";
    }


    /**
     * Initialise les dépendances nécessaires pour le gestionnaire de mise à jour. Cette méthode joue un rôle crucial
     * dans l'application du modèle de conception "Pattern Observer" (Observateur) associé au principe d'"Injection de Dépendances".
     * Grâce à ce modèle et à ce principe, les classes peuvent observer et interagir les unes avec les autres sans créer
     * de dépendances directes, ce qui contribue à une structure de code modulaire et maintenable.
     * 
     * La méthode initialise les objets et attributs nécessaires pour la mise à jour du jeu. Elle configure les éléments
     * requis tels que la région et le joueur... Cette initialisation prépare le terrain pour une gestion efficace
     * de la logique de jeu et des interactions avec les entrées du joueur.
     * 
     * @param region La région du jeu pour la mise à jour.
     * @param player Le joueur jouable de la partie.
     */
    public void initialize(Region region, PlayableEntity player, WheelMenus wheelMenus) {
        this.region = region;
        this.player = player;
        this.wheelMenus = wheelMenus;

    }
    /**Mises à jour nécessaires */
    public void update() {

        player.getEntityStateMachine().executeCurrentAction(player);
        updateEntities(); // Mettre à jour les behavior et positions des entités du jeu
        // Autres mises à jour nécessaires
        LootManager.getInstance().update(2);
    }

    /**
     * Mettre à jour les entités du jeu (mouvement, animation, etc.)
     */
    private void updateEntities() {
        for(BasicObject obj : region.getBasicObjectsOnRegion()) {
            if(obj instanceof IAEntity){
            ((IAEntity) obj).update(0.1f);
            }
        }
        if(region.getBasicObjectsOnRegionNeedSort()){
            region.getBasicObjectsOnRegion().sort((obj1, obj2) -> {
                float zProf1 = obj1.getZProf();
                float zProf2 = obj2.getZProf();

                if (zProf1 > zProf2) {
                    return -1; // Tri décroissant
                } else if (zProf1 < zProf2) {
                    return 1;
                } else {
                    return 0;
                }
            });
            //Liste triée: nouveau tri pas nécessaire tant que de nouveaux mouvements d'entités n'auront pas lieu
            region.setBasicObjectsOnRegionNeedSort(false);
        }

    }

    public void setActiveMenu(String menu){
        this.activeMenu = menu;
    }


    public void invertActiveMenu(String menu){
        if(menu.equals(activeMenu)){
            activeMenu = "game";
        }else{
            activeMenu = menu;
        }
    }

    public void invertActiveMenuWheel(String wheel){
        if(activeMenu.equals("wheel") ){
            activeMenu = "game";
            wheelMenus.setActiveWheel("");

        }else{
            activeMenu = "wheel";
            wheelMenus.setActiveWheel(wheel);
        }
    }

    public String activeMenu() {
        return activeMenu;
    }

}
