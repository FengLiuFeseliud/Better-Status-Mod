package fengliu.betterstatus.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import fengliu.betterstatus.BetterStatusClient;
import fengliu.betterstatus.bar.*;
import fengliu.betterstatus.config.Configs;
import fengliu.betterstatus.util.KnapsackManager;
import fengliu.betterstatus.util.StatusEffectHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;

@Mixin(InGameHud.class)
public abstract class MixinInGameHub {
    @Shadow @Final private static Identifier PUMPKIN_BLUR;
    @Shadow @Final private MinecraftClient client;
    @Shadow private LivingEntity getRiddenEntity(){ return null; }

    private float maxAbsorption = 0;
    private static final Identifier BARS_TEXTURE = new Identifier(
        BetterStatusClient.MOD_ID, "textures/gui/bars.png"
    );
    private static final Identifier BAR_ICONS_TEXTURE = new Identifier(
        "minecraft", "textures/gui/icons.png"
    );
    private static final int TEXTURE_Y = 240;
    private static final int TEXTURE_X = 81;
    private int scaledWidth = 0;
    private int scaledHeight = 0;

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
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9,
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
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9,
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
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9,
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

    private static final StatusBar hungerBar = new BackStatusBar(
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
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9,
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

    private static final StatusBar foodSaturationBar = new StatusBar(
        null, BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9, null, new TwinkleBarOffset(0, 18)
    ){
        @Override
        public String getBarValueString(float value) {
            return String.valueOf((int) value);
        }

        @Override
        public void drawValue(MatrixStack matrices, int x, int y) {
            String darValue = this.getBarValueString(this.value);
            textRenderer.draw(matrices, "+" + darValue, x + 50, y + 1,  this.color);

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

        @Override
        public void drawValueBar(MatrixStack matrices, IOffsetItem barOffset, int x, int y) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.5f);
            DrawableHelper.drawTexture(matrices, x, y, barOffset.offsetX(), barOffset.offsetY(), this.progress, this.maxHeight, this.texturesWidth, this.texturesHeight);
        }
    }.setBarOffsetVarietyGroup(new OffsetItem[]{
        new OffsetItem(0, 135) {
            @Override
            public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                return value != 0;
            }
        }
    });

    private static final StatusBar airBar = new BackStatusBar(
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
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9,
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
    }.setBarOffsetVarietyGroup(statusBarOffsetItemGroup(new int[]{153, 162, 171}));

    private static final StatusBar riddenHeartBar = new BackStatusBar(
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
        BARS_TEXTURE, TEXTURE_X, TEXTURE_Y, 81, 9,
        new OffsetItem(0, 0) {
            @Override
            public boolean canDraw(float value, float maxValue, PlayerEntity player) {
                return value > 0;
            }
        }, new TwinkleBarOffset(0, 18)
    ).setBarOffsetVarietyGroup(statusBarOffsetItemGroup(new int[]{27, 36, 45}));

    private int[] getScaledXY(){
        Window window = this.client.getWindow();
        this.scaledWidth = window.getScaledWidth();
        this.scaledHeight = window.getScaledHeight();
        int x = this.scaledWidth / 2 - 91;
        int y = this.scaledHeight - 39;

        return new int[]{x, y};
    }

    private void drawHungerBar(MatrixStack matrices, PlayerEntity player, int x, int y){
        HungerManager hunger = player.getHungerManager();
        hungerBar.setColor(
            Configs.GUI.HUNGER_VALUE_FONT_COLOR.getIntegerValue()
        ).setProgress(hunger.getFoodLevel(), 20).drawBar(matrices, x, y);

//        foodSaturationBar.setProgress(hunger.getSaturationLevel(), 20).drawBar(matrices, x, y);
    }

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void renderStatusBars(MatrixStack matrices, CallbackInfo info){
        PlayerEntity player = client.player;
        if (player == null || Configs.ENABLE.DRAW_CLASSIC_STATUS_BAR.getBooleanValue()){
            return;
        }

        int[] scaled = this.getScaledXY();
        int x = scaled[0], y = scaled[1];

        float absorption = player.getAbsorptionAmount();

        healthBar.setColor(
            Configs.GUI.HEALTH_VALUE_FONT_COLOR.getIntegerValue()
        ).setProgress(player.getHealth(), player.getMaxHealth()).drawBar(matrices, x, y);

        if (Configs.ENHANCE.ALWAYS_RENDER_FOOD.getBooleanValue()){
            if (riddenHeartBar.getValue() > 0){
                this.drawHungerBar(matrices, player, x + 101, y - 10);
            } else {
                this.drawHungerBar(matrices, player, x + 101, y);
            }
        }

        if(maxAbsorption == 0 || absorption >= maxAbsorption){
            maxAbsorption = absorption;
        }

        absorptionBar.setColor(
            Configs.GUI.ABSORPTION_VALUE_FONT_COLOR.getIntegerValue()
        ).setProgress(absorption, maxAbsorption).drawBar(matrices, x, y - 10);

        airBar.setColor(
            Configs.GUI.AIR_VALUE_FONT_COLOR.getIntegerValue()
        ).setProgress(player.getAir(), player.getMaxAir()).drawBar(matrices, x + 101, y - 10);

        armorBar.setColor(
            Configs.GUI.ARMOR_VALUE_FONT_COLOR.getIntegerValue()
        ).setProgress(player.getArmor(), 20);

        if(absorptionBar.getValue() == 0){
            armorBar.drawBar(matrices, x, y - 10);
        } else {
            armorBar.drawBar(matrices, x, y - 20);
        }

        info.cancel();
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void renderItemStatus(MatrixStack matrices, float tickDelta, CallbackInfo info){
        PlayerEntity player = client.player;
        if (player == null){
            return;
        }

        if (player.isCreative() || player.isSpectator()){
            return;
        }

        int[] scaled = this.getScaledXY();
        int x = scaled[0], y = scaled[1];

        KnapsackManager knapsackManager = new KnapsackManager(player);
        if (Configs.ENABLE.DRAW_ITEMS_STATUS.getBooleanValue()){
            knapsackManager.drawItemsStatus(matrices, x + 195, y + 20);
        }

        if (Configs.ENABLE.DRAW_HAND_ITEM_ENCHANTMENTS.getBooleanValue()){
            if (Configs.ENABLE.DRAW_MAIN_HAND_ITEM_ENCHANTMENTS.getBooleanValue()){
                knapsackManager.drawItemEnchantments(player.getMainHandStack(), matrices, x + 285, y + 23);
            }

            if (Configs.ENABLE.DRAW_OFFSET_HAND_ITEM_ENCHANTMENTS.getBooleanValue()){
                knapsackManager.drawItemEnchantments(player.getOffHandStack(), matrices, x - 145, y + 23);
            }
        }

        if (Configs.ENABLE.DRAW_ARMORS_STATUS.getBooleanValue()){
            knapsackManager.drawArmorStackStatus(matrices, x - 50, y + 20);
        }

        if (Configs.ENABLE.DRAW_ITEMS_DANGER_STATUS_INFO.getBooleanValue()){
            knapsackManager.drawItemDangerStatusInfo(matrices,this.scaledWidth / 2, this.scaledHeight / 2 + 8);
        }

        if (Configs.HOTKEY.LOOK_KNAPSACK_STATUS.getKeybind().isPressed()){
            knapsackManager.drawKnapsack(new MatrixStack(), x - 5, y - 45);
        }



    }

    @Inject(method = "renderMountHealth", at = @At("HEAD"), cancellable = true)
    private void renderMountHealth(MatrixStack matrices, CallbackInfo info){
        if (Configs.ENABLE.DRAW_CLASSIC_STATUS_BAR.getBooleanValue()){
            return;
        }

        LivingEntity ridden = this.getRiddenEntity();
        if (ridden == null){
            riddenHeartBar.setProgress(0, 0);
            return;
        }
        int[] scaled = this.getScaledXY();
        int x = scaled[0], y = scaled[1];

        riddenHeartBar.setProgress(ridden.getHealth(), ridden.getMaxHealth()).drawBar(matrices, x + 101, y);

        info.cancel();
    }

    @ModifyVariable(method = "renderMountHealth", at = @At(value = "STORE", ordinal = 0), ordinal = 2)
    private int mountHealthUp(int y) {
        assert client.interactionManager != null;
        if (client.interactionManager.hasStatusBars() && Configs.ENHANCE.ALWAYS_RENDER_FOOD.getBooleanValue()) {
            y -= 10;
        }

        return y;
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;getHeartCount(Lnet/minecraft/entity/LivingEntity;)I"))
    private int alwaysRenderFood(InGameHud inGameHud, LivingEntity entity) {
        if (Configs.ENHANCE.ALWAYS_RENDER_FOOD.getBooleanValue()){
            return 0;
        }

        return 1;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getJumpingMount()Lnet/minecraft/entity/JumpingMount;"))
    private JumpingMount switchBar(ClientPlayerEntity player) {
        JumpingMount jumpingMount = player.getJumpingMount();
        assert client.interactionManager != null;
        if (!client.interactionManager.hasExperienceBar()
            || client.options.jumpKey.isPressed() || player.getMountJumpStrength() > 0
            || !Configs.ENHANCE.MOUNT_JUMPING_SWITCH_JUMP_BAR.getBooleanValue()){

            return jumpingMount;
        }

        return null;
    }

    @Inject(method = "renderStatusEffectOverlay", at = @At("RETURN"))
    protected void renderStatusEffectOverlay(MatrixStack matrices, CallbackInfo info) {
        PlayerEntity player = client.player;
        if (player == null){
            return;
        }

        if (Configs.ENABLE.DRAW_STATUS_EFFECT_TIMER.getBooleanValue()){
            StatusEffectHelper.drawStatusEffectTimer(player, matrices, this.scaledWidth);
        }
    }

    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    private void renderOverlay(Identifier texture, float opacity, CallbackInfo info) {
        if (!texture.equals(PUMPKIN_BLUR) || !Configs.ENHANCE.DRAW_PUMPKIN_BLUR.getBooleanValue()){
            return;
        }

        info.cancel();
    }

}
