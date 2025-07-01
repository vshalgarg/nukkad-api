
package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository  extends JpaRepository<RatingEntity, Long> {
}
