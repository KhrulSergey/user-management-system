package mngt.util;


import com.fasterxml.jackson.databind.util.StdConverter;
import mngt.model.Role;

/**
 * Конвертер-обертка для сущности Роль
 */
public class SerializerRoleJsonConverter extends StdConverter<Role, Long> {

    @Override
    public Long convert(Role value) {
        return value.getId();
    }
}
