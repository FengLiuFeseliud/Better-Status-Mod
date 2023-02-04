package fengliu.betterstatus.bar;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public interface IBar {
    /**
     * 获取栏数值
     * @return 数值
     */
    float getValue();

    /**
     * 获取栏最大数值
     * @return 最大数值
     */
    float getMaxValue();

    float getMaxWidth();

    float getMaxHeight();

    /**
     * 获取栏材质
     * @return 材质
     */
    Identifier getTextures();

    /**
     * 设置栏变化数组
     * @param group 栏徧移量组数组
     * @return 栏自身
     */
    IBar setBarOffsetVarietyGroup(OffsetItem[] group);

    /**
     * 获取栏数值显示字符串
     * @param value 数值
     * @return 字符串
     */
    String getBarValueString(float value);

    /**
     * 获取栏显示进度
     * @return 栏显示进度
     */
    int getProgress();

    /**
     * 设置栏显示进度
     * @param value 新数值
     * @param maxValue 新最大数值
     * @return 栏自身
     */
    IBar setProgress(float value, float maxValue);

    /**
     * 绘制栏
     * @param matrices matrices
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    void drawBar(MatrixStack matrices, int x, int y);
}
