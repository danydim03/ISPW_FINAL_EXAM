package org.example.model.role.Amministratore;

import org.example.model.role.AbstractRole;
import org.example.model.user.User;

public class Amministratore extends AbstractRole {
    private final String department;
    private final int accessLevel;

    public Amministratore(User user) {
        this(user, "Generale", 3);
    }

    public Amministratore(User user, String department, int accessLevel) {
        super(user); // puntatore all'oggetto User
        // lo user viene puntato correttamente tramite puntatori e non tramite
        // l'utilizzo di chiavi esterne come nel modello relazionale.
        this.department = department != null ? department : "Generale";
        this.accessLevel = Math.max(1, Math.min(5, accessLevel));
    }

    public String getDepartment() {
        return department;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    @Override
    public Amministratore getAmministratoreRole() {
        return this;
    }
}
