package com.eazybytes.eazyschool.rest;

import com.eazybytes.eazyschool.constants.EasySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.model.Response;
import com.eazybytes.eazyschool.repository.ContactRepository;
import jdk.jshell.Snippet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/contact",produces ={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
@CrossOrigin(origins = "*") //Allows any domains to access my api's
public class ContactRestController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/getMessagesByStatus")
    public List<Contact> getMessagesByStatus(@RequestParam("status") String status) {
        return contactRepository.findByStatus(status);
    }

    @GetMapping("/getAllMsgStatus")
    public List<Contact> getAllMsgStatus(@RequestBody Contact contact) {
        if(contact.getStatus() !=null && contact !=null){
              return contactRepository.findByStatus(contact.getStatus());
        }else{
            return List.of();
        }
    }

    @PostMapping("/saveMsg")
    public ResponseEntity<Response> saveMsg(@RequestHeader("invocationFrom")String invocationFrom,
                                            @Valid @RequestBody Contact contact)
    {
        log.info(String.format("Header invocationFrom : %s",invocationFrom));
        contactRepository.save(contact);
        Response response=new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Msg Created Successfully..!!");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isMsgSaved","true")
                .body(response);
    }

    @DeleteMapping("/deleteMsg")
    public ResponseEntity<Response> deleteMsg(RequestEntity<Contact> requestEntity){
        HttpHeaders headers = requestEntity.getHeaders();
        headers.forEach((key,value)->{
            log.info(String.format("Header %s=%s",key,value.stream().collect(Collectors.joining("|"))));
        });
        Contact contact = requestEntity.getBody();
        contactRepository.deleteById(contact.getContactId());
        Response response=new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Msg Deleted Successfully..!! ");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/closeMsg")
    public ResponseEntity<Response> closeMsg(RequestEntity<Contact> reqContact)
    {
        Contact contact = reqContact.getBody();
        Optional<Contact> contact1 = contactRepository.findById(contact.getContactId());
        Response response=new Response();
        if(contact1.isPresent()){
            contact1.get().setStatus(EasySchoolConstants.CLOSE);
            contactRepository.save(contact1.get());
        }else{
            response.setStatusCode("400");
            response.setStatusMsg("Invalid contactId Received..!! ");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
        response.setStatusMsg("Message Successfully Closed..!! ");
        response.setStatusCode("200");
        return new ResponseEntity<Response>(response,HttpStatus.OK);
    }
}
