/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Venny
 */
public class RentalReservationNotFoundException extends Exception {

    public RentalReservationNotFoundException() {
    }

    public RentalReservationNotFoundException(String message) {
        super(message);
    }
    
}
