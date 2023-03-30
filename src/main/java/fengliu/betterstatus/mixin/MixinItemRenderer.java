package fengliu.betterstatus.mixin;

import fengliu.betterstatus.config.Configs;
import fengliu.betterstatus.util.BoxHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Inject(method = "renderInGuiWithOverrides(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At("RETURN"))
    public void renderInGuiWithOverrides(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo info) {
        if (!Configs.ENABLE.DRAW_BOX_MAIN_ITEM.getBooleanValue()){
            return;
        }

        BoxHelper boxHelper = new BoxHelper(stack);
        if (!boxHelper.isAllItemSame()){
            return;
        }

        MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(matrices, new ItemStack(boxHelper.getSameItem()), x - 5, y - 5);
    }
}
