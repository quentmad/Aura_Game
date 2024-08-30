package aura_game.app.rework;

import aura_game.app.GameManager.Game;
import aura_game.app.rework.TypeEnum.BlockEntityType;
import aura_game.app.rework.TypeEnum.ToolType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

//Anciennement "Item"
public class BlockEntity extends AbstractEntity{
    private boolean breakable;
    private final List<ToolType> breakableBy;
    private final String type;
    private boolean movable;
    private final String description;
    private String color;


    public BlockEntity(BlockEntityType type, int casePosX, int casePosY, String color){
        //TODO !! attention hitbox parametres
        super(type.name(),type.imageWidth(), type.imageHeight(), type.contentImageWidth(), type.contentImageHeight(), type.offsetY(),
                type.maxLife(), type.maxLife(), new Hitbox(type.getApproximativeHitbox(),type.getPreciseHitbox()), Game.getInstance().getPhysicsComponent(), type.stature(), type.deathLoot(), false, type.getFullNameFileFromColor(color), (casePosX - 1) * 32 + 1, (casePosY - 1) * 32 + 1);
        this.breakable = type.breakable();
        this.breakableBy = type.breakableByFromType();
        this.type = type.type();
        this.movable = type.movable();
        this.description = type.description();
        this.color = color;
        String fullpath = type.getFullNameFileFromColor(color);
        if(fullpath == null){
            throw new IllegalArgumentException("The color "+color+ "doesn't exist for "+type.name());
        }

    }

    //getters
    public boolean breakable(){
        return breakable;
    }

    public List<ToolType> breakableBy(){
        return breakableBy;
    }

    public boolean movable(){
        return movable;
    }


    public int z(){
        return this.posC().y() + offsetY();
    }


    /**
     * Déplace le block de dx et dy
     * Avance de 1 en 1 pour éviter la sortie de la map
     * @param dx déplacement en x
     * @param dy déplacement en y
     */
    public void move(int dx, int dy) {
        if(!movable){
            return;
        }
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

    /**
     * Render le block
     * @param batch
     * @param region actuelle
     */
    @Override
    public void render(SpriteBatch batch, Region region) {
        batch.draw(texture(), getPosOnScreenX(region.camera().position().x()), getPosOnScreenY(region.camera().position().y()));
        ui().drawBar(this, batch);
    }
}
