package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.OpinionOfBetaReader;
import nitpicksy.literarysociety.model.OpinionOfEditor;

import java.util.List;

public interface OpinionOfEditorService {

    OpinionOfEditor findNewestByBookId(Long bookId);

}
