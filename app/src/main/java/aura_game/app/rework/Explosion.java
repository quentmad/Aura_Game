package aura_game.app.rework;

import aura_game.app.LPCActions.Animation;
import aura_game.app.Util.Triplet;

import java.util.List;

public class Explosion extends AbstractEntity{

    private Animation animation;
    private float radius;
    private float damage;



    public Explosion(String name, int imageWidth, int imageHeight, int contentImageWidth, int contentImageHeight, int offsetY,      float durability, float maxDurability, Hitbox hitbox, PhysicsComponent physics, int stature, List<Triplet<String,Integer,Integer>> deathLoot, boolean onGround, String fileTexture, int posX, int posY, Animation animation, float radius, float damage) {
        super(name, imageWidth, imageHeight, contentImageWidth, contentImageHeight, offsetY, durability, maxDurability, hitbox, physics, stature, deathLoot, onGround, fileTexture, posX, posY);
        this.animation = animation;
        this.radius = radius;
        this.damage = damage;

    }


}
