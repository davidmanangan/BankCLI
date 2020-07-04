package iam.davidmanangan.bankcli.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iam.davidmanangan.bankcli.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{ }

