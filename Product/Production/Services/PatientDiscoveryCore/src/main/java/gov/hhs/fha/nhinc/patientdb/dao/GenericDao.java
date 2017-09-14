/**
 *
 */
package gov.hhs.fha.nhinc.patientdb.dao;

import java.io.Serializable;

/**
 * @author PVenkatakrishnan
 *
 */
public interface GenericDao<T, PK extends Serializable> {
    boolean create(T t);

    T read(PK id, Class<T> objectType);

    boolean update(T t);

    void delete(T t);
}