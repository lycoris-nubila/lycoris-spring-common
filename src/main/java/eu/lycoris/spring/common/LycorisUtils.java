package eu.lycoris.spring.common;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class LycorisUtils {

  public static long generateLong(@NotNull UUID id) {
    final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
    buffer.putLong(id.getLeastSignificantBits());
    buffer.putLong(id.getMostSignificantBits());

    final BigInteger bi = new BigInteger(buffer.array());
    return Math.abs(bi.longValue());
  }
}
