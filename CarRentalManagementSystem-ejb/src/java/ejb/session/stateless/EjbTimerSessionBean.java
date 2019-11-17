package ejb.session.stateless;

import entity.CarEntity;
import entity.CategoryEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.RentalReservationEntity;
import entity.TransitDriverDispatchRecordEntity;
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
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.comparator.SortRentalReservationPickup;
import util.comparator.SortRentalReservationReturn;
import util.enumeration.RentalStatusEnum;
import util.exception.OutletNotFoundException;
import util.exception.RentalReservationNotFoundException;

@Stateless
@Remote(EjbTimerSessionBeanRemote.class)
@Local(EjbTimerSessionBeanLocal.class)
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    @EJB(name = "OutletEntitySessionBeanLocal")
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;

    @EJB(name = "CarEntitySessionBeanLocal")
    private CarEntitySessionBeanLocal carEntitySessionBeanLocal;

    @EJB(name = "RentalReservationEntitySessionBeanLocal")
    private RentalReservationEntitySessionBeanLocal rentalReservationEntitySessionBeanLocal;

    @EJB(name = "TransitDriverDispatchRecordSessionBeanLocal")
    private TransitDriverDispatchRecordSessionBeanLocal transitDriverDispatchRecordSessionBeanLocal;

    @EJB(name = "ModelEntitySessionBeanLocal")
    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;

    @Resource SessionContext sessionContext;
    
    @Override
    public void createTimer(String dateStr) { 
        TimerService timerService = sessionContext.getTimerService();
        timerService.createTimer(5000, dateStr);
   }
   
   //Timeout can only call methods with void as return type
   @Timeout
   public void handleTimeout(Timer timer) {
        try {
            String dateInfo = timer.getInfo().toString();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date date = simpleDateFormat.parse(dateInfo + " 02:00");
            allocateCars(date);
       } catch(ParseException ex) {
           System.out.println("Date entered wrongly");
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
        Long currOutletId;
        Long currCategoryId;
        Long currModelId;
        CarEntity currCar;
        List<RentalReservationEntity> categoryPickups = new ArrayList<>();
        
        //the allocation
        while (pointerReturn < returnTotal && pointerPickup < pickupTotal) {
            RentalReservationEntity returned = returnedRentalReservations.get(pointerReturn);
            RentalReservationEntity pickup = pickupRentalReservations.get(pointerPickup);
            
            Date timeReturned = returned.getRentalEndTime();
            Date timePickup = pickup.getRentalStartTime();
            
            if(timeReturned.compareTo(timePickup) <=0) {
                //when the earliest reservation is a car returned, we add to availCars for that model and outlet
                currOutletId = returned.getReturnOutletEntity().getOutletId();
                currCar = returned.getCarEntity();
                currModelId = currCar.getModelEntity().getModelId();
                
                int outletIndex = outletIds.indexOf(currOutletId);
                int modelIndex = modelIds.indexOf(currModelId);
                availCars[outletIndex][modelIndex].getCars().add(currCar);           
            } else {
                //when the earliest reservation is a car pickuped, associate car with rental reservation and remove from the availCars, and if the car is not from the outlet, create a transit driver dispatch record
                currOutletId = pickup.getReturnOutletEntity().getOutletId();
                int outletIndex = outletIds.indexOf(currOutletId);
                if(pickup.getModelEntity() != null) {
                    currModelId = pickup.getModelEntity().getModelId();                    
                    int modelIndex = modelIds.indexOf(currModelId);
                    if(!availCars[outletIndex][modelIndex].getCars().isEmpty()) {
                        int carsNum = availCars[outletIndex][modelIndex].getCars().size();
                        currCar = availCars[outletIndex][modelIndex].getCars().remove(carsNum);
                        
                        //set associations
                        pickup.setCarEntity(currCar);
                        currCar.setRentalReservationEntity(pickup);
                        rentalReservationEntitySessionBeanLocal.updateRentalReservation(pickup);
                    } else {
                        //loop through same model but different outlet, and choose the first car on the list
                        boolean allocated = false;
                        for(int i = 0; i < outletNum; i++) {
                            if (i != modelIndex) {
                                if (!availCars[i][modelIndex].getCars().isEmpty()) {
//                                    int carsNum = availCars[i][modelIndex].getCars().size();
                                    currCar = availCars[i][modelIndex].getCars().remove(0);

                                    //set associations
                                    pickup.setCarEntity(currCar);
                                    currCar.setRentalReservationEntity(pickup);
                                    rentalReservationEntitySessionBeanLocal.updateRentalReservation(returned);
                                    
                                    //create transit driver dispatch record for employees to fetch
                                    try {
                                        transitDriverDispatchRecordSessionBeanLocal.createTransitDriverDispatchRecordEntity(new TransitDriverDispatchRecordEntity(pickup.getRentalStartTime(), outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(outletIds.get(i)).getName()), pickup.getRentalReservationId(), currOutletId);
                                    } catch(OutletNotFoundException | RentalReservationNotFoundException ex) {
                                        System.out.println("Error in creating transit driver dispatch record");
                                    }
                                    allocated = true;
                                }
                                if(allocated) {
                                    break;
                                }
                            }
                        }
                    }

                } else {
                    //rentalReservations by category will only be allocated later
                    categoryPickups.add(pickup);
                }
            }
        }
        
        //when cars to be picked up are not allocated yet, we do not need to worry abt the converse, because there is no actual changes to 
        while (pointerPickup < pickupTotal) {
            RentalReservationEntity pickup = pickupRentalReservations.get(pointerPickup);
            //when the earliest reservation is a car pickuped, associate car with rental reservation and remove from the availCars, and if the car is not from the outlet, create a transit driver dispatch record
            currOutletId = pickup.getReturnOutletEntity().getOutletId();
            int outletIndex = outletIds.indexOf(currOutletId);
            if(pickup.getModelEntity() != null) {
                currModelId = pickup.getModelEntity().getModelId();                    
                int modelIndex = modelIds.indexOf(currModelId);
                if(!availCars[outletIndex][modelIndex].getCars().isEmpty()) {
                    int carsNum = availCars[outletIndex][modelIndex].getCars().size();
                    currCar = availCars[outletIndex][modelIndex].getCars().remove(carsNum);

                    //set associations
                    pickup.setCarEntity(currCar);
                    currCar.setRentalReservationEntity(pickup);
                    rentalReservationEntitySessionBeanLocal.updateRentalReservation(pickup);
                } else {
                    //loop through same model but different outlet, and choose the first car on the list
                    boolean allocated = false;
                    for(int i = 0; i < outletNum; i++) {
                        if (i != modelIndex) {
                            if (!availCars[i][modelIndex].getCars().isEmpty()) {
//                                    int carsNum = availCars[i][modelIndex].getCars().size();
                                currCar = availCars[i][modelIndex].getCars().remove(0);

                                //set associations
                                pickup.setCarEntity(currCar);
                                currCar.setRentalReservationEntity(pickup);
                                rentalReservationEntitySessionBeanLocal.updateRentalReservation(pickup);

                                //create transit driver dispatch record for employees to fetch
                                try {
                                    transitDriverDispatchRecordSessionBeanLocal.createTransitDriverDispatchRecordEntity(new TransitDriverDispatchRecordEntity(pickup.getRentalStartTime(), outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(outletIds.get(i)).getName()), pickup.getRentalReservationId(), currOutletId);
                                } catch(OutletNotFoundException | RentalReservationNotFoundException ex) {
                                    System.out.println("Error in creating transit driver dispatch record");
                                }
                                allocated = true;
                            }
                            if(allocated) {
                                break;
                            }
                        }
                    }
                }

            } else {
                //rentalReservations by category will only be allocated later
                categoryPickups.add(pickup);
            }
        }
        
        //allocate renral reservation based on category their cars
        while(!categoryPickups.isEmpty()) {
            RentalReservationEntity pickup = pickupRentalReservations.get(pointerPickup);
            currOutletId = pickup.getReturnOutletEntity().getOutletId();
            int outletIndex = outletIds.indexOf(currOutletId);
            CategoryEntity currCategory = pickup.getCategoryEntity();
//            List<Integer> modelIndexinCat = new ArrayList<>();
            
            for (ModelEntity modelinCat: currCategory.getModelEntities()) {
//                modelIndexinCat.add(modelIds.indexOf(modelinCat.getModelId()));
                int modelIndex = modelIds.indexOf(modelinCat.getModelId());
                boolean allocated = false;
                
                if(!availCars[outletIndex][modelIndex].getCars().isEmpty()) {
                    int carsNum = availCars[outletIndex][modelIndex].getCars().size();
                    currCar = availCars[outletIndex][modelIndex].getCars().remove(carsNum);

                    //set associations
                    pickup.setCarEntity(currCar);
                    currCar.setRentalReservationEntity(pickup);
                    rentalReservationEntitySessionBeanLocal.updateRentalReservation(pickup);
                    allocated = true;
                } else {
                    //loop through same model but different outlet, and choose the first car on the list
                    for(int i = 0; i < outletNum; i++) {
                        if (i != modelIndex) {
                            if (!availCars[i][modelIndex].getCars().isEmpty()) {
    //                                    int carsNum = availCars[i][modelIndex].getCars().size();
                                currCar = availCars[i][modelIndex].getCars().remove(0);

                                //set associations
                                pickup.setCarEntity(currCar);
                                currCar.setRentalReservationEntity(pickup);
                                rentalReservationEntitySessionBeanLocal.updateRentalReservation(pickup);

                                //create transit driver dispatch record for employees to fetch
                                try {
                                    transitDriverDispatchRecordSessionBeanLocal.createTransitDriverDispatchRecordEntity(new TransitDriverDispatchRecordEntity(pickup.getRentalStartTime(), outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(outletIds.get(i)).getName()), pickup.getRentalReservationId(), currOutletId);
                                } catch(OutletNotFoundException | RentalReservationNotFoundException ex) {
                                    System.out.println("Error in creating transit driver dispatch record");
                                }
                                allocated = true;
                            }
                            if(allocated) {
                                break;
                            }
                        }
                    }
                }
                if(allocated) {
                    break;
                }
            }
        }
        
        
    }
    
   
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