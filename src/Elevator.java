import java.util.Comparator;
import java.util.PriorityQueue;

import static java.lang.Thread.sleep;

public class Elevator implements Runnable{
    private int floor;
    private PriorityQueue<Integer> to;
    private int from;
    private boolean isActive;
    private Direction direction;
    private int elevNum;

    Elevator(int n) {
        floor = 1;
        elevNum = n;
        to = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (Math.abs(floor - o1) < Math.abs(floor - o2)) {
                    return o2;
                }
                return o1;
            }
        });
        from = 0;
        isActive = true;
        direction = Direction.WAITING;
    }

    public void run() {
        System.out.printf("Elevator %d has started working!\n", elevNum);
        while (isActive || direction != Direction.WAITING) {
            switch (direction) {
                case UP -> {
                    ++floor;
                }
                case DOWN -> {
                    --floor;
                }
                case WAITING -> {
                    try {
                        synchronized (this) {
                            wait();
                        }
                    }
                    catch (InterruptedException e) {}
                }
            }
            if (from == 0 && !to.isEmpty() && floor == to.peek()) {
                to.poll();
                if (to.isEmpty()) {
                    direction = Direction.WAITING;
                }
            }
            if (floor == from || from == 0) {
                if (!to.isEmpty() && floor < to.peek()) {
                    direction = Direction.UP;
                }
                else if (!to.isEmpty() && floor > to.peek()) {
                    direction = Direction.DOWN;
                }
                from = 0;
            }
            System.out.printf("Elevator %d is on %d-th floor!\n", elevNum, floor);
            try {
                sleep(500);
            }
            catch (InterruptedException e) {}
        }
        System.out.printf("Elevator %d has ended working!\n", elevNum);
    }
    public void setInactive() {
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public synchronized void setWay(Request req) {
        to.add(req.getFloorTo());
        from = req.getFloorFrom();
        if (floor < from) {
            direction = Direction.UP;

        }
        else if (floor > from) {
            direction = Direction.DOWN;
        }
    }


    public void setDirection(Direction dir) {
        direction = dir;
    }
    public Direction getDirection() {
        return direction;
    }

    public synchronized int getFloor() {
        return floor;
    }
}
