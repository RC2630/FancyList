import java.util.Scanner;

import static java.lang.System.out;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Scanner scStr = new Scanner(System.in);

    private static void run() {
        out.print("\nEnter numbers into Fancy List (-2630 to stop):\n\n");
        FancyList<Double> fancyList = new FancyList<>();
        double temp;
        while (true) {
            temp = scanner.nextDouble();
            if (temp != -2630) {
                fancyList.add(temp);
            } else {
                break;
            }
        }
        int currSelection;
        boolean loop = true;
        while (loop) {
            out.print("\n(1) Enter iterator type (normal, reverse, ascending, descending, odd before even)"
                    + "\n(2) Display list in order of current iterator"
                    + "\n(0) Exit program\n\nEnter choice: ");
            currSelection = scanner.nextInt();
            switch (currSelection) {
                case 1:
                    out.print("\nEnter iterator type: ");
                    String type = scStr.nextLine();
                    fancyList.setMode(type);
                    out.print("\nThe fancy list's iterator type is now set to: " + type + "\n");
                    break;
                case 2:
                    try {
                        if (fancyList.isEmpty()) {
                            out.print("\nThe current fancy list is empty!\n");
                            break;
                        }
                        out.print("\nHere is the fancy list in the order of the current iterator:");
                        for (double d : fancyList) {
                            out.print(" " + d);
                        }
                        out.print("\n");
                        break;
                    } catch (CustomExceptions.NoSuchIteratorException e) {
                        out.print(" NO VALID ITERATOR SELECTED\n");
                        break;
                    }
                case 0:
                    loop = false;
                    out.print("\nBye!\n");
                    break;
                default:
                    out.print("\nChoice is invalid.\n");
            }
        }
    }

    public static void main(String[] args) {
        try {
            Main.run();
        } catch (Throwable t) {
            out.print("\nAn error occurred.\n");
        }
    }

}