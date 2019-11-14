package ejb.session.stateless;

import entity.OutletEntity;
import java.util.List;
import util.exception.OutletNotFoundException;

public interface OutletEntitySessionBeanRemote {
    public OutletEntity retrieveOutletEntityByOutletId(Long outletId) throws OutletNotFoundException;

    public List<OutletEntity> retrieveAllOutlets();

    public Long createNewOutlet(OutletEntity newOutletEntity);
    
    public void updateOutlet(OutletEntity outletEntity);
}
