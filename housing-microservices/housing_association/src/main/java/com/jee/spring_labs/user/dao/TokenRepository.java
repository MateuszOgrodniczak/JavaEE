package com.jee.spring_labs.user.dao;

import com.jee.spring_labs.user.model.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface TokenRepository extends JpaRepository<ActivationToken, Long> {
}
