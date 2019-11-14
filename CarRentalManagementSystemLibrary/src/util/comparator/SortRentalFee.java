package util.comparator;

import entity.RentalRateEntity;
import java.util.Comparator;

public class SortRentalFee implements Comparator<RentalRateEntity> {
    @Override
    public int compare(RentalRateEntity r1, RentalRateEntity r2) {
        Double rate1 = r1.getRatePerDay();
        Double rate2 = r2.getRatePerDay();
        return rate1.compareTo(rate2);
    }
}
