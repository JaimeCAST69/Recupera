<%--
  Created by IntelliJ IDEA.
  User: oskit
  Date: 28/06/2023
  Time: 10:36 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Pokemon</title>
    <jsp:include page="../../layouts/head.jsp"/>
</head>
<body>
<%--        NV NAVBAR--%>
    <div class="container">

        <div class="row">
            <div class="col">
                <br>
                <a class="navbar-brand mb-sm-5"  href="#">
                <img src="../../assets/img/pokebola.png"  alt="bootstrap" width="35" height="35"  >
                    POKEMONS
                </a>
                <br><br>
                <div class="card">
                    <div class="card-header">
                        <div class="row">
                            <div class="col">Listado</div>
                            <div class="col text-end">
                                <button type="button" data-bs-toggle="modal" data-bs-target="#createPokemon" class="btn btn-outline-primary btn-sm">
                                    AGREGAR</button>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="container-fluid">
                            <div class="form-group mb-3">
                                <div class="row">
                                        <s:forEach items="${pokemons}" var="pokemons">

                                                    <div class="col" >
                                                        <div class="card" style="width: 18rem;">
                                                                <%--                                <img src="/pokemon/loadfile?file=${pokemons.id}" class="card-img-top" alt="${pokemons.name}">--%>
                                                            <img src="../../assets/img/charizard.gif" class="card-img-top" alt="${pokemons.name}">
                                                            <div class="card-body">
                                                                <h5 class="card-title">${pokemons.name}</h5>
                                                                <p class="card-text">Poder: ${pokemons.power}</p>
                                                                <p class="card-text">Puntos de experiencia: ${pokemons.ps}</p>
                                                                <p class="card-text">Puntos de salud: ${pokemons.hp}</p>
                                                                <p class="card-text">Habilidades: ${pokemons.abilities}</p>
                                                                <button type="button" class="btn btn-primary">
                                                                    <i data-feather="edit"></i>EDITAR
                                                                </button>
                                                            </div>
                                                        </div>
                                                        <br><br>
                                                    </div>
                                            <br><br><br><br>
                                        </s:forEach>

                                </div>
                            </div>

                        </div>


                    </div>
                </div>
            </div>
        </div>
    </div>

<!-- Modal -->
<div class="modal fade" id="createPokemon" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="staticBackdropLabel">Agregar Pokemon</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>


            <div class="modal-body">
                <form id="pokemon-form" class="needs-validation" action="/api/pokemon/save" method="post">
                    <div class="container-fuid">
                        <div class="form-group mb-3">
                            <div class="row">
                                <div class="col">
                                    <div class="form-floating">
                                        <input class="form-control" placeholder="i" name="name" id="name" type="text" required>
                                        <label for="name">Nombre</label>
                                        <div class="invalid-feedback is-invalid">
                                            Campo obligatorio
                                        </div>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="form-floating">
                                        <input class="form-control" placeholder="i" name="power" id="power" type="text"  required>
                                        <label for="power">Poder</label>
                                        <div class="invalid-feedback">Campo obligatorio</div>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="form-floating">
                                        <input class="form-control" placeholder="i" name="hp" id="hp" type="text" required>
                                        <label for="hp">HP</label>
                                        <div class="invalid-feedback">Campo obligatorio</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-group mb-3">
                            <div class="row">
                                <div class="col">
                                    <div class="form-floating">
                                        <input class="form-control" placeholder="i" name="weight" id="weight" type="text" required>
                                        <label for="weight">Peso</label>
                                        <div class="invalid-feedback">Campo obligatorio</div>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="form-floating">
                                        <input class="form-control" placeholder="i" name="height" id="height" type="text" required>
                                        <label for="height">Altura</label>
                                        <div class="invalid-feedback">Campo obligatorio</div>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="form-floating">
                                        <input class="form-control" placeholder="i" name="ps" id="ps" type="text" required>
                                        <label for="ps">Puntos de Experiencia</label>
                                        <div class="invalid-feedback">Campo obligatorio</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-group mb-3">
                            <div class="row">
                                <div class="col">
                                    <div class="form-floating">
                                        <input class="form-control" placeholder="i" name="abilities" id="abilities" type="text" required>
                                        <label for="abilities">Habilidades</label>
                                        <div class="invalid-feedback">Campo obligatorio</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-group mb-3">

                            <div class="row">
                                <div class="col">
                                        <select id="pokemonType" name="pokemonType" class="form-select" aria-label="Default select example" required>

                                            <option selected>Tipo de Pokemon</option>
                                            <s:forEach items="${types}" var="types">
                                            <option value="${types.id}">${types.description}</option>
                                            </s:forEach>

                                        </select>
                                    <div class="invalid-feedback">Campo obligatorio</div>

                                </div>
                            </div>

                        </div>

                        <div class="form-group mb-3">

                            <div class="row">
                                <div class="col">
                                    <select class="form-select" aria-label="Default select example" id="personId" name="personId" required>

                                        <option selected>Persona Que Pertenece Pokemon</option>
                                        <s:forEach items="${people}" var="people">
                                            <option value="${people.id}">${people.name} ${people.surname} ${people.lastname}</option>
                                        </s:forEach>

                                    </select>
                                    <div class="invalid-feedback">Campo obligatorio</div>

                                </div>

                            </div>

                        </div>


                    </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                <button type="submit" class="btn btn-primary">Agregar</button>
            </div>


            </form>

        </div>
    </div>

</div>


    <jsp:include page="../../layouts/footer.jsp"/>

<script>
    (function (){
        const form = document.getElementById("user-form"); //document hace referencia a la pantalla
        form.addEventListener("submit",function (event){
            if(!form.checkValidity()){ //check revisa que no este vacio el formulario
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add("was-validated")
        }, false); //false que no necesita que haga mas cosas que solo la función
    })(); //Se ejecuta mientras este en pantalla
</script>
</body>
</html>


