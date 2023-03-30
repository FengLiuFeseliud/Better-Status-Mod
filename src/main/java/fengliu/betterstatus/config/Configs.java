package fengliu.betterstatus.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fengliu.betterstatus.BetterStatusClient;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;

import java.io.File;
import java.util.List;

public class Configs implements IConfigHandler {
    public static Configs INSTANCE = new Configs();
    private static final String CONFIG_FILE_NAME = BetterStatusClient.MOD_ID + ".json";

    public static class ALL {
        public static final ConfigBooleanHotkeyed DRAW_CLASSIC_STATUS_BAR = new ConfigBooleanHotkeyed("betterstatus.config.draw.classic.status.bar", false, "", "betterstatus.config.draw.classic.status.bar.comment", "betterstatus.config.draw.classic.status.bar.pretty");
        public static final ConfigBooleanHotkeyed DRAW_ITEMS_STATUS = new ConfigBooleanHotkeyed("betterstatus.config.draw.item.status", true, "", "betterstatus.config.draw.item.status.comment", "betterstatus.config.draw.item.status.pretty");
        public static final ConfigBooleanHotkeyed DRAW_ITEM_DAMAGE_PERCENTAGE = new ConfigBooleanHotkeyed("betterstatus.config.draw.item.damage.percentage", false, "", "betterstatus.config.draw.item.damage.percentage.comment", "betterstatus.config.draw.item.damage.percentage.pretty");
        public static final ConfigBooleanHotkeyed DRAW_HAND_ITEM_ENCHANTMENTS = new ConfigBooleanHotkeyed("betterstatus.config.draw.hand.item.enchantments", true, "", "betterstatus.config.draw.hand.item.enchantments.comment", "betterstatus.config.draw.hand.item.enchantments.pretty");
        public static final ConfigBooleanHotkeyed DRAW_MAIN_HAND_ITEM_ENCHANTMENTS = new ConfigBooleanHotkeyed("betterstatus.config.draw.main.hand.item.enchantments", true, "", "betterstatus.config.draw.main.hand.item.enchantments.comment", "betterstatus.config.draw.main.hand.item.enchantments.pretty");
        public static final ConfigBooleanHotkeyed DRAW_OFFSET_HAND_ITEM_ENCHANTMENTS = new ConfigBooleanHotkeyed("betterstatus.config.draw.offset.hand.item.enchantments", true, "", "betterstatus.config.draw.offset.hand.item.enchantments.comment", "betterstatus.config.draw.offset.hand.item.enchantments.pretty");
        public static final ConfigBooleanHotkeyed DRAW_ARMORS_STATUS = new ConfigBooleanHotkeyed("betterstatus.config.draw.armors.status", true, "", "betterstatus.config.draw.armors.status.comment", "betterstatus.config.draw.armors.status.pretty");
        public static final ConfigBooleanHotkeyed DRAW_ITEMS_DANGER_STATUS_INFO = new ConfigBooleanHotkeyed("betterstatus.config.draw.item.danger.status.info", true, "", "betterstatus.config.draw.item.danger.status.info.comment", "betterstatus.config.draw.item.danger.status.info.pretty");
        public static final ConfigBooleanHotkeyed DRAW_STATUS_EFFECT_TIMER = new ConfigBooleanHotkeyed("betterstatus.config.draw.status.effect.timer", true, "", "betterstatus.config.draw.status.effect.timer.comment", "betterstatus.config.draw.status.effect.timer.pretty");
        public static final ConfigBooleanHotkeyed DRAW_BOX_MAIN_ITEM = new ConfigBooleanHotkeyed("betterstatus.config.draw.box.main.item", false, "", "betterstatus.config.draw.box.main.item.comment", "betterstatus.config.draw.box.main.item.pretty");
        public static final ConfigColor HEALTH_VALUE_FONT_COLOR = new ConfigColor("betterstatus.config.health.value.font.color", "#00890000", "betterstatus.config.health.value.font.color.comment");
        public static final ConfigColor ABSORPTION_VALUE_FONT_COLOR = new ConfigColor("betterstatus.config.absorption.value.font.color", "#00886023", "betterstatus.config.absorption.value.font.color.comment");
        public static final ConfigColor ARMOR_VALUE_FONT_COLOR = new ConfigColor("betterstatus.config.armor.value.font.color", "#008B8B8B", "betterstatus.config.armor.value.font.color.comment");
        public static final ConfigColor HUNGER_VALUE_FONT_COLOR = new ConfigColor("betterstatus.config.hunger.value.font.color", "#00886241", "betterstatus.config.hunger.value.font.color.comment");
        public static final ConfigColor AIR_VALUE_FONT_COLOR = new ConfigColor("betterstatus.config.air.value.font.color", "#006F7D88", "betterstatus.config.air.value.font.color.comment");
        public static final ConfigString ITEM_DAMAGE_PERCENTAGE_PLACES = new ConfigString("betterstatus.config.item.damage.percentage.places", "#0%", "betterstatus.config.item.damage.percentage.places.comment");
        public static final ConfigColor ITEM_STATUS_COUNT_FONT_COLOR = new ConfigColor("betterstatus.config.item.status.count.font.color", "#00FFFFFF", "betterstatus.config.item.status.count.font.color.comment");
        public static final ConfigColor ITEM_STATUS_SAFE_FONT_COLOR = new ConfigColor("betterstatus.config.item.status.safe.font.color", "#005AFF00", "betterstatus.config.item.status.safe.font.color.comment");
        public static final ConfigColor ITEM_STATUS_SLIGHTLY_FONT_COLOR = new ConfigColor("betterstatus.config.item.status.slightly.font.color", "#00DAFF00", "betterstatus.config.item.status.slightly.font.color.comment");
        public static final ConfigColor ITEM_STATUS_ATTENTION_FONT_COLOR = new ConfigColor("betterstatus.config.item.status.attention.font.color", "#00FFC000", "betterstatus.config.item.status.attention.font.color.comment");
        public static final ConfigColor ITEM_STATUS_WARNING_FONT_COLOR = new ConfigColor("betterstatus.config.item.status.waring.font.color", "#00FF7100", "betterstatus.config.item.status.waring.font.color.comment");
        public static final ConfigColor ITEM_STATUS_DANGER_FONT_COLOR = new ConfigColor("betterstatus.config.item.status.danger.font.color", "#00FF0000", "betterstatus.config.item.status.danger.font.color.comment");
        public static final ConfigBoolean MOUNT_JUMPING_SWITCH_JUMP_BAR = new ConfigBoolean("betterstatus.config.mount.jumping.switch.bar", true, "betterstatus.config.mount.jumping.switch.bar.comment");
        public static final ConfigBooleanHotkeyed PROHIBIT_DRAW_OVERLAY = new ConfigBooleanHotkeyed("betterstatus.config.prohibit.draw.overlay", false, "", "betterstatus.config.prohibit.draw.overlay.comment", "betterstatus.config.prohibit.draw.overlay.pretty");
        public static final ConfigBooleanHotkeyed PROHIBIT_DRAW_PUMPKIN_BLUR = new ConfigBooleanHotkeyed("betterstatus.config.prohibit.draw.pumpkin.blur", false, "", "betterstatus.config.prohibit.draw.pumpkin.blur.comment", "betterstatus.config.prohibit.draw.pumpkin.blur.pretty");
        public static final ConfigBooleanHotkeyed PROHIBIT_DRAW_POWDER_SNOW_OUTLINE = new ConfigBooleanHotkeyed("betterstatus.config.prohibit.draw.powder.snow.outline", false, "", "betterstatus.config.prohibit.draw.powder.snow.outline.comment", "betterstatus.config.prohibit.draw.powder.snow.outline.pretty");
        public static final ConfigBooleanHotkeyed PROHIBIT_DRAW_SPYGLASS_SCOPE = new ConfigBooleanHotkeyed("betterstatus.config.prohibit.draw.spyglass.scope", false, "", "betterstatus.config.prohibit.draw.spyglass.scope.comment", "betterstatus.config.prohibit.draw.spyglass.scope.pretty");
        public static final ConfigBoolean ALWAYS_RENDER_FOOD = new ConfigBoolean("betterstatus.config.always.render.food", true, "betterstatus.config.always.render.food.comment");
        public static final ConfigHotkey OPEN_CONFIG_GUI = new ConfigHotkey("betterstatus.config.hotkey.open.config.gui", "LEFT_CONTROL,B", "betterstatus.config.hotkey.open.config.gui.comment");
        public static final ConfigHotkey LOOK_KNAPSACK_STATUS = new ConfigHotkey("betterstatus.config.hotkey.look.knapsack.status", "", "betterstatus.config.hotkey.look.knapsack.status.comment");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
            DRAW_CLASSIC_STATUS_BAR,
            DRAW_ITEMS_STATUS,
            DRAW_ITEM_DAMAGE_PERCENTAGE,
            DRAW_HAND_ITEM_ENCHANTMENTS,
            DRAW_MAIN_HAND_ITEM_ENCHANTMENTS,
            DRAW_OFFSET_HAND_ITEM_ENCHANTMENTS,
            DRAW_ARMORS_STATUS,
            DRAW_ITEMS_DANGER_STATUS_INFO,
            DRAW_STATUS_EFFECT_TIMER,
            HEALTH_VALUE_FONT_COLOR,
            ABSORPTION_VALUE_FONT_COLOR,
            ARMOR_VALUE_FONT_COLOR,
            HUNGER_VALUE_FONT_COLOR,
            AIR_VALUE_FONT_COLOR,
            ITEM_DAMAGE_PERCENTAGE_PLACES,
            ITEM_STATUS_COUNT_FONT_COLOR,
            ITEM_STATUS_SAFE_FONT_COLOR,
            ITEM_STATUS_SLIGHTLY_FONT_COLOR,
            ITEM_STATUS_ATTENTION_FONT_COLOR,
            ITEM_STATUS_WARNING_FONT_COLOR,
            ITEM_STATUS_DANGER_FONT_COLOR,
            PROHIBIT_DRAW_OVERLAY,
            PROHIBIT_DRAW_PUMPKIN_BLUR,
            PROHIBIT_DRAW_POWDER_SNOW_OUTLINE,
            PROHIBIT_DRAW_SPYGLASS_SCOPE,
            MOUNT_JUMPING_SWITCH_JUMP_BAR,
            ALWAYS_RENDER_FOOD,
            OPEN_CONFIG_GUI,
            LOOK_KNAPSACK_STATUS
        );
    }

    public static class ENABLE {
        public static final ConfigBooleanHotkeyed DRAW_CLASSIC_STATUS_BAR = ALL.DRAW_CLASSIC_STATUS_BAR;
        public static final ConfigBooleanHotkeyed DRAW_ITEMS_STATUS = ALL.DRAW_ITEMS_STATUS;
        public static final ConfigBooleanHotkeyed DRAW_ITEM_DAMAGE_PERCENTAGE = ALL.DRAW_ITEM_DAMAGE_PERCENTAGE;
        public static final ConfigBooleanHotkeyed DRAW_HAND_ITEM_ENCHANTMENTS = ALL.DRAW_HAND_ITEM_ENCHANTMENTS;
        public static final ConfigBooleanHotkeyed DRAW_MAIN_HAND_ITEM_ENCHANTMENTS = ALL.DRAW_MAIN_HAND_ITEM_ENCHANTMENTS;
        public static final ConfigBooleanHotkeyed DRAW_OFFSET_HAND_ITEM_ENCHANTMENTS = ALL.DRAW_OFFSET_HAND_ITEM_ENCHANTMENTS;
        public static final ConfigBooleanHotkeyed DRAW_ARMORS_STATUS = ALL.DRAW_ARMORS_STATUS;
        public static final ConfigBooleanHotkeyed DRAW_ITEMS_DANGER_STATUS_INFO = ALL.DRAW_ITEMS_DANGER_STATUS_INFO;
        public static final ConfigBooleanHotkeyed DRAW_STATUS_EFFECT_TIMER = ALL.DRAW_STATUS_EFFECT_TIMER;
        public static final ConfigBooleanHotkeyed DRAW_BOX_MAIN_ITEM = ALL.DRAW_BOX_MAIN_ITEM;

        public static final ImmutableList<ConfigBooleanHotkeyed> HOTKEY_LIST = ImmutableList.of(
            DRAW_CLASSIC_STATUS_BAR,
            DRAW_ITEMS_STATUS,
            DRAW_ITEM_DAMAGE_PERCENTAGE,
            DRAW_HAND_ITEM_ENCHANTMENTS,
            DRAW_MAIN_HAND_ITEM_ENCHANTMENTS,
            DRAW_OFFSET_HAND_ITEM_ENCHANTMENTS,
            DRAW_ARMORS_STATUS,
            DRAW_ITEMS_DANGER_STATUS_INFO,
            DRAW_STATUS_EFFECT_TIMER,
            DRAW_BOX_MAIN_ITEM
        );
    }

    public static class GUI {
        public static final ConfigColor HEALTH_VALUE_FONT_COLOR = ALL.HEALTH_VALUE_FONT_COLOR;
        public static final ConfigColor ABSORPTION_VALUE_FONT_COLOR = ALL.ABSORPTION_VALUE_FONT_COLOR;
        public static final ConfigColor ARMOR_VALUE_FONT_COLOR = ALL.ARMOR_VALUE_FONT_COLOR;
        public static final ConfigColor HUNGER_VALUE_FONT_COLOR = ALL.HUNGER_VALUE_FONT_COLOR;
        public static final ConfigColor AIR_VALUE_FONT_COLOR = ALL.AIR_VALUE_FONT_COLOR;
        public static final ConfigString ITEM_DAMAGE_PERCENTAGE_PLACES = ALL.ITEM_DAMAGE_PERCENTAGE_PLACES;
        public static final ConfigColor ITEM_STATUS_SAFE_FONT_COLOR = ALL.ITEM_STATUS_SAFE_FONT_COLOR;
        public static final ConfigColor ITEM_STATUS_COUNT_FONT_COLOR = ALL.ITEM_STATUS_COUNT_FONT_COLOR;
        public static final ConfigColor ITEM_STATUS_SLIGHTLY_FONT_COLOR = ALL.ITEM_STATUS_SLIGHTLY_FONT_COLOR;
        public static final ConfigColor ITEM_STATUS_ATTENTION_FONT_COLOR = ALL.ITEM_STATUS_ATTENTION_FONT_COLOR;
        public static final ConfigColor ITEM_STATUS_WARNING_FONT_COLOR = ALL.ITEM_STATUS_WARNING_FONT_COLOR;
        public static final ConfigColor ITEM_STATUS_DANGER_FONT_COLOR = ALL.ITEM_STATUS_DANGER_FONT_COLOR;

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
            HEALTH_VALUE_FONT_COLOR,
            ABSORPTION_VALUE_FONT_COLOR,
            ARMOR_VALUE_FONT_COLOR,
            HUNGER_VALUE_FONT_COLOR,
            AIR_VALUE_FONT_COLOR,
            ITEM_DAMAGE_PERCENTAGE_PLACES,
            ITEM_STATUS_COUNT_FONT_COLOR,
            ITEM_STATUS_SAFE_FONT_COLOR,
            ITEM_STATUS_SLIGHTLY_FONT_COLOR,
            ITEM_STATUS_ATTENTION_FONT_COLOR,
            ITEM_STATUS_WARNING_FONT_COLOR,
            ITEM_STATUS_DANGER_FONT_COLOR
        );
    }

    public static class ENHANCE {
        public static final ConfigBooleanHotkeyed PROHIBIT_DRAW_OVERLAY = ALL.PROHIBIT_DRAW_OVERLAY;
        public static final ConfigBooleanHotkeyed PROHIBIT_DRAW_PUMPKIN_BLUR = ALL.PROHIBIT_DRAW_PUMPKIN_BLUR;
        public static final ConfigBooleanHotkeyed PROHIBIT_DRAW_POWDER_SNOW_OUTLINE = ALL.PROHIBIT_DRAW_POWDER_SNOW_OUTLINE;
        public static final ConfigBooleanHotkeyed PROHIBIT_DRAW_SPYGLASS_SCOPE = ALL.PROHIBIT_DRAW_SPYGLASS_SCOPE;
        public static final ConfigBoolean ALWAYS_RENDER_FOOD = ALL.ALWAYS_RENDER_FOOD;
        public static final ConfigBoolean MOUNT_JUMPING_SWITCH_JUMP_BAR = ALL.MOUNT_JUMPING_SWITCH_JUMP_BAR;

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
            PROHIBIT_DRAW_OVERLAY,
            PROHIBIT_DRAW_PUMPKIN_BLUR,
            PROHIBIT_DRAW_SPYGLASS_SCOPE,
            PROHIBIT_DRAW_POWDER_SNOW_OUTLINE,
            MOUNT_JUMPING_SWITCH_JUMP_BAR,
            ALWAYS_RENDER_FOOD
        );

        public static final ImmutableList<ConfigBooleanHotkeyed> HOTKEY_LIST = ImmutableList.of(
            PROHIBIT_DRAW_OVERLAY,
            PROHIBIT_DRAW_PUMPKIN_BLUR,
            PROHIBIT_DRAW_SPYGLASS_SCOPE,
            PROHIBIT_DRAW_POWDER_SNOW_OUTLINE
        );
    }

    public static class HOTKEY {
        public static final ConfigHotkey OPEN_CONFIG_GUI = ALL.OPEN_CONFIG_GUI;
        public static final ConfigHotkey LOOK_KNAPSACK_STATUS = ALL.LOOK_KNAPSACK_STATUS;

        public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            OPEN_CONFIG_GUI,
            LOOK_KNAPSACK_STATUS
        );
    }

    @Override
    public void load() {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);
        if (configFile.isFile() && configFile.exists()) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);
            if(element == null || !element.isJsonObject()){
                return;
            }

            JsonObject root = element.getAsJsonObject();
            ConfigUtils.readConfigBase(root, "ALLConfigs", ALL.OPTIONS);
        }
    }

    @Override
    public void save() {
        File dir = FileUtils.getConfigDirectory();
        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();
            ConfigUtils.writeConfigBase(root, "ALLConfigs", ALL.OPTIONS);
            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
    }
}
