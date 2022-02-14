package it.cedacri.resources;

import it.cedacri.model.Movie;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/movies")
@Tag(name = "Movie Resources", description = "Movie Rest APi")
public class MovieResource {
    private static List<Movie> movies = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getMovie",
            summary = "Get Movies",
            description = "Get all movies inside the list"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getMovie() {
        return Response.ok(movies).build();
    }


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    @Operation(
            operationId = "countMovies",
            summary = "Count Movies",
            description = "Size of the List Movies"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    public Integer countMovies() {
        return movies.size();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "createMovie",
            summary = "Create Movie",
            description = "Create a new movie"
    )
    @APIResponse(
            responseCode = "201",
            description = "Movie created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response createMovie(
            @RequestBody(
                    description = "Movie to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Movie.class))
            )
            Movie movie) {
        if (!movie.getTitle().trim().isEmpty()) {
            movies.add(movie);
            return Response.status(Response.Status.CREATED).entity(movies).build();
        } else return Response.serverError().build();
    }

    @PUT
    @Path("{id}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "updateMovie",
            summary = "Update Movie",
            description = "Update a existent movie"
    )
    @APIResponse(
            responseCode = "200",
            description = "Movie updated",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response updateMovie(
            @Parameter(
                    description = "Movie id",
                    required = true
            )
            @PathParam("id") Long id,

            @Parameter(
                    description = "Movie Title",
                    required = true
            )
            @PathParam("title") String title) {
        movies = movies.stream().peek(movie -> {
            if (movie.getId().equals(id)) {
                movie.setTitle(title);
            }
        }).collect(Collectors.toList());
        return Response.ok(movies).build();
    }


    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "deleteMovie",
            summary = "Delete Movie",
            description = "Delete a existent movie"
    )
    @APIResponse(
            responseCode = "204",
            description = "Movie deleted",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Movie not valid",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response deleteMovie(
            @Parameter(
                    name = "Movie id",
                    required = true
            )
            @PathParam("id") Long id) {
        Optional<Movie> movieToDelete = movies.stream().filter(movie -> movie.getId().equals(id)).findFirst();
        boolean removed = false;
        if (movieToDelete.isPresent()) {
            removed = movies.remove(movieToDelete.get());
        }
        if (removed) {
            return Response.noContent().build();
        } else return Response.status(400).build();
    }
}
