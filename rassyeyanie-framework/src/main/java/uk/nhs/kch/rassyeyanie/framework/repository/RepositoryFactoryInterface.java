package uk.nhs.kch.rassyeyanie.framework.repository;

import java.util.Calendar;

import uk.nhs.kch.rassyeyanie.framework.dto.KeyValuePairItem;
import uk.nhs.kch.rassyeyanie.framework.dto.SimpleItem;

@Deprecated
public interface RepositoryFactoryInterface {
	public Calendar getCurrentDate();

	public RepositoryInterface<KeyValuePairItem> getKeyValuePairItemRepository();

	public RepositoryInterface<SimpleItem> getSimpleItemRepository();
}
