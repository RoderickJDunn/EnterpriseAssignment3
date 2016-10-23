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
public class SpriteTest {
		static BouncingSprites b;

		@BeforeClass
		public static void setUpBeforeClass() throws Exception {
				b = new BouncingSprites();
				//new Thread(b).start();
                Thread.sleep(1000);
        }

		@Test
		public void validateCheckIfInside() {
                System.out.println("Validating checkIfInside");
                Sprite s = b.getPanel().newTestSprite(150, 150, 0, 0);
                assertTrue(s.checkIfInside());

                s = b.getPanel().newTestSprite(0, 0, 0, 0);
                assertFalse(s.checkIfInside());
		}

        @Test
        public void validateDetectingEntranceToBox() {
                System.out.println("Validating DetectingEntranceToBox");
                Sprite s = b.getPanel().newTestSprite(150, 150, 0, 0);
                assertTrue(s.detectEntrance(true, false));
        }

        @Test
        public void validateDetectingExitOutOfBox() {
                System.out.println("Validating DetectingEntranceToBox");
                Sprite s = b.getPanel().newTestSprite(150, 150, 0, 0);
                assertTrue(s.detectExit(false, true));
        }
}