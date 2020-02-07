package mngt.util;


import com.fasterxml.jackson.databind.util.StdConverter;
import mngt.repository.RoleRepository;
import mngt.model.Role;

import java.util.Optional;

/**
 * Конвертер для получения списка идентификаторов сущности "Роль"
 */
public class DeserializerRoleJsonConverter extends StdConverter<Long, Role> {

    private RoleRepository roleRepository;

    public DeserializerRoleJsonConverter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role convert(Long value) {
        return Optional.ofNullable(roleRepository.findOne(value))
                .orElse(new Role());
    }
}
