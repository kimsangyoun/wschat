package com.ksy.restaptemplate.advice;

import com.ksy.restaptemplate.advice.exception.CAuthenticationEntryPointException;
import com.ksy.restaptemplate.advice.exception.CEmailSigninFailedException;
import com.ksy.restaptemplate.advice.exception.CEmailSignupFailedException;
import com.ksy.restaptemplate.advice.exception.CTestNotFoundException;
import com.ksy.restaptemplate.advice.exception.CUserNotFoundException;
import com.ksy.restaptemplate.model.response.CommonResult;
import com.ksy.restaptemplate.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.ksy.restaptemplate")
public class ExceptionAdvice {
    private final ResponseService responseService;
    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        // �삁�쇅 泥섎━�쓽 硫붿떆吏�瑜� MessageSource�뿉�꽌 媛��졇�삤�룄濡� �닔�젙
        e.printStackTrace();
        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.msg"));
    }

    @ExceptionHandler(CTestNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult testNotFoundException(HttpServletRequest request, CTestNotFoundException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("testNotFound.code")), getMessage("testNotFound.msg"));
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
    }

    @ExceptionHandler(CEmailSigninFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult emailSigninFailedException(HttpServletRequest request, CEmailSigninFailedException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("emailSigninFailed.code")), getMessage("emailSigninFailed.msg"));
    }

    @ExceptionHandler(CEmailSignupFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult emailSignupFailedException(HttpServletRequest request, CEmailSignupFailedException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("emailSignupFailed.code")), getMessage("emailSignupFailed.msg"));
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    public CommonResult authenticationEntryPointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("entryPointException.code")), getMessage("entryPointException.msg"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public CommonResult AccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("accessDenied.code")), getMessage("accessDenied.msg"));
    }

    // code�젙蹂댁뿉 �빐�떦�븯�뒗 硫붿떆吏�瑜� 議고쉶�빀�땲�떎.
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code�젙蹂�, 異붽� argument濡� �쁽�옱 locale�뿉 留욌뒗 硫붿떆吏�瑜� 議고쉶�빀�땲�떎.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}