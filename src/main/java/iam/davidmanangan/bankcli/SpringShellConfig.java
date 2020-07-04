package iam.davidmanangan.bankcli;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import iam.davidmanangan.bankcli.prompt.settings.ShellHelper;

import org.jline.terminal.Terminal;

@EnableJpaRepositories
@EnableTransactionManagement
@Configuration
public class SpringShellConfig {

    @Bean
    public ShellHelper shellHelper(@Lazy Terminal terminal) {
            return new ShellHelper(terminal);
    }

}