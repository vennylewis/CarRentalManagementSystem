package util.comparator;

import entity.RentalRateEntity;
import java.util.Comparator;

public class SortRentalRate implements Comparator<RentalRateEntity> {
    @Override
    public int compare(RentalRateEntity r1, RentalRateEntity r2) {
        // compare by Category (alphabetically) first
        if (r1.getCategoryEntity().getCategoryId() == r2. getCategoryEntity().getCategoryId()) {
            // then compare by validity start date
            if (r1.getValidityStartDate() == null) {
                return -1;
            } else if (r2.getValidityStartDate() == null) {
                return 1;
            } else {
                return r1.getValidityStartDate().compareTo(r2.getValidityStartDate());
            }
        } else {
            return r1.getCategoryEntity().getCategoryId().compareTo(r2.getCategoryEntity().getCategoryId());
        }
    }
}
