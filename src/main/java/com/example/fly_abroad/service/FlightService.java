package com.example.fly_abroad.service;

import com.example.fly_abroad.dto.BookTicketDto;
import com.example.fly_abroad.dto.CreateFlightDto;
import com.example.fly_abroad.dto.SearchFlightDto;
import com.example.fly_abroad.entity.*;
import com.example.fly_abroad.mapper.CreateFlightDtoMapper;
import com.example.fly_abroad.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private AirlineRepository airlineRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    public boolean save(CreateFlightDto createFlightDto, User user) {

        Optional<Airline> byAdministrator = airlineRepository.findByAdministrator(user);

        if (byAdministrator.isPresent()) {

            Airline airline = byAdministrator.get();
            Flight flight = CreateFlightDtoMapper.createFlightDtoToFlight(createFlightDto);
            flight.setAirline(airline);
            Optional<Airport> from = airportRepository.findByName(flight.getFrom().getName());
            Optional<Airport> to = airportRepository.findByName(flight.getTo().getName());
            from.ifPresent(flight::setFrom);
            to.ifPresent(flight::setTo);

            if (!flightRepository.existsByFrom_NameAndTo_NameAndDepartureAndArrival(flight.getFrom().getName(), flight.getTo().getName(), flight.getDeparture(), flight.getArrival())) {
                Set<Flight> flights = airline.getFlights();
                flights.add(flight);
                airline.setFlights(flights);

                airlineRepository.save(airline);
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public PageableFlights findByAirlineName(String airlineName, int page, int size) {

        List<Flight> flights = flightRepository.findAllByAirline_Name(airlineName, PageRequest.of(page, size));
        Long countFlights = flightRepository.countAllByAirline_Name(airlineName);
        return createPageableFlight(flights, size, page, countFlights);
    }

    @Transactional(readOnly = true)
    public Optional<Flight> findById(Long id) {
        return flightRepository.findById(id);
    }

    public void update(Flight flight) {
        flightRepository.save(flight);
    }

    @Transactional(readOnly = true)
    public PageableFlights findByParams(SearchFlightDto searchFlightDto, int page, int size) {

        LocalDateTime localDateTime = LocalDateTime.parse(searchFlightDto.getDepartureDay() + "T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        List<Flight> flights = flightRepository.findAllByFrom_Address_CityAndTo_Address_CityAndDepartureAfterAndDepartureBeforeAndUnbookedTicketsQuantityIsGreaterThanEqual(
                searchFlightDto.getFromCity(), searchFlightDto.getToCity(), localDateTime, localDateTime.plusDays(1), searchFlightDto.getTicketQuantity(), PageRequest.of(page - 1, size));
        Long countFlights = flightRepository.countAllByFrom_Address_CityAndTo_Address_CityAndDepartureAfterAndDepartureBeforeAndUnbookedTicketsQuantityIsGreaterThanEqual(
                searchFlightDto.getFromCity(), searchFlightDto.getToCity(), localDateTime, localDateTime.plusDays(1), searchFlightDto.getTicketQuantity());
        return createPageableFlight(flights, size, page, countFlights);
    }

    public boolean bookTicket(BookTicketDto bookTicketDto, User user) {
        Optional<Flight> byId = flightRepository.findById(bookTicketDto.getFlightId());

        if (byId.isPresent()) {

            Flight flight = byId.get();
            Optional<Ticket> byFlight_idAndTicketClass = ticketRepository.findByFlight_IdAndTicketClass(bookTicketDto.getFlightId(), bookTicketDto.getTicketClass());

            if (byFlight_idAndTicketClass.isPresent()) {

                Ticket ticket = byFlight_idAndTicketClass.get();

                if (ticket.getUnbookedQuantity() >= bookTicketDto.getQuantity()) {
                    BookedTicket bookedTicket = BookedTicket.builder()
                            .ticket(ticket)
                            .customer(user)
                            .quantity(bookTicketDto.getQuantity())
                            .build();

                    ticket.setBookedQuantity(ticket.getBookedQuantity() + bookedTicket.getQuantity());
                    ticket.setUnbookedQuantity(ticket.getUnbookedQuantity() - bookedTicket.getQuantity());
                    flight.setUnbookedTicketsQuantity(flight.getUnbookedTicketsQuantity() - bookedTicket.getQuantity());

                    List<BookedTicket> bookedTickets = user.getBookedTickets();
                    bookedTickets.add(bookedTicket);
                    user.setBookedTickets(bookedTickets);

                    userRepository.save(user);
                    return true;
                }
            }
        }
        return false;
    }

    public PageableFlights findAllByTo(String city, int page, int size){
        List<Flight> flights = flightRepository.findAllByTo_Address_City(city, PageRequest.of(page - 1, size, Sort.by("departure").ascending()));
        Long countFlights = flightRepository.countAllByTo_Address_City(city);
        return createPageableFlight(flights, size, page, countFlights);
    }

    private PageableFlights createPageableFlight(List<Flight> flights, int size, int page,  Long countFlights) {
        return PageableFlights.builder()
                .flights(flights)
                .size(size)
                .page(page)
                .countOfPages((long) Math.ceil(countFlights.doubleValue() / size))
                .build();
    }
}
