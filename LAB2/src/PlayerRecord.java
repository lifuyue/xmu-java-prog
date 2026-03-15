import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * 玩家记录类。
 * <p>
 * 该类用于保存某一位玩家在多人模式中的获奖情况。
 * 它主要维护两类信息：
 * - awardDetails：按时间顺序记录玩家实际拿到的奖项名称，方便最终展示；
 * - counts：按奖项类型统计数量，方便后续如果需要做更细的统计扩展。
 */
public class PlayerRecord {
    /**
     * 玩家名字，例如“玩家1”“玩家2”。
     */
    private final String name;

    /**
     * 玩家实际获奖明细。
     * 这里只记录真正发到手的奖，不记录库存已空导致没领到的奖。
     */
    private final List<String> awardDetails = new ArrayList<>();

    /**
     * 玩家按奖项类别统计的获奖次数。
     */
    private final EnumMap<PrizeType, Integer> counts = new EnumMap<>(PrizeType.class);

    public PlayerRecord(String name) {
        this.name = name;

        // 初始化所有奖项计数为 0，避免后面读取时遇到 null。
        for (PrizeType prizeType : PrizeType.values()) {
            counts.put(prizeType, 0);
        }
    }

    /**
     * 把某一轮的发奖结果累加到该玩家名下。
     */
    public void addAwardOutcome(AwardOutcome outcome) {
        for (String label : outcome.getGrantedPrizeLabels()) {
            awardDetails.add(label);
        }
        for (PrizeType prizeType : outcome.getGrantedPrizeTypes()) {
            counts.put(prizeType, counts.get(prizeType) + 1);
        }
    }

    /**
     * 返回玩家名字。
     */
    public String getName() {
        return name;
    }

    /**
     * 返回获奖明细列表。
     */
    public List<String> getAwardDetails() {
        return awardDetails;
    }

    /**
     * 返回某类奖项的累计获奖次数。
     */
    public int getCount(PrizeType prizeType) {
        return counts.get(prizeType);
    }
}
