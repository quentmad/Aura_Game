package aura_game.app.rework;

import com.badlogic.gdx.graphics.Texture;

public class SimpleObject extends AbstractObject{


    public SimpleObject(String name, int imageWidth, int imageHeight, int contentImageWidth, int contentImageHeight, int offsetY, String fileTexture, int posX, int posY) {
        super(name, imageWidth, imageHeight, contentImageWidth, contentImageHeight, offsetY, fileTexture, posX, posY);
    }

    public int z(){
        return 0;
    }

}
