package org.necros.data;

import java.util.List;
import java.util.Map;

import org.necros.paging.Pager;

public interface MapDAO {
	public abstract Map<String, Object> add(Map<String, Object> data) throws GeneralDataAccessException;
	public abstract Map<String, Object> update(Map<String, Object> data, String[] updatableFieilds) throws GeneralDataAccessException;
	public abstract Map<String, Object> remove(String id, boolean permanent) throws GeneralDataAccessException;

	public abstract List<Map<String, Object>> all();
	public abstract int countAll();
	public abstract Pager<Map<String, Object>> pageAll(Pager<Map<String, Object>> page);
	
	public abstract List<Map<String, Object>> filtered(String filterText);
	public abstract int countFiltered(String filterText);
	public abstract Pager<Map<String, Object>> pageFiltered(String filterText, Pager<Map<String, Object>> page);
}
