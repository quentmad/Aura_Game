package aura_game.app.Objects.Tool;

import aura_game.app.Objects.CollidableObject;
import aura_game.app.Objects.Entity;
import aura_game.app.Orientation;
import aura_game.app.Type.EntityType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Projectile {//extends Entity {
    /*
    private float damage;
    private Vector2 direction;
    private float speed;

    public Projectile(EntityType entityType, int x, int y, float speed, Orientation orientation, float damage) {
        super(entityType, speed, x, y);
        this.damage = damage;
        this.direction = getProjectileVec(orientation.getX(), orientation.getY(), speed);
        this.speed = speed;
    }
*/

    /**
     * Calcule et retourne un vecteur normalisé qui représente la direction et la vitesse d'un projectile.
     *
     * @param dirX La composante x de la direction du projectile. Cette valeur doit être comprise entre -1 et 1.
     * @param dirY La composante y de la direction du projectile. Cette valeur doit être comprise entre -1 et 1.
     * @param speed La vitesse du projectile. Si cette valeur est 0, une vitesse par défaut de 30 est utilisée.
     *
     * @return Un objet Vector2 qui représente la direction et la vitesse du projectile. Le vecteur est normalisé, ce qui signifie que sa longueur est de 1, et il est ensuite mis à l'échelle par la vitesse du projectile.
     */
    /*
    private Vector2 getProjectileVec(float dirX, float dirY, float speed) {
        float vecSpeed = 30;
        if (speed != 0) {
            vecSpeed = speed;
        }
        return new Vector2(dirX, dirY).nor().scl(vecSpeed);
    }

    public void updatePosition() {
        // Met à jour la position du projectile en fonction de sa direction et de sa vitesse
        int newX = getPosC_X() + (int)(direction.x * speed);
        int newY = getPosC_Y() + (int)(direction.y * speed);

        // Met à jour la position de l'entité
        setPosC_X(newX);
        setPosC_Y(newY);
        // Met à jour la hitbox de l'entité
        updateHitbox();

    }
    // ... autres méthodes, etc.


    public void handleCollision(Entity entity) {
        // Reduce the entity's health by the projectile's damage
        entity.hurt(this.damage);

        // Other collision handling logic (e.g., play a sound, create an explosion, etc.) can go here
    }
    */

}
