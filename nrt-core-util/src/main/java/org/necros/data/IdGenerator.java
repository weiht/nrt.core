/**
 * 
 */
package org.necros.data;

import java.io.Serializable;

/**
 * @author weiht
 *
 */
public interface IdGenerator {
	public abstract Serializable generate();
}
