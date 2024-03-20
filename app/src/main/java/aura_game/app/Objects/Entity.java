package aura_game.app.Objects;

import java.util.ArrayList;
import java.util.List;

import aura_game.app.LPCActions.EntityStateMachine;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import org.apache.commons.lang3.tuple.Pair;

import aura_game.app.Region;
import aura_game.app.GameManager.AudioManager;
import aura_game.app.GameManager.Game;
import aura_game.app.SpriteSheet.*;
import aura_game.app.Type.EntityType;


//Les spritesheet ont de base x=13 et y=21 sprites
public class Entity extends CollidableObject {

    private final SpriteSheetInfo spriteSheetInfo;
    /**Etant donné que certaines actions avec certaines armes (hache et slash par exemple) doivent commencer et finir par 2 au lieu de 0.*/
    private int currentBeginX;

    /**Hauteur de l'hitbox de l'entite pour marcher etc (colission/avec pts de vue iso)*/
    private int hitboxHeightFlat;

    /**Point bas gauche du debut du rectangle d'hitbox, par rapport à la position de l'entite */
    private int posHitboxX;
    /**Point bas gauche du debut du rectangle d'hitbox, par rapport à la position de l'entite */
    private int posHitboxY;
    /**Largeur du rectangle hitbox */
    private int hitboxWidth;
    /**Permet à chaque instance d'entité du même type d'accéder à la même instance de SpriteSheetData associée à ce type d'entité*/
    //private final SpriteSheetData spriteSheetData;

    /**Instance*/
    private int refreshTEMPO = 0;//TODO TEMPO
    private Region actualRegion;
    /**Dégats que fait l'entité, sans armes */
    private final float degatDefault;

    /**Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    private final Pair<Integer,Integer> hitZoneLenghtDefault;
    /** Décallage par rapport au point en bas à gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un côté touchant le rectangleHitbox (selon la direction)
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité
    */
    private final Pair<Integer,Integer> hitZonePointDecallageDefault;
    
    private int speed;

    private EntityStateMachine entityStateMachine;

    public Entity(EntityType type, int speed, int initX, int initY){
        
        super(type.getName(),new Rectangle(), initX,initY,
        type.tall(), new Polygon(new float[]{1,1,2,2,3,3}), type.hitboxHeight(),0,type.deathLoots());

        this.entityStateMachine = new EntityStateMachine();//Stocke les différentes actions
        spriteSheetInfo = new SpriteSheetInfo(type.spriteWidth(),type.spriteHeight(),"Entities/"+type.getName());
        //this.spriteSheetData = SpriteSheetDataManager.getSpriteSheetDataInstance(type);
        this.posHitboxX = this.getPosC_X() + spriteSheetInfo.SPRITE_WIDTH()/2 - hitboxWidth/2;
        this.posHitboxY = this.getPosC_Y() ;//TODO OK ??? BIEN CENTRE EN TERME DE X (souvent mais en Y c'est PosY + offY nn ?)
        this.hitboxWidth = type.hitboxWidth();
        this.currentBeginX = 0;
        this.speed = speed;
        this.hitboxHeightFlat=type.hitboxHeightFlat();

        this.getHitboxFlat().set(posHitboxX, (posHitboxY), hitboxWidth, hitboxHeightFlat);
        this.setHitboxPolygon(new Polygon(new float[]{posHitboxX,(posHitboxY),  posHitboxX,((posHitboxY)+hitboxHeightFlat),  (posHitboxX+hitboxWidth),((posHitboxY)+hitboxHeightFlat),  (posHitboxX+hitboxWidth),(posHitboxY)}));

        this.actualRegion = Game.getInstance().getRegion();
        this.hitZoneLenghtDefault = type.hitZoneLenghtDefault();
        this.hitZonePointDecallageDefault = type.hitZonePointDecallageDefault();
        this.degatDefault = type.degatDefault();
    }


    public void setCurrentBeginX(int beginX){
        currentBeginX = beginX;
    }
    public int getCurrentBeginX(){
        return currentBeginX;
    }
    
    /**@return le SpriteSheetInfo contenant SPRITE_WIDTH, SPRITE_HEIGHT, Texture et TextureRegion[][] */
    public SpriteSheetInfo getSpriteSheetInfo(){
        return spriteSheetInfo;
    }

    public int getHitboxHeightFlat() {
        return hitboxHeightFlat;
    }

    public int getPosHitboxX(){
        return posHitboxX;
    }
    //POur Y c'est juste posCY + off nn ??
    public int getPosHitboxY(){
        return posHitboxY;
    }

    /**Largeur du rectangle hitbox */
    public int getHitboxWidth(){
        return hitboxWidth;
    }

    /**@return la position X du joueur au niveau du milieu de sprite */
    public int getCenterX(){
        return getPosC_X() + (spriteSheetInfo.SPRITE_WIDTH())/2;
    }


    public void updateHitbox(){
        this.posHitboxX =this.getPosC_X() + spriteSheetInfo.SPRITE_WIDTH()/2 - hitboxWidth/2;
        this.posHitboxY =this.getPosC_Y();
        getHitboxFlat().setX(posHitboxX);
        getHitboxFlat().setY(posHitboxY);
        this.setHitboxPolygon(new Polygon(new float[]{posHitboxX,posHitboxY,  posHitboxX,(posHitboxY+hitboxHeightFlat),  (posHitboxX+hitboxWidth),(posHitboxY+hitboxHeightFlat),  (posHitboxX+hitboxWidth),posHitboxY}));

    }

    public int getDifferrenceImageHitboxX(){
        return (int)getHitboxFlat().getX()- this.getPosC_X();//TODO bon X ?
    }

    public Region getActualRegion() {
        return actualRegion;
    }

    public EntityStateMachine getEntityStateMachine() {
        return entityStateMachine;
    }

    /**
     * Verifie si le X est dans la region (selon la hitbox du personnage) (au niveau des bords)
     * @param x la coordonnée x dont on veut tester l'appartenance
     * @return true si x est dans la region 0 sinon
     */
    public boolean isXOnRegion(int x){
        return (x >= -hitboxWidth && x <= (actualRegion.getCarte().getWidth() - hitboxWidth)) ;
    }

    /**
     * Verifie si le Y est dans la region (selon la hitbox du personnage) (au niveau des bords)
     * @param y la coordonnée y dont on veut tester l'appartenance
     * @return true si y est dans la region 0 sinon
     */
    public boolean isYOnRegion(int y){
        return (y >= 0 && y <= (actualRegion.getCarte().getHeight() - getHitboxHeight())) ;
    }


    /**@return le point x a laquelle il faut mettre le loot pour qu'il soit parfaitement centré horizontalement sur l'entité */
    public int getLootSpawnCenterX(int lootWidth){
        return getPosC_X() + (spriteSheetInfo.SPRITE_WIDTH() / 2) - (lootWidth / 2);
    }


    /**
     * Déplace l'entité de dx en X et dy en Y tout en gérant les collisions.
     * Met à jour posC_X et posC_Y par tranche de 1 pour evité des blocages en bord de map, et appelle updateHitbox().
     *
     * @param dx Le déplacement en X.
     * @param dy Le déplacement en Y.
     */
    public void move(int dx, int dy) {
        int remainingDx = Math.abs(dx)*speed;
        int remainingDy = Math.abs(dy)*speed;

        int stepMoveDx = (dx > 0) ? 1 : -1;
        while (remainingDx != 0) {
            
            if (isXOnRegion(getPosC_X() + stepMoveDx)) {
                this.addToPosC_X(stepMoveDx);
                remainingDx -= 1;
            } else {
                break; // Stop le déplacement si collision ou bord atteint
            }
        }
        int stepMoveDy = (dy > 0) ? 1 : -1;
        while (remainingDy != 0) {
            
            if (isYOnRegion(getPosC_Y() + stepMoveDy)) {
                this.addToPosC_Y(stepMoveDy);
                remainingDy -= 1;
            } else {
                break; // Stop le déplacement si collision ou bord atteint
            }
        }

        updateHitbox(); // Met à jour l'hitbox une fois les déplacements effectués
        
        if(this instanceof IAEntity){//TODO: Afin de détecter lors de coups...
            if(refreshTEMPO %5 ==0){
            getActualRegion().getGridIAEntity().update(this);
            }refreshTEMPO++;
        }
    }
    
    /** Si une colission entre Rectangle est détecté (quadtree) cette méthode est appelé
     * pour avoir une colission plus précise, dans le cas où l'object avec lequel cette entite est en colission
     * a une PolygonHitbox et pas seulement un rectangle comme les entites 
     * @param nextPosX la prochaine position x de l'entite (bas à gauche)
     * @param nextPosY la prochaine position y de l'entite
     * @param objCol l'objet avec l'entité est détecté en colission au niveau du rectangle hitbox (si il se déplace)
    */
    public boolean willCollidePolygoneMove(int nextPosX, int nextPosY, CollidableObject objCol){
        boolean collisionDetected = false;
        int directionX = (int) Math.signum(nextPosX - getPosC_X());
        int directionY = (int) Math.signum(nextPosY - getPosC_Y());
        for (int x = posHitboxX; x < posHitboxX + getHitboxFlat().getWidth(); x++) {
            if (directionY==-1 && objCol.getHitboxPolygon().contains(x, posHitboxY)) {//TODO: ----------overlaps existe pas, contains trop long ?....
                // Il y a collision sur le bord supérieur de l'hitbox
                collisionDetected = true;
                //TODO:Return la couleur pour comparer ensuite en fonction de l'utilité
            }
            if (directionY==1 && objCol.getHitboxPolygon().contains(x, posHitboxY+(int)getHitboxFlat().getHeight())) {
                // Il y a collision sur le bord inférieur de l'hitbox
                //System.out.println("colission en bas de l'arb");
                collisionDetected = true;
            }
        }
        for (int y = posHitboxY; y < posHitboxY + getHitboxFlat().getHeight(); y++) {
            if (directionX == 1 && objCol.getHitboxPolygon().contains(posHitboxX+ (int)getHitboxFlat().getWidth(), y)) {
                // Il y a collision sur le bord gauche de l'hitbox
                collisionDetected = true;
            }
            if (directionX == -1 && objCol.getHitboxPolygon().contains(posHitboxX, y)) {
                // Il y a collision sur le bord droit de l'hitbox
                collisionDetected = true;
            }
        }     
        return collisionDetected;
    }

    /**Cette méthode est appelé lorsqu'on souhaite donner un coup, à la fin de l'animation statique.
     * Recherche avec le rectangle de la zone de dégat de l'objet utilisé (ou main) (selon la direction) utilisé si il y a un item/entité dans la zone.
     * Si oui, on lance hurt(int pv) sur ces objets.
     */
    public void hit(){//Animation de mort (attendre fin anim coup) + ajout detection polynom
        Rectangle zoneDegat = zoneDegatFromDirection(entityStateMachine.getCurrentOrientation().getDirection());
        //System.out.println(zoneDegat);
        //Liste des items en colission par rapport à leur rectangle
        List<CollidableObject> itemsColRect = actualRegion.getGridItem().getCollidingObjects(zoneDegat).getList();
        //Liste des entity en colission (pas besoin d'autres test)//TODO pas encore PLAYER
        List<CollidableObject> entityCol = actualRegion.getGridIAEntity().getCollidingObjects(zoneDegat).getList();
        boolean col = false;
        for(CollidableObject ent : entityCol){
            //System.out.println("ENTITY HURT");
            AudioManager.getInstance().playSound("entity","hurt",0.1f);
            ent.hurt(1);
            if(ent.life <=0){//TODO DIE + Animation
                Game.getInstance().getRegion().killFromRegion((Entity)ent); //TODO: mettre dans collidableOBJ et que ca le fasse sur l'obj souhaité
            }
        }
        for (CollidableObject item : itemsColRect) {
            if(item.isPresentInZoneNoMove(zoneDegat)){//Verification polygon
                AudioManager.getInstance().playSound("wood","hurt",0.1f);
                //System.out.println("Item HURT");
                item.hurt(1);
                //System.out.println("item touche");
                if(item.life <=0){//TODO DIE + Animation
                    Game.getInstance().getRegion().killFromRegion((Item)item); //TODO: mettre dans collidableOBJ et que ca le fasse sur l'obj souhaité
                }
            }
        }

        
    }



    /**Donne le rectangle de colission de l'outil/main, selon la direction de l'entite... */
    public Rectangle zoneDegatFromDirection(String dir){
        //Point en bas à gauche du rectangle, décallé par hitZonePointDecallageDefault
        int startX=0;
        int startY=0;
        int width=0;
        int height=0;
        int marge = 15;
        if(this instanceof PlayableEntity){
            Pair<Integer, Integer> hitZoneLenght = getCurrentHitZoneLenght();
            Pair<Integer, Integer> hitZonePointDecallage = getCurrentHitZonePointDecallage();
            }
            if(dir.equals("U")||dir.equals("D")){
                //System.out.println("up taille");
                width = hitZoneLenghtDefault.getLeft();
                height = hitZoneLenghtDefault.getRight();
            }
            else{
                width = hitZoneLenghtDefault.getRight();
                height = hitZoneLenghtDefault.getLeft();
            }
            //System.out.println(currentDirectionLetter);
            switch(dir){
                case "U":
                    //System.out.println("up xy");
                    startX = posHitboxX + hitZonePointDecallageDefault.getLeft() ;//Décal: long interieur:1 gauche(+)
                    startY = posHitboxY + marge + hitboxHeightFlat + hitZonePointDecallageDefault.getRight();//Decal: Eloignement:2 haut(+)
                    break;
                case "D":
                    startX = posHitboxX + hitZonePointDecallageDefault.getLeft() ;//Décal: long interieur:1 gauche(+)
                    startY = posHitboxY - height + marge - hitZonePointDecallageDefault.getRight();//Decal: Eloignement:2 bas(-)
                    break;//Ajustement /4 car pas symetrique coup haut bas
                case "R":
                    startX = posHitboxX + hitboxWidth + hitZonePointDecallageDefault.getRight();//Decal: Eloignement:2 droite(+)
                    startY = posHitboxY + hitZonePointDecallageDefault.getLeft() ;//Décal: long interieur:1 haut(+)
                    break;
                case "L":
                    startX = posHitboxX - width - hitZonePointDecallageDefault.getRight();//Decal: Eloignement:2 gauche(-)
                    startY = posHitboxY + hitZonePointDecallageDefault.getLeft() ;//Décal: long interieur:1 haut(+)
                    break;
                default:
                    System.out.print("error, currentDirectionLetter is null for zoneDegatFromDirection ");
                    startX = 0;
                    startY = 0;
            }
        //}
        return new Rectangle(startX, startY,width, height);
    }

    /**Pour les entités on retourne la valeur par defaut.
     *  Décallage par rapport au point en bas a gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un coté touchant le rectangleHitbox (selon la direction) 
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité*/
    private Pair<Integer, Integer> getCurrentHitZonePointDecallage() {
        return hitZonePointDecallageDefault;
    }

    /**Pour les entités on retourne la valeur par defaut.
     * Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    private Pair<Integer, Integer> getCurrentHitZoneLenght() {
        return hitZoneLenghtDefault;
    }

     /** Donne les entités dans la zone (selon leur rectangle hitbox)*/
    public List<Entity> collideZoneNoMove(List<Entity> entCol,Rectangle zone){
        List<Entity> entityCol = new ArrayList<Entity>(); 
        for (Entity ent : entCol) {
            if ((zone.overlaps(ent.getHitboxFlat()))||(zone.contains(ent.getHitboxFlat()))||(ent.getHitboxFlat().contains(zone))){
                entityCol.add(ent);
            }
        }
        return entityCol;
    }

    /**
     * Vérifie s'il y a une collision avec d'autres objets ou le sol.
     *
     * @param dx La variation horizontale de la position.
     * @param dy La variation verticale de la position.
     * @return true s'il y a une collision, false sinon.
     */
    public boolean isColliding(int dx, int dy){
        if(!actualRegion.willCollideGroundMove(getPosC_X() + dx, getPosC_Y() + dy, this)){
            //On met a jour l'emplacement de la hitbox rectangle par rapport au possible mouvement
            Rectangle actRect = getHitboxFlat();
            getHitboxFlat().set(actRect.x+dx, actRect.y + dy, actRect.width, actRect.height);
            List<CollidableObject> objColList = actualRegion.getGridItem().getCollidingObjects(getHitboxFlat()).getList();

            for (CollidableObject object : objColList) {
                if(willCollidePolygoneMove(getPosC_X() + dx,getPosC_Y()+dy, object)){
                    getHitboxFlat().set(actRect);//Sinon on remet la position avant mouvement (pour le hitbox)
                    return true;//Il y a colission (avec autres objets)
                }
            }
            return false;//Pas de colission
        }
        return true;//Il y a colission (avec le sol)
    }

    
    /**
     * Proximité d'ennemis : Vérifiez si des ennemis se trouvent à proximité de l'entité. Vous pouvez utiliser la position de l'entité et la position des ennemis pour calculer la distance entre eux. Si la distance est inférieure à une certaine valeur seuil, vous pouvez considérer que l'entité est en danger.
     * Santé de l'entité : Vérifiez si la santé de l'entité est inférieure à un certain seuil critique. Si la santé est basse, cela peut indiquer que l'entité est en danger.
     * Dommages récents : Si l'entité a subi des dommages récents, cela peut être un indicateur qu'elle est en danger. Vous pouvez utiliser un compteur de dommages ou une variable pour suivre les dommages subis par l'entité.
     * Ennemis visibles : Vérifiez si des ennemis sont visibles pour l'entité. Vous pouvez utiliser des techniques de détection de ligne de vue ou de champ de vision pour déterminer si des ennemis se trouvent dans le champ de vision de l'entité. Si des ennemis sont visibles, cela peut indiquer un danger potentiel.
     * Actions hostiles des ennemis : Si les ennemis ont récemment effectué des actions hostiles ou agressives envers l'entité, cela peut être un signe de danger imminent.
     **/
    public boolean isInDanger() {

        return false;
    }

    /**   
     * Vérification de la ligne de vue : Vous pouvez utiliser une vérification de ligne de vue pour déterminer si rien ne bloque la vision entre l'entité et le joueur. Cela peut être réalisé en traçant une ligne droite entre les positions de l'entité et du joueur, puis en vérifiant s'il y a des obstacles ou des murs entre eux. Si la ligne de vue est obstruée, l'entité ne peut pas voir le joueur.
     * Champ de vision limité : Vous pouvez attribuer à chaque entité un champ de vision limité. Si le joueur se trouve à l'intérieur de ce champ de vision, l'entité peut le détecter. Vous pouvez utiliser des concepts tels que des angles de vision et des cônes pour déterminer si le joueur se trouve dans le champ de vision de l'entité.
     * Bruits ou mouvements : Vous pouvez prendre en compte les bruits ou mouvements émis par le joueur. Si le joueur effectue des actions bruyantes ou se déplace de manière notable, cela peut attirer l'attention de l'entité et la rendre plus susceptible de le détecter.
     * Distance : Vous pouvez définir une distance maximale à partir de laquelle l'entité peut détecter le joueur. Si la distance entre l'entité et le joueur est supérieure à cette limite, l'entité ne pourra pas le voir.
     **/
    public boolean canSee(Entity player) {

        return false;
    }


    
}
