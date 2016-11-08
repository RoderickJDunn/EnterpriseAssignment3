package bouncingsprites_test;
import bouncingsprites.Buffer;
import bouncingsprites.Sprite;
import bouncingsprites.SpriteSimulation;
import bouncingsprites.SynchronizedBuffer;
import bouncingspritesclient.SpritesClientLauncher;
import dataaccess.DataAccess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.*;
import utils.LogIt;

import javax.xml.crypto.Data;
import java.awt.*;
import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

//package bouncingsprites_test;
//
//import bouncingsprites.BouncingSprites;
//import bouncingsprites.Sprite;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
///**
// * Created by Roderick on 2016-09-25.
// */
public class HibernateSpriteTest {
    static SpriteSimulation sim;

    DataAccess dataAccess;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        sim = new SpriteSimulation();
    }

    @Before
    public void setUp() {
        dataAccess = DataAccess.getInstance();
    }

    @Test
    public void testSaveRetreiveSprite() {
        System.out.println("HEllooooo");

        Session session = dataAccess.getSession();
        Sprite sprite = new Sprite(sim, new SynchronizedBuffer(), 100, 100, 2, 2, Color.CYAN);
        session.beginTransaction();
        session.save(sprite);
        session.getTransaction().commit();
        session.close();

        int id = sprite.getId();

        session = dataAccess.getSession();
        session.beginTransaction();
        Sprite persistedSprite = session.get(Sprite.class, id);
        session.close();

        assertTrue(sprite.getId() == persistedSprite.getId());
        assertTrue(sprite.getX() == persistedSprite.getX());
        assertTrue(sprite.getY() == persistedSprite.getY());
        assertTrue(sprite.getColor().toString().equals(persistedSprite.getColor().toString()));
    }

    @Test
    public void testGetAllPersistedSprites() {
        Sprite sprite = new Sprite(sim, new SynchronizedBuffer(), 100, 100, 2, 2, Color.CYAN);
        Sprite sprite2 = new Sprite(sim, new SynchronizedBuffer(), 50, 50, 1, 0, Color.PINK);
        Sprite sprite3 = new Sprite(sim, new SynchronizedBuffer(), 125, 12, 5, 4, Color.ORANGE);
        ArrayList<Sprite> sprites = new ArrayList<>();
        sprites.add(sprite);
        sprites.add(sprite2);
        sprites.add(sprite3);
        dataAccess.saveSprites(sprites);
        ArrayList<Sprite> retrievedSprites = new ArrayList<>();
        retrievedSprites.addAll(dataAccess.getPersistedSprites());
        assertTrue(sprites.size() == retrievedSprites.size());
    }

    @After
    public void runAfterClass() {
        dataAccess.terminate();
    }

//    @Test
//    public void testSaveSprite() {
//        Sprite sprite = new Sprite(sim, new SynchronizedBuffer(), 100, 100, 2, 2, Color.CYAN);
//        session.beginTransaction();
//        session.save(sprite);
//        session.getTransaction().commit();
//    }


//    @Test
//    public void validateCheckIfInside() {
//            System.out.println("Validating checkIfInside");
//            Sprite s = b.getSimulation().newTestSprite(150, 150, 0, 0);
//            assertTrue(s.checkIfInside());
//
//            s = b.getSimulation().newTestSprite(0, 0, 0, 0);
//            assertFalse(s.checkIfInside());
//    }
//
//    @Test
//    public void validateDetectingEntranceToBox() {
//            System.out.println("Validating DetectingEntranceToBox");
//            Sprite s = b.getSimulation().newTestSprite(150, 150, 0, 0);
//            assertTrue(s.detectEntrance(true, false));
//    }
//
//    @Test
//    public void validateDetectingExitOutOfBox() {
//            System.out.println("Validating DetectingEntranceToBox");
//            Sprite s = b.getSimulation().newTestSprite(150, 150, 0, 0);
//            assertTrue(s.detectExit(false, true));
//    }
}