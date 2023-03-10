package fengliu.betterstatus.config;

import fengliu.betterstatus.BetterStatusClient;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.screen.Screen;

import java.util.Collections;
import java.util.List;

public class ConfigGui extends GuiConfigsBase {

    public enum ConfigGuiTab {
        ALL("betterstatus.gui.tab.all"),
        ENABLE("betterstatus.gui.tab.enable"),
        GUI("betterstatus.gui.tab.gui"),
        ENHANCE("betterstatus.gui.tab.enhance"),
        HOTKEY("betterstatus.gui.tab.hotkey");

        private final String translationKey;

        ConfigGuiTab(String translationKey){
            this.translationKey = translationKey;
        }

        public String getDisplayName(){
            return StringUtils.translate(this.translationKey);
        }
    }

    public static class TabManager {
        private static ConfigGuiTab configGuiTab = ConfigGuiTab.ALL;

        public static void setConfigGuiTab(ConfigGuiTab tab) {
            configGuiTab = tab;
        }

        public static ConfigGuiTab getConfigGuiTab() {
            return configGuiTab;
        }
    }

    private record ButtonListener(ConfigGuiTab tab, ConfigGui parent) implements IButtonActionListener {
        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton){
            TabManager.setConfigGuiTab(this.tab);

            this.parent.reCreateListWidget(); // apply the new config width
            this.parent.getListWidget().resetScrollbarPosition();
            this.parent.initGui();
        }
    }

    private int createButton(int x, int y, ConfigGuiTab tab) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, 20, tab.getDisplayName());
        button.setEnabled(TabManager.getConfigGuiTab() != tab);
        this.addButton(button, new ButtonListener(tab, this));

        return button.getWidth() + 2;
    }

    public ConfigGui(Screen screen){
        super(10, 50, BetterStatusClient.MOD_ID, null, "betterstatus.gui.configs.title");
        this.parentScreen = screen;
    }

    public ConfigGui(){
        super(10, 50, BetterStatusClient.MOD_ID, null, "betterstatus.gui.configs.title");
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();

        int x = 10;
        int y = 26;

        for(ConfigGuiTab tab: ConfigGuiTab.values()){
            x += this.createButton(x, y, tab);
        }
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs(){
        List<? extends IConfigBase> configs;
        ConfigGuiTab tab = TabManager.getConfigGuiTab();

        if (tab == ConfigGuiTab.ALL) {
            configs = Configs.ALL.OPTIONS;
        } else if(tab == ConfigGuiTab.ENABLE){
            configs = Configs.ENABLE.HOTKEY_LIST;
        } else if(tab == ConfigGuiTab.GUI){
            configs = Configs.GUI.OPTIONS;
        } else if(tab == ConfigGuiTab.ENHANCE){
            configs = Configs.ENHANCE.OPTIONS;
        } else if(tab == ConfigGuiTab.HOTKEY){
            configs = Configs.HOTKEY.HOTKEY_LIST;
        } else {
            return Collections.emptyList();
        }

        return ConfigOptionWrapper.createFor(configs);
    }

    @Override
    public void removed() {
        super.removed();
        Configs.INSTANCE.save();
    }
}
