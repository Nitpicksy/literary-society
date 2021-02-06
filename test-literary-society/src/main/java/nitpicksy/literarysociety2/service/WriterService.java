package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.model.Writer;

public interface WriterService {

    Writer findByUsername(String username);

    Writer findByEmail(String email);

    Writer findById(Long id);

}
