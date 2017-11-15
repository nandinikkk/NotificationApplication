package com.location.reminder.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

public class GenericDaoJpaImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

	protected Class<T> entityClass;

	@PersistenceContext
	protected EntityManager entityManager;

	public GenericDaoJpaImpl() {

		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}

	@Override
	public T create(T t) {

		//entityManager.getTransaction().begin();
		this.entityManager.persist(t);
		this.entityManager.flush();

		//entityManager.getTransaction().commit();

		return t;
	}

	@Override
	public T read(PK id) {
		return this.entityManager.find(entityClass, id);
	}

	@Override
	public T update(T t) {

		try {
			
			//entityManager.getTransaction().begin();
			t = this.entityManager.merge(t);
			//entityManager.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;

	}

	@Override
	public void delete(T t) {
		entityManager.getTransaction().begin();

		t = this.entityManager.merge(t);
		this.entityManager.remove(t);
		entityManager.getTransaction().commit();

	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {

		//entityManager.getTransaction().begin();

		List<T> data = entityManager.createQuery("Select t from " + entityClass.getSimpleName() + " t").getResultList();
		//entityManager.getTransaction().commit();
		return data;
	}
}
