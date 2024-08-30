package aura_game.app.rework;

import aura_game.app.LootManager;
import aura_game.app.UIBar;
import aura_game.app.Util.Triplet;
import aura_game.app.rework.TypeEnum.LootableObjectType;

import java.util.List;

import static aura_game.app.GameManager.Game.actualRegionHeight;
import static aura_game.app.GameManager.Game.actualRegionWidth;

public abstract class AbstractEntity extends AbstractObject {

    private float durability;

    private float maxDurability;

    private Hitbox hitbox;

    private PhysicsComponent physics;

    private UIBar ui;

    private boolean onGround;

    /** taille réelle de l'entité en "hauteur" */
    private int stature;

    private List<Triplet<String,Integer,Integer>> deathLoot;

    /** classic*/
    public AbstractEntity(String name, int imageWidth, int imageHeight, int contentImageWidth, int contentImageHeight, int offsetY,      float durability, float maxDurability, Hitbox hitbox, PhysicsComponent physics, int stature, List<Triplet<String,Integer,Integer>> deathLoot, boolean onGround, String fileTexture, int posX, int posY) {
        super(name, imageWidth, imageHeight, contentImageWidth, contentImageHeight, offsetY, fileTexture, posX, posY);
        this.durability = durability;
        this.maxDurability = maxDurability;
        this.hitbox = hitbox;//spriteSheetInfo.SPRITE_WIDTH()/2 - hitboxWidth/2         //decallageX
        this.physics = physics;
        this.ui = new UIBar();
        this.stature = stature;
        this.deathLoot = deathLoot;

        this.hitbox.update(posX, posY);
    }

    /**
     * @return la position de l'entité en x pour le spawn de loot
     */
    public int getLootSpawnCenterX(){
        return posC().x() + (imageWidth() / 2) - 16;
    }

    public Hitbox hitbox(){
        return hitbox;
    }

    /**
     * Réduit la durabilité de this et "tue" l'entité si la durabilité est inférieure à 0
     *
     * @param pv les points de vie à retirer
     */
    public void hurt(float pv){
        ui.resetCounterLastHurt();
        durability-= pv;
        ui().setBarWidthAlive(Math.round((durability() / maxDurability()) * ui().getBarWidth())); // Calcul de la largeur de la barre
        if(durability <= 0){
            setOnGround(false);//die
        }
    }



    /**
     * Génère les loots de l'entité morte et les ajoute au lootManager
     * La génération des loots est basée sur la liste deathLoot, qui contient des triplets de la forme (lootType, min, max)
     * @param lootManager le lootManager qui gère les loots, afin de limiter les dépendances.
     */
    public void spawnDeathLoots(LootManager lootManager) {

        for(int i =0; i<deathLoot.size();i++){
            //Pour chaque loots possible, on en génère un nombre aléatoire entre min et max
            LootableObjectType lootType= LootableObjectType.valueOf(deathLoot.get(i).first());
            int nbLoots =(int)Math.floor(Math.random() * (deathLoot.get(i).third() - deathLoot.get(i).second() + 1)) + deathLoot.get(i).second();
            for(int j = 0; j < nbLoots; j++){
                // On appel spawnLoot en définisant une direction
                int dx = Math.round((float) (Math.random() * 2 - 1));
                int dy = Math.round((float) (Math.random() * 2 - 1));
                lootManager.spawnLoot(lootType, getLootSpawnCenterX(), posC().y()+lootType.offsetY(),true , lootManager.getJumpVec(dx, dy, 1));
            }
        }

    }


    public float durability() {
        return durability;
    }

    public float maxDurability(){
        return maxDurability;
    }

    public int stature() {
        return stature;
    }

    /** reduit jusqua 0) la durabilité de l'entité
     * @return true si la durabilité est à 0, false sinon
     * */
    public boolean reduceDurability(float pv){
        durability -= pv;
        if(durability <= 0){
            durability = 0;
            return true;
        }
        return false;
    }
    /**
     * Vérifie si x est dans la region
     */
    public boolean isXOnRegion(int x){
        return (x >= -hitbox().width && x <= (actualRegionWidth - hitbox().width)) ;
    }

    /**
     * Vérifie si y est dans la region
     */
    public boolean isYOnRegion(int y){
        return (y >= 0 && y <= (actualRegionHeight - hitbox().height)) ;
    }



    /**
     * @return true si l'entité est au sol, false sinon
     */
    public boolean isOnGround() {
        return onGround;
    }

    /**
     * Set on ground
     * @param onGround
     */
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public Grid getAdaptedGrid(){
        return physics.gridBlockEntity;
    }

    public PhysicsComponent physics(){
        return physics;
    }

    /**
    * Getters
    * @return la profondeur de l'entité
     */
    public int z(){
        return posC().y();
    }

    public UIBar ui(){
        return ui;
    }

    /**@return le point x a laquelle il faut mettre le loot pour qu'il soit parfaitement centré horizontalement sur l'entité */
    public int getLootSpawnCenterX(int lootWidth){
        return posC().x() + (imageWidth() / 2) - (lootWidth / 2);
    }

    /**
     * Calcule la distance entre this et other, en utilisant les coordonnées de leur hitbox
     * @param other
     * @return
     */
    public int calculateDistanceBetweenHitboxes(AbstractEntity other) {
        int thisLeft = this.posC().x();
        int thisRight = this.posC().x() + (int)this.hitbox().approximativeHitbox().width;
        int thisTop = this.posC().y() + (int)this.hitbox().approximativeHitbox().height;
        int thisBottom = this.posC().y();

        int otherLeft = other.posC().x();
        int otherRight = other.posC().x() + (int)other.hitbox().approximativeHitbox().width;
        int otherTop = other.posC().y() + (int)other.hitbox().approximativeHitbox().height;
        int otherBottom = other.posC().y();

        int dx = Math.max(0, Math.min(thisLeft - otherRight, otherLeft - thisRight));
        int dy = Math.max(0, Math.min(thisBottom - otherTop, otherBottom - thisTop));

        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calcule la distance entre this et other, en utilisant les coordonnées de leur centre, et concidérant une hitbox ronde
     * @param other
     * @return
     */
    public int calculateDistanceFromCenter(AbstractEntity other) {
        int diam =(int) Math.min(this.hitbox().approximativeHitbox().width, this.hitbox().approximativeHitbox().height);
        int dx = this.getCenterX() - other.getCenterX();
        int dy = this.getCenterY() - other.getCenterY();
        return (int) Math.sqrt(dx * dx + dy * dy) - diam/2;
    }

    public void toStringInfo(){
        System.out.println("Durability: "+durability);
        System.out.println("MaxDurability: "+maxDurability);
        System.out.println("Stature: "+stature);
        System.out.println("DeathLoot: "+deathLoot);
        System.out.println("OnGround: "+onGround);
        System.out.println("Hitbox: "+hitbox);
        System.out.println("Physics: "+physics);
        System.out.println("UI: "+ui);
        super.toStringInfo();
        System.out.println(" ");
    }

}
