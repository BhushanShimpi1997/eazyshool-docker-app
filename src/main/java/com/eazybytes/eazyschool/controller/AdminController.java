package com.eazybytes.eazyschool.controller;


import com.eazybytes.eazyschool.model.Course;
import com.eazybytes.eazyschool.model.EazyClass;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.CourseRepository;
import com.eazybytes.eazyschool.repository.EazyClassRepository;
import com.eazybytes.eazyschool.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;


@Slf4j
@Controller
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {


    private EazyClassRepository eazyClassRepository;

    private PersonRepository personRepository;

    private CourseRepository courseRepository;

    @RequestMapping("/displayClasses")
    public ModelAndView displayClasses(Model model,HttpSession session) {
        List<EazyClass> eazyClasses = eazyClassRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("classes.html");
        modelAndView.addObject("eazyClasses",eazyClasses);
        modelAndView.addObject("eazyClass", new EazyClass());
        return modelAndView;
    }

    @PostMapping("/addNewClass")
    public ModelAndView addNewClass(Model model, @ModelAttribute("eazyClass") EazyClass eazyClass) {
        eazyClassRepository.save(eazyClass);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @RequestMapping(value="/deleteClass",method = RequestMethod.GET)
    public ModelAndView deleteClass(@RequestParam(value = "id") int id)
    {
        Optional<EazyClass> eazyClassEntity=eazyClassRepository.findById(id);
        for(Person person: eazyClassEntity.get().getPersons()){
            person.setEazyClass(null);
            personRepository.save(person);
        }
        eazyClassRepository.delete(eazyClassEntity.get());
        ModelAndView model=new ModelAndView("redirect:/admin/displayClasses");

        return model;
    }

    @RequestMapping("/displayStudents")
    public ModelAndView displayStudents(@RequestParam(value = "classId")int id,
                                        @RequestParam(value="error",required = false) String error, HttpSession httpSession)
    {
        String errorMessage=null;
        ModelAndView model=new ModelAndView();
        Optional<EazyClass> eazyClass=eazyClassRepository.findById(id);
        httpSession.setAttribute("ActiveClass",eazyClass.get());
        model.addObject("eazyClass",eazyClass.get());
        model.addObject("person",new Person());
        if (error !=null){
            errorMessage="Invalid Email ID";
            model.addObject("errorMessage",errorMessage);
        }
        model.setViewName("students.html");
        return model;
    }

    @RequestMapping(value="/addStudent", method = RequestMethod.POST)
    public ModelAndView addStudent(@ModelAttribute("person")Person person,HttpSession session)
    {
        ModelAndView model=new ModelAndView();
        EazyClass eazyClass=(EazyClass) session.getAttribute("ActiveClass");
        Person savedPerson=personRepository.readByEmail(person.getEmail());
        if (savedPerson==null || ! (savedPerson.getPersonId()>0)){
            model.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId()+"&error=true");
            return model;
        }
        savedPerson.setEazyClass(eazyClass);
        personRepository.save(savedPerson);
        eazyClass.getPersons().add(savedPerson);
        eazyClassRepository.save(eazyClass);
        model.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
        return model;
    }

    @RequestMapping(value = "/deleteStudent")
    public ModelAndView deleteStudent(@RequestParam(value = "personId") int id,HttpSession session){
       ModelAndView model=new ModelAndView();
       Optional<Person> personEntity=personRepository.findById(id);
       personEntity.get().setEazyClass(null);
       personRepository.save(personEntity.get());
       EazyClass eazyClass=(EazyClass) session.getAttribute("ActiveClass");
       eazyClass.getPersons().remove(personEntity.get());
       eazyClassRepository.save(eazyClass);
       session.setAttribute("ActiveClass",eazyClass);
       model.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
       return model;
    }

    @GetMapping("/displayCourses")
    public ModelAndView displayCourses(){
        List<Course> courses = courseRepository.findAll(Sort.by("fees").ascending());
        ModelAndView model=new ModelAndView();
        model.addObject("course",new Course());
        model.addObject("courses",courses);
        model.setViewName("courses_secure.html");
        return model;
    }

    @PostMapping("/addNewCourse")
    public ModelAndView addNewCourse(@ModelAttribute(name = "course")Course course)
    {
        ModelAndView model=new ModelAndView();
        courseRepository.save(course);
        model.setViewName("redirect:/admin/displayCourses");
        return model;
    }

    @GetMapping("/viewStudents")
    public ModelAndView viewStudents(@RequestParam(value = "id")int id,HttpSession session,
                                     @RequestParam(required = false) String error)
    {
        String errorMessage=null;
        ModelAndView model=new ModelAndView();
        Optional<Course> courses=courseRepository.findById(id);
        session.setAttribute("ActiveCourse",courses.get());
        model.addObject("courses",courses.get());
        model.addObject("person",new Person());
        if (error !=null){
            errorMessage="Invalid Email Entered !!";
            model.addObject("errorMessage",errorMessage);
        }
        model.setViewName("course_students.html");
        return model;
    }

    @PostMapping("/addStudentToCourse")
    public ModelAndView addStudentToCourse(@ModelAttribute("person")Person person,HttpSession session){
        ModelAndView model=new ModelAndView();
        Course course = (Course) session.getAttribute("ActiveCourse");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if(personEntity==null || !(personEntity.getPersonId()>0)){
            model.setViewName("redirect:/admin/viewStudents?id="+course.getCourseId()
                    +"&error=true");
            return model;
        }
        personEntity.getCourses().add(course);
        personRepository.save(personEntity);
        course.getPersons().add(personEntity);
        courseRepository.save(course);
        session.setAttribute("ActiveCourse",course);
        model.setViewName("redirect:/admin/viewStudents?id="+course.getCourseId());
        return model;
    }

    @RequestMapping("/deleteStudentFromCourse")
    public ModelAndView deleteStudentFromCourse(@RequestParam int personId,HttpSession session){
        ModelAndView model=new ModelAndView();
        Optional<Person> person = personRepository.findById(personId);
        Course course=(Course) session.getAttribute("ActiveCourse");
        person.get().getCourses().remove(course);
        personRepository.save(person.get());
        course.getPersons().remove(person);
        courseRepository.save(course);
        model.setViewName("redirect:/admin/viewStudents?id="+course.getCourseId());
        return model;
    }
}
