import java.util.EnumMap;
import java.util.Map;

/**
 * 奖池类。
 * <p>
 * 该类负责管理多人模式中的奖项库存。
 * 它关心的不是“有没有命中奖”，而是“命中的奖还能不能发出去”。
 * <p>
 * 设计上维护了两张表：
 * - remaining：每类奖项还剩多少；
 * - awarded：每类奖项已经发出去多少。
 */
public class PrizePool {
    /**
     * 每类奖项的剩余库存。
     */
    private final EnumMap<PrizeType, Integer> remaining = new EnumMap<>(PrizeType.class);

    /**
     * 每类奖项的已发数量。
     */
    private final EnumMap<PrizeType, Integer> awarded = new EnumMap<>(PrizeType.class);

    /**
     * 初始化标准奖池库存。
     * 按题目常见设定：
     * - 状元 1
     * - 对堂 2
     * - 三红 4
     * - 四进 8
     * - 二举 16
     * - 一秀 32
     */
    public PrizePool() {
        initializePrize(PrizeType.ZHUANGYUAN, 1);
        initializePrize(PrizeType.DUITANG, 2);
        initializePrize(PrizeType.SANHONG, 4);
        initializePrize(PrizeType.SIJIN, 8);
        initializePrize(PrizeType.ERJU, 16);
        initializePrize(PrizeType.YIXIU, 32);
    }

    /**
     * 初始化某一类奖项。
     */
    private void initializePrize(PrizeType prizeType, int count) {
        remaining.put(prizeType, count);
        awarded.put(prizeType, 0);
    }

    /**
     * 根据一次命中结果，从奖池中尝试领取奖项。
     * <p>
     * 例如“四进 + 二举”属于组合奖，本方法会逐项检查：
     * - 四进有没有库存；
     * - 二举有没有库存；
     * 所以有可能出现“领到四进，但二举已发完”的情况。
     *
     * @param result 一次掷骰的规则判奖结果
     * @return 实际发奖结果
     */
    public AwardOutcome claimAwards(RollResult result) {
        AwardOutcome outcome = new AwardOutcome();
        for (PrizeType prizeType : result.getHitPrizes()) {
            if (prizeType == PrizeType.NONE) {
                continue;
            }

            String label = result.getDisplayLabelForPrize(prizeType);
            int left = remaining.get(prizeType);
            if (left > 0) {
                // 有库存：扣减剩余数量，同时增加已发数量。
                remaining.put(prizeType, left - 1);
                awarded.put(prizeType, awarded.get(prizeType) + 1);
                outcome.addGrantedPrize(prizeType, label);
            } else {
                // 没库存：记录为“命中但未发出”。
                outcome.addExhaustedPrize(label);
            }
        }
        return outcome;
    }

    /**
     * 判断奖池是否已经全部发完。
     */
    public boolean isEmpty() {
        for (Map.Entry<PrizeType, Integer> entry : remaining.entrySet()) {
            if (entry.getValue() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 查询某类奖项的剩余库存。
     */
    public int getRemainingCount(PrizeType prizeType) {
        return remaining.get(prizeType);
    }

    /**
     * 查询某类奖项的已发数量。
     */
    public int getAwardedCount(PrizeType prizeType) {
        return awarded.get(prizeType);
    }
}
