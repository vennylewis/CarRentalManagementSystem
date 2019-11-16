///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package ejb.session.stateless;
//
//import entity.CarEntity;
//import entity.CategoryEntity;
//import entity.ModelEntity;
//import entity.OutletEntity;
//import entity.RentalRateEntity;
//import entity.RentalReservationEntity;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import javax.ejb.EJB;
//import javax.ejb.Local;
//import javax.ejb.Remote;
//import javax.ejb.Stateless;
//import util.comparator.SortRentalFee;
//import util.enumeration.StatusEnum;
//import util.exception.CategoryNotFoundException;
//import util.exception.NoRentalRateApplicableException;
//import util.exception.OutletNotFoundException;
//
//
//@Stateless
//@Remote(ReservationSessionBeanRemote.class)
//@Local(ReservationSessionBeanLocal.class)
//public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {
//
//    @EJB(name = "categoryEntitySessionBeanLocal")
//    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
//
//    @EJB(name = "outletEntitySessionBeanLocal")
//    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
//
//    @EJB(name = "modelEntitySessionBeanLocal")
//    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;
//    
//    @EJB(name = "rentalReservationEntitySessionBeanLocal")
//    private RentalReservationEntitySessionBeanLocal rentalReservationEntitySessionBeanLocal;
//    
//    private Date rentalStartTime;
//    private Date rentalEndTime;
//    private OutletEntity pickupOutletEntity;
//    private OutletEntity returnOutletEntity;
//    List<ModelEntity> availableModels;
//    List<CategoryEntity> availableCategories;
//    ArrayList<Double> rentalFeeperCategory;
//    
//    @Override
//    public void triggerSearch(Date rentalStartTime, Date rentalEndTime, Long pickupOutletEntityId, Long returnOutletEntityId) {       
//        List<CategoryEntity> allCategories = categoryEntitySessionBeanLocal.retrieveAllCategories();
//        availableModels = new ArrayList<>();
//        availableCategories = new ArrayList<>();
//        rentalFeeperCategory = new ArrayList<>();
//        
//        try {
//            OutletEntity pickupOutletEntity = outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(pickupOutletEntityId);
//            OutletEntity returnOutletEntity = outletEntitySessionBeanLocal.retrieveOutletEntityByOutletId(returnOutletEntityId);
//
//            for(CategoryEntity cat: allCategories){
//                try {
//                    Integer catNumReserved = 0;
//                    List<ModelEntity> availModelsinCat = new ArrayList<>();
//
//                    double rentalFee = calculateRentalFee(cat, rentalStartTime, rentalEndTime);
//
//                    if(!cat.getRentalReservationEntities().isEmpty()) {
//                        List<RentalReservationEntity> catReservations = cat.getRentalReservationEntities();
//                        for(RentalReservationEntity catReservation: catReservations) {
//                            boolean carCanBeReserved = checkCarIsAvailable(rentalStartTime, rentalEndTime, catReservation.getRentalStartTime(), catReservation.getRentalEndTime(), catReservation.getReturnOutletEntity(), pickupOutletEntity);
//                            if(carCanBeReserved == false) {
//                                catNumReserved++;
//                            }
//                        }      
//                    }
//
//                    System.out.println(cat.getCategoryName().toString() + " has this number of cars reserved based on category " + catNumReserved);
//
//                    Integer totalAvailCarsInModelsNum = 0;
//                    List<ModelEntity> modelsinCat = cat.getModelEntities();
//                    for(ModelEntity model: modelsinCat) {
//                        Integer availCarsNum = model.getCarEntities().size();
//                        System.out.println(model.getMake() + " total available initial cars " + availCarsNum);
//                        if (!model.getRentalReservationEntities().isEmpty()) {
//                            List<RentalReservationEntity> modelReservations = model.getRentalReservationEntities();
//                            for(RentalReservationEntity modelReservation: modelReservations) {
//                                boolean carCanBeReserved = checkCarIsAvailable(rentalStartTime, rentalEndTime, modelReservation.getRentalStartTime(), modelReservation.getRentalEndTime(), modelReservation.getReturnOutletEntity(), pickupOutletEntity);
//                                if(carCanBeReserved == false) {
//                                    availCarsNum--;
//                                }
//                                System.out.println(model.getMake() + " aft each deducting for 1 reservation " + availCarsNum);
//                            }
//                        }
//
//                        if(availCarsNum > 0) {
//                            List<CarEntity> carsInModel = model.getCarEntities();
//                            for(CarEntity car: carsInModel) {
//                                if(car.getCarStatus().equals(StatusEnum.DISABLED)){
//                                    availCarsNum--;
//                                }
//                            }
//                            totalAvailCarsInModelsNum = totalAvailCarsInModelsNum + availCarsNum;
//                            availModelsinCat.add(model);
//                            System.out.println("Model is available for renting, and added to availModelsinCat list");
//                        }
//
//                        System.out.println("Total num of cars available in the model aft adding (or not) 1 model " + totalAvailCarsInModelsNum);
//
//                    }
//
//                    Integer availCarsInCatNum = totalAvailCarsInModelsNum - catNumReserved;
//                    System.out.println("availCarsInCatNum (the number of cars after removing cars reserved based on models and categories) " + availCarsInCatNum);
//
//                    if(availCarsInCatNum > 0) {
//                        for(ModelEntity availModel : availModelsinCat) {
//                            System.out.println("Add model to the list of models that can be booked by customer " + availModel.getMake());
//                            availableModels.add(availModel);
//                        }
//                        availableCategories.add(cat);
//                        rentalFeeperCategory.add(rentalFee);
//                    }
//                } catch (NoRentalRateApplicableException ex) {
//                    System.out.println("No rental rates are applicable for this category");
//                }
//            }
//        } catch(OutletNotFoundException ex) {
//            System.out.println("Outlet Not Found Exception");
//        }
//    }
//    
//    
//    @Override
//    public List<CategoryEntity> getCategories(){
//        return availableCategories;
//    }
//    
//    @Override
//    public List<ModelEntity> getModels(){
//        return availableModels;
//    }
//    
//    @Override
//    public ArrayList<Double> getRentalFeePerCategory() {
//        return rentalFeeperCategory;
//    }
//    
//    private boolean checkCarIsAvailable(Date rentalStartTime, Date rentalEndTime, Date existingStartTime, Date existingEndTime, OutletEntity existingReturnOutlet, OutletEntity rentalPickupOutlet) {
//        boolean check = true;
//        System.out.println("Existing start time " + existingStartTime + " and end time " + existingEndTime);
//        System.out.println("Existing return outlet " + existingReturnOutlet.getName() + " and Rental pickup outlet " + rentalPickupOutlet.getName());
//        System.out.println("Rental start time " + rentalStartTime + " and end time " + rentalEndTime);
//        if(existingStartTime.before(rentalEndTime) && existingStartTime.after(rentalStartTime)) {
//            check = false;
//        } else if(existingEndTime.before(rentalEndTime) && existingEndTime.after(rentalStartTime)) {
//            check = false;
//        } else if(existingStartTime.before(rentalStartTime) && existingEndTime.after(rentalEndTime)) {
//            check = false;
//        }  else if(existingEndTime.before(rentalStartTime)) {
//            Long difference = rentalStartTime.getTime() - existingEndTime.getTime();
//            Long minDiff = TimeUnit.MILLISECONDS.toMinutes(difference);
//            if (minDiff < 120 && existingReturnOutlet.equals(rentalPickupOutlet) == false) {
//                check = false;
//            }
//        }
//        System.out.println("Car is available for booking" + check);
//        return check;
//    }
//    
//    private double calculateRentalFee(CategoryEntity category, Date rentalStart, Date rentalEnd) throws NoRentalRateApplicableException {
//        double rentalFee = 0;
////        try {
////            CategoryEntity category = categoryEntitySessionBeanLocal.retrieveCategoryEntityByCategoryId(categoryId);
//            List<RentalRateEntity> availableRentalRates = category.getRentalRateEntities();
//            List<RentalRateEntity> applicableRentalRates = new ArrayList<>();
//            //I am using clone, such that it doesn't directly change rentalDate which was what caused problems
//            Date currentDate = (Date) rentalStart.clone();
//            if (!availableRentalRates.isEmpty()) {
//                // for each day, find the cheapest rental rate and add to total fee
//                while (currentDate.getDate() <= rentalEnd.getDate()) {
//                    applicableRentalRates = new ArrayList<>();
//                    //for debugging
//                    //System.out.println(availableRentalRates.size() + " available");
//                    for (RentalRateEntity rentalRateEntity : availableRentalRates) {
//                        //System.out.println(rentalRateEntity.getRentalRateId());
//                        if (rentalRateEntity.getValidityStartDate() == null && rentalRateEntity.getValidityEndDate() == null) {
//                            applicableRentalRates.add(rentalRateEntity);
//                        } else if (rentalRateEntity.getValidityStartDate().getDate() <= currentDate.getDate() && rentalRateEntity.getValidityEndDate().getDate() >= currentDate.getDate()) {
//                            applicableRentalRates.add(rentalRateEntity);
//                        }
//                    }
//                    //for debugging
//                    //System.out.println(applicableRentalRates.size() + " applicable");
//                    if (!applicableRentalRates.isEmpty()) {
//                        applicableRentalRates.sort(new SortRentalFee());
//                        rentalFee += applicableRentalRates.get(0).getRatePerDay();
//                    } else {
//                        throw new NoRentalRateApplicableException("No rental rate applicable! Try reserving another date.");
//                    }
//                    currentDate.setDate(currentDate.getDate() + 1);
//                }
//            } else {
//                System.out.println("No available rental rates for the selected category!");
//            }
////        } catch (CategoryNotFoundException ex) {
////            System.out.println("Category not found");
////        }
//        return rentalFee;
//    }
//
//}
