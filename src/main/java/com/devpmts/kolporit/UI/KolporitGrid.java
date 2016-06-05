package com.devpmts.kolporit.UI;

import javax.annotation.PostConstruct;

import org.springframework.data.repository.CrudRepository;

import com.vaadin.ui.Grid;

public abstract class KolporitGrid<E> extends Grid {

	public void addRowsFromRepo(CrudRepository<E, String> repo) {
		repo.findAll().forEach(object -> addRow(createRowObjectArray(object)));
	}

	@PostConstruct
	public void fill() {
		setImmediate(true);
		createColumns();
		addRowsFromRepo(getRepo());
	}

	abstract Object[] createRowObjectArray(E e);

	abstract void createColumns();

	protected abstract CrudRepository<E, String> getRepo();

}