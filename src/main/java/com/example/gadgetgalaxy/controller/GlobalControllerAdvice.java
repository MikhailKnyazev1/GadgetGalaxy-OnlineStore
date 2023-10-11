package com.example.gadgetgalaxy.controller;

import com.example.gadgetgalaxy.rdb.security.RdbUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.gadgetgalaxy.entity.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addCurrentUserToModel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof RdbUserDetails) {
            User currentUser = ((RdbUserDetails) auth.getPrincipal()).getUser();
            model.addAttribute("currentUser", currentUser);
        } else {
            System.err.println("Unexpected principal type: " + auth.getPrincipal());
        }
    }

    @ModelAttribute("formatter")
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    }
}
