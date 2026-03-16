package dev.hollink.bankstanding.overlay;

import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class BankstandingOverlayManager
{
	private final OverlayManager overlayManager;

	private final BankstandingLevelProgressOverlay progressOverlay;
	private final BankstandingDebugOverlay debugInfoOverlay;
	private final BankLocationDebugOverlay debugBankOverlay;
	private final ConfettiOverlay confettiOverlay;
	private final BankStatsOverlay bankStatsOverlay;

	public void init()
	{
		progressOverlay.init();

		overlayManager.add(progressOverlay);
		overlayManager.add(debugInfoOverlay);
		overlayManager.add(debugBankOverlay);
		overlayManager.add(confettiOverlay);
		overlayManager.add(bankStatsOverlay);
	}

	public void destroy()
	{
		overlayManager.remove(confettiOverlay);
		overlayManager.remove(progressOverlay);
		overlayManager.remove(debugInfoOverlay);
		overlayManager.remove(debugBankOverlay);
		overlayManager.remove(bankStatsOverlay);

		progressOverlay.destroy();
	}
}
