package fengliu.betterstatus.event;

import fengliu.betterstatus.BetterStatusClient;
import fengliu.betterstatus.config.Configs;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.hotkeys.*;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler, IMouseInputHandler {
    private static final InputHandler INSTANCE = new InputHandler();

    public static InputHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void addKeysToMap(IKeybindManager manager) {
        for (ConfigBooleanHotkeyed configHotkey : Configs.ENABLE.HOTKEY_LIST) {
            manager.addKeybindToMap(configHotkey.getKeybind());
        }

        for (ConfigBooleanHotkeyed configHotkey : Configs.ENHANCE.HOTKEY_LIST) {
            manager.addKeybindToMap(configHotkey.getKeybind());
        }

        for (IHotkey hotkey : Configs.HOTKEY.HOTKEY_LIST) {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        manager.addHotkeysForCategory(BetterStatusClient.MOD_ID, "betterstatus.enable.hotkeys", Configs.ENABLE.HOTKEY_LIST);
        manager.addHotkeysForCategory(BetterStatusClient.MOD_ID, "betterstatus.enhance.hotkeys", Configs.ENHANCE.HOTKEY_LIST);
        manager.addHotkeysForCategory(BetterStatusClient.MOD_ID, "betterstatus.hotkeys", Configs.HOTKEY.HOTKEY_LIST);
    }
}
