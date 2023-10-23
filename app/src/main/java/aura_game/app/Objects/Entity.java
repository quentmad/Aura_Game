package aura_game.app.Objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import org.apache.commons.lang3.tuple.Pair;

import aura_game.app.Grid;
import aura_game.app.Region;
import aura_game.app.GameManager.AudioManager;
import aura_game.app.GameManager.Game;
import aura_game.app.SpriteSheet.*;
import aura_game.app.Type.EntityType;


//Les spritesheet ont de base x=13 et y=21 sprites
public class Entity extends CollidableObject {
    //LibGDX
    private final SpriteSheetInfo spriteSheetInfo;
    private int currentSpriteX;//--> Mouvements des differents mouvements d'une action
    private int currentSpriteY;//-> Action choisi
    /**
     *Etant donné que certaines action avec certaines armes (hache et slash par exemple) doivent commencer et finir par 2 au lieu de 0
     */
    private int currentBeginX;

    /**Hauteur de l'hitbox de l'entite pour marcher etc (colission/avec pts de vue iso)*/
    private int hitboxHeightFlat;
    /**endSpriteX de la derniere Direction car lorsque l'action s'arrete, ca passe a null */
    private int lastEndSpriteX;
    /**Point bas gauche du debut du rectangle d'hitbox, par rapport a la position de l'entite */
    private int posHitboxX;
    /**Point bas gauche du debut du rectangle d'hitbox, par rapport a la position de l'entite */
    private int posHitboxY;
    /**Largeur du rectangle hitbox */
    private int hitboxWidth;
    /**Permet à chaque instance d'entité du même type d'accéder à la même instance de SpriteSheetData associée à ce type d'entité*/
    private final SpriteSheetData spriteSheetData;
    private int[] tabOfAction;
    private String actualActionName;

    /**Lorsque l'action en cours est SLASH, les 3 dernières sprites ont une durée de 1, sinon 2 */
    private final int[] spritesActionDuration;
    private int currentSpriteDuration;
    private final int defaultSpriteDuration;

    Pair<Integer,Integer> currentDeplacement;
    /**(U,D,L,R) utile pour attaquer... changeAction (action+currentDirectionLetter) */
    private String currentDirectionLetter;

    /**Instance*/
    //private Inventory inventoryMenu;

    /**Instance*/
    private int refreshTEMPO = 0;//TODO TEMPO
    private Region actualRegion;
    /**dégats que fait l'entité, sans armes */
    private final float degatDefault;

    /**Longueur de la zone de dégats sans armes: 1e: le long de l'entité, 2e: en s'éloignant de l'entité, formant le rectangle commencant en hitZoneLenghtDefault*/
    private final Pair<Integer,Integer> hitZoneLenghtDefault;
    /** Décallage par rapport au point en bas a gauche du rectangle de longueur hitZoneLenghtDefault, le rectangle ayant un coté touchant le rectangleHitbox (selon la direction) 
     * 1er: décallage vers le long intérieur de l'entité, 2e: en s'éloignant de l'entité
    */
    private final Pair<Integer,Integer> hitZonePointDecallageDefault;
    
    private int speed;

    public Entity(EntityType type, int speed, int initX, int initY){
        
        super(type.getName(),new Rectangle(), initX,initY,
        type.tall(), new Polygon(new float[]{1,1,2,2,3,3}), type.hitboxHeight(),0,type.deathLoots());
        spriteSheetInfo = new SpriteSheetInfo(type.spriteWidth(),type.spriteHeight(),"Entities/"+type.getName());
        this.spriteSheetData = SpriteSheetDataManager.getSpriteSheetDataInstance(type);
        this.posHitboxX = this.getPosC_X() + spriteSheetInfo.SPRITE_WIDTH()/2 - hitboxWidth/2;
        this.posHitboxY = this.getPosC_Y() ;//+ spriteSheetInfo.getSPRITE_HEIGHT()/2 - hitboxHeight/2; //TODO OK ??? BIEN CENTRE EN TERME DE X (souvent mais en Y c'est PosY + offY nn ?)
        this.hitboxWidth = type.hitboxWidth();
        //On initialise l'action de départ et les durées des sprites
        this.currentSpriteDuration = 0;
        this.defaultSpriteDuration = 2;
        this.spritesActionDuration = new int[]{defaultSpriteDuration,defaultSpriteDuration,defaultSpriteDuration,defaultSpriteDuration,defaultSpriteDuration,defaultSpriteDuration,
            defaultSpriteDuration,defaultSpriteDuration,defaultSpriteDuration,defaultSpriteDuration,defaultSpriteDuration,defaultSpriteDuration,defaultSpriteDuration,defaultSpriteDuration,defaultSpriteDuration,defaultSpriteDuration};
        currentDirectionLetter = "D";
        changeAction("Walk_D");

        this.currentSpriteX=0;
        this.currentSpriteY=tabOfAction[1];
        this.currentBeginX = 0;
        this.speed = speed;
        this.hitboxHeightFlat=type.hitboxHeightFlat();

        this.getHitboxFlat().set(posHitboxX, (posHitboxY), hitboxWidth, hitboxHeightFlat);
        this.setHitboxPolygon(new Polygon(new float[]{posHitboxX,(posHitboxY),  posHitboxX,((posHitboxY)+hitboxHeightFlat),  (posHitboxX+hitboxWidth),((posHitboxY)+hitboxHeightFlat),  (posHitboxX+hitboxWidth),(posHitboxY)}));
        this.lastEndSpriteX = type.lastEndSpriteX();
        //--------instance de spriteSheetData pour trouver les sprites de début et de fin d'actions X et Y---------
        //this.inventoryMenu = Game.getInstance().getInventory();
        this.actualRegion = Game.getInstance().getRegion();
        this.hitZoneLenghtDefault = type.hitZoneLenghtDefault();
        this.hitZonePointDecallageDefault = type.hitZonePointDecallageDefault();
        this.degatDefault = type.degatDefault();
    } 

    /**Durée (en nombre de render) par défaut d'un sprite*/
    public int getDefaultSpriteDuration() {
        return defaultSpriteDuration;
    }

    /**Tableau stockant la durée des sprites de l'action actuelle*/
    public int[] getSpritesActionDuration() {
        return spritesActionDuration;
    }

    public void setCurrentBeginX(int beginX){
        currentBeginX = beginX;
    }
    public int getCurrentBeginX(){
        return currentBeginX;
    }

    public Pair<Integer,Integer> getCurrentDeplacement(){
        return currentDeplacement;
    }
    
    /**@return le SpriteSheetInfo contenant SPRITE_WIDTH, SPRITE_HEIGHT, Texture et TextureRegion[][] */
    public SpriteSheetInfo getSpriteSheetInfo(){
        return spriteSheetInfo;
    }
    public int getCurrentSpriteX() {
        return currentSpriteX;
    }
    public void setCurrentSpriteX(int x) {
        this.currentSpriteX = x;
    }

    public int getCurrentSpriteY() {
        return currentSpriteY;
    }

    public int getHitboxHeightFlat() {
        return hitboxHeightFlat;
    }

    public int getLastEndSpriteX(){
        return lastEndSpriteX;
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

    /**Méthode appelé lorsqu'on souhaite changer d'action
     * Si c'est une action statique alors on déduit d'abord la direction de l'action à partir de la direction actuelle de l'entité
     * Change tabOfAction, la direction (U,D,L,R) utile pour attaquer... 
     * et le déplacement c y (par exemple 0 1 pour U)
     * Met a null tabOfAction si l'action est null
     * Si c'est une action dont certaines sprites ont une durée différentes de celle par défault, on modifie le spritesActionDuration[]
     */
    public void changeAction(String action){
        if(action !=null){
            //Alors c'est une action static: on déduit la direction en fonction de la direction actuelle de l'entité
            if(!String.valueOf(action.charAt(action.length() - 2)).equals("_")){
                action = action + "_" + currentDirectionLetter;
            }
            this.actualActionName = action;
            this.tabOfAction = spriteSheetData.getTabofAction(action);
            this.currentDirectionLetter = String.valueOf(action.charAt(action.length() - 1));
            currentDeplacement = this.getDeplacementFromAction(action);//Defini x y current
            //Si c'est un PlayableEntity et qu'il a un Tool en main, penser à mettre à jour spriteY et size 
            if (this instanceof PlayableEntity && Game.getInstance().isGameStarted()){//!!!!!!!!
                if(!((PlayableEntity)this).getCurrentMeleeToolName().equals("")){
                    ((PlayableEntity)this).updateSpriteToolInfo();//TODO comparer pour pas lancer ca a chaque change action meme h24 sans outils
                    ((PlayableEntity)this).updateSpriteDurationFromActionName();
                }

            }        
        }else {this.tabOfAction = null;}
        currentSpriteDuration = 0;  
    }


    public String getActualActionName(){
        return actualActionName;

    }

    /**
     * Donne les mouvements X Y de l'action 
     * @param currentAction l'action
     * @return une pair X Y du type (1,0)
     */
    private Pair<Integer, Integer> getDeplacementFromAction(String currentAction) {
        switch (currentAction) {
            case "Walk_U":
                return Pair.of(0, 1);
            case "Walk_L":
                return Pair.of(-1, 0);
            case "Walk_D":
                return Pair.of(0, -1);
            case "Walk_R":
                return Pair.of(1, 0);
            default:
                return Pair.of(0, 0); // Action sans mouvement
        }
    }


    public void updateHitbox(){
        this.posHitboxX =this.getPosC_X() + spriteSheetInfo.SPRITE_WIDTH()/2 - hitboxWidth/2;
        this.posHitboxY =this.getPosC_Y();// + spriteSheetInfo.getSPRITE_HEIGHT()/2 - hitboxHeight/2; 
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

    /**update le spriteX en lui attribuant le spriteX suivant de l'action actuelle */
    public void updateCurrentSpriteX(){//TODO: on fait vraiment le 0 au debut ?
        currentSpriteX = (currentSpriteX + 1) % (tabOfAction[0]+1);
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
     * @return
     */
    public void hit(){//Animation de mort (attendre fin anim coup) + ajout detection polynom
        Rectangle zoneDegat = zoneDegatFromDirection();
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
    public Rectangle zoneDegatFromDirection(){
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
            if(currentDirectionLetter.equals("U")||currentDirectionLetter.equals("D")){
                //System.out.println("up taille");
                width = hitZoneLenghtDefault.getLeft();
                height = hitZoneLenghtDefault.getRight();
            }
            else{
                width = hitZoneLenghtDefault.getRight();
                height = hitZoneLenghtDefault.getLeft();
            }
            //System.out.println(currentDirectionLetter);
            switch(currentDirectionLetter){
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
     * Exécute l'action actuellement en cours pour cette entité.
     * Si une action est en cours, elle est soit une action continue/avec mouvement (Walk_L, Walk_R, etc.) 
     * auquel cas on lance CheckColissionAndExecuteContiniousAction
     * soit une action statique (Slash, Thrust, etc.) auquel cas on lance executeStaticAction.
     * Si aucune action n'est en cours, on s'assure que l'ont est bien à la sprite 0.
     */
    public void doAction(){
        currentSpriteDuration++;
        int currentActionDuration = getSpritesActionDuration()[currentSpriteX];
        if (currentSpriteDuration >= currentActionDuration){//Si le sprite a une durée de 2 on mettra 2 fois plus de temps a l'execute (slash)
            currentSpriteDuration = 0;    
            //Une action est en cours
            if(tabOfAction != null){
                lastEndSpriteX = tabOfAction[0];
                currentSpriteY = tabOfAction[1];
                int dx = currentDeplacement.getLeft();
                int dy = currentDeplacement.getRight();

                if(dx != 0 || dy != 0){//Cas:1 Mouvement -> Walk
                    CheckColissionAndExecuteContiniousAction(dx, dy);
                }else{
                    executeStaticAction();
                }

            } else if(getCurrentSpriteX() % getLastEndSpriteX() != 0){
                    //Sinon il ni a pas d'action en cours: on laisse l'image de fin de la derniere 
                    currentSpriteX = 0;
            }
        }
    }

    /**             - - - - - - - - - - - Appelé par doAction() si dx ou dy n'est pas nul - - - - - - - - - - -
     * Permet de déplacer une entité en prenant en compte les collisions avec d'autres objets dans la scène.
     * Dans un premier temps, elle vérifie si elle a une direction, cela signifirait qu'elle doit être déplacée. Ensuite, elle calcule le déplacement horizontal et vertical ("dx" et "dy") en fonction de la direction de l'entité.
     * La méthode vérifie ensuite s'il y aura une collision avec le sol si l'entité est déplacée en "dx" et "dy". Si ce n'est pas le cas, elle met à jour la position de la hitbox de l'entité et vérifie s'il y a collision avec d'autres objets dans la scène en appelant "getHitboxQuadtree().getCollidingObjects(ent)".
     * Si la liste d'objets en collision est vide, cela signifie qu'il n'y a pas de collision et donc l'entité est déplacée en appelant la méthode "move(dx,dy, actualMap)". Sinon, la hitbox de l'entité est remise à sa position précédente (avant posible mouvement).
     * Si l'entité n'a pas de direction, la méthode vérifie si la dernière animation est terminée. Si ce n'est pas le cas, elle réinitialise la position de l'entité.
     * Enfin, la méthode met à jour la position de l'entité dans la scène en appelant la méthode "updateEntityPosition(ent)".
     */
    private void CheckColissionAndExecuteContiniousAction(int dx, int dy) {
        if(!actualRegion.willCollideGroundMove(getPosC_X() + dx, getPosC_Y() + dy, this)){
            //On met a jour l'emplacement de la hitbox rectangle par rapprt au possible mouvement
            Rectangle actRect = getHitboxFlat();
            getHitboxFlat().set(actRect.x+dx, actRect.y + dy, actRect.width, actRect.height);
            //List<CollidableObject> objColList = actualRegion.getQuadtreeItem().getCollidingObjects(this);
            List<CollidableObject> objColList = actualRegion.getGridItem().getCollidingObjects(getHitboxFlat()).getList();
            boolean col = false;
            for (CollidableObject object : objColList) {
                if(willCollidePolygoneMove(getPosC_X() + dx,getPosC_Y()+dy, object)){
                    col = true;
                } 
            }
            if(!col){
                move(dx,dy);
                updateCurrentSpriteX();
                if(!(this instanceof IAEntity)){
                    actualRegion.calculAndUpdatePosition(this);//Update pour que le plan/map bouge en fonction du joueur TODO: les autres entites doivent pas faire sur la cam (sauf si cinematique...)
                }
            }else{
                getHitboxFlat().set(actRect);//Sinon on remet la position avant mouvement (pour le hitbox)
                changeAction(null);//Il y a colission (avec autres objets): plus d'action en cours
            }
        }
        else{changeAction(null);}//Il y a colission (avec le sol): plus d'action en cours
    }

    /**
     * Cette méthode est utilisée pour exécuter une action statique une seule fois, puis l'arrêter.
     * Méthode privée appelée par doAction() lorsque l'entité est en train d'exécuter une action statique.
     * Elle avance l'animation d'une seule itération et vérifie si l'animation est terminée.
     * Si l'animation est terminée, la méthode lance finishAction.
     */
    private void executeStaticAction() {

        updateCurrentSpriteX();
        // Vérifie si l'animation est terminée
        if(this instanceof PlayableEntity && currentSpriteX == currentBeginX){
            //SI c'est un slash, alors on lance un hit, à le fin de son action !
            if(getActualActionName().substring(0, getActualActionName().length() - 2).equals("Slash")){hit();}//ou d'autres combat...
            Game.getInstance().getMyInputProc().finishAction();
            Game.getInstance().getMyInputProc().setOnGoingStaticAction(false);
        }
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

    /**
     * Récupère l'entité la plus proche de l'entité cible dans le quadtree.
     * TODO: prendre en compte feroce/innofencif
     * @param quadtree     le quadtree à explorer
     * @return l'entité la plus proche de l'entité cible (this), ou null s'il n'y en a pas
     */
    /* 
    public Entity findClosestEntity(Grid quadtree) {
        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;
        Entity player = Game.getInstance().getPlayer();
        findClosestEntityRecursive(this, quadtree, closestEntity, closestDistance);
        if(this != player && player != null &&  this.calculateDistance(player) < closestDistance){
            return player;
            //changer aussi distance si besoin ?
        }
        return closestEntity;
    }*/

    /**
     * Recherche récursive de l'entité la plus proche en explorant les sous-quadtree.
     *
     * @param targetEntity    l'entité cible
     * @param quadtree        le quadtree actuel
     * @param closestEntity   l'entité la plus proche trouvée jusqu'à présent
     * @param closestDistance la distance à l'entité la plus proche trouvée jusqu'à présent
     */
    /*
    private void findClosestEntityRecursive(Entity targetEntity, Grid quadtree, Entity closestEntity, double closestDistance) {
        if (quadtree == null) {
            return;
        }

        List<CollidableObject> objects = new ArrayList<>();
        quadtree.retrieve(objects,targetEntity); // Obtenez les objets du nœud actuel

        for (CollidableObject object : objects) {
            if (object instanceof Entity) { // && ferroce
                Entity entity = (Entity) object;
                double distance = targetEntity.calculateDistance(entity);

                if (distance < closestDistance && targetEntity != entity) {
                    closestEntity = entity;
                    closestDistance = distance;
                }
            }
        }
        /*Recherche récursive de l'entité la plus proche en explorant les sous-quadtree
        Explore les sous-quadtree pour trouver l'entité la plus proche. 
        Si les limites du sous-quadtree se chevauchent avec les limites de l'entité cible, 
        on continue la recherche récursive en utilisant ce sous-quadtree comme nouveau quadtree actuel. 
        Cela permet d'explorer en profondeur et de couvrir l'ensemble du quadtree pour trouver l'entité la plus proche. */
       /*
        for (int i = 0; i < quadtree.getNodes().length; i++) {
            if (quadtree.getNodes()[i] != null && quadtree.getNodes()[i].getBounds().overlaps(targetEntity.getHitboxFlat())) {
                findClosestEntityRecursive(targetEntity, quadtree.getNodes()[i], closestEntity, closestDistance);
            }
        }
    }*/


    
}
