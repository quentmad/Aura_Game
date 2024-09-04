package aura_game.app.Weather;

import aura_game.app.LPCActions.Animation;
import aura_game.app.rework.Hitbox;
import aura_game.app.rework.Particle;
import aura_game.app.rework.Point;
import aura_game.app.rework.Region;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.apache.commons.lang3.tuple.Pair;

/**Goute de pluie */
public class Drop extends Particle {
    private int lifetime; // Durée de vie de la goutte
    private long startTime; // Temps de départ de la goutte
    private boolean moving; // Indicateur de mouvement de la goutte
    private boolean toRemove; // Indicateur de suppression de la goutte
    private final Point initialPos;

    public Drop(int size, Point pos, int startTime, int width, int height) {
        super("drop_"+size,24, 24, 24, 24, 0, new Hitbox(new Rectangle(pos.x(),pos.y(), width, height),new Polygon(new float[]{pos.x(),pos.y(), pos.x()+width,pos.y(), pos.x()+width,pos.y()+height, pos.x()+width,pos.y()+height})), 0, null, false, "src/main/resources/Particles/drop_"+size+".png", pos.x(), pos.y(), (int)((Math.random() * 50) + 200), new Vector2(-1, -2), new Animation(0,2, new int[]{2,2,3}, false), "src/main/resources/Particles/drop_floor_"+(int) ((Math.random() * 3) + 1)+".png");
        this.initialPos = new Point(pos.x(),pos.y());
        this.moving = true;
        this.lifetime = (int) (Math.random() * 20) + 1;
        this.startTime = startTime;
        this.moving = true;
        this.toRemove = false;
    }

    /**
     * Met à jour la position de la goutte en fonction du temps écoulé.
     *
     * @param deltaTime Temps écoulé depuis la dernière frame.
     */
    public void update(float deltaTime, int currentTime) {
        if (moving) {
            move((int)(direction.x * speed() * deltaTime),(int)(direction.y * speed() * deltaTime)) ;//SORTIE DE MAP PB ? OVERRIDE ??
        }else{//On floor
            Pair<Boolean,Integer> p = animation().returnUpdatedSpriteXWithDuration(currentSpriteX());
            boolean finish = p.getLeft();
            this.setCurrentSpriteX(p.getRight());
            if(finish) {
                toRemove = true;
            }
        }
        if (moving && currentTime - startTime >= lifetime) {//floor puis remove
            hitbox().update(posC().x()- initialPos.x(),posC().y()- initialPos.y());
            moving = false;
            //n'explose pas si il y a collision là où il doit exploser
           // System.out.println("Drop collide ?: " + physics().isColliding(this, new Point(posC().x(),posC().y())));
            if(this.posC().x()<5 || this.posC().y() < 5 ||  Math.random() < 0.85 || physics().isColliding(this, new Point(posC().x(),posC().y())) != 0){
                toRemove = true;
            }
        }
    }
    /**
     * Déplace l'entité de dx et dy
     * NE se préocupe pas de la sortie de la map
     * @param dx déplacement en x
     * @param dy déplacement en y
     */
    @Override
    public void move(int dx, int dy) {
        this.addToPosCX(dx);
        this.addToPosCY(dy);
    }


    public boolean toRemove(){
        return toRemove;
    }

    public boolean isMoving(){
        return moving;
    }

    /**
     * Rendu de la goutte (dans le ciel ou sur le sol).
     */
    @Override
    public void render(SpriteBatch batch, Region region) {
        if (moving) {
            batch.draw(texture(), getPosOnScreenX(region.camera().position().x()), getPosOnScreenY(region.camera().position().y()));
        }else {
            //floor
            super.render(batch, region);
        }

    }
}