package mx.edu.utez.secondapp.models.crud;

import mx.edu.utez.secondapp.models.pokemon.PokemonType;
import mx.edu.utez.secondapp.models.user.Person;

import java.util.List;

public interface DaoRepository <T>{
    List<T> findAll();

    List<PokemonType> searchType();

    List<Person> searchPerson();

    T findOne(Long id);
    boolean save(T object);
    boolean update(T object);
    boolean delete(Long id);
}
