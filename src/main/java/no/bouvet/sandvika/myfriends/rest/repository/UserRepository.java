package no.bouvet.sandvika.myfriends.rest.repository;

import no.bouvet.sandvika.myfriends.rest.model.User;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by sondre.engell on 19.01.2016.
 */

@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findByLastName(@Param("lastName") String lastName);

    User findByUserName(@Param("userName") String userName);

    List<User> findByPositionNear(@Param("position") Point position, @Param("distance") Distance distance);

}
