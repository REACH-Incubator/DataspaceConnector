package de.fraunhofer.isst.dataspaceconnector.controller.v2;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import de.fraunhofer.isst.dataspaceconnector.services.resources.v2.backend.BaseUniDirectionalLinkerService;
import de.fraunhofer.isst.dataspaceconnector.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Offers REST-Api Endpoints for modifying relations between resources.
 *
 * @param <T> The service type for handling the relations logic.
 */
public class BaseResourceChildController<T extends BaseUniDirectionalLinkerService<?, ?, ?, ?>> {
    /**
     * The linker between two resources.
     **/
    @Autowired
    private T linker;

    /**
     * Default constructor.
     */
    protected BaseResourceChildController() {
        // This constructor is intentionally empty. Nothing to do here.
    }

    /**
     * Get all resources of the same type linked to the passed resource.
     * Endpoint for GET requests.
     *
     * @param ownerId The id of the owning resource.
     * @return The children of the resource.
     */
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<?> getResource(
            @Valid @PathVariable(name = "id") final UUID ownerId,
            @RequestParam(required = false, defaultValue = "0") final Integer page,
            @RequestParam(required = false, defaultValue = "30") final Integer size,
            @RequestParam(required = false) final Sort sort) {
        final var pageable = PageRequest.of(page == null ? 1 : page, size == null ? 30 : size);
        final var entities = linker.get(ownerId, pageable);
//        PagedModel<V> model;
//        if (entities.hasContent()) {
//            model = pagedResourcesAssembler.toModel(entities, assembler);
//        } else {
//            model = (PagedModel<V>) pagedResourcesAssembler.toEmptyModel(entities, resourceType);
//        }

        return ResponseEntity.ok(entities);
    }

    /**
     * Add resources as children to a resource. Endpoint for POST requests.
     *
     * @param ownerId   The owning resource.
     * @param resources The children to be added.
     * @return Response with code 200 (Ok) and the new children's list.
     */
    @PostMapping
    public HttpEntity<?> addResources(@Valid @PathVariable(name = "id") final UUID ownerId,
            @Valid @RequestBody final List<URI> resources) {
        linker.add(ownerId, toSet(resources));
        // Send back the list of children after modification.
        // See https://tools.ietf.org/html/rfc7231#section-4.3.3 and
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/200
        return this.getResource(ownerId, null, null, null);
    }

    /**
     * Replace the children of a resource. Endpoint for PUT requests.
     *
     * @param ownerId   The id of the resource which children should be
     *                  replaced.
     * @param resources The resources that should be added as children.
     * @return Response with code 204 (No_Content).
     */
    @PutMapping
    public HttpEntity<Void> replaceResources(@Valid @PathVariable(name = "id") final UUID ownerId,
            @Valid @RequestBody final List<URI> resources) {
        linker.replace(ownerId, toSet(resources));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Remove a list of children of a resource. Endpoint for DELETE requests.
     *
     * @param ownerId   The id of the resource which children should be removed.
     * @param resources The list of children to be removed.
     * @return Response with code 204 (No_Content).
     */
    @DeleteMapping
    public HttpEntity<Void> removeResources(@Valid @PathVariable(name = "id") final UUID ownerId,
            @Valid @RequestBody final List<URI> resources) {
        linker.remove(ownerId, toSet(resources));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private static Set<UUID> toSet(final List<URI> uris) {
        return uris.parallelStream().map(UUIDUtils::uuidFromUri).collect(Collectors.toSet());
    }
}
