import java.time.LocalTime;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.lang.Thread.sleep;

public class RequestController implements Runnable {
    private final int timeToSleep;
    private final int requestsNum;
    private final int floorsNum;
    private boolean isRunning;
    private Queue<Request> requests;

    RequestController(int seconds, int reqNum, int flNum) throws IllegalArgumentException {
        if (seconds < 0 || reqNum < 0 || flNum < 0) {
            throw new IllegalArgumentException();
        }
        timeToSleep = seconds;
        requestsNum = reqNum;
        floorsNum = flNum;
        requests = new ConcurrentLinkedQueue<>();
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
    public boolean isThereRequests() {
        return !(requests.isEmpty());
    }
    public Request getRequest() {
        return requests.poll();
    }

    public void run() {
        isRunning = true;
        for (int i = 0; i < requestsNum; ++i) {
            Random rand = new Random(LocalTime.now().getSecond());
            int from = 0, to = 0;
            while (from == 0 || to == 0 || from == to) {
                from = rand.nextInt(floorsNum) + 1;
                to = rand.nextInt(floorsNum) + 1;
            }
            System.out.printf("%d New request is created from %d to %d!\n", i + 1, from, to);
            requests.add(new Request(from, to));
            try {
                sleep(timeToSleep * 1000);
            }
            catch (IllegalArgumentException e) {
            }
            catch (InterruptedException e) {
            }
        }
        isRunning = false;
        System.out.println("All requests are created!");
    }
    private void createRequest() {
    }
}
