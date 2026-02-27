package com.fixmatch.repository;

import com.fixmatch.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * SkillRepository - Repository for Skill entity
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    /**
     * Find skill by name
     */
    Optional<Skill> findByName(String name);

    /**
     * Check if skill exists by name
     */
    boolean existsByName(String name);
}
