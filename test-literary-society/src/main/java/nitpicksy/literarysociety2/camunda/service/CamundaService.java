package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.dto.camunda.EnumKeyValueDTO;
import nitpicksy.literarysociety2.dto.response.FormFieldsDTO;
import nitpicksy.literarysociety2.dto.response.ProcessDataDTO;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.FormFieldImpl;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CamundaService {

    private RuntimeService runtimeService;

    private TaskService taskService;

    private FormService formService;

    public ProcessDataDTO start(String processId) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processId);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        return new ProcessDataDTO(pi.getId(), task.getId());
    }

    public FormFieldsDTO getFormFields(String piId, String taskId) {
        TaskFormData tfd = formService.getTaskFormData(taskId);
        List<FormField> properties = tfd.getFormFields();

        return new FormFieldsDTO(piId, taskId, properties);
    }

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

    public void findAndCompleteActiveTask(String processInstanceId) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
        taskService.complete(task.getId());
    }

    public void completeTask(String taskId) {
        taskService.complete(taskId);
    }

    public List<Long> extractIds(String selectedIdsString) {
        List<Long> ids = new ArrayList<>();
        String[] selectedIdsArray = selectedIdsString.split(",");
        for (String idStr : selectedIdsArray) {
            if (idStr.contains("_")) {
                Long id = Long.valueOf(idStr.split("_")[1]);
                ids.add(id);
            }
        }

        return ids;
    }

    public Long extractId(String selectedIdString) {
        Long id = null;
        if (selectedIdString.contains("_")) {
            id = Long.valueOf(selectedIdString.split("_")[1]);
        }

        return id;
    }

    public String extractValue(String selectedValueString) {
        String value = null;
        if (selectedValueString.contains("_")) {
            value = selectedValueString.split("_")[1];
        }

        return value;
    }

    public List<TaskDto> getTasksByAssignee(String userId) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(userId)
                .orderByDueDate().asc()
                .list();
        return tasks.stream().map(TaskDto::fromEntity).collect(Collectors.toList());
    }

    public String getProcessVariable(String piId, String name) {
        return (String) runtimeService.getVariable(piId, name);
    }

    public void setProcessVariable(String piId, String variable, Object value) {
        runtimeService.setVariable(piId, variable, value);
    }

    public void messageEventReceived(String messageName, String user) {
        Execution execution = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName(messageName)
                .processVariableValueEquals("user", user)
                .singleResult();
        runtimeService.messageEventReceived(messageName, execution.getId());
    }

    @Autowired
    public CamundaService(RuntimeService runtimeService, TaskService taskService, FormService formService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.formService = formService;
    }

}
