package aura_game.app.rework;

import aura_game.app.GameManager.Game;
import aura_game.app.Util.Triplet;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;

public class MovableEntity extends AbstractEntity{

    private int speed;

    public MovableEntity(String name,int imageWidth, int imageHeight, int contentImageWidth, int contentImageHeight, int offsetY,      float durability, float maxDurability, Hitbox hitbox, int stature, List<Triplet<String,Integer,Integer>> deathLoot, boolean onGround, String fileTexture, int posX, int posY,   int speed) {
        super(name, imageWidth, imageHeight, contentImageWidth, contentImageHeight, offsetY, durability, maxDurability, hitbox, Game.getInstance().getPhysicsMovableComponent(), stature, deathLoot, onGround, fileTexture, posX, posY);
        this.speed = speed;
    }


    /**
     * Déplace l'entité de dx et dy
     * Avance de 1 en 1 pour éviter la sortie de la map
     * @param dx déplacement en x
     * @param dy déplacement en y
     */
    public void move(int dx, int dy) {
        //System.out.println("dx dy" + dx + " " + dy);
        int bx = posC().x();
        int by = posC().y();
        int remainingDx = Math.abs(dx);
        int remainingDy = Math.abs(dy);

        int stepMoveDx = (dx > 0) ? 1 : -1;
        while (remainingDx != 0) {

            if (isXOnRegion(posC().x() + stepMoveDx)) {
                this.addToPosCX(stepMoveDx);
                remainingDx -= 1;
            } else {
                break; // Stop le déplacement si collision ou bord atteint
            }
        }
        int stepMoveDy = (dy > 0) ? 1 : -1;
        while (remainingDy != 0) {

            if (isYOnRegion(posC().y() + stepMoveDy)) {
                this.addToPosCY(stepMoveDy);
                remainingDy -= 1;
            } else {
                break; // Stop le déplacement si collision ou bord atteint
            }
        }

        hitbox().update(posC().x() - bx,posC().y() - by);
    }


    @Override
    public Grid getAdaptedGrid(){
        return physics().gridActorEntity();
    }

    public int speed() {
        return speed;
    }
}
