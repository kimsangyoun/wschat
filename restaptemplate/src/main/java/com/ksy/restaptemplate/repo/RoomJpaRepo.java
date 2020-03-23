package com.ksy.restaptemplate.repo;

import com.ksy.restaptemplate.entity.Room;
import com.ksy.restaptemplate.entity.Test;
import com.ksy.restaptemplate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomJpaRepo extends JpaRepository<Room, Long> {
	Optional<Room> findById(int id);
}
