package org.example.use_cases.storico_ordini;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.model.ordine.Ordine;
import org.example.model.ordine.OrdineDAOInterface;
import org.example.model.user.User;

import java.util.List;

public class StoricoOrdiniController {

    public List<Ordine> getOrdiniByCliente(User cliente) throws Exception {
        OrdineDAOInterface dao = DAOFactoryAbstract.getInstance().getOrdineDAO();
        // Assuming database ID is used as identifier for orders
        return dao.getOrdiniByCliente(cliente.getId());
    }
}
