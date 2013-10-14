/**
 * 
 */
package org.necros.data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author weiht
 *
 */
public class UuidStringGenerator implements IdGenerator {
	public Serializable generate() {
		return UUID.randomUUID().toString();
	}
}
