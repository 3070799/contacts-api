package com.ohol.pavel.contactsapi.user.auth.controller;

import com.ohol.pavel.contactsapi.security.CurrentUser;
import com.ohol.pavel.contactsapi.security.CustomUserDetails;
import com.ohol.pavel.contactsapi.user.auth.service.AuthService;
import com.ohol.pavel.contactsapi.user.dto.AuthDTO;
import com.ohol.pavel.contactsapi.user.dto.LoginRequest;
import com.ohol.pavel.contactsapi.user.dto.RegisterRequest;
import com.ohol.pavel.contactsapi.user.dto.UserDTO;
import com.ohol.pavel.contactsapi.user.service.UserService;
import com.ohol.pavel.contactsapi.rest.RestConstants;
import com.ohol.pavel.contactsapi.user.model.UserRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * @author Pavel Ohol
 */
@RestController
@CrossOrigin
@RequestMapping(
        produces = "application/json;charset=UTF-8")
@AllArgsConstructor
@Api(tags = "auth-controller")
public class DefaultAuthController implements AuthController {

    private final ConversionService theConversionService;

    private final UserService theUserService;

    private final AuthService theAuthService;

    @Override
    @PostMapping(path = RestConstants.Auth.REGISTER_USER)
    @ApiOperation("Register new user.")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest request) {
        return new ResponseEntity<>(theConversionService.convert(
                theUserService.register(request, UserRole.USER), UserDTO.class
        ), CREATED);
    }

    @Override
    @PostMapping(path = RestConstants.Auth.LOGIN)
    @ApiOperation("Login.")
    public ResponseEntity<AuthDTO> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(theAuthService.login(request));
    }

    @Override
    @PostMapping(path = RestConstants.Auth.LOGOUT)
    @ApiOperation("Logout.")
    public void logout(
            @CurrentUser CustomUserDetails userDetails,
            HttpServletRequest request) {
        theAuthService.logout(userDetails.getUsername(), request);
    }

    @Override
    @PostMapping(path = RestConstants.Auth.CHECK)
    @ApiOperation("Check.")
    public ResponseEntity<AuthDTO> check(
            @CurrentUser CustomUserDetails userDetails,
            HttpServletRequest request) {
        return ResponseEntity.ok(theAuthService.check(userDetails.getUsername(), request));
    }
}
