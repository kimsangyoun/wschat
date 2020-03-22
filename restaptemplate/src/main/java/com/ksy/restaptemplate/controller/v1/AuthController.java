package com.ksy.restaptemplate.controller.v1;

import com.ksy.restaptemplate.advice.exception.CEmailSigninFailedException;
import com.ksy.restaptemplate.config.security.JwtTokenProvider;
import com.ksy.restaptemplate.entity.User;
import com.ksy.restaptemplate.model.response.SingleResult;
import com.ksy.restaptemplate.repo.UserJpaRepo;
import com.ksy.restaptemplate.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/auth")
public class AuthController {

    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String id,
                                       @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
        User user = userJpaRepo.findByEmail(id).orElseThrow(CEmailSigninFailedException::new);
        System.out.println("user" + user.getEmail());
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CEmailSigninFailedException();

        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles()));

    }


}