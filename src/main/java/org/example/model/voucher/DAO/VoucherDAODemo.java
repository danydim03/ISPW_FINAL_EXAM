package org.example.model.voucher.DAO;

import org.example.exceptions.ObjectNotFoundException;
import org.example.model.voucher.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class VoucherDAODemo implements VoucherDAOInterface {

    private static final List<Voucher> MOCK_VOUCHERS = new ArrayList<>();
    private static final AtomicLong sequence = new AtomicLong(1);

    static {
        // Add some dummy vouchers
        // Using correct constructors found in classes
        MOCK_VOUCHERS.add(new VoucherPercentuale("SCONTO10", 10.0));
        MOCK_VOUCHERS.add(new VoucherFisso("BONUS5", 5.00, 10.00));

        // Assign IDs
        for (Voucher v : MOCK_VOUCHERS) {
            v.setId(sequence.getAndIncrement());
        }
    }

    @Override
    public Voucher getVoucherByCodice(String codice) throws ObjectNotFoundException {
        return MOCK_VOUCHERS.stream()
                .filter(v -> v.getCodice().equalsIgnoreCase(codice))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Voucher non trovato"));
    }

    @Override
    public Voucher getVoucherById(Long id) throws ObjectNotFoundException {
        return MOCK_VOUCHERS.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Voucher non trovato"));
    }

    @Override
    public List<Voucher> getAllVoucherAttivi() {
        return MOCK_VOUCHERS.stream()
                .filter(Voucher::isAttivo)
                .toList();
    }

    @Override
    public void insert(Voucher voucher) {
        voucher.setId(sequence.getAndIncrement());
        MOCK_VOUCHERS.add(voucher);
    }

    @Override
    public void delete(Voucher voucher) {
        MOCK_VOUCHERS.removeIf(v -> v.getId().equals(voucher.getId()));
    }

    @Override
    public void update(Voucher voucher) {
        for (int i = 0; i < MOCK_VOUCHERS.size(); i++) {
            if (MOCK_VOUCHERS.get(i).getId().equals(voucher.getId())) {
                MOCK_VOUCHERS.set(i, voucher);
                return;
            }
        }
    }
}
