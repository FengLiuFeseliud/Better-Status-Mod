package fengliu.betterstatus.bar;

public interface IBarOffsetItem {

    /**
     * 栏材质 X 轴偏移量
     * @return X 轴偏移量
     */
    int barOffsetX();

    /**
     * 栏材质 Y 轴偏移量
     * @return Y 轴偏移量
     */
    int barOffsetY();

    /**
     * 是否可以绘制该偏移量组栏材质
     * @param value 栏数值
     * @param maxValue 最大数值
     * @return true 可以绘制
     */
    boolean canDraw(float value, float maxValue);
}
