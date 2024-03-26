package aura_game.app.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import aura_game.app.Region;
import aura_game.app.GameManager.Game;
import aura_game.app.Type.LootType;
import aura_game.app.Util.Util;

/**
 * Classe représentant les objets de butin dans le jeu.
 */
public class Loot extends BasicObject{
    private final String name;//stick, leaf...
    private String type; // Type de l'objet de butin (flèche, bombe, clé, etc.).
    /**Size 32 * 32*/
    private Texture texture;// Sprite de l'objet de butin.
    private Texture shadowTexture;// Sprite de l'ombre de l'objet de butin.
    private int offY;
    //private int frameH;
    //private int frameW;
    private String description;
    //private float x; // Position horizontale de l'objet de butin.
    //private float y; // Position verticale de l'objet de butin.
    //public float rot; // Rotation de l'objet de butin.
    /**Si l'objet de butin est mort /dans l'inventaire ou au sol... (ramassé ou disparu)*/
    protected boolean collected;
    private boolean bouncing; // Indique si l'objet de butin est en train de rebondir.
    private float bounceY; // Hauteur de rebond de l'objet de butin.
    private int maxBounceY;//Hauteur max du saut
    private Vector2 dir; // Direction de l'objet de butin (vecteur).
    private PlayableEntity player;
    private Region region;
    private LootType lootType;//LootType correspondant au loot
    private boolean bouncingUp;

    // Constructeur de Loot //TODO + LootType
    public Loot(LootType lt, int x, int y, boolean bounce, Vector2 dir, boolean collected) {
        super(x,y);
        this.name = lt.getName();
        this.type = lt.type();
        this.texture = new Texture(lt.texturePath());
        this.shadowTexture = new Texture(lt.shadowTexturePath());
        this.offY = lt.offY();
        this.description = lt.description();
        this.lootType = lt;
        //this.rot = 0;
        this.collected = collected;
        this.bouncing = false;
        this.bounceY = 0;
        this.maxBounceY = 20;
        this.dir = dir;
        this.bouncingUp =true;
        player = Game.getInstance().getPlayer();
        region = Game.getInstance().getRegion();
        if(!collected){
            region.addSortedObject(this);
        }
        if (bounce && !collected) {
            startBounce();
        }

    }

    public String getName(){
        return name;
    }
    
    public Texture getTexture() {
        return texture;
    }
    public Texture getShadowTexture() {
        return shadowTexture;
    }

    public String getType() {
        return type;
    }

    public LootType getLootType(){
        return lootType;
    }

    public boolean isCollected(){
        return collected;
    }
//TODO INTT
    public int getBounceY(){
        return Math.round(bounceY);
    }

    /**
     * Met à jour l'objet de butin.
     * @param dt Delta-temps.
     */
    public void update(float dt) {
        // Mettre à jour l'animation si elle existe
        /*
        if (anim != null) {
            anim.update(dt);
        }*/
        
        // Mettre à jour la position si en train de rebondir et une direction est spécifiée
        if (bouncing && dir != null) {
            addToPosC_X(Math.round(dir.x * dt*3));
            addToPosC_Y(Math.round(dir.y * dt*3));
        }
    
        // Calculer la distance entre le loot et le joueur //TODO Centre bas joueur TODO: quadtree ?
        float distanceToPlayer = Util.distanceBetween(getPosC_X(), getPosC_Y(), player.getCenterX(), (float)player.getPosC_Y());
        
        // Vérifier si le loot est à proximité du joueur et n'est pas déjà ramassé ou en train de rebondir
        if (distanceToPlayer < 20 && !collected && !bouncing) {
    
            // Jouer le son associé au loot
            //dj.play(sound, "static", "effect");
    
            // Si le type de loot est "heart", augmenter la santé du joueur
            /*
            if ("heart".equals(type)) {
                if (player.health < data.maxHealth) {
                    player.health++;
                }
            }*/

            if(this instanceof Tool) {
                region.retrieveToolFromRegionAndPutInWheel((Tool)this);
            }else{
                region.retrieveLootFromRegionAndPutInInventory(this);

            }
            //System.out.println("Devrait prendre le loot");
        }
    /*
        // Gérer le mouvement en grappin si applicable
        if (hookVec != null && grapple.state == -1) {
            x += hookVec.x * grapple.speed * -1 * dt;
            y += hookVec.y * grapple.speed * -1 * dt;
        }
    
        // Gérer le mouvement lorsqu'il est récupéré par le boomerang
        if (boomeranged) {
            x = boomerang.x;
            y = boomerang.y - 4;
        }*/
        // Mettre à jour l'animation de rebondissement si nécessaire
        /*TODO saut: APRES*/
        if (bouncing) {
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
    }

/*
    public void draw(SpriteBatch batch) {
    
        // Dessiner l'animation si elle existe, sinon dessiner l'image du loot
        /*if (anim != null) {
            anim.draw(texture, x, y + offY + bounceY, null, null, null, frameW / 2, frameH / 2);
        } else {*/
            // Dessiner l'image du loot
            //batch.draw(texture, x, y + offY + bounceY, rot, null, null, texture.getWidth() / 2, texture.getHeight() / 2);
            //batch.draw(texture, getPosC_X(), getPosC_Y() /*+ bounceY*/);
        //}
    //}

        // Méthode pour activer le rebondissement
        public void startBounce() {
            bouncing = true;
            //bouncingUp = true;
            bounceY = 0;
        }
    /*
        // Gérer le rebondissement si nécessaire
    if (bounce) {
        bouncing = true;
        
        // Callback pour arrêter le rebondissement une fois terminé
        Runnable finishBounce = new Runnable() {
            @Override
            public void run() {
                bouncing = false;
            }
        };
        
        // Callback pour effectuer la chute après le rebondissement
        Runnable fall = new Runnable() {
            @Override
            public void run() {
                Tween.to(Loot.this, Accessor.BOUNCE_Y, 0.25f)
                    .target(0)
                    .ease(Quad.IN)
                    .setCallbackTriggers(TweenCallback.END)
                    .setCallback(finishBounce)
                    .start(Game.getInstance().getTweenManager());
            }
        };
        
        // Animer le rebondissement suivi de la chute
        Tween.to(this, Accessor.BOUNCE_Y, 0.125f)
            .target(-16)
            .ease(Quad.OUT)
            .setCallbackTriggers(TweenCallback.END)
            .setCallback(fall)
            .start(Game.getInstance().getTweenManager());
    }*/



        public void setCollected(boolean b) {
            collected = true;
        }
}