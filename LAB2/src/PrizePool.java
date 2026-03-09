import java.util.EnumMap;
import java.util.Map;

public class PrizePool {
    private final EnumMap<PrizeType, Integer> remaining = new EnumMap<>(PrizeType.class);
    private final EnumMap<PrizeType, Integer> awarded = new EnumMap<>(PrizeType.class);

    public PrizePool() {
        initializePrize(PrizeType.ZHUANGYUAN, 1);
        initializePrize(PrizeType.DUITANG, 2);
        initializePrize(PrizeType.SANHONG, 4);
        initializePrize(PrizeType.SIJIN, 8);
        initializePrize(PrizeType.ERJU, 16);
        initializePrize(PrizeType.YIXIU, 32);
    }

    private void initializePrize(PrizeType prizeType, int count) {
        remaining.put(prizeType, count);
        awarded.put(prizeType, 0);
    }

    public AwardOutcome claimAwards(RollResult result) {
        AwardOutcome outcome = new AwardOutcome();
        for (PrizeType prizeType : result.getHitPrizes()) {
            if (prizeType == PrizeType.NONE) {
                continue;
            }

            String label = result.getDisplayLabelForPrize(prizeType);
            int left = remaining.get(prizeType);
            if (left > 0) {
                remaining.put(prizeType, left - 1);
                awarded.put(prizeType, awarded.get(prizeType) + 1);
                outcome.addGrantedPrize(prizeType, label);
            } else {
                outcome.addExhaustedPrize(label);
            }
        }
        return outcome;
    }

    public boolean isEmpty() {
        for (Map.Entry<PrizeType, Integer> entry : remaining.entrySet()) {
            if (entry.getValue() > 0) {
                return false;
            }
        }
        return true;
    }

    public int getRemainingCount(PrizeType prizeType) {
        return remaining.get(prizeType);
    }

    public int getAwardedCount(PrizeType prizeType) {
        return awarded.get(prizeType);
    }
}
