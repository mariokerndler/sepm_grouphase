package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;

public record ApplicationUserDto(String userName, String name, String surname, String email, String address,
                                 String password, Boolean admin, UserRole userRole) {
}
