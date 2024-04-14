package dev.lecture.direction.repository;

import dev.lecture.direction.entity.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectionRepository extends JpaRepository<Direction, Long>{
}
