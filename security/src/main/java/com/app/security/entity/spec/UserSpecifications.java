package com.app.security.entity.spec;

import com.app.security.entity.User;
import com.app.security.entity.User_;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    // SPIEGAZIONE METODO
    public static Specification<User> hasUsername( String username) {
        // Il metodo Specification<User> hasUsername(String username) restituisce un oggetto Specification<User>
        return (root, query, criteriaBuilder) -> {
            // Se username è null
            if (username == null) {
                return criteriaBuilder.conjunction(); // Nessuna condizione
            }
            // Ritorna
            return criteriaBuilder.equal(root.get( User_.USERNAME), username);
        };
    }

    public static Specification<User> hasEmail( String email) {
        return (root, query, criteriaBuilder) -> {

            if (email == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get(User_.EMAIL), email);
        };
    }
}
