package aura_game.app.rework;

import aura_game.app.Util.Triplet;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;

public class SimpleEntity extends AbstractEntity{

    private boolean breakable;


    public SimpleEntity(String name, int imageWidth, int imageHeight, int contentImageWidth, int contentImageHeight, int offsetY, float durability, float maxDurability, Hitbox hitbox, PhysicsComponent physics, int stature, List<Triplet<String, Integer, Integer>> deathLoot, boolean onGround, String fileTexture, int posX, int posY, Texture texture, boolean breakable) {
        super(name, imageWidth, imageHeight, contentImageWidth, contentImageHeight, offsetY, durability, maxDurability, hitbox, physics, stature, deathLoot, onGround, fileTexture, posX, posY);
        this.breakable = breakable;
    }
}
