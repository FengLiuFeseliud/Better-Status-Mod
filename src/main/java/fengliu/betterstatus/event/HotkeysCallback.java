package fengliu.betterstatus.event;

import fengliu.betterstatus.config.ConfigGui;
import fengliu.betterstatus.config.Configs;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

public class HotkeysCallback implements IHotkeyCallback  {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private int scaledWidth = 0;
    private int scaledHeight = 0;

    private int[] getScaledXY(){
        Window window = this.client.getWindow();
        this.scaledWidth = window.getScaledWidth();
        this.scaledHeight = window.getScaledHeight();
        int x = this.scaledWidth / 2 - 91;
        int y = this.scaledHeight - 39;

        return new int[]{x, y};
    }


    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key) {
        if (key == Configs.HOTKEY.OPEN_CONFIG_GUI.getKeybind() && action == KeyAction.PRESS){
            GuiBase.openGui(new ConfigGui());
            return true;
        }

        return false;
    }

    public static void init(){
        HotkeysCallback hotkeysCallback = new HotkeysCallback();

        for (ConfigHotkey hotkey: Configs.HOTKEY.HOTKEY_LIST){
            hotkey.getKeybind().setCallback(hotkeysCallback);
        }
    }
}
