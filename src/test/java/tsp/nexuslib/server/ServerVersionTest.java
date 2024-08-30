package tsp.nexuslib.server;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ServerVersionTest {

    @Test
    void test() {
        MockBukkit.mock();
        Assertions.assertDoesNotThrow(ServerVersion::getVersion);
    }

}
