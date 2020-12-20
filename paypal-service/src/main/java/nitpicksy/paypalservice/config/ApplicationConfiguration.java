package nitpicksy.paypalservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ApplicationConfiguration {

    @Value("${LOG_STORAGE:paypal.log}")
    private String logStorage;

    @Value("${LOG_BACKUP_1:paypal-backup1.log}")
    private String logBackup1;

    @Value("${LOG_BACKUP_2:paypal-backup2.log}")
    private String logBackup2;
}
