package nitpicksy.bank.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ApplicationConfiguration {

    @Value("${LOG_STORAGE:bank.log}")
    private String logStorage;

    @Value("${LOG_BACKUP_1:bank-backup1.log}")
    private String logBackup1;

    @Value("${LOG_BACKUP_2:bank-backup2.log}")
    private String logBackup2;
}
