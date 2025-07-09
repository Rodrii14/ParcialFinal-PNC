package com.uca.parcialfinalncapas.service.impl;

import com.uca.parcialfinalncapas.dto.request.UserCreateRequest;
import com.uca.parcialfinalncapas.dto.request.UserUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.UserResponse;
import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.exceptions.UserNotFoundException;
import com.uca.parcialfinalncapas.repository.UserRepository;
import com.uca.parcialfinalncapas.service.UserService;
import com.uca.parcialfinalncapas.utils.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse findByUsername(String username) {
        if (userRepository.findByUsername(username) != null) {
            throw new UserNotFoundException("No existe un usuario con el correo: " + username);
        }

        return UserMapper.toDTO(userRepository.findByUsername(username));
    }

    @Override
    public UserResponse save(UserCreateRequest user) {

        if (userRepository.findByUsername(user.getCorreo()) != null) {
            throw new UserNotFoundException("Ya existe un usuario con el correo: " + user.getCorreo());
        }

        return UserMapper.toDTO(userRepository.save(UserMapper.toEntityCreate(user)));
    }

    @Override
    public UserResponse update(UserUpdateRequest user) {
        if (userRepository.findById(user.getId()).isEmpty()) {
            throw new UserNotFoundException("No se encontró un usuario con el ID: " + user.getId());
        }

        return UserMapper.toDTO(userRepository.save(UserMapper.toEntityUpdate(user)));
    }

    @Override
    public void delete(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("No se encontró un usuario con el ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponse> findAll() {
        return UserMapper.toDTOList(userRepository.findAll());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
