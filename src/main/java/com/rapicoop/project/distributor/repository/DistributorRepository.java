package com.rapicoop.project.distributor.repository;

import com.rapicoop.project.distributor.model.Distributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DistributorRepository extends JpaRepository<Distributor, Long> {

}
