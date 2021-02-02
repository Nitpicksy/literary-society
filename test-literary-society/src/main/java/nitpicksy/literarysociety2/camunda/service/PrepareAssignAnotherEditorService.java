package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.dto.camunda.EnumKeyValueDTO;
import nitpicksy.literarysociety2.enumeration.UserStatus;
import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrepareAssignAnotherEditorService implements JavaDelegate {

    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Activated timeout, assigning new editors");

        List<User> editors = userService.findAllWithRoleAndStatus("ROLE_EDITOR", UserStatus.ACTIVE);

        String editorUsername = (String) execution.getVariable("editor");
        User lazyEditor = userService.findByUsername(editorUsername);

        String mainEditorUsername = (String) execution.getVariable("mainEditor");
        User mainEditor = userService.findByUsername(mainEditorUsername);

        //TODO: jos urednika u data.sql
        List<EnumKeyValueDTO> enumList = new ArrayList<>();
        for (User editor : editors) {
            if (editor.getUsername().equals(lazyEditor.getUsername()) || editor.getUsername().equals(mainEditor.getUsername())) {
                continue;
            }
            String key = "id_" + editor.getUserId();
            String editorInfo = editor.getFirstName() + " " + editor.getLastName();
            enumList.add(new EnumKeyValueDTO(key, editorInfo));
        }
        execution.setVariable("selectOtherEditorsList", enumList);
    }

    @Autowired
    public PrepareAssignAnotherEditorService(UserService userService) {
        this.userService = userService;
    }
}