package com.innouvous.balancetracker.data;

import java.util.List;

/**
 * Created by Allan on 7/12/2016.
 */
public interface IDataStore {
    Long insertProvider(Provider provider) throws Exception;
    void updateProvider(Provider provider) throws Exception;
    void deleteProvider(long id) throws Exception;

    List<Provider> getProviders() throws Exception;
    Provider getProvider(Long id) throws Exception;

    boolean providerExists(String name, Long id) throws Exception;
}
