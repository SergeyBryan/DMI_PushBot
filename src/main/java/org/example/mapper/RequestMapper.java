package org.example.mapper;

import org.example.dto.RequestExcelDTO;
import org.example.entity.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * The RequestMapper interface is a component used for mapping Request objects to RequestExcelDTO objects.
 * Uses the MapStruct library for object mapping.
 */
@Mapper(componentModel = "spring")
public interface RequestMapper {
    /**
     * Retrieves the email from a Request object.
     *
     * @param request The Request object.
     * @return The email of the user in the Request, or null if the user is null.
     */
    @Named("getEmail")
    default String getEmail(Request request) {
        if (request.getUser() != null) {
            return request.getUser().getEmail();
        } else {
            return null;
        }
    }

    /**
     * Maps a Request object to a RequestExcelDTO object.
     *
     * @param request The Request object to map.
     * @return The mapped RequestExcelDTO object.
     */
    @Mapping(target = "email", expression = "java(getEmail(request))")
    RequestExcelDTO requestToRequestExcel(Request request);

    /**
     * Maps a list of Request objects to a list of RequestExcelDTO objects.
     *
     * @param list The list of Request objects to map.
     * @return The mapped list of RequestExcelDTO objects.
     */
    List<RequestExcelDTO> requestToRequestExcel(List<Request> list);

}
