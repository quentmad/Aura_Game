package aura_game.app;

import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aura_game.app.GameManager.AudioManager;

/**Utile pour gérer l'affichage des points commun entre le menu de inventory et crafting */
public abstract class BasicMenu {

    protected Sprite menuSprite;
    protected Sprite selectedSlotSprite;   

    //-----------DRAW INVENTORY-------------
    protected final int lootWidth;
    protected final int lootHeight;
    private final int startSlotX;// Position de départ de la première image de loot
    private final int startSlotY;
    private final int padding;
    private final int numColumns;

    /**slot selectionné, permet de mettre le carré de selection sur le bon slot...*/
    protected int slotSelected;
    /**Permet d'afficher les items de manière plus petit que la taille du slot*/
    protected final int marge;
    /**Nombre de slots total dans le menu (à gauche)*/
    protected final int nbSlotsMenu;
    protected int slotRestantMenu;
    private AudioManager audioManager;    

    

    public BasicMenu(String nameMenu, String nameSelectedSlot, int lootWidth, int lootHeight, int startSlotX,
            int startSlotY, int padding, int numColumns, int slotSelected, int marge, int nbSlotsMenu) {
        menuSprite = new Sprite(new Texture(Gdx.files.internal("src/main/resources/"+nameMenu+".png")));
        menuSprite.setCenter(600, 300);
        this.selectedSlotSprite = new Sprite (new Texture(Gdx.files.internal("src/main/resources/"+nameSelectedSlot+".png")));;
        this.lootWidth = lootWidth;
        this.lootHeight = lootHeight;
        this.startSlotX = startSlotX;
        this.startSlotY = startSlotY;
        this.padding = padding;
        this.numColumns = numColumns;
        this.slotSelected = slotSelected;
        this.marge = marge;
        this.nbSlotsMenu = nbSlotsMenu;
        this.slotRestantMenu = nbSlotsMenu;
    }


    public BasicMenu(String colorMenu) {
        //Textures et sprites:
        // Initialisez le sprite du menu avec la texture chargée
        menuSprite = new Sprite(new Texture(Gdx.files.internal("src/main/resources/MENU_"+colorMenu+".png")));
        menuSprite.setCenter(600, 300);
        // Initialisez le sprite du slot selectionné avec la texture chargée
        selectedSlotSprite = new Sprite (new Texture(Gdx.files.internal("src/main/resources/selectedSlot.png")));
        //Valeurs:
        this.lootWidth = 80;
        this.lootHeight = 80;
        this.startSlotX = 180;// Position de départ de la première image de loot
        this.startSlotY = 371;
        this.padding = 10;
        this.numColumns = 6;
        this.slotSelected = 0;
        this.marge = 10;
        this.nbSlotsMenu = 24;
        this.slotRestantMenu = nbSlotsMenu;
        audioManager = AudioManager.getInstance();
        
    }


//GETTERS
    public AudioManager getAudioManager(){
        return audioManager;
    }

    /**
     * @param direction "R" ou "L"
     * @return
     */
    public int moveSlotSelected(String direction){//TODO: vers haut et bas aussi
        //Si on appuie sur gauche et qu'on peut encore aller à gauche
         if(direction == "L" && slotSelected > 0){
            audioManager.playSound(audioManager.getSoundMoveSelectedItem(), 0.1f);
            return --slotSelected;
         }
         else if(direction == "R"  && slotSelected < (nbSlotsMenu-slotRestantMenu)-1){
            audioManager.playSound(audioManager.getSoundMoveSelectedItem(), 0.1f);
            return ++slotSelected;
        }
        return slotSelected;
        
    }


    public void drawTextCenterIn(String text, int xCenter,int y,float taille, SpriteBatch batch, BitmapFont font){
        font.getData().setScale(taille);
        GlyphLayout layout = new GlyphLayout(font, text); //Mesure la largeur du texte
        float x = xCenter - layout.width / 2; //Calcul de l'emplacement x pour centrer le texte
        font.draw(batch, text, x, y); //Dessinez le texte centré
        font.getData().setScale(1.0f);
    }

    public Pair<Integer,Integer> getXYPositionLoot(int i){
        int x = startSlotX + (i % numColumns) * (lootWidth + padding);
        int y = startSlotY - (i / numColumns) * (lootHeight + padding);
        return Pair.of(x, y);

    }

    public void dispose() {
        menuSprite.getTexture().dispose();
        selectedSlotSprite.getTexture().dispose();
    }

}