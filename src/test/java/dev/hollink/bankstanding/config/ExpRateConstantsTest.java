package dev.hollink.bankstanding.config;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static dev.hollink.bankstanding.config.ActivityState.AFK;
import static dev.hollink.bankstanding.config.ActivityState.CHATTING;
import static dev.hollink.bankstanding.config.ActivityState.GRINDING;
import static dev.hollink.bankstanding.config.ActivityState.LOAFING;
import static dev.hollink.bankstanding.config.BankDistance.CLOSE;
import static dev.hollink.bankstanding.config.BankDistance.FAR;
import static dev.hollink.bankstanding.config.BankDistance.INSIDE;
import static dev.hollink.bankstanding.config.BankDistance.NEAR;
import static dev.hollink.bankstanding.config.BankDistance.NOWHERE_NEAR;
import static dev.hollink.bankstanding.config.BankDistance.VERY_CLOSE;
import static org.junit.Assert.assertTrue;

@Slf4j
public class ExpRateConstantsTest
{
	@Test
	public void shouldAwardGoodExpPerHourDependingOnStates()
	{
		List<Combination> testCases = List.of(
			new Combination(AFK, INSIDE, 14400),
			new Combination(CHATTING, INSIDE, 12960),
			new Combination(LOAFING, INSIDE, 3_600),
			new Combination(GRINDING, INSIDE, 0),
			new Combination(AFK, VERY_CLOSE, 11_520),
			new Combination(CHATTING, VERY_CLOSE, 10368),
			new Combination(LOAFING, VERY_CLOSE, 2880),
			new Combination(GRINDING, VERY_CLOSE, 0),
			new Combination(AFK, CLOSE, 8640),
			new Combination(CHATTING, CLOSE, 7776),
			new Combination(LOAFING, CLOSE, 2160),
			new Combination(GRINDING, CLOSE, 0),
			new Combination(AFK, NEAR, 3600),
			new Combination(CHATTING, NEAR, 3240),
			new Combination(LOAFING, NEAR, 900),
			new Combination(GRINDING, NEAR, 0),
			new Combination(AFK, FAR, 720),
			new Combination(CHATTING, FAR, 648),
			new Combination(LOAFING, FAR, 180),
			new Combination(GRINDING, FAR, 0),
			new Combination(AFK, NOWHERE_NEAR, 0),
			new Combination(CHATTING, NOWHERE_NEAR, 0),
			new Combination(LOAFING, NOWHERE_NEAR, 0),
			new Combination(GRINDING, NOWHERE_NEAR, 0)
		);

		List<String> failedTests = testCases.stream()
			.filter(tc -> tc.expPerHour() != tc.expectedHourlyExpRate)
			.map(Combination::reason)
			.collect(Collectors.toList());

		failedTests.forEach(log::info);
		assertTrue("There are testcases that fail, see preceding logs...", failedTests.isEmpty());
	}

	@RequiredArgsConstructor
	public static class Combination
	{
		private final ActivityState state;
		private final BankDistance distance;
		private final double expectedHourlyExpRate;

		public double expPerHour()
		{
			double expPerAction = ExpRateConstants.calculateExpGain(state, distance);
			long actionsPerHour = 3600 / ExpRateConstants.TIME_BETWEEN_DROPS.getSeconds();

			return Math.round(expPerAction * actionsPerHour);
		}

		public String reason()
		{
			return String.format("State %s with a %s distance should give %.0f xp/h but was %.0f", state, distance, expectedHourlyExpRate, this.expPerHour());
		}
	}
}