package io.quazr.backend.Instances;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface InstancesRepository extends JpaRepository<InstanceEntity, InstanceCID>{
    @Transactional
    @Query(value = """
        SELECT * FROM quazr_create_quizr_instances(
            :quizr, :creator
        );
    """, nativeQuery = true)
    InstanceEntity createInstance(
        @Param("quizr") String quizr,
        @Param("creator") UUID creator
    );
}
