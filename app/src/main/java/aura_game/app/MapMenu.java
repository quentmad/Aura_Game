package aura_game.app;

import aura_game.app.GameManager.FontManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MapMenu extends BasicMenu {
    private static MapMenu instance;

    private MapMenu(String colorMenu) {
        super(colorMenu);
    }

    public static MapMenu getInstance() {
        if (instance == null) {
            instance = new MapMenu("BLUE");
        }
        return instance;
    }

    public void render(SpriteBatch batch) {
        // Affichez le menu à l'écran
        menuSprite.draw(batch);

        drawTextCenterIn("MAP",640,600,2.0f,batch, FontManager.getInstance().getFontMenuTitle());
        drawTextCenterIn("Here you can see the map, not implemented yet...",980,400,1.0f,batch, FontManager.getInstance().getFontDescription());
    }
}
