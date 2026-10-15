package io.quazr.backend.Users.Settings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingDefinitionsRepository extends JpaRepository<SettingDefinition, String> {
    List<SettingDefinition> findAllByOrderBySettingPathAsc();

    @Query("""
        SELECT d FROM SettingDefinition d 
        WHERE d.settingPath = :namespace 
           OR d.settingPath LIKE CONCAT(:namespace, '.%')
        ORDER BY d.settingPath
    """)
    List<SettingDefinition> findByNamespacerBySettingPathAsc(@Param("namespace") String namespace);
}