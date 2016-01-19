package no.bouvet.sandvika.myfriends.rest.repository;

import no.bouvet.sandvika.myfriends.rest.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by sondre.engell on 19.01.2016.
 */


@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    List<User> findByLastName(@Param("lastName") String lastName);

}
