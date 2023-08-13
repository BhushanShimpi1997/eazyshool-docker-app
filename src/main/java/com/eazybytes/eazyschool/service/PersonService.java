package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.constants.EasySchoolConstants;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Roles;
import com.eazybytes.eazyschool.repository.PersonRepository;
import com.eazybytes.eazyschool.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean createPerson(Person person){
        boolean isSaved=false;
        Roles roles= rolesRepository.getByRoleName(EasySchoolConstants.STUDENT_ROLE);
        person.setRoles(roles);
        person.setPwd(passwordEncoder.encode(person.getPwd()));
        person=personRepository.save(person);
        if (person !=null && person.getPersonId()>0){
            isSaved=true;
        }
        return isSaved;
    }
}
