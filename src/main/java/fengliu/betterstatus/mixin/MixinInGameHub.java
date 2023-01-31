package fengliu.betterstatus.mixin;

import fengliu.betterstatus.BetterStatusClient;
import fengliu.betterstatus.bar.BarIcon;
import fengliu.betterstatus.bar.OffsetItem;
import fengliu.betterstatus.bar.StatusBar;
import fengliu.betterstatus.bar.TwinkleBarOffset;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
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
    @Shadow private LivingEntity getRiddenEntity(){ return null; }

    private float maxAbsorption = 0;
    private static final Identifier BARS_TEXTURE = new Identifier(
        BetterStatusClient.MOD_ID, "textures/gui/bars.png"
    );
    private static final Identifier BAR_ICONS_TEXTURE = new Identifier(
        "minecraft", "textures/gui/icons.png"
    );
    private static final int TEXTURE_Y = 180;
    private static final int TEXTURE_X = 81;

    private static OffsetItem[] statusBarOffsetItemGroup(int[] offsetYs){
        return new OffsetItem[]{
            new OffsetItem(0, offsetYs[0]) {
                @Override
                public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                    return value >= maxValue * 0.66;
                }
            },
            new OffsetItem(0, offsetYs[1]) {
                @Override
                public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                    return value < maxValue * 0.66 && value >= maxValue * 0.33;
                }
            },
            new OffsetItem(0, offsetYs[2]) {
                @Override
                public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                    return value < maxValue * 0.33;
                }
            }
        };
    }

    private static final StatusBar healthBar = new StatusBar(
        new BarIcon(
            BAR_ICONS_TEXTURE, 256, 256, 9, 9
        ).setIconOffsetVarietyGroup(new OffsetItem[]{
            new OffsetItem(88, 0) {
                @Override
                public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                    return player.hasStatusEffect(StatusEffects.POISON);
                }
            },
            new OffsetItem(124, 0) {
                @Override
                public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                    return player.hasStatusEffect(StatusEffects.WITHER);
                }
            },
            new OffsetItem(178, 0) {
                @Override
                public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                    return player.isFrozen();
                }
            },
            new OffsetItem(52, 0) {
                @Override
                public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                    return true;
                }
            },
        }),
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9, 0x000000,
        new OffsetItem(0, 0) {
            @Override
            public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                return true;
            }
        }, new TwinkleBarOffset(0, 18)
    ).setBarOffsetVarietyGroup(statusBarOffsetItemGroup(new int[]{27, 36, 45}));

    private static final StatusBar absorptionBar = new StatusBar(
        new BarIcon(
            BAR_ICONS_TEXTURE, 256, 256, 9, 9
        ).setIconOffsetVarietyGroup(new OffsetItem[]{
            new OffsetItem(160, 0) {
                @Override
                public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                    return true;
                }
            },
        }),
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9, 0x000000,
        new OffsetItem(0, 0) {
            @Override
            public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                return value > 0;
            }
        }, new TwinkleBarOffset(0, 18)
    ).setBarOffsetVarietyGroup(statusBarOffsetItemGroup(new int[]{54, 63, 72}));

    private static final StatusBar armorBar = new StatusBar(
        new BarIcon(
            BAR_ICONS_TEXTURE, 256, 256, 9, 9
        ).setIconOffsetVarietyGroup(new OffsetItem[]{
            new OffsetItem(43, 9) {
                @Override
                public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                    return true;
                }
            },
        }),
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9, 0x000000,
        new OffsetItem(0, 0) {
            @Override
            public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                return value > 0;
            }
        }, new TwinkleBarOffset(0, 18)
    ){
        @Override
        public String getBarValueString(float value) {
            return String.valueOf((int) value);
        }
    }.setBarOffsetVarietyGroup(statusBarOffsetItemGroup(new int[]{81, 90, 99}));

    private static final StatusBar hungerBar = new StatusBar(
        new BarIcon(
            BAR_ICONS_TEXTURE, 256, 256, 9, 9
        ).inTail(true).setIconOffsetVarietyGroup(new OffsetItem[]{
            new OffsetItem(88, 27) {
                @Override
                public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                    return player.hasStatusEffect(StatusEffects.HUNGER);
                }
            },
            new OffsetItem(52, 27) {
                @Override
                public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                    return true;
                }
            },
        }),
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9, 0x000000,
        new OffsetItem(0, 0) {
            @Override
            public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                return true;
            }
        }, new TwinkleBarOffset(0, 18)
    ){
        @Override
        public String getBarValueString(float value) {
            return String.valueOf((int) value);
        }
    }.setBarOffsetVarietyGroup(statusBarOffsetItemGroup(new int[]{108, 117, 126}));

    private static final StatusBar airBar = new StatusBar(
        new BarIcon(
            BAR_ICONS_TEXTURE, 256, 256, 9, 9
        ).inTail(true).setIconOffsetVarietyGroup(new OffsetItem[]{
            new OffsetItem(16, 18) {
                @Override
                public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                    return true;
                }
            },
        }),
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9, 0x000000,
        new OffsetItem(0, 0) {
            @Override
            public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                return value != maxValue;
            }
        }, new TwinkleBarOffset(0, 18)
    ){
        @Override
        public String getBarValueString(float value) {
            return new DecimalFormat("##0.0%").format((value + 1.0) / (this.getMaxValue() * 1.0));
        }
    }.setBarOffsetVarietyGroup(statusBarOffsetItemGroup(new int[]{135, 144, 153}));

    private static final StatusBar riddenHeartBar = new StatusBar(
        new BarIcon(
            BAR_ICONS_TEXTURE, 256, 256, 9, 9
        ).inTail(true).setIconOffsetVarietyGroup(new OffsetItem[]{
            new OffsetItem(88, 9) {
                @Override
                public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                    return true;
                }
            }
        }),
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9, 0x000000,
        new OffsetItem(0, 0) {
            @Override
            public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                return value > 0;
            }
        }, new TwinkleBarOffset(0, 18)
    ).setBarOffsetVarietyGroup(statusBarOffsetItemGroup(new int[]{27, 36, 45}));

    private int[] getScaledXY(){
        Window window = this.client.getWindow();
        int scaledWidth = window.getScaledWidth();
        int scaledHeight = window.getScaledHeight();
        int x = scaledWidth / 2 - 91;
        int y = scaledHeight - 39;

        return new int[]{x, y};
    }

    @Overwrite
    private void renderStatusBars(MatrixStack matrices){
        PlayerEntity player = client.player;
        if (player == null){
            return;
        }

        int[] scaled = this.getScaledXY();
        int x = scaled[0], y = scaled[1];

        float absorption = player.getAbsorptionAmount();
        healthBar.setProgress(player.getHealth(), player.getMaxHealth()).drawBar(matrices, x, y);

        if (riddenHeartBar.getValue() > 0){
            hungerBar.setProgress(player.getHungerManager().getFoodLevel(), 20).drawBar(matrices, x + 101, y - 10);
        } else {
            hungerBar.setProgress(player.getHungerManager().getFoodLevel(), 20).drawBar(matrices, x + 101, y);
        }

        if(maxAbsorption == 0 || absorption >= maxAbsorption){
            maxAbsorption = absorption;
        }

        absorptionBar.setProgress(absorption, maxAbsorption).drawBar(matrices, x, y - 10);
        airBar.setProgress(player.getAir(), player.getMaxAir()).drawBar(matrices, x + 101, y - 10);

        armorBar.setProgress(player.getArmor(), 20);
        if(absorptionBar.getValue() == 0){
            armorBar.drawBar(matrices, x, y - 10);
        } else {
            armorBar.drawBar(matrices, x, y - 20);
        }

    }

    @Overwrite
    private void renderMountHealth(MatrixStack matrices){
        LivingEntity ridden = this.getRiddenEntity();
        if (ridden == null){
            riddenHeartBar.setProgress(0, 0);
            return;
        }
        int[] scaled = this.getScaledXY();
        int x = scaled[0], y = scaled[1];

        riddenHeartBar.setProgress(ridden.getHealth(), ridden.getMaxHealth()).drawBar(matrices, x + 101, y);
    }

}
