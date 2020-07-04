package iam.davidmanangan.bankcli.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iam.davidmanangan.bankcli.model.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Integer>{ }
