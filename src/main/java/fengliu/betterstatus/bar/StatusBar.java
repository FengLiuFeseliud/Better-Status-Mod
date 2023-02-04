package fengliu.betterstatus.bar;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public class StatusBar implements IBar {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    protected static final TextRenderer textRenderer = client.textRenderer;
    public final Identifier textures;
    public final int texturesWidth;
    public final int texturesHeight;
    protected final OffsetItem emptyBarOffset;
    protected final OffsetItem twinkleBarOffset;
    protected final int maxWidth;
    protected final int maxHeight;
    protected final int color;
    protected OffsetItem[] barOffsetItems;
    private BarIcon icon;
    protected float value = 0;
    protected float maxValue = 0;
    protected float oldValue = 0;
    protected int oldValueShow = 0;
    protected boolean oldValueShowEnd = false;
    protected int progress = 0;

    public StatusBar(@Nullable BarIcon icon, Identifier textures, int texturesWidth, int texturesHeight, int barWidth, int barHeight, int color, @Nullable OffsetItem emptyBarOffset, @Nullable OffsetItem twinkleBarOffset){
        this.icon = icon;
        if (this.icon != null){
            this.icon = icon.setBar(this);
        }

        this.textures = textures;
        this.texturesWidth = texturesWidth;
        this.texturesHeight = texturesHeight;
        this.emptyBarOffset = emptyBarOffset;
        this.twinkleBarOffset = twinkleBarOffset;
        this.maxWidth = barWidth;
        this.maxHeight = barHeight;
        this.color = color;
    }

    @Override
    public Identifier getTextures() {
        return this.textures;
    }

    @Override
    public StatusBar setBarOffsetVarietyGroup(OffsetItem[] items) {
        this.barOffsetItems = items;
        return this;
    }

    @Override
    public String getBarValueString(float value) {
        return new DecimalFormat("##0.0").format(value);
    }

    @Override
    public int getProgress() {
        return this.progress;
    }

    @Override
    public StatusBar setProgress(float value, float maxValue) {
        this.value = value;
        this.maxValue = maxValue;
        this.progress = Math.round(this.maxWidth / maxValue * value);
        return this;
    }

    @Override
    public float getValue() {
        return this.value;
    }

    @Override
    public float getMaxValue() {
        return this.maxValue;
    }

    @Override
    public float getMaxWidth() {
        return this.maxWidth;
    }

    @Override
    public float getMaxHeight() {
        return this.maxHeight;
    }

    protected void resetOldValue(){
        if(this.value != this.oldValue && oldValueShowEnd){
            this.oldValueShowEnd = false;
            this.oldValueShow = 100;
        }else if(this.oldValueShow > 0){
            this.oldValueShow -= 1;
        }else{
            this.oldValueShowEnd = true;
            this.oldValue = this.value;
        }
    }

    /**
     * 绘制条数值变化
     * @param matrices matrices
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    protected void drawValue(MatrixStack matrices, int x, int y) {
        String darValue = this.getBarValueString(this.value);
        textRenderer.draw(matrices, darValue, x + 41 - (float) (darValue.length() / 2 * 4.5), y + 1,  this.color);

        this.resetOldValue();
        float downValue = this.oldValue - this.value;
        if (downValue == 0 || this.oldValueShowEnd){
            return;
        }

        if(downValue > 0){
            textRenderer.draw(matrices, "-" + this.getBarValueString(downValue), x + this.maxWidth - 15, y + 1, color);
        }

        if(downValue < 0){
            textRenderer.draw(matrices, "+" + this.getBarValueString(Math.abs(downValue)), x + this.maxWidth - 15, y + 1, color);
        }
    }

    /**
     * 绘制条闪烁效果
     * @param matrices matrices
     * @param player 玩家
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    protected void drawTwinkleBar(MatrixStack matrices, PlayerEntity player, int x, int y) {
        if (!this.twinkleBarOffset.canDraw(this.value, this.maxValue, player)){
            return;
        }

        DrawableHelper.drawTexture(matrices, x, y, this.twinkleBarOffset.offsetX(), this.twinkleBarOffset.offsetY(), this.maxWidth, this.maxHeight, this.texturesWidth, this.texturesHeight);
    }

    /**
     * 绘制基础条
     * @param matrices matrices
     * @param barOffset 条材质偏移量对
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    protected void drawValueBar(MatrixStack matrices, IOffsetItem barOffset, int x, int y) {
        if (this.emptyBarOffset != null){
            DrawableHelper.drawTexture(matrices, x, y, this.emptyBarOffset.offsetX(), this.emptyBarOffset.offsetY(), this.maxWidth, this.maxHeight, this.texturesWidth, this.texturesHeight);
        }
        DrawableHelper.drawTexture(matrices, x, y, barOffset.offsetX(), barOffset.offsetY(), this.progress, this.maxHeight, this.texturesWidth, this.texturesHeight);
    }

    @Override
    public void drawBar(MatrixStack matrices, int x, int y) {
        PlayerEntity player = client.player;
        if (player == null){
            return;
        }

        if (this.emptyBarOffset != null){
            if (!this.emptyBarOffset.canDraw(this.value, this.maxValue, player)){
                return;
            }
        }

        IOffsetItem lastOffset = IOffsetItem.getCanDrawItem(this.barOffsetItems, this.value, this.maxValue, player);
        if (lastOffset == null){
            return;
        }

        RenderSystem.setShaderTexture(0, this.textures);
        if (lastOffset.canDraw(this.value, this.maxValue, player)){
            this.drawValueBar(matrices, lastOffset,  x, y);
        }

        if (!this.oldValueShowEnd && this.twinkleBarOffset != null){
            this.drawTwinkleBar(matrices, player, x, y);
        }

        if (this.icon != null){
            this.icon.drawIcon(matrices, x, y);
        }

        this.drawValue(matrices, x, y);
    }
}
