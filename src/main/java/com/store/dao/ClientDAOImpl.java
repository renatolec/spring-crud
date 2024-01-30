package com.store.dao;

import com.store.entity.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ClientDAOImpl implements ClientDAO {

    private EntityManager entityManager;

    @Autowired
    public ClientDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Client client) {
        entityManager.persist(client);
    }

    @Override
    @Transactional
    public void update(Client client) {
        entityManager.merge(client);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Client client = entityManager.find(Client.class, id);
        entityManager.remove(client);
    }

    @Override
    public Client findById(Integer id) {
        return entityManager.find(Client.class, id);
    }

    @Override
    public List<Client> findByName(String name) {
        TypedQuery<Client> typedQuery = entityManager.createQuery("FROM Client WHERE name LIKE :name", Client.class);
        typedQuery.setParameter("name", name + "%");
        return typedQuery.getResultList();
    }
}
