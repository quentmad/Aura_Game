package aura_game.app.rework;

import aura_game.app.GameManager.FontManager;
import aura_game.app.GameManager.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import static aura_game.app.GameManager.Game.*;

public class Debug {

    private static Debug instance;

    private ShapeRenderer shapeRenderer;//VIRER C'est pour draw les polygons pour tester collisions zone

    private  TextureRegion[][] keyboardButtons;

    private Sprite spriteKeyboardButtonsMenuTexture;

    //public int drawButtons;

    private Debug() {
        this.shapeRenderer = new ShapeRenderer();
        keyboardButtons = TextureRegion.split(new Texture("src/main/resources/KeyboardButtons.png"), 48, 48);
        this.spriteKeyboardButtonsMenuTexture = new Sprite( new Texture("src/main/resources/help_commands_BLUE.png"));
        this.spriteKeyboardButtonsMenuTexture.setCenter(screenWidth/2, screenHeight/2);
        //drawButtons = 0;
        //test police
    }


    /**
     * Obtient une instance unique de la classe Game (Singleton).
     *
     * @return L'instance de Game.
     */
    public static Debug getInstance() {
        if (instance == null) {
            instance = new Debug();
        }
        return instance;
    }

    public TextureRegion getKeyboardButtons(int i, int j){
        return keyboardButtons[i][j];
    }

    public void renderZoneDegat(ActorEntity ent){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Rectangle r = ent.zoneDegatFromDirection(ent.stateComponant().getCurrentOrientation().getDirection());
        shapeRenderer.rect(r.getX() - camX(), r.getY() - camY(), r.getWidth(),r.getHeight());
        shapeRenderer.end();
    }

    public void renderHitboxPolygon(AbstractEntity ent){
        //System.out.println("points of the hitbox polygon \n" + ent.hitbox().preciseHitbox.getTransformedVertices()[0]);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //Decaller les points pour qu'ils soient bien affichés au bon endroit par rapport a la camera
        shapeRenderer.polygon(ent.hitbox().translateIntermediatePoints(ent.getPosOnScreenX(camX()),ent.getPosOnScreenY(camY())));
        //shapeRenderer.rect(ent.hitbox().approximativeHitbox.getX(), ent.hitbox().approximativeHitbox.getY(), ent.hitbox().approximativeHitbox.getWidth(), ent.hitbox().approximativeHitbox.getHeight());
        shapeRenderer.end();
    }

    public void drawPoint(int x, int y){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(x - camX(), y - camY(), 2,10);
        shapeRenderer.end();
    }

    /**
     * Affiche les commandes du clavier et le menu avec titre
     * @param batch
     */
    public void drawHelpButtons(SpriteBatch batch){
        spriteKeyboardButtonsMenuTexture.draw(batch);
        drawTextCenterIn("COMMANDS",640,600,2.5f,batch,FontManager.getInstance().getFontTitle2());

        int interval = 50;
        int decX1 = 240;
        int decX2 = 520;
        int beginY = 470;
        float size2 = 1.2f;
        batch.draw(keyboardButtons[3][0], decX1, beginY);//E
        drawTextBeginIn("menu", decX1 + 52, beginY+32,size2, batch, FontManager.getInstance().getFontDescription());

        batch.draw(keyboardButtons[5][4], decX1, beginY-interval);//C
        drawTextBeginIn("Accept an action", decX1 + 52, beginY+32-interval, size2,batch, FontManager.getInstance().getFontDescription());

        batch.draw(keyboardButtons[4][1], decX1, beginY-2*interval);//R ( [y vers bas][x])
        drawTextBeginIn("Wheel for ranged weapons", decX1 + 52, beginY+32-2*interval,size2, batch, FontManager.getInstance().getFontDescription());

        batch.draw(keyboardButtons[3][4], decX1, beginY-3*interval);//M
        drawTextBeginIn("Wheel for melee weapons", decX1 + 52, beginY+32-3*interval,size2, batch, FontManager.getInstance().getFontDescription());
        //walk
        batch.draw(keyboardButtons[0][0], decX1, beginY-4*interval);//TOP
        batch.draw(keyboardButtons[0][2], decX1+46, beginY-4*interval);//LEFT
        batch.draw(keyboardButtons[0][1], decX1+2*46, beginY-4*interval);//BOTTOM
        batch.draw(keyboardButtons[0][3], decX1+3*46, beginY-4*interval);//RIGHT
        drawTextBeginIn("Walk", decX1 + 52 + 138, beginY+32-4*interval,size2, batch, FontManager.getInstance().getFontDescription());

        batch.draw(keyboardButtons[6][6], decX1, beginY-5*interval);//Click left
        drawTextBeginIn("Slash", decX1 + 52, beginY+32-5*interval,size2, batch, FontManager.getInstance().getFontDescription());

        batch.draw(keyboardButtons[6][7], decX1, beginY-6*interval);//Click right
        drawTextBeginIn("Shoot", decX1 + 52, beginY+32-6*interval, size2,batch, FontManager.getInstance().getFontDescription());

        batch.draw(keyboardButtons[3][3], decX1, beginY-7*interval);//L
        drawTextBeginIn("Spellcast", decX1 + 52, beginY+32-7*interval, size2,batch, FontManager.getInstance().getFontDescription());

        batch.draw(keyboardButtons[3][7], decX1 + decX2, beginY);//P
        drawTextBeginIn("Thrust", decX1 + 52  + decX2, beginY+32, size2,batch, FontManager.getInstance().getFontDescription());

        batch.draw(keyboardButtons[6][5], decX1 + decX2, beginY-interval);//SPACE
        drawTextBeginIn("Jump", decX1 + 52 + decX2, beginY+32-interval,size2, batch, FontManager.getInstance().getFontDescription());

        batch.draw(keyboardButtons[2][3], decX1 + decX2, beginY-2*interval);//D
        drawTextBeginIn("Run", decX1 + 52 + decX2, beginY+32-2*interval,size2, batch, FontManager.getInstance().getFontDescription());

        batch.draw(keyboardButtons[4][4], decX1 + decX2, beginY-3*interval);//U
        drawTextBeginIn("Carry an object", decX1 + 52 + decX2, beginY+32-3*interval,size2, batch, FontManager.getInstance().getFontDescription());

        batch.draw(keyboardButtons[5][0], decX1 + decX2, beginY-4*interval);//Y
        drawTextBeginIn("Push an object", decX1 + 52 + decX2, beginY+32-4*interval,size2, batch, FontManager.getInstance().getFontDescription());

        batch.draw(keyboardButtons[5][4], decX1+decX2, beginY-5*interval);//<- (a ajouter)
        drawTextBeginIn("Drop the actual object", decX1 + 52 + decX2, beginY+32-5*interval,size2, batch, FontManager.getInstance().getFontDescription());

        batch.draw(keyboardButtons[2][0], decX1 + decX2, beginY-6*interval);//-> A
        batch.draw(keyboardButtons[5][1], decX1+46 + decX2, beginY-6*interval);//-> Z
        drawTextBeginIn("Set the current tool", decX1 + 52 +48 + decX2, beginY+32-6*interval, size2,batch, FontManager.getInstance().getFontDescription());

        //drawButtons--;

    }

    public int camX(){
        return Game.getInstance().getRegion().camera().position().x();
    }

    public int camY(){
        return Game.getInstance().getRegion().camera().position().y();
    }

    /**
     * Permet de dessiner les boutons pour un certain nombre de secondes (attention au nombre d'images par secondes)
     * @param i
     */
    /*public void drawButtonsForSeconds(int i) {
        drawButtons = i * 60;
    }*/



    /**
     * Dessine un texte centré dans une position donnée.
     *
     * @param text Le texte que l'on veut afficher.
     * @param xBegin La coordonnée x du début du texte.
     * @param y La coordonnée y du texte.
     * @param batch Le SpriteBatch utilisé pour le rendu.
     */
    public void drawTextBeginIn(String text, int xBegin, int y,float taille, SpriteBatch batch, BitmapFont font) {
        font.getData().setScale(taille);
        GlyphLayout layout = new GlyphLayout(font, text); //Mesure la largeur du texte
        font.draw(batch, text, xBegin, y); //Dessinez le texte centré
        font.getData().setScale(1.0f);

    }

    /**
     * Dessine un texte centré dans une position donnée.
     *
     * @param text Le texte que l'on veut afficher.
     * @param xEnd La coordonnée x de fin du texte.
     * @param y La coordonnée y du texte.
     * @param batch Le SpriteBatch utilisé pour le rendu.
     */
    public void drawTextEndIn(String text, int xEnd, int y,float taille, SpriteBatch batch, BitmapFont font) {
        font.getData().setScale(taille);
        GlyphLayout layout = new GlyphLayout(font, text); //Mesure la largeur du texte
        font.draw(batch, text, xEnd - layout.width, y); //Dessinez le texte centré
        font.getData().setScale(1.0f);
    }

    public void drawTextCenterIn(String text, int xCenter,int y,float taille, SpriteBatch batch, BitmapFont font){
        font.getData().setScale(taille);
        GlyphLayout layout = new GlyphLayout(font, text); //Mesure la largeur du texte
        float x = xCenter - layout.width / 2; //Calcul de l'emplacement x pour centrer le texte
        font.draw(batch, text, x, y); //Dessinez le texte centré
        font.getData().setScale(1.0f);
    }
}
