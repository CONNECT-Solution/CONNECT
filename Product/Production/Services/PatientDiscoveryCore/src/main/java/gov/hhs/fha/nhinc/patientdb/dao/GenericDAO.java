package gov.hhs.fha.nhinc.patientdb.dao;

public interface GenericDAO<T> {
    boolean create(T t);

    void delete(T t);

    T read(Object id, Class<T> objectType);

    boolean update(T t);
}
