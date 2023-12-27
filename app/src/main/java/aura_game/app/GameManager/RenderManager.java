package aura_game.app.GameManager;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import aura_game.app.CraftingMenu;
import aura_game.app.InventoryMenu;
import aura_game.app.LootManager;
import aura_game.app.Region;
import aura_game.app.WheelMenus;
import aura_game.app.Objects.BasicObject;
import aura_game.app.Objects.IAEntity;
import aura_game.app.Objects.Item;
import aura_game.app.Objects.Loot;
import aura_game.app.Objects.PlayableEntity;
import aura_game.app.Weather.*;

/**
 * Responsable de l'affichage/render sur l'écran.
 */
public class RenderManager {

    private Region region;
    private PlayableEntity player;
    private InventoryMenu playerInventory;
    private CraftingMenu crafting;
    private WheelMenus wheelMenus;
    private Sky sky;
    private UpdateManager updateManager;
    /**Police pour afficher du texte */
    private BitmapFont font;
    /**lot de sprites pour les graphiques 2D */
    private SpriteBatch batch;
    /**pour dessiner des formes */
    private ShapeRenderer shapeRenderer;//VIRER C'est pour draw les polygons pour tester colissions zone
    private LootManager lootManager;
    /**
     * Initialise les dépendances nécessaires pour le gestionnaire de rendu. Cette méthode joue un rôle clé dans
     * l'application du modèle de conception "Pattern Observer" (Observateur) combiné au principe d'"Injection de Dépendances".
     * L'utilisation de ce modèle et de ce principe vise à maintenir une structure de code souple et modulaire,
     * en permettant aux classes de s'observer mutuellement sans créer de dépendances directes.
     * 
     * Cette méthode permet d'éviter la création de dépendances cycliques entre les classes et facilite les interactions
     * entre les gestionnaires de manière fluide. Elle initialise les objets et attributs requis pour le rendu, tels que
     * la région, le joueur, l'inventaire du joueur et le ciel. De plus, elle configure des éléments essentiels pour
     * le rendu, comme le font pour le texte, le lot de sprites pour les graphiques 2D et le ShapeRenderer pour les formes.
     * 
     * @param region La région du jeu à afficher.
     * @param player Le joueur jouable de la partie.
     * @param inventory L'inventaire du joueur.
     * @param sky Le ciel du jeu.
     */
    public void initialize(Region region, PlayableEntity player, InventoryMenu inventory,CraftingMenu crafting, Sky sky, UpdateManager updateManager, WheelMenus wheelMenu) {
        this.region = region;
        this.player = player;
        this.playerInventory = inventory;
        this.crafting = crafting;
        this.wheelMenus = wheelMenu;
        this.sky = sky;
        this.updateManager = updateManager ;
        this.lootManager = LootManager.getInstance();

        //Initialisation des éléments pour le rendu
        this.font = new BitmapFont(); 
        //this.font = new BitmapFont(Gdx.files.internal("src/main/resources/FUTURAM.ttf"),false);
        this.batch = new SpriteBatch(); 
        this.shapeRenderer = new ShapeRenderer();
    }

    
    /**Render de tous les élements que l'ont veut afficher à l'écran */
    public void render() {
        batch.begin();
        float dt = Gdx.graphics.getDeltaTime();//DELTA TIME 
        renderMap();
        renderObjects();
        renderSky(dt);
        //render les elements heart, slots tools...
        wheelMenus.renderActualTool(batch);
        renderActiveMenu();
        batch.end();
        tempoRenderZoneDegatPlayer();
    }
    /**Pour tester/verifier */
    public void tempoRenderZoneDegatPlayer(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Rectangle r = player.zoneDegatFromDirection();
        shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(),r.getHeight());
        shapeRenderer.end();
    }

    private void renderMap() {
        batch.draw(region.getCarte(), -region.getCamera().getX(), -region.getCamera().getY());
    }

    private void renderObjects(){
        //Affichage en fonction de la profondeur
        boolean boyDrawn = false;
        //System.out.println("sprite player :" +player.getCurrentSpriteX());
        for(BasicObject obj :region.getBasicObjectsOnRegion()){
            if(!boyDrawn && player.getZProf()>obj.getZProf()){//Y du boy <-> Z
                batch.draw(player.getSpriteSheetInfo().spriteSheetRegions()[player.getCurrentSpriteY()][player.getCurrentSpriteX()], player.getPosOnScreenX(region.getCamera().getX()), player.getPosOnScreenY(region.getCamera().getY()));
                renderTool();
                boyDrawn = true;
            }
            if(obj instanceof Item){
                Item item = (Item) obj; // Conversion forcée au type Item
                batch.draw(item.getTextureItem(),item.getPosOnScreenX(region.getCamera().getX()), item.getPosOnScreenY(region.getCamera().getY()) );
                item.getUI().drawBar(item, batch);
            }else if(obj instanceof Loot){
                Loot l = (Loot) obj;
                batch.draw(l.getShadowTexture(),l.getPosOnScreenX(region.getCamera().getX()), l.getPosOnScreenY(region.getCamera().getY()+l.getBounceY()),32,32 );//TODO 64 to 32 ok ????
                batch.draw(l.getTexture(),l.getPosOnScreenX(region.getCamera().getX()), l.getPosOnScreenY(region.getCamera().getY()+l.getBounceY()),32,32 );
            }
            else{//Entity
                IAEntity entity = (IAEntity) obj;
                //System.out.println("Y "+entity.getCurrentSpriteY()+ "X "+entity.getCurrentSpriteX());
                batch.draw(entity.getSpriteSheetInfo().spriteSheetRegions()[entity.getCurrentSpriteY()][entity.getCurrentSpriteX()], entity.getPosOnScreenX(region.getCamera().getX()), entity.getPosOnScreenY(region.getCamera().getY()));
                entity.getUI().drawBar(entity, batch);
            }
        }
        if(!boyDrawn){     
            batch.draw(player.getSpriteSheetInfo().spriteSheetRegions()[player.getCurrentSpriteY()][player.getCurrentSpriteX()], player.getPosOnScreenX(region.getCamera().getX()), player.getPosOnScreenY(region.getCamera().getY()));
            renderTool();
        }
    
    }

    public void renderActiveMenu(){
        switch(updateManager.activeMenu()){
            case "game":
                break;
            case "inventory":
                playerInventory.render(batch, font);
                break;
            case "crafting":
                crafting.render(batch, font);
                break;
            case "wheel":
                wheelMenus.render(batch, font);
                break;
            default:
                break;

        }
    }

    private void renderSky(float deltaTime) {
        sky.render(batch, deltaTime/10);
    
    }

    private void renderPolygonsForTest(){
        for(BasicObject obj : region.getBasicObjectsOnRegion()){
            if(obj instanceof Item){
                Item it = (Item) obj;
                // Commencez le dessin
                ShapeRenderer shapeRenderer = new ShapeRenderer();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.polygon(it.getHitboxPolygon().getVertices());
                shapeRenderer.end();
            }
        }
    }


    /**Affiche l'outil/arme selectionné actuellement par le joueur, ou n'affiche rien si il n'y a rien de selectionné */
    public void renderTool(){

        if( player.getCurrentToolName()  != "" && player.getCurrentToolSizeSprite() != -1){
            //Décallage de x et y si on a une sprite plus grande que celle du personnage
            int marge = (player.getCurrentToolSizeSprite() - 64);
            if(marge>0){marge/=2;}else{marge=0;}
            TextureRegion[][] textureReg= player.getTextureRegionTool(player.getCurrentToolSizeSprite() );
            batch.draw(textureReg[player.getCurrentToolSpriteY()][player.getCurrentSpriteX()], player.getPosOnScreenX(region.getCamera().getX())-marge, player.getPosOnScreenY(region.getCamera().getY())-marge);
        }
    }
    public void dispose() {
        // Libérer les ressources utilisées par le renderer
        batch.dispose();
        playerInventory.dispose();
        player.getSpriteSheetInfo().texture().dispose();
        font.dispose();
    }
    
}
