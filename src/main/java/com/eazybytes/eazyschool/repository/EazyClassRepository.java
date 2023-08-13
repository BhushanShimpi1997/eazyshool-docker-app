package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.EazyClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EazyClassRepository extends JpaRepository<EazyClass,Integer> {

}
