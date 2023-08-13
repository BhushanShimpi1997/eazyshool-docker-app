package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Address;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Profile;
import com.eazybytes.eazyschool.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller("profileControllerBean")
public class ProfileController {

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping(value = "/displayProfile", method = RequestMethod.GET)
    public ModelAndView displayProfile(HttpSession session) {
        ModelAndView model = new ModelAndView();
        Profile profile = new Profile();
        Person person = (Person) session.getAttribute("loggedInUser");
        profile.setName(person.getName());
        profile.setEmail(person.getEmail());
        profile.setMobileNumber(person.getMobileNumber());
        if (person.getAddress() != null && person.getAddress().getAddressId() > 0) {
            profile.setAddress1(person.getAddress().getAddress1());
            profile.setAddress2(person.getAddress().getAddress2());
            profile.setState(person.getAddress().getState());
            profile.setCity(person.getAddress().getCity());
            profile.setZipCode(person.getAddress().getZipCode());
        }
        model.addObject("profile", profile);
        model.setViewName("profile.html");
        return model;
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public String updateProfile(@Valid @ModelAttribute("profile") Profile profile, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            return "profile.html";
        }
        Person person = (Person) session.getAttribute("loggedInUser");
        person.setName(profile.getName());
        person.setEmail(profile.getEmail());
        person.setMobileNumber(profile.getMobileNumber());
        if (person.getAddress() == null || !(person.getAddress().getAddressId() > 0)) {
            person.setAddress(new Address());
        }
        person.getAddress().setAddress1(profile.getAddress1());
        person.getAddress().setAddress2(profile.getAddress2());
        person.getAddress().setCity(profile.getCity());
        person.getAddress().setState(profile.getState());
        person.getAddress().setZipCode(profile.getZipCode());

        Person savedPerson=personRepository.save(person);
        session.setAttribute("loggedInPerson",savedPerson);
        return "redirect:/displayProfile";
    }
}
