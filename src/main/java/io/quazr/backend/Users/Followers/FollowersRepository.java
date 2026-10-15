package io.quazr.backend.Users.Followers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowersRepository extends JpaRepository<FollowerEntity, FollowerCompositeKey> {
    
}