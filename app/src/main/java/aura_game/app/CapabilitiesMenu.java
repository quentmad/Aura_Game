package aura_game.app;

import aura_game.app.GameManager.FontManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CapabilitiesMenu extends BasicMenu {
    private static CapabilitiesMenu instance;

    private CapabilitiesMenu(String colorMenu) {
        super(colorMenu);
    }

    public static CapabilitiesMenu getInstance() {
        if (instance == null) {
            instance = new CapabilitiesMenu("BLUE");
        }
        return instance;
    }


    public void render(SpriteBatch batch) {
        // Affichez le menu à l'écran
        menuSprite.draw(batch);

        drawTextCenterIn("CAPABILITIES",640,600,2.0f,batch, FontManager.getInstance().getFontMenuTitle());
        drawTextCenterIn("Here you can see the capabilities and skills of Aiden and Aura...",990,400,1.0f,batch, FontManager.getInstance().getFontDescription());
    }

}
