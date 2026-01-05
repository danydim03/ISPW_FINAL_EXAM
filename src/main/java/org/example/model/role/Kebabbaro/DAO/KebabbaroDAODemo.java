// java
    package org.example.model.role.Kebabbaro.DAO;

    import org.example.exceptions.DAOException;
    import org.example.model.role.Kebabbaro.Kebabbaro;
    import org.example.model.user.User;

    public class KebabbaroDAODemo implements KebabbaroDAOInterface {

        @Override
        public Kebabbaro getKebabbaroByUser(User user) throws DAOException {
            // Return a mock Kebabbaro wrapping the user
            // Constructor: Kebabbaro(User user, List<String> signatureDishes, int
            // maxOrdersPerHour)
            return new Kebabbaro(user, new java.util.ArrayList<>(), 50);
        }

        @Override
        public void insert(Kebabbaro kebabbaro) {
            // Metodo intenzionalmente vuoto:
            // Questa è una DAO di demo/mock usata per testing o sviluppo senza persistenza.
            // L'operazione di insert è un no-op per evitare effetti collaterali e mantenere
            // comportamento prevedibile durante i test. Implementare nella DAO reale se necessario.
        }

        @Override
        public void delete(Kebabbaro kebabbaro) {
            // Metodo intenzionalmente vuoto:
            // Come per insert, delete è lasciato vuoto perché questa classe è solo una
            // simulazione; non deve cancellare dati reali durante lo sviluppo/test.
        }

        @Override
        public void update(Kebabbaro kebabbaro) {
            // Metodo intenzionalmente vuoto:
            // L'update non modifica lo stato nella demo per mantenere isolamento dal layer di persistenza.
        }
    }