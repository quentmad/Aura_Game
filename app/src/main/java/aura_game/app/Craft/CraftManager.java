package aura_game.app.Craft;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import aura_game.app.HashmapManager;
import aura_game.app.InventoryMenu;
import aura_game.app.LootStack;
import aura_game.app.WheelManager;
import aura_game.app.Objects.Loot;
import aura_game.app.Objects.Tool.Tool;
import aura_game.app.Type.LootType;
import aura_game.app.Type.ToolType;

//Il y a deux type de craft ! Le craft des loots (ajout dans l'inventaire) et les items qu'on place et craft via le Plan
public class CraftManager {

    /**Un nom et une quantité à partir d'une liste de loots avec quantité*/
    private HashmapManager<Pair<String,Integer>, List<LootStack>> recipeManager;//TODO: craft un trucs qui a plusieurs qté

    //private HashmapManager<LootType, List<LootStack>> recipeManager;//TODO: craft un trucs qui a plusieurs qté
    
    /**Contient les loots disponible au craft. Permet d'afficher dans le menu tous les loots craftable, 
     * et retrouvé via selectedSlot les ingredients du craft sélectionné pour les afficher  */
    private List<LootType> availableLoots;
    private InventoryMenu inventoryMenu;
    private WheelManager wheelManager;

    public CraftManager() {
        this.recipeManager = new HashmapManager<>();
        this.availableLoots = new ArrayList<>();
        this.inventoryMenu = InventoryMenu.getInstance();
        this.wheelManager = WheelManager.getInstance();
        initHashmap();
        initAvailableLoot();

    }

    /**Loots disponible à crafter */
    public List<LootType> getAvailableLoots(){
        return availableLoots;
    }

    public HashmapManager<Pair<String,Integer>, List<LootStack>> getRecipeManager(){
        return recipeManager;
    }

    /**@return le lootType d'availableLoots à l'emplacement i*/
    public LootType getAvailableLootsI(int i){
        return availableLoots.get(i);
    }
    
    /**
     * Cette méthode permet de fabriquer un tool ou loot en utilisant une paire de nom et de quantité.
     * Elle récupère les ingrédients nécessaires au craft dans la hashmap recipeManager.
     * Ensuite, elle vérifie si les ressources nécessaires sont présentes dans l'inventaire.
     * Si les ressources sont disponibles, elle retire les ressources de l'inventaire et ajoute l'objet fabriqué.
     * Si les ressources ne sont pas disponibles, elle affiche un message d'erreur.
     * @param nameQuantity la pair de nom et quantité du loot ou tool qu'on souhaite créer
     * @return {@code true} si le craft est réussi et false sinon.
     */
    public boolean craftItem(Pair<String,Integer> nameQuantity) {
        //Récupère les ingredients nécessaire au craft dans la hashmap
        List<LootStack> ingredients = recipeManager.getData(nameQuantity);
        if(ingredients !=null){
            // Vérifier si les ressources nécessaires sont dans l'inventaire
            boolean hasIngredients = true;
            for (LootStack ingredient : ingredients) {
                if (!inventoryMenu.hasLoot(ingredient.getLootType(), ingredient.getQuantity())) {
                    hasIngredients = false;
                    System.out.println("You don't have enough resources to craft " +nameQuantity.getRight()+" "+nameQuantity.getLeft());
                    return false;
                }
            }
            
            if (hasIngredients) {
                //BRUIT DE FABRICATION en fct du type
                // Retirer les ressources de l'inventaire
                for (LootStack ingredient : ingredients) {
                    inventoryMenu.removeFromInventory(ingredient.getLootType(), ingredient.getQuantity());
                }

                if(isAWeapon(LootType.valueOf(nameQuantity.getLeft()))){
                    System.out.println("Arme !");
                    wheelManager.add(new Tool(ToolType.valueOf(nameQuantity.getLeft()), 0, 0, false, null, true));
                }else{
                    System.out.println("Non arme !");
                    // Ajouter l'objet fabriqué à l'inventaire
                    inventoryMenu.addToInventory(new Loot(LootType.valueOf(nameQuantity.getLeft()),0,0,false,null,true),nameQuantity.getRight());//TODO: SI full : drop devant
                }
                //TODO: c pas coherent le param addItemToInventory
                return true; //Craft réussi
            } else {
                System.out.println("You need more resources to craft" +nameQuantity.getRight()+" "+nameQuantity.getLeft());
                return false; //Pas assez de ressources
            }
        }
        return false;
    }


    /**Ajout des crafts /ingrédients à la hashmap
     * Il y a tous les craft dès l'initialisation
     */
    public void initHashmap() {

        List<LootStack> ingredientsHache = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 5));
            add(new LootStack(LootType.rock, 4));
        }};
        recipeManager.addData(Pair.of("hache",1), ingredientsHache);

        List<LootStack> ingredientsMarteau = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 2));
            add(new LootStack(LootType.branch, 1));
            add(new LootStack(LootType.rock, 3));
        }};
        recipeManager.addData(Pair.of("marteau",1), ingredientsMarteau);

        List<LootStack> ingredientsPioche = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 4));
            add(new LootStack(LootType.rock, 4));
        }};
        recipeManager.addData(Pair.of("pioche",1), ingredientsPioche);

        List<LootStack> ingredientsPelle = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 4));
            add(new LootStack(LootType.rock, 5));
        }};
        recipeManager.addData(Pair.of("pelle",1), ingredientsPelle);
    
        List<LootStack> ingredientsFlail = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 5));
            add(new LootStack(LootType.rock, 8));
        }};
        recipeManager.addData(Pair.of("flail",1), ingredientsFlail);

        List<LootStack> ingredientsHache_de_guerre = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 2));
            add(new LootStack(LootType.branch, 2));
            add(new LootStack(LootType.rock, 10));
        }};
        recipeManager.addData(Pair.of("hache_de_guerre",1), ingredientsHache_de_guerre);

        List<LootStack> ingredientsMace = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 5));
            add(new LootStack(LootType.rock, 4));
        }};
        recipeManager.addData(Pair.of("mace",1), ingredientsMace); 

        List<LootStack> ingredientsSimple_staff = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 3));
            add(new LootStack(LootType.branch, 2));
        }};
        recipeManager.addData(Pair.of("simple_staff",1), ingredientsSimple_staff);   

        List<LootStack> ingredientsDiamond_staff = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 5));
            add(new LootStack(LootType.branch, 2));
        }};
        recipeManager.addData(Pair.of("diamond_staff",1), ingredientsDiamond_staff);   

        List<LootStack> ingredientsGnarled_staff = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 7));
            add(new LootStack(LootType.branch, 1));
        }};
        recipeManager.addData(Pair.of("gnarled_staff",1), ingredientsGnarled_staff);      

        List<LootStack> ingredientsLoop_staff = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 8));
            add(new LootStack(LootType.branch, 1));
        }};
        recipeManager.addData(Pair.of("loop_staff",1), ingredientsLoop_staff);      

        List<LootStack> ingredientsLongue_lance = new ArrayList<>() {{
            add(new LootStack(LootType.branch, 1));
            add(new LootStack(LootType.rock, 5));
        }};
        recipeManager.addData(Pair.of("longue_lance",1), ingredientsLongue_lance);      

        List<LootStack> ingredientsMagic = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 7));
            add(new LootStack(LootType.branch, 3));
            add(new LootStack(LootType.rock, 2));

        }};
        recipeManager.addData(Pair.of("magic",1), ingredientsMagic);      

        List<LootStack> ingredientsMagic_alt = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 5));
            add(new LootStack(LootType.branch, 3));
            add(new LootStack(LootType.rock, 2));
        }};
        recipeManager.addData(Pair.of("magic_alt",1), ingredientsMagic_alt);  

        List<LootStack> ingredientsTrident = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 8));
            add(new LootStack(LootType.branch, 1));
            add(new LootStack(LootType.rock, 2));
        }};
        recipeManager.addData(Pair.of("trident",1), ingredientsTrident);      

        List<LootStack> ingredientsSpear_metal = new ArrayList<>() {{
            add(new LootStack(LootType.branch, 2));
            add(new LootStack(LootType.rock, 5));
        }};
        recipeManager.addData(Pair.of("spear_metal",1), ingredientsSpear_metal);      

        List<LootStack> ingredientsSpear_wooden = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 4));
            add(new LootStack(LootType.branch, 2));
        }};
        recipeManager.addData(Pair.of("spear_wooden",1), ingredientsSpear_wooden);      

        List<LootStack> ingredientsArc_normal = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 4));
            add(new LootStack(LootType.branch, 1));
        }};
        recipeManager.addData(Pair.of("arc_normal",1), ingredientsArc_normal);      

        List<LootStack> ingredientsArc_great = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 4));
            add(new LootStack(LootType.branch, 2));
        }};
        recipeManager.addData(Pair.of("arc_great",1), ingredientsArc_great);      

        List<LootStack> ingredientsArbalette = new ArrayList<>() {{
            add(new LootStack(LootType.branch, 1));
            add(new LootStack(LootType.stick, 5));
            add(new LootStack(LootType.rock, 3));
        }};
        recipeManager.addData(Pair.of("arbalette",1), ingredientsArbalette);      

        List<LootStack> ingredientsLongue_epee = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 4));
            add(new LootStack(LootType.branch, 1));
            add(new LootStack(LootType.rock, 7));
        }};
        recipeManager.addData(Pair.of("longue_epee",1), ingredientsLongue_epee);      

        List<LootStack> ingredientsKatana = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 2));
            add(new LootStack(LootType.branch, 1));
            add(new LootStack(LootType.rock, 6));
        }};
        recipeManager.addData(Pair.of("katana",1), ingredientsKatana);      

        List<LootStack> ingredientsSabre = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 7));
            add(new LootStack(LootType.branch, 1));
        }};
        recipeManager.addData(Pair.of("sabre",1), ingredientsSabre);      

        List<LootStack> ingredientsRappier = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 7));
            add(new LootStack(LootType.rock, 10));
        }};
        recipeManager.addData(Pair.of("rappier",1), ingredientsRappier);      

        List<LootStack> ingredientsPoignard = new ArrayList<>() {{
            add(new LootStack(LootType.stick, 5));
            add(new LootStack(LootType.rock, 6));
        }};
        recipeManager.addData(Pair.of("poignard",1), ingredientsPoignard);      

    }

    /**Initialise les craft disponible dès le début du jeu, on débloque les autres au fur et à mesure */
    public void initAvailableLoot() {
        availableLoots.add(LootType.hache);
        availableLoots.add(LootType.marteau);
        availableLoots.add(LootType.pioche);
        availableLoots.add(LootType.pelle);
        availableLoots.add(LootType.flail);
        availableLoots.add(LootType.hache_de_guerre);
        availableLoots.add(LootType.mace);
        availableLoots.add(LootType.simple_staff);
        availableLoots.add(LootType.diamond_staff);
        availableLoots.add(LootType.gnarled_staff);
        availableLoots.add(LootType.loop_staff);
        availableLoots.add(LootType.longue_lance);

        availableLoots.add(LootType.magic);
        availableLoots.add(LootType.magic_alt);
        availableLoots.add(LootType.trident);
        availableLoots.add(LootType.spear_metal);
        availableLoots.add(LootType.spear_wooden);
        availableLoots.add(LootType.arc_normal);
        availableLoots.add(LootType.arc_great);
        availableLoots.add(LootType.arbalette);

        availableLoots.add(LootType.longue_epee);
        availableLoots.add(LootType.katana);
        availableLoots.add(LootType.sabre);
        availableLoots.add(LootType.rappier);
        //availableLoots.add(LootType.poignard);//TODO scroll inventaire...
    }
        
        /**Savoir si un loot est une arme (qui est donc un extends...)
         * Permet de mettre l'element dans l'inventaire ou wheelMenu en fonction...
         */
        public boolean isAWeapon(LootType lt){
            return (lt.type().equals("tool") ||lt.type().equals("melee")||lt.type().equals("ranged")||lt.type().equals("ammunition"));
        }
         
}
