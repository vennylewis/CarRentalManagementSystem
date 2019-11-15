package ejb.session.stateless;

import entity.PartnerEntity;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;

public interface PartnerEntitySessionBeanLocal {

    public PartnerEntity createPartnerEntity(PartnerEntity newPartnerEntity);

    public PartnerEntity retrievePartnerEntityByPartnerId(Long partnerId);

    public PartnerEntity retrievePartnerEntitybyEmail(String email) throws PartnerNotFoundException;

    public PartnerEntity login(String email, String password) throws InvalidLoginCredentialException;

    public void updatePartner(PartnerEntity partnerEntity);

    public void cancelReservation(Long partnerId, Long rentalReservationEntityId);

    public void reservePartnerCustomerReservation(Long partnerId, Long rentalReservationEntityId, Long customerId);
    
}
