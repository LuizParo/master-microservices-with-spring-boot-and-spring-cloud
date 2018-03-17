package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(
        path = "/users",
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
)
public class UserResource {

    private final UserInMemoryDao dao;

    @Autowired
    public UserResource(UserInMemoryDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public Iterable<User> retrieveAllUsers() {
        return this.dao.findAll();
    }

    @GetMapping("/{id}")
    public Resource<User> retrieveUser(@PathVariable Long id) {
        User user = this.dao.findOne(id).orElseThrow(() -> new UserNotFoundException(String.format("id - %d", id)));

        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());

        Resource<User> resource = new Resource<>(user);
        resource.add(linkTo.withRel("all-users"));

        return resource;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        User savedUser = this.dao.save(user);

        URI newUserLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId().orElse(0L))
                .toUri();

        return ResponseEntity.created(newUserLocation).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> removeUser(@PathVariable Long id) {
        this.dao.remove(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("id - %d", id)));

        return ResponseEntity.noContent().build();
    }
}