package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety2.model.Reader;
import nitpicksy.literarysociety2.service.ReaderService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SetChosenBetaReadersService implements JavaDelegate {

    private CamundaService camundaService;

    private ReaderService readerService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));
        List<Long> betaReaderIds = camundaService.extractIds(map.get("selectBetaReader"));

        List<Reader> betaReaders = readerService.findByIds(betaReaderIds);
        List<String> betaReaderUsernames = betaReaders.stream().map(Reader::getUsername).collect(Collectors.toList());

        execution.setVariable("chosenBetaReaders", betaReaderUsernames);
        System.out.println("Chosen Beta-readers: " + betaReaderUsernames);
    }

    @Autowired
    public SetChosenBetaReadersService(CamundaService camundaService, ReaderService readerService) {
        this.camundaService = camundaService;
        this.readerService = readerService;
    }

}
