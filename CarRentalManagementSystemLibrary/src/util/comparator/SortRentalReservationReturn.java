package util.comparator;

import entity.RentalReservationEntity;
import java.util.Comparator;
import java.util.Date;

public class SortRentalReservationReturn implements Comparator<RentalReservationEntity>{
    @Override
    public int compare(RentalReservationEntity rr1, RentalReservationEntity rr2) {
        Date r1 = rr1.getRentalEndTime();
        Date r2 = rr2.getRentalEndTime();
        return r1.compareTo(r2);
    }
}
