package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.model.OpinionOfEditor;

public interface OpinionOfEditorService {

    OpinionOfEditor findNewestByBookId(Long bookId);

}
