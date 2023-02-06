package fengliu.betterstatus.util;

import com.google.common.collect.Ordering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collection;

public class StatusEffectHelper {
    protected static final MinecraftClient client = MinecraftClient.getInstance();

    public static void drawStatusEffectTimer(PlayerEntity player, MatrixStack matrices, int scaledWidth){
        Collection<StatusEffectInstance> collection = player.getStatusEffects();
        if (collection.isEmpty()) {
            return;
        }

        int x = scaledWidth - 21;
        int dx = scaledWidth - 21;
        for (StatusEffectInstance statusEffectInstance : Ordering.natural().reverse().sortedCopy(collection)) {
            if (!statusEffectInstance.getEffectType().isBeneficial()){
                client.textRenderer.draw(matrices,
                    TimeUtil.getSimpleTimeToString(statusEffectInstance.getDuration()),
                    dx, 41, 0xffffff
                );
                dx -= 25;
                continue;
            }

            client.textRenderer.draw(matrices,
                TimeUtil.getSimpleTimeToString(statusEffectInstance.getDuration()),
                x, 15, 0xffffff
            );
            x -= 25;
        }
    }
}
