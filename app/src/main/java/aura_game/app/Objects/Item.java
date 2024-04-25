package aura_game.app.Objects;

import java.util.ArrayList;
import java.util.List;

import aura_game.app.GameManager.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import aura_game.app.Region;
import aura_game.app.Type.ItemType;

/**
 * Un item a une image (à l'inverse d'une entite qui un spritesheet)
*/
public class Item extends CollidableObject {
    // ItemType:
    
    private Texture textureItem;
    private int z;// Profondeur: y du debut de l'item
    private float[] listOriginalPolygon;
    /**Largeur de la texture complète (image complete)*/
    private final int textureWidth;
    /**Hauteur de la texture complète (image complete)*/
    private final int textureHeight; 
    /**
     * Permet de visualiser le type de l'item, non modifiable
     */
    private final ItemType type;
    private final String color;

    // position de l'item sur la carte --> point en bas à gauche de l'image
    public Item(ItemType type, int casePosX, int casePosY, String color) {
        super(type.getName(),new Rectangle(), (casePosX - 1) * 32 + 1, (casePosY - 1) * 32 + 1,
        type.tall(), new Polygon(new float[] { 1, 1, 2, 2, 3, 3 }),type.hitboxHeight(),type.offY(), type.deathLoots());
        String fullpath = type.getFullNameFileFromColor(color);
        if(fullpath == null){
            throw new IllegalArgumentException("The color "+color+ "doesn't exist for "+type.getName());   
        }
        this.color = color;
        this.textureItem = new Texture(fullpath);                                                         
        this.getHitboxFlat().set(getPosC_X(), getPosC_Y(), textureItem.getWidth(),textureItem.getHeight());
        this.z = getPosC_Y() + type.offY();
        this.listOriginalPolygon = type.listPolygon();
        this.textureWidth = type.textureWidth();
        this.textureHeight = type.textureHeight();
        this.type = type;

        this.updateHitboxsAndPosition(casePosX, casePosY);//Met à jour la position de l'item et les hitboxs
    }

    public Texture getTextureItem() {
        return textureItem;
    }

    public int getZ() {
        return z;
    }
    /**
     * Permet de set la position de l'item à partir d'un x y et de cette liste de points du polygon
     * @return la liste des points du polygon de base de l'item (origine à 0 0)
     */
    public float[] getListOriginalPolygon(){
        return listOriginalPolygon;
    }

    /**
     * Retourne l'ItemType de l'item
     * Utile pour pouvoir créer de nouveaux items "comme celui-ci"
     * @return l'ItemType de l'Item
     */
    public ItemType getType(){
        return type;
    }

    //TODO: Tous les items n'ont pas besoin de différentes color......
    public String getColor(){
        return color;
    }


    /**@return la position X du joueur au niveau du milieu de sprite */
    public int getCenterX(){
        return getPosC_X() + (textureWidth)/2;
    }

    /**@return le point x à lequel il faut mettre le loot pour qu'il soit parfaitement centré horizontalement sur l'item */
    public int getLootSpawnCenterX(int lootWidth){
        return getPosC_X() + (textureWidth / 2) - (lootWidth / 2);
    }

    /**
     * Met a jour la position de l'item, le hitbox rectangle et polygon à partir des coordonnées en parametre
     * @param casePosX
     * @param casePosY
     */
    public void updateHitboxsAndPosition(int casePosX, int casePosY){
        //Maj hitboxXPos...
        //TODO je dois mettre quoi pour hitboxposX... ??????
        this.setPosC_X((casePosX - 1) * 32 + 1);
        this.setPosC_Y((casePosY - 1) * 32 + 1);
        this.z = getPosC_Y() + getOffY();
         this.updateHitbox();
        
    }

    public void updateHitbox(){
        this.getHitboxFlat().set(getPosC_X(), getPosC_Y(), textureItem.getWidth(),textureItem.getHeight());
        this.setHitboxPolygon(new Polygon(updatePolygonPoints()));
    }

    /**
    * Met a jour la position de l'item, le hitbox rectangle et polygon à partir des coordonnées en parametre* @param casePosX
     * @param dx déplacement en x
     * @param dy déplacement en y
     */
    public void moveHitboxsAndPosition(int dx, int dy){
        System.out.println("move the" + this.getName() +" of " + dx+", dy: "+dy);
        this.setPosC_X(this.getPosC_X() + dx);
        this.setPosC_Y(this.getPosC_Y() + dy);
        this.z = getPosC_Y() + getOffY();
        this.updateHitbox();

    }
    /**
     * Met a jour la position de l'item, le hitbox rectangle et polygon à partir des coordonnées en parametre
     *
     * @return la liste des points du polygon de l'item avec la position actuelle de l'item
     */

    public float[] updatePolygonPoints(){
        float[] points = new float[getListOriginalPolygon().length];
        for (int i = 0; i < getListOriginalPolygon().length; i++) {
            points[i] = getListOriginalPolygon()[i];
            if (i % 2 == 0) {
                points[i] += this.getPosC_X();
            } else {
                points[i] += this.getPosC_Y();
            }
        }
        return points;
    }



    /**
     * Détermine s'il y a une collision avec la map / les entités /items avec la hitbox
     * ayant pour origine ox oy: lorsqu'on veut poser un item... (pas de 
     * "mouvement/direction")
     * 
     * @param region sur lequel l'objet se trouve lors de l'action effectuée
     * @return true si une collision est détecté, false sinon
     */
    public boolean willCollideIn(Region region) {
        float[] polyf = getHitboxPolygon().getVertices();// Tableau avec l'ensemble des points du polygone hitbox
        
        // Collision avec polygon:
        List<CollidableObject> objColList = region.getGridItem().getCollidingObjectsWithoutItself(getHitboxFlat(), this).getList();
        // Detection au niveau de la region/pixmap
        if(willCollideGroundNoMove(region, polyf)){return true;}
        //Detection avec les autres entités
        if(!willCollidePolygonNoMove(polyf, objColList, region).isEmpty()){return true;}
        return false;
    }

    /**
     * Détermine s'il y a une collision avec la map / les entités /items avec la hitbox de this.
     * On ignore la possible collission avec l'objet passé en paramètre
     * @param region sur lequel l'objet se trouve lors de l'action effectuée
     * @param ignoredObject l'objet avec lequel on ne veut pas détecter de collision
     * @return
     */
    public boolean willCollideInIgnoring(Region region, CollidableObject ignoredObject){

        float[] polyf = getHitboxPolygon().getVertices();// Tableau avec l'ensemble des points du polygone hitbox

        // Collision avec polygon:
        List<CollidableObject> objColList = region.getGridItem().getCollidingObjectsWithoutItself(getHitboxFlat(), this).getList();

        // Detection au niveau de la region/pixmap
        if(willCollideGroundNoMove(region, polyf)){return true;}
        //Detection avec les autres entités
        List<CollidableObject> colissionPoly = willCollidePolygonNoMove(polyf, objColList, region);
        if(!colissionPoly.isEmpty()){
            //Si il y a collision avec un objet autre que celui qu'on veut ignorer
            System.out.println(this.getName() + "have col with other object");
            System.out.println("colissionPoly: size: "+colissionPoly.size());
            for (CollidableObject collidableObject : colissionPoly) {
                System.out.println("col with: "+collidableObject.getName());
            }
            if(colissionPoly.size() == 1 && colissionPoly.get(0) == ignoredObject){
                System.out.println("col with ignored object only");
                return false;
            }
            return true;
        }
        return false;
    }




    /**Pour les items (sans mouvement), dit s'il y a collision de this avec PixColision map
     * @param region la region cible
     * @param vertices la liste des points du polygon de this
    */
    public boolean willCollideGroundNoMove(Region region, float[] vertices){
        for (int i = 0; i < vertices.length; i += 2) {// i: x, i+1: y
            /*Pour tous les points du polygon, on verifie si il y aurait collision avec
             pixmap (le sol de la region)*/
            if (region.getPixColision().getPixel(Math.round(vertices[i]),
                    (region.getRegionHeight() - Math.round(vertices[i + 1]))) != 0) {// Y a pour origine le haut
                //System.out.println("region collision at x: " + polyf[i] + ", y: " + polyf[i + 1]);
                return true;
            }
        }
        return false;
    }
    /**Pour les items (sans mouvement), dit s'il y a collision avec un autre CollidableObject de objCol
     * @param verticesColission la liste des points du polygon de this, celui qui veut faire l'action/lance la méthode
     * @param objCol la liste des CollidableObjects avec lequel il y a eu collision au niveau du rectangle hitbox
     * @param region la region cible
    */
    public List<CollidableObject> willCollidePolygonNoMove(float[] verticesColission, List<CollidableObject> objCol, Region region){
        List<CollidableObject> colissionPoly = new ArrayList<>();
        // Detection au niveau des objects (quadtree)
        for (CollidableObject object : objCol) {// Si la liste est non vide c'est qu'il y a des colissions au niveau des
                                          // rectangles
            //System.out.println("col rectangle");
            for (int i = 0; i < verticesColission.length; i += 2) {
                if (object.getHitboxPolygon().contains(Math.round(verticesColission[i]), Math.round(verticesColission[i + 1]))) {// Y a pour origine le bas                                                                    
                                                                                                         
                    //System.out.println("col with objdetected");
                    colissionPoly.add(object);
                    //On passe au prochain objet
                    break;
                }

            }
        }
        return colissionPoly;        
    }


    /**
     * Déplace l'entité de dx en X et dy en Y tout en gérant les collisions.
     * Met à jour posC_X et posC_Y par tranche de 1 pour evité des blocages en bord de map, et appelle updateHitbox().
     *
     * @param dx Le déplacement en X.
     * @param dy Le déplacement en Y.
     */
    public void move(int dx, int dy, int speed) {
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

        updateHitbox(); // Met à jour l'hitbox une fois les déplacements effectués.
        Game.getInstance().getRegion().getGridItem().update(this);

    }


    /**
     * Verifie si le X est dans la region (selon la hitbox du personnage) (au niveau des bords)
     * @param x la coordonnée x dont on veut tester l'appartenance
     * @return true si x est dans la region 0 sinon
     */
    public boolean isXOnRegion(int x){
        return (x >= -textureWidth && x <= (Game.getInstance().getRegion().getRegionWidth() - textureWidth)) ;
    }

    /**
     * Verifie si le Y est dans la region (selon la hitbox du personnage) (au niveau des bords)
     * @param y la coordonnée y dont on veut tester l'appartenance
     * @return true si y est dans la region 0 sinon
     */
    public boolean isYOnRegion(int y){
        return (y >= 0 && y <= (Game.getInstance().getRegion().getRegionHeight() - getHitboxHeight())) ;
    }
}
