package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.Membership;
import nitpicksy.literarysociety.repository.MembershipRepository;
import nitpicksy.literarysociety.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipServiceImpl implements MembershipService {

    private MembershipRepository membershipRepository;

    @Override
    public Membership findByUserId(Long id) {
        return membershipRepository.findByUserId(id);
    }

    @Autowired
    public MembershipServiceImpl(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }
}
