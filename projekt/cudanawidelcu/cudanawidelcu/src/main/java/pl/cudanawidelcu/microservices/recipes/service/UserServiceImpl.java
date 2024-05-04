package pl.cudanawidelcu.microservices.recipes.service;

import pl.cudanawidelcu.microservices.recipes.dto.RoleDto;
import pl.cudanawidelcu.microservices.recipes.dto.UserDto;
import pl.cudanawidelcu.microservices.recipes.model.Role;
import pl.cudanawidelcu.microservices.recipes.model.User;
import pl.cudanawidelcu.microservices.recipes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.cudanawidelcu.microservices.recipes.util.UserMapper;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordServiceImpl passwordService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordServiceImpl passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    @Transactional
    public void createUser(UserDto dto) {
        User user = new User();

        user.setLogin(dto.getLogin());
        user.setPassword(passwordService.hashPassword(dto.getPassword()));
        user.setName(dto.getName());

        Set<Role> roles = UserMapper.convertRoleDtoSetToRoleSet(dto.getRoles());
        user.setRoles(roles);
        user.setEnabled(dto.isEnabled());
        user.setEmail(dto.getEmail());

        userRepository.save(user);
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::convertUserToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<UserDto> findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDto userDto = UserMapper.convertUserToUserDto(user);
            return Optional.of(userDto);
        }
        return Optional.empty();
    }
}
