package org.necros.registry;

/**
 * @author weiht
 *
 */
public enum ContentType {
	/**
	 * 无类型（未知类型）
	 */
	None,
	/**
	 * 整数型
	 */
	Long,
	/**
	 * 浮点型
	 */
	Double,
	/**
	 * 字符串
	 */
	String,
	/**
	 * 长文本
	 */
	Clob,
	/**
	 * 二进制大字段
	 */
	Blob,
	/**
	 * URL
	 */
	Url,
	/**
	 * 本地磁盘文件
	 */
	File,
	/**
	 * POJO对象实例（序列化）
	 */
	Serialized
}