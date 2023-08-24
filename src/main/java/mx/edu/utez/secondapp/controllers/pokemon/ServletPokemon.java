package mx.edu.utez.secondapp.controllers.pokemon;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import mx.edu.utez.secondapp.models.pokemon.DaoPokemon;
import mx.edu.utez.secondapp.models.pokemon.Pokemon;
import mx.edu.utez.secondapp.models.pokemon.PokemonType;
import mx.edu.utez.secondapp.models.user.Person;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.lang.Long.parseLong;

@WebServlet(name = "pokemon", urlPatterns = {
        "/api/pokemon/all",
        "/api/pokemon/one",
        "/api/pokemon/person",
        "/api/pokemon/create",
        "/api/pokemon/save",
        "/api/pokemon/edit",
        "/api/pokemon/update",
        "/api/pokemon/enable_disable",
})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 100
)
public class ServletPokemon extends HttpServlet {
    private String action, redirect = "/api/pokemon/all", id, name, abilities, fileName;
    private Double ps, hp, weight, height, power;
    private String personId;
    private String pokemonType;
    //LINUX - "/"
    private String directory = "D:" + File.separator + "pokedex";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        action = req.getServletPath();
        switch (action){
            case "/api/pokemon/all":
                //Consultar pokemons
                req.setAttribute("pokemons", new DaoPokemon().findAll());
                req.setAttribute("types", new DaoPokemon().searchType());
                req.setAttribute("people", new DaoPokemon().searchPerson());
                redirect = "/views/pokemon/index.jsp";
                break;
            case "/api/pokemon/one":
                redirect = "/views/pokemon/one.jsp";
                break;
            case "/api/pokemon/person":
                //Consultar pokemons ligados a una persona
                redirect = "/views/pokemon/person-id.jsp";
                break;
            case "/api/pokemon/create":
                //Consultar a las personas
                //Consultar los tipos de pokemon
                redirect = "/views/pokemon/create.jsp";
                break;
            case "/api/pokemon/edit":
                //Consultar los tipos de pokemon
                redirect = "/views/pokemon/edit.jsp";
                break;
        }
        req.getRequestDispatcher(redirect).forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        action = req.getServletPath();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        switch (action){
            case "/api/pokemon/save":
                for (Part part: req.getParts()){
                    fileName = part.getSubmittedFileName();
                    if (fileName != null)
                        part.write(directory + File.separator + fileName);
                }
                name = req.getParameter("name");
                height = Double.valueOf(req.getParameter("height"));
                weight = Double.valueOf(req.getParameter("weight"));
                ps = Double.valueOf(req.getParameter("ps"));
                hp = Double.valueOf(req.getParameter("hp"));
                power = Double.valueOf(req.getParameter("power"));
                abilities = req.getParameter("abilities");

                personId = req.getParameter("personId");
                Person person = new Person();
                person.setId(parseLong(personId));

                pokemonType = req.getParameter("pokemonType");
                PokemonType type = new PokemonType();
                type.setId(parseLong(pokemonType));


                Pokemon pokemon1 = new Pokemon(0,name, ps, hp, power, weight, height, abilities, person, type);
                boolean result = new DaoPokemon().save(pokemon1);
                if (result){
                    redirect = "/api/pokemon/all?result= " + result + "&message=" + URLEncoder.encode("¡Éxito! Pokemon registrado correctamente.",
                            StandardCharsets.UTF_8);

                }else{
                    redirect = "//api/pokemon/all?result= " + result + "&message=" + URLEncoder.encode("¡Error! Acción no realizada correctamente.",
                            StandardCharsets.UTF_8);
                }


                break;
            case "/api/pokemon/update":
                break;
            case "/api/pokemon/enable-disable":
                break;
        }
        resp.sendRedirect(req.getContextPath() + redirect);
    }
}


