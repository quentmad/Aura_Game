package aura_game.app.rework;

import aura_game.app.GameManager.TextureManager;
import aura_game.app.LPCActions.Animation;
import aura_game.app.Util.Triplet;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Particle extends MovableEntity{

    protected Vector2 direction;
    private Animation animation;
    private TextureRegion[][] spriteSheetRegions;// Division de l'image en sous-images correspondant à chaque sprite.de la forme Y X
    private int currentSpriteX;

    public Particle(String name, int imageWidth, int imageHeight, int contentImageWidth, int contentImageHeight, int offsetY, Hitbox hitbox, int stature, List<Triplet<String,Integer,Integer>> deathLoot, boolean onGround, String texture, int posX, int posY, int speed, Vector2 direction, Animation animation, String textureanimPath) {
        super(name, imageWidth, imageHeight, contentImageWidth, contentImageHeight, offsetY, 0, 0, hitbox, stature, deathLoot, onGround,texture, posX, posY, speed);
        this.direction = direction;
        this.animation = animation;
        this.currentSpriteX = 0;
        Texture textureanim = TextureManager.getInstance().getTexture(textureanimPath);
        this.spriteSheetRegions  = TextureRegion.split(textureanim, imageWidth, imageHeight);

    }



    /**
     * Déplace l'entité de dx et dy
     * Ne se préocupe pas de la sortie de la map
     * @param dx déplacement en x
     * @param dy déplacement en y
     */
    @Override
    public void move(int dx, int dy) {
        this.addToPosCX(dx);
        this.addToPosCY(dy);

        hitbox().update(dx,dy);
    }

    public Animation animation(){
        return animation;
    }

    public int currentSpriteX(){
        return currentSpriteX;
    }

    public void setCurrentSpriteX(int currentSpriteX){
        this.currentSpriteX = currentSpriteX;
    }

    /**
     * Render the particle animation
     * @param batch
     * @param region
     */
    @Override
    public void render(SpriteBatch batch, Region region) {
        batch.draw(spriteSheetRegions[0][currentSpriteX], this.getPosOnScreenX(region.camera().position().x()), this.getPosOnScreenY(region.camera().position().y()));
    }


}
