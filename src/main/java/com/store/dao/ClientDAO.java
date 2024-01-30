package com.store.dao;

import com.store.entity.Client;

import java.util.List;

public interface ClientDAO {

    void save(Client client);

    void update(Client client);

    void delete(Integer id);
    Client findById(Integer id);

    List<Client> findByName(String name);
}
