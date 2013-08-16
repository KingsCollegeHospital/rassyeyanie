package uk.nhs.kch.rassyeyanie.framework.repository;

@Deprecated
public interface RepositoryInterface<T> {
	T Get(String context, String key);
}
