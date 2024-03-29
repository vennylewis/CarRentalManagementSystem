
package util.comparator;

import entity.CarEntity;
import java.util.Comparator;

public class SortCar implements Comparator<CarEntity> {
    @Override
    public int compare(CarEntity c1, CarEntity c2) {
        // compare by Category (alphabetically) first
        if (c1.getModelEntity().getCategoryEntity().getCategoryId() == c2.getModelEntity().getCategoryEntity().getCategoryId()) {
            // then compare by Model (alphabetically)
            if (c1.getModelEntity().getMake().equals(c2.getModelEntity().getMake())) {
                // finally, compare by license plate no (alphabetically)
                return c1.getLicensePlateNo().compareTo(c2.getLicensePlateNo());
            } else {
                return c1.getModelEntity().getMake().compareTo(c2.getModelEntity().getMake());
            }
        } else {
            return c1.getModelEntity().getCategoryEntity().getCategoryId().compareTo(c2.getModelEntity().getCategoryEntity().getCategoryId());
        }
    }
}
