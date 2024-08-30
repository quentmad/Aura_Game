package aura_game.app.rework;

import aura_game.app.Util.Triplet;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class Vehicle extends MovableEntity{

    private ActorEntity driver;

    public Vehicle(String name, int imageWidth, int imageHeight, int contentImageWidth, int contentImageHeight, int offsetY, float durability, float maxDurability, Hitbox hitbox, int stature, List<Triplet<String, Integer, Integer>> deathLoot, boolean onGround, String fileTexture, int posX, int posY, int speed, ActorEntity driver) {
        super(name, imageWidth, imageHeight, contentImageWidth, contentImageHeight, offsetY, durability, maxDurability, hitbox, stature, deathLoot, onGround, fileTexture, posX, posY, speed);
        this.driver = driver;
    }
}
