package com.ksy.restaptemplate.controller.v1;

import com.ksy.restaptemplate.advice.exception.CTestNotFoundException;
import com.ksy.restaptemplate.entity.Test;
import com.ksy.restaptemplate.model.response.CommonResult;
import com.ksy.restaptemplate.model.response.ListResult;
import com.ksy.restaptemplate.model.response.SingleResult;
import com.ksy.restaptemplate.repo.TestJpaRepo;
import com.ksy.restaptemplate.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"1. Test"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class TestController {

    private final TestJpaRepo testJpaRepo;
    private final ResponseService responseService; // 결과를 처리할 Service

    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
    @GetMapping(value = "/tests")
    public ListResult<Test> findAllTest() {
        // 결과데이터가 여러건인경우 getListResult를 이용해서 결과를 출력한다.
        return responseService.getListResult(testJpaRepo.findAll());
    }

    @ApiOperation(value = "테스트 단건 조회", notes = "testId로 테스트를 조회한다")
    @GetMapping(value = "/test/{testid}")
    public SingleResult<Test> findUserById(@ApiParam(value = "테스트ID", required = true) @PathVariable long testid, @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
        // 결과데이터가 단일건인경우 getBasicResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(testJpaRepo.findById(testid).orElseThrow(CTestNotFoundException::new));
    }

    @ApiOperation(value = "테스트 입력", notes = "테스트를 입력한다")
    @PostMapping(value = "/test")
    public SingleResult<Test> save(@ApiParam(value = "테스트아이디", required = true) @RequestParam String tid,
                                   @ApiParam(value = "테스트이름", required = true) @RequestParam String name) {
        Test test = Test.builder()
                .tid(tid)
                .name(name)
                .build();
        return responseService.getSingleResult(testJpaRepo.save(test));
    }

    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @PutMapping(value = "/test")
    public SingleResult<Test> modify(
            @ApiParam(value = "테스트번호", required = true) @RequestParam long msrl,
            @ApiParam(value = "테스트아이디", required = true) @RequestParam String uid,
            @ApiParam(value = "테스트이름", required = true) @RequestParam String name) {
        Test test = Test.builder()
                .tseq(msrl)
                .tid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(testJpaRepo.save(test));
    }

    @ApiOperation(value = "회원 삭제", notes = "userId로 회원정보를 삭제한다")
    @DeleteMapping(value = "/test/{msrl}")
    public CommonResult delete(
            @ApiParam(value = "테스트번호", required = true) @PathVariable long msrl) {
        testJpaRepo.deleteById(msrl);
        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }
}