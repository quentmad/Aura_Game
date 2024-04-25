package aura_game.app.Objects.Tool;

import aura_game.app.Objects.PlayableEntity;
import aura_game.app.Type.EntityType;
import aura_game.app.Type.ToolType;
import com.badlogic.gdx.math.Vector2;

public class RangedTool extends Tool {
    private EntityType projectileEntType;
    private float projectileSpeed;
    private float projectileDamage;

    /**
     * Constructeur de la classe RangedTool.
     *
     * @param toolType Le type de l'outil.
     * @param projectileEntType Le type de projectile que cet outil tire.
     * @param projectileSpeed La vitesse à laquelle les projectiles sont tirés par cet outil.
     * @param projectileDamage Les dégâts infligés par les projectiles tirés par cet outil.
     */
    public RangedTool(ToolType toolType, EntityType projectileEntType, float projectileSpeed, float projectileDamage) {
        super(toolType, 0, 0, false, new Vector2(0,0), false);
        this.projectileEntType = projectileEntType;
        this.projectileSpeed = projectileSpeed;
        this.projectileDamage = projectileDamage;
    }

    /**
     * Méthode pour tirer un projectile.
     * Ajoute un projectile à la liste des projectiles actifs, qui sera géré par ProjectileManager.
     *
     * @param shooter L'entité qui tire le projectile.
     * @param projectileEntType Le type de projectile à tirer.
     */
    public void shoot(PlayableEntity shooter, EntityType projectileEntType) {
        // Créez un nouveau projectile et ajoutez-le à la liste des projectiles actifs
        //Projectile projectile = new Projectile(projectileEntType, shooter.getPosC_X(), shooter.getPosC_Y(), projectileSpeed, shooter.getEntityStateMachine().getCurrentOrientation(), projectileDamage);
        //ProjectileManager.getInstance().addProjectile(projectile);
    }

    // ... constructeur, getters, setters, etc.
}