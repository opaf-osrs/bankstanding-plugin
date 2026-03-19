package dev.hollink.bankstanding.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityState
{
	AFK(1),
	CHATTING(0.90),
	LOAFING(0.25),
	GRINDING(0),
	NULL(0);

	public final double expMultiplier;
}