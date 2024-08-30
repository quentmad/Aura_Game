package aura_game.app;

import aura_game.app.GameManager.Game;
import aura_game.app.rework.AbstractEntity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**Cette classe permet de génerer les coeurs/barres... des CollidableObject */
public class UIBar {//RENOMMER LIFE ??? et include life et maxlife, hurt... ?
    
//Placé 10 au dessus du CollidableObject, ne s'affiche que si la barre est pas entière, et disparait au bout de 5s si il ne prend pas d'autres dégats

private final Texture bar_font;
private final Texture bar_red;
private final int barWidth;
protected final int barHeight;
/**Barre mis a jour  */
private int barWidthAlive;

private int counterLastHurt;

    public UIBar(){
        this.bar_font =  new Texture(Gdx.files.internal("src/main/resources/UI/bar_font.png"));
        this.bar_red =  new Texture(Gdx.files.internal("src/main/resources/UI/bar_red.png"));
        this.barWidth = bar_font.getWidth();
        this.barHeight = bar_font.getHeight();
    }

    public float getBarWidth() {
        return barWidth;
    }

    public int getBarWidthAlive() {
        return barWidthAlive;
    }

    public void setBarWidthAlive(int barWidthAlive) {
        this.barWidthAlive = barWidthAlive;
    }

    /**Dessine la barre de vie au dessus des CollidableObjects en vie, dont la vie n'est pas entière.
     * On l'affiche en centerX et au niveau de sa stature, en fonction de la camera du jeu.
     * La taille de la barre rouge est mis a jour avec hurt()
     */
    public void drawBar(AbstractEntity obj, SpriteBatch batch){
        if(obj.durability() < obj.maxDurability() && obj.durability() !=0 && counterLastHurt < 100){
            int x = Math.round(obj.getCenterX() - getBarWidth()/2) - Game.getInstance().getRegion().camera().position().x();
            int y = obj.posC().y()+ obj.stature() + 25 - Game.getInstance().getRegion().camera().position().y();
            batch.draw(bar_font, x, y);                                     //TODO: pas propre le Game.getInstance().getRegion().camera().position().x()...
            batch.draw(bar_red,x, y,0, 0, getBarWidthAlive(),barHeight );

            counterLastHurt++;
        }
    }

    public void resetCounterLastHurt() {
        this.counterLastHurt = 0;
    }
}
