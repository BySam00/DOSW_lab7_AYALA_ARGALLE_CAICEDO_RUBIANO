package edu.eci.dosw.oficioya.mapper;

import edu.eci.dosw.oficioya.entity.Worker;
import edu.eci.dosw.oficioya.model.WorkerModel;
import org.mapstruct.Mapper;

// uses = UserMapper: para convertir el campo "user" anidado reutiliza UserMapper
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface WorkerMapper {

    WorkerModel toModel(Worker entity);

    Worker toEntity(WorkerModel model);
}
