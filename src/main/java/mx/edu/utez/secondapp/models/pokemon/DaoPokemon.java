package mx.edu.utez.secondapp.models.pokemon;

import mx.edu.utez.secondapp.models.crud.DaoRepository;
import mx.edu.utez.secondapp.models.user.Person;
import mx.edu.utez.secondapp.utils.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoPokemon implements DaoRepository<Pokemon> {
    //Crear los metodos CRUD
    //All, One, Create, Update, Enable-Disable
    private Connection conn;
    private PreparedStatement pstm;
    private ResultSet rs;

    @Override
    public List<Pokemon> findAll() {
        List<Pokemon> pokemons = new ArrayList<>();
        try {
            conn = new MySQLConnection().connect();
            String query = "SELECT p.*," +
                    "       p2.id as personId," +
                    "       p2.name as personName," +
                    "       p2.surname," +
                    "       p2.lastname," +
                    "       t.id as typeId," +
                    "       t.description" +
                    " FROM pokemons p" +
                    "         INNER JOIN people p2 on p.people_id = p2.id" +
                    "         INNER JOIN types t on p.types_id = t.id;";
            pstm  =conn.prepareStatement(query);
            rs = pstm.executeQuery();
            while (rs.next()){
                Pokemon pokemon = new Pokemon();
                pokemon.setId(rs.getLong("id"));
                pokemon.setName(rs.getString("name"));
                pokemon.setHeight(rs.getDouble("height"));
                pokemon.setWeight(rs.getDouble("weight"));
                pokemon.setPs(rs.getDouble("ps"));
                pokemon.setHp(rs.getDouble("hp"));
                pokemon.setPower(rs.getDouble("power"));
                pokemon.setAbilities(rs.getString("abilities"));
                Person person = new Person();
                person.setId(rs.getLong("personId"));
                person.setName(rs.getString("personName"));
                person.setSurname(rs.getString("surname"));
                person.setLastname(rs.getString("lastname"));
                pokemon.setPerson(person);
                PokemonType type = new PokemonType();
                type.setId(rs.getLong("typeId"));
                type.setDescription(rs.getString("description"));
                pokemon.setType(type);
                pokemons.add(pokemon);
            }
        }catch (SQLException e){
            Logger.getLogger(DaoPokemon.class.getName()).log(Level.SEVERE,"ERROR findAll"+e.getMessage());
        }finally {
            close();
        }
        return pokemons;
    }

    @Override
    public List<PokemonType> searchType() {
        List<PokemonType> types = new ArrayList<>();
        try {
            conn = new MySQLConnection().connect();
            String query = "SELECT *  FROM types;";
            pstm  =conn.prepareStatement(query);
            rs = pstm.executeQuery();
            while (rs.next()){
                PokemonType type = new PokemonType();
                type.setId(rs.getLong("id"));
                type.setDescription(rs.getString("description"));
                types.add(type);
            }
        }catch (SQLException e){
            Logger.getLogger(DaoPokemon.class.getName()).log(Level.SEVERE,"ERROR searchType"+e.getMessage());
        }finally {
            close();
        }
        return types;
    }

    @Override
    public List<Person> searchPerson() {
        List<Person> people = new ArrayList<>();
        try {
            conn = new MySQLConnection().connect();
            String query = "SELECT *  FROM people;";
            pstm  =conn.prepareStatement(query);
            rs = pstm.executeQuery();
            while (rs.next()){
                Person person = new Person();
                person.setId(rs.getLong("id"));
                person.setName(rs.getString("name"));
                person.setSurname(rs.getString("surname"));
                person.setLastname((rs.getString("lastname")));
                people.add(person);
            }
        }catch (SQLException e){
            Logger.getLogger(DaoPokemon.class.getName()).log(Level.SEVERE,"ERROR searchType"+e.getMessage());
        }finally {
            close();
        }
        return people;
    }

    @Override
    public Pokemon findOne(Long id) {
        try {
            conn = new MySQLConnection().connect();
            String query = "SELECT * FROM pokemons where id = ?;";
            pstm  =conn.prepareStatement(query);
            pstm.setLong(1,id);
            rs = pstm.executeQuery();
            Pokemon pokemon = new Pokemon();
            while (rs.next()){
                pokemon.setId(rs.getLong("id"));
                pokemon.setName(rs.getString("name"));
                pokemon.setPower(rs.getDouble("power"));
                pokemon.setWeight(rs.getDouble("weight"));
                pokemon.setHeight(rs.getDouble("height"));
                pokemon.setAbilities(rs.getString("abilities"));
                pokemon.setHp(rs.getDouble("hp"));
                pokemon.setPs(rs.getDouble("ps"));
                pokemon.setPerson((Person) rs.getObject("people_id"));
                pokemon.setType((PokemonType) rs.getObject("types_id"));
            }
            return pokemon;
        }catch (SQLException e){
            Logger.getLogger(DaoPokemon.class.getName()).log(Level.SEVERE,"ERROR findOne"+e.getMessage());
        }finally {
            close();
        }
        return null;
    }

    @Override
    public boolean save(Pokemon object) {
        try{
            conn = new MySQLConnection().connect();
            conn.setAutoCommit(false); //Preparar transaccion
            String query = "INSERT INTO pokemons (name, ps, hp, power, weight, height, abilities, people_id, types_id)" +
                    "VALUES (?,?,?,?,?,?,?,?,?);";
            pstm = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setString(1,object.getName());
            pstm.setDouble(2,object.getPs());
            pstm.setDouble(3,object.getHp());
            pstm.setDouble(4,object.getPower());
            pstm.setDouble(5,object.getWeight());
            pstm.setDouble(6,object.getHeight());
            pstm.setString(7,object.getAbilities());
            pstm.setLong(8,object.getPerson().getId());
            pstm.setLong(9,object.getType().getId());

            rs = pstm.executeQuery();
            if (rs.next()){
                long id = rs.getLong(1); //ID pokemon
                String querySaveImg = "INSERT INTO pokemon_img (file, person_id) VALUES(?,?);";
                pstm = conn.prepareStatement(querySaveImg);
                pstm.setBytes(1,object.getFile());
                pstm.setLong(2, id);
                pstm.execute();
            }
            conn.commit();
            return true;
        }catch (SQLException e){
            Logger.getLogger(DaoPokemon.class.getName()).log(Level.SEVERE, "Error save" + e.getMessage());
            conn.rollback();
        }finally {
            close();
        }
        return false;
    }

    @Override
    public boolean update(Pokemon object) {
        try{
            conn = new MySQLConnection().connect();
            String query = "UPDATE pokemons  SET name = ?,power = ?,weight = ?,height = ?,abilities = ?,hp = ?,people_id = ?, types_id =?, ps WHERE id = ?;";
            pstm = conn.prepareStatement(query);
            pstm.setString(1,object.getName());
            pstm.setDouble(2,object.getPower());
            pstm.setDouble(3,object.getWeight());
            pstm.setDouble(4,object.getHeight());
            pstm.setString(5,object.getAbilities());
            pstm.setDouble(6,object.getHp());
            pstm.setObject(7,object.getPerson());
            pstm.setObject(8,object.getType());
            pstm.setDouble(9,object.getPs());
            pstm.setLong(10,object.getId());
            return  pstm.executeUpdate() > 0;
        }catch (SQLException e){
            Logger.getLogger(DaoPokemon.class.getName()).log(Level.SEVERE, "Error save" + e.getMessage());
        }finally {
            close();
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        try {
            conn = new MySQLConnection().connect();
            String query = "DELETE FROM pokemons WHERE id = ?;";
            pstm = conn.prepareStatement(query);
            pstm.setLong(1,id);
            return pstm.executeUpdate() == 1;
        }catch (SQLException e){
            Logger.getLogger(DaoPokemon.class.getName()).log(Level.SEVERE, "Error delete" + e.getMessage());
        }finally{
            close();
        }
        return false;
    }


    public void close(){
        try {
            if (conn != null) conn.close();
            if (pstm != null) pstm.close();
            if (rs != null) rs.close();
        }catch (SQLException e){

        }
    }
}
