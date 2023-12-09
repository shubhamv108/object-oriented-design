package hotelbooking.services;

import hotelbooking.entities.Reservation;
import hotelbooking.entities.RoomType;

public interface IReservationService {

    Reservation reserve(int hotelId, RoomType roomType);

}
