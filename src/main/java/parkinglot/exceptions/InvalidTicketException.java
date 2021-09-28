package parkinglot.exceptions;

import parkinglot.models.Ticket;

public class InvalidTicketException extends RuntimeException {

    public InvalidTicketException(Ticket ticket) {
        super(String.format("Invalid ticket: %s", ticket));
    }

    public InvalidTicketException(Ticket ticket, String message) {
        super(String.format("Invalid ticket: %s; Message: %s", ticket, message));
    }

}
