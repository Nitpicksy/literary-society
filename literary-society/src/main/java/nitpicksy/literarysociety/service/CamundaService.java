package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.dto.response.FormFieldsDTO;

public interface CamundaService {

    FormFieldsDTO start(String processId);

    FormFieldsDTO setEnumValues(FormFieldsDTO formFieldsDTO);

    void complete(String taskId);
}
