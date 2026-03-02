package dev.hollink.bankstanding.events;

import dev.hollink.bankstanding.domain.BankstandingLevel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BankstandingExperienceGainedEvent implements BankstandingEvent
{
	BankstandingLevel skill;
	double experienceGained;
	boolean leveledUp;
}
