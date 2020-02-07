package mngt.repository;

import mngt.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Интерфейс доступа к данным сущности "Пользователи" из БД
 */
@CacheConfig(cacheNames = "usersCache")
public interface UserRepository extends CrudRepository<User, Long> {

    @Cacheable
    @Override
    User save(User entity);

    @Cacheable
    @Override
    User findOne(Long id);

    @Override
    void delete(Long id);

    @Cacheable
    Page<User> findAll(Pageable pageable);
}
