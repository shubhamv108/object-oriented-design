package hotelbooking.taxinvoice;

import hotelbooking.entities.Reservation;
import hotelmanagement.Hotel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Invoice {

    private final int invoiceId;

    private final Date startDate;
    private final Date endDate;

    private final Hotel hotel;
    private final List<Reservation> reservations = new ArrayList<>();

    private BigDecimal totalTaxAmount;

    public Invoice(
            final int invoiceId,
            final Date startDate,
            final Date endDate,
            final Hotel hotel) {
        this.invoiceId = invoiceId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotel = hotel;
    }
}


