package aura_game.app;

import aura_game.app.GameManager.FontManager;
import aura_game.app.rework.TypeEnum.BlockEntityType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.apache.commons.lang3.tuple.Pair;

public class StoryMenu extends BasicMenu {
    private static StoryMenu instance;

    private StoryMenu(String colorMenu) {
        super(colorMenu);
    }

    public static StoryMenu getInstance() {
        if (instance == null) {
            instance = new StoryMenu("BLUE");
        }
        return instance;
    }

    public void render(SpriteBatch batch) {
        // Affichez le menu à l'écran
        menuSprite.draw(batch);

        drawTextCenterIn("STORY AND QUESTS",640,600,2.0f,batch, FontManager.getInstance().getFontMenuTitle());
        drawTextCenterIn("Here you can see the story and quests, not implemented yet...",980,400,1.0f,batch, FontManager.getInstance().getFontDescription());
    }
}
