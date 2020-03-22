package com.ksy.restaptemplate.repo;

import com.ksy.restaptemplate.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestJpaRepo extends JpaRepository<Test, Long> {

}
