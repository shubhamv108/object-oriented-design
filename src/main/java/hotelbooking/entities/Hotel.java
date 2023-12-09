package hotelbooking.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hotel {
    private String id;
    private String name;
    private User owner;
    private Map<RoomType, Collection<Room>> rooms = new HashMap<>();
    private Map<RoomType, BigDecimal> roomPrices = new HashMap<>();
    private List<HotelFacility> facilities = new ArrayList<>();
    private commons.Address address;

    private City city;
}
