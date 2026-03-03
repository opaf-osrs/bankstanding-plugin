package dev.hollink.bankstanding.overlay;

import dev.hollink.bankstanding.BankstandingConfig;
import dev.hollink.bankstanding.config.ActivityState;
import dev.hollink.bankstanding.constant.TimeConstants;
import dev.hollink.bankstanding.domain.PlayerState;
import dev.hollink.bankstanding.events.BankstandingEvent;
import dev.hollink.bankstanding.events.BankstandingEventBus;
import dev.hollink.bankstanding.events.BankstandingExperienceGainedEvent;
import dev.hollink.bankstanding.events.BankstandingPlayerStateChangedEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class BankstandingDebugOverlay extends OverlayPanel
{
	private final BankstandingEventBus events;
	private final BankstandingConfig config;

	private Instant nextUpdate;
	private ActivityState state;

	public void init()
	{
		nextUpdate = Instant.now().plus(TimeConstants.TIME_TILL_INITIAL_EXP);
		state = ActivityState.GRINDING;

		events.register(this::onEvent);
	}

	public void destroy()
	{
		events.unregister(this::onEvent);
	}

	private void onEvent(BankstandingEvent event)
	{
		log.debug("Updating debug state...");
		if (event instanceof BankstandingExperienceGainedEvent)
		{
			nextUpdate = Instant.now().plus(TimeConstants.TIME_BETWEEN_DROPS);
		}
		if (event instanceof BankstandingPlayerStateChangedEvent)
		{
			nextUpdate = Instant.now().plus(TimeConstants.TIME_TILL_INITIAL_EXP);
			state = ((BankstandingPlayerStateChangedEvent) event).getNewState().getActivity();
		}
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.displayDebugPanel())
		{
			return super.render(graphics);
		}

		panelComponent.setBorder(new Rectangle(6, 4, 6, 4));
		panelComponent.getChildren().add(
			TitleComponent.builder()
				.text("BS Debugger")
				.color(Color.WHITE)
				.build()
		);
		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("State:").leftColor(Color.WHITE)
				.right(state.name())
				.build());;
		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("Xp in:").leftColor(Color.WHITE)
				.right(String.format("%ds", getSecondsUntil(nextUpdate)))
				.build());

		return super.render(graphics);
	}

	private long getSecondsUntil(Instant target)
	{
		long seconds = Duration.between(Instant.now(), target).getSeconds();
		return Math.max(0, seconds);
	}
}
