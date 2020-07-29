package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
public class UserJpaResource {

    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping(path = "/jpa/users")
    public List<User> retrieveAllUsers() {
        return UserRepository.findAll();
    }

    @GetMapping(path = "/jpa/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable Integer id) {
        Optional<User> user = UserRepository.findById(id);
        if (!user.isPresent())
            throw new UserNotFoundExecption("id-"+id);

        EntityModel<User> resource = EntityModel.of(user.get());
        WebMvcLinkBuilder linkTo = 
                linkTo(methodOn(this.getClass()).retrieveAllUsers());
                
        resource.add(linkTo.withRel("all-users"));

        //HATEOAS
        
        return resource;
    }

    @PostMapping(path="/jpa/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
        User savedUSer = UserRepository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUSer.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/jpa/users/{id}")
    public void deleteUser(@PathVariable Integer id){
        UserRepository.deleteById(id);
        // If it fails it will return an exception which be handeled by ExceptionHandler
    }

    @GetMapping(path = "/jpa/users/{id}/posts")
    public List<Post> retrieveAllPosts(@PathVariable int id) {
        Optional<User> user =  UserRepository.findById(id);
        if (!user.isPresent()){
            throw new UserNotFoundExecption("id-" +id);
        }
        return user.get().getPosts();
    }

    @PostMapping(path = "/jpa/users/{id}/posts")
    public ResponseEntity<Void> createPost(@PathVariable int id, @RequestBody Post post){
        Optional<User> userOptional =  UserRepository.findById(id);
        if (!userOptional.isPresent()){
            throw new UserNotFoundExecption("id-" +id);
        }   
        User user = userOptional.get();
        post.setUser(user);
        postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}