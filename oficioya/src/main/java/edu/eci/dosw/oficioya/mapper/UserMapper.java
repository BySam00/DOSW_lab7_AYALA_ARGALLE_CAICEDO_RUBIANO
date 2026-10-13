package edu.eci.dosw.oficioya.mapper;

import edu.eci.dosw.oficioya.entity.User;
import edu.eci.dosw.oficioya.model.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserModel toModel(User entity);

    User toEntity(UserModel model);
}