package mngt.repository;

import mngt.model.Role;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

/**
 * Интерфейс доступа к данным сущности "Роль" из БД
 */
@CacheConfig(cacheNames = "roleCache")
public interface RoleRepository extends CrudRepository<Role, Long> {

    @Cacheable
    @Override
    Role findOne(Long aLong);
}
