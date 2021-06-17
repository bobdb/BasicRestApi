package com.example.basicrestapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findAll();
	User findById(@Param("id") long id);

	@Transactional
	@Modifying
	@Query("UPDATE User u SET u.points=:points WHERE u.id=:userId")
    void updatePointsById(@Param("userId") long id, @Param("points") long points );
}
