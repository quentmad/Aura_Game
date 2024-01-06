package aura_game.app.Objects;

import org.apache.commons.lang3.tuple.Pair;

/**Cette classe est un point commun entre Loot et Item/Entités */
public abstract class BasicObject {

    /**Position du basicObject sur la carte --> point en bas à gauche de l'image */
    private  int posC_X;
    /**Position du basicObject sur la carte --> point en bas à gauche de l'image */
    private int posC_Y;    

    public BasicObject(int x, int y){
        this.posC_X = x;
        this.posC_Y = y;
    }

    /**Position du basicObject sur la carte --> point en bas à gauche de l'image */
    public int getPosC_X() {
        return posC_X;
    }
    
    /**Position du basicObject sur la carte --> point en bas à gauche de l'image */
    public int getPosC_Y() {
        return posC_Y;
    }
    /**Modification de la position x
     * @param x ajout à la position PosC_X actuelle
     */
    public void addToPosC_X(int x) {
        posC_X+=x;
    }
    
    /**Modification de la position y
     * @param y ajout à la position PosC_Y actuelle
     */
    public void addToPosC_Y(int y) {
        posC_Y+=y;
    }


    /**Modification de la position x
     * @param x la nouvelle position X
     */
    public void setPosC_X(int x) {
        posC_X=x;
    }
    
    /**Modification de la position y
     * @param y la nouvelle position Y
     */    
    public void setPosC_Y(int y) {
        posC_Y=y;
    }
    /**
     * Calcule la position en X du basicObject sur l'écran en fonction de la position
     * de la caméra, permettant ainsi de gérer le décalage de la map.
     *
     * @param cameraX La position en X de la caméra.
     * @return La position en X du basicObject sur l'écran.
     */
    public int getPosOnScreenX(int cameraX) {
        return getPosC_X() - cameraX;
    }

    /**
     * Calcule la position en Y du basicObject sur l'écran en fonction de la position
     * de la caméra, permettant ainsi de gérer le décalage de la map.
     *
     * @param cameraY La position en Y de la caméra.
     * @return La position en Y du basicObject sur l'écran.
     */
    public int getPosOnScreenY(int cameraY) {
        return getPosC_Y() - cameraY;
    }
    
    /**
     * Permet d'afficher dans l'ordre les éléments sur la map
     * @return getZ() si c'est un item ou getPosC_Y si c'est une Entity ou basicObject
     */
    public int getZProf(){
        if(this instanceof Item){
            return ((Item) this).getZ();
        }
        return getPosC_Y();//TODO: pas beau pour loots
    }

    /**
     * Distance entre l'objet et obj2, par rapport au point en bas à droite de l'entité.
     * Pour récupérer les valeurs :
     * int dx = calculateDistance(obj2).getLeft()
     * int dy = calculateDistance(obj2).getRight()
     * @param obj2 l'objet 2
     * @return la pair dx dy 
     */
    public Pair<Integer,Integer> calculateDistancePair(CollidableObject obj2) {
        int dx = this.getPosC_X() - obj2.getPosC_X();
        int dy = this.getPosC_Y() - obj2.getPosC_Y();
        return Pair.of(dx, dy);
    }

    /**
     * Distance entre l'objet et obj2, par rapport au point en bas à droite de l'entité.
     * @param obj2 l'objet 2
     * @return le int correspondant à la distance
     */
    public int calculateDistance(CollidableObject obj2) {
        int dx = this.getPosC_X() - obj2.getPosC_X();
        int dy = this.getPosC_Y() - obj2.getPosC_Y();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }
}
