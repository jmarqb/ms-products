package com.jmarqb.productsapi;

import com.jmarqb.productsapi.domain.model.Pagination;

public class Util {

	public static Pagination getPagination(int page, int size, String sort) {
		return new Pagination(page, size, sort, "uid");
	}
}
