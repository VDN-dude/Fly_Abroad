package com.example.fly_abroad.controller;

import com.example.fly_abroad.dto.CreateAirportDto;
import com.example.fly_abroad.entity.Airport;
import com.example.fly_abroad.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AirportService airportService;

    @GetMapping("/add-airport")
    public String addAirport(Model model){
        model.addAttribute("newAirport", new CreateAirportDto());
        return "add-airport";
    }

    @PostMapping("/add-airport")
    public String addAirport(@ModelAttribute("newAirport") @Valid CreateAirportDto createAirportDto,
                              BindingResult bindingResult,
                              Model model){

        if (bindingResult.hasErrors()) return "add-airport";

        if (airportService.save(createAirportDto)) {
            return "redirect:/";
        } else {
            model.addAttribute("createError", "This airport already exist");
            return "add-airport";
        }
    }

    @GetMapping("/edit-airport")
    public String editAirport(@RequestParam("name") String airportName,
                               HttpServletResponse response,
                               Model model){

        Optional<Airport> byName = airportService.findByName(airportName);

       if (byName.isPresent()) {
           model.addAttribute("airport", byName.get());
       } else {
           response.setStatus(404);
       }
        return "edit-airport";
    }

    @PostMapping("/edit-airport")
    public String editAirport(@ModelAttribute("airport") Airport airport){

        airportService.update(airport);
        return "redirect:edit-airport?name=" + airport.getName();
    }
}
