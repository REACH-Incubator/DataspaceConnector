/*
 * Copyright 2020 Fraunhofer Institute for Software and Systems Engineering
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.dataspaceconnector.repository;

import io.dataspaceconnector.model.appstore.AppStore;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.UUID;

/**
 * The repository containing all objects of type {@link AppStore}.
 */
@Repository
public interface AppStoreRepository extends BaseEntityRepository<AppStore> {

    /**
     * Get all related appStores for an app id.
     *
     * @param id App id for which relative app stores should be found.
     * @return The found app store.
     */
    @Query("SELECT r "
            + "FROM AppStore r "
            + "left join fetch r.apps a "
            + "WHERE a.id = :id "
            + "AND r.deleted = false")
    AppStore findAppStoreWithAppId(UUID id);


    /**
     * Get app store by location address.
     * @param location The location uri.
     * @return The found app store.
     */
    @Query("SELECT r "
            + "FROM AppStore r "
            + "WHERE r.location = :location "
            + "AND r.deleted = false ")
    AppStore findAppStoreWithLocation(URI location);
}
