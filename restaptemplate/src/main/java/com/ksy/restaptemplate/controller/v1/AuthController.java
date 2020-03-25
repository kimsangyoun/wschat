package com.ksy.restaptemplate.controller.v1;

import com.ksy.restaptemplate.advice.exception.CEmailSigninFailedException;
import com.ksy.restaptemplate.advice.exception.CUserNotFoundException;
import com.ksy.restaptemplate.config.security.JwtTokenProvider;
import com.ksy.restaptemplate.entity.User;
import com.ksy.restaptemplate.model.response.SingleResult;
import com.ksy.restaptemplate.model.user.ParamsUser;
import com.ksy.restaptemplate.repo.UserJpaRepo;
import com.ksy.restaptemplate.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public SingleResult<String> signin(@Valid @RequestBody ParamsUser _user) {

        User user = userJpaRepo.findByEmail(_user.getEmail()).orElseThrow(CEmailSigninFailedException::new);

        if (!passwordEncoder.matches(_user.getPassword(), user.getPassword()))
            throw new CEmailSigninFailedException();
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles()));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "Me", notes = "token을 받아 token의 주인인 user를 return합니다.")
    @GetMapping(value = "/me")
    public SingleResult<User> me(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String uid = jwtTokenProvider.getUserId(token);
        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(userJpaRepo.findById(Long.parseLong(uid)).orElseThrow(CUserNotFoundException::new));
    }
}