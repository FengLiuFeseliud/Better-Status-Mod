package fengliu.betterstatus.bar;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class BackStatusBar extends StatusBar {

    public BackStatusBar(@Nullable BarIcon icon, Identifier textures, int texturesWidth, int texturesHeight, int barWidth, int barHeight, int color, OffsetItem emptyBarOffset, @Nullable OffsetItem twinkleBarOffset) {
        super(icon, textures, texturesWidth, texturesHeight, barWidth, barHeight, color, emptyBarOffset, twinkleBarOffset);
    }

    @Override
    public void drawValueBar(MatrixStack matrices, IOffsetItem barOffset, int x, int y) {
        DrawableHelper.drawTexture(matrices, x, y, barOffset.offsetX(), barOffset.offsetY(), this.maxWidth, this.maxHeight, this.texturesWidth, this.texturesHeight);
        DrawableHelper.drawTexture(matrices, x, y, this.emptyBarOffset.offsetX(), this.emptyBarOffset.offsetY(), this.maxWidth - this.progress, this.maxHeight, this.texturesWidth, this.texturesHeight);
    }
}
