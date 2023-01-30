package fengliu.betterstatus.bar;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BarIcon implements IBarIcon {
    private IBar bar = null;
    private final Identifier textures;
    private final int texturesWidth;
    private final int texturesHeight;
    private final int iconWidth;
    private final int iconHeight;
    protected OffsetItem[] iconOffsetItems;
    private boolean inTail = false;


    public BarIcon(Identifier textures, int texturesWidth, int texturesHeight, int iconWidth, int iconHeight){
        this.textures = textures;
        this.texturesWidth = texturesWidth;
        this.texturesHeight = texturesHeight;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
    }

    @Override
    public Identifier getTextures() {
        return this.textures;
    }

    @Override
    public BarIcon inTail(boolean in) {
        this.inTail = in;
        return this;
    }

    @Override
    public BarIcon setBar(IBar bar) {
        this.bar = bar;
        return this;
    }

    @Override
    public BarIcon setIconOffsetVarietyGroup(OffsetItem[] group) {
        this.iconOffsetItems = group;
        return this;
    }

    @Override
    public void drawIcon(MatrixStack matrices, int x, int y) {
        if(this.bar == null || this.textures == null){
            return;
        }

        IOffsetItem lastItem = IOffsetItem.getCanDrawItem(this.iconOffsetItems, this.bar.getValue(), this.bar.getMaxValue());
        if (lastItem == null){
            return;
        }

        RenderSystem.setShaderTexture(0, this.textures);
        if(this.inTail){
            DrawableHelper.drawTexture(matrices, (int) (x + this.bar.getMaxWidth()), y, lastItem.offsetX(), lastItem.offsetY(), this.iconWidth, this.iconHeight, this.texturesWidth, this.texturesHeight);
        } else {
            DrawableHelper.drawTexture(matrices, x - 10, y, lastItem.offsetX(), lastItem.offsetY(), this.iconWidth, this.iconHeight, this.texturesWidth, this.texturesHeight);
        }
    }
}
