package bouncingsprites_test;

import bouncingsprites.BouncingSprites;
import bouncingsprites.Sprite;
import bouncingsprites.SpritePanel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

/**
 * Created by Roderick on 2016-09-25.
 */
public class SynchronizedBufferTest {
    static BouncingSprites b;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        b = new BouncingSprites();
        //new Thread(b).start();
        Thread.sleep(1000);
    }



}