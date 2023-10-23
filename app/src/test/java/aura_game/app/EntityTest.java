
package aura_game.app;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;//BeforeClass
import org.junit.jupiter.api.BeforeEach;//Before
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import aura_game.app.Objects.Entity;
import aura_game.app.Type.EntityType;

import org.apache.commons.lang3.tuple.Pair;

public class EntityTest {

    private Entity entity;

    @BeforeEach
    public void setUp() {
        // Création de l'entité pour les tests
        Rectangle hitboxFlat = new Rectangle(0, 0, 10, 10);
        int posC_X = 0;
        int posC_Y = 0;
        int hitboxTall = 0;
        Polygon hitboxPolygon = new Polygon(new float[]{1, 1, 2, 2, 3, 3});
        //EntitySpriteSheet entitySpriteSheet = new EntitySpriteSheet("Cerf", 32, 32, 32, 32, 32, 32, 1);
        int speed = 1;
        entity = new Entity(EntityType.cerf1, speed, posC_X, posC_Y);
    }

    @Test
    public void testChangeAction() {
        entity.changeAction("Walk_U");
        assertEquals("Walk_U", entity.getActualActionName());
    }

    @Test
    public void testCalculateDistance() {
        // Créez une autre entité à une certaine distance de l'entité actuelle
        Entity otherEntity = new Entity(null, 0, 50, 50);
        int expectedDistance = 71; // Calculé à l'aide de la formule sqrt((50-0)^2 + (50-0)^2) = sqrt(5000) ≈ 71
        int actualDistance = entity.calculateDistance(otherEntity);
        assertEquals(expectedDistance, actualDistance);
    }
/*
    @Test
    public void testFindClosestEntity() {
        Quadtree quadtree = new Quadtree(new Rectangle(0, 0, 100, 100));
        Entity closestEntity = entity.findClosestEntity(quadtree);
        assertNull(closestEntity); // Le quadtree est vide, donc il ne doit y avoir aucune entité proche

        // Ajoutez des entités dans le quadtree
        Entity entity1 = new Entity(null, 0, 20, 20);
        Entity entity2 = new Entity(null, 0, 30, 30);
        quadtree.insert(entity1);
        quadtree.insert(entity2);

        closestEntity = entity.findClosestEntity(quadtree);
        assertEquals(entity1, closestEntity); // entity1 est la plus proche de l'entité actuelle
    }

    @Test//TESTER SEULEMENT PAR DOACTION
    public void testExecuteStaticAction() {
        // Test avec une action statique en cours
        entity.changeAction("Slash_U");
        entity.updateCurrentSpriteX();
        entity.executeStaticAction();
        Assert.assertEquals(1, entity.getCurrentSpriteX()); // L'animation doit avancer d'une itération

        // Test avec une action statique terminée
        for (int i = 0; i < entity.getLastEndSpriteX(); i++) {
            entity.updateCurrentSpriteX(); // Passe à travers toute l'animation
        }
        entity.executeStaticAction();
        Assert.assertEquals(0, entity.getCurrentSpriteX()); // L'animation doit être réinitialisée à 0
    }

    @Test
    public void testCheckColissionAndExecuteContiniousAction() {
        // À compléter avec des cas de test pour checkColissionAndExecuteContiniousAction
        // Assurez-vous de tester les scénarios avec et sans collision
        // et vérifiez si l'entité se déplace correctement
    }

    @Test
    public void testMove() {
        // À compléter avec des cas de test pour la méthode move
        // Assurez-vous de tester les scénarios où le mouvement est dans la zone de la carte
        // et ceux où le mouvement est en dehors de la zone de la carte
    }

    @Test
    public void testPutItemFromInventoryOnRegion() {
        // À compléter avec des cas de test pour la méthode putItemFromInventoryOnRegion
        // Assurez-vous de tester les scénarios où l'objet peut être posé sur la carte
        // et ceux où il ne peut pas être posé en raison d'une collision avec d'autres objets
    }
    */
    // Ajoutez d'autres tests pour les autres méthodes et fonctionnalités importantes de la classe Entity
}