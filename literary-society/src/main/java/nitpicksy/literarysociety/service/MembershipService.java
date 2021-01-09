package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Membership;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.User;

public interface MembershipService {

    Membership findLatestUserMembership(Long id);

    Membership findUserSubscription(Long id);

    boolean checkIfUserMembershipIsValid(Long id);

    Membership createMembership(User user, Merchant merchant);

    Membership createSubscriptionMembership(User user, Merchant merchant);
}
