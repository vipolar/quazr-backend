package io.quazr.backend.Users.Settings;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingDefinitionsService {
    private final SettingDefinitionsRepository settingDefinitionsRepository;
    
    public SettingDefinitionsService(SettingDefinitionsRepository settingDefinitionsRepository) {
        this.settingDefinitionsRepository = settingDefinitionsRepository;
    }

    public List<SettingDefinition> findSettingDefinitionsByNamespace(String namespace) {
        if (namespace == null || namespace.isBlank() || namespace.isEmpty()) {
            return this.settingDefinitionsRepository.findAllByOrderBySettingPathAsc();
        }

        String safeNamespace = namespace.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");

        return this.settingDefinitionsRepository.findByNamespacerBySettingPathAsc(safeNamespace);
    }
}
