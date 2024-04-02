package dev.lecture.pharmacy.api.repository;

import dev.lecture.pharmacy.api.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

}
