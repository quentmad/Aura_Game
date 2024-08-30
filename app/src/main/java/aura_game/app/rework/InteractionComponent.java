package aura_game.app.rework;

import aura_game.app.CraftableBlock.BlockPlan;
import aura_game.app.GameManager.AnimatedBlockEntityManager;
import aura_game.app.GameManager.Game;
import aura_game.app.InventoryMenu;
import aura_game.app.LootManager;
import aura_game.app.LootStack;
import aura_game.app.WheelManager;
import aura_game.app.rework.TypeEnum.LootableObjectType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class InteractionComponent {

    /**
     * Objets présents sur la carte (blocks, loots, enemies...)
     * Utile pour afficher dans le bon ordre Z les entités (superposition...)
     */
    private ObjectsOnMap<AbstractObject> abstractObjectsOnGround;
    /**
     * Instance unique
     */
    private InventoryMenu inventoryMenu;
    /**
     * Instance unique
     */
    private WheelManager wheelManager;

    private AnimatedBlockEntityManager animatedBlockEntityManager;

    public InteractionComponent(){
        this.abstractObjectsOnGround = new ObjectsOnMap<>();
        this.inventoryMenu = Game.getInstance().getInventory();;
        this.wheelManager = Game.getInstance().getWheelManager();
        this.animatedBlockEntityManager = new AnimatedBlockEntityManager();
    }

    /**
     * Retire les objets qui ne sont plus au sol
     * Si l'objet est une entité, retire l'entité de la grille adaptée et génère les loots
     * Si l'objet est un lootable, le retire de la région et le met dans le menu d'inventaire ou wheel
     */
    public void update(float dt) {
        animatedBlockEntityManager.update(dt);
        List<AbstractObject> toRemove = new ArrayList<>();

        synchronized (abstractObjectsOnGround.objects()) {
            List<LootableObject> lootToStack = new ArrayList<>();
            for (AbstractObject ent : new ArrayList<>(abstractObjectsOnGround.objects())) {
                if (!ent.isOnGround() && !(ent instanceof BlockPlan)) {//Les plansBlock ne sont pas sur le sol pourtant ne sont pas à retirer (on veut les retirer alors qu'ils n'ont pas ete mis dans la grid)
                    toRemove.add(ent);
                    if (ent instanceof AbstractEntity) {
                        ((AbstractEntity) ent).getAdaptedGrid().remove((AbstractEntity) ent);
                        ((AbstractEntity) ent).spawnDeathLoots(LootManager.getInstance());
                    } else if (ent instanceof LootableObject) {
                        //Ajout dans une liste tempo pour rajouter par stack dans l'inventaire
                        if(ent instanceof Tool) {
                            if (wheelManager.add((Tool) ent)) {
                                ent.setOnGround(false);
                            }
                        }else {
                            //Stack les loots a mettre dans l'inventaire
                            lootToStack.add((LootableObject) ent);
                        }
                    }
                }
            }
            //Stack:
            List<LootStack> stackedLoot = stackLootFromList(lootToStack);
            for (LootStack stack : stackedLoot) {
                if(inventoryMenu.addToInventory(stack.getLootType(), stack.getQuantity())){
                    //Set onGround false
                    for (AbstractObject loot : lootToStack) {
                        if(loot.name().equals(stack.getLootType().name())){
                            loot.setOnGround(false);
                        }
                    }
                }
            }

            abstractObjectsOnGround.objects().removeAll(toRemove);
        }
    }

    /**
     * Transforme une liste de LootableObject en liste de LootStack
     * @return
     */
    public List<LootStack> stackLootFromList(List<LootableObject> lootList) {
        List<LootStack> lootStacks = new ArrayList<>();
        for (LootableObject loot : lootList) {
            boolean added = false;
            for (LootStack stack : lootStacks) {
                if (stack.getLootType().name().equals(loot.name())) {
                    stack.addQuantity(1);
                    added = true;
                    break;
                }
            }
            if (!added) {
                lootStacks.add(new LootStack(LootableObjectType.valueOf(loot.getName()), 1));
            }
        }
        return lootStacks;
    }

    public ObjectsOnMap<AbstractObject> abstractObjectsOnGround() {
        return abstractObjectsOnGround;
    }

    public AnimatedBlockEntityManager animatedBlockEntityManager() {
        return animatedBlockEntityManager;
    }
}
