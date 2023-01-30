package fengliu.betterstatus.bar;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.text.DecimalFormat;

public class StatusBar implements IBar {
    private static final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    public final Identifier textures;
    public final int texturesWidth;
    public final int texturesHeight;
    protected final int emptyBarOffsetX;
    protected final int emptyBarOffsetY;
    protected final int maxWidth;
    protected final int maxHeight;
    protected final int color;
    protected BarOffsetItem[] barOffsetItems;
    private float value = 0;
    private float maxValue = 0;
    private float oldValue = 0;
    private int oldValueShow = 0;
    private boolean oldValueShowEnd = false;
    private int progress = 0;

    public StatusBar(Identifier textures, int texturesWidth, int texturesHeight, int barWidth, int barHeight, int color, int emptyBarOffsetX, int emptyBarOffsetY){
        this.textures = textures;
        this.texturesWidth = texturesWidth;
        this.texturesHeight = texturesHeight;
        this.emptyBarOffsetX = emptyBarOffsetX;
        this.emptyBarOffsetY = emptyBarOffsetY;
        this.maxWidth = barWidth;
        this.maxHeight = barHeight;
        this.color = color;
    }

    @Override
    public Identifier getTextures() {
        return this.textures;
    }

    @Override
    public StatusBar setBarOffsetVarietyGroup(BarOffsetItem[] items) {
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
    public void drawBar(MatrixStack matrices, int x, int y) {
        RenderSystem.setShaderTexture(0, this.textures);
        DrawableHelper.drawTexture(matrices, x, y, this.emptyBarOffsetX, this.emptyBarOffsetY, this.maxWidth, this.maxHeight, this.texturesWidth, this.texturesHeight);


        IBarOffsetItem lastItem = null;
        for(IBarOffsetItem item: this.barOffsetItems){
            if (item.canDraw(this.value, this.maxValue)){
                lastItem = item;
            }
        }

        if (lastItem == null){
            return;
        }

        DrawableHelper.drawTexture(matrices, x, y, lastItem.barOffsetX(), lastItem.barOffsetY(), this.progress, this.maxHeight, this.texturesWidth, this.texturesHeight);

        String darValue = this.getBarValueString(this.value);
        textRenderer.draw(matrices, darValue, x + 41 - (float) (darValue.length() / 2 * 4.5), y + 1,  this.color);

        if(this.value != this.oldValue && oldValueShowEnd){
            this.oldValueShowEnd = false;
            this.oldValueShow = 100;
        }else if(this.oldValueShow > 0){
            this.oldValueShow -= 1;
        }else{
            this.oldValueShowEnd = true;
            this.oldValue = this.value;
        }

        float downValue = this.oldValue - this.value;
        if (downValue != 0 && !this.oldValueShowEnd){
            if(downValue > 0){
                textRenderer.draw(matrices, "-" + this.getBarValueString(downValue), x + this.maxWidth - 15, y + 1, color);
            }

            if(downValue < 0){
                textRenderer.draw(matrices, "+" + this.getBarValueString(Math.abs(downValue)), x + this.maxWidth - 15, y + 1, color);
            }
        }

    }
}
