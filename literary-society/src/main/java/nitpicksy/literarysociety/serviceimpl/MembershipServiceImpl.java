package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.Membership;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.repository.MembershipRepository;
import nitpicksy.literarysociety.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MembershipServiceImpl implements MembershipService {

    private MembershipRepository membershipRepository;

    @Override
    public Membership findByUserId(Long id) {
        return membershipRepository.findByUserId(id);
    }

    @Override
    public boolean checkIfUserMembershipIsValid(Long id) {
        if(membershipRepository.findByUserIdAndExpirationDateGreaterThanEqual(id, LocalDate.now()) != null){
            return true;
        }
        return false;
    }

    @Autowired
    public MembershipServiceImpl(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }
}
