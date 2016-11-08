package bouncingsprites;

//

import utils.LogIt;

/**
 * Manage synchronized access to the number of occupants currently within the box, and
 * limit Sprite entry and exit based on certain rules.
 * This class was adapted from "Fig. 23.16: SynchronizedBuffer.java" of Dietel.
 */
public class SynchronizedBuffer implements Buffer {

    /**
     * The number of sprites currently residing within the box. Value is shared by
     * all sprites, inluding both consumers and producers
     */
    private int occupants = 0; // shared by producer and consumer threads

    /**
     * Debugging-related variables to track the number of sprites currently frozen
     * either waiting to enter the box, or waiting to exit
     */
    private int waitingToEnter = 0;
    private int waitingToExit = 0;


    /**
     * Increment the number of occupants of the box. Called by a Sprite that is
     * attempting to enter the box. Wait for space to become available if necessary
     *
     * @throws InterruptedException
     */
    public synchronized void blockingAdd()
            throws InterruptedException {
        // If there are already 2+ occupants inside the box, this Sprite/thread waits
        while (occupants > 1) {
            waitingToEnter++;
            LogIt.verbose("Line to enter: %d", waitingToEnter);
            wait();
            waitingToEnter--;
        }
        occupants++; // Increment the # of occupants inside the box
        notifyAll(); // tell waiting thread(s) to enter runnable state
    }


    /**
     * Decrement the number of occupants inside the box. Called by a Sprite that is
     * attempting to leave the box. Wait for there to be 2 Sprites total inside the box
     * (including this one), before permitting a Sprite to leave.
     *
     * @throws InterruptedException
     */
    public synchronized void blockingRemove() throws InterruptedException {
        // If this is the only Sprite in the box, wait. Allow exit if another Sprite enters.
        while (occupants <= 1) {
            waitingToExit++;
            LogIt.verbose("Line to exit: ", waitingToExit);
            wait();
            waitingToExit--;
        }
        occupants--; // Decrement the # of occupants in the box

        notifyAll(); // tell waiting thread(s) to enter runnable state

    }

    // display current operation and buffer state; for demo only
//    private synchronized void displayState(String operation) {
//        System.out.printf("%-40s%d\t\t", operation, occupants);
//    }
}



/**************************************************************************
 * (C) Copyright 1992-2015 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/