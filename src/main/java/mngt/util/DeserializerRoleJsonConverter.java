package mngt.util;


import com.fasterxml.jackson.databind.util.StdConverter;
import mngt.model.Role;
import mngt.repository.RoleRepository;

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
        return roleRepository.findById(value).orElse(new Role());
    }
}
