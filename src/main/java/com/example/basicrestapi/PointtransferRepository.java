package com.example.basicrestapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointtransferRepository extends JpaRepository<Pointtransfer, Long> {
	List<Pointtransfer> findAll();

	@Query("SELECT p FROM Pointtransfer p WHERE p.userId=:userId ORDER BY p.timestamp")
	List<Pointtransfer>  findByUserId(@Param("userId") long userId);

	Pointtransfer save(Pointtransfer pointTransfer);
}
