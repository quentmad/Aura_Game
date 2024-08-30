package aura_game.app.GameManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import aura_game.app.*;
import aura_game.app.CraftableBlock.BlockPlan;
import aura_game.app.CraftableBlock.CraftBlockManager;
import aura_game.app.LPCActions.PushState;
import aura_game.app.rework.*;
import aura_game.app.rework.TypeEnum.ActorEntityType;
import aura_game.app.rework.TypeEnum.BlockEntityType;
import aura_game.app.rework.TypeEnum.LootableObjectType;
import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.Input;

/**
 * Système de chargement, initialisation d'éléments (pas de création des objets)
 */
public class LoadManager {
    private UpdateManager updateManager;
    private Region region;
    private Player player;
    private InventoryMenu playerInventory;
    private InputHandler inputHandler;
    private CraftingLootMenu craftingLootMenu;
    private CraftingBlockMenu craftingBlockMenu;
    private CraftBlockManager craftBlockManager;
    private WheelManager wheelManager;

    private PhysicsMovableComponent physicsMovableComponent;
    private PhysicsComponent physicsComponent;

    private int idCounter = 0;



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
     * @param craftingLootMenu Le gestionnaire de craft
     * @param wheelMenu les menus de ranged et meele
     */
    public void initialize(UpdateManager updateManager, Region region, Player player, InventoryMenu inventory, InputHandler inputHandler, CraftingLootMenu craftingLootMenu, CraftingBlockMenu craftingBlockMenu, CraftBlockManager craftBlockManager, WheelManager wheelMenu, PhysicsMovableComponent physicsMovableComponent, PhysicsComponent physicsComponent) {
        this.updateManager = updateManager;
        this.region = region;
        this.player = player;
        this.playerInventory = inventory;
        this.inputHandler = inputHandler;
        this.craftingLootMenu = craftingLootMenu;
        this.craftingBlockMenu = craftingBlockMenu;
        this.craftBlockManager = craftBlockManager;
        this.wheelManager = wheelMenu;
        this.physicsMovableComponent = physicsMovableComponent;
        this.physicsComponent = physicsComponent;
    }
        // Initialisez les éléments nécessaires ici


    public void startNewGame() {
        // Logique de démarrage d'une nouvelle partie
        loadBlockOnRegion("mapForest");
        loadRegionAnimatedBlocks("mapForest");
        initHashMapInputHandler();
        // Autres étapes de création de la partie
    }

    private void loadBlockOnRegion(String regionName) {//TODO: il peut y avoir plusieurs map !
        // Chargez la disposition des objets à partir du fichier
        loadRegionLayoutBlocks(regionName);
        // Chargez la disposition des entités (hors player) à partir du fichier
        //loadMapLayoutIAActorEntities(regionName);

    }

    /**
     * Charge et lis le fichier puis place les ia actors entités (hors player) aux bonnes coordonnées sur la region/map en question
     * @param regionName le nom de la map pour charger le fichier de la region/map layout d'items
     */
    private void loadMapLayoutIAActorEntities(String regionName) {
        IAActorEntity actEnt;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/layoutEntity/"+regionName+"_layout.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+"); // Sépare les parties de la ligne par des espaces ou des virgules
                if (parts.length >= 3) {
                    String actorEntityName = parts[0];
                    int initX = Integer.parseInt(parts[1]);
                    int initY = Integer.parseInt(parts[2]);
                    // Créez l'objet correspondant avec le nom itemName
                    ActorEntityType actorEntityType = ActorEntityType.valueOf(actorEntityName);
                    if (actorEntityType != null) {
                        actEnt = new IAActorEntity(actorEntityType,initX,initY,0.6f);
                        // Placez l'objet sur la carte aux coordonnées (x, y)
                        //System.out.println("typeID:"+itemId + " casex:" + caseX + ",casey:"+ caseY +"\n");
                        if(!region.physicsMovableComponent().isPolygonCollidingWithPixmap(actEnt.hitbox().translateIntermediatePoints(initX,initY))){//CheckColission avec le sol
                            region.interactionComponent().abstractObjectsOnGround().add(actEnt);
                            actEnt.setOnGround(true);
                            physicsMovableComponent.gridActorEntity().add(actEnt, physicsComponent.gridBlockEntity().getCaseFor(actEnt.hitbox().approximativeHitbox()));//TODO TEST
                            //region.getGridIAEntity().add(actEnt, region.getGridItem().getCaseFor(actEnt.getHitboxFlat()));//TODO TEST
                        }else{System.out.println("error during placement of entity " + actorEntityName +" in position " +initX+ ", " +initY+ ": collision with ground detected !");}
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Charge et lis le fichier puis place les objets/items aux bonnes coordonnées sur la map en question s'il n'y a pas de collision
     * @param regionName le nom de la map pour charger le fichier de la map layout d'items
     */
    private void loadRegionLayoutBlocks(String regionName) {
        BlockEntity block;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/layoutBlock/"+regionName+"_layout.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+"); // Sépare les parties de la ligne par des espaces ou des virgules
                if (parts.length >= 3) {
                    String itemName = parts[0];
                    String color = parts[1];
                    int caseX = Integer.parseInt(parts[2]);
                    int caseY = Integer.parseInt(parts[3]);
                    // Créez l'objet correspondant avec le nom itemName
                    BlockEntityType blockEntityType = BlockEntityType.valueOf(itemName);
                    if (blockEntityType != null) {
                        block = new BlockEntity(blockEntityType,caseX,caseY,color);
                        // Placez l'objet sur la carte aux coordonnées (x, y)
                        region.addBlockOnMap(block);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Charge et lis le fichier puis place les blocks sans colissions (animated) aux bonnes coordonnées sur la map en question s'il n'y a pas de collision
     * @param regionName le nom de la map pour charger le fichier de la map layout d'items
     */
    private void loadRegionAnimatedBlocks(String regionName) {
        AnimatedBlockEntity block;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/layoutBlock/"+regionName+"_animated_layout.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+"); // Sépare les parties de la ligne par des espaces ou des virgules
                if (parts.length >= 3) {
                    String itemName = parts[0];
                    String color = parts[1];
                    int caseX = Integer.parseInt(parts[2]);
                    int caseY = Integer.parseInt(parts[3]);
                    // Créez l'objet correspondant avec le nom itemName
                    BlockEntityType blockEntityType = BlockEntityType.valueOf(itemName);
                    if (blockEntityType != null) {
                        block = new AnimatedBlockEntity(blockEntityType,caseX,caseY,color);
                        // Placez l'objet sur la carte aux coordonnées (x, y)
                        region.addNonCollidableBlockOnMap(block);
                        region.interactionComponent().animatedBlockEntityManager().add(block);
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
     * Rempli la hashmap qui relit les keycode aux actions effectuées (par exemple si on appuie sur E ça ouvre/ferme l'inventaire)
     */
    private void initHashMapInputHandler(){

        inputHandler.mapKeyToAction(Input.Keys.BACKSPACE, () -> {
            if (updateManager.getActiveMenu().equals("INVENTORY")){
                playerInventory.drop();
            }
            if (updateManager.getActiveMenu().equals("wheel")){
                wheelManager.getCurrentOpenWheel().drop(player);
            }

        });
        inputHandler.mapKeyToAction(Input.Keys.W,()-> {
            if (CommonInfoMenu.getInstance().getCurrentMenu() != null && CommonInfoMenu.getInstance().getLeftMenu() != null) {
                CommonInfoMenu.getInstance().setCurrentMenu(CommonInfoMenu.getInstance().getLeftMenu());
                updateManager.setActiveMenu(CommonInfoMenu.getInstance().getCurrentMenu().name());
            } else if(updateManager.getActiveMenu().equals("game") && craftBlockManager.getClosestBlock()!=null) craftBlockManager.slideSelectedSlotToAdd(-1);

        });
        inputHandler.mapKeyToAction(Input.Keys.X,()-> {
            if (CommonInfoMenu.getInstance().getCurrentMenu() != null && CommonInfoMenu.getInstance().getRightMenu() != null) {
                CommonInfoMenu.getInstance().setCurrentMenu(CommonInfoMenu.getInstance().getRightMenu());
                updateManager.setActiveMenu(CommonInfoMenu.getInstance().getCurrentMenu().name());
            } else if(updateManager.getActiveMenu().equals("game") && craftBlockManager.getClosestBlock()!=null) craftBlockManager.slideSelectedSlotToAdd(1);

        });

        inputHandler.mapKeyToAction(Input.Keys.I, () -> { updateManager.invertActiveMenu(CommonInfoMenu.Menu.INVENTORY);});
        inputHandler.mapKeyToAction(Input.Keys.R, () -> { updateManager.invertCurrentOpenMenuWheel("rangedWeapons");});

        inputHandler.mapKeyToAction(Input.Keys.M, () -> { updateManager.invertCurrentOpenMenuWheel("meleeWeapons");});

        inputHandler.mapKeyToAction(Input.Keys.LEFT,() -> {
            switch(updateManager.getActiveMenu()){
                case "game": player.stateComponant().changeAction("Walk", Orientation.WEST);break;
                case "INVENTORY" : playerInventory.moveSlotSelected("L");break;
                case "CRAFT_LOOT" : craftingLootMenu.moveSlotAndLootsSelected("L");break;
                case "CRAFT_BLOCK" : craftingBlockMenu.moveSlotAndLootsSelected("L");break;
                case "wheel" : wheelManager.moveSlotSelected("L");break;
                case "placementBlock" : craftingBlockMenu.getCraftBlockManager().moveLocationBlockPlan(player,-4,0);break;

            } });

        inputHandler.mapKeyToAction(Input.Keys.RIGHT,() -> {
            switch(updateManager.getActiveMenu()){
                case "game": player.stateComponant().changeAction("Walk", Orientation.EAST); break;
                case "INVENTORY" : playerInventory.moveSlotSelected("R");break;
                case "CRAFT_LOOT" : craftingLootMenu.moveSlotAndLootsSelected("R");break;
                case "CRAFT_BLOCK" : craftingBlockMenu.moveSlotAndLootsSelected("R");break;
                case "wheel" : wheelManager.moveSlotSelected("R");break;
                case "placementBlock" :
                    craftingBlockMenu.getCraftBlockManager().moveLocationBlockPlan(player,4,0);break;
            } });

        inputHandler.mapKeyToAction(Input.Keys.UP,() -> {
                    if (updateManager.getActiveMenu().equals("game")) {
                        player.stateComponant().changeAction("Walk", Orientation.NORTH);
                    }else if(updateManager.getActiveMenu().equals("placementBlock")){
                        craftingBlockMenu.getCraftBlockManager().moveLocationBlockPlan(player,0,4);
                    }
                });
        inputHandler.mapKeyToAction(Input.Keys.DOWN,() -> {
            if (updateManager.getActiveMenu().equals("game")){
                player.stateComponant().changeAction("Walk", Orientation.SOUTH);
            }else if(updateManager.getActiveMenu().equals("placementBlock")){
                craftingBlockMenu.getCraftBlockManager().moveLocationBlockPlan(player,0,-4);
            }
        });
        inputHandler.mapKeyToAction(0,() -> {//Input.Keys.K
            if (updateManager.getActiveMenu().equals("game")){
                player.stateComponant().changeAction("Slash", player.stateComponant().getCurrentOrientation());
            }
        });
        inputHandler.mapKeyToAction(Input.Keys.L,() -> {
            if (updateManager.getActiveMenu().equals("game")){
                player.stateComponant().changeAction("SpellCast", player.stateComponant().getCurrentOrientation());
            }
        });
        inputHandler.mapKeyToAction(Input.Keys.P,() -> {
            if (updateManager.getActiveMenu().equals("game")){
                player.stateComponant().changeAction("Thrust", player.stateComponant().getCurrentOrientation());
            }
        });
        inputHandler.mapKeyToAction(Input.Keys.SPACE,() -> {
            if (updateManager.getActiveMenu().equals("game")){
                player.stateComponant().changeAction("Jump", player.stateComponant().getCurrentOrientation());
            }
        });
        inputHandler.mapKeyToAction(Input.Keys.D,() -> {//Run if already walking
            //System.out.println("a" + player.stateComponant().getCurrentStateName());
            if (/*updateManager.activeMenu().equals("game") && */ player.stateComponant().getCurrentStateName().equals("Walk")){
                //System.out.println("-----------------------------run now please");
                player.stateComponant().changeAction("Run", player.stateComponant().getCurrentOrientation());
            }
        });
        inputHandler.mapKeyToAction(Input.Keys.U,() -> {//Carry if already walking
            System.out.println("a" + player.stateComponant().getCurrentStateName());
            if (player.stateComponant().getCurrentStateName().equals("Walk")){
                System.out.println("-----------------------------carry now please");
                player.stateComponant().changeAction("Carry", player.stateComponant().getCurrentOrientation());
            }
        });
        inputHandler.mapKeyToAction(Input.Keys.Y,() -> {//Push position
            System.out.println("a" + player.stateComponant().getCurrentStateName());
            if (player.stateComponant().getCurrentStateName().equals("Idle")){
                System.out.println("-----------------------------push position if object");
                player.stateComponant().changeAction("Push", player.stateComponant().getCurrentOrientation());
                //On regarde si il y a une entité à pousser
                ((PushState)player.stateComponant().getCurrentState()).findObjectToPush(player);

            }
        });
        inputHandler.mapKeyToAction(1,() -> {//O
            if (updateManager.getActiveMenu().equals("game")){
                player.stateComponant().changeAction("Shoot", player.stateComponant().getCurrentOrientation());
            }
        });
                //?CHEAT HELP TEST
        inputHandler.mapKeyToAction(Input.Keys.S, () -> {
            player.hurt(0.5f);
            playerInventory.addToInventory(LootableObjectType.branch,12);
            playerInventory.addToInventory(LootableObjectType.stick,12);
            playerInventory.addToInventory(LootableObjectType.rock,12);
        });

        inputHandler.mapKeyToAction(Input.Keys.ENTER,() -> {
            if (updateManager.getActiveMenu().equals("CRAFT_LOOT")) {
                craftingLootMenu.getCraftManager().craftLoot(Pair.of(craftingLootMenu.getLootSelected().name(),1));
            }else if (updateManager.getActiveMenu().equals("CRAFT_BLOCK")) {
                craftingBlockMenu.getCraftBlockManager().addToBlockInCreation(new BlockPlan(BlockEntityType.valueOf(craftingBlockMenu.getBlockSelected().name()),player.posC().x(),player.posC().y(), "default", id()), player);
                updateManager.setActiveMenu("placementBlock");
            }else if (updateManager.getActiveMenu().equals("wheel")) { wheelManager.setActualTool(player);
            }else if(updateManager.getActiveMenu().equals("placementBlock")){
                if(craftingBlockMenu.getCraftBlockManager().validatePlaceBlockPlan(player))
                updateManager.setActiveMenu("game");
            }else if(updateManager.getActiveMenu().equals("game")){
                if(craftBlockManager.getClosestBlock()!=null){
                    craftBlockManager.addElementToClosestBlockInCreation();
                }
            }
        });

        inputHandler.mapKeyToAction(Input.Keys.A, () -> wheelManager.getMeleeWeapons().setActualEquippedToolToFavoriteOfThisWheel(player)); //TODO LAISSER NCA ICI ??? C'est ici qu'on dit qu'elle type d'arme on veut....
        inputHandler.mapKeyToAction(Input.Keys.Z, () -> wheelManager.getRangedWeapons().setActualEquippedToolToFavoriteOfThisWheel(player));

        //Draw Commands Keyboard for 100s
        inputHandler.mapKeyToAction(Input.Keys.H,() -> updateManager.invertActiveMenu("helpMenu"));


    }

    public int id(){
        return idCounter++;
    }

}
