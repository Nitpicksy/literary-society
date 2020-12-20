package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Writer;

public interface WriterService {

    Writer findByUsername(String username);

    Writer findByEmail(String email);

    Writer findById(Long id);

}
