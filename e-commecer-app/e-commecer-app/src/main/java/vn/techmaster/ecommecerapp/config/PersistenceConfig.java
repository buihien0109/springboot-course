package vn.techmaster.ecommecerapp.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import vn.techmaster.ecommecerapp.entity.User;

@Configuration
@EntityScan("vn.techmaster.ecommecerapp.entity")
@EnableJpaRepositories("vn.techmaster.ecommecerapp.repository")
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class PersistenceConfig {
    @Bean
    AuditorAware<User> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
