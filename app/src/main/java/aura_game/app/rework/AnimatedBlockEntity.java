package aura_game.app.rework;

import aura_game.app.LPCActions.Animation;
import aura_game.app.Util.Triplet;
import aura_game.app.rework.TypeEnum.BlockEntityType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

public class AnimatedBlockEntity extends BlockEntity{
    private Animation animation;
    private float frameDurationCounter = 0;
    private int currentSpriteX = 0;

    private TextureRegion[][] spriteSheetRegions;// Division de l'image en sous-images correspondant à chaque sprite.de la forme Y X

    public AnimatedBlockEntity(BlockEntityType type, int casePosX, int casePosY, String color){

        super(type, casePosX, casePosY, color);
        this.spriteSheetRegions  = TextureRegion.split(texture(), imageWidth(), imageHeight());
        this.animation = new Animation(0, (this.texture().getWidth()/type.imageWidth())-1,true);

    }

    public void update(float dt){
        updateSpriteXWithDuration(dt);
    }

    /**
     * Ajoute 1 au temps de la frame actuelle et passe à la frame suivante si besoin.
     * @return true si l'animation a effectué un "tour" complet.
     */
    public boolean updateSpriteXWithDuration(float dt) {
        frameDurationCounter+= dt;
        if(frameDurationCounter >= animation.getFrameDuration(currentSpriteX) ) {
            frameDurationCounter = 0;
            return incrementCurrentSpriteX();//Execution complete de l'animation ?
        }
        return false;
    }

    /**Met à jour le spriteX en lui attribuant le spriteX suivant de l'action actuelle
     * @return true si l'animation a effectué un tour complet (est revenu à 0)
     */
    private boolean incrementCurrentSpriteX(){//TODO: on fait vraiment le 0 au debut ?
        int old = currentSpriteX;
        currentSpriteX = (currentSpriteX + 1);
        if(currentSpriteX > animation.getLastIndexX()){//On a fait un tour complet
            currentSpriteX = 0;
            return true;
        }
        return false;

    }

    @Override
    public void render(SpriteBatch batch, Region region) {
        batch.draw(spriteSheetRegions[0][currentSpriteX], this.getPosOnScreenX(region.camera().position().x()), this.getPosOnScreenY(region.camera().position().y()));
    }

}
