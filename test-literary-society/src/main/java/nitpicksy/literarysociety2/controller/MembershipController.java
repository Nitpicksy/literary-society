package nitpicksy.literarysociety2.controller;

import nitpicksy.literarysociety2.dto.response.MembershipDTO;
import nitpicksy.literarysociety2.mapper.MembershipDtoMapper;
import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.service.MembershipService;
import nitpicksy.literarysociety2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/memberships")
public class MembershipController {

    private MembershipService membershipService;
    private MembershipDtoMapper membershipDtoMapper;
    private UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MembershipDTO> getMembership() {
        User authenticated = userService.getAuthenticatedUser();
        return new ResponseEntity<>(membershipDtoMapper.toDto(membershipService.findLatestUserMembership(authenticated.getUserId())), HttpStatus.OK);
    }

    @Autowired
    public MembershipController(MembershipService membershipService, MembershipDtoMapper membershipDtoMapper, UserService userService) {
        this.membershipService = membershipService;
        this.membershipDtoMapper = membershipDtoMapper;
        this.userService = userService;
    }
}
