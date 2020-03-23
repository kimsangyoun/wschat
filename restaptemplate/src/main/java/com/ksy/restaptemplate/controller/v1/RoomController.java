package com.ksy.restaptemplate.controller.v1;

import java.util.Collections;

import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ksy.restaptemplate.advice.exception.CEmailSignupFailedException;
import com.ksy.restaptemplate.advice.exception.CUserNotFoundException;
import com.ksy.restaptemplate.entity.Room;
import com.ksy.restaptemplate.entity.User;
import com.ksy.restaptemplate.model.response.CommonResult;
import com.ksy.restaptemplate.model.response.ListResult;
import com.ksy.restaptemplate.model.response.SingleResult;
import com.ksy.restaptemplate.model.user.ParamsRoom;
import com.ksy.restaptemplate.model.user.ParamsUser;
import com.ksy.restaptemplate.repo.RoomJpaRepo;
import com.ksy.restaptemplate.repo.UserJpaRepo;
import com.ksy.restaptemplate.service.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = {"3. Room"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class RoomController {

	private final RoomJpaRepo roomJpaRepo;
	private final ResponseService responseService; // ����� ó���� Service
	private final PasswordEncoder passwordEncoder;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "�α��� ���� �� access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "�� ����Ʈ ��ȸ", notes = "��� ä�ù��� ��ȸ�Ѵ�")
	@GetMapping(value = "/rooms")
	public ListResult<Room> findAllRoom() {
		// ��������Ͱ� �������ΰ�� getListResult�� �̿��ؼ� ����� ����Ѵ�.
		return responseService.getListResult(roomJpaRepo.findAll());
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "�α��� ���� �� access_token", required = false, dataType = "String", paramType = "header") })
	@ApiOperation(value = "ä�ù� �ܰ� ��ȸ", notes = "���ȣ(id)�� ä�ù��� ��ȸ�Ѵ�")
	@GetMapping(value = "/room/{msrl}")
	public SingleResult<Room> findRoomById(@ApiParam(value = "���", defaultValue = "ko") @RequestParam String lang , @PathVariable int id) {
		// SecurityContext���� �������� ȸ���� ������ ���´�.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// ��������Ͱ� ���ϰ��ΰ�� getSingleResult�� �̿��ؼ� ����� ����Ѵ�.
		return responseService.getSingleResult(roomJpaRepo.findById(id).orElseThrow(CUserNotFoundException::new));
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "�α��� ���� �� access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "ä�ù� ����", notes = "�������� �����Ѵ�")
	@PutMapping(value = "/room")
	public SingleResult<Room> modify(@ApiParam(value = "���ȣ", required = true) @RequestParam int id,
			@ApiParam(value = "���̸�", required = true) @RequestParam String name) {
		Room room = Room.builder().id(id).name(name).build();
		return responseService.getSingleResult(roomJpaRepo.save(room));
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "�α��� ���� �� access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "�� ����", notes = "roomId�� ȸ�������� �����Ѵ�")
	@DeleteMapping(value = "/room/{id}")
	public CommonResult delete(@ApiParam(value = "���ȣ", required = true) @PathVariable int id) {
		roomJpaRepo.deleteById((long) id);
		// ���� ��� ������ �ʿ��Ѱ�� getSuccessResult()�� �̿��Ͽ� ����� ����Ѵ�.
		return responseService.getSuccessResult();
	}
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "�α��� ���� �� access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "�����", notes = "�Է� ���� name �� ������ ���� ���� �Ѵ�.")
	@PostMapping(value = "/room")
	public CommonResult create(@Valid @RequestBody ParamsRoom room) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String userid = authentication.getName();
		roomJpaRepo.save(Room.builder().name(room.getName()).reg_by(userid).build());
		// ���� ��� ������ �ʿ��Ѱ�� getSuccessResult()�� �̿��Ͽ� ����� ����Ѵ�.
		return responseService.getSuccessResult();
	}
}
