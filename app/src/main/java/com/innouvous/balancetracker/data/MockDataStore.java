package com.innouvous.balancetracker.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Allan on 7/14/2016.
 */
public class MockDataStore implements IDataStore {
    private HashMap<Long, Provider> providers;

    private long nextId = 1;

    public MockDataStore()
    {
        providers = new HashMap<>();

        try {
            Provider provider;

            provider = new Provider("Metrocard", 20, 2.75);
            insertProvider(provider);

            provider = new Provider("PATH", 80, 1);
            insertProvider(provider);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long insertProvider(Provider provider) throws Exception {

        if (providerExists(provider.getName(), null))
        {
            throwProviderExists();
        }

        Long id = nextId++;
        provider.setId(id);

        providers.put(id, provider);
        return id;
    }

    @Override
    public void updateProvider(Provider provider) throws Exception {
        if (providerExists(provider.getName(), provider.getId()))
        {
            throwProviderExists();
        }

        providers.put(provider.getId(), provider);
    }

    private void throwProviderExists() throws Exception {
        throw new Exception("A provider with the same name already exists.");
    }

    @Override
    public void deleteProvider(long id) throws Exception {
        providers.remove(id);
    }

    @Override
    public List<Provider> getProviders() throws Exception {
        return new ArrayList<>(providers.values());
    }

    @Override
    public Provider getProvider(Long id) throws Exception {
        return providers.get(id);
    }

    @Override
    public boolean providerExists(String name, Long id) throws Exception {
        for (Provider p : providers.values())
        {
            if (p.getName().equals(name) && p.getId() != id)
                return true;
        }

        return false;
    }
}
