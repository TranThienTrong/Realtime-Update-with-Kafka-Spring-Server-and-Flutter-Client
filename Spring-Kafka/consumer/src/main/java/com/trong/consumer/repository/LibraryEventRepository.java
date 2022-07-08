package com.trong.consumer.repository;

import com.trong.consumer.model.LibraryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LibraryEventRepository extends JpaRepository< LibraryEvent,Integer> {



}
