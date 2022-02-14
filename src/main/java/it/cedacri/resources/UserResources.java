package it.cedacri.resources;


import it.cedacri.model.User;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/user")
@Tag(name = "User Resources", description = "User Rest APi")
public class UserResources {
    private List<User> users = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getAllUsers",
            summary = "Get Users",
            description = "Get all user inside the list"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getAllUsers() {
        return Response.ok(users).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    @Operation(
            operationId = "countUsers",
            summary = "Count User",
            description = "Size of the List users"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    public Integer countUsers() {
        return users.size();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "createUser",
            summary = "Create User",
            description = "Create a new user"
    )
    @APIResponse(
            responseCode = "201",
            description = "User created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response createUser(
            @RequestBody(
                    description = "User to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = User.class))
            )
                    User user) {
        if (!user.getFullName().trim().isEmpty() && user.getId() >= 0) {
            users.add(user);
            return Response.status(Response.Status.CREATED).entity(users).build();
        } else return Response.serverError().build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "updateUser",
            summary = "Update User",
            description = "Update a existent user"
    )
    @APIResponse(
            responseCode = "200",
            description = "User updated",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response updateUser(
            @Parameter(
                    description = "User id",
                    required = true
            ) @QueryParam("userId")
                    Long userId,

            @Parameter(
                    description = "User full name",
                    required = true
            ) @QueryParam("fullName")
                    String fullName,

            @Parameter(
                    description = "User movie id",
                    required = true
            ) @QueryParam("movieId")
                    String movieId,

            @Parameter(
                    description = "User movie rating",
                    required = true
            ) @QueryParam("rating")
                    int rating) {
        users = users.stream().peek(user -> {
            if (user.getId().equals(userId)) {
                user.setFullName(fullName);
                Map<String, Integer> ratings = user.getRatings();
                ratings.replace(movieId, rating);
                user.setRatings(ratings);
            }
        }).collect(Collectors.toList());
        return Response.ok(users).build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "deleteUser",
            summary = "Delete user",
            description = "Delete a existent user"
    )
    @APIResponse(
            responseCode = "204",
            description = "User deleted",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "User not valid",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response deleteUser(
            @Parameter(
                    name = "User id",
                    required = true
            )
            @PathParam("id") Long id) {
        Optional<User> usersToDelete = users.stream().filter(user -> user.getId().equals(id)).findFirst();
        boolean removed = false;
        if (usersToDelete.isPresent()) {
            removed = users.remove(usersToDelete.get());
        }
        if (removed) {
            return Response.noContent().build();
        } else return Response.status(400).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getInfoFor(@PathParam("id") Long id) {
        Optional<User> searchedUser = users.stream().filter(user -> user.getId().equals(id)).findFirst();
        if (searchedUser.isPresent()) {
            return Response.ok(searchedUser).build();
        } else return Response.serverError().build();
    }
}
