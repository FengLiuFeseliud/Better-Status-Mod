package fengliu.betterstatus.bar;

public interface IOffsetItem {

    /**
     * 材质 X 轴偏移量
     * @return X 轴偏移量
     */
    int offsetX();

    /**
     * 材质 Y 轴偏移量
     * @return Y 轴偏移量
     */
    int offsetY();

    /**
     * 是否可以绘制该偏移量组材质
     * @param value 栏数值
     * @param maxValue 最大数值
     * @return true 可以绘制
     */
    boolean canDraw(float value, float maxValue);

    /**
     * 获取可以绘制的偏移量组, 没有为 null
     * @param offsetItems 偏移量组数组
     * @param value 栏数值
     * @param maxValue 最大数值
     * @return 可以绘制的偏移量组, 没有为 null
     */
    static IOffsetItem getCanDrawItem(IOffsetItem[] offsetItems, float value, float maxValue){
        for(IOffsetItem item: offsetItems){
            if (item.canDraw(value, maxValue)){
                return item;
            }
        }
        return null;
    }
}
