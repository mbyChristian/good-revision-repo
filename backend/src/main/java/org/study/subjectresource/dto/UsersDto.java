package org.study.subjectresource.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.study.subjectresource.entity.Users;
import org.study.subjectresource.validators.PasswordMatches;


@PasswordMatches
@Builder
public record UsersDto(
        Long id,
        @NotNull(message = "Le prénom ne doit pas être null")
        @NotEmpty(message = "Le prénom ne doit pas être vide")
        @NotBlank(message = "Le prénom ne doit pas être vide")
        @Size(min = 2, message = "Le prénom doit avoir 2 caractères min")
        String firstName,

        @NotNull(message = "Le nom ne doit pas être null")
        @NotEmpty(message = "Le nom ne doit pas être vide")
        @NotBlank(message = "Le nom ne doit pas être vide")
        @Size(min = 2, message = "Le nom doit avoir 2 caractères min")
        String lastName,

        @NotNull(message = "Le nom d'utilisateur ne doit pas être null")
        @NotEmpty(message = "Le nom d'utilisateur ne doit pas être vide")
        @NotBlank(message = "Le nom d'utilisateur ne doit pas être vide")
        @Size(min = 2, message = "Le nom d'utilisateur doit avoir 2 caractères min")
        String username,

        @NotNull(message = "L'email ne doit pas être null")
        @NotEmpty(message = "L'email ne doit pas être vide")
        @NotBlank(message = "L'email ne doit pas être vide")
        @Email(message = "L'email n'est pas conforme")
        String email,

        @NotNull(message = "Le mot de passe ne doit pas être null")
        @NotEmpty(message = "Le mot de passe ne doit pas être vide")
        @NotBlank(message = "Le mot de passe ne doit pas être vide")
        @Size(min = 8, max = 20, message = "Le mot de passe doit avoir entre 8 et 20 caractères")
        String password,

        @NotNull(message = "La confirmation du mot de passe ne doit pas être null")
        @NotEmpty(message = "La confirmation du mot de passe ne doit pas être vide")
        @NotBlank(message = "La confirmation du mot de passe ne doit pas être vide")
        String confirmPassword,

        String role
) {
    public static UsersDto toDto(Users user) {
        return new UsersDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                null,
                user.getRole().getRoleName()
        );
    }

    public static Users toEntity(UsersDto dto) {
        return Users.builder()
                .firstName(dto.firstName)
                .lastName(dto.lastName)
                .username(dto.username)
                .email(dto.email)
                .password(dto.password)
                .build();
    }
}
