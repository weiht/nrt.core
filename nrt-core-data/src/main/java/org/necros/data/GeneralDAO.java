package org.necros.data;

import java.util.List;

import org.necros.paging.Pager;

public interface GeneralDAO {
	public abstract Object add(Object data) throws GeneralDataAccessException;
	public abstract Object update(Object data, String[] updatableFieilds) throws GeneralDataAccessException;
	public abstract Object remove(String id, boolean permanent) throws GeneralDataAccessException;

	public abstract List all();
	public abstract int countAll();
	public abstract Pager pageAll(Pager page);
	
	public abstract List filtered(String filterText);
	public abstract int countFiltered(String filterText);
	public abstract Pager pageFiltered(String filterText, Pager page);
}
