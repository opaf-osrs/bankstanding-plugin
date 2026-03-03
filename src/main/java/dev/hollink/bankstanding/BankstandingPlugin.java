package dev.hollink.bankstanding;

import com.google.inject.Provides;
import dev.hollink.bankstanding.overlay.BankstandingDebugOverlay;
import dev.hollink.bankstanding.overlay.BankstandingLevelProgressOverlay;
import dev.hollink.bankstanding.state.BankstandingExperienceManager;
import dev.hollink.bankstanding.state.PlayerStateManager;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Bankstanding",
	description = "Gain (fake) experience with doing absolutely nothing",
	tags = {"bankstanding", "xp", "experience", "nothing", "afk"}
)
public class BankstandingPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private PlayerStateManager playerStateManager;

	@Inject
	private BankstandingExperienceManager experienceManager;

	@Inject
	private BankstandingLevelProgressOverlay progressOverlay;

	@Inject
	private BankstandingDebugOverlay debugOverlay;

	@Inject
	private OverlayManager overlayManager;

	@Override
	protected void startUp()
	{
		overlayManager.add(progressOverlay);
		overlayManager.add(debugOverlay);

		debugOverlay.init();
		experienceManager.init();
		progressOverlay.init();
	}

	@Override
	protected void shutDown()
	{
		debugOverlay.destroy();
		progressOverlay.destroy();
		experienceManager.destroy();

		overlayManager.remove(debugOverlay);
		overlayManager.remove(progressOverlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			log.debug("Player logged in, start bankstanding experience tracking");
			playerStateManager.startUp();
			experienceManager.startUp();
//			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		if (client.getLocalPlayer() == null)
		{
			return;
		}

		playerStateManager.checkForStateChanges();
		experienceManager.onTick();
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		playerStateManager.onChatMessage(event);
	}

	@Provides
	BankstandingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BankstandingConfig.class);
	}
}
