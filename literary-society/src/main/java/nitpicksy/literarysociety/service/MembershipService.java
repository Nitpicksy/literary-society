package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Membership;

public interface MembershipService {

    Membership findByUserId(Long id);

}
