package fengliu.betterstatus.bar;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public interface IBarIcon {

    /**
     * 获取材质
     * @return 材质
     */
    Identifier getTextures();

    /**
     * 在栏尾部绘制图标
     * @param in true 尾部
     * @return 图标自身
     */
    IBarIcon inTail(boolean in);

    /**
     * 将图标绑定栏
     * @param bar 栏
     * @return 图标自身
     */
    IBarIcon setBar(IBar bar);

    /**
     * 设置图标变化数组
     * @param group 图标徧移量组数组
     * @return 图标自身
     */
    IBarIcon setIconOffsetVarietyGroup(OffsetItem[] group);

    /**
     * 绘制图标
     * @param matrices matrices
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    void drawIcon(MatrixStack matrices, int x, int y);
}
