package aura_game.app.GameManager;

import aura_game.app.CraftingBlockMenu;
import aura_game.app.LootManager;
import aura_game.app.Notifications.NotificationManager;
import aura_game.app.Weather.RainManager;
import aura_game.app.WheelManager;
import aura_game.app.rework.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import static java.lang.System.currentTimeMillis;


//TODO: creer une classe SaveManager pour gérer la sauvegarde et le chargement du jeu
/**
 * Responsable des mises à jour du jeu, y compris la gestion des entités et de l'inventaire.
 */
public class UpdateManager {

    private Region region;
    private Player player;
    private String activeMenu;
    private CommonInfoMenu commonInfoMenu;
    private WheelManager wheelManager;
    private RainManager rainManager;
    private NotificationManager notificationManager;
    //Texture instance unique
    private Texture bar_font;
    private Texture bar_red;

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
    public void initialize(Region region, Player player, WheelManager wheelManager, RainManager rainManager,CommonInfoMenu commonInfoMenu, NotificationManager notificationManager) {
        this.bar_font =  new Texture(Gdx.files.internal("src/main/resources/UI/bar_font.png"));
        this.bar_red =  new Texture(Gdx.files.internal("src/main/resources/UI/bar_red.png"));
        this.region = region;
        this.player = player;
        this.wheelManager = wheelManager;
        this.rainManager = rainManager;
        this.commonInfoMenu = commonInfoMenu;
        this.notificationManager = notificationManager;

    }
    /**Mises à jour nécessaires */
    public void update(float dt) {

        player.stateComponant().executeCurrentAction(player);
        updateEntities();
        notificationManager.update(0.1f);
        // Mettre à jour les behavior et positions des entités du jeu
        // Autres mises à jour nécessaires
        region.interactionComponent().update(dt);
        LootManager.getInstance().update(2);
        // Mettre à jour la pluie, utilisation du lock pour éviter les problèmes de concurrence et ne permettre qu'une seul mise à jour àpar tick
        synchronized (rainManager.lock()) {
            rainManager.lock().notify();
        }
        //Tri de la liste ObjectsOnMap
        if(region.interactionComponent().abstractObjectsOnGround().needSort()) {
            region.interactionComponent().abstractObjectsOnGround().sort();
            System.out.println("est trié : " + region.interactionComponent().abstractObjectsOnGround().isSorted());
            //Liste triée: nouveau tri pas nécessaire tant que de nouveaux mouvements d'entités n'auront pas lieu
            region.interactionComponent().abstractObjectsOnGround().setNeedSort(false);
        }
    }

    /**
     * Mettre à jour les entités du jeu (mouvement, animation, etc.)
     */
    private void updateEntities() {
        for(AbstractObject obj : region.interactionComponent().abstractObjectsOnGround().objects()) {
            if(obj instanceof IAActorEntity){
            ((IAActorEntity) obj).update(0.1f);
            }
        }

    }

    /**
     * Inverse le menu actif entre le menu de jeu et le menu en parametre (ouvre le menu en parametre)
     */
    public void invertActiveMenu(CommonInfoMenu.Menu menu){
        if(menu.name().equals(activeMenu)){
            activeMenu = "game";
            commonInfoMenu.setCurrentMenu(null);//TEST

        }else{
            activeMenu = menu.name();
            commonInfoMenu.setCurrentMenu(menu);//TEST TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO
        }
    }

    /**Pour ceux qui ne sont pas dans MENU, a changer...*/
    public void invertActiveMenu(String othermenu){
        if(othermenu.equals(activeMenu)){
            activeMenu = "game";
            commonInfoMenu.setCurrentMenu(null);//TEST

        }else{
            activeMenu = othermenu;
            commonInfoMenu.setCurrentMenu(null);//TEST TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO
        }
    }



    public void setActiveMenu(String menu){
        if(menu.equals("placementBlock"))commonInfoMenu.setCurrentMenu(null);
        activeMenu = menu;
    }

    /**
     * Inverse le menu actif entre le menu de jeu et le menu de la roue en parametre
     * @param wheel meleeWeapons ou rangedWeapons
     * Appel la méthode {@code setCurrentOpenWheel} de {@code wheelManager}
     */
    public void invertCurrentOpenMenuWheel(String wheel){
        commonInfoMenu.setCurrentMenu(null);
        if(activeMenu.equals("wheel") ){
            activeMenu = "game";
            wheelManager.setCurrentOpenWheel("");

        }else{
            activeMenu = "wheel";
            wheelManager.setCurrentOpenWheel(wheel);
        }
    }

    public String getActiveMenu(){
        return activeMenu;
    }

}
