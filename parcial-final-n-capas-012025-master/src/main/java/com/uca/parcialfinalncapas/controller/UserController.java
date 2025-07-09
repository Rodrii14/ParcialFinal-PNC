package com.uca.parcialfinalncapas.controller;

import com.uca.parcialfinalncapas.dto.request.UserCreateRequest;
import com.uca.parcialfinalncapas.dto.request.UserUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.GeneralResponse;
import com.uca.parcialfinalncapas.dto.response.UserResponse;
import com.uca.parcialfinalncapas.entities.Token;
import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.service.UserService;
import com.uca.parcialfinalncapas.service.iTokenServices;
import com.uca.parcialfinalncapas.utils.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private final iTokenServices TokenServices;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/all")
    public ResponseEntity<GeneralResponse> getAllUsers() {
        List<UserResponse> users = userService.findAll();

        return ResponseBuilderUtil.buildResponse(
                "Usuarios obtenidos correctamente",
                users.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK,
                users
        );
    }

    @GetMapping("/{correo}")
    public ResponseEntity<GeneralResponse> getUserByCorreo(@PathVariable String correo) {
        UserResponse user = userService.findByUsername(correo);
        return ResponseBuilderUtil.buildResponse("Usuario encontrado", HttpStatus.OK, user);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<GeneralResponse> login(@RequestBody AccessUserDto userDto) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

        Token token = TokenServices.generateToken((User) auth.getPrincipal());

        return ResponseBuilderUtil.buildResponse("Access granted", HttpStatus.CREATED, token.getToken());
    }


    @PostMapping
    public ResponseEntity<GeneralResponse> createUser(@Valid @RequestBody UserCreateRequest user) {
        UserResponse createdUser = userService.save(user);

        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getCorreo(), user.getPassword()));

        Token token = TokenServices.generateToken((User) auth.getPrincipal());

        return ResponseBuilderUtil.buildResponse("Usuario creado correctamente", HttpStatus.CREATED, token.getToken());
    }

    @PutMapping
    public ResponseEntity<GeneralResponse> updateUser(@Valid @RequestBody UserUpdateRequest user) {
        UserResponse updatedUser = userService.update(user);
        return ResponseBuilderUtil.buildResponse("Usuario actualizado correctamente", HttpStatus.OK, updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseBuilderUtil.buildResponse("Usuario eliminado correctamente", HttpStatus.OK, null);
    }
}
