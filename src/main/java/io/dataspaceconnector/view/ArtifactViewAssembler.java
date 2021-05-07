package io.dataspaceconnector.view;

import java.util.UUID;

import io.dataspaceconnector.controller.resources.RelationControllers;
import io.dataspaceconnector.controller.resources.ResourceControllers.ArtifactController;
import io.dataspaceconnector.model.Artifact;
import io.dataspaceconnector.model.QueryInput;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembles the REST resource for an artifact.
 */
@Component
@NoArgsConstructor
public class ArtifactViewAssembler
        implements RepresentationModelAssembler<Artifact, ArtifactView>, SelfLinking {
    /**
     * Construct the ArtifactView from an Artifact.
     *
     * @param artifact The artifact.
     * @return The new view.
     */
    @Override
    public ArtifactView toModel(final Artifact artifact) {
        final var modelMapper = new ModelMapper();
        final var view = modelMapper.map(artifact, ArtifactView.class);
        view.add(getSelfLink(artifact.getId()));

        final var dataLink = linkTo(methodOn(ArtifactController.class)
                .getData(artifact.getId(), new QueryInput()))
                .withRel("data");
        view.add(dataLink);

        final var repLink =
                WebMvcLinkBuilder.linkTo(
                        methodOn(RelationControllers.ArtifactsToRepresentations.class)
                                .getResource(artifact.getId(), null, null, null))
                                 .withRel("representations");
        view.add(repLink);

        final var agreementLink =
                linkTo(methodOn(RelationControllers.ArtifactsToAgreements.class)
                        .getResource(artifact.getId(), null, null, null))
                        .withRel("agreements");
        view.add(agreementLink);

        return view;
    }

    @Override
    public final Link getSelfLink(final UUID entityId) {
        return ViewAssemblerHelper.getSelfLink(entityId, ArtifactController.class);
    }
}
