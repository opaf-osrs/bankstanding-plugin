package dev.hollink.bankstanding;

import dev.hollink.bankstanding.config.BankDistance;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Units;

import static dev.hollink.bankstanding.BankstandingConfig.CONFIG_GROUP;

@ConfigGroup(CONFIG_GROUP)
public interface BankstandingConfig extends Config
{
	String CONFIG_GROUP = "Bankstanding";
	String CURRENT_EXPERIENCE_CONFIG_KEY = "current_experience";

	@ConfigItem(
		keyName = "overlayTimout",
		name = "Overlay timeout",
		description = "Show the experience panel for N seconds after the last exp drop."
	)
	@Units(Units.SECONDS)
	default int panelFadeTime()
	{
		return 90;
	}

	@ConfigItem(
		keyName = "overlayDistance",
		name = "Overlay max bank distance",
		description = "Hide the overlay when moving outside a bank regardless of exp drops.<br/>" +
			"Experience will still be earned within 25 tiles.<br/><br/>" +
			"note: 0 disables this feature."
	)
	default BankDistance panelHideDistance()
	{
		return BankDistance.FAR;
	}

	@ConfigItem(
		keyName = "overlayAlwaysOn",
		name = "Keep overlay always visible",
		description = "Always show the bank standing experience panel, regardless of exp or distance."
	)
	default boolean panelAlwaysOn()
	{
		return false;
	}

	@ConfigSection(
		name = "Debug",
		description = "Debugging panels and region highlights",
		position = 2,
		closedByDefault = true
	)
	String DEBUG_SECTION = "debug";

	@ConfigItem(
		keyName = "bankHighlight",
		name = "Highlight bank tiles",
		description = "Draw a tilemaker on the tiles which are configured in as 'Bank'",
		section = DEBUG_SECTION
	)
	default boolean bankHighlight()
	{
		return false;
	}

	@ConfigItem(
		keyName = "debugPanel",
		name = "Display debug panel",
		description = "Add a secondary panel which shows the active player state and distance towards the closest bank",
		section = DEBUG_SECTION
	)
	default boolean displayDebugPanel()
	{
		return false;
	}
}
