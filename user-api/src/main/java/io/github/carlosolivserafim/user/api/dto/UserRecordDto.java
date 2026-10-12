package io.github.carlosolivserafim.user.api.dto;

public record UserRecordDto(String name,
                            String cpf,
                            String email,
                            String phoneNumber) {
}