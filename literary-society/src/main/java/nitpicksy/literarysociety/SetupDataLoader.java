package nitpicksy.literarysociety;

import nitpicksy.literarysociety.common.RandomPasswordGenerator;
import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.Permission;
import nitpicksy.literarysociety.model.Role;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.repository.PermissionRepository;
import nitpicksy.literarysociety.repository.RoleRepository;
import nitpicksy.literarysociety.repository.UserRepository;
import nitpicksy.literarysociety.repository.WriterRepository;
import nitpicksy.literarysociety.service.EmailNotificationService;
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

    private RoleRepository roleRepository;

    private PermissionRepository permissionRepository;

    private UserRepository userRepository;

    private WriterRepository writerRepository;

    private PasswordEncoder passwordEncoder;

    private EmailNotificationService emailNotificationService;

    private Environment environment;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        Permission manageEditors = createPermissionIfNotFound("MANAGE_EDITORS");
        Permission manageLecturers = createPermissionIfNotFound("MANAGE_LECTURER");


        Set<Permission> adminPermissions = new HashSet<>(Arrays.asList(manageEditors, manageLecturers));
        createRoleIfNotFound("ROLE_ADMIN", adminPermissions);

        Set<Permission> readerPermissions = new HashSet<>();
        createRoleIfNotFound("ROLE_READER", readerPermissions);

        Set<Permission> writerPermissions = new HashSet<>();
        createRoleIfNotFound("ROLE_WRITER", writerPermissions);

        Set<Permission> committeeMemberPermissions = new HashSet<>();
        createRoleIfNotFound("ROLE_COMMITTEE_MEMBER", committeeMemberPermissions);

        Set<Permission> editorPermissions = new HashSet<>();
        createRoleIfNotFound("ROLE_EDITOR", editorPermissions);

        Set<Permission> lecturerPermissions = new HashSet<>();
        createRoleIfNotFound("ROLE_LECTURER", lecturerPermissions);

        Set<Permission> merchantPermissions = new HashSet<>();
        createRoleIfNotFound("ROLE_MERCHANT", merchantPermissions);

        RandomPasswordGenerator randomPasswordGenerator = new RandomPasswordGenerator();
        String generatedPassword = randomPasswordGenerator.generatePassword();

        User admin = new User("Petar", "Peric", "Belgrade", "Serbia", "petarperic@maildrop.cc", "petar",
                passwordEncoder.encode(generatedPassword),
                roleRepository.findByName("ROLE_ADMIN"), UserStatus.NEVER_LOGGED_IN);

        if (userRepository.findByUsername(admin.getUsername()) != null) {
            return;
        }

        // TODO: Obrisati kad se user-i ne budu dodavali skriptom
        assignRoles();

        userRepository.save(admin);
        composeAndSendEmailToChangePassword(admin.getEmail(), generatedPassword);

        alreadySetup = true;
    }

    private void assignRoles() {
        Role roleWriter = roleRepository.findByName("ROLE_WRITER");
        Writer writer1 = writerRepository.findOneById(1L);
        writer1.setRole(roleWriter);
        writerRepository.save(writer1);
        Writer writer2 = writerRepository.findOneById(2L);
        writer2.setRole(roleWriter);
        writerRepository.save(writer2);

        Role roleEditor = roleRepository.findByName("ROLE_EDITOR");
        User editor1 = userRepository.findOneById(3L);
        editor1.setRole(roleEditor);
        userRepository.save(editor1);
        User editor2 = userRepository.findOneById(4L);
        editor2.setRole(roleEditor);
        userRepository.save(editor2);
        User editor3 = userRepository.findOneById(5L);
        editor3.setRole(roleEditor);
        userRepository.save(editor3);

        Role roleCommitteeMember = roleRepository.findByName("ROLE_COMMITTEE_MEMBER");
        User member1 = userRepository.findOneById(6L);
        member1.setRole(roleCommitteeMember);
        userRepository.save(member1);
        User member2 = userRepository.findOneById(7L);
        member2.setRole(roleCommitteeMember);
        userRepository.save(member2);
        User member3 = userRepository.findOneById(8L);
        member3.setRole(roleCommitteeMember);
        userRepository.save(member3);

        Role roleMerchant = roleRepository.findByName("ROLE_MERCHANT");
        User defaultMerchant = userRepository.findOneById(9L);
        defaultMerchant.setRole(roleMerchant);
        userRepository.save(defaultMerchant);
    }

    private void composeAndSendEmailToChangePassword(String recipientEmail, String generatedPassword) {
        String subject = "Activate account";
        StringBuilder sb = new StringBuilder();
        sb.append("An account for you on Literary Society website has been created.");
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
    public SetupDataLoader(RoleRepository roleRepository, PermissionRepository permissionRepository, UserRepository userRepository,
                           WriterRepository writerRepository, PasswordEncoder passwordEncoder, EmailNotificationService emailNotificationService,
                           Environment environment) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.writerRepository = writerRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailNotificationService = emailNotificationService;
        this.environment = environment;
    }
}
