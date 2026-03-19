package dev.hollink.bankstanding.config;

import java.time.Duration;

/**
 * A base exp rate of 50 exp per 30 secondes.
 * This results in the following MAX exp/h:
 * <p>
 * 50xp * 1.2 * 2.0 * 120 = 14400
 * <p>
 * With ~15K exp/h it will take
 * - 36 days of doing nothing to get 99.
 * - 555 days of doing nothing to get 200 M
 */
public interface ExpRateConstants
{

	int BASE_EXPERIENCE = 100;

	Duration TIME_BETWEEN_DROPS = Duration.ofSeconds(25);

	static double calculateExpGain(ActivityState state, BankDistance bankDistance) {
		return BASE_EXPERIENCE * state.expMultiplier * bankDistance.expMultiplier;
	}
}
