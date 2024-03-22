package aura_game.app.Objects;

import aura_game.app.ToolManager;
import aura_game.app.Type.EntityType;

public class PlayableEntity extends Entity {

    private ToolManager toolManager;

    public PlayableEntity(EntityType typeSheet, int speed) {
        super(typeSheet, speed, 20, 20);
        toolManager = new ToolManager(this);
    }

    public ToolManager getToolManager() {
        return toolManager;
    }
}
