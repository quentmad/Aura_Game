package aura_game.app.rework;

import aura_game.app.GameManager.Game;
import aura_game.app.Util.Util;
import aura_game.app.rework.TypeEnum.LootableObjectType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class LootableObject extends AbstractObject {
    private final String name;//stick, leaf...
    private String type; // Type de l'objet de butin.
    private Texture shadowTexture;// Sprite de l'ombre de l'objet de butin.
    private String description;
    //Fusion avec onGround?
    //protected boolean collected; --> OnGround
    private boolean bouncing; // Indique si l'objet de butin est en train de rebondir.
    private float bounceY; // Hauteur de rebond de l'objet de butin.
    private int maxBounceY;//Hauteur max du saut
    private Vector2 dir; // Direction de l'objet de butin (vecteur).
    private Player player;
    private boolean bouncingUp;

    // Constructeur de Loot //TODO + LootType
    public LootableObject(LootableObjectType lt, int x, int y, boolean bounce, Vector2 dir, boolean onGround) {
        super (lt.name(),lt.imageWidth(), lt.imageHeight(), lt.contentImageWidth(), lt.contentImageHeight(), lt.offsetY(),lt.texturePath(),x, y);
        this.name = lt.name();
        this.type = lt.type();
        this.shadowTexture = new Texture(lt.shadowTexturePath());
        this.description = lt.description();
        //this.collected = collected;
        //TEMPO
        this.setOnGround(onGround);
        this.bouncing = false;
        this.bounceY = 0;
        this.maxBounceY = 20;
        this.dir = dir;
        this.bouncingUp =true;
        player = Game.getInstance().getPlayer();
        if(onGround){
            Game.getInstance().getRegion().interactionComponent().abstractObjectsOnGround().add(this);
        }
        if (bounce && onGround) {
            startBounce();
        }

    }

    public String getName(){
        return name;
    }

    public Texture getShadowTexture() {
        return shadowTexture;
    }

    public String getType() {
        return type;
    }

    public int getBounceY(){
        return Math.round(bounceY);
    }

    /**
     * Met à jour l'objet de butin.
     * @param dt Delta-temps.
     */
    public void update(float dt) {

        // Met à jour la position si en train de rebondir et une direction est spécifiée
        if (bouncing && dir != null) {
            addToPosCX(Math.round(dir.x * dt*3));
            addToPosCY(Math.round(dir.y * dt*3));
        }

        float distanceToPlayer = Util.distanceBetween(posC().x(), posC().y(), player.getCenterX(), (float)player.posC().y());

        //Si le loot est à proximité du joueur et n'est pas déjà ramassé ou en train de rebondir
        if (distanceToPlayer < 20 && isOnGround() && !bouncing) {
            this.setOnGround(false);
        }

        if (bouncing) {
            bounce(dt);
        }

    }



    public void bounce(float dt) {
        if (bouncingUp) {
            bounceY -= dt * 4;//bounceSpeed;//TODO: voir processing gravité
            if (bounceY <= -24) {
                bouncingUp = false;
            }
        } else {
            bounceY += dt * 4;//bounceSpeed;
            if (bounceY >= 0) {
                bouncingUp = true;
                bouncing = false;
                bounceY = 0;
            }
        }
    }

    /**
     * Méthode pour activer le rebondissement
     */
    public void startBounce() {
        bouncing = true;
        bounceY = 0;
    }

    @Override
    public int z() {
        return posC().y();
    }

    /**
     * Rendu de l'objet de butin.
     * @param batch
     * @param region
     */
    @Override
    public void render(SpriteBatch batch, Region region){
        batch.draw(getShadowTexture(),getPosOnScreenX(region.camera().position().x()), getPosOnScreenY(region.camera().position().y()+getBounceY()),32,32 );//TODO 64 to 32 ok ????
        batch.draw(texture(),getPosOnScreenX(region.camera().position().x()), getPosOnScreenY(region.camera().position().y()+getBounceY()),32,32 );

    }
}
