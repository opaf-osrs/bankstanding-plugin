package dev.hollink.bankstanding.events;

import dev.hollink.bankstanding.events.BankstandingExperienceGainedEvent.BankstandingExperienceGainedEventBuilder;
import dev.hollink.bankstanding.events.BankstandingPlayerStateChangedEvent.BankstandingPlayerStateChangedEventBuilder;

public interface BankstandingEvent
{
	static BankstandingPlayerStateChangedEventBuilder stateChanged() {
		return BankstandingPlayerStateChangedEvent.builder();
	}

	static BankstandingExperienceGainedEventBuilder experienceGained()
	{
		return BankstandingExperienceGainedEvent.builder();
	}
}
