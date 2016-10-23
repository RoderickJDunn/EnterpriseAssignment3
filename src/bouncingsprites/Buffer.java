package bouncingsprites;

/**
 *  This interface was adapted from "Fig. 23.16: Buffer.java" of Dietel.
 */
public interface Buffer {
    // place int value into Buffer
    public void blockingAdd() throws InterruptedException;

    // obtain int value from Buffer
    public void blockingRemove() throws InterruptedException;
}
