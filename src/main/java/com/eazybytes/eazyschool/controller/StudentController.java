package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Course;
import com.eazybytes.eazyschool.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Set;

@Controller
@RequestMapping("student")
public class StudentController {

    @RequestMapping("/displayCourses")
    public ModelAndView displayCourses(HttpSession session)
    {
        ModelAndView model=new ModelAndView();
        Person person =(Person) session.getAttribute("loggedInUser");
        model.addObject("person",person);
        model.setViewName("courses_enrolled.html");
        return model;
    }
}
