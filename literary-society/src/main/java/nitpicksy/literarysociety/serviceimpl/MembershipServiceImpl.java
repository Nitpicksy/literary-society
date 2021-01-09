package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.constants.RoleConstants;
import nitpicksy.literarysociety.model.Membership;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.PriceList;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.repository.MembershipRepository;
import nitpicksy.literarysociety.service.MembershipService;
import nitpicksy.literarysociety.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MembershipServiceImpl implements MembershipService {

    private MembershipRepository membershipRepository;
    private PriceListService priceListService;

    @Override
    public Membership findLatestUserMembership(Long id) {
        Membership subscription = membershipRepository.findByUserIdAndExpirationDateIsNullAndIsSubscribedIsTrue(id);
        if (subscription != null) {
            return subscription;
        }

        return membershipRepository.findByUserIdAndExpirationDateGreaterThanEqual(id, LocalDate.now());
    }

    @Override
    public Membership findUserSubscription(Long id) {
        return membershipRepository.findByUserIdAndExpirationDateIsNullAndIsSubscribedIsTrue(id);
    }

    @Override
    public boolean checkIfUserMembershipIsValid(Long id) {
        return findLatestUserMembership(id) != null;
    }

    @Override
    public Membership createMembership(User user, Merchant merchant) {
        PriceList priceList = priceListService.findLatestPriceList();
        Double amount;
        int months;

        if (user.getRole().getName().equals(RoleConstants.ROLE_READER)) {
            amount = priceList.getMembershipForReader();
            months = 1;
        } else {
            amount = priceList.getMembershipForWriter();
            months = 3;
        }

        Membership membership = new Membership(user, amount, LocalDate.now().plusMonths(months), false, merchant);
        return membershipRepository.save(membership);
    }

    @Override
    public Membership createSubscriptionMembership(User user, Merchant merchant) {
        PriceList priceList = priceListService.findLatestPriceList();
        Double amount;

        if (user.getRole().getName().equals(RoleConstants.ROLE_READER)) {
            amount = priceList.getMembershipForReader();
        } else {
            amount = priceList.getMembershipForWriter();
        }

        Membership membership = new Membership(user, amount, null, true, merchant);
        return membershipRepository.save(membership);
    }

    @Autowired
    public MembershipServiceImpl(MembershipRepository membershipRepository, PriceListService priceListService) {
        this.membershipRepository = membershipRepository;
        this.priceListService = priceListService;
    }
}
