package aura_game.app;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aura_game.app.Objects.PlayableEntity;
import aura_game.app.Objects.Tool;

/**Class singleton regroupant tous les wheels différents (ranged, melee...) */
public class WheelMenus {
    private static WheelMenus instance;

    private Wheel<Tool> meleeWeapons;
    private Wheel<Tool> rangedWeapons;

    /**Wheel actuellement actif */
    private Wheel<Tool> activeWheel; //Un seul attribut pour gérer le menu actif


    private WheelMenus(String colorMenu ) {
        activeWheel = null;
        meleeWeapons = new Wheel<>(colorMenu, 12, 64, "slotWheelMenuMelee",0);
        rangedWeapons = new Wheel<>(colorMenu, 12, 64, "slotWheelMenuRanged",80);

    }

    public static WheelMenus getInstance() {
        if (instance == null) {
            instance = new WheelMenus("BLUE");
        }
        return instance;
    }

    

    public Wheel<Tool> getMeleeWeapons() {
        return meleeWeapons;
    }

    public Wheel<Tool> getRangedWeapons() {
        return rangedWeapons;
    }

    /**Defini le Wheel actif, si le string est un nom valide */
    public void setActiveWheel(String wheel) {
        //if (isValidWheelName(wheel)) {
            switch (wheel) {
                case "meleeWeapons":
                    activeWheel = meleeWeapons;
                    break;
                case "rangedWeapons":
                    activeWheel = rangedWeapons;
                    break;
                default:
                    activeWheel = null;
                    break;
            }
        //}
    }

    /**
     * @return {@code true} si w est un menu existant /valide, sinon {@code false} 
     */
    public boolean isValidWheelName(String w){
        switch(w){
            case "meleeWeapons":
                return true;
            case "rangedWeapons":
                return true;
            default:
            return false;
        }
    }

    /** 
     * Appel la méthode du {@code activeWheel}
     * Ce lance lorsqu'on appuie sur {@code enter} quand le wheelMenu est ouvert.
     * Defini l'arme actuelle (le nom) de cette categorie du joueur à l'arme du selected slotIndex
     * @param player
     */
    public void setActualTool(PlayableEntity player){
        if (activeWheel != null) {
            activeWheel.setActualToolCategory(player);
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
        if (activeWheel != null) {
            activeWheel.moveSlotSelected(direction);
        }     }


  /**Appel la méthode des deux wheel*/
    public void renderActualTool(SpriteBatch batch) {
        meleeWeapons.renderActualTool(batch);
        rangedWeapons.renderActualTool(batch);
    }


    /**Appel la méthode du {@code activeWheel}*/
    public void render(SpriteBatch batch, BitmapFont font) {
        if (activeWheel != null) {
            activeWheel.render(batch, font);
        }  
    }  



}
