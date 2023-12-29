package aura_game.app;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import aura_game.app.GameManager.Game;
import aura_game.app.Objects.BasicObject;
import aura_game.app.Objects.CollidableObject;
import aura_game.app.Objects.Entity;
import aura_game.app.Objects.Item;
import aura_game.app.Objects.Loot;


/**
 * Représente une région du jeu avec sa carte, ses objets et ses entités.
 */
public class Region {

    /**La texture de la carte de la région*/
    private Texture carte;

    /**Pixmap pour la détection de collision (montagnes, etc.)*/
    private Pixmap pixColision;

    /**Largeur de la région*/
    private int regionWidth;
    /**Hauteur de la région*/
    private int regionHeight;
    /**Largeur de l'écran*/
    private final int screenWidth;
    /**Hauteur de l'écran*/
    private final int screenHeight;
    /**Moitié de la largeur de l'écran*/
    private final int halfScreenWidth;
    /**Moitié de la hauteur de l'écran*/
    private final int halfScreenHeight;
    /**camera de la region, permet de décaler la map...*/
    private MyCamera cam;
    /**Limites de la caméra, selon la taille de la carte*/
    private int cameraMaxX; 
    /**Limites de la caméra, selon la taille de la carte*/
    private int cameraMaxY;
    /**Seuil de la caméra en X pour déclencher le défilement*/
    private int CAMERA_THRESHOLD_X;
    /**Seuil de la caméra en Y pour déclencher le défilement*/
    private int CAMERA_THRESHOLD_Y;

    /**Objets présents sur la carte (items, IAentités...)
     * Utile pour afficher dans le bon ordre Z les entités (superposition...)*/
    private List<BasicObject> basicObjectsOnRegion;
    /**Permet de lancer sort() sur ObjectsOnRegion que s'il y a eu au moins un mouvement*/
    private boolean objectsOnMapNeedSort;

    //private Quadtree quadtreeItem;
    private Grid gridItem;
    private Grid gridIAEntity;
    //private Quadtree quadtreeIAEntity;

    /**Instance unique*/
    private InventoryMenu inventoryMenu;
    //private LootManager lootManager;

    public Region(String nameMap, String namePixmap){
        this.carte = new Texture(Gdx.files.internal("src/main/resources/"+nameMap+".png"));//Sans ombres
        this.pixColision = new Pixmap(Gdx.files.internal("src/main/resources/"+namePixmap+".png"));
       // this.pixDecoColision =  new Pixmap(Gdx.files.internal(pathPixDecoColision));
        this.regionWidth = carte.getWidth();
        this.regionHeight = carte.getHeight();
        this.screenWidth = Game.getInstance().getScreenWidth();
        this.screenHeight = Game.getInstance().getScreenHeight();
        this.halfScreenWidth = screenWidth / 2;
        this.halfScreenHeight = screenHeight / 2;
        this.cameraMaxX = regionWidth - screenWidth;
        this.cameraMaxY = regionHeight - screenHeight;
        this.cam = new MyCamera();
        this.CAMERA_THRESHOLD_X=200;
        this.CAMERA_THRESHOLD_Y=90;
        this.basicObjectsOnRegion= new ArrayList<>();
        //this.entitiesOnMap=  new ArrayList<IAEntity>();
        //this.quadtreeItem = new Quadtree(0, new Rectangle(0, 0, regionWidth, regionHeight));
        this.gridItem = new Grid(500);
        this.gridIAEntity = new Grid(500);
        //this.quadtreeIAEntity = new Quadtree(0, new Rectangle(0, 0, regionWidth, regionHeight));
        this.inventoryMenu = Game.getInstance().getInventory();
        //this.lootManager = LootManager.getInstance();
        objectsOnMapNeedSort = false;
    }


    public Texture getCarte() {
        return carte;
    }

    public Pixmap getPixColision() {
        return pixColision;
    }
    public void setPixColision(Pixmap pxm) {
        pixColision= pxm;
    }
    
    public MyCamera getCamera(){
        return this.cam;
    }
    
    public int getRegionWidth(){
        return regionHeight;
    }

    public int getRegionHeight(){
        return regionHeight;
    }

    public List<BasicObject> getBasicObjectsOnRegion(){
        return basicObjectsOnRegion;
    }

    /**
     * Permet de lancer sort() sur basicObjectsOnRegion que s'il y a eu au moins un mouvement
     */
    public boolean getBasicObjectsOnRegionNeedSort(){
        return objectsOnMapNeedSort;
    }

    /**
     * Permet de lancer sort() sur basicObjectsOnRegion que s'il y a eu au moins un mouvement
     */    
    public void setBasicObjectsOnRegionNeedSort(boolean bool){
        objectsOnMapNeedSort = bool;
    }

    /**
     * Lorsqu'une entité souhaite bouger, on utilise le quadtree des items pour vérifier s'il y a une collision avec un item à la destination souhaitée.
     * Egalement lorsqu'on souhaite poser un item au sol
     */
    /*
    public Quadtree getQuadtreeItem(){
        return quadtreeItem;
    }*/

    public Grid getGridItem(){
        return gridItem;
    }
    public Grid getGridIAEntity(){
        return gridIAEntity;
    }

    /**
     * Lorsque qu'on veut poser un item ou attaquer une entité
     */
    /*public Quadtree getQuadtreeIAEntity(){
        return quadtreeIAEntity;
    }*/

    /**
     * Appelé par le(s) PlayableEntity lorsqu'il(s) bouge(nt)
     * Met à jour la position de la caméra pour que la carte "coulisse" si le joueur est au bord de l'écran
     * @param entity
     */
    public void calculAndUpdatePosition(Entity entity) {
        int[] positionVariation = calculatePositionVariation(entity);
        int dx = positionVariation[0];
        int dy = positionVariation[1];
        if(dx !=0 || dy != 0){
            if(cam != null){
                cam.updatePosition(dx, dy, regionWidth, regionHeight, screenWidth, screenHeight);
            }

        }
    }

    /**
     * Calcule la variation de position de l'entité en fonction de sa position par rapport à l'écran.
     * Lorsqu'on est sur le bord et qu'on avance l'ecran se décale
    */
     public int[] calculatePositionVariation(Entity entity) {    
        int dx = 0;
        //Bord droit
        if ((entity.getPosOnScreenX(cam.getX()) > halfScreenWidth + CAMERA_THRESHOLD_X)) {
            dx = entity.getPosOnScreenX(cam.getX()) - halfScreenWidth - CAMERA_THRESHOLD_X;
        //Bord gauche
        } else if (entity.getPosOnScreenX(cam.getX()) < CAMERA_THRESHOLD_X) {
            dx = entity.getPosOnScreenX(cam.getX()) - CAMERA_THRESHOLD_X;
        }
    
        int dy = 0;
        //Bord haut
        if (entity.getPosOnScreenY(cam.getY()) > halfScreenHeight + CAMERA_THRESHOLD_Y) {
            dy = entity.getPosOnScreenY(cam.getY()) - halfScreenHeight - CAMERA_THRESHOLD_Y;
        //Bord bas
        } else if (entity.getPosOnScreenY(cam.getY()) < CAMERA_THRESHOLD_Y) {
            dy = entity.getPosOnScreenY(cam.getY()) - CAMERA_THRESHOLD_Y;
        }
        // On met à jour la position de la caméra en ajoutant la variation de position de l'entité,
        // tout en s'assurant que la caméra ne sort pas des limites de la carte
        if (cam.getX() + dx < 0) {
            dx = - cam.getX();
        } else if (cam.getX() + dx > cameraMaxX) {
            dx = cameraMaxX - cam.getX();
        }
    
        if (cam.getY() + dy < 0) {
            dy = - cam.getY();
        } else if (cam.getY() + dy > cameraMaxY) {
            dy = cameraMaxY - cam.getY();
        }
    
        return new int[]{dx, dy};
    }
    
    /**
     * Nous informe si l'entité va etre en colission avec la map après son mouvement (pixmap...)
     * On verifie le "cadre" de l'hitbox
     * @param nextPosX
     * @param nextPosY
     * @param ent
     * @return
     */
    public boolean willCollideGroundMove(int nextPosX, int nextPosY, Entity ent){
        boolean collisionDetected = false;
        int directionX = (int) Math.signum(nextPosX - ent.getPosC_X());
        int directionY = (int) Math.signum(nextPosY - ent.getPosC_Y());
        
        for (int x = ent.getPosHitboxX(); x < ent.getPosHitboxX() + ent.getHitboxFlat().getWidth(); x++) {
            if (directionY==-1 && getPixColision().getPixel(x, (regionHeight - (ent.getPosHitboxY()))) != 0) {
                // Il y a collision sur le bord supérieur de l'hitbox
                collisionDetected = true;
                
            }
            if (directionY==1 && getPixColision().getPixel(x, regionHeight - (ent.getPosHitboxY() + (int)ent.getHitboxFlat().getHeight())) != 0) {
                // Il y a collision sur le bord inférieur de l'hitbox
                collisionDetected = true;
            }
        }
        for (int y = ent.getPosHitboxY(); y < ent.getPosHitboxY() + ent.getHitboxFlat().getHeight(); y++) {
            if (directionX ==1 && getPixColision().getPixel(ent.getPosHitboxX()+ (int)ent.getHitboxFlat().getWidth(), regionHeight - y) != 0) {
                // Il y a collision sur le bord gauche de l'hitbox
                collisionDetected = true;
            }
            if (directionX ==-1 && getPixColision().getPixel(ent.getPosHitboxX() ,regionHeight - y) != 0) {
                // Il y a collision sur le bord droit de l'hitbox
                collisionDetected = true;
            }
        }
        return collisionDetected;
    }

    //On tri la liste en fonction de la profondeur du basicObject pour les afficher 
    //de manière decroissant et savoir plus facilement à quelle couche afficher les entites 
    /**
     *Ajoute le basicObject (loots, items, entités) a la liste de la map, trié selon Z (s'affichant sur la map, dans l'ordre de Z)
     * @param newBasicObj objt a rajouter à la liste
     **/
    public void addSortedObject(BasicObject newBasicObj){//generic pour tt les items
        //System.out.println("passage \n");
        int index = 0;
        for (BasicObject o : basicObjectsOnRegion) {
            if (newBasicObj.getZProf() > o.getZProf()) {
                break;
            }
            index++;
        }
        basicObjectsOnRegion.add(index, newBasicObj);

    }


    /**
     * Appelé lorsque l'entité souhaite prendre un item par terre
     * Verifie si un item est récupérable, puis retourne l'object 
     * @param ent l'entité voulant récuperer l'item sur la region/map
     * @return l'item qui peut etre récupéré depuis la position de l'entité (avec la marge), null si il n'y en a pas
     */
    public Item takeableItemFromRegion(Entity ent){
        float[] polyf = ent.getHitboxPolygon().getVertices();// Tableau avec l'ensemble des points du polygone hitbox
        // Colission avec polygon:
        //List<CollidableObject> objColList = getQuadtreeItem().getCollidingObjects(ent);
        List<CollidableObject> objColList = getGridItem().getCollidingObjects(ent.getHitboxFlat()).getList();

        // Detection au niveau des objects (quadtree)
        for (CollidableObject object : objColList) {// Si la liste est non vide c'est qu'il y a des colissions au niveau des rectangles                         
            //System.out.println("col item retrieved at rectangle");
            for (int i = 0; i < polyf.length; i += 2) {
                if (object.getHitboxPolygon().contains(Math.round(polyf[i]), Math.round(polyf[i + 1]))) {// Y a pour origine le bas                                                                                                                                                                                                             
                    if(object instanceof Item){
                        System.out.println("an item has been taken");
                        return (Item)object;
                    }

                }

            }
        }
        return null;

    }

    /**TODO: mettre dans Item ????
     * Retire l'item de la region/map accessible depuis la  position de ent, si c'est possible, puis fait spawn les loots correspondant
     * @param ent l'entité voulant récuperer l'item sur la region/map
     * @return
     */
    public boolean killItemFromRegion(Entity ent){
        Item item = takeableItemFromRegion(ent);
        //boolean succeed = false;
        if(item != null){
            //succeed = inventoryMenu.addLootToInventory(item);
            //if(succeed){//Si l'item a été mis dans l'inventaire on le retire de la region/map
            //on retire l'item de l'ecran
            basicObjectsOnRegion.remove(item);
            //On retire sa hitbox de l'ecran également
            getGridItem().remove(item,item.getListIndexGrid());
            //getQuadtreeItem().remove(item);
            item.spawnDeathLoots();
            return true;
            //}else {System.out.print("inventory full, the max is " + inventoryMenu.getSlotRestant() );}

        }
        return false;
    }


    /**TODO: mettre dans Item ????
     * Retire l'item de la region/map accessible depuis la  position de ent, si c'est possible, puis fait spawn les loots correspondant
     * @param item l'item qu'on souhaite retirer (mort?)
     * @return
     */
    public boolean killFromRegion(Item item){
        if(item != null){
            //on retire l'item de l'ecran
            basicObjectsOnRegion.remove(item);
            //On retire sa hitbox de l'ecran également
            getGridItem().remove(item,item.getListIndexGrid());
            item.spawnDeathLoots();
            return true;
        }
        return false;
    }

    /**TODO: mettre dans Ent ????
     * Retire l'item de la region/map accessible depuis la  position de ent, si c'est possible, puis fait spawn les loots correspondant
     * @param ent l'entité qu'on souhaite retirer (mort?)
     * @return
     */
    public boolean killFromRegion(Entity ent){
        if(ent != null){
            //on retire l'item de l'ecran
            basicObjectsOnRegion.remove(ent);
            //On retire sa hitbox de l'ecran également
            getGridIAEntity().remove(ent,ent.getListIndexGrid());
            ent.spawnDeathLoots();
            return true;
        }
        return false;
    }


    /**
     * Cette classe est appelé quand le joueur passe au-dessus le loot et va le récupérer
     * Ainsi, on regarde si on peut le mettre dans l'inventaire, 
     * si c'est le cas on défini le loot comme collected (ce qui le retira de loots), et on le retire de basicObjectsOnRegion
     * @param loot le loot qu'on souhaite recupéré
     * @return true si l'opération s'est déroulé avec succès (place dans l'inventaire) sinon false
     */
    public boolean retrieveLootFromRegionAndPutInInventory(Loot loot){
        if(loot !=null){
            boolean succeed = inventoryMenu.addToInventory(loot,1);
            if(succeed){
                loot.setCollected(true);
                basicObjectsOnRegion.remove(loot);
            }
        }//Mettre dans playable ?

        return true;
    }

    
}
