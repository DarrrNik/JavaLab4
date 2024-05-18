public class ElevatorController implements Runnable {
    private Elevator elev1, elev2;
    private RequestController requests;

    ElevatorController(RequestController reqs) {
        elev1 = new Elevator(1);
        elev2 = new Elevator(2);
        requests = reqs;
    }

    public void run() {
        (new Thread(elev1)).start();
        (new Thread(elev2)).start();

        Request req = null;
        while (requests.isRunning() || requests.isThereRequests() || req != null) {
             req = (req == null) ? (requests.getRequest()) : (req);
             if (req == null) {
                 continue;
             }

            if (elev1.getDirection() == Direction.WAITING) {
                synchronized (elev1) {
                    System.out.printf("Elevator 1 has accessed request from %d to %d!\n", req.getFloorFrom(), req.getFloorTo());
                    elev1.setWay(req);
                    elev1.notify();
                }
                req = null;
                continue;
            }
            if (elev2.getDirection() == Direction.WAITING) {
                synchronized (elev2) {
                    System.out.printf("Elevator 2 has accessed request from %d to %d!\n", req.getFloorFrom(), req.getFloorTo());
                    elev2.setWay(req);
                    elev2.notify();
                }
                req = null;
                continue;
            }

            if (req.getFloorFrom() < req.getFloorTo()) {
                synchronized (elev1) {
                    if (elev1.getDirection() == Direction.UP && elev1.getFloor() < req.getFloorFrom()) {
                        System.out.printf("Elevator 1 has accessed request from %d to %d!\n", req.getFloorFrom(), req.getFloorTo());
                        elev1.setWay(req);
                        req = null;
                        continue;
                    }
                }
                synchronized (elev2) {
                    if (elev2.getDirection() == Direction.UP && elev2.getFloor() < req.getFloorFrom()) {
                        System.out.printf("Elevator 2 has accessed request from %d to %d!\n", req.getFloorFrom(), req.getFloorTo());
                        elev2.setWay(req);
                        req = null;
                        continue;
                    }
                }
            }
            else if (req.getFloorFrom() > req.getFloorTo()) {
                synchronized (elev1) {
                    if (elev1.getDirection() == Direction.DOWN && elev1.getFloor() > req.getFloorFrom()) {
                        elev1.setWay(req);
                        System.out.printf("Elevator 1 has accessed request from %d to %d!\n", req.getFloorFrom(), req.getFloorTo());
                        req = null;
                        continue;
                    }
                }
                synchronized (elev2) {
                    if (elev2.getDirection() == Direction.DOWN && elev2.getFloor() > req.getFloorFrom()) {
                        elev2.setWay(req);
                        System.out.printf("Elevator 2 has accessed request from %d to %d!\n", req.getFloorFrom(), req.getFloorTo());
                        req = null;
                        continue;
                    }
                }
            }
            else {
                req = null;
            }
        }


        elev1.setInactive();
        synchronized (elev1) {
            elev1.notify();
        }

        elev2.setInactive();
        synchronized (elev2) {
            elev2.notify();
        }

        System.out.println("Program is ended!");
    }
}
