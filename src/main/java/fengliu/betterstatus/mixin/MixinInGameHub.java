package fengliu.betterstatus.mixin;

import fengliu.betterstatus.BetterStatusClient;
import fengliu.betterstatus.bar.BarOffsetItem;
import fengliu.betterstatus.bar.StatusBar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.text.DecimalFormat;

@Mixin(InGameHud.class)
public class MixinInGameHub {
    @Shadow @Final private MinecraftClient client;
    private float maxAbsorption = 0;
    private static final Identifier BARS_TEXTURE = new Identifier(
        BetterStatusClient.MOD_ID, "textures/gui/bars.png"
    );
    private static final int TEXTURE_Y = 180;
    private static final int TEXTURE_X = 81;

    private static final StatusBar healthBar = new StatusBar(
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9, 0x000000, 0, 0
    ).setBarOffsetVarietyGroup(new BarOffsetItem[]{
        new BarOffsetItem(0, 27) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value <= maxValue;
            }
        },
        new BarOffsetItem(0, 36) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value < maxValue * 0.66;
            }
        },
        new BarOffsetItem(0, 45) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value < maxValue * 0.33;
            }
        }
    });

    private static final StatusBar absorptionBar = new StatusBar(
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9, 0x000000, 0, 0
    ).setBarOffsetVarietyGroup(new BarOffsetItem[]{
        new BarOffsetItem(0, 54) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value <= maxValue;
            }
        },
        new BarOffsetItem(0, 63) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value < maxValue * 0.66;
            }
        },
        new BarOffsetItem(0, 72) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value < maxValue * 0.33;
            }
        }
    });

    private static final StatusBar armorBar = new StatusBar(
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9, 0x000000, 0, 0
    ).setBarOffsetVarietyGroup(new BarOffsetItem[]{
        new BarOffsetItem(0, 81) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value <= maxValue;
            }
        },
        new BarOffsetItem(0, 90) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value < maxValue * 0.66;
            }
        },
        new BarOffsetItem(0, 99) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value < maxValue * 0.33;
            }
        }
    });

    private static final StatusBar hungerBar = new StatusBar(
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9, 0x000000, 0, 0
    ).setBarOffsetVarietyGroup(new BarOffsetItem[]{
        new BarOffsetItem(0, 108) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value <= maxValue;
            }
        },
        new BarOffsetItem(0, 117) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value < maxValue * 0.66;
            }
        },
        new BarOffsetItem(0, 126) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value < maxValue * 0.33;
            }
        }
    });

    private static final StatusBar airBar = new StatusBar(
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9, 0x000000, 0, 0
    ){
        @Override
        public String getBarValueString(float value) {
            return new DecimalFormat("##0.0%").format((value + 1.0) / (this.getMaxValue() * 1.0));
        }

    }.setBarOffsetVarietyGroup(new BarOffsetItem[]{
        new BarOffsetItem(0, 135) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value <= maxValue;
            }
        },
        new BarOffsetItem(0, 144) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value < maxValue * 0.66;
            }
        },
        new BarOffsetItem(0, 153) {
            @Override
            public boolean canDraw(float value, float maxValue) {
                return value < maxValue * 0.33;
            }
        }
    });

    @Overwrite
    private void renderStatusBars(MatrixStack matrices){
        PlayerEntity player = client.player;
        if (player == null){
            return;
        }

        Window window = this.client.getWindow();
        int scaledWidth = window.getScaledWidth();
        int scaledHeight = window.getScaledHeight();
        int x = scaledWidth / 2 - 91;
        int y = scaledHeight - 39;

        int armor = player.getArmor();
        float absorption = player.getAbsorptionAmount();
        int air = player.getAir();

        healthBar.setProgress(player.getHealth(), player.getMaxHealth()).drawBar(matrices, x, y);
        hungerBar.setProgress(player.getHungerManager().getFoodLevel(), 20).drawBar(matrices, x + 101, y);

        if(maxAbsorption == 0 || absorption >= maxAbsorption){
            maxAbsorption = absorption;
        }

        if (absorption > 0) {
            absorptionBar.setProgress(absorption, maxAbsorption).drawBar(matrices, x, y - 10);
        }

        if (player.getMaxAir() != air){
            airBar.setProgress(air, player.getMaxAir()).drawBar(matrices, x + 101, y - 10);
        }

        armorBar.setProgress(armor, 20);
        if(armor > 0){
            if(absorption == 0){
                armorBar.drawBar(matrices, x, y - 10);
            } else {
                armorBar.drawBar(matrices, x, y - 20);
            }
        }

    }

}
