package org.example.model.food;

/**
 * Value Object immutabile che rappresenta i metadati di un add-on.
 * 
 * <p>
 * <b>GRASP: Pure Fabrication</b> - Questa classe è creata per separare
 * la responsabilità di rappresentare i metadati degli add-on dalla
 * responsabilità di decorazione del pattern Decorator.
 * </p>
 * 
 * <p>
 * <b>BCE: Non è un'Entity</b> - È un Value Object puro, immutabile,
 * usato solo per trasportare dati dal DAO al Controller.
 * </p>
 * 
 * <p>
 * <b>GoF Compliance</b> - Permette ai Decorator di essere usati
 * ESCLUSIVAMENTE come veri decoratori (sempre con componente reale)
 * </p>
 */
public final class AddOnDescriptor {

    private final Long id;
    private final String nome;
    private final double costo;
    private final int durata;
    private final String className;

    /**
     * Costruisce un AddOnDescriptor con tutti i suoi attributi.
     * 
     * @param id        ID del record nel database
     * @param nome      nome descrittivo dell'add-on (es. "Cipolla")
     * @param costo     costo aggiuntivo dell'add-on
     * @param durata    durata aggiuntiva in minuti
     * @param className nome della classe ConcreteDecorator (es. "Cipolla")
     */
    public AddOnDescriptor(Long id, String nome, double costo, int durata, String className) {
        this.id = id;
        this.nome = nome;
        this.costo = costo;
        this.durata = durata;
        this.className = className;
    }

    /**
     * @return ID del record nel database
     */
    public Long getId() {
        return id;
    }

    /**
     * @return nome descrittivo dell'add-on
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return costo aggiuntivo dell'add-on
     */
    public double getCosto() {
        return costo;
    }

    /**
     * @return durata aggiuntiva in minuti
     */
    public int getDurata() {
        return durata;
    }

    /**
     * @return nome della classe ConcreteDecorator
     */
    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return String.format("AddOnDescriptor{id=%d, nome='%s', costo=%.2f, durata=%d, className='%s'}",
                id, nome, costo, durata, className);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AddOnDescriptor that = (AddOnDescriptor) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
