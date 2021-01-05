package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.service.MembershipService;
import nitpicksy.literarysociety.service.MerchantService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipPaymentService implements JavaDelegate {

    private UserService userService;
    private MembershipService membershipService;
    private MerchantService merchantService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String username = (String) execution.getVariable("user");
        User user = userService.findByUsername(username);

        if (!membershipService.checkIfUserMembershipIsValid(user.getUserId())) {
            membershipService.createMembership(user, merchantService.findOurMerchant());
        }
    }

    @Autowired
    public MembershipPaymentService(UserService userService, MembershipService membershipService, MerchantService merchantService) {
        this.userService = userService;
        this.membershipService = membershipService;
        this.merchantService = merchantService;
    }
}
