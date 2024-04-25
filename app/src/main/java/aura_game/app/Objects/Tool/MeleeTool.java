package aura_game.app.Objects.Tool;

import aura_game.app.Type.ToolType;
import com.badlogic.gdx.math.Vector2;

public class MeleeTool extends Tool{
    public MeleeTool(ToolType toolType, int x, int y, boolean bounce, Vector2 dir, boolean collected) {
        super(toolType, x, y, bounce, dir, collected);
    }
}
