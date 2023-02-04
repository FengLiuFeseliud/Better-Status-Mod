package fengliu.betterstatus.event;

import fengliu.betterstatus.BetterStatusClient;
import fengliu.betterstatus.config.Configs;
import fi.dy.masa.malilib.hotkeys.*;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler, IMouseInputHandler {
    private static final InputHandler INSTANCE = new InputHandler();

    public static InputHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void addKeysToMap(IKeybindManager manager) {
        for (IHotkey hotkey : Configs.HOTKEY.HOTKEY_LIST) {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        manager.addHotkeysForCategory(BetterStatusClient.MOD_ID, "cloudmusic.hotkeys", Configs.HOTKEY.HOTKEY_LIST);
    }
}
