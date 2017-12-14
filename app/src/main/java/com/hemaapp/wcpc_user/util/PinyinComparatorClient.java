package com.hemaapp.wcpc_user.util;


import com.hemaapp.wcpc_user.model.Client;

import java.util.Comparator;

/**
 * 拼音比较器
 */
public class PinyinComparatorClient implements Comparator<Client> {

	public int compare(Client o1, Client o2) {
		if (o1.getCharindex().equals("@")
				|| o2.getCharindex().equals("#")) {
			return -1;
		} else if (o1.getCharindex().equals("#")
				|| o2.getCharindex().equals("@")) {
			return 1;
		} else {
			return o1.getCharindex().compareTo(o2.getCharindex());
		}
	}

}
