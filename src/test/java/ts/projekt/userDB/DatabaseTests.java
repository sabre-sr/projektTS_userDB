package ts.projekt.userDB;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class DatabaseTests {
    ImmutablePair<String, byte[]> hashPair;
    @Before
    public void setUp() {
        String testSalt = "e04fd020000000000000000000000000";
        hashPair = new ImmutablePair<>("test", hexStringToByteArray(testSalt));
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
