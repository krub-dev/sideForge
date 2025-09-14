package com.sideforge.service.impl;

import com.sideforge.dto.user.UserResponseDTO;
import com.sideforge.dto.user.UserUpdateDTO;
import com.sideforge.enums.Role;
import com.sideforge.exception.ResourceNotFoundException;
import com.sideforge.model.Admin;
import com.sideforge.model.User;
import com.sideforge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserById_found() {
        Admin user = Admin.builder()
                .id(1L)
                .username("test")
                .email("test@mail.com")
                .passwordHash("pass")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO dto = userService.getUserById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("test", dto.getUsername());
        assertEquals("test@mail.com", dto.getEmail());
        assertEquals(Role.ADMIN, dto.getRole());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(2L));
    }

    @Test
    void getAllUsers_success() {
        List<User> users = Arrays.asList(
                Admin.builder().id(1L).username("a").email("a@mail.com").passwordHash("p1").role(Role.ADMIN).build(),
                Admin.builder().id(2L).username("b").email("b@mail.com").passwordHash("p2").role(Role.ADMIN).build()
        );
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("a", result.get(0).getUsername());
        assertEquals("b", result.get(1).getUsername());
    }

    @Test
    void updateUser_success() {
        UserUpdateDTO dto = UserUpdateDTO.builder()
                .username("updated")
                .email("updated@mail.com")
                .password("newpass")
                .role(Role.ADMIN)
                .build();

        Admin user = Admin.builder()
                .id(1L)
                .username("old")
                .email("old@mail.com")
                .passwordHash("oldpass")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.updateUser(1L, dto);

        assertEquals("updated", response.getUsername());
        assertEquals("updated@mail.com", response.getEmail());
        assertEquals(Role.ADMIN, response.getRole());
    }

    @Test
    void updateUser_notFound() {
        UserUpdateDTO dto = UserUpdateDTO.builder().build();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, dto));
    }

    @Test
    void deleteUser_success() {
        Admin user = Admin.builder().id(1L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void getUsersPage_success() {
        Admin user = Admin.builder().id(1L).username("pageuser").email("p@mail.com").passwordHash("p").role(Role.ADMIN).build();
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<UserResponseDTO> result = userService.getUsersPage(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("pageuser", result.getContent().get(0).getUsername());
    }

    @Test
    void getUsersPageByRole_success() {
        Admin user = Admin.builder().id(1L).username("roleuser").email("r@mail.com").passwordHash("p").role(Role.ADMIN).build();
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAllByRole(eq(Role.ADMIN), any(Pageable.class))).thenReturn(page);

        Page<UserResponseDTO> result = userService.getUsersPageByRole("ADMIN", PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("roleuser", result.getContent().get(0).getUsername());
    }
}