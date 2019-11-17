package holidayreservationsystemclient;

import java.util.Scanner;

public class MainApp {

    // private PartnerEntity currentPartner;
    // private CustomerEntity currentCustomer;
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Web-based CaRMS Reservation Client for Holiday Reservation System ***\n");
            System.out.println("1: Partner Login");
            System.out.println("2: Partner Search Car");
            System.out.println("3: Partner Reserve Car");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    doLogin();
                } else if (response == 2) {
                    break;
                } else if (response == 3) {
                    break;
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option. Try again!");
                }
            }
            
            if(response == 4) {
                break;
            }
        }
    }
    
    private void doLogin() {

        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";

        System.out.println("*** CaRMS Reservation System :: Login Customer ***\n");
        System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        //throws InvalidLoginCredentialException
//        if (email.length() > 0 && password.length() > 0) {
//            currentCustomer = login(email, password);
//        } else {
//            throw new InvalidLoginCredentialException("Missing login credential!");
//        }

    }
    
}
