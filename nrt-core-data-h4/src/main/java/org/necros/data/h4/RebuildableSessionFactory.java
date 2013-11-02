package org.necros.data.h4;

import org.hibernate.SessionFactory;

public interface RebuildableSessionFactory extends SessionFactory {
	public abstract void rebuild();
}
