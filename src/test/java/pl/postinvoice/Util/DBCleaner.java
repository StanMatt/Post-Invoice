package pl.postinvoice.Util;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DBCleaner {

    private final EntityManager entityManager;
    @Transactional
    void clean() {
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        List<String> tables = ((List<Object[]>) entityManager.createNativeQuery("SHOW tables").getResultList())
                .stream()
                .map(el -> el[0].toString())
                .toList();

        tables.forEach(table -> entityManager.createNativeQuery(String.format("DELETE FROM %s", table)).executeUpdate());
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();




    }
}
