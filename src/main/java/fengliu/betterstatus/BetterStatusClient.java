package fengliu.betterstatus;

import fengliu.betterstatus.config.Configs;
import fengliu.betterstatus.event.HotkeysCallback;
import fengliu.betterstatus.event.InputHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterStatusClient implements ClientModInitializer {
	public static final String MOD_ID = "betterstatus";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitializeClient() {
		Configs.INSTANCE.load();
		HotkeysCallback.init();

		InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
		InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
		InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());
	}
}
