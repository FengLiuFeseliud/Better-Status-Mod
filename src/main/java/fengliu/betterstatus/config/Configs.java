package fengliu.betterstatus.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fengliu.betterstatus.BetterStatusClient;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigColor;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;

import java.io.File;
import java.util.List;

public class Configs implements IConfigHandler {
    public static Configs INSTANCE = new Configs();
    private static final String CONFIG_FILE_NAME = BetterStatusClient.MOD_ID + ".json";

    public static class ALL {
        public static final ConfigColor HEALTH_VALUE_FONT_COLOR = new ConfigColor("betterstatus.config.health.value.font.color", "#00890000", "betterstatus.config.health.value.font.color.comment");
        public static final ConfigColor ABSORPTION_VALUE_FONT_COLOR = new ConfigColor("betterstatus.config.absorption.value.font.color", "#00886023", "betterstatus.config.absorption.value.font.color.comment");
        public static final ConfigColor ARMOR_VALUE_FONT_COLOR = new ConfigColor("betterstatus.config.armor.value.font.color", "#008B8B8B", "betterstatus.config.armor.value.font.color.comment");
        public static final ConfigColor HUNGER_VALUE_FONT_COLOR = new ConfigColor("betterstatus.config.hunger.value.font.color", "#00886241", "betterstatus.config.hunger.value.font.color.comment");
        public static final ConfigColor AIR_VALUE_FONT_COLOR = new ConfigColor("betterstatus.config.air.value.font.color", "#006F7D88", "betterstatus.config.air.value.font.color.comment");
        public static final ConfigColor ITEM_STATUS_FONT_COLOR = new ConfigColor("betterstatus.config.item.status.font.color", "#00FFFFFF", "betterstatus.config.item.status.font.color.comment");
        public static final ConfigColor ITEM_STATUS_ATTENTION_FONT_COLOR = new ConfigColor("betterstatus.config.item.status.attention.font.color", "#00FF8A00", "betterstatus.config.item.status.attention.font.color.comment");
        public static final ConfigColor ITEM_STATUS_WARNING_FONT_COLOR = new ConfigColor("betterstatus.config.item.status.waring.font.color", "#00FF0000", "betterstatus.config.item.status.waring.font.color.comment");
        public static final ConfigColor ITEM_STATUS_DANGER_FONT_COLOR = new ConfigColor("betterstatus.config.item.status.danger.font.color", "#00FF0000", "betterstatus.config.item.status.danger.font.color.comment");
        public static final ConfigHotkey OPEN_CONFIG_GUI = new ConfigHotkey("betterstatus.config.hotkey.open.config.gui", "LEFT_CONTROL,B", "betterstatus.config.hotkey.open.config.gui.comment");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
            HEALTH_VALUE_FONT_COLOR,
            ABSORPTION_VALUE_FONT_COLOR,
            ARMOR_VALUE_FONT_COLOR,
            HUNGER_VALUE_FONT_COLOR,
            AIR_VALUE_FONT_COLOR,
            ITEM_STATUS_FONT_COLOR,
            ITEM_STATUS_ATTENTION_FONT_COLOR,
            ITEM_STATUS_WARNING_FONT_COLOR,
            ITEM_STATUS_DANGER_FONT_COLOR,
            OPEN_CONFIG_GUI
        );
    }

    public static class GUI {
        public static final ConfigColor HEALTH_VALUE_FONT_COLOR = ALL.HEALTH_VALUE_FONT_COLOR;
        public static final ConfigColor ABSORPTION_VALUE_FONT_COLOR = ALL.ABSORPTION_VALUE_FONT_COLOR;
        public static final ConfigColor ARMOR_VALUE_FONT_COLOR = ALL.ARMOR_VALUE_FONT_COLOR;
        public static final ConfigColor HUNGER_VALUE_FONT_COLOR = ALL.HUNGER_VALUE_FONT_COLOR;
        public static final ConfigColor AIR_VALUE_FONT_COLOR = ALL.AIR_VALUE_FONT_COLOR;
        public static final ConfigColor ITEM_STATUS_FONT_COLOR = ALL.ITEM_STATUS_FONT_COLOR;
        public static final ConfigColor ITEM_STATUS_ATTENTION_FONT_COLOR = ALL.ITEM_STATUS_ATTENTION_FONT_COLOR;
        public static final ConfigColor ITEM_STATUS_WARNING_FONT_COLOR = ALL.ITEM_STATUS_WARNING_FONT_COLOR;
        public static final ConfigColor ITEM_STATUS_DANGER_FONT_COLOR = ALL.ITEM_STATUS_DANGER_FONT_COLOR;

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
            HEALTH_VALUE_FONT_COLOR,
            ABSORPTION_VALUE_FONT_COLOR,
            ARMOR_VALUE_FONT_COLOR,
            HUNGER_VALUE_FONT_COLOR,
            AIR_VALUE_FONT_COLOR,
            ITEM_STATUS_ATTENTION_FONT_COLOR,
            ITEM_STATUS_WARNING_FONT_COLOR,
            ITEM_STATUS_DANGER_FONT_COLOR,
            ITEM_STATUS_FONT_COLOR
        );
    }

    public static class HOTKEY {
        public static final ConfigHotkey OPEN_CONFIG_GUI = ALL.OPEN_CONFIG_GUI;

        public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            OPEN_CONFIG_GUI
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
