package de.holarse.backend.db.repositories;

import de.holarse.backend.db.FrontPageItem;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface FrontPageRepository extends CrudRepository<FrontPageItem, Long> {

    @Query(value = "SELECT f.* from fpitems f where (f.publishfrom is null or f.publishfrom >= now()) and (f.publishuntil is null or f.publishuntil <= now()) order by created", 
           nativeQuery = true)
    List<FrontPageItem> getFrontScreen();
    
}