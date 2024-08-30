package aura_game.app.GameManager;


import aura_game.app.rework.Debug;
import aura_game.app.rework.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Classe qui gère les infos communs à tous les menus.
 */
public class CommonInfoMenu {

    private static CommonInfoMenu instance;
    private Texture heartFull;
    private Texture heartEmpty;
    private Texture heartHalf;
    private Player player;
    private Menu currentMenu;

    //private Sprite playerHead;
    private Sprite leftButton;
    private Sprite rightButton;


    private CommonInfoMenu(){

    }

    public void initialize(Player player){
        this.player = player;
        this.heartFull = new Texture("src/main/resources/UI/heart_full2.png");
        this.heartEmpty = new Texture("src/main/resources/UI/heart_empty2.png");
        this.heartHalf = new Texture("src/main/resources/UI/heart_half2.png");

        //this.playerHead = new Sprite(new Texture("src/main/resources/player_head.png"));
        this.leftButton = new Sprite(Debug.getInstance().getKeyboardButtons(4,6));
        leftButton.setCenter(60,40);
        this.rightButton = new Sprite(Debug.getInstance().getKeyboardButtons(4,7));
        rightButton.setCenter(1220,40);
        //this.currentMenu = Menu.STORY;
    }

    public static CommonInfoMenu getInstance(){
        if(instance == null){
            instance = new CommonInfoMenu();
        }
        return instance;
    }

    public Menu getCurrentMenu(){
        return currentMenu;
    }

    /**
     *
     * @return le menu de gauche du menu actuellement affiché ou null si on est sur le premier menu
     */
    public Menu getLeftMenu(){
        if(currentMenu.ordinal() == 0){
            return null;
        }
        return Menu.values()[currentMenu.ordinal()-1];
    }

    public void setCurrentMenu(Menu menu){
        this.currentMenu = menu;
    }

    /**
     *
     * @return le menu de droite du menu actuellement affiché ou null si on est sur le dernier menu
     */
    public Menu getRightMenu(){
        if(currentMenu.ordinal() == Menu.values().length-1){
            return null;
        }
        return Menu.values()[currentMenu.ordinal()+1];
    }

    /**
     * Affiche les coeurs de vie du joueur, lorsque le menu est ouvert
     * @param life
     */
    public void renderHeart(int maxLife, float life, SpriteBatch batch){
        if(life >0) {
            int nbHeartFull = (int) Math.floor(life);
            int isHalf = life % 1 == 0 ? 0 : 1;
            int nbHeartEmpty = maxLife - nbHeartFull - isHalf;
            int lastDraw = 0;
            for (int i = 0; i < nbHeartFull; i++) {
                batch.draw(heartFull, 160 + i * 22, 606);
                lastDraw++;
            }
            if (isHalf == 1) {
                batch.draw(heartHalf, 160 + lastDraw * 22, 606);
                lastDraw++;
            }
            for (int i = 0; i < nbHeartEmpty; i++) {
                batch.draw(heartEmpty, 160 + lastDraw * 22, 606);
                lastDraw++;
            }
        }

    }

    public void render(SpriteBatch batch){
        renderHeart((int)player.maxDurability(), player.durability(), batch);
        if(getLeftMenu()!=null){
            leftButton.draw(batch);
            Debug.getInstance().drawTextBeginIn(getLeftMenu().name, 100, 50, 1.5f, batch, FontManager.getInstance().getFontDescription());
        }

        if(getRightMenu()!=null){
            rightButton.draw(batch);
            Debug.getInstance().drawTextEndIn(getRightMenu().name, 1180, 50, 1.5f, batch, FontManager.getInstance().getFontDescription());
        }
    }








    enum Menu {

        STORY,
        MAP,
        INVENTORY,
        CRAFT_LOOT,
        CRAFT_BLOCK,
        CAPABILITIES;
        //for GAME; --> null

        private String name;
        private Texture icon;

        Menu() {
            this.name = this.name().toLowerCase();
            //this.icon = new Texture("src/main/resources/" + name+"icon.png");
        }


    }
}