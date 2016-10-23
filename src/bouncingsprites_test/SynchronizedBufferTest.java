package bouncingsprites_test;

import bouncingsprites.BouncingSprites;
import org.junit.BeforeClass;

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