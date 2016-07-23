package com.qg.fangrui.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.management.RuntimeErrorException;

/**
 * 
 * @author Administrator
 *
 * ���ڴ��������û�������
 *
 * @param <K> �û����û���
 * @param <V> ��Ӧ��Socket�߳�
 */

public class MyMap<K, V> extends HashMap<K, V>
{
    	/**
    	 * ����value��ɾ��key���û��˳��̣߳�
    	 * @param value Object ����(�û��߳�)
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
	 * ����value��������Ӧ��key(�����û��̻߳���û���)
	 * @param value V����(�û��߳�)
	 * @return �̶߳�Ӧ���û��������򷵻�null
	 */
	public K getKeyByValue(V value)
	{
		for(K key : keySet())
		{
			//����ҵ�
			if(get(key).equals(value) && get(key) == value)
			{
				return key;
			}
		}
		//�Ҳ���
		return null;
	}
	
	/**
	 * ��������̵߳�Set����
	 * @return ���������̵߳�Set����
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
	 * ��дHashMap��put����
	 */
	public V put(K key, V value)
	{
	    	//����keySet()���ϣ����������keyֵ�ظ��������׳��Զ���RuntimeExce
		for(K k : keySet())
		{
			if(k.equals(key))
			{
				throw new RuntimeException("��������ͬ��keyֵ��");
			}
		}
		//����valSet()���ϣ����������keyֵ�ظ��������׳��Զ���RuntimeExce
		for (V val : valueSet())
		{
			//����hashMap��Ĵ���Ƚ�ԭ��Ҫ�� equals �� hashCode ����ͬ
			if(val.equals(value) && val.hashCode() == value.hashCode())
			{
				throw new RuntimeException("�����������ͬ��valueֵ��");
			}
		}
		//���ݸ��෽����������߳�
		return super.put(key, value);
	}
}
