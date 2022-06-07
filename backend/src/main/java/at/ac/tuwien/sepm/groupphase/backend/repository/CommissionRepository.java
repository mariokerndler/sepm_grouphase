package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommissionRepository extends JpaRepository<Commission, Long> {
    List<Commission> findCommissionsByArtist(Long id);

    @Query(nativeQuery = true, value = "SELECT  * FROM COMMISSION WHERE LOWER(TITLE) LIKE :searchName  AND  (CUSTOMER_ID LIKE :userId OR (CUSTOMER_ID IS NULL AND LENGTH(:userId)<3 )) AND  (ARTIST_ID LIKE :artistId OR (ARTIST_ID IS NULL AND LENGTH(:artistId)<3 )) AND PRICE > :priceLower AND PRICE <:priceUpper")
    List<Commission> searchCommissions(@Param("searchName") String searchName, @Param("priceLower") String priceLower,
                                       @Param("priceUpper") String artistId,
                                       @Param("userId") String userId,
                                       @Param("artistId") String priceUpper);

    @Query(nativeQuery = true, value = "SELECT  * FROM COMMISSION WHERE LOWER(TITLE) LIKE :searchName AND  (CUSTOMER_ID LIKE :userId OR (CUSTOMER_ID IS NULL AND LENGTH(:userId)<3 )) AND   (ARTIST_ID LIKE :artistId OR (ARTIST_ID IS NULL AND LENGTH(:artistId)<3 )) AND PRICE > :priceLower AND PRICE <:priceUpper ORDER BY :date")
    List<Commission> searchCommissionsDate(@Param("searchName") String searchName, @Param("priceLower") String priceLower,
                                           @Param("priceUpper") String priceUpper,
                                           @Param("artistId") String artistId,
                                           @Param("userId") String userId,
                                           @Param("date") String date);

}