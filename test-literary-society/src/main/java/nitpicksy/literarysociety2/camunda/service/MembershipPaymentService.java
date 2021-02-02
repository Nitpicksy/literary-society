package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.service.MembershipService;
import nitpicksy.literarysociety2.service.MerchantService;
import nitpicksy.literarysociety2.service.UserService;
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
