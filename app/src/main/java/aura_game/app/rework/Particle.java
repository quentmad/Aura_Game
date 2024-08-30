package aura_game.app.rework;

import aura_game.app.LPCActions.Animation;
import aura_game.app.Util.Triplet;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

public class Particle extends MovableEntity{

    private Vector3 movement;
    private Animation animation;

    public Particle(String name, int imageWidth, int imageHeight, int contentImageWidth, int contentImageHeight, int offsetY,      float durability, float maxDurability, Hitbox hitbox, int stature, List<Triplet<String,Integer,Integer>> deathLoot, boolean onGround, String fileTexture, int posX, int posY,   int speed,      Vector3 movement, Animation animation) {
        super(name, imageWidth, imageHeight, contentImageWidth, contentImageHeight, offsetY, durability, maxDurability, hitbox, stature, deathLoot, onGround, fileTexture, posX, posY, speed);
        this.movement = movement;
        this.animation = animation;

    }


}
