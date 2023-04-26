package sk.stuba.fei.uim.oop;

public enum Direction {
    UPTODOWN(0, 1),
    RIGHTTOLEFT(1, 0),
    UPLEFT(-1, 1),
    DOWNLEFT(-1, -1),
    UPRIGHT(1, 1),
    DOWNRIGHT(1, -1),
    UP(0, 1),
    DOWN(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private int x;
    private int y;
    private int angle;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
        this.angle = (int) Math.toDegrees(Math.atan2(y, x));
    }

}
