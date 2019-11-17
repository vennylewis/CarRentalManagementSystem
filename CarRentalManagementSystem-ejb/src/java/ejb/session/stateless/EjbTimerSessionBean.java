package ejb.session.stateless;

import entity.RentalReservationEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.ScheduleExpression;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

@Stateless
@Remote(EjbTimerSessionBeanRemote.class)
@Local(EjbTimerSessionBeanLocal.class)
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @Resource SessionContext sessionContext;
    
   public void createTimer(String dateStr) { 
       TimerService timerService = sessionContext.getTimerService();
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
       Date date = new Date();
       try {
            date = simpleDateFormat.parse(dateStr + " " + "02:00");
       } catch(ParseException ex) {
           System.out.println("Date is not accurate");
       }
       timerService.createSingleActionTimer(date, new TimerConfig());
       //make it trigger allocateCars()
       
   }
//   public List<RentalReservationEntity> allocateCars() { 
//       
//   }
   
   
}
