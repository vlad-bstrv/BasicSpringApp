package ru.vladbstrv.managerapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vladbstrv.managerapp.entity.Authority;
import ru.vladbstrv.managerapp.repository.SelmagUserRepository;

@Service
@RequiredArgsConstructor
public class SelmagUserDetailsService implements UserDetailsService {

    private final SelmagUserRepository userRepository;

    /**
     * тк права пользователя получаются лениво, то можно в User указать @ManyToMany(fetch = FetchType.EAGER)
     * А можно проще здесь указать @Transactional(readOnly = true), тогда authorities будет подгружаться
     * в рамках одной транзакции и не будет выбрасываться исключение
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getAuthorities().stream()
                                .map(Authority::getAuthority)
                                .map(SimpleGrantedAuthority::new)
                                .toList())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
