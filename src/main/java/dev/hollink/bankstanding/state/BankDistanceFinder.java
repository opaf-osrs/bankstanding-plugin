package dev.hollink.bankstanding.state;

import dev.hollink.bankstanding.config.BankDistance;
import dev.hollink.bankstanding.config.BankLocation;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import net.runelite.api.coords.WorldPoint;

@UtilityClass
public class BankDistanceFinder
{
	public static Optional<BankLocation> getCLosestBank(WorldPoint from)
	{
		return Arrays.stream(BankLocation.values())
			.min(Comparator.comparingInt(bank -> bank.centerPoint.distanceTo(from)));
	}

	public static BankDistance getDistanceFromClosestBank(WorldPoint from)
	{
		return getCLosestBank(from)
			.map(bank -> bank.centerPoint.distanceTo(from))
			.map(BankDistance::fromDistance)
			.orElse(BankDistance.NOWHERE_NEAR);
	}
}
