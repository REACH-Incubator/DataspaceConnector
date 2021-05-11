package io.dataspaceconnector.model.messages;

import java.net.URI;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionResponseMessageDescTest {
    @Test
    void defaultConstructor_nothing_emptyDesc() {
        /* ARRANGE */
        // Nothing to arrange here.

        /* ACT */
        final var result = new DescriptionResponseMessageDesc();

        /* ASSERT */
        assertNull(result.getCorrelationMessage());
        assertNull(result.getRecipient());
    }

    @Test
    void allArgsConstructor_valid_validDesc() {
        /* ARRANGE */
        final var recipient = URI.create("someRecipient");
        final var message = URI.create("someMessage");

        /* ACT */
        final var result = new DescriptionResponseMessageDesc(recipient, message);

        /* ASSERT */
        assertEquals(recipient, result.getRecipient());
        assertEquals(message, result.getCorrelationMessage());
    }

    @Test
    void equals_everything_valid() {
        EqualsVerifier.simple().forClass(DescriptionResponseMessageDesc.class).verify();
    }
}
