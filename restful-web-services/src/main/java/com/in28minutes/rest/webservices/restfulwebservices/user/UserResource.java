package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
 
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserResource {
    @Autowired
    private UserDaoService usersDaoService;

    @GetMapping(path = "/users")
    public List<User> retrieveAllUsers() {
        return usersDaoService.findAll();
    }

    @GetMapping(path = "/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable Integer id) {
        User user = usersDaoService.findOne(id);
        if (user == null)
            throw new UserNotFoundExecption("id-"+id);

        EntityModel<User> resource = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = 
                linkTo(methodOn(this.getClass()).retrieveAllUsers());
                
        resource.add(linkTo.withRel("all-users"));

        //HATEOAS
        
        return resource;
    }

    @PostMapping(path="/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
        User savedUSer = usersDaoService.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUSer.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable Integer id){
        User user = usersDaoService.deleteById(id);
        if (user==null)
            throw new UserNotFoundExecption("id-"+id);
    }
}