package aura_game.app.rework;

import aura_game.app.CraftableBlock.CraftBlockManager;
import aura_game.app.GameManager.Game;
import aura_game.app.rework.TypeEnum.ActorEntityType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends ToolWieldingEntity{

    CraftBlockManager craftBlockManager;

    public Player(ActorEntityType type, int posX, int posY) {
        super (type, posX, posY);
    }

    public void initialize(CraftBlockManager craftBlockManager){
        this.craftBlockManager = craftBlockManager;
    }

    public void render(SpriteBatch batch, Region region){
        batch.draw(spriteSheetRegions()[stateComponant().getCurrentState().getCurrentSpriteY()][stateComponant().getCurrentState().getCurrentSpriteX()], getPosOnScreenX(region.camera().position().x()), getPosOnScreenY(region.camera().position().y()));
        toolManager().render(batch);
    }


    @Override
    public void move(int dx, int dy){
        super.move(dx, dy);
        craftBlockManager.calculClosestBlockInCreation(this);
    }
}
