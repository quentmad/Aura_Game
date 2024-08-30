package aura_game.app.rework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractObject {

    private final String name;
    private Point posC;
    /**Largeur de l'image de l'entité (un sprite)*/
    private int imageWidth;
    /**Hauteur de l'image de l'entité (un sprite)*/
    private int imageHeight;
    private int contentImageWidth;
    private int contentImageHeight;
    private int offsetY;
    private boolean onGround;

    private Texture texture; // Chargement de la sprite sheet Texture


    public AbstractObject(String name, int imageWidth, int imageHeight, int contentImageWidth, int contentImageHeight, int offsetY, String fileTexture, int posX, int posY) {
        this.posC = new Point(posX, posY);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.contentImageWidth = contentImageWidth;
        this.contentImageHeight = contentImageHeight;
        this.offsetY = offsetY;
        this.onGround = false;//TODO
        this.name = name;
        if(this instanceof Player){
            this.texture = new Texture("src/main/resources/Entities/Aiden_Complete_V3.png");

        }else {
            this.texture = new Texture(Gdx.files.internal(fileTexture));
        }


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

    public int getPosOnScreenX(int cameraX) {
        return posC.x() - cameraX;
    }

    public int getPosOnScreenY(int cameraY) {
        return posC.y() - cameraY;
    }

    public int imageWidth() {
        return imageWidth;
    }

    public int imageHeight() {
        return imageHeight;
    }

    public Point posC() {
        return posC;
    }

    public int z(){
        return posC.y();
    }

    public int offsetY() {
        return offsetY;
    }

    public void addToPosCX(int x) {
        posC.setX(posC.x() + x);
    }

    public void addToPosCY(int y) {
        posC.setY(posC.y() + y);
    }

    /**@return la position X du joueur au niveau du milieu de sprite */
    public int getCenterX() {
            return posC().x() + imageWidth/2;

    }

    public String name(){
        return name;
    }

    /**@return la position Y du joueur au niveau du milieu de sprite */
    public int getCenterY() {
        return posC().y() + imageHeight/2;
    }


    public Texture texture() {
        return texture;
    }

    /**
     * Distance entre l'objet et obj2, par rapport au point en bas à droite de l'entité.
     * Pour récupérer les valeurs :
     * int dx = calculateDistance(obj2).getLeft()
     * int dy = calculateDistance(obj2).getRight()
     * @param obj2 l'objet 2
     * @return la pair dx dy
     */
    public Pair<Integer,Integer> calculateDistancePair(AbstractObject obj2) {
        int dx = this.posC().x() - obj2.posC().x();
        int dy = this.posC().y() - obj2.posC().y();
        return Pair.of(dx, dy);
    }

    /**
     * Distance entre l'objet et obj2, par rapport au centre de l'entité.
     * @param obj2 l'objet 2
     * @return la distance (entier)
     */
    public int calculateDistance(AbstractObject obj2) {
        int dx = this.getCenterX() - obj2.getCenterX();
        int dy = this.getCenterY() - obj2.getCenterY();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    public void toStringInfo(){
        System.out.println("name :" + name);
        System.out.println("posX : "+posC.x()+" posY : "+posC.y());
    }

    public void render(SpriteBatch batch, Region region){
        System.out.println("abs object");
        batch.draw(texture, posC.x(), posC.y());
    }


}
