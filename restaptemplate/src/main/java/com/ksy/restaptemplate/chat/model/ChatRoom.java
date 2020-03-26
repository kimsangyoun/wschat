package com.ksy.restaptemplate.chat.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ksy.restaptemplate.entity.Room;
import com.ksy.restaptemplate.entity.Room.RoomBuilder;

@Builder
@Getter
@Setter
public class ChatRoom implements Serializable {
	 
    private static final long serialVersionUID = 6494678977089006639L;
 
    private String roomId;
    private String name;
    private long userCount; // 채팅방 인원수

}
