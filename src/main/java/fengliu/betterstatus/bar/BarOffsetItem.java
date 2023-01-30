package fengliu.betterstatus.bar;

public abstract class BarOffsetItem implements IBarOffsetItem {
    private final int barOffsetX;
    private final int barOffsetY;

    public BarOffsetItem(int barOffsetX, int barOffsetY) {
        this.barOffsetX = barOffsetX;
        this.barOffsetY = barOffsetY;
    }

    @Override
    public int barOffsetX() {
        return this.barOffsetX;
    }

    @Override
    public int barOffsetY() {
        return this.barOffsetY;
    }

}
