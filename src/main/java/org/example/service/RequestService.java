package org.example.service;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import org.example.dto.RequestExcelDTO;
import org.example.entity.Request;
import org.example.mapper.RequestMapperImpl;
import org.example.repository.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
/**
 * A service class that performs operations related to Requests.
 */
@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapperImpl requestMapper;


    public RequestService(RequestRepository requestRepository, RequestMapperImpl requestMapper) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
    }
    /**
     * Creates a new Request by saving it to the repository.
     *
     * @param request The Request to be created.
     */
    public void create(Request request) {
        requestRepository.save(request);
    }
    /**
     * Retrieves a list of all Requests.
     *
     * @return A list of Request objects.
     */
    public List<Request> requestList() {
        return requestRepository.findAll();
    }

    /**
     * Converts the list of Requests to a file in Excel format.
     *
     * @return The generated Excel file containing the Request data.
     * @throws IOException If an error occurs while creating the file.
     */
    public File requestsToExcel() throws IOException {
        List<Request> requests = requestList();
        List<RequestExcelDTO> requestExcels = requestMapper.requestToRequestExcel(requests);
        return createFile(requestExcels);
    }
    /**
     * Creates a file in Excel format using the provided list of RequestExcelDTOs.
     *
     * @param list The list of RequestExcelDTOs.
     * @return The generated Excel file.
     * @throws IOException If an error occurs while creating the file.
     */
    public File createFile(List<RequestExcelDTO> list) throws IOException {
        File file = new File("request.xlsx");
        Xcelite xcelite = new Xcelite();
        try {
            file.createNewFile();
            XceliteSheet xceliteSheet = xcelite.createSheet("requests");
            SheetWriter<RequestExcelDTO> writer = xceliteSheet.getBeanWriter(RequestExcelDTO.class);
            writer.write(list);
            xcelite.write(file);
            return file;
        } catch (IOException e) {
            throw new IOException("Failed to create file");
        }
    }
    /**
     * Deletes Requests that are older than one week.
     */
    @Transactional
    public void deleteRequestsByWeek() {
        LocalDateTime localDateTime = LocalDateTime.now().minusWeeks(1);
        requestRepository.deleteRequestsByWeek(localDateTime);
    }

}