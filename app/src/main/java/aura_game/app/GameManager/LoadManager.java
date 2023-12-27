package aura_game.app.GameManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.Input;

import aura_game.app.CraftingMenu;
import aura_game.app.InputHandler;
import aura_game.app.InventoryMenu;
import aura_game.app.Region;
import aura_game.app.WheelMenus;
import aura_game.app.Objects.IAEntity;
import aura_game.app.Objects.Item;
import aura_game.app.Objects.Loot;
import aura_game.app.Objects.PlayableEntity;
import aura_game.app.Type.EntityType;
import aura_game.app.Type.ItemType;
import aura_game.app.Type.LootType;

/**
 * Systeme de chargement, initialisation d'élements (pas de création des objets)
 */
public class LoadManager {
    private UpdateManager updateManager;
    private Region region;
    private PlayableEntity player;
    private InventoryMenu playerInventory;
    private InputHandler inputHandler;
    private CraftingMenu crafting;
    private WheelMenus wheelMenus;

    
    /**
     * Initialise les dépendances et configurations nécessaires pour le gestionnaire de chargement. Cette méthode joue un rôle central
     * dans l'application du modèle de conception "Pattern Observer" (Observateur) conjointement avec le principe d'"Injection de Dépendances".
     * En utilisant ce modèle et ce principe, les classes peuvent s'observer mutuellement sans créer de dépendances directes.
     * <p>
     * La méthode met en place les dépendances requises pour le gestionnaire de chargement. Elle prépare les objets et attributs
     * cruciaux, notamment le gestionnaire de mise à jour, la région du jeu, le joueur, l'inventaire du joueur et le gestionnaire d'entrée.
     * Cette initialisation crée une base solide pour le chargement des éléments du jeu en garantissant que les dépendances sont correctement
     * établies et prêtes à être utilisées.
     *
     * @param updateManager Le gestionnaire de mise à jour du jeu.
     * @param region La région du jeu pour le chargement.
     * @param player Le joueur jouable de la partie.
     * @param inventory L'inventaire du joueur.
     * @param inputHandler Le gestionnaire d'entrée pour le jeu.
     */
    public void initialize(UpdateManager updateManager,Region region, PlayableEntity player, InventoryMenu inventory, InputHandler inputHandler, CraftingMenu crafting, WheelMenus wheelMenu) {
        this.updateManager = updateManager;
        this.region = region;
        this.player = player;
        this.playerInventory = inventory;
        this.inputHandler = inputHandler;
        this.crafting = crafting;
        this.wheelMenus = wheelMenu;
    }
        // Initialisez les éléments nécessaires ici
    

    public void startNewGame() {
        // Logique de démarrage d'une nouvelle partie
        loadObjectsOnRegion("mapForest");
        initHashMapInputHandler();
        // Autres étapes de création de la partie
    }

    private void loadObjectsOnRegion(String regionName) {//TODO: il peut y avoir plusieurs map !  
        // Chargez la disposition des objets à partir du fichier
        loadRegionLayoutItems(regionName);
        // Chargez la disposition des entités (hors player) à partir du fichier
        loadMapLayoutEntities(regionName);

    }

    /**
     * Charge et lis le fichier puis place les entités (hors player) aux bonnes coordonnées sur la region/map en question
     * @param regionName le nom de la map pour charger le fichier de la region/map layout d'items
     */
    public void loadMapLayoutEntities(String regionName) {
        IAEntity ent;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/layoutEntity/"+regionName+"_layout.txt"));
            //BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+"); // Sépare les parties de la ligne par des espaces ou des virgules
                if (parts.length >= 3) {
                    String entityName = parts[0];
                    int initX = Integer.parseInt(parts[1]);
                    int initY = Integer.parseInt(parts[2]);
                    // Créez l'objet correspondant avec le nom itemName
                    EntityType entityType = EntityType.valueOf(entityName);
                    if (entityType != null) {
                        ent = new IAEntity(entityType, 4, 6.0f, initX, initY);
                        // Placez l'objet sur la carte aux coordonnées (x, y)
                        //System.out.println("typeID:"+itemId + " casex:" + caseX + ",casey:"+ caseY +"\n");
                        if(!region.willCollideGroundMove(initX,initY, ent)){//CheckColission avec le sol
                            region.addSortedObject(ent);
                            //region.getQuadtreeIAEntity().insert(ent);
                            region.getGridIAEntity().add(ent, region.getGridItem().getCaseFor(ent.getHitboxFlat()));//TODO TEST
                        }else{System.out.println("error during placement of entity " + entityName +" in position " +initX+ ", " +initY+ ": collision with ground detected !");}
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Charge et lis le fichier puis place les objets/items aux bonnes coordonnées sur la map en question (si il n'y a pas de colission)
     * @param regionName le nom de la map pour charger le fichier de la map layout d'items
     */
    public void loadRegionLayoutItems(String regionName) {
        Item ite;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/layoutItem/"+regionName+"_layout.txt"));
            //BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+"); // Sépare les parties de la ligne par des espaces ou des virgules
                if (parts.length >= 3) {
                    String itemName = parts[0];
                    String color = parts[1];
                    int caseX = Integer.parseInt(parts[2]);
                    int caseY = Integer.parseInt(parts[3]);
                    // Créez l'objet correspondant avec le nom itemName
                    ItemType itemType = ItemType.valueOf(itemName);
                    if (itemType != null) {
                        ite = new Item(itemType, caseX, caseY, color);
                        // Placez l'objet sur la carte aux coordonnées (x, y)
                        //System.out.println("typeName:"+itemName+ " casex:" + caseX + ",casey:"+ caseY + "color"+color+"\n");
                        if(!ite.willCollideIn(region)){
                            region.addSortedObject(ite);
                            //region.getQuadtreeItem().insert(ite);
                            region.getGridItem().add(ite, region.getGridItem().getCaseFor(ite.getHitboxFlat()));//TODO TEST
                        }else{System.out.println("error during placement of item "+ itemName +" in case " +caseX+ ", " +caseY+ ": collision detected !");}
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //TODO TODO TODO TODO
    /**
     * Rempli la hashmap qui relis les keycode aux actions effectués (par exemple si on appuie sur E ca ouvre/ferme l'inventaire)
     */
    private void initHashMapInputHandler(){

        inputHandler.mapKeyToAction(Input.Keys.BACKSPACE, () -> {
            if (updateManager.activeMenu().equals("inventory")){
                playerInventory.drop();
            }   });
        inputHandler.mapKeyToAction(Input.Keys.E, () -> { updateManager.invertActiveMenu("inventory");});
        inputHandler.mapKeyToAction(Input.Keys.C, () -> { updateManager.invertActiveMenu("crafting");});
        inputHandler.mapKeyToAction(Input.Keys.R, () -> { updateManager.invertActiveMenuWheel("rangedWeapons");/*wheelMenus.setActiveWheel("rangedWeapons");*/});
        inputHandler.mapKeyToAction(Input.Keys.M, () -> { updateManager.invertActiveMenuWheel("meleeWeapons");/*wheelMenus.setActiveWheel("meleeWeapons");*/});

        inputHandler.mapKeyToAction(Input.Keys.LEFT,() -> {
            switch(updateManager.activeMenu()){
                case "game": player.changeAction("Walk_L"); break;
                case "inventory" : playerInventory.moveSlotSelected("L");break;
                case "crafting" : crafting.moveSlotAndLootsSelected("L");break;
                case "wheel" : wheelMenus.moveSlotSelected("L");break;
            } });

        inputHandler.mapKeyToAction(Input.Keys.RIGHT,() -> {
            switch(updateManager.activeMenu()){
                case "game": player.changeAction("Walk_R"); break;
                case "inventory" : playerInventory.moveSlotSelected("R");break;
                case "crafting" : crafting.moveSlotAndLootsSelected("R");break;
                case "wheel" : wheelMenus.moveSlotSelected("R");break;
            } });

        inputHandler.mapKeyToAction(Input.Keys.UP,() -> {
            if (updateManager.activeMenu().equals("game")) {    
                player.changeAction("Walk_U");} });
        inputHandler.mapKeyToAction(Input.Keys.DOWN,() -> {
            if (updateManager.activeMenu().equals("game")){  
                player.changeAction("Walk_D");} });
        inputHandler.mapKeyToAction(Input.Keys.K,() -> {
            if (updateManager.activeMenu().equals("game")){
                player.changeAction("Slash");}  });
        inputHandler.mapKeyToAction(Input.Keys.L,() -> {
            if (updateManager.activeMenu().equals("game")){  
                player.changeAction("SpellCast");} });
        inputHandler.mapKeyToAction(Input.Keys.P,() -> { 
            if (updateManager.activeMenu().equals("game")){  
                player.changeAction("Thrust");} });
        inputHandler.mapKeyToAction(Input.Keys.O,() -> {
            if (updateManager.activeMenu().equals("game")){  
                player.changeAction("Shoot");}  });
                //?CHEAT HELP TEST
        inputHandler.mapKeyToAction(Input.Keys.S, () -> {
            playerInventory.addToInventory(new Loot(LootType.branch,0,0,false,null,true),12);
            playerInventory.addToInventory(new Loot(LootType.stick,0,0,false,null,true),12);
            playerInventory.addToInventory(new Loot(LootType.rock,0,0,false,null,true),12);
        });

        inputHandler.mapKeyToAction(Input.Keys.ENTER,() -> {
            if (updateManager.activeMenu().equals("crafting")) {    
                crafting.getCraftManager().craftItem(Pair.of(crafting.getLootSelected().getName(),1));
            }else if (updateManager.activeMenu().equals("wheel")) { wheelMenus.setActualTool(player);}
        });

        inputHandler.mapKeyToAction(Input.Keys.A, () -> { wheelMenus.getMeleeWeapons().setActualToolPlayer(player);});
        inputHandler.mapKeyToAction(Input.Keys.Z, () -> { wheelMenus.getRangedWeapons().setActualToolPlayer(player);});
    }
    
}
