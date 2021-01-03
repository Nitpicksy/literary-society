package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Membership;
import nitpicksy.literarysociety.model.User;

public interface MembershipService {

    Membership findLatestUserMembership(User user);

    boolean checkIfUserMembershipIsValid(Long id);
}
