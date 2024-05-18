import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int secondsBeforeRequest = 0, requestsNum = 0, floorsNum = 0;
        System.out.print("Enter the number of floors: ");
        floorsNum = scanner.nextInt();
        System.out.print("\nEnter seconds before new request: ");
        secondsBeforeRequest = scanner.nextInt();
        System.out.print("\nEnter requests number: ");
        requestsNum = scanner.nextInt();

        RequestController reqControl = null;
        do {
            try {
                reqControl = new RequestController(secondsBeforeRequest, requestsNum, floorsNum);
            } catch (IllegalArgumentException e) {
                if (secondsBeforeRequest < 0) {
                    System.out.print("\nThe number of seconds before new request is lesser than zero. Enter another one: ");
                    secondsBeforeRequest = scanner.nextInt();
                }
                if (requestsNum < 0) {
                    System.out.print("\nRequests num is lesser than zero. Enter another one: ");
                    requestsNum = scanner.nextInt();
                }
                if (floorsNum < 0) {
                    System.out.print("\nThe number of floors is lesser then zero. Enter another one: ");
                    floorsNum = scanner.nextInt();
                }
            }
        } while (reqControl == null);
        System.out.println();

        ElevatorController elevators = new ElevatorController(reqControl);

        (new Thread(reqControl)).start();
        (new Thread(elevators)).start();


    }
}
