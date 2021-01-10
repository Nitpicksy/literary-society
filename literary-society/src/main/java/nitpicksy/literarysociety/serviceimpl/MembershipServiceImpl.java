package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.constants.RoleConstants;
import nitpicksy.literarysociety.model.Log;
import nitpicksy.literarysociety.model.Membership;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.PriceList;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.repository.MembershipRepository;
import nitpicksy.literarysociety.service.LogService;
import nitpicksy.literarysociety.service.MembershipService;
import nitpicksy.literarysociety.service.PriceListService;
import nitpicksy.literarysociety.utils.IPAddressProvider;
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
    public Membership findLatestUserMembership(User user) {
        return membershipRepository.findByUserIdAndExpirationDateGreaterThanEqual(user.getUserId(), LocalDate.now());
    }

    @Override
    public boolean checkIfUserMembershipIsValid(Long id) {
        if (membershipRepository.findByUserIdAndExpirationDateGreaterThanEqual(id, LocalDate.now()) != null) {
            return true;
        }
        return false;
    }

    @Override
    public Membership createMembership(User user, Merchant merchant) {
        PriceList priceList = priceListService.findLatestPriceList();
        Double amount;

        if (user.getRole().getName().equals(RoleConstants.ROLE_READER)) {
            amount = priceList.getMembershipForReader();
        } else {
            amount = priceList.getMembershipForWriter();
        }

        Membership membership = new Membership(user, amount, LocalDate.now().plusMonths(3), false, merchant);
        Membership savedMembership = membershipRepository.save(membership);
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ADDMEMB",
                String.format("Membership %s successfully created from IP address %s",savedMembership.getId(), ipAddressProvider.get())));
        return savedMembership;
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
