package br.com.project.screenmatch.service;

public interface IDataConversion {

    <T> T dataObtain(String json, Class<T> classe);
}
