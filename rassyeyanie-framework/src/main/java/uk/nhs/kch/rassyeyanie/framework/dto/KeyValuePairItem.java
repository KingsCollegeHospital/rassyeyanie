package uk.nhs.kch.rassyeyanie.framework.dto;

@Deprecated
public class KeyValuePairItem extends SimpleItem {

	private static final long serialVersionUID = -6952242568721791511L;

	private String itemValue;

	public KeyValuePairItem() {
		super();
	}

	public KeyValuePairItem(long id) {
		super(id);
	}

	public KeyValuePairItem(long id, String context) {
		super(id, context);
	}

	public KeyValuePairItem(long id, String context, String itemKey) {
		super(id, context, itemKey);
	}

	public KeyValuePairItem(long id, String context, String itemKey,
			String itemValue) {
		super(id, context, itemKey);
		this.itemValue = itemValue;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
}
