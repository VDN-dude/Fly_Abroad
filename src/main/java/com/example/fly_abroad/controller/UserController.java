package com.example.fly_abroad.controller;

import com.example.fly_abroad.dto.RegUserDto;
import com.example.fly_abroad.entity.PageableBookedTicket;
import com.example.fly_abroad.entity.User;
import com.example.fly_abroad.service.UserService;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("regUser", new RegUserDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("regUser") @Valid RegUserDto regUserDto,
                           BindingResult bindingResult,
                           Model model){

        if (bindingResult.hasErrors()) return "register";

        if (userService.save(regUserDto)) {
            return "redirect:/";
        } else {
            model.addAttribute("regError", "RegError");
            return "register";
        }

    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user,
                          @RequestParam(defaultValue = "1") @Min(1) int page,
                          @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size,
                          Model model){

        Optional<PageableBookedTicket> pageableBookedTicket = userService.paginatedBookedTicket(user, page, size);
        pageableBookedTicket.ifPresent(bookedTicket -> model.addAttribute("pageableBookedTickets", bookedTicket));
        model.addAttribute("user", user);
        return "user-profile";
    }
}
