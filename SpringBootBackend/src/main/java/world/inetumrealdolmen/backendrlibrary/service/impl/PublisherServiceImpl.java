package world.inetumrealdolmen.backendrlibrary.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.inetumrealdolmen.backendrlibrary.domain.Publisher;
import world.inetumrealdolmen.backendrlibrary.domain.Subject;
import world.inetumrealdolmen.backendrlibrary.domain.dao.PublisherDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.PublisherDto;
import world.inetumrealdolmen.backendrlibrary.exception.AlreadyExistsException;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.PublisherRepository;
import world.inetumrealdolmen.backendrlibrary.service.PublisherService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;
    @Override
    public List<PublisherDto> getAllPublishers() {
        List<Publisher> publishers = publisherRepository.findAll();
        if(publishers.isEmpty()){
            throw new NotFoundException("No publishers are registered in the application");
        }
        return createPublisherDtos(publishers);
    }

    @Override
    public PublisherDto createPublisher(PublisherDao publisherDao) {
        Publisher newPublisher;
        if(publisherDao.getId() == 0) {
            newPublisher = Publisher.builder()
                    .name(publisherDao.getName())
                    .build();

            publisherRepository.save(newPublisher);
            return createPublisherDto((newPublisher));

        } else{
            throw new AlreadyExistsException(String.format("Publisher with id %s already exists", publisherDao.getId()));
        }    }

    private List<PublisherDto> createPublisherDtos(List<Publisher> publishers) {
        List<PublisherDto> publisherDtos = new ArrayList<>();
        for(var publisher:publishers){
            publisherDtos.add(createPublisherDto(publisher));
        }
        return publisherDtos;
    }

    private PublisherDto createPublisherDto(Publisher publisher) {
        return PublisherDto.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .build();
    }

}
