package com.rapicoop.project.repository;

import com.rapicoop.project.model.Distributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class DistributorRepository implements JpaRepository<Distributor, Long> {

}
