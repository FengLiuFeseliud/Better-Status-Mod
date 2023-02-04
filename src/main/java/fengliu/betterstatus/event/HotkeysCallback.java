package fengliu.betterstatus.event;

import fengliu.betterstatus.config.ConfigGui;
import fengliu.betterstatus.config.Configs;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.minecraft.client.MinecraftClient;

public class HotkeysCallback implements IHotkeyCallback  {
    private final MinecraftClient client = MinecraftClient.getInstance();

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

        Configs.HOTKEY.OPEN_CONFIG_GUI.getKeybind().setCallback(hotkeysCallback);
    }
}
