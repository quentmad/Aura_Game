package aura_game.app;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aura_game.app.Objects.PlayableEntity;
import aura_game.app.Objects.Tool;

/**Class singleton regroupant tous les wheels différents (ranged, melee...) */
public class WheelManager {
    private static WheelManager instance;

    private Wheel<Tool> meleeWeapons;
    private Wheel<Tool> rangedWeapons;

    /**Wheel actuellement ouvert (actif) */
    private Wheel<Tool> currentOpenWheel; //Un seul attribut pour gérer le menu actif


    private WheelManager(String colorMenu ) {
        currentOpenWheel = null;
        meleeWeapons = new Wheel<>("melee",colorMenu, 12, 64, "slotWheelMenuMelee",0);
        rangedWeapons = new Wheel<>("ranged",colorMenu, 12, 64, "slotWheelMenuRanged",80);

    }

    public static WheelManager getInstance() {
        if (instance == null) {
            instance = new WheelManager("BLUE");
        }
        return instance;
    }

    

    public Wheel<Tool> getMeleeWeapons() {
        return meleeWeapons;
    }

    public Wheel<Tool> getRangedWeapons() {
        return rangedWeapons;
    }

    public Wheel<Tool> getCurrentOpenWheel() {
        return currentOpenWheel;
    }
    /**
     * Défini le wheel actuellement ouvert à celui en paramètre*/
    public void setCurrentOpenWheel(String wheel) {
            switch (wheel) {
                case "meleeWeapons":
                    currentOpenWheel = meleeWeapons;
                    break;
                case "rangedWeapons":
                    currentOpenWheel = rangedWeapons;
                    break;
                default:
                    currentOpenWheel = null;
                    break;
            }
    }

    /**
     * @return {@code true} si w est un menu existant /valide, sinon {@code false} 
     */
    public boolean isValidWheelName(String w){
        return switch (w) {
            case "meleeWeapons" -> true;
            case "rangedWeapons" -> true;
            default -> false;
        };
    }

    /** 
     * Appel la méthode du {@code activeWheel}
     * Ce lance lorsqu'on appuie sur {@code enter} quand le wheelMenu est ouvert.
     * Defini l'arme actuelle (le nom) de cette categorie du joueur à l'arme du selected slotIndex
     * @param player
     */
    public void setActualTool(PlayableEntity player){
        if (currentOpenWheel != null) {
            currentOpenWheel.setActualToolForCategory(player);
            if(player.getToolManager().getCurrentEquippedToolCategory().equals(currentOpenWheel.getNAME())) {//Si c'est la catégorie actuelle
                currentOpenWheel.setActualEquippedToolToFavoriteOfThisWheel(player);
            }

        } 
    }


    /** Ajoute dans le wheel de sa categorie (et non dans le {@code wheel actif }) */
    public boolean add(Tool element){
        if(element.getType().equals("melee") || element.getType().equals("tool")){
            return meleeWeapons.add(element);
        }else if(element.getType().equals("ranged")){
            return rangedWeapons.add(element);
        }
        return false;

    }

    /** Retire du wheel de sa categorie et non dans le {@code wheel actif} */
    public boolean remove(Tool element){
        if(element.getType().equals("melee") || element.getType().equals("tool")){
            return meleeWeapons.remove(element);
        }else if(element.getType().equals("ranged")){
            return rangedWeapons.remove(element);
        }
        return false;
    }

    /**Appel la méthode du {@code activeWheel}
     * Bouge le slotSelected de WheelMenu 
     * @param direction "R" ou "L"
     */
    public void moveSlotSelected(String direction){
        if (currentOpenWheel != null) {
            currentOpenWheel.moveSlotSelected(direction);
        }     }


  /**Appel la méthode des deux wheel*/
    public void renderActualTool(SpriteBatch batch) {
        meleeWeapons.renderActualTool(batch);
        rangedWeapons.renderActualTool(batch);
    }


    /**Appel la méthode du {@code activeWheel}*/
    public void render(SpriteBatch batch, BitmapFont font) {
        if (currentOpenWheel != null) {
            currentOpenWheel.render(batch, font);
        }  
    }  



}
