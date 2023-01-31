package fengliu.betterstatus.bar;


public abstract class OffsetItem implements IOffsetItem {
    private final int offsetX;
    private final int offsetY;

    public OffsetItem(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public int offsetX() {
        return this.offsetX;
    }

    @Override
    public int offsetY() {
        return this.offsetY;
    }

}
