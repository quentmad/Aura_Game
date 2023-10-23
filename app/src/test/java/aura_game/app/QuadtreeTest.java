package aura_game.app;
/*
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.Rectangle;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Before;

public class QuadtreeTest {
    private Quadtree quadtree;
    private Quadtree quadtreeCol;
    //private Rectangle rect1, rect2, rect3, rect4, rect5, rect6, rect7, rect8;
    private Object ob1, ob2, ob3, ob4, ob5, ob6, ob7, ob8;
    private Object objRetrieve;
    private Object o1,o2;

    @BeforeEach
    public void setUp() throws Exception {
        quadtree = new Quadtree(0, new Rectangle(0, 0, 100, 100));
        //--------------------------------------------------------
        quadtreeCol = new Quadtree(0, new Rectangle(0, 0, 100, 100));

        objRetrieve = new Object(new Rectangle(0, 0, 100, 100), 0, 0, 0, null);
        Object o1 = new Object(new Rectangle(10, 10, 20, 20),0,0,0,null);
        Object o2 = new Object(new Rectangle(80, 80, 10, 10),0,0,0,null);
        quadtree.insert(o1);
        quadtree.insert(o2);
        
        ob1 = new Object(new Rectangle(10, 10, 20, 20),0,0,0,null);
        ob2 = new Object(new Rectangle(30, 10, 20, 20),0,0,0,null);
        ob3 = new Object(new Rectangle(50, 10, 20, 20),0,0,0,null);
        ob4 = new Object(new Rectangle(10, 30, 20, 20),0,0,0,null);
        ob5 = new Object(new Rectangle(20, 20, 40, 40),0,0,0,null);
        ob6 = new Object(new Rectangle(50, 30, 20, 20),0,0,0,null);
        ob7 = new Object(new Rectangle(10, 50, 20, 20),0,0,0,null);
        ob8 = new Object(new Rectangle(30, 50, 20, 20),0,0,0,null);
        quadtreeCol.insert(ob1);
        quadtreeCol.insert(ob2);
        quadtreeCol.insert(ob3);
        quadtreeCol.insert(ob4);
        quadtreeCol.insert(ob5);
        quadtreeCol.insert(ob6);
        quadtreeCol.insert(ob7);
        quadtreeCol.insert(ob8);
    }
    
    @Test
    public void testInsert() {
        Object o1 = new Object(new Rectangle(10, 10, 20, 20),0,0,0,null);
        Object o2 = new Object(new Rectangle(80, 80, 10, 10),0,0,0,null);
        List<Object> retrieved = new ArrayList<>();
        quadtree.retrieve(retrieved, objRetrieve);
        assertTrue(retrieved.contains(o1));
        assertTrue(retrieved.contains(o2));
    }

    @Test
    public void testRemove() {
        Object o = new Object (new Rectangle(50, 50, 10, 10), 0, 0, 0, null);
        quadtree.insert(o);
        assertTrue(quadtree.remove(o));
        List<Object> retrieved = new ArrayList<>();
        quadtree.retrieve(retrieved, objRetrieve);
        assertFalse(retrieved.contains(o));
    }


    @Test
    public void testUpdate() {
        o2 = new Object(new Rectangle(70, 70, 10, 10),0,0,0,null);
        quadtree.update(o2);
        List<Object> retrieved = new ArrayList<>();
        quadtree.retrieve(retrieved, objRetrieve);
        assertTrue(retrieved.contains(o1));
        assertTrue(retrieved.contains(o2));
    }

    
    @Test
    public void testRetrieve() {
        List<Object> returnObjects = new ArrayList<>();
        quadtree.retrieve(returnObjects, objRetrieve);
        assertEquals(2, returnObjects.size());
        assertTrue(returnObjects.contains(o1));
        assertTrue(returnObjects.contains(o2));
    } 


    @Test
    void testGetCollidingRectangle() {
        List<Object> expectedCollisions = new ArrayList<>();
        //expectedCollisions.add(rect2);
        expectedCollisions.add(ob5);
        assertEquals(expectedCollisions, quadtreeCol.getCollidingObjects(ob1));
        assertEquals(expectedCollisions, quadtreeCol.getCollidingObjects(ob1));
        expectedCollisions.clear();
        expectedCollisions.add(ob1);
        expectedCollisions.add(ob2);
        expectedCollisions.add(ob3);
        expectedCollisions.add(ob4);
        expectedCollisions.add(ob6);
        expectedCollisions.add(ob7);
        expectedCollisions.add(ob8);
        assertEquals(expectedCollisions, quadtreeCol.getCollidingObjects(ob5));
        expectedCollisions.clear();
        assertEquals(expectedCollisions, quadtreeCol.getCollidingObjects(new Object(new Rectangle(2, 2, 6, 6),0,0,0,null)));
    }
    
    
}


*/