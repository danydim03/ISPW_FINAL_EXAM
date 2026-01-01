// java
package org.example.model.role.Kebabbaro;

import org.example.model.role.AbstractRole;
import org.example.model.user.User;

import java.util.Collections;
import java.util.List;

public class Kebabbaro extends AbstractRole {
    private static final String DEFAULT_SPECIALTY = "kebab";

    private final List<String> signatureDishes;
    private final int maxOrdersPerHour;

    public Kebabbaro(User user, List<String> signatureDishes, int maxOrdersPerHour) {
        super(user); // puntatore all'oggetto User
        // lo user viene puntato correttamente tramite puntatori e non tramite
        // l'utilizzo di chiavi esterne come nel modello relazionale.
        this.signatureDishes = signatureDishes == null ? Collections.emptyList() : List.copyOf(signatureDishes);
        this.maxOrdersPerHour = Math.max(0, maxOrdersPerHour);
    }

    public String getSpecialty() {
        return DEFAULT_SPECIALTY;
    }

    public List<String> getSignatureDishes() {
        return signatureDishes;
    }

    public int getMaxOrdersPerHour() {
        return maxOrdersPerHour;
    }

    public String describeRole() {
        String name = (getUser() != null && getUser().getName() != null) ? getUser().getName() : "Unknown";
        return String.format("%s lavora con %s e può gestire fino a %d ordini/ora",
                name, signatureDishes, maxOrdersPerHour);
    }

    @Override
    public Kebabbaro getKebabbaroRole() {
        return this;
    }
}
