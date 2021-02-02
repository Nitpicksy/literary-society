package nitpicksy.literarysociety2.serviceimpl;

import nitpicksy.literarysociety2.constants.RoleConstants;
import nitpicksy.literarysociety2.model.Log;
import nitpicksy.literarysociety2.model.Membership;
import nitpicksy.literarysociety2.model.Merchant;
import nitpicksy.literarysociety2.model.PriceList;
import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.repository.MembershipRepository;
import nitpicksy.literarysociety2.service.LogService;
import nitpicksy.literarysociety2.service.MembershipService;
import nitpicksy.literarysociety2.service.PriceListService;
import nitpicksy.literarysociety2.utils.IPAddressProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MembershipServiceImpl implements MembershipService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private MembershipRepository membershipRepository;
    private PriceListService priceListService;

    private LogService logService;

    private IPAddressProvider ipAddressProvider;

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
        Membership savedMembership = membershipRepository.save(membership);
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ADDMEMB",
                String.format("Membership %s successfully created from IP address %s",savedMembership.getId(), ipAddressProvider.get())));
        return savedMembership;
    }

    @Override
    public Membership createSubscriptionMembership(User user, Merchant merchant, String subscriptionId) {
        PriceList priceList = priceListService.findLatestPriceList();
        Double amount;

        if (user.getRole().getName().equals(RoleConstants.ROLE_READER)) {
            amount = priceList.getMembershipForReader();
        } else {
            amount = priceList.getMembershipForWriter();
        }

        Membership membership = new Membership(null, user, amount, null, true, subscriptionId, merchant);
        Membership savedMembership = membershipRepository.save(membership);
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ADDMEMB",
                String.format("Membership %s successfully created from IP address %s",savedMembership.getId(), ipAddressProvider.get())));
        return savedMembership;
    }

    @Override
    public void deleteSubscriptionMembership(Long id) {
        membershipRepository.deleteById(id);
    }

    @Autowired
    public MembershipServiceImpl(MembershipRepository membershipRepository, PriceListService priceListService,
                                 LogService logService, IPAddressProvider ipAddressProvider) {
        this.membershipRepository = membershipRepository;
        this.priceListService = priceListService;
        this.logService = logService;
        this.ipAddressProvider = ipAddressProvider;
    }
}
