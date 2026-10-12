package io.github.carlosolivserafim.user.api.service;

import io.github.carlosolivserafim.user.api.controller.BadRequestException;
import io.github.carlosolivserafim.user.api.controller.NotFoundException;
import io.github.carlosolivserafim.user.api.dto.UserRecordDto;
import io.github.carlosolivserafim.user.api.model.User;
import io.github.carlosolivserafim.user.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @CacheEvict(value = "users", allEntries = true)
    public User createUser(UserRecordDto usuarioDto){
        User user = new User();
        user.setName(usuarioDto.name());
        user.setCpf(usuarioDto.cpf());
        user.setEmail(usuarioDto.email());
        user.setPhoneNumber(usuarioDto.phoneNumber());
        user.setCreatedAt(LocalDateTime.now());

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Cacheable(value = "users")
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Cacheable(value = "users")
    public User findUserById(Long id){
        return userRepository.findById(id)
               .orElseThrow(() -> new NotFoundException("Usuário não encontrado para o ID: "+id));
    }

    @CacheEvict(value = "users", allEntries = true)
    public User updateUser(Long id, UserRecordDto usuarioDto) {
        User userExistente = findUserById(id);

        userExistente.setName(usuarioDto.name());
        userExistente.setEmail(usuarioDto.email());
        userExistente.setCpf(usuarioDto.cpf());
        userExistente.setPhoneNumber(usuarioDto.phoneNumber());
        userExistente.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(userExistente);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long id) {
        User userExistente = findUserById(id);

        userRepository.delete(userExistente);
    }
}