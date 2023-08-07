package com.example.fly_abroad.controller;

import com.example.fly_abroad.dto.CreateFlightDto;
import com.example.fly_abroad.dto.RegAirlineDto;
import com.example.fly_abroad.entity.Airline;
import com.example.fly_abroad.entity.Flight;
import com.example.fly_abroad.entity.PageableFlights;
import com.example.fly_abroad.entity.User;
import com.example.fly_abroad.service.AirlineService;
import com.example.fly_abroad.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

@Controller
@RequestMapping("/airline")
public class AirlineController {

    @Autowired
    private AirlineService airlineService;
    @Autowired
    private FlightService flightService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("newAirline", new RegAirlineDto());
        return "airline-register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("newAirline") @Valid RegAirlineDto regAirlineDto,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) return "airline-register";

        if (airlineService.save(regAirlineDto)) {
            return "redirect:/";
        } else {
            model.addAttribute("regError", "Airline with this name already exist!");
            return "airline-register";
        }
    }

    @GetMapping("/manage")
    public String manage(@AuthenticationPrincipal User user,
                         Model model) {

        Optional<Airline> byUser = airlineService.findByUser(user);

        if (byUser.isPresent()) {
            model.addAttribute("airline", byUser.get());
        } else {
            model.addAttribute("error", "error");
        }
        return "airline-manage";
    }

    @GetMapping("/manage/edit-info")
    public String editInfo(@RequestParam String paramName,
                           @AuthenticationPrincipal User user,
                           Model model) {;
        Optional<Airline> byUser = airlineService.findByUser(user);
        if (byUser.isPresent()) {
            model.addAttribute("paramName", paramName);
            model.addAttribute("airline", byUser.get());
        } else {
            model.addAttribute("error", "error");
        }
        return "airline-edit-info";
    }

    @PostMapping("/manage/edit-info")
    public String editInfo(@ModelAttribute("airline") Airline airline,
                           @RequestParam String paramName,
                           @RequestParam Long id,
                           Model model) {

        if (airlineService.updateInfo(id, paramName, airline)) {
            return "redirect:/airline/manage";
        }
        model.addAttribute("error", "error");
        return "airline-edit-info";
    }

    @GetMapping("/manage/flights")
    public String flights(@AuthenticationPrincipal User user,
                          @RequestParam(defaultValue = "1") @Min(1) int page,
                          @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size,
                          Model model){

        Optional<Airline> byUser = airlineService.findByUser(user);
        if (byUser.isPresent()){
            PageableFlights pageableFlights = flightService.findByAirlineName(byUser.get().getName(), page-1, size);
            model.addAttribute("pageableFlights", pageableFlights);
            return "airline-manage-flights";
        }

        return "redirect:error";
    }

    @GetMapping("/manage/flights/create-flight")
    public String addFlight(Model model){
        model.addAttribute("newFlight", new CreateFlightDto());
        return "airline-manage-flights-create";
    }

    @PostMapping("/manage/flights/create-flight")
    public String addFlight(@ModelAttribute("newFlight") CreateFlightDto createFlightDto,
                            BindingResult bindingResult,
                            @AuthenticationPrincipal User user,
                            Model model){

        if (bindingResult.hasErrors()) return "airline-manage-flights-create";

        if (flightService.save(createFlightDto, user)) {
            return "redirect:/airline/manage/flights";
        } else {

            model.addAttribute("createFlightError", "This flight cannot be created, because this route is busy at the time");
            return "airline-manage-flights-create";
        }
    }

    @GetMapping("/manage/flights/edit-flight")
    public String editFlight(@RequestParam Long id,
                             Model model){

        Optional<Flight> byId = flightService.findById(id);

        if (byId.isPresent()){
            model.addAttribute("flight", byId.get());
        } else {
            model.addAttribute("flightIsnExist", "Flight is not exist with this id");
        }
        return "airline-manage-flights-edit";
    }

    @PostMapping("/manage/flights/edit-flight")
    public String editFlight(@ModelAttribute("flight") @Valid Flight flight){

        flightService.update(flight);
        return "airline-manage-flights-edit";
    }
}
