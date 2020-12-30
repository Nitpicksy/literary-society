package nitpicksy.paymentgateway;

import nitpicksy.paymentgateway.common.RandomPasswordGenerator;
import nitpicksy.paymentgateway.enumeration.AdminStatus;
import nitpicksy.paymentgateway.model.Admin;
import nitpicksy.paymentgateway.model.Permission;
import nitpicksy.paymentgateway.model.Role;
import nitpicksy.paymentgateway.repository.AdminRepository;
import nitpicksy.paymentgateway.repository.PermissionRepository;
import nitpicksy.paymentgateway.repository.RoleRepository;
import nitpicksy.paymentgateway.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailNotificationService emailNotificationService;

    private final Environment environment;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        Permission managePaymentMethods = createPermissionIfNotFound("MANAGE_PAYMENT_METHODS");
        Permission manageCompanies = createPermissionIfNotFound("MANAGE_COMPANIES");
        Permission createOrder = createPermissionIfNotFound("CREATE_ORDER");

        Set<Permission> adminPermissions = new HashSet<>(Arrays.asList(managePaymentMethods, manageCompanies));
        createRoleIfNotFound("ROLE_ADMIN", adminPermissions);

        Set<Permission> companyPermissions = new HashSet<>(Arrays.asList(createOrder));
        createRoleIfNotFound("ROLE_COMPANY", companyPermissions);

        RandomPasswordGenerator randomPasswordGenerator = new RandomPasswordGenerator();
        String generatedPassword = randomPasswordGenerator.generatePassword();

        //String email, String username, String password, Role role, AdminStatus status
        Admin admin = new Admin("admin@maildrop.cc", "admin", passwordEncoder.encode(generatedPassword), roleRepository.findByName("ROLE_ADMIN"),
                AdminStatus.NEVER_LOGGED_IN);

        if (adminRepository.findByUsername(admin.getUsername()) != null) {
            return;
        }

        adminRepository.save(admin);
        composeAndSendEmailToChangePassword(admin.getEmail(), generatedPassword);

        alreadySetup = true;
    }


    private void composeAndSendEmailToChangePassword(String recipientEmail, String generatedPassword) {
        String subject = "Activate account";
        StringBuilder sb = new StringBuilder();
        sb.append("An account for you on Payment Gateway website has been created.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("You can log in using your username and the following password: ");
        sb.append(generatedPassword);
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Because of the security protocol, you will have to change this given password the first time you log in.");
        sb.append(System.lineSeparator());
        sb.append("To do that click the following link:");
        sb.append(System.lineSeparator());
        sb.append(getLocalhostURL());
        sb.append("change-password");
        String text = sb.toString();

        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    private String getLocalhostURL() {
        return environment.getProperty("LOCALHOST_URL");
    }

    @Transactional
    public Permission createPermissionIfNotFound(String name) {
        Permission permission = permissionRepository.findByName(name);
        if (permission == null) {
            permission = new Permission();
            permission.setName(name);
            return permissionRepository.save(permission);
        }
        return permission;
    }

    @Transactional
    public void createRoleIfNotFound(String name, Set<Permission> permissions) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPermissions(permissions);
            roleRepository.save(role);
        }
    }

    @Autowired
    public SetupDataLoader(RoleRepository roleRepository, PermissionRepository permissionRepository, AdminRepository adminRepository,
                           PasswordEncoder passwordEncoder, EmailNotificationService emailNotificationService,
                           Environment environment) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailNotificationService = emailNotificationService;
        this.environment = environment;
    }
}
