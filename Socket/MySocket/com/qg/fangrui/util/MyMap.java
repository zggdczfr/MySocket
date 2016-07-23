package com.qg.fangrui.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.management.RuntimeErrorException;

/**
 * 
 * @author Administrator
 *
 * 用于储存在线用户的名单
 *
 * @param <K> 用户的用户名
 * @param <V> 对应的Socket线程
 */

public class MyMap<K, V> extends HashMap<K, V>
{
    	/**
    	 * 根据value来删除key（用户退出线程）
    	 * @param value Object 对象(用户线程)
    	 */
	public void removeByValue(Object value)
	{
		for(Object key : keySet())
		{
			if(get(key) == value)
			{
				remove(key);
				break;
			}
		}
	}
	
	
	/**
	 * 根据value来查找相应的key(根据用户线程获得用户名)
	 * @param value V对象(用户线程)
	 * @return 线程对应的用户名，否则返回null
	 */
	public K getKeyByValue(V value)
	{
		for(K key : keySet())
		{
			//如果找到
			if(get(key).equals(value) && get(key) == value)
			{
				return key;
			}
		}
		//找不到
		return null;
	}
	
	/**
	 * 获得所有线程的Set集合
	 * @return 返回所有线程的Set集合
	 */
	public Set<V> valueSet()
	{
		Set<V> values = new HashSet<V>();
		for(Object key : keySet())
		{
			values.add(get(key));
		}
		
		return  values;
	}
	
	/**
	 * 重写HashMap的put方法
	 */
	public V put(K key, V value)
	{
	    	//遍历keySet()集合，不允许出现key值重复，否则抛出自定义RuntimeExce
		for(K k : keySet())
		{
			if(k.equals(key))
			{
				throw new RuntimeException("不允许相同的key值！");
			}
		}
		//遍历valSet()集合，不允许出现key值重复，否则抛出自定义RuntimeExce
		for (V val : valueSet())
		{
			//根据hashMap表的储存比较原则，要求 equals 和 hashCode 都相同
			if(val.equals(value) && val.hashCode() == value.hashCode())
			{
				throw new RuntimeException("不允许存在相同的value值！");
			}
		}
		//根据父类方法添加在线线程
		return super.put(key, value);
	}
}
