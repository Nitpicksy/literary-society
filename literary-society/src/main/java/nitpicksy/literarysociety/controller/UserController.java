package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.constants.RoleConstants;
import nitpicksy.literarysociety.dto.request.UserRequestDTO;
import nitpicksy.literarysociety.dto.response.UserResponseDTO;
import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.mapper.UserRequestMapper;
import nitpicksy.literarysociety.mapper.UserResponseMapper;
import nitpicksy.literarysociety.model.Log;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.service.LogService;
import nitpicksy.literarysociety.service.UserService;
import nitpicksy.literarysociety.utils.IPAddressProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;

    private UserRequestMapper userRequestMapper;

    private UserResponseMapper userResponseMapper;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> signUp(@Valid @RequestBody UserRequestDTO userDTO, @RequestParam @Pattern(regexp = "(?i)(lecturers|editors)$", message = "Type is not valid.")  String type) throws NoSuchAlgorithmException {
        if(!userDTO.getPassword().equals(userDTO.getRepeatedPassword())){
            throw new InvalidDataException("Password and repeated password don't match",HttpStatus.BAD_REQUEST);
        }
        User user = userRequestMapper.toEntity(userDTO);
        if(type.equals("lecturers")){
            user.setRole(userService.findRoleByName(RoleConstants.ROLE_LECTURER));
        }else if(type.equals("editors")){
            user.setRole(userService.findRoleByName(RoleConstants.ROLE_EDITOR));
        }else{
            throw new InvalidDataException("You can sign up as a lecturer or as an editor",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userResponseMapper.toDto(userService.signUp(user)), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findLecturersAndEditors() {
        return new ResponseEntity<>(userService.findByRoleNameAndStatusInOrRoleNameAndStatusIn(RoleConstants.ROLE_LECTURER, Arrays.asList(UserStatus.WAITING_APPROVAL,
                UserStatus.ACTIVE), RoleConstants.ROLE_EDITOR,Arrays.asList(UserStatus.WAITING_APPROVAL, UserStatus.ACTIVE)).stream()
                .map(user -> userResponseMapper.toDto(user)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> changeUserStatus(@PathVariable @Positive Long id,
                                                                          @RequestParam @Pattern(regexp = "(?i)(approve|reject)$", message = "Status is not valid.") String status) {
        return new ResponseEntity<>(userResponseMapper.toDto(userService.changeUserStatus(id, status)), HttpStatus.OK);
    }

    @Autowired
    public UserController(UserService userService, UserRequestMapper userRequestMapper,UserResponseMapper userResponseMapper) {
        this.userService = userService;
        this.userRequestMapper = userRequestMapper;
        this.userResponseMapper = userResponseMapper;
    }
}
