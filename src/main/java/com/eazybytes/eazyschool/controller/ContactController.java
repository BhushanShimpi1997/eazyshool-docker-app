package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;



@Controller
@Slf4j
public class ContactController {

    private ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    //Logger log = Logger.getLogger("ContactController.class");

    @RequestMapping("/contact")
    public String displayContactPage(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact.html";
    }

  /*  @RequestMapping(path = "/saveMsg", method = RequestMethod.POST)
    public ModelAndView saveMessage(@RequestParam String name, @RequestParam String mobileNum,
                                    @RequestParam String email,@RequestParam String subject,
                                     @RequestParam String message)

    {
          log.info("Name:"+name);
          log.info("Mobile Number: "+mobileNum);
          log.info("email: "+email );
          log.info("subject: "+subject);
          log.info("Message: "+message);
          return new ModelAndView("redirect:/contact");
    }*/

    @RequestMapping(path = "/saveMsg", method = RequestMethod.POST)
    public String saveMessage(@Valid @ModelAttribute("contact") Contact contact, Errors errors) {
        if (errors.hasErrors()) {
            log.info("Contact Form validations failed due to: " + errors.toString());
            return "contact.html";
        }
        boolean result = contactService.saveMessageDetails(contact);
        log.info("info" + result);
        return "redirect:/contact";
    }

    @RequestMapping(value = "/displayMessages/page/{pageNum}", method = RequestMethod.GET)
    public ModelAndView displayMessages(Model model, @PathVariable("pageNum")int pageNum,
                                        @RequestParam(value = "sortField")String sortField,
                                        @RequestParam(value = "sortDir")String sortDir)
    {
        Page<Contact> msgPage = contactService.findMsgsWithOpenStatus(pageNum,sortField,sortDir);
        List<Contact>contactMsgs=msgPage.getContent();
        ModelAndView md = new ModelAndView();
        md.addObject("currentPage",pageNum);
        md.addObject("totalPages",msgPage.getTotalPages());
        md.addObject("totalMsgs",msgPage.getTotalElements());
        md.addObject("sortField",sortField);
        md.addObject("sortDir",sortDir);
        md.addObject("reverseSortDir",sortDir.equals("asc")?"desc":"asc");
        md.addObject("contactMsgs",contactMsgs);
        md.setViewName("messages.html");
        return md;

    }
    @RequestMapping(value="/closeMsg",method = RequestMethod.GET)
    public String closeMsg(@RequestParam int id){
        contactService.updateMsgStatus(id);
         return "redirect:/displayMessages";
    }
}
