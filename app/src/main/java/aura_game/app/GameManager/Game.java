package aura_game.app.GameManager;

import com.badlogic.gdx.Audio;

import aura_game.app.CraftingMenu;
import aura_game.app.InputHandler;
import aura_game.app.InventoryMenu;
import aura_game.app.MyInputProc;
import aura_game.app.Region;
import aura_game.app.WheelMenus;
import aura_game.app.Craft.CraftManager;
import aura_game.app.Objects.PlayableEntity;
import aura_game.app.Type.EntityType;
import aura_game.app.Weather.Sky;

/**
 * Classe qui orchestre la gestion du jeu.
 * Responsable de l'initialisation des gestionnaires, de la création des objets de jeu et de la gestion du cycle de vie du jeu.
 */
public class Game {
    //TODO Créer un map qui est une liste de Region + un Sky
    private static Game instance;
    private LoadManager loadManager;
    private UpdateManager updateManager;
    private RenderManager renderManager;

    private InventoryMenu playerInventory;
    private CraftingMenu crafting;
    private WheelMenus wheelMenus;
    private Region region; 
    private PlayableEntity player;
    private Sky sky ;
    private InputHandler inputHandler;
    private MyInputProc inputProcessor; 
    private boolean isGameStarted;
    //private AudioManager audioManager;
    /**Largeur de l'écran*/
    private final int screenWidth;
    /**Hauteur de l'écran*/
    private final int screenHeight;

    private Game(){
        isGameStarted = false;
        this.screenWidth = 1200;
        this.screenHeight = 600;
    }

    /**
     * Obtient une instance unique de la classe Game (Singleton).
     *
     * @return L'instance de Game.
     */
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    /**
     * Démarre le jeu en initialisant les gestionnaires, les objets de jeu et les dépendances.
     * Cette méthode met en place l'environnement de jeu et prépare les éléments nécessaires pour démarrer le jeu.
     */
    public void start() {
        playerInventory = InventoryMenu.getInstance();
        wheelMenus = WheelMenus.getInstance();//PAS besoin de init car pas de dépendances
        crafting = CraftingMenu.getInstance();
        region = new Region("map_AuraDark2", "mappix_CollisionX2");
        player = new PlayableEntity(EntityType.player, 5);//4
        sky = new Sky();
        inputHandler = InputHandler.getInstance();
        inputProcessor  = MyInputProc.getInstance();
        CraftManager craftManager = new CraftManager();

        this.loadManager = new LoadManager();
        this.updateManager = new UpdateManager();
        this.renderManager = new RenderManager();

        // Injection de dépendances
        this.playerInventory.initialize(player);
        this.crafting.initialize(craftManager, playerInventory);
        this.loadManager.initialize(updateManager, region, player, playerInventory, inputHandler,crafting, wheelMenus);
        this.updateManager.initialize(region, player,wheelMenus);
        this.renderManager.initialize(region, player, playerInventory,crafting, sky, updateManager,wheelMenus);

        

    }

    public InventoryMenu getInventory(){
        return playerInventory;
    }

    public WheelMenus getWheelMenus(){
        return wheelMenus;
    }

    public CraftingMenu getCrafting(){
        return crafting;
    }

    public Region getRegion(){
        return region;
    }
    
    public PlayableEntity getPlayer(){
        return player;
    }

    public Sky getSky(){
        return sky;
    }

    public InputHandler getInputHandler(){
        return inputHandler;
    }
    
    public MyInputProc getMyInputProc(){
        return inputProcessor;
    }

    public UpdateManager getUpdateManager() {
        return updateManager;
    }

    public LoadManager getLoadManager() {
        return loadManager;
    }

    public RenderManager getRenderManager() {
        return renderManager;
    }
    
    /**@return true si le jeu a fini d'etre initialisé/chargé, sinon false */
    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(Boolean v){
        isGameStarted = v;
    }

    public int getScreenWidth(){
        return screenWidth;
    }

    public int getScreenHeight(){
        return screenHeight;
    }

}
