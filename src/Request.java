public class Request {
    private final int floorTo;
    private final int floorFrom;

    Request(int from, int to) {
        floorTo = to;
        floorFrom = from;
    }

    public int getFloorTo() {
        return floorTo;
    }

    public int getFloorFrom() {
        return floorFrom;
    }
}
