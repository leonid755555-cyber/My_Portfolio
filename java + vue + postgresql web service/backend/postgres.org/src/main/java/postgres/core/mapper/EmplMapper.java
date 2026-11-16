package postgres.core.mapper;

import postgres.core.dto.EmplDTO;
import postgres.core.mapper.PostsMapper;
import postgres.core.services.PostsService;
import postgres.core.entities.Empl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PostsService.class)
public abstract class EmplMapper implements BaseMapper<Empl, EmplDTO> {
    @Override
    @Mapping(source = "post.id", target = "postId")
    public abstract EmplDTO entityToDto(Empl empl);
    @Override
    @Mapping(source = "postId", target = "post")
    public abstract Empl dtoToEntity(EmplDTO emplDTO);
}
