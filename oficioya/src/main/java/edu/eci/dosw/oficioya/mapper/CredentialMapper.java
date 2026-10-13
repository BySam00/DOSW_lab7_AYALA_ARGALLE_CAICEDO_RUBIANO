package edu.eci.dosw.oficioya.mapper;

import edu.eci.dosw.oficioya.entity.Credential;
import edu.eci.dosw.oficioya.model.CredentialModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CredentialMapper {

    CredentialModel toModel(Credential entity);

    Credential toEntity(CredentialModel model);
}