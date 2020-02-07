package mngt;

import mngt.controller.UserController;
import mngt.repository.RoleRepository;
import mngt.repository.UserRepository;
import mngt.service.UserService;
import mngt.service.UserServiceImpl;
import mngt.util.DeserializerRoleJsonConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

@EnableCaching
@EnableJpaRepositories
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public UserController userController(UserService userService) {
		return new UserController(userService);
	}

	@Bean
	public UserService userInteractor(UserRepository userRepository, Validator validator) {
		return new UserServiceImpl(userRepository, validator);
	}

	@Bean
	public DeserializerRoleJsonConverter deserializerRoleJsonConverter(RoleRepository roleRepository) {
		return new DeserializerRoleJsonConverter(roleRepository);
	}

	@Bean
	public LocalValidatorFactoryBean validatorFactoryBean() {
		return new LocalValidatorFactoryBean();
	}
}
