package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.constants.RoleConstants;
import nitpicksy.literarysociety.dto.UserDTO;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.exceptionHandler.InvalidTokenException;
import nitpicksy.literarysociety.mapper.UserMapper;
import nitpicksy.literarysociety.model.Log;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.security.NoSuchAlgorithmException;

@Validated
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;

    private UserMapper userMapper;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserDTO userDTO, @RequestParam @Pattern(regexp = "(?i)(lecturers|editors)$", message = "Type is not valid.")  String type) throws NoSuchAlgorithmException {
        if(!userDTO.getPassword().equals(userDTO.getRepeatedPassword())){
            throw new InvalidDataException("Password and repeated password don't match",HttpStatus.BAD_REQUEST);
        }
        User user = userMapper.toEntity(userDTO);
        if(type.equals("lecturers")){
            user.setRole(userService.findRoleByName(RoleConstants.ROLE_LECTURER));
        }else if(type.equals("editors")){
            user.setRole(userService.findRoleByName(RoleConstants.ROLE_EDITOR));
        }else{
            throw new InvalidDataException("You can sign up as a lecturer or as an editor",HttpStatus.BAD_REQUEST);
        }
        userService.signUp(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public UserController(UserService userService,UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }
}
