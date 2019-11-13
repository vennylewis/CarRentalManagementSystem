package util.comparator;

import entity.ModelEntity;
import java.util.Comparator;

public class SortModel implements Comparator<ModelEntity> {
    @Override
    public int compare(ModelEntity m1, ModelEntity m2) {
        // compare by Category (alphabetically) first
        if (m1.getCategoryEntity().getCategoryId() == m2.getCategoryEntity().getCategoryId()) {
            // then compare by name
            return m1.getMake().compareTo(m2.getMake());
        } else {
            return m1.getCategoryEntity().getCategoryId().compareTo(m2.getCategoryEntity().getCategoryId());
        }
    }
}
