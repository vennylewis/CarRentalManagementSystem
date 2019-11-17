package ejb.session.stateless;

import entity.CarEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.RentalReservationEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.ScheduleExpression;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.comparator.SortRentalReservationPickup;
import util.comparator.SortRentalReservationReturn;
import util.enumeration.RentalStatusEnum;

@Stateless
@Remote(EjbTimerSessionBeanRemote.class)
@Local(EjbTimerSessionBeanLocal.class)
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB(name = "OutletEntitySessionBeanLocal")
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @EJB(name = "CarEntitySessionBeanLocal")
    private CarEntitySessionBeanLocal carEntitySessionBeanLocal;

    @EJB(name = "RentalReservationEntitySessionBeanLocal")
    private RentalReservationEntitySessionBeanLocal rentalReservationEntitySessionBeanLocal;

    @EJB(name = "TransitDriverDispatchRecordSessionBeanLocal")
    private TransitDriverDispatchRecordSessionBeanLocal transitDriverDispatchRecordSessionBeanLocal;

    @EJB(name = "ModelEntitySessionBeanLocal")
    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;
    
    
    @Resource SessionContext sessionContext;
    
   public void createTimer(String dateStr) { 
        TimerService timerService = sessionContext.getTimerService();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        try {
             date = simpleDateFormat.parse(dateStr + " " + "02:00");
             timerService.createSingleActionTimer(date, new TimerConfig());
             //make it trigger allocateCars()
        } catch(ParseException ex) {
            System.out.println("Date is not accurate");
        }
   }
   
   public void allocateCars(Date date) { 
        //initialise data structures
        List<ModelEntity> allModels = modelEntitySessionBeanLocal.retrieveAllModels();
        int modelNum = allModels.size();
        List<OutletEntity> allOutlets = outletEntitySessionBeanLocal.retrieveAllOutlets();
        int outletNum = allOutlets.size();
        List<CarEntity> allCars = carEntitySessionBeanLocal.retrieveAllCars();
        List<RentalReservationEntity> allRentalReservations = rentalReservationEntitySessionBeanLocal.retrieveAllReservations();
        List<RentalReservationEntity> returnedRentalReservations = new ArrayList<>();
        List<RentalReservationEntity> pickupRentalReservations = new ArrayList<>();
 //       int[][] availCars = new int[outletNum][modelNum];
 //       List<Long>[][] availCars = new 
        AvailCars[][] availCars = new AvailCars[outletNum][modelNum];    
        for(int outletIndex = 0; outletIndex < outletNum; outletIndex++) {
            for(int modelIndex = 0; modelIndex < modelIndex; modelIndex++) {
                availCars[outletIndex][modelIndex] = new AvailCars(allOutlets.get(outletIndex), allModels.get(modelIndex));
            }
        }

        //an arraylist to see what model each modelIndex corresponds to
        List<Long> outletIds = new ArrayList<>();
        for(OutletEntity outlet: allOutlets) {
            outletIds.add(outlet.getOutletId());
        }
        
        List<Long> modelIds = new ArrayList<>();
        for(ModelEntity model: allModels) {
            modelIds.add(model.getModelId());
        }

        //populate the 2D array of availCars custom class with available cars for allocation
        int currOutletIndex = 0;
        for (OutletEntity currOutlet: allOutlets) {
            List<CarEntity> allOutletCars = currOutlet.getCarEntities();
            for (CarEntity currCar: allOutletCars) {
                ModelEntity model = currCar.getModelEntity();
                int currModelIndex = modelIds.indexOf(model); 
                if(currCar.getRentalStatus().equals(RentalStatusEnum.IN_OUTLET)) {
                    availCars[currOutletIndex][currModelIndex].getCars().add(currCar);
                }          
            }
            currOutletIndex++;
        }
        
        //get rental reservations which falls on the day itself, bot for return and pick up
        for(RentalReservationEntity rentalReservation: allRentalReservations) {
            if(rentalReservation.getRentalEndTime().getDate() == date.getDate()){
                returnedRentalReservations.add(rentalReservation);
            }
            
            if(rentalReservation.getRentalStartTime().getDate() == date.getDate()) {
                pickupRentalReservations.add(rentalReservation);
            }
        }
        returnedRentalReservations.sort(new SortRentalReservationReturn());
        pickupRentalReservations.sort(new SortRentalReservationPickup());
        
        int pointerReturn = 0;
        int pointerPickup = 0;
        int returnTotal = returnedRentalReservations.size();
        int pickupTotal = pickupRentalReservations.size();
        OutletEntity currOutlet;
        ModelEntity currModel;
        CarEntity currCar;
        
        //the allocation
        while (pointerReturn < returnTotal && pointerPickup < pickupTotal) {
            RentalReservationEntity returned = returnedRentalReservations.get(pointerReturn);
            RentalReservationEntity pickup = pickupRentalReservations.get(pointerPickup);
            
            Date timeReturned = returned.getRentalEndTime();
            Date timePickup = pickup.getRentalStartTime();
            
            if(timeReturned.compareTo(timePickup) <=0) {
                //when the earliest reservation is a car returned
                currOutlet = returned.getReturnOutletEntity();
                currModel = returned.getModelEntity();
                currCar = returned.getCarEntity();
                
                
//                availCars[][].getCars().add(currCar);
                
            }
        }
        
        //when cars to be picked up are not allocated yet, we do not need to worry abt the converse
        while (pointerPickup < pickupTotal) {
            
        }
        
    }
    
//   private List<RentalReservationEntity> getReturnedRentalReservation(Date date) {
//       List<RentalReservationEntity
//   
//   }
//   
//   private List<RentalReservationEntity> getPickupRentalReservation(Date date) {
//   
//   }
   
    private boolean checkTimeIsBetweenTimePeriod(Date toCheck, Date start, Date end) {  
        boolean check = false;
        
        if(start.before(end)) { //Time period is from morning to night
            if(toCheck.compareTo(start) >= 0 && toCheck.compareTo(end) <= 0) {
                check = true;
            }
        } else { //Time period is from night to morning
            if(toCheck.compareTo(end) >= 0 && toCheck.compareTo(start) <=0) {
                check = true;
            }
        }   
        
        return check;
    }
}

class AvailCars {
    private ModelEntity model;
    private OutletEntity outlet;
    private List<CarEntity> cars;
    private Date dateReturned;

    public AvailCars() {
        cars = new ArrayList<>();
    }

       
    public AvailCars(OutletEntity outlet, ModelEntity model) {
        this();
        
        this.model = model;
        this.outlet = outlet;
    }
       
       

    public ModelEntity getModel() {
        return model;
    }

    public void setModel(ModelEntity model) {
        this.model = model;
    }

    public OutletEntity getOutlet() {
        return outlet;
    }

    public void setOutlet(OutletEntity outlet) {
        this.outlet = outlet;
    }

    public List<CarEntity> getCars() {
        return cars;
    }

    public void setCars(List<CarEntity> cars) {
        this.cars = cars;
    }

    public Date getDateReturned() {
        return dateReturned;
    }

    public void setDateReturned(Date dateReturned) {
        this.dateReturned = dateReturned;
    }
}