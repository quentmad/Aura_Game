package aura_game.app.GameManager;

import aura_game.app.*;
import aura_game.app.CraftableBlock.CraftBlockManager;
import aura_game.app.Notifications.NotificationManager;
import aura_game.app.rework.*;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aura_game.app.Weather.*;

/**
 * Responsable de l'affichage/render sur l'écran.
 */
public class RenderManager {

    private Region region;
    private Player player;
    private InventoryMenu playerInventory;
    private CraftingLootMenu craftingLootMenu;
    private CraftingBlockMenu craftingBlockMenu;
    private CapabilitiesMenu capabilitiesMenu;
    private CraftBlockManager craftBlockManager;
    private MapMenu mapMenu;
    private StoryMenu storyMenu;
    private CommonInfoMenu commonInfoMenu;
    private WheelManager wheelManager;
    private RainManager rainManager;
    private Sky sky;
    private UpdateManager updateManager;
    /**Lot de sprites pour les graphiques 2D */
    private SpriteBatch batch;
    /**Pour dessiner des formes */
    private LootManager lootManager;
    private NotificationManager notificationManager;
    /**
     * Initialise les dépendances nécessaires pour le gestionnaire de rendu. Cette méthode joue un rôle clé dans
     * l'application du modèle de conception "Pattern Observer" (Observateur) combiné au principe d'"Injection de Dépendances".
     * L'utilisation de ce modèle et de ce principe vise à maintenir une structure de code souple et modulaire,
     * en permettant aux classes de s'observer mutuellement sans créer de dépendances directes.
     *
     * Cette méthode permet d'éviter la création de dépendances cycliques entre les classes et facilite les interactions
     * entre les gestionnaires de manière fluide. Elle initialise les objets et attributs requis pour le rendu, tels que
     * la région, le joueur, l'inventaire du joueur et le ciel. De plus, elle configure des éléments essentiels pour
     * le rendu, comme le font pour le texte, le lot de sprites pour les graphiques 2D et le ShapeRenderer pour les formes.
     * 
     * @param region La région du jeu à afficher.
     * @param player Le joueur jouable de la partie.
     * @param inventory L'inventaire du joueur.
     * @param sky Le ciel du jeu.
     */
    public void initialize(Region region, Player player, InventoryMenu inventory, CraftingLootMenu craftingLootMenu, CraftingBlockMenu craftingBlockMenu,CapabilitiesMenu capabilitiesMenu, MapMenu mapMenu, StoryMenu storyMenu, CommonInfoMenu commonInfoMenu,CraftBlockManager craftBlockManager, NotificationManager notificationManager, Sky sky,RainManager rainManager, UpdateManager updateManager, WheelManager wheelMenu) {
        this.region = region;
        this.player = player;
        this.playerInventory = inventory;
        this.craftingLootMenu = craftingLootMenu;
        this.craftingBlockMenu = craftingBlockMenu;
        this.capabilitiesMenu = capabilitiesMenu;
        this.mapMenu = mapMenu;
        this.storyMenu = storyMenu;
        this.commonInfoMenu = commonInfoMenu;
        this.craftBlockManager = craftBlockManager;
        this.wheelManager = wheelMenu;
        this.sky = sky;
        this.rainManager = rainManager;
        this.updateManager = updateManager ;
        this.lootManager = LootManager.getInstance();
        this.notificationManager = notificationManager;
        this.batch = new SpriteBatch();
    }

    
    /**Render de tous les élements que l'on veut afficher à l'écran */
    public void render() {
        batch.begin();
        float dt = Gdx.graphics.getDeltaTime();//DELTA TIME 
        renderMap();
        synchronized (rainManager.lock()) {
            rainManager.renderDropsOnFloor(batch, region);
        }
        renderObjects();
        synchronized (rainManager.lock()) {
            rainManager.renderDropsOnSky(batch, region);
        }
        //renderSky(dt);
        wheelManager.renderActualTool(batch);
        craftBlockManager.render(batch);
        renderActiveMenu();
        if(commonInfoMenu.getCurrentMenu() != null){
            commonInfoMenu.render(batch);
        }
        notificationManager.render(batch);
        batch.end();
        Debug.getInstance().renderZoneDegat(player);
       // Debug.getInstance().renderHitboxPolygon(player);
    }


    private void renderMap() {
        //draw the pixmap groundPixCollision
        batch.draw(region.texture(), -region.camera().position().x(), -region.camera().position().y());
    }

    private void renderObjects(){
        //Affichage en fonction de la profondeur
        boolean boyDrawn = false;
        for(AbstractObject obj :region.interactionComponent().abstractObjectsOnGround().objects()){
            if(!boyDrawn && player.z() >obj.z()){
                player.render(batch, region);
                boyDrawn = true;
            }
            //System.out.println("Rendering object : "+obj.name());
            obj.render(batch, region);
        }
        if(!boyDrawn){
            player.render(batch, region);
        }
    
    }

    public void renderActiveMenu(){
        switch(updateManager.getActiveMenu()){
            case "game":
                break;
            case "INVENTORY":
                playerInventory.render(batch);
                break;
            case "CRAFT_LOOT":
                craftingLootMenu.render(batch);
                break;
            case "CRAFT_BLOCK":
                craftingBlockMenu.render(batch);
                break;
            case "CAPABILITIES":
                capabilitiesMenu.render(batch);
                break;
            case "MAP":
                mapMenu.render(batch);
                break;
            case "STORY":
                storyMenu.render(batch);
                break;
            case "helpMenu":
                Debug.getInstance().drawHelpButtons(batch);
                break;
            case "wheel":
                wheelManager.render(batch);
                break;
            case "placementBlock":
                break;
        }
    }

    private void renderSky(float deltaTime) {
        sky.render(batch, deltaTime/10);
    
    }

    public void dispose() {
        // Libérer les ressources utilisées par le renderer
        rainManager.stopUpdateThread();
        batch.dispose();
        playerInventory.dispose();
        player.texture().dispose();
        FontManager.getInstance().dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
