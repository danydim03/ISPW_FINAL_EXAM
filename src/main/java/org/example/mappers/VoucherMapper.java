package org.example.mappers;

import org.example.model.voucher.Voucher;
import org.example.model.voucher.VoucherFisso;
import org.example.model.voucher.VoucherPercentuale;
import org.example.use_cases.crea_ordine.beans.VoucherBean;

/**
 * Mapper per convertire entity Voucher in VoucherBean.
 * 
 * Pattern GRASP applicato: Pure Fabrication
 * - Non rappresenta un concetto di dominio
 * - Creato per aumentare High Cohesion nei Controller
 * - Centralizza la logica di conversione Voucher → VoucherBean
 * 
 * Benefici:
 * - UsaVoucherController non ha più metodi di conversione
 * - Se cambia VoucherBean, si modifica solo questo Mapper
 * - Gestisce correttamente i sottotipi (VoucherPercentuale, VoucherFisso)
 */
public class VoucherMapper {

    private VoucherMapper() {
        // Utility class - previene istanziazione
    }

    /**
     * Converte un'entity Voucher in un VoucherBean.
     * Gestisce automaticamente i sottotipi VoucherPercentuale e VoucherFisso.
     * 
     * @param voucher l'entity da convertire
     * @return VoucherBean popolato, null se voucher è null
     */
    public static VoucherBean toBean(Voucher voucher) {
        if (voucher == null) {
            return null;
        }

        VoucherBean bean = new VoucherBean();
        bean.setCodice(voucher.getCodice());
        bean.setDescrizione(voucher.getDescrizione());
        bean.setTipoVoucher(voucher.getTipoVoucher());

        // Gestione sottotipi con pattern matching (Java 16+)
        if (voucher instanceof VoucherPercentuale voucherPercentuale) {
            bean.setValore(voucherPercentuale.getPercentuale());
        } else if (voucher instanceof VoucherFisso voucherFisso) {
            bean.setValore(voucherFisso.getImportoSconto());
        }

        return bean;
    }
}
