package com.soak.sshframe.support;

import java.util.List;

public class PaginationSupport<T> {

	private int pageSize = 12;
	private List<T> items;

	private int totalCount;

	private int[] indexes = new int[0];

	private int startIndex = 0;
	
	private long spendTime = 0;
	
	// 页数
	private int pageCount = 0;
	
	// 当前页
	private int currentPage = 0;

	public static final int indexToPage(int index, int pageSize) {
		return index % pageSize;
	}
	
	public PaginationSupport(List<T> items, int totalCount) {
		setTotalCount(totalCount);
		setItems(items);
		setStartIndex(0);
	}

	public PaginationSupport(List<T> items, int totalCount, int startIndex) {
		setTotalCount(totalCount);
		setItems(items);
		setStartIndex(startIndex);
	}

	public PaginationSupport(List<T> items, int totalCount, int startIndex, int pageSize) {
		setPageSize(pageSize);
		setTotalCount(totalCount);
		setItems(items);
		setStartIndex(startIndex);
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		if (totalCount > 0) {
			this.totalCount = totalCount;
			int count = totalCount / pageSize;
			if (totalCount % pageSize > 0)
				count++;
			this.pageCount = count;
			indexes = new int[count];
			for (int i = 0; i < count; i++) {
				indexes[i] = pageSize * i;
			}
		} else {
			this.totalCount = 0;
		}
	}

	public int[] getIndexes() {
		return indexes;
	}

	public void setIndexes(int[] indexes) {
		this.indexes = indexes;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		if (totalCount <= 0 || pageSize <= 0)
			this.startIndex = 0;
		else if (startIndex >= totalCount)
			this.startIndex = indexes[indexes.length - 1];
		else if (startIndex < 0)
			this.startIndex = 0;
		else {
			this.startIndex = indexes[startIndex / pageSize];
		}
		
		this.currentPage = this.startIndex / pageSize;
	}

	public int getNextIndex() {
		int nextIndex = getStartIndex() + pageSize;
		if (nextIndex >= totalCount)
			return getStartIndex();
		else
			return nextIndex;
	}

	public int getPreviousIndex() {
		int previousIndex = getStartIndex() - pageSize;
		if (previousIndex < 0)
			return 0;
		else
			return previousIndex;
	}

	public long getSpendTime() {
		return spendTime;
	}

	public void setSpendTime(long spendTime) {
		this.spendTime = spendTime;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
