import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AwardOutcome {
    private final List<PrizeType> grantedPrizeTypes = new ArrayList<>();
    private final List<String> grantedPrizeLabels = new ArrayList<>();
    private final List<String> exhaustedPrizeLabels = new ArrayList<>();

    public void addGrantedPrize(PrizeType prizeType, String label) {
        grantedPrizeTypes.add(prizeType);
        grantedPrizeLabels.add(label);
    }

    public void addExhaustedPrize(String label) {
        exhaustedPrizeLabels.add(label);
    }

    public List<PrizeType> getGrantedPrizeTypes() {
        return Collections.unmodifiableList(grantedPrizeTypes);
    }

    public List<String> getGrantedPrizeLabels() {
        return Collections.unmodifiableList(grantedPrizeLabels);
    }

    public List<String> getExhaustedPrizeLabels() {
        return Collections.unmodifiableList(exhaustedPrizeLabels);
    }

    public boolean hasGrantedPrize() {
        return !grantedPrizeLabels.isEmpty();
    }

    public boolean hasExhaustedPrize() {
        return !exhaustedPrizeLabels.isEmpty();
    }
}
