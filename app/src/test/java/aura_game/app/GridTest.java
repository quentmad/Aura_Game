package aura_game.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.math.Rectangle;
import aura_game.app.Objects.CollidableObject;

/**
 * Classe de test pour la classe Grid.
 */
public class GridTest {
    private Grid grid;
    private CollidableObject objBasic;
    @BeforeEach
    public void setUp() {
        Gdx.files = new LwjglFiles();
        Gdx.app = new LwjglApplication(new ApplicationListener() {
            @Override
            public void create() {}
            @Override
            public void render() {}
            @Override
            public void dispose() {}
            @Override
            public void resize(int width, int height) {
                throw new UnsupportedOperationException("Unimplemented method 'resize'");
            }
            @Override
            public void pause() {
                throw new UnsupportedOperationException("Unimplemented method 'pause'");
            }
            @Override
            public void resume() {
                throw new UnsupportedOperationException("Unimplemented method 'resume'");
            }
        });
        grid = new Grid(10);
        objBasic = new CollidableObject("ObjBasic", new Rectangle(5, 5, 10, 10), 0, 0, 0, null, 0, 0, null);
    }



    
    @Test
    public void testGetCaseFor() {
        Rectangle rect = new Rectangle(0, 0, 20, 20);
        List<Pair<Integer, Integer>> expected = new ArrayList<>();
        expected.add(Pair.of(0, 0));
        expected.add(Pair.of(0, 1));
        expected.add(Pair.of(1, 0));
        expected.add(Pair.of(1, 1));
        List<Pair<Integer, Integer>> result = grid.getCaseFor(rect);
        assertEquals(expected, result);
    }

    @Test
    public void testGetNeighborsCasesFor() {
        List<Pair<Integer, Integer>> gridIndex = new ArrayList<>();
        gridIndex.add(Pair.of(0, 0));
        gridIndex.add(Pair.of(0, 1));
        List<Pair<Integer, Integer>> expected = new ArrayList<>();
        expected.add(Pair.of(-1, -1));
        expected.add(Pair.of(-1, 0));
        expected.add(Pair.of(-1, 1));
        expected.add(Pair.of(0, -1));
        expected.add(Pair.of(0, 2));
        expected.add(Pair.of(1, -1));
        expected.add(Pair.of(1, 0));
        expected.add(Pair.of(1, 1));
        expected.add(Pair.of(1, 2));
        List<Pair<Integer, Integer>> result = grid.getNeighborsCasesFor(gridIndex);
        assertEquals(expected, result);
    }

    @Test
    public void testGetCollidingObjects() {
        Rectangle zoneCollision = new Rectangle(0, 0, 20, 20);
        //CollidableObject obj1 = new CollidableObject("Obj1", new Rectangle(5, 5, 10, 10), 0, 0, 0, null, 0, 0, null);
        CollidableObject obj2 = new CollidableObject("Obj2", new Rectangle(25, 25, 10, 10), 0, 0, 0, null, 0, 0, null);
        CollidableObject obj3 = new CollidableObject("Obj3", new Rectangle(15, 15, 10, 10), 0, 0, 0, null, 0, 0, null);
        grid.add(objBasic, objBasic.getListIndexGrid());
        grid.add(obj2,obj2.getListIndexGrid());
        grid.add(obj3, obj3.getListIndexGrid());
        ListCollidableObject expected = new ListCollidableObject();
        expected.add(objBasic);
        expected.add(obj3);
        ListCollidableObject result = grid.getCollidingObjects(zoneCollision);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInZone() {
        Rectangle zone = new Rectangle(0, 0, 20, 20);
        //CollidableObject obj1 = new CollidableObject("Obj1", new Rectangle(5, 5, 10, 10), 0, 0, 0, null, 0, 0, null);
        CollidableObject obj2 = new CollidableObject("Obj2", new Rectangle(25, 25, 10, 10), 0, 0, 0, null, 0, 0, null);
        boolean result1 = grid.isInZone(zone, objBasic);
        boolean result2 = grid.isInZone(zone, obj2);
        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testGetClosestObjectFrom() {
        //CollidableObject objCible = new CollidableObject("ObjCible", new Rectangle(5, 5, 10, 10), 0, 0, 0, null, 0, 0, null);
        CollidableObject obj1 = new CollidableObject("Obj1", new Rectangle(20, 20, 10, 10), 0, 0, 0, null, 0, 0, null);
        CollidableObject obj2 = new CollidableObject("Obj2", new Rectangle(30, 30, 10, 10), 0, 0, 0, null, 0, 0, null);
        CollidableObject obj3 = new CollidableObject("Obj3", new Rectangle(15, 15, 10, 10), 0, 0, 0, null, 0, 0, null);
        grid.add(obj1,obj1.getListIndexGrid());
        grid.add(obj2,obj2.getListIndexGrid());
        grid.add(obj2,obj3.getListIndexGrid());
        CollidableObject expected = obj3;
        CollidableObject result = grid.getClosestObjectFrom(objBasic);
        assertEquals(expected, result);
    }

    @Test
    public void testClosestObject() {
        //CollidableObject objCible = new CollidableObject("ObjCible", new Rectangle(5, 5, 10, 10), 0, 0, 0, null, 0, 0, null);
        CollidableObject obj1 = new CollidableObject("Obj1", new Rectangle(20, 20, 10, 10), 0, 0, 0, null, 0, 0, null);
        CollidableObject obj2 = new CollidableObject("Obj2", new Rectangle(30, 30, 10, 10), 0, 0, 0, null, 0, 0, null);
        CollidableObject obj3 = new CollidableObject("Obj3", new Rectangle(15, 15, 10, 10), 0, 0, 0, null, 0, 0, null);
        ListCollidableObject potentialObj = new ListCollidableObject();
        potentialObj.add(obj1);
        potentialObj.add(obj2);
        potentialObj.add(obj3);
        Pair<CollidableObject, Double> expected = Pair.of(obj3, 10.0);
        Pair<CollidableObject, Double> result = grid.closestObject(potentialObj, objBasic);
        assertEquals(expected, result);
    }

    @Test
    public void testDistanceBetween() {
        Rectangle rect1 = new Rectangle(0, 0, 10, 10);
        Rectangle rect2 = new Rectangle(20, 20, 10, 10);
        double expected = 28.284271247461902;
        double result = grid.distanceBetween(rect1, rect2);
        assertEquals(expected, result, 0.0001);
    }


    @Test
    public void testUpdate() {
        //CollidableObject obj = new CollidableObject("Obj", new Rectangle(5, 5, 10, 10), 0, 0, 0, null, 0, 0, null);
        grid.add(objBasic, objBasic.getListIndexGrid());
        boolean result1 = grid.update(objBasic);
        boolean result2 = grid.update(objBasic);
        assertTrue(result1);
        assertFalse(result2);
    }
    /**
     * Test de la méthode add().
     * Vérifie si la méthode ajoute correctement un objet à la grille.
     */
    @Test
    public void testAdd() {
        //CollidableObject obj = new CollidableObject("Obj", new Rectangle(5, 5, 10, 10), 0, 0, 0, null, 0, 0, null);
        List<Pair<Integer, Integer>> listIndex = new ArrayList<>();
        listIndex.add(Pair.of(0, 0));
        listIndex.add(Pair.of(0, 1));
        grid.add(objBasic, listIndex);
        ListCollidableObject expected = new ListCollidableObject();
        expected.add(objBasic);

        assertEquals(expected, grid.getCollidingObjects(objBasic.getHitboxFlat()));
    }

    /**
     * Test de la méthode remove().
     * Vérifie si la méthode supprime correctement un objet de la grille.
     */
    @Test
    public void testRemove() {
        //CollidableObject obj = new CollidableObject("Obj", new Rectangle(5, 5, 10, 10), 0, 0, 0, null, 0, 0, null);
        List<Pair<Integer, Integer>> listIndex = new ArrayList<>();
        listIndex.add(Pair.of(0, 0)); 
        listIndex.add(Pair.of(0, 1)); 
        grid.add(objBasic, objBasic.getListIndexGrid()); 
        grid.remove(objBasic, listIndex); 
        ListCollidableObject expected = new ListCollidableObject(); 
        assertEquals(expected, grid.getCollidingObjects(objBasic.getHitboxFlat())); 
    } 


    
}