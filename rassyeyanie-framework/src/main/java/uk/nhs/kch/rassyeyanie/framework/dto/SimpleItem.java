package uk.nhs.kch.rassyeyanie.framework.dto;

import java.io.Serializable;

@Deprecated
public class SimpleItem implements Cloneable, Serializable {

	private static final long serialVersionUID = 8517572438225072162L;

	private long id;

	private String context;

	private String itemKey;

	public SimpleItem() {
		this.id = 0L;
	}

	public SimpleItem(long id) {
		this.id = id;
	}

	public SimpleItem(long id, String context) {
		this.id = id;
		this.context = context;
	}

	public SimpleItem(long id, String context, String itemKey) {
		this.id = id;
		this.context = context;
		this.setItemKey(itemKey);
	}

	public String getContext() {
		return context;
	}

	public long getId() {
		return this.id;
	}

	public String getItemKey() {
		return itemKey;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}
}
