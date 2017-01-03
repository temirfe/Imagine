package kg.prosoft.imagine;

import java.util.Scanner;

/**
 * Created by ProsoftPC on 10/13/2016.
 */
public class Irrelated {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int lightX = in.nextInt(); // the X position of the light of power
        int lightY = in.nextInt(); // the Y position of the light of power
        int initialTX = in.nextInt(); // Thor's starting X position
        int initialTY = in.nextInt(); // Thor's starting Y position

        int currentX = initialTX;
        int currentY = initialTY;

        // game loop
        while (true) {
            int remainingTurns = in.nextInt(); // The remaining amount of turns Thor can move. Do not remove this line.

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            if(currentX>lightX && currentY>lightY){
                System.out.println("NW");
                currentX--;
                currentY--;
            }

            else if(currentX<lightX && currentY<lightY){
                System.out.println("SE");
                currentX++;
                currentY++;
            }
            else if(currentX<lightX && currentY>lightY){
                System.out.println("NE");
                currentX++;
                currentY--;
            }
            else if(currentX>lightX && currentY<lightY){
                System.out.println("SW");
                currentX--;
                currentY++;
            }
            else if(currentX<lightX){
                System.out.println("E");
                currentX++;
            }
            else if(currentX>lightX){
                System.out.println("W");
                currentX--;
            }
            else if(currentY<lightY){
                System.out.println("S");
                currentY++;
            }
            else if(currentY>lightY){
                System.out.println("N");
                currentY--;
            }

        }
    }
}
