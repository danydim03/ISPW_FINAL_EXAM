package org.example.model.ordine.DAO;

import org.example.enums.StatoOrdine;
import org.example.exceptions.ObjectNotFoundException;
import org.example.model.ordine.Ordine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class OrdineDAODemo implements OrdineDAOInterface {

    private static final List<Ordine> MOCK_ORDINI = new ArrayList<>();
    private static final String DEMO_CLIENT_ID = "CLI001";
    private static final AtomicLong sequence = new AtomicLong(1);

    static {
        // Pre-populate with some orders for the Demo Client (CLI001)
        try {
            // Order 1: Delivered
            Ordine o1 = new Ordine(DEMO_CLIENT_ID);
            o1.setNumeroOrdine(sequence.getAndIncrement());
            o1.aggiungiProdotto(new org.example.model.food.PaninoDonerKebab());
            o1.setStato(StatoOrdine.CONSEGNATO);
            o1.setDataConferma(java.time.LocalDateTime.now().minusDays(2));
            MOCK_ORDINI.add(o1);

            // Order 2: In Preparation
            Ordine o2 = new Ordine(DEMO_CLIENT_ID);
            o2.setNumeroOrdine(sequence.getAndIncrement());
            o2.aggiungiProdotto(new org.example.model.food.PiadinaDonerKebab());
            o2.aggiungiProdotto(new org.example.model.food.KebabAlPiatto());
            o2.setStato(StatoOrdine.IN_PREPARAZIONE);
            o2.setDataConferma(java.time.LocalDateTime.now().minusMinutes(30));
            MOCK_ORDINI.add(o2);

            // Order 3: Delivered (Older)
            Ordine o3 = new Ordine(DEMO_CLIENT_ID);
            o3.setNumeroOrdine(sequence.getAndIncrement());
            o3.aggiungiProdotto(new org.example.model.food.PaninoDonerKebab());
            o3.setStato(StatoOrdine.CONSEGNATO);
            o3.setDataConferma(java.time.LocalDateTime.now().minusDays(10));
            MOCK_ORDINI.add(o3);

            System.out.println("[DEMO MODE] Loaded " + MOCK_ORDINI.size() + " mock orders.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Ordine ordine) {
        ordine.setNumeroOrdine(sequence.getAndIncrement());
        MOCK_ORDINI.add(ordine);
    }

    @Override
    public void delete(Ordine ordine) {
        MOCK_ORDINI.removeIf(o -> o.getNumeroOrdine().equals(ordine.getNumeroOrdine()));
    }

    @Override
    public void update(Ordine ordine) {
        for (int i = 0; i < MOCK_ORDINI.size(); i++) {
            if (MOCK_ORDINI.get(i).getNumeroOrdine().equals(ordine.getNumeroOrdine())) {
                MOCK_ORDINI.set(i, ordine);
                return;
            }
        }
    }

    @Override
    public Ordine getOrdineByNumero(Long numeroOrdine) throws ObjectNotFoundException {
        return MOCK_ORDINI.stream()
                .filter(o -> o.getNumeroOrdine().equals(numeroOrdine))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Ordine non trovato"));
    }

    @Override
    public List<Ordine> getOrdiniByCliente(String clienteId) {
        return MOCK_ORDINI.stream()
                .filter(o -> o.getClienteId().equals(clienteId)) // Fixed: use getClienteId()
                .toList();
    }

    @Override
    public List<Ordine> getOrdiniByStato(StatoOrdine stato) {
        return MOCK_ORDINI.stream()
                .filter(o -> o.getStato() == stato)
                .toList();
    }

    @Override
    public Long getNextNumeroOrdine() {
        return sequence.get();
    }
}
