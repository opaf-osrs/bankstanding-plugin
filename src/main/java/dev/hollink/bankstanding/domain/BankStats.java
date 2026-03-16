package dev.hollink.bankstanding.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BankStats
{
	private long totalTicks;
	private long idleTicks;
	private long activeTicks;
	private int visits;

	public void addTick(boolean idle)
	{
		totalTicks++;
		if (idle)
		{
			idleTicks++;
		}
		else
		{
			activeTicks++;
		}
	}

	public void addVisit()
	{
		visits++;
	}

	public void merge(BankStats other)
	{
		totalTicks += other.totalTicks;
		idleTicks += other.idleTicks;
		activeTicks += other.activeTicks;
		visits += other.visits;
	}

	public static String formatTicksToTime(long ticks)
	{
		long seconds = Math.round(ticks * 0.6);
		long hours = seconds / 3600;
		long minutes = (seconds % 3600) / 60;
		long secs = seconds % 60;
		if (hours > 0)
		{
			return String.format("%dh %02dm %02ds", hours, minutes, secs);
		}
		if (minutes > 0)
		{
			return String.format("%dm %02ds", minutes, secs);
		}
		return String.format("%ds", secs);
	}

	public int idlePercent()
	{
		if (totalTicks == 0)
		{
			return 0;
		}
		return (int) (idleTicks * 100 / totalTicks);
	}

	public int activePercent()
	{
		if (totalTicks == 0)
		{
			return 0;
		}
		return (int) (activeTicks * 100 / totalTicks);
	}
}