package de.fraunhofer.isst.dataspaceconnector.repositories;

import de.fraunhofer.isst.dataspaceconnector.model.Artifact;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * The repository containing all objects of type {@link Artifact}.
 */
public interface ArtifactRepository extends BaseEntityRepository<Artifact> {

    /**
     * Finds all artifacts of a specific resource.
     *
     * @param resourceId ID of the resource
     * @return list of all artifacts of the resource
     */
    @Query("SELECT a FROM Artifact a, Representation r, OfferedResource o WHERE o.id = :resourceId "
            + "AND r MEMBER OF o.representations AND a MEMBER OF r.artifacts")
    List<Artifact> findAllByResourceId(UUID resourceId);

    /**
     * Finds all artifacts referenced in a specific agreement.
     *
     * @param agreementId ID of the agreement
     * @return list of all artifacts referenced in the agreement
     */
    @Query("SELECT a FROM Artifact a INNER JOIN Agreement ag ON a MEMBER OF ag.artifacts "
            + "WHERE ag.id = :agreementId")
    List<Artifact> findAllByAgreement(UUID agreementId);

}
