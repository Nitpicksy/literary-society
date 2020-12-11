package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.dto.camunda.EnumKeyValueDTO;
import nitpicksy.literarysociety.dto.response.FormFieldsDTO;
import nitpicksy.literarysociety.model.Genre;
import nitpicksy.literarysociety.service.CamundaService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.FormFieldImpl;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CamundaServiceImpl implements CamundaService {

    private RuntimeService runtimeService;

    private TaskService taskService;

    private FormService formService;

    private RepositoryService repositoryService;

    @Override
    public FormFieldsDTO start(String processId) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processId);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        
        return new FormFieldsDTO(pi.getId(), task.getId(), properties);
    }

    @Override
    public FormFieldsDTO setEnumValues(FormFieldsDTO formFieldsDTO) {
        List<FormField> formFields = new ArrayList<>();
        for (FormField formField : formFieldsDTO.getFormFields()) {
            if (formField.getTypeName().equals("enum")) {
                EnumFormType oldEnumType = (EnumFormType) formField.getType();
                if (oldEnumType.getValues().isEmpty()) {
                    String processVariable = formField.getId() + "List";
                    String processInstanceId = formFieldsDTO.getProcessInstanceId();
                    List<EnumKeyValueDTO> enumList = (List<EnumKeyValueDTO>) runtimeService.getVariable(processInstanceId, processVariable);
                    if (!enumList.isEmpty()) {
                        Map<String, String> enumMap = enumList.stream().collect(Collectors.toMap(EnumKeyValueDTO::getKey, EnumKeyValueDTO::getValue));
                        EnumFormType newEnumType = new EnumFormType(enumMap);
                        FormFieldImpl formFieldImpl = (FormFieldImpl) formField;
                        formFieldImpl.setType(newEnumType);
                        formFields.add(formFieldImpl);
                    } else {
                        formFields.add(formField);
                    }
                } else {
                    formFields.add(formField);
                }
            } else {
                formFields.add(formField);
            }
        }

        if (!formFields.isEmpty()) {
            formFieldsDTO.setFormFields(formFields);
        }

        return formFieldsDTO;
    }

    @Autowired
    public CamundaServiceImpl(RuntimeService runtimeService, TaskService taskService, FormService formService,
                              RepositoryService repositoryService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.formService = formService;
        this.repositoryService = repositoryService;
    }

}
