package com.eazybytes.eazyschool.controller;


import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class DashboardController {

    @Autowired
    private PersonRepository personRepository;
    @RequestMapping("/dashboard")
    public String displayDashboardPage(Model model, Authentication authentication, HttpSession session){
        Person person=personRepository.readByEmail(authentication.getName());
        model.addAttribute("username",person.getName());
        model.addAttribute("roles",authentication.getAuthorities().toString());
        session.setAttribute("loggedInUser",person);
        if(null !=person.getEazyClass() && person.getEazyClass().getName() !=null)
        {
            model.addAttribute("enrolledClass",person.getEazyClass().getName());
        }
        return "dashboard.html";
    }
}
