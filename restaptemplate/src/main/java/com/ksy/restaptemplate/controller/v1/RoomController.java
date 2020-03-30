package com.ksy.restaptemplate.controller.v1;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.ksy.restaptemplate.chat.model.ChatRoom;
import com.ksy.restaptemplate.chat.repo.ChatRoomRepository;

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
    private final ResponseService responseService; // 결과를 처리할 Service
    private final ChatRoomRepository chatRoomRepo; // 결과를 처리할 Service
    private final PasswordEncoder passwordEncoder;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")})
    @ApiOperation(value = "룸 리스트 조회", notes = "모든 채팅방을 조회한다")
    @GetMapping(value = "/rooms")
    public ListResult<ChatRoom> findAllRoom() {
    	List<ChatRoom> chatRooms = chatRoomRepo.findAllRoom();
        chatRooms.stream().forEach(room -> room.setUserCount(chatRoomRepo.getUserCount(room.getRoomId())));
        return responseService.getListResult(chatRooms);
        
        // 결과데이터가 여러건인경우 getListResult를 이용해서 결과를 출력한다.
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")})
    @ApiOperation(value = "채팅방 단건 조회", notes = "방번호(id)로 채팅방을 조회한다")
    @GetMapping(value = "/room/{msrl}")
    public SingleResult<Room> findRoomById(@ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang, @PathVariable int id) {
        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(roomJpaRepo.findById(id).orElseThrow(CUserNotFoundException::new));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")})
    @ApiOperation(value = "채팅방 수정", notes = "방정보를 수정한다")
    @PutMapping(value = "/room")
    public SingleResult<Room> modify(@ApiParam(value = "방번호", required = true) @RequestParam String id,
                                     @ApiParam(value = "방이름", required = true) @RequestParam String name) {
        Room room = Room.builder().id(id).name(name).build();
        return responseService.getSingleResult(roomJpaRepo.save(room));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")})
    @ApiOperation(value = "방 삭제", notes = "roomId로 회원정보를 삭제한다")
    @DeleteMapping(value = "/room/{id}")
    public CommonResult delete(@ApiParam(value = "방번호", required = true) @PathVariable int id) {
        roomJpaRepo.deleteById((long) id);
        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")})
    @ApiOperation(value = "방생성", notes = "입력 받은 name 을 가지고 방을 생성 한다.")
    @PostMapping(value = "/room")
    public SingleResult<Room> create(@Valid @RequestBody ParamsRoom _room) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();
        String roomid = UUID.randomUUID().toString();
        Room room= Room.builder().id(roomid).name(_room.getName()).reg_by(userid).build();
        roomJpaRepo.save(room); //디비에 저장하였고.
        chatRoomRepo.createChatRoom(room);

        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSingleResult(room);
    }
}