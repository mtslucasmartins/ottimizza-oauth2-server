package br.com.ottimizza.application.domain.mappers;

public interface Mapper<U, T> {

    static <U, T> T fromDTO(U u) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    static <U, T> U fromEntity(T t) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

}