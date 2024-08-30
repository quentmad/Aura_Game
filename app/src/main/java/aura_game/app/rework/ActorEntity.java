package aura_game.app.rework;
;
import aura_game.app.LPCActions.EntityStateMachine;
import aura_game.app.rework.TypeEnum.ActorEntityType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import org.apache.commons.lang3.tuple.Pair;

public class ActorEntity extends MovableEntity{

    private int currentBeginX;
    private EntityStateMachine stateComponant;

    private float damage;

    private TextureRegion[][] spriteSheetRegions;// Division de l'image en sous-images correspondant à chaque sprite.de la forme Y X

    //(comment?) stocker le default hitZoneLenghtDefault et hitZonePointDecallageDefault ici
    /**Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    private final Pair<Integer,Integer> hitZoneLenghtDefault;
    /** Décallage par rapport au point en bas à gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un côté touchant le rectangleHitbox (selon la direction)
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité
     */
    private final Pair<Integer,Integer> hitZonePointDecallageDefault;

    private Pair<Integer, Integer> currentHitZoneLenght;
    private Pair<Integer, Integer> currentHitZonePointDecallage;


    public ActorEntity(ActorEntityType type, int posX, int posY) {                                                                                                   //todo ATTENTION HITBOX PARAM

        super(type.getName(), type.imageWidth(), type.imageHeight(), type.contentImageWidth(), type.contentImageHeight(), type.offsetY(), type.maxLife(), type.maxLife(), new Hitbox(type.getApproximativeHitbox(), type.getPreciseHitbox()), type.stature(), type.deathLoot(), false, type.texturePath(), posX, posY, type.speed());
        this.currentBeginX = 0;
        this.stateComponant = new EntityStateMachine();//Stocke les différentes actions
        this.damage = type.damage();
        this.spriteSheetRegions = TextureRegion.split(this.texture(), imageWidth(), imageHeight());
        this.hitZoneLenghtDefault = type.hitZoneLenghtDefault();
        this.hitZonePointDecallageDefault = type.hitZonePointDecallageDefault();
        this.currentHitZoneLenght = hitZoneLenghtDefault;
        this.currentHitZonePointDecallage = hitZonePointDecallageDefault;

    }





    public void hit(){
        Rectangle currentHitZone = zoneDegatFromDirection(stateComponant.getCurrentOrientation().getDirection());
        //if zonehit
        for(AbstractEntity ent : physics().getCollidingObjectsInBothGrids(currentHitZone)){
            ent.hurt(damage);
        }
    }

    public Rectangle zoneDegatFromDirection(String direction) {
        //return the default degat zone because there is no tool
        return zoneDegat();
    }

    public EntityStateMachine stateComponant() {
        return stateComponant;
    }


    public TextureRegion[][] spriteSheetRegions(){
        return spriteSheetRegions;
    }

    public Pair<Integer, Integer> hitZoneLenghtDefault() {
        return hitZoneLenghtDefault;
    }

    public Pair<Integer, Integer> hitZonePointDecallageDefault() {
        return hitZonePointDecallageDefault;
    }

    public float damage() {
        return damage;
    }

    /**
     * Cette méthode calcule et renvoie un rectangle correspondant à la zone de dégâts sans outil (par défaut) de l'actor entité.
     * La zone de dégâts est déterminée en fonction de la direction de l'entité et de l'outil.
     * @return Rectangle Un objet Rectangle représentant la zone de dégâts.
     */
    public Rectangle zoneDegat(){
        int marge = 15;
        String dir = stateComponant().getCurrentOrientation().getDirection();
        int width = dir.equals("U") || dir.equals("D") ? currentHitZoneLenght.getLeft() : currentHitZoneLenght.getRight();
        int height = dir.equals("U") || dir.equals("D") ? currentHitZoneLenght.getRight() : currentHitZoneLenght.getLeft();
        int startX = (int)hitbox().approximativeHitbox().getX() + currentHitZonePointDecallage.getLeft();
        int startY = (int)hitbox().approximativeHitbox().getY() + (dir.equals("U") ? marge + hitbox().height + currentHitZonePointDecallage.getRight() : -height + marge - currentHitZonePointDecallage.getRight());

        if(dir.equals("R") || dir.equals("L")) {
            startX += dir.equals("R") ? hitbox().width + currentHitZonePointDecallage.getRight() : -width - currentHitZonePointDecallage.getRight();
        }

        return new Rectangle(startX, startY, width, height);
    }


    public boolean isInDanger(){
        return false;
    }

    public boolean canSee(){
        return false;
    }

    public void setCurrentBeginX(int currentBeginX) {
        this.currentBeginX = currentBeginX;
    }

    public void setCurrentHitZoneLenght(Pair<Integer, Integer> currentHitZoneLenght) {
        this.currentHitZoneLenght = currentHitZoneLenght;
    }

    public void setCurrentHitZonePointDecallage(Pair<Integer, Integer> currentHitZonePointDecallage) {
        this.currentHitZonePointDecallage = currentHitZonePointDecallage;
    }

    public void render(SpriteBatch batch, Region region){
        batch.draw(spriteSheetRegions[stateComponant.getCurrentState().getCurrentSpriteY()][stateComponant.getCurrentState().getCurrentSpriteX()], getPosOnScreenX(region.camera().position().x()), getPosOnScreenY(region.camera().position().y()));
        ui().drawBar(this, batch);
    }

}
