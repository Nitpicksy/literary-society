package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.model.Membership;
import nitpicksy.literarysociety2.model.Merchant;
import nitpicksy.literarysociety2.model.User;

public interface MembershipService {

    Membership findLatestUserMembership(Long id);

    Membership findUserSubscription(Long id);

    boolean checkIfUserMembershipIsValid(Long id);

    Membership createMembership(User user, Merchant merchant);

    Membership createSubscriptionMembership(User user, Merchant merchant, String subscriptionId);

    void deleteSubscriptionMembership(Long id);
}
