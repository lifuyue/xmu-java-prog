import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class PlayerRecord {
    private final String name;
    private final List<String> awardDetails = new ArrayList<>();
    private final EnumMap<PrizeType, Integer> counts = new EnumMap<>(PrizeType.class);

    public PlayerRecord(String name) {
        this.name = name;
        for (PrizeType prizeType : PrizeType.values()) {
            counts.put(prizeType, 0);
        }
    }

    public void addAwardOutcome(AwardOutcome outcome) {
        for (String label : outcome.getGrantedPrizeLabels()) {
            awardDetails.add(label);
        }
        for (PrizeType prizeType : outcome.getGrantedPrizeTypes()) {
            counts.put(prizeType, counts.get(prizeType) + 1);
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getAwardDetails() {
        return awardDetails;
    }

    public int getCount(PrizeType prizeType) {
        return counts.get(prizeType);
    }
}
