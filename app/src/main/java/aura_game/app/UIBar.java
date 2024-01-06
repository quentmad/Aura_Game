package aura_game.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import aura_game.app.Objects.CollidableObject;

/**Cette classe permet de génerer les coeurs/barres... des CollidableObject */
public class UIBar {//RENOMMER LIFE ??? et include life et maxlife, hurt... ?
    
//Placé 10 au dessus du CollidableObject, ne s'affiche que si la barre est pas entière, et disparait au bout de 5s si il ne prend pas d'autres dégats

private final Texture bar_font;
private final Texture bar_red;
private final float barWidth;
protected final float barHeight;
/**Barre mis a jour  */
private int barWidthAlive;

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
     * On l'affiche en centerX et au niveau de son 'tall'
     * La taille de la barre rouge est mis a jour avec hurt()
     */
    public void drawBar(CollidableObject obj, SpriteBatch batch){
        if(obj.getLife() < obj.getMaxLives() && obj.getLife() !=0 ){
            //TextureRegion region = new TextureRegion(bar_red, 0, 0, barWidthMaj, barHeight);
            int x = Math.round(obj.getCenterX() - getBarWidth()/2);
            int y = obj.getPosC_Y() + obj.getOffY()+ obj.getTall();
            batch.draw(bar_font, x, y);
            batch.draw(bar_red,x, y,getBarWidthAlive(),barHeight );

        }
    }
}
