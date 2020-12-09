package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.dto.response.FormFieldsDTO;
import nitpicksy.literarysociety.service.CamundaService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    public CamundaServiceImpl(RuntimeService runtimeService, TaskService taskService, FormService formService, RepositoryService repositoryService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.formService = formService;
        this.repositoryService = repositoryService;
    }

}
