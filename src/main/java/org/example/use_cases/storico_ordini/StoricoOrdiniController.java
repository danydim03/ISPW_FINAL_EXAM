package org.example.use_cases.storico_ordini;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.model.ordine.Ordine;
import org.example.model.ordine.DAO.OrdineDAOInterface;
import org.example.model.user.User;

import java.util.List;

public class StoricoOrdiniController {

    public List<Ordine> getOrdiniByCliente(User cliente) throws Exception {
        OrdineDAOInterface dao = DAOFactoryAbstract.getInstance().getOrdineDAO();

        // Debug: Print all relevant IDs
        System.out.println("=== DEBUG StoricoOrdiniController ===");
        System.out.println("User.getId(): " + cliente.getId());
        System.out.println("User.getEmail(): " + cliente.getEmail());
        System.out.println("User.getCodiceFiscale(): " + cliente.getCodiceFiscale());
        if (cliente.getRole() != null && cliente.getRole().getClienteRole() != null) {
            System.out.println(
                    "Cliente.getCodiceFiscale() (Role ID): " + cliente.getRole().getClienteRole().getCodiceFiscale());
        } else {
            System.out.println("Cliente Role is NULL!");
        }

        // Use user.getId() directly - from USER table, this should be 'CLI001'
        String searchId = cliente.getId();
        System.out.println("Searching orders with ID: " + searchId);

        List<Ordine> result = dao.getOrdiniByCliente(searchId);
        System.out.println("Number of orders found: " + result.size());
        System.out.println("=== END DEBUG ===");

        return result;
    }
}
