package dev.hollink.bankstanding.overlay;

import static dev.hollink.bankstanding.constant.TimeConstants.PROGRESS_PANEL_FADE_DELAY;
import dev.hollink.bankstanding.domain.BankstandingLevel;
import dev.hollink.bankstanding.events.BankstandingEvent;
import dev.hollink.bankstanding.events.BankstandingEventBus;
import dev.hollink.bankstanding.events.BankstandingExperienceGainedEvent;
import dev.hollink.bankstanding.state.BankstandingExperienceManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Experience;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.ProgressBarComponent;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class BankstandingLevelProgressOverlay extends OverlayPanel
{
	private final BankstandingEventBus events;
	private final BankstandingExperienceManager experienceManager;

	private Instant lastExpDrop;
	private int currentLvl;
	private int currentXp;
	private int xpToLevel;
	private float progress;

	public void init()
	{
		BankstandingLevel bankstanding = experienceManager.getBankstanding();
		updateInternalState(bankstanding.getCurrentLevel(), bankstanding.getExperience());

		events.register(this::onEvent);
	}

	public void destroy()
	{
		events.unregister(this::onEvent);
	}

	private void onEvent(BankstandingEvent event)
	{
		if (event instanceof BankstandingExperienceGainedEvent)
		{
			BankstandingExperienceGainedEvent bankstanding = (BankstandingExperienceGainedEvent) event;
			updateInternalState(bankstanding.getSkill().getCurrentLevel(), bankstanding.getSkill().getExperience());
		}
	}

	private void updateInternalState(int currentLevel, double currentExperience)
	{

		int xpAtStartOfLevel = Experience.getXpForLevel(currentLevel);
		int xpForNextLevel = Experience.getXpForLevel(currentLevel + 1);

		double xpIntoLevel = currentExperience - xpAtStartOfLevel;
		double xpRequiredForLevel = xpForNextLevel - xpAtStartOfLevel;

		if (xpRequiredForLevel <= 0)
		{
			return; // safety guard
		}

		// Clamp between 0 and 1 to avoid floating precision issues
		this.progress = Math.max(0f, Math.min(1f, (float) (xpIntoLevel / xpRequiredForLevel)));
		this.currentLvl = currentLevel;
		this.currentXp = (int) currentExperience;
		this.xpToLevel = (int) xpRequiredForLevel;
		this.lastExpDrop = Instant.now();
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (Duration.between(lastExpDrop, Instant.now()).compareTo(PROGRESS_PANEL_FADE_DELAY) >= 0)
		{
			return super.render(graphics);
		}
		panelComponent.setBorder(new Rectangle(6, 4, 6, 4));
		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("Bankstanding").leftColor(Color.WHITE)
				.right(String.valueOf(currentLvl)).rightColor(Color.WHITE)
				.build());
		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("Current xp:").leftColor(Color.YELLOW)
				.right(String.valueOf(currentXp)).rightColor(Color.WHITE)
				.build());
		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("Xp to level:").leftColor(Color.YELLOW)
				.right(String.valueOf(xpToLevel)).rightColor(Color.WHITE)
				.build());
		panelComponent.getChildren().add(LineComponent.builder().build());
		panelComponent.getChildren().add(buildProgressBarComponent(progress));
		return super.render(graphics);
	}

	private ProgressBarComponent buildProgressBarComponent(float percentage)
	{
		ProgressBarComponent progressBar = new ProgressBarComponent();
		progressBar.setMinimum(0);
		progressBar.setMaximum(1);
		progressBar.setValue(progress);
		progressBar.setForegroundColor(Color.GREEN);
		return progressBar;
	}
}
