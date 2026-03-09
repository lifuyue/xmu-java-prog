import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RollResult {
    private final int[] dice;
    private final PrizeType primaryPrize;
    private final ChampionType championType;
    private final List<PrizeType> hitPrizes;

    public RollResult(int[] dice, PrizeType primaryPrize, ChampionType championType, List<PrizeType> hitPrizes) {
        this.dice = Arrays.copyOf(dice, dice.length);
        this.primaryPrize = primaryPrize;
        this.championType = championType;
        this.hitPrizes = Collections.unmodifiableList(new ArrayList<>(hitPrizes));
    }

    public int[] getDice() {
        return Arrays.copyOf(dice, dice.length);
    }

    public PrizeType getPrimaryPrize() {
        return primaryPrize;
    }

    public ChampionType getChampionType() {
        return championType;
    }

    public List<PrizeType> getHitPrizes() {
        return hitPrizes;
    }

    public boolean hasPrize() {
        return primaryPrize != PrizeType.NONE;
    }

    public String getResultDescription() {
        if (primaryPrize == PrizeType.ZHUANGYUAN) {
            return "状元档：" + championType.getDisplayName();
        }
        if (hitPrizes.isEmpty()) {
            return PrizeType.NONE.getDisplayName();
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < hitPrizes.size(); i++) {
            if (i > 0) {
                builder.append(" + ");
            }
            builder.append(hitPrizes.get(i).getDisplayName());
        }
        return builder.toString();
    }

    public String getDisplayLabelForPrize(PrizeType prizeType) {
        if (prizeType == PrizeType.ZHUANGYUAN) {
            return PrizeType.ZHUANGYUAN.getDisplayName() + "（" + championType.getDisplayName() + "）";
        }
        return prizeType.getDisplayName();
    }
}
