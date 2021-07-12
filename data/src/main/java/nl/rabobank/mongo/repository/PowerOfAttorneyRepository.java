package nl.rabobank.mongo.repository;

import nl.rabobank.authorizations.PowerOfAttorney;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PowerOfAttorneyRepository extends MongoRepository<PowerOfAttorney, String> {

    List<PowerOfAttorney> findAllByGranteeName(String granteeName);
}
