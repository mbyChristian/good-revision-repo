package org.study.subjectresource.service;


import org.springframework.stereotype.Service;
import org.study.subjectresource.dto.UsersDto;



import java.util.List;

@Service
public interface UsersService {
    UsersDto save(UsersDto usersDto);
    UsersDto findById(Long id);
    List<UsersDto> findAll();
    UsersDto update(Long id,UsersDto usersDto);
    void delete(Long id);
    UsersDto register(UsersDto usersDto);
    Long getTotalUsers();
     void toAdmin(Long userId);
    boolean isEmailAvailable(String email);
    boolean isUsernameAvailable(String email);

}
