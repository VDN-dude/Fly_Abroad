package com.example.fly_abroad.controller;

import com.example.fly_abroad.dto.BookTicketDto;
import com.example.fly_abroad.dto.SearchFlightDto;
import com.example.fly_abroad.entity.*;
import com.example.fly_abroad.service.AirportService;
import com.example.fly_abroad.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private AirportService airportService;
    @Autowired
    private FlightService flightService;

    @GetMapping
    public String home(Model model) {
        List<Airport> top5 = airportService.findTop5();
        model.addAttribute("topAirports", top5);
        return "home";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String city,
                         @RequestParam(defaultValue = "1") @Min(1) int page,
                         @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size,
                         Model model) {

        PageableFlights pageableFlights = flightService.findAllByTo(city, page, size);
        model.addAttribute("pageableFlights", pageableFlights);
        model.addAttribute("searchFlight", new SearchFlightDto());
        return "flight-search";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("searchFlight") @Valid SearchFlightDto searchFlightDto,
                         BindingResult bindingResult,
                         @RequestParam(defaultValue = "1") @Min(1) int page,
                         @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size,
                         Model model) {

        if (bindingResult.hasErrors()) return "flight-search";

        PageableFlights pageableFlights = flightService.findAllByParams(searchFlightDto, page, size);
        model.addAttribute("pageableFlights", pageableFlights);
        return "flight-search";
    }

    @GetMapping("/book-ticket")
    public String bookTicket(@RequestParam Long id,
                             HttpServletResponse response,
                             Model model) {

        Optional<Flight> byId = flightService.findById(id);
        if (byId.isPresent()){
            BookTicketDto bookTicketDto = new BookTicketDto();
            bookTicketDto.setFlightId(id);
            model.addAttribute("bookTickets", bookTicketDto);
            model.addAttribute("flight", byId.get());
        } else {
            response.setStatus(404);
        }
        return "book-ticket";
    }

    @PostMapping("/book-ticket")
    public String bookTicket(@AuthenticationPrincipal User user,
                             @ModelAttribute("bookTickets") BookTicketDto bookTicketDto,
                             RedirectAttributes redirectAttributes) {

        if (flightService.bookTicket(bookTicketDto, user)) {
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("bookTicketError", "You have booked more tickets than it is possible");
            return "redirect:/book-ticket?id=" + bookTicketDto.getFlightId();
        }
    }
}
