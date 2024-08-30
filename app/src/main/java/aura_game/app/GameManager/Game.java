package aura_game.app.GameManager;

import aura_game.app.*;
import aura_game.app.Craft.CraftLootManager;
import aura_game.app.CraftableBlock.CraftBlockManager;
import aura_game.app.Notifications.NotificationManager;
import aura_game.app.Weather.Sky;
import aura_game.app.rework.*;
import aura_game.app.rework.TypeEnum.ActorEntityType;

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
    private NotificationManager notificationManager;

    //TODO : regrouper les menus dans commonMenuInfo ??
    private InventoryMenu playerInventory;
    private CommonInfoMenu commonInfoMenu;
    private CraftingLootMenu craftingLootMenu;
    private CraftingBlockMenu craftingBlockMenu;
    private CapabilitiesMenu capabilitiesMenu;
    private MapMenu mapMenu;
    private StoryMenu storyMenu;
    private WheelManager wheelManager;
    private Region region; 
    private Player player;
    private Sky sky ;
    private InputHandler inputHandler;
    private MyInputProc inputProcessor;
    private PhysicsMovableComponent physicsMovableComponent;
    private PhysicsComponent physicsComponent;


    private static boolean isGameStarted;
    //private AudioManager audioManager;
    /**Largeur de l'écran*/
    public static final int screenWidth = 1280;
    /**Hauteur de l'écran*/
    public static final int screenHeight = 720;

    /**Largeur de la région actuelle*/
    public static int actualRegionHeight = 2880;//Hauteur L  'init autre part ??
    /**Hauteur de la région actuelle*/
    public static int actualRegionWidth = 3840;//Largeur

    private Game(){
        isGameStarted = false;
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
        wheelManager = WheelManager.getInstance();//PAS besoin de init car pas de dépendances
        craftingLootMenu = CraftingLootMenu.getInstance();
        craftingBlockMenu = CraftingBlockMenu.getInstance();
        capabilitiesMenu = CapabilitiesMenu.getInstance();
        mapMenu = MapMenu.getInstance();
        storyMenu = StoryMenu.getInstance();
        region = new Region("map_AuraDark2", "mappix_CollisionX2");
        sky = new Sky();
        physicsMovableComponent = region.physicsMovableComponent();
        physicsComponent = region.physicsMovableComponent().get();
        player = new Player(ActorEntityType.player,10,10);//4
        commonInfoMenu = CommonInfoMenu.getInstance();
        inputHandler = InputHandler.getInstance();
        inputProcessor  = MyInputProc.getInstance();
        notificationManager = NotificationManager.getInstance();
        CraftLootManager craftLootManager = new CraftLootManager();
        CraftBlockManager craftBlockManager = new CraftBlockManager();

        this.loadManager = new LoadManager();
        this.updateManager = new UpdateManager();
        this.renderManager = new RenderManager();

        // Injection de dépendances
        this.playerInventory.initialize(player);
        this.craftingLootMenu.initialize(craftLootManager, playerInventory);
        this.craftingBlockMenu.initialize(craftBlockManager, playerInventory);
        this.loadManager.initialize(updateManager, region, player, playerInventory, inputHandler, craftingLootMenu, craftingBlockMenu,craftBlockManager, wheelManager, physicsMovableComponent, physicsComponent);
        this.updateManager.initialize(region, player, wheelManager, commonInfoMenu, notificationManager);
        this.renderManager.initialize(region, player, playerInventory, craftingLootMenu, craftingBlockMenu,capabilitiesMenu,mapMenu,storyMenu, commonInfoMenu, craftBlockManager,notificationManager,sky, updateManager, wheelManager);
        this.commonInfoMenu.initialize(player);
        this.player.initialize(craftBlockManager);
        player.stateComponant().changeAction("Idle", Orientation.SOUTH);//init
        //test();

    }

    public InventoryMenu getInventory(){
        return playerInventory;
    }

    public WheelManager getWheelManager(){
        return wheelManager;
    }

    public CraftingLootMenu getCraftingLootMenu(){
        return craftingLootMenu;
    }

    public CraftingBlockMenu getCraftingBlockMenu(){
        return craftingBlockMenu;
    }

    public CommonInfoMenu getCommonMenuManager(){
        return commonInfoMenu;
    }

    public Region getRegion(){
        return region;
    }
    
    public Player getPlayer(){
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

    public PhysicsMovableComponent getPhysicsMovableComponent(){
        return physicsMovableComponent;
    }

    public PhysicsComponent getPhysicsComponent(){
        return physicsComponent;
    }

}
