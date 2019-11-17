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
//       int[][] availCars = new int[outletNum][modelNum];
//       List<Long>[][] availCars = new 
       AvailCars[][] availCars = new AvailCars[outletNum][modelNum];    
       for(int outletIndex = 0; outletIndex < outletNum; outletIndex++) {
           for(int modelIndex = 0; modelIndex < modelIndex; modelIndex++) {
               availCars[outletIndex][modelIndex] = new AvailCars(allOutlets.get(outletIndex), allModels.get(modelIndex));
           }
       }
       
       //an arraylist to see what model each modelIndex corresponds to
       List<Long> modelIds = new ArrayList<>();
       for(ModelEntity model: allModels) {
           modelIds.add(model.getModelId());
       }
       
       //populate data 
       for (OutletEntity currOutlet: allOutlets) {
           List<CarEntity> allOutletCars = currOutlet.getCarEntities();
//           for (CarEntity currCar: allOutletCars) {
//               if(currCar)
//           }
           
       }
   }
       
   }

class AvailCars {
    private ModelEntity model;
    private OutletEntity outlet;
    private List<CarEntity> cars;

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
}