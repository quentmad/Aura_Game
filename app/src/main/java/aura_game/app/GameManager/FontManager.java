package aura_game.app.GameManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontManager {
    private static FontManager instance;

    /**TitleTools font*/
    BitmapFont fontTitle2;

    BitmapFont fontDescription;

    BitmapFont fontMenuTitle;


    private FontManager() {
        FreeTypeFontGenerator generator1 = new FreeTypeFontGenerator(Gdx.files.internal("src/main/resources/font/TitleTools.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter1.size = 15; // Set the font size
        fontTitle2 = generator1.generateFont(parameter1);

        FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("src/main/resources/font/DescriptionMenu.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 20; // Set the font size
        fontDescription = generator2.generateFont(parameter2);

        FreeTypeFontGenerator generator3 = new FreeTypeFontGenerator(Gdx.files.internal("src/main/resources/font/nasalization-rg.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter3.size = 26; // Set the font size
        fontMenuTitle = generator3.generateFont(parameter3);

    }



    public static FontManager getInstance() {
        if (instance == null) {
            instance = new FontManager();
        }
        return instance;
    }

    public BitmapFont getFontTitle2() {
        return fontTitle2;
    }

    public BitmapFont getFontDescription() {
        return fontDescription;
    }

    public BitmapFont getFontMenuTitle() {
        return fontMenuTitle;
    }

    public void dispose() {
        fontTitle2.dispose();
        fontDescription.dispose();
        fontMenuTitle.dispose();
    }

}
