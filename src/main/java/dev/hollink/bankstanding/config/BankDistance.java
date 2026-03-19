package dev.hollink.bankstanding.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BankDistance
{
	INSIDE(1, Integer.MIN_VALUE, 0),
	VERY_CLOSE(0.8, 1, 2),
	CLOSE(0.6, 3, 6),
	NEAR(0.25, 7, 11),
	FAR(0.05, 12, 15),
	NOWHERE_NEAR(0.0, 16, Integer.MAX_VALUE);

	public final double expMultiplier;
	private final int minDistance;
	private final int maxDistance;

	public static BankDistance fromDistance(int distance)
	{
		for (BankDistance value : values())
		{
			if (distance >= value.minDistance && distance <= value.maxDistance)
			{
				return value;
			}
		}
		return NOWHERE_NEAR;
	}
}